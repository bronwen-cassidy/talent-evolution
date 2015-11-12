/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;

import com.zynap.common.util.StringUtil;
import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.web.analysis.populations.PopulationUtils;
import com.zynap.talentstudio.web.analysis.reports.jasper.JasperCsvExporter;
import com.zynap.talentstudio.web.analysis.reports.support.ReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ResponseUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ReportExportController extends ZynapDefaultFormController implements ReportConstants {

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        RunReportWrapperBean wrapper = (RunReportWrapperBean) command;
        Report originalReport = wrapper.getReport();
        // need to copy the report and remove any color displayable information for csv (cannot export images)
        TabularReport csvReport = new TabularReport(originalReport.getId(), originalReport.getLabel(), originalReport.getDescription(), originalReport.getAccessType());
        csvReport.setDefaultPopulation(originalReport.getDefaultPopulation());
        csvReport.setReportType(originalReport.getReportType());
        csvReport.setLastLineItem(originalReport.isLastLineItem());
        List<Column> columns = originalReport.getColumns();

        for (Column column : columns) {
            Column cloned = (Column) column.clone();
            cloned.setColorDisplayable(false);
            csvReport.addColumn(cloned);
        }

        reportService.compileReportDesign(csvReport);
        wrapper.setReport(csvReport);
        wrapper.setCvsExport(true);
        String personalDrillDown = request.getParameter(ParameterConstants.PERSONAL_DRILL_DOWN);
        Long userId = ZynapWebUtils.getUserId(request);
        if(personalDrillDown != null) {
            userId = IDomainObject.ROOT_USER_ID;
        }
        reportRunner.run(wrapper, userId);

        JRCsvExporter exporter = new JasperCsvExporter();
        StringBuffer output = new StringBuffer();

        exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, wrapper.getDelimiter());
        exporter.setParameter(JRCsvExporterParameter.RECORD_DELIMITER, StringUtil.LINE_SEPARATOR_WINDOWS);
        exporter.setParameter(JRCsvExporterParameter.OUTPUT_STRING_BUFFER, output);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, wrapper.getFilledReport().getJasperPrint());

        exporter.exportReport();

        final String fileExtension = wrapper.getDelimiter().equals(ReportConstants.COMMA) ? CSV_FILE_EXT : TXT_FILE_EXT;
        final String label = wrapper.getReport().getLabel();

        final String fileName = label + fileExtension;

        String outputString = output.toString();
        // post process the results        
        byte[] outputBytes = outputString.getBytes();

        if (outputString.length() <= 0) outputBytes = EMPTY_RESULTS.getBytes();
        wrapper.setCvsExport(false);
        ResponseUtils.writeToResponse(response, request, fileName, outputBytes, true);

        return null;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long reportId = RequestUtils.getRequiredLongParameter(request, EXPORTED_REPORT_ID);
        String populationIdString = RequestUtils.getStringParameter(request, ParameterConstants.POPULATION_ID, null);
        String originalPopulationId = RequestUtils.getStringParameter(request, ORIGINAL_POPULATION_ID, null);

        Long[] populationIds = StringUtil.convertToLongArray(StringUtils.commaDelimitedListToStringArray(populationIdString));

        Population population = PopulationUtils.buildPopulation(populationIds, populationIds[0], analysisService);
        // this is the case if we have come from a cross tab
        if (originalPopulationId != null) {
            // build a population
            String chartType = RequestUtils.getStringParameter(request, CHART_REPORT_PARAM, null);
            if (chartType != null) {
                population = PopulationUtils.buildChartDrillDown(request, populationEngine, population);
            } else {
                PopulationUtils.addColumnPopulationCriteria(population, request);
            }
        }

        Report report = (Report) reportService.findById(reportId);
        report.setDefaultPopulation(population);
        RunReportWrapperBean runReportWrapperBean = new RunReportWrapperBean(report);
        runReportWrapperBean.setPopulationIds(populationIds);

        int sortOrder = RequestUtils.getIntParameter(request, SORT_ORDER, -10);
        if (sortOrder != -10) {
            runReportWrapperBean.setSortOrder(sortOrder);
        }

        String orderBy = RequestUtils.getStringParameter(request, ORDER_BY, null);
        if (orderBy != null) {
            runReportWrapperBean.setOrderBy(orderBy);
        }
        return runReportWrapperBean;
    }

    public void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        RunReportWrapperBean runner = (RunReportWrapperBean) command;
        final String delimiter = runner.getDelimiter();
        if (delimiter == null || delimiter.length() == 0) {
            errors.rejectValue("delimiter", "error.delimiter.required", "The delimiter is a required field");
        }
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setReportRunner(ReportRunner reportRunner) {
        this.reportRunner = reportRunner;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    private IReportService reportService;
    private IAnalysisService analysisService;
    private ReportRunner reportRunner;
    private IPopulationEngine populationEngine;

    private static final String CSV_FILE_EXT = ".csv";
    private static final String TXT_FILE_EXT = ".txt";
}
