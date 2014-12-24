/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTabCellInfo;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.web.analysis.reports.support.ReportServiceHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

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
public class AddCrossTabWizardController extends BaseCrossTabWizardController {

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ReportWrapperBean bean = (ReportWrapperBean) command;
        Report report = bean.getModifiedReport();

        return ReportServiceHelper.createReport(getSuccessView(), getReportService(), report, ZynapWebUtils.getUserSession(request));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        UserSession userSession = ZynapWebUtils.getUserSession(request);
        Report report = new CrossTabReport();
        report.setUserId(userSession.getId());
        report.setReportType(Report.CROSSTAB_REPORT);

        final Collection<MenuSection> menuSections = getReportService().getMenuSections();
        final Collection<MenuSection> homeMenuSections = getReportService().getHomePageReportMenuSections();

        CrossTabReportWrapperBean bean = new CrossTabReportWrapperBean(report, menuSections, homeMenuSections, getRunReportURL());
        bean.setVerticalColumn(new Column(null, null, null, null, "left"));
        bean.setHorizontalColumn(new Column(null, null, null, null, "left"));
        bean.setGroups(wrapGroups(getUserGroups(), report));
        return bean;
    }

    protected void setSuitablePopulations(ReportWrapperBean bean, Long userId) throws TalentStudioException {

        final CrossTabReportWrapperBean crossTabReportWrapperBean = (CrossTabReportWrapperBean) bean;
        final ColumnWrapperBean horizontalColumnWrapperBean = crossTabReportWrapperBean.getHorizontalColumn();
        final ColumnWrapperBean verticalColumnWrapperBean = crossTabReportWrapperBean.getVerticalColumn();

        if (horizontalColumnWrapperBean.hasAttributeName() || verticalColumnWrapperBean.hasAttributeName()) {
            bean.setPopulations(getPopulations(userId, bean.getDefaultPopulation(), bean.getAccess()));
        } else {
            bean.setPopulations(getPopulations(userId, null, bean.getAccess()));
        }
    }

    protected List<CrossTabCellInfo> getExistingCrossTabCellInfos(CrossTabReportWrapperBean wrapperBean) {
        return wrapperBean.extractCrosstabCellInfos();
    }
}
