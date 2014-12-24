package com.zynap.talentstudio.web.security.area;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User: amark
 * Date: 16-Mar-2005
 * Time: 16:13:03
 */
public class AreaValidator implements Validator {

    /**
     * Return whether or not this object can validate objects
     * of the given class.
     */
    public boolean supports(Class clazz) {
        return AreaWrapperBean.class.isAssignableFrom(clazz);
    }

    /**
     * Validate an object, which must be of a class for which
     * the supports() method returned true.
     *
     * @param obj Populated object to validate
     * @param errors Errors object we're building. May contain
     * errors for this field relating to types.
     */
    public void validate(Object obj, Errors errors) {
        validateCoreValues(obj, errors);
    }

    /**
     * Validate the core values of the object (currently only label is required.)
     *
     * @param obj Populated object to validate
     * @param errors Errors object we're building. May contain
     * errors for this field relating to types.
     */
    public void validateCoreValues(Object obj, Errors errors) {
         ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.area.label.required", "'Name' is a required field.");
    }
}
