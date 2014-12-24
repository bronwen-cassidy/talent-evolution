/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;

import com.zynap.common.util.StringUtil;
import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.analysis.reports.jasper.JasperCsvExporter;
import com.zynap.talentstudio.web.analysis.reports.views.RunProgressReportWrapper;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ResponseUtils;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 20-Oct-2008 11:11:10
 */
public class ProgressReportCsvExportController extends ProgressReportPdfExportController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long reportId = RequestUtils.getRequiredLongParameter(request, EXPORTED_REPORT_ID);
        Long nodeId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);

        Subject subject = subjectService.findById(nodeId);

        Report originalReport = (Report) reportService.findById(reportId);
        RunProgressReportWrapper wrapper = new RunProgressReportWrapper(originalReport, subject);

        //Long userId = ZynapWebUtils.getUserId(request);
        // always show as though root??
        reportRunner.run(wrapper, IDomainObject.ROOT_USER_ID);

        JRCsvExporter exporter = new JasperCsvExporter();
        StringBuffer output = new StringBuffer();

        exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, wrapper.getDelimiter());
        exporter.setParameter(JRCsvExporterParameter.RECORD_DELIMITER, StringUtil.LINE_SEPARATOR_WINDOWS);
        exporter.setParameter(JRCsvExporterParameter.OUTPUT_STRING_BUFFER, output);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, wrapper.getFilledReport().getJasperPrint());

        exporter.exportReport();

        final String fileExtension = ".csv";
        final String label = originalReport.getLabel();
        final String fileName = label + fileExtension;

        String outputString = output.toString();
        // post process the results
        byte[] outputBytes = outputString.getBytes();

        if (outputString.length() <= 0) outputBytes = EMPTY_RESULTS.getBytes();

        ResponseUtils.writeToResponse(response, request, fileName, outputBytes, true);
        return null;
    }
}