/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.web.analysis.reports.support.ReportServiceHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class AddReportWizardController extends BaseTabularReportWizardController {

    protected ModelAndView processFinishInternal(Report report, HttpServletRequest request) throws TalentStudioException {
        return ReportServiceHelper.createReport(getSuccessView(), getReportService(), report, ZynapWebUtils.getUserSession(request));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        Report report = new TabularReport();
        report.setUserId(userSession.getId());
        report.setReportType(Report.TABULAR_REPORT);
        final Collection menuSections = getReportService().getMenuSections();
        final Collection homeMenuSections = getReportService().getHomePageReportMenuSections();
        TabularReportWrapperBean tabularReportWrapperBean = new TabularReportWrapperBean(report, menuSections, homeMenuSections, getRunReportURL());
        tabularReportWrapperBean.setGroups(wrapGroups(getUserGroups(), report));
        return tabularReportWrapperBean;
    }

    protected void setSuitablePopulations(ReportWrapperBean bean, Long userId) throws TalentStudioException {
        if(bean.getColumns().isEmpty()) {
            bean.setPopulations(getPopulations(userId, null, bean.getAccess()));
        } else {
            bean.setPopulations(getPopulations(userId, bean.getDefaultPopulation(), bean.getAccess()));
        }
    }
}
