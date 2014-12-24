/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
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
public class AddPopulationWizardController extends BasePopulationWizardController {

    public AddPopulationWizardController() {
        super();
    }

    public Object formBackingObject(HttpServletRequest request) throws Exception {

        PopulationWrapperBean populationWrapperBean = (PopulationWrapperBean) super.recoverFormBackingObject(request);
        if (populationWrapperBean == null) {
            Population population = new Population();
            populationWrapperBean = new PopulationWrapperBean(population);
            populationWrapperBean.setGroups(wrapGroups(getUserGroups(), population));
        }

        return populationWrapperBean;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        PopulationWrapperBean population = (PopulationWrapperBean) command;

        Long principalId = ZynapWebUtils.getUserId(request);
        Population pMod = population.getModifiedPopulation();
        try {
            analysisService.create(pMod, principalId);

        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("population.label", "error.duplicate.label", e.getMessage());
            return showForm(request, errors, getPages()[0]);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.POPULATION_ID, pMod.getId());
        return new ModelAndView(view);
    }


    /**
     * Validation - validates core values on page 0 and criteria for page 1.
     * <br/> Validates brackets on finish for page 1 only.
     *
     * @param command - the form backing object a {@link com.zynap.talentstudio.web.analysis.populations.PopulationWrapperBean}
     * @param errors - the errors object containing any validation errors
     * @param page   - the page being displayed
     * @param finish - finished or not
     */
    protected void validatePage(Object command, Errors errors, int page, boolean finish) {

        PopulationValidator populationValidator = (PopulationValidator) getValidator();
        PopulationWrapperBean wrapper = (PopulationWrapperBean) command;
        switch (page) {
            case ADD_CORE_DETAILS_PAGE_INDEX:
                // handle page one validation
                if (wrapper.getTarget() != 0)
                    populationValidator.validateRequiredValues(wrapper, errors);
                break;
            case NOT_ADD_CRITERIA_INDEX:
            case ADD_CRITERIA_INDEX:
                if (wrapper.getIndex() == null)
                    populationValidator.validateCriteria(wrapper, errors);

                if (finish) {
                    validateBrackets(wrapper, errors);
                }

                break;
        }
    }
}