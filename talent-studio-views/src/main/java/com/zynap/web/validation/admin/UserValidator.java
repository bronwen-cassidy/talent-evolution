package com.zynap.web.validation.admin;

import com.zynap.talentstudio.web.security.admin.UserWrapperBean;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    public boolean supports(Class aClass) {
        return aClass.equals(UserWrapperBean.class);
    }

    /**
     * Validates both core values only.
     *
     * @param obj
     * @param errors
     */
    public void validate(Object obj, Errors errors) {
        validateCoreValues(obj, errors);
    }

    /**
     * Validate title, first name, last name, preferred given name and user name.
     *
     * @param o
     * @param errors
     */
    public void validateCoreValues(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "error.firstname.missing", "'First Name' is a required field.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "secondName", "error.lastname.missing", "'Last Name' is a required field.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginInfo.username", "error.username.missing", "'User Name' is required.");
    }

    /**
     * Validate password and repeated password.
     *
     * @param o
     * @param errors
     */
    public void validatePassword(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "loginInfo.password", "error.password.missing", "'Password' is a required field.");
        ValidationUtils.rejectIfEmpty(errors, "loginInfo.repeatedPassword", "error.repeat.password.missing", "'Repeat Password' is a required field.");

        // Validate that the two password fields match
        UserWrapperBean user = (UserWrapperBean) o;
        final String password = user.getLoginInfo().getPassword();
        final String repeatedPassword = user.getLoginInfo().getRepeatedPassword();
        if (StringUtils.hasText(password) && StringUtils.hasText(repeatedPassword) && !password.equals(repeatedPassword)) {
            errors.rejectValue("loginInfo.password", "error.password.mismatch", null, "Passwords do not match.");
        }
    }

}
