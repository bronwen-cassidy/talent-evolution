package com.zynap.web.validation.admin;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.web.utils.ZynapValidationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User: ssoong
 * Date: 10-Feb-2004
 * Time: 13:33:06
 */
public class LookUpValueValidator implements Validator {

    public boolean supports(Class aClass) {
        return aClass.equals(LookupValue.class);
    }

    public void validate(Object o, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.required.field", "Display name is a required field.");
        ZynapValidationUtils.rejectGreater100(errors, "label", "error.max.length.exceeded.100", "Display name must have less than 100 characters");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.description.required", "Description is a required field.");
        ZynapValidationUtils.rejectGreater4000(errors, "description", "error.max.length.exceeded.4000", "Description must have less than 4000 characters");

        LookupValue lookupValue = (LookupValue) o;
        if (lookupValue.getSortOrder() < 0) {
            errors.rejectValue("sortOrder", "field.is.numeric", "Please enter a number greater than zero.");
        }
    }
}
