package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * User: amark
 * Date: 07-Sep-2005
 * Time: 14:56:44
 */
public class ReportServiceHelper {

    public static ModelAndView createReport(String view, IReportService reportService, Report report, UserSession userSession) throws TalentStudioException {
        reportService.create(report);
        reloadMenuItems(userSession);

        return buildModelAndView(view, report);
    }

    public static void deleteReport(IReportService reportService, Report report, UserSession userSession) throws TalentStudioException {
        reportService.delete(report);
        reloadMenuItems(userSession);
    }

    public static ModelAndView updateReport(String view, IReportService reportService, Report report, UserSession userSession) throws TalentStudioException {
        reportService.update(report);
        reloadMenuItems(userSession);

        return buildModelAndView(view, report);
    }

    public static ModelAndView buildModelAndView(String view, Report report) {
        RedirectView redirectView = new ZynapRedirectView(view, ParameterConstants.REPORT_ID, report.getId());
        return new ModelAndView(redirectView);
    }

    private static void reloadMenuItems(UserSession userSession) throws TalentStudioException {
        userSession.reloadMenus();
    }
}
