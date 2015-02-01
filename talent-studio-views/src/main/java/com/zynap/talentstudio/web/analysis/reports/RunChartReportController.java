/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.chart.ChartDataStructure;
import com.zynap.talentstudio.web.analysis.reports.views.RunChartReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.history.HistoryHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class RunChartReportController extends RunReportController {

    protected Collection getRunnableReports(RunReportWrapperBean reportWrapperBean, UserSession userSession) {
        return getReportService().findAll(Report.CHART_REPORT, userSession.getId());
    }

    @Override
    public Class getReportClass() {
        return ChartReport.class;
    }

    /**
     * Run the report.
     *
     * @param wrapper
     * @param userId
     * @param request
     * @throws com.zynap.exception.TalentStudioException
     */
    protected void runReport(RunReportWrapperBean wrapper, Long userId, HttpServletRequest request) throws TalentStudioException {
        reportRunner.run(wrapper, userId);
        // add the results to the session
        ChartDataStructure data = ((RunChartReportWrapper) wrapper).getData();
        HttpSession session = request.getSession();
        session.removeAttribute(session.getId());

        data.setId(session.getId());
        session.setAttribute(data.getId(), data);
    }

    protected RunReportWrapperBean recoverFormBackingObject(HttpServletRequest request) {
        return (RunChartReportWrapper) HistoryHelper.recoverCommand(request, RunChartReportWrapper.class);
    }

    protected RunReportWrapperBean createFormBackingObject(Report report, Long userId) {
        return new RunChartReportWrapper(report);
    }
}