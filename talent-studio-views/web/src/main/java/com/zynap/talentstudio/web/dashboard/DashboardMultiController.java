/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.dashboard.DashboardItem;
import com.zynap.talentstudio.dashboard.IDashboardService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.util.StringUtils;
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
 * @since 17-May-2010 19:25:37
 */
public class DashboardMultiController extends ZynapMultiActionController {

    public ModelAndView listDashboards(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Dashboard> dashboards = dashboardService.findAll();
        Map<String, Object> myModel = new HashMap<String, Object>();
        myModel.put(DASHBOARDS, dashboards);
        return new ModelAndView("listdashboards", ControllerConstants.MODEL_NAME, myModel);
    }

    public ModelAndView deleteDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long dashboardId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.DASHBOARD_ID);
        dashboardService.delete(dashboardId);
        return new ModelAndView(new ZynapRedirectView("listdashboards.htm"));
    }

    public ModelAndView viewDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long dashboardId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.DASHBOARD_ID);
        Dashboard dashboard = (Dashboard) dashboardService.findById(dashboardId);
        Map<String, Object> myModel = new HashMap<String, Object>();
        myModel.put(DASHBOARD, dashboard);
        return new ModelAndView("viewdashboard", ControllerConstants.MODEL_NAME, myModel);
    }

    /**
     * Method is called from JQuery! used to build the dashboard item that has been selected
     * 
     * @param request
     * @param response
     * @return ModelAndView containing the jsp snippet to be included
     * @throws Exception
     */
    public ModelAndView viewDashboardItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String elementId = request.getParameter("id");
        String index = request.getParameter("index");
        // id looks like aaa_123
        if (elementId == null) return null;
        final String[] values = StringUtils.split(elementId, "_");
        if(values == null) return null;
        Long id = new Long(values[1]);
        Report report = (Report) reportService.findById(id);
        report.getColumns().size();
        DashboardItem item = new DashboardItem();
        item.setReport(report);
        DashboardItemWrapper wrapper = new DashboardItemWrapper(item);
        wrapper.setIndex(Integer.parseInt(index));
        // go an get the report
        Map<String, Object> myModel = new HashMap<String, Object>();
        myModel.put("report", report);
        myModel.put("item", wrapper);
        return new ModelAndView("dashboarditem", ControllerConstants.MODEL_NAME, myModel);
    }

    public void setDashboardService(IDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    private IDashboardService dashboardService;
    private IReportService reportService;

    public static final String DASHBOARDS = "dashboards";
    public static final String DASHBOARD = "dashboard";
}
