/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.MetricReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.reports.support.ReportServiceHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class AddMetricReportController extends BaseMetricReportController {

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ReportWrapperBean bean = (ReportWrapperBean) command;
        Report report = bean.getModifiedReport();
        return ReportServiceHelper.createReport(getSuccessView(), getReportService(), report, ZynapWebUtils.getUserSession(request));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        UserSession userSession = ZynapWebUtils.getUserSession(request);
        Report report = new MetricReport();
        report.setUserId(userSession.getId());
        report.setReportType(Report.METRIC_REPORT);

        final Collection menuSections = getReportService().getMenuSections();
        final Collection homeMenuSections = getReportService().getHomePageReportMenuSections();

        MetricReportWrapperBean metricReportWrapperBean = new MetricReportWrapperBean(report, menuSections, homeMenuSections, getRunReportURL());
        metricReportWrapperBean.setGroups(wrapGroups(getUserGroups(), report));
        return metricReportWrapperBean;

    }

    protected void setSuitablePopulations(ReportWrapperBean bean, Long userId) throws TalentStudioException {
        if(bean.getColumns().isEmpty()) {
            bean.setPopulations(getPopulations(userId, null, bean.getAccess()));
        } else {
            bean.setPopulations(getPopulations(userId, bean.getDefaultPopulation(), bean.getAccess()));
        }
    }
}
