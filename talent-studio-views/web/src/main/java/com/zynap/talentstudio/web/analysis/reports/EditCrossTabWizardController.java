/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTabCellInfo;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.web.analysis.reports.support.ReportServiceHelper;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditCrossTabWizardController extends BaseCrossTabWizardController {

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ReportWrapperBean bean = (ReportWrapperBean) command;
        Report report = bean.getModifiedReport();

        return ReportServiceHelper.updateReport(getSuccessView(), getReportService(), report, ZynapWebUtils.getUserSession(request));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final Long userId = ZynapWebUtils.getUserId(request);

        Long reportId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.REPORT_ID);
        final CrossTabReport report = (CrossTabReport) getReportService().findById(reportId);
        
        // lazy load these crossTabCellInfos
        final List<CrossTabCellInfo> cellInfos = report.getCrossTabCellInfos();
        cellInfos.size();

        final Collection<MenuSection> menuSections = getReportService().getMenuSections();
        final Collection<MenuSection> homeMenuSections = getReportService().getHomePageReportMenuSections();

        CrossTabReportWrapperBean bean = new CrossTabReportWrapperBean(report, menuSections, homeMenuSections, getRunReportURL());       
        initMetrics(bean, userId);
        initDrilldownReports(bean, userId);

        setColumnAttributes(bean);
        bean.checkColumnAttributes();
        bean.removeInvalidItems();
        bean.setPopulations(getPopulations(userId, bean.getDefaultPopulation(), bean.getAccess()));
        bean.setGroups(wrapGroups(getUserGroups(), report));
        //assignCellLabels(bean);
        return bean;
    }

    protected void addStaticFields(RedirectView view, Object command) {
        ReportWrapperBean bean = (ReportWrapperBean) command;
        view.addStaticAttribute(ParameterConstants.REPORT_ID, bean.getModifiedReport().getId());
    }

    protected void checkReportOwner(ReportWrapperBean reportWrapperBean, Long userId) {
        reportWrapperBean.setScopeEditable(User.ROOT_USER_ID.equals(userId) || userId.equals(reportWrapperBean.getReport().getUserId()));
    }

    protected void setSuitablePopulations(ReportWrapperBean bean, Long userId) throws TalentStudioException {
        if (bean.hasAccessChanged()) {
            bean.setPopulations(getPopulations(userId, bean.getOriginalPopulation(), bean.getAccess()));
            bean.updateAccess();
        }
    }

    protected List<CrossTabCellInfo> getExistingCrossTabCellInfos(CrossTabReportWrapperBean wrapperBean) {
        final CrossTabReport report = (CrossTabReport) wrapperBean.getReport();
        return report.getCrossTabCellInfos();
    }
}
