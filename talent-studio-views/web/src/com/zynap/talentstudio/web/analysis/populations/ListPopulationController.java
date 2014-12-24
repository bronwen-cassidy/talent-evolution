package com.zynap.talentstudio.web.analysis.populations;


import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ControllerUtils;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: ssoong
 * Date: 23-Apr-2004
 * Time: 16:01:13
 * To change this template use File | Settings | File Templates.
 */
public class ListPopulationController extends ZynapDefaultFormController {

    /**
     * Overriden form submission method - prevents reloading of populations when sorting populations.
     * <br> See {@link ControllerUtils}.isSearchFormSubmission()) for further details.
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

        Collection<PopulationDto> populations = analysisService.findAll(null, ZynapWebUtils.getUserId(request), null);
        return new ListPopulationWrapperBean(populations);
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    private IAnalysisService analysisService;
}
