/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.AppraisalSummaryReport;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Sep-2009 15:02:35
 */
public class AppraisalReportMultiController extends ZynapMultiActionController  {

    public ModelAndView listAppraisalReports(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final List<Report> reports = reportService.findAll(Report.APPRAISAL_REPORT);
        Map myModel = new HashMap();
        myModel.put("reports", reports);
        return new ModelAndView("listappraisalreports", ControllerConstants.MODEL_NAME, myModel);
    }

    public ModelAndView viewAppraisalReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Long reportId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.REPORT_ID);
        Report report = (Report) reportService.findById(reportId);
        Map model = new HashMap();
        model.put("report", report);
        return new ModelAndView("viewappraisalreport", ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView deleteAppraisalReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Long reportId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.REPORT_ID);
        Report report = (Report) reportService.findById(reportId);
        reportService.delete(report);
        return new ModelAndView(new ZynapRedirectView("listappraisalreports.htm"));
    }


    public ModelAndView publishAppraisalReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeStatus(request, AppraisalSummaryReport.STATUS_PUBLISHED);
    }

    public ModelAndView archiveAppraisalReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeStatus(request, AppraisalSummaryReport.STATUS_ARCHIVED);
    }

    public ModelAndView reopenAppraisalReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeStatus(request, AppraisalSummaryReport.STATUS_NEW);
    }

    private ModelAndView executeStatus(HttpServletRequest request, String status) throws Exception {
        final Long reportId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.REPORT_ID);
        AppraisalSummaryReport report = (AppraisalSummaryReport) reportService.findById(reportId);
        report.setStatus(status);
        reportService.update(report);
        return new ModelAndView(new ZynapRedirectView("listappraisalreports.htm"));
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    private IReportService reportService;
}
