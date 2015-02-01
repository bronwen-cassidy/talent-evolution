/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.web.analysis.reports.support.ReportServiceHelper;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.domain.admin.User;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditReportWizardController extends BaseTabularReportWizardController {

    protected ModelAndView processFinishInternal(Report report, HttpServletRequest request) throws TalentStudioException {
        return ReportServiceHelper.updateReport(getSuccessView(), getReportService(), report, ZynapWebUtils.getUserSession(request));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long reportId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.REPORT_ID);

        final Report report = (Report) getReportService().findById(reportId);
        final Collection<MenuSection> menuSections = getReportService().getMenuSections();
        final Collection<MenuSection> homeMenuSections = getReportService().getHomePageReportMenuSections();

        TabularReportWrapperBean reportWrapperBean = new TabularReportWrapperBean(report, menuSections, homeMenuSections, getRunReportURL());

        setColumnAttributes(reportWrapperBean);
        reportWrapperBean.checkColumnAttributes();
        // remove any invlaid items
        reportWrapperBean.removeInvalidItems();

        reportWrapperBean.setPopulations(getPopulations(ZynapWebUtils.getUserId(request), reportWrapperBean.getDefaultPopulation(), reportWrapperBean.getAccess()));
        reportWrapperBean.setGroups(wrapGroups(getUserGroups(), report));
        return reportWrapperBean;
    }

    protected void addStaticFields(RedirectView view, Object command) {
        ReportWrapperBean bean = (ReportWrapperBean) command;
        view.addStaticAttribute(ParameterConstants.REPORT_ID, bean.getModifiedReport().getId());
    }

    /**
     * Only reset the population options when the scope changes. So when the scope becomes private
     * get private and public populations of type,
     */
    protected void setSuitablePopulations(ReportWrapperBean bean, Long userId) throws TalentStudioException {
        if (bean.hasAccessChanged()) {
            bean.setPopulations(getPopulations(userId, bean.getOriginalPopulation(), bean.getAccess()));
            bean.updateAccess();
        }
    }

    protected void checkReportOwner(ReportWrapperBean reportWrapperBean, Long userId) {
        reportWrapperBean.setScopeEditable(User.ROOT_USER_ID.equals(userId) || userId.equals(reportWrapperBean.getReport().getUserId()));
    }
}
