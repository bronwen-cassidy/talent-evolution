package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.reports.support.ReportServiceHelper;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: amark
 * Date: 22-Nov-2005
 * Time: 14:34:18
 */
public class DeleteReportController extends ZynapDefaultFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long id = RequestUtils.getRequiredLongParameter(request, ParameterConstants.REPORT_ID);

        Report report = (Report) reportService.findById(id);
        report.getMenuItems().size();

        return report;
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        Report report = (Report) command;

        try {
            ReportServiceHelper.deleteReport(reportService, report, ZynapWebUtils.getUserSession(request));
            return new ModelAndView(new ZynapRedirectView(getSuccessView()));
        } catch (DataIntegrityViolationException e) {
            errors.reject("error.report.in.use", "This report is used in at least one report and cannot be deleted.");
            return showForm(request, errors, getFormView(), getModel(errors));
        }
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        Report report = (Report) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.REPORT_ID, report.getId()));
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    private IReportService reportService;
}
