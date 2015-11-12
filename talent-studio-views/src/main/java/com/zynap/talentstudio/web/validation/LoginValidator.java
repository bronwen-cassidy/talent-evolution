package com.zynap.talentstudio.web.validation;

import com.zynap.domain.admin.LoginInfo;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 02-Feb-2004
 * Time: 17:31:07
 * Validator that checks user has entered a username and password.
 * <br> Only checks that the username and password are not null - does not check that they are valid for the system.
 */
public class LoginValidator implements Validator {

    public boolean supports(Class aClass) {
        return aClass.equals(LoginInfo.class);
    }

    /**
     * Check user name and password have been supplied.
     *
     * @param o The object being validated (expects a {@link com.zynap.domain.admin.LoginInfo} object
     * @param errors
     */
    public void validate(Object o, Errors errors) {
        LoginInfo loginInfo = (LoginInfo) o;

        if (loginInfo == null || !StringUtils.hasText(loginInfo.getUsername()))
            errors.rejectValue("username", "error.login.username.missing", null, "Please enter your username.");
        if (loginInfo == null || !StringUtils.hasText(loginInfo.getPassword()))
            errors.rejectValue("password", "error.login.password.missing", null, "Please enter your password.");
    }
}
