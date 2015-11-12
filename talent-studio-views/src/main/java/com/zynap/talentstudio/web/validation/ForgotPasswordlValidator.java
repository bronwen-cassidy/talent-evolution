/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.validation;

import com.zynap.domain.admin.LoginInfo;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 30-Jan-2009 10:41:08
 */
public class ForgotPasswordlValidator implements Validator {

    public boolean supports(Class aClass) {
        return aClass.equals(LoginInfo.class);
    }


    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.login.username.missing", "Please enter your username.");
    }
}