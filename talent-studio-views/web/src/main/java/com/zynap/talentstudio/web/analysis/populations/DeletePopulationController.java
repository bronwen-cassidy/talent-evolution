/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DeletePopulationController extends ZynapDefaultFormController {

    public DeletePopulationController() {
    }

    /**
     * Retrieves the population for the given id in the request.
     *
     * @param request
     * @return Object an instance of an {@link com.zynap.talentstudio.analysis.populations.Population} object
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {
        Long populationId = BasePopulationWizardController.getPopulationId(request);
        return analysisService.findById(populationId);
    }

    /**
     * Delete the population.
     *
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        Population populationBase = (Population) command;
        try {
            analysisService.delete(populationBase);
            return new ModelAndView(new ZynapRedirectView(getSuccessView()));
        } catch (DataIntegrityViolationException e) {
            errors.reject("error.population.in.use", "This population is used in at least one report and cannot be deleted.");
            return showFormAndModel(request, errors, getFormView());            
        }
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        Population population = (Population) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.POPULATION_ID, population.getId()));
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    private IAnalysisService analysisService;

}
