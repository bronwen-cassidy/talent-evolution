/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

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
import com.zynap.talentstudio.web.analysis.reports.jasper.JasperPdfExporter;
import com.zynap.talentstudio.web.analysis.reports.support.ReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ResponseUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 20-Oct-2008 11:11:10
 */
public class ReportPdfExportController implements Controller, ReportConstants {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long reportId = RequestUtils.getRequiredLongParameter(request, EXPORTED_REPORT_ID);
        String populationIdString = RequestUtils.getStringParameter(request, ParameterConstants.POPULATION_ID, null);
        String originalPopulationId = RequestUtils.getStringParameter(request, ORIGINAL_POPULATION_ID, null);

        Long[] populationIds = StringUtil.convertToLongArray(StringUtils.commaDelimitedListToStringArray(populationIdString));
        Population population = PopulationUtils.buildPopulation(populationIds, populationIds[0], analysisService);

        if (originalPopulationId != null) {
            // build a population
            String chartType = RequestUtils.getStringParameter(request, CHART_REPORT_PARAM, null);
            if (chartType != null) {
                population = PopulationUtils.buildChartDrillDown(request, populationEngine, population);
            } else {
                PopulationUtils.addColumnPopulationCriteria(population, request);
            }
        }

        Report originalReport = (Report) reportService.findById(reportId);
        originalReport.setDefaultPopulation(population);

        RunReportWrapperBean wrapper = new RunReportWrapperBean(originalReport);

        Report pdfReport = new TabularReport(originalReport.getId(), originalReport.getLabel(), originalReport.getDescription(), originalReport.getAccessType());
        pdfReport.setDefaultPopulation(originalReport.getDefaultPopulation());
        pdfReport.setReportType(originalReport.getReportType());
        List<Column> columns = originalReport.getColumns();

        for (Column column : columns) {
            Column cloned = (Column) column.clone();
            cloned.setColorDisplayable(false);
            pdfReport.addColumn(cloned);
        }

        reportService.compileReportDesign(pdfReport);
        wrapper.setReport(pdfReport);
        wrapper.setCvsExport(true);

        String personalDrillDown = request.getParameter(ParameterConstants.PERSONAL_DRILL_DOWN);
        Long userId = ZynapWebUtils.getUserId(request);
        if(personalDrillDown != null) {
            userId = IDomainObject.ROOT_USER_ID;
        }
        reportRunner.run(wrapper, userId);

        JasperPdfExporter jpdfExport =new JasperPdfExporter(population.getLabel(), "population", wrapper, messageSource, request);
        byte[] pdfByteArray=jpdfExport.runPdfExport();

        final String fileExtension = PDF_FILE_EXT;
        final String label = wrapper.getReport().getLabel();

        final String fileName = label + fileExtension;
        this.bytes = pdfByteArray;

        if(pdfByteArray.length < 1) {
            return null;
        }

        ResponseUtils.writeToResponse(response, request, fileName, pdfByteArray, true);

        return null;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setReportRunner(ReportRunner reportRunner) {
        this.reportRunner = reportRunner;
    }

    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    protected IAnalysisService analysisService;
    protected IReportService reportService;
    ReportRunner reportRunner;
    private IPopulationEngine populationEngine;

    private static final String PDF_FILE_EXT = ".pdf";
    public byte[] bytes;
    org.springframework.context.support.ReloadableResourceBundleMessageSource messageSource;
}
