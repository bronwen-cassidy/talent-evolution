/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 */
public class EditPopulationWizardController extends BasePopulationWizardController {

    public EditPopulationWizardController() {
        super();
        setInitialPage(NOT_ADD_CRITERIA_INDEX);
    }

    public Object formBackingObject(HttpServletRequest request) throws Exception {

        Long populationId = getPopulationId(request);
        Population population = (Population) analysisService.findById(populationId);

        final AnalysisAttributeCollection analysisAttributeCollection = builder.buildCollection();
        PopulationWrapperBean populationWrapperBean = new PopulationWrapperBean(population, analysisAttributeCollection);

        if (AccessType.PUBLIC_ACCESS.toString().equals(populationWrapperBean.getPopulation().getScope())) {
            populationWrapperBean.setScopeChangeable(!analysisService.populationInPublicReport(populationWrapperBean.getPopulation().getId()));
        }

        populationWrapperBean.removeInvalidCriterias();
        referenceDataForCriteria(populationWrapperBean);
        setNodeLabelInCriteria(populationWrapperBean);
        setInitialPage(NOT_ADD_CRITERIA_INDEX);

        populationWrapperBean.setGroups(wrapGroups(getUserGroups(), population));

        return populationWrapperBean;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        PopulationWrapperBean populationWrapperBean = (PopulationWrapperBean) command;
        Population modifiedPopulation = populationWrapperBean.getModifiedPopulation();

        try {
            analysisService.update(modifiedPopulation);
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("population.label", "error.duplicate.label", e.getMessage());
            return showForm(request, errors, getPages()[0]);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.POPULATION_ID, modifiedPopulation.getId());
        return new ModelAndView(view);
    }

    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        PopulationWrapperBean wrapper = (PopulationWrapperBean) o;
        RedirectView view = new ZynapRedirectView(getCancelView());
        view.addStaticAttribute(ParameterConstants.POPULATION_ID, wrapper.getPopulation().getId());
        return new ModelAndView(view);
    }

    /**
     * Validation - validates core values and criteria on single page.
     * <br/> Validates brackets on finish.
     *
     * @param command
     * @param errors
     * @param page
     * @param finish
     */
    protected void validatePage(Object command, Errors errors, int page, boolean finish) {

        PopulationValidator populationValidator = (PopulationValidator) getValidator();
        PopulationWrapperBean wrapper = (PopulationWrapperBean) command;
        switch (page) {
            case NOT_ADD_CRITERIA_INDEX:
            case ADD_CRITERIA_INDEX:
                populationValidator.validateRequiredValues(wrapper, errors);
                if (wrapper.getIndex() == null)
                    populationValidator.validateCriteria(wrapper, errors);

                if (finish) {
                    validateBrackets(wrapper, errors);
                }

                break;
        }
    }
}
