/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.UserPassword;
import com.zynap.exception.AlphaOnlyRuleException;
import com.zynap.exception.InvalidPasswordException;
import com.zynap.exception.MaxLengthRuleException;
import com.zynap.exception.MinLengthRuleException;
import com.zynap.exception.UniqueRuleException;
import com.zynap.exception.UserLoginFailedException;
import com.zynap.talentstudio.arenas.ArenaMenuHandler;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller used when a user is forced to change their password.
 *
 * @author Andreas Andersson
 * @version $Revision: $
 *          $Id: $
 * @since 18/03/2004
 */
public class ChangePwdController extends ZynapDefaultFormController {

    /**
     * Get the form backing object.
     *
     * @param request The HttpServletRequest
     * @return an instance of {@link UserPassword}
     * @throws ServletException
     */
    protected Object formBackingObject(HttpServletRequest request) throws ServletException {

        Long userId = RequestUtils.getRequiredLongParameter(request, ControllerConstants.USER_ID);
        String userName = RequestUtils.getRequiredStringParameter(request, ControllerConstants.USER_NAME);

        UserPassword password = new UserPassword();
        password.setUserId(userId);
        password.setUsername(userName);

        logger.debug("Change Password - [" + userId + "," + userName + "]");

        // see if password change is mandatory
        final String parameter = request.getParameter(ControllerConstants.FORCE_PWD_CHANGE);
        if (StringUtils.hasText(parameter)) {
            password.setForceChange(true);
        }

        return password;
    }

    /**
     * This method is called when the change password form has been posted.
     * <br> The user can only reach the form if they are a new user who needs to enter a new password,
     * or a user whose password has expired.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param command Formbacking object
     * @param errors BindException
     * @return The ModelAndView
     * @see com.zynap.domain.admin.UserPassword
     */
    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        // Read form backing object and its parameters
        UserPassword pwdObject = (UserPassword) command;
        LoginInfo loginInfo = new LoginInfo();

        try {
            loginInfo = getUserService().changePassword(pwdObject);
        } catch (UniqueRuleException ue) {
            errors.rejectValue("newPassword", "error.uniquepwd.rule.violated", "You cannot re-use a recent previous password. Please select another.");
        } catch (MinLengthRuleException me) {
            errors.rejectValue("newPassword", "error.minlength.rule.violated", me.getMessage());
        } catch (MaxLengthRuleException mle) {
            errors.rejectValue("newPassword", "error.maxlength.rule.violated", mle.getMessage());
        } catch (AlphaOnlyRuleException ae) {
            errors.rejectValue("newPassword", "error.alpha.rule.violated", "Password contains illegal characters");
        } catch (UserLoginFailedException le) {
            errors.rejectValue("username", le.getMessage(), null, "Login failed");
        } catch (InvalidPasswordException ie) {
            errors.rejectValue("oldPassword", ie.getMessage(), null, "Existing password does not match");
        }

        // If we have any errors the go back to the form that was posted
        if (errors.hasErrors()) {
            return showForm(request, response, errors);
        }

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        // indicates that user has not logged in and has been forced to change password
        // - therefore log the user in and then possibly redirect to previous url if required
        if (userSession == null) {
            try {

                LoginInfo newLoginInfo = new LoginInfo();
                newLoginInfo.setUser(loginInfo.getUser());
                newLoginInfo.setUsername(pwdObject.getUsername());
                newLoginInfo.setPassword(pwdObject.getNewPassword());
                UserPrincipal principal = getUserService().logInUser(newLoginInfo, request.getSession().getId(), request.getRemoteAddr());
                userSession = new UserSession(principal, getMenuHandler());
                userSession.setLocale(request.getLocale());
                userSession.setMultiTenant(multiTenantSystem);

                // Save userSession object in session
                ZynapWebUtils.setUserSession(request, userSession);
                if(principal.getUserId() != null) {
                    final Long uid = principal.getUserId();
                    new Thread(new PermitHandler(uid, request.getSession(false), userService)).start();
                }

            } catch (Exception e) {

                logger.error("Failed to log in user " + loginInfo.getUser().getId() + " after changing password because of: ", e);

                // redirect user to login page
                response.sendRedirect(ZynapWebUtils.addContextPath(request, "/login.htm"));
                return null;
            }
        }

        // redirect to start of app
        response.sendRedirect(request.getContextPath());

        return null;
    }

    public ArenaMenuHandler getMenuHandler() {
        return menuHandler;
    }

    public void setMenuHandler(ArenaMenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setMultiTenantSystem(boolean multiTenantSystem) {
        this.multiTenantSystem = multiTenantSystem;
    }

    private ArenaMenuHandler menuHandler;
    private IUserService userService;
    private boolean multiTenantSystem;
}
