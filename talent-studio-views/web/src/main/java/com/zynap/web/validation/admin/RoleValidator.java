package com.zynap.web.validation.admin;

import com.zynap.talentstudio.security.roles.Role;
import com.zynap.web.utils.ZynapValidationUtils;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: ssoong
 * Date: 10-Feb-2004
 * Time: 13:33:06
 * To change this template use Options | File Templates.
 */
public class RoleValidator implements Validator {

    public boolean supports(Class aClass) {
        return aClass.equals(Role.class);
    }

    /**
     * Validates the field inputs for adding a new role into the system
     *
     * @param o
     * @param errors
     */
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "label", "error.role.name.required", "Role name id a required value");
        ValidationUtils.rejectIfEmpty(errors, "description", "error.role.description.required", "Description is a required value");
        ZynapValidationUtils.rejectGreater255(errors, "description", "error.max.length.exceeded.255", "Cannot be more than 255 characters");
    }
}
