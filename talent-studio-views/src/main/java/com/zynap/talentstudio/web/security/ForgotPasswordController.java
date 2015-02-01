/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security;

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.mail.IMailNotification;
import com.zynap.talentstudio.security.users.IUserService;

import org.springframework.mail.MailException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 30-Jan-2009 10:15:55
 */
public class ForgotPasswordController extends SimpleFormController {

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return new LoginInfo();
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        return super.showForm(request, errors, getFormView());
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        LoginInfo loginInfo = (LoginInfo) command;
        User user;
        try {
            user = userService.findByUserName(loginInfo.getUsername());
        } catch (TalentStudioException e) {
            errors.rejectValue("username", "error.invalid.login.username", null, "The username does not exist.");
            return showForm(request, response, errors);
        }

        if (user == null) {
            errors.rejectValue("username", "error.invalid.login.username", null, "The username does not exist.");
            return showForm(request, response, errors);
        } else if (!StringUtils.hasLength(user.getCoreDetail().getContactEmail())) {
            errors.rejectValue("username", "error.invalid.password.reminder", null, "The user does not have a valid email address please contact your system administrator");
            return showForm(request, response, errors);
        }

        try {
            LoginInfo userLogin = user.getLoginInfo();

            String password = getPassword(userLogin.getPassword());
            // unlock any locked user accounts
            userLogin.setLocked(false);
            userLogin.setNumberLoginAttempts(0);
            userService.update(userLogin);
            sendPasswordReminderEmail(password, user);
            
        } catch (TalentStudioException e) {
            logger.error(e.getMessage(), e);
            errors.rejectValue("username", "error.unknown", new Object[]{e.getMessage()}, "The user does not have a valid email address please contact your system administrator");
            return showForm(request, response, errors);
        } catch (MailException e) {
            logger.error("MAIL ERROR: SEND FAILED!! " + e.getMessage(), e);
        }

        return new ModelAndView(new RedirectView(getSuccessView()));
    }

    private void sendPasswordReminderEmail(String password, User user) {
        passwordReminderMailNotification.send("/login.htm", user, password);
    }

    private String getPassword(String password) {
        return userService.decrypt(password);
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public IMailNotification getPasswordReminderMailNotification() {
        return passwordReminderMailNotification;
    }

    public void setPasswordReminderMailNotification(IMailNotification passwordReminderMailNotification) {
        this.passwordReminderMailNotification = passwordReminderMailNotification;
    }

    private IUserService userService;
    private IMailNotification passwordReminderMailNotification;
}
