/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Sep-2009 15:02:35
 */
public class ProgressReportMultiController extends ZynapMultiActionController  {

    public ModelAndView listProgressReports(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final List<Report> reports = reportService.findAll(Report.PROGRESS_REPORT);
        Map myModel = new HashMap();
        myModel.put("reports", reports);
        return new ModelAndView("listprogressreports", ControllerConstants.MODEL_NAME, myModel);
    }

    public ModelAndView viewProgressReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Long reportId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.REPORT_ID);
        Report report = (Report) reportService.findById(reportId);
        Map model = new HashMap();
        model.put("report", report);
        return new ModelAndView("runviewprogressreport", ControllerConstants.MODEL_NAME, model);
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    private IReportService reportService;
}