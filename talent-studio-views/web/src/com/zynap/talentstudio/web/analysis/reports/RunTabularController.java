/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.history.HistoryHelper;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class RunTabularController extends RunReportController {

    protected Collection getRunnableReports(RunReportWrapperBean reportWrapperBean, UserSession userSession) {
        return getReportService().findAllStandardReports(userSession.getId());
    }

    @Override
    public Class getReportClass() {
        return TabularReport.class;
    }

    /**
     * Run the report.
     *
     * @param wrapper
     * @param userId
     * @param request
     * @throws TalentStudioException
     */
    protected void runReport(RunReportWrapperBean wrapper, Long userId, HttpServletRequest request) throws TalentStudioException {
        reportRunner.run(wrapper, userId);
    }

    protected RunReportWrapperBean recoverFormBackingObject(HttpServletRequest request) {
        return (RunReportWrapperBean) HistoryHelper.recoverCommand(request, RunReportWrapperBean.class);
    }

    protected RunReportWrapperBean createFormBackingObject(Report report, Long userId) {
        return new RunReportWrapperBean(report);
    }
}
