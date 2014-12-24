/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.analysis.reports.jasper.JasperPdfExporter;
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
public class ProgressReportPdfExportController extends ReportPdfExportController {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long reportId = RequestUtils.getRequiredLongParameter(request, EXPORTED_REPORT_ID);
        Long nodeId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);

        Subject subject = subjectService.findById(nodeId);

        Report originalReport = (Report) reportService.findById(reportId);
        RunProgressReportWrapper wrapper = new RunProgressReportWrapper(originalReport, subject);

        wrapper.setCvsExport(true);
        // permissions always as root. If you can see the tab and the report you can see all the answers
        //Long userId = ZynapWebUtils.getUserId(request);
        reportRunner.run(wrapper, IDomainObject.ROOT_USER_ID);

        JasperPdfExporter jpdfExport =new JasperPdfExporter(subject.getLabel(), "full.name", wrapper, messageSource, request);
        byte[] pdfByteArray=jpdfExport.runPdfExport();

        final String fileExtension = PDF_FILE_EXT;
        final String label = wrapper.getReport().getLabel();

        final String fileName = label + fileExtension;

        if (pdfByteArray.length > 0) {
            ResponseUtils.writeToResponse(response, request, fileName, pdfByteArray, true);
        }

        return null;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    ISubjectService subjectService;
    private static final String PDF_FILE_EXT = ".pdf";
}