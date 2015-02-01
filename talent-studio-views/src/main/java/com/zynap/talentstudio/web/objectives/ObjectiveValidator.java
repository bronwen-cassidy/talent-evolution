/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.web.organisation.attributes.AttributeValueValidationFactory;
import com.zynap.talentstudio.web.organisation.attributes.AttributeValueValidationUtils;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-May-2006 13:35:57
 */
public class ObjectiveValidator extends ObjectiveSetValidator {

    public boolean supports(Class clazz) {
        return ObjectiveSetFormBean.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
    }

    /**
     * Validates each objective
     *
     * @param wrapperBean
     * @param errors
     */
    public void validateObjectiveSet(ObjectiveSetFormBean wrapperBean, Errors errors) {
        final List currentObjectives = wrapperBean.getObjectives();
        validateObjectives(currentObjectives, errors);
    }

    /**
     * Overridden to add extra validation for the parent id and the attributes.
     *
     * @param objective The objective
     * @param errors    The errors object
     * @param index     The index of the list
     */
    public void validateObjective(ObjectiveWrapperBean objective, Errors errors, int index) {
        super.validateObjective(objective, errors, index);
        if (objective.getSelectedParentId() == null) errors.rejectValue("objectives[" + index + "].parentDesc", "error.required.field");
        String prefix = "objectives[" + index + "].wrappedDynamicAttributes";
        AttributeValueValidationUtils.validateAttributes(factory, objective.getWrappedDynamicAttributes(), errors, prefix, null);
    }

    public void validateOrgUnitObjectives(ObjectiveSetFormBean bean, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "objectiveSet.label", "error.required.field");
        List objectives = bean.getObjectives();
        validateObjectives(objectives, errors);        
    }

    public final void setFactory(AttributeValueValidationFactory factory) {
        this.factory = factory;
    }

    private AttributeValueValidationFactory factory;
}
