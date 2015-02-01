/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.ObjectiveSet;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 08-Mar-2007 11:12:01
 */
public class ObjectiveSetValidator implements Validator {

    public boolean supports(Class clazz) {
        return CorporateObjectivesFormBean.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) target;
        validateObjectiveSet(bean.getModifiedObjectiveSet(), errors);
        validateObjectives(bean.getObjectives(), errors);
    }

    private void validateObjectiveSet(ObjectiveSet objectiveSet, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "objectiveSet.label", "error.required.field");
        if(objectiveSet.getLabel() != null && objectiveSet.getLabel().length() > 500) errors.rejectValue("objectiveSet.label", "error.max.length.exceeded.500");
        Date date = objectiveSet.getExpiryDate();
        if(date != null) {
            Date today = new Date();
            if(date.before(today)) {
                errors.rejectValue("objectiveSet.expiryDate", "error.expiry.date.before.today");
            }
        }
    }

    public void validateObjectives(final List objectives, Errors errors) {

        List<String> labels = new ArrayList<String>(objectives.size());
        List<String> descriptions = new ArrayList<String>(objectives.size());

        for (int i = 0; i < objectives.size(); i++) {
            ObjectiveWrapperBean wrapper = (ObjectiveWrapperBean) objectives.get(i);

            // validate common
            validateObjective(wrapper, errors, i);

            String label = wrapper.getLabel();
            String description = wrapper.getDescription();

            if(labels.contains(label)) {
                errors.rejectValue("objectives[" + i + "].label", "error.duplicate.label");
            } else {
                labels.add(label);
            }
            if(descriptions.contains(description)) {
                errors.rejectValue("objectives[" + i + "].description", "error.duplicate.field");
            } else {
                descriptions.add(description);
            }
        }
    }

    public void validateObjective(ObjectiveWrapperBean wrapper, Errors errors, int i) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "objectives[" + i + "].label", "error.required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "objectives[" + i + "].description", "error.required.field");
        final String label = wrapper.getLabel();
        final String description = wrapper.getDescription();
        if(label != null && label.length() > 500) errors.rejectValue("objectives[" + i + "].label", "error.max.length.exceeded.500");
        if(description != null && description.length() > 2000) errors.rejectValue("objectives[" + i + "].description", "error.max.length.exceeded.2000");
    }
}
