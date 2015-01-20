/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.domain.QueryParameter;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportDto;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.dashboard.DashboardItem;
import com.zynap.talentstudio.dashboard.IDashboardService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-May-2010 19:23:04
 */
public class DashboardBuilderController extends ZynapDefaultFormController {

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long dashboardId = RequestUtils.getLongParameter(request, ParameterConstants.DASHBOARD_ID);
        Dashboard dashboard;
        if(dashboardId == null) {
            dashboard = new Dashboard(type);
        } else {
            dashboard = (Dashboard) dashboardService.findById(dashboardId);
        }
        DashboardBuilderWrapper wrapper = new DashboardBuilderWrapper(dashboard);
        // we need a lazy list of items, a dynamic list
        final List<DashboardItemWrapper> wrappedItems = wrapItems(dashboard.getDashboardItems());

        List<DashboardItemWrapper> dashboardItems = LazyList.decorate(wrappedItems, FactoryUtils.instantiateFactory(DashboardItemWrapper.class));
        wrapper.setDashboardItems(dashboardItems);
        // get the populations to publish this view to if necessary
        wrapper.setPopulations(analysisService.findAll(Node.SUBJECT_UNIT_TYPE_, ZynapWebUtils.getUserId(request), AccessType.PUBLIC_ACCESS.toString()));

        if (wrapper.isPersonType()) {
            wrapper.setRoles(roleManager.getActiveAccessRoles());
            wrapper.setGroups(groupService.find(Group.TYPE_HOMEPAGE));
        }
        return wrapper;
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        DashboardBuilderWrapper wrapper = (DashboardBuilderWrapper) command;
        Map<String, Object> refData = new HashMap<String, Object>();

        Map<String, QueryParameter> parameters = new HashMap<String, QueryParameter>();
        parameters.put("accessType", new QueryParameter(AccessType.PUBLIC_ACCESS.toString(), QueryParameter.STRING));
        parameters.put("reportType", new QueryParameter(new String[]{Report.CHART_REPORT, Report.TABULAR_REPORT, Report.PROGRESS_REPORT}, QueryParameter.STRINGARRAY));
        parameters.put("populationType", new QueryParameter(Node.SUBJECT_UNIT_TYPE_, QueryParameter.STRING));
        if(wrapper.isPersonalType()) parameters.put("personal", new QueryParameter("T", QueryParameter.STRING));

        List<ReportDto> reportDtoList = reportService.findReports(parameters);

        List<ReportDto> chartReports = new ArrayList<ReportDto>();
        List<ReportDto> tabularReports = new ArrayList<ReportDto>();
        List<ReportDto> progressReports = new ArrayList<ReportDto>();
        for (ReportDto reportDto : reportDtoList) {
            if(reportDto.isTabularReport()) {
                tabularReports.add(reportDto);
            } else if (reportDto.isChartReport()){
                chartReports.add(reportDto);
            } else {
                progressReports.add(reportDto);
            }
        }
        Collections.sort(tabularReports);
        Collections.sort(chartReports);
        refData.put("tabreports", tabularReports);
        refData.put("chartreports", chartReports);
        refData.put("progressreports", progressReports);
        refData.put("dashboardType", type);
        return refData;
    }

    @Override
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        DashboardBuilderWrapper wrapper = (DashboardBuilderWrapper) command;        
        // roles and groups only valid for person dashboards not personal dashboards
        if (wrapper.isPersonType()) {
            if(RequestUtils.getLongParameters(request, "groupIds").length == 0) wrapper.setGroupIds(new Long[0]);
            if(RequestUtils.getLongParameters(request, "roleIds").length == 0) wrapper.setRoleIds(new Long[0]);
        }
    }

    @Override
    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {        
        DashboardBuilderWrapper wrapper = (DashboardBuilderWrapper) command;
        Dashboard result = wrapper.getModifiedDashboard();
        try {
            dashboardService.createOrUpdate(result);
        } catch (TalentStudioException e) {
            errors.reject("error.unknown", e.getMessage());
        }
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.DASHBOARD_ID, result.getId()));
    }

    @Override
    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        DashboardBuilderWrapper wrapper = (DashboardBuilderWrapper) command;
        RedirectView view;
        if(wrapper.getId() == null) {
            view = new ZynapRedirectView(getCancelView());
        } else {
            view = new ZynapRedirectView(getCancelView(), ParameterConstants.DASHBOARD_ID, wrapper.getId());
        }

        return new ModelAndView(view);
    }

    private List<DashboardItemWrapper> wrapItems(List<DashboardItem> dashboardItems) {
        List<DashboardItemWrapper> wrappers = new ArrayList<DashboardItemWrapper>();
        for (DashboardItem dashboardItem : dashboardItems) {
            wrappers.add(new DashboardItemWrapper(dashboardItem));
        }
        return wrappers;
    }

    public void setDashboardService(IDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    public void setType(String type) {
        this.type = type;
    }

    private IDashboardService dashboardService;
    private IReportService reportService;
    private IAnalysisService analysisService;
    private IRoleManager roleManager;
    private IGroupService groupService;
    private String type;
}
