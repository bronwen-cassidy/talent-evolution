package com.zynap.web.controller.admin;

import com.zynap.domain.admin.UserPassword;
import com.zynap.web.validation.ChangePwdValidator;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller used by an administrator to change a user's password.
 * User: aandersson
 * Date: 12-Oct-2004
 * Time: 10:32:24
 */
public class AdminResetPasswordFormController extends BaseResetPasswordFormController {

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        UserPassword pwdObject = (UserPassword) command;
        ChangePwdValidator changePwdValidator = new ChangePwdValidator();
        changePwdValidator.validateNewPassword(pwdObject, errors);
    }
}
