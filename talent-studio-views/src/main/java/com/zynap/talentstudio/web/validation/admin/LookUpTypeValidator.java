package com.zynap.talentstudio.web.validation.admin;

import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.web.utils.ZynapValidationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User: ssoong
 * Date: 10-Feb-2004
 * Time: 13:33:06
 */
public class LookUpTypeValidator implements Validator {

    public boolean supports(Class aClass) {
        return aClass.equals(LookupType.class);
    }

    public void validate(Object o, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.label.required", "Label is a required field.");
        ZynapValidationUtils.rejectGreater80(errors, "label", "error.max.length.exceeded.80", "Label must have less than 80 characters.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.description.required", "Description is a required field.");
        ZynapValidationUtils.rejectGreater4000(errors, "description", "error.max.length.exceeded.4000", "Description must have less than 4000 characters.");
    }
}
