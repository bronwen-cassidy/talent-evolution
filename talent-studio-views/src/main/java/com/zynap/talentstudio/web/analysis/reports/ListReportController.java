package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.ReportDto;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ControllerUtils;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;

public class ListReportController extends ZynapDefaultFormController {

    /**
     * Overriden form submission method - prevents reloading of populations when sorting populations.
     * <br> See {@link com.zynap.talentstudio.web.utils.controller.ControllerUtils}.isSearchFormSubmission()) for further details.
     *
     * @param request
     * @return True if this is a form submission.
     */
    protected boolean isFormSubmission(HttpServletRequest request) {
        return (ControllerUtils.isSearchFormSubmission(request)) || super.isFormSubmission(request);
    }

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        return showForm(request, errors, getFormView());
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException, TalentStudioException {

        Collection<ReportDto> reports = reportService.findAll(reportType, ZynapWebUtils.getUserId(request));
        return new ListReportWrapperBean(reports);
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    private IReportService reportService;
    private String reportType;
}
