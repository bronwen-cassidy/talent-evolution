package com.zynap.talentstudio.web.security;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.exception.ForcePasswordChangeException;
import com.zynap.exception.LoginAttemptsExceededException;
import com.zynap.exception.PasswordExpiredException;
import com.zynap.exception.UserLoginFailedException;
import com.zynap.talentstudio.arenas.IArenaMenuHandler;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller that processes login.
 * <br> Takes the supplied user name and password, and attempts to log the user in.
 * <br> If the login succeeds, creates a new {@link UserSession} object to hold the {@link UserPrincipal} object returned.
 * <br> If the login fails it may be for any number of reasons - for example, the user's password may have expired.
 * <p/>
 * User: aandersson
 * Date: 02-Feb-2004
 * Time: 17:30:40
 */
public class LoginController extends ZynapDefaultFormController {

    /**
     * Get the form backing object.
     *
     * @param request The HttpServletRequest
     * @return an instance of {@link LoginInfo}
     * @throws ServletException
     */
    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        return new LoginInfo();
    }

    /**
     * Overriden so as to ensure that people cannot access this URL directly if they are already logged in.
     *
     * @param request
     * @param response
     * @param errors
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {

        if (ZynapWebUtils.getUserSession(request) != null) {
            response.sendRedirect(request.getContextPath());
        }

        return super.showForm(request, response, errors);
    }

    /**
     * This method is called when the login form has been posted.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param command  Formbacking object
     * @param errors   BindException
     * @return The ModelAndView
     * @see com.zynap.domain.admin.LoginInfo
     */
    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        final String sessionId = request.getSession().getId();

        LoginInfo loginInfo = (LoginInfo) command;
        ModelAndView view = null;
        UserPrincipal userPrincipal;

        try {

            userPrincipal = userService.logInUser(loginInfo, sessionId, request.getRemoteAddr());

            UserSession userSession = new UserSession(userPrincipal, menuHandler);
            userSession.setLocale(request.getLocale());
            userSession.setMultiTenant(multiTenantSystem);
            // save userSession object in session
            ZynapWebUtils.setUserSession(request, userSession);

            if(userPrincipal.getUserId() != null) {
                final Long uid = userPrincipal.getUserId();
                new Thread(new PermitHandler(uid, request.getSession(false), userService)).start();
            }

            String savedURL = (String) WebUtils.getSessionAttribute(request, LoginInterceptor.SAVED_URL);
            String url = StringUtils.hasText(savedURL) ? savedURL : request.getContextPath();

            // clear saved URL
            WebUtils.setSessionAttribute(request, LoginInterceptor.SAVED_URL, null);

            response.sendRedirect(ZynapWebUtils.addContextPath(request, url));

        } catch (LoginAttemptsExceededException e) {

            errors.reject("error.number.login.attempts.exceeded", "You have exceeded the maximum number of attempts allowed to login.");
            loginInfo.setLocked(true);
            view = new ModelAndView(getFormView(), getModel(errors));

        } catch (UserLoginFailedException uex) {

            logger.debug(uex.getMessage());
            errors.reject("error.invalid.login", "Invalid username and password");
            loginInfo.resetPassword();
            view = new ModelAndView(getFormView(), getModel(errors));

        } catch (PasswordExpiredException pex) {

            Map<String, Object> model = new HashMap<String, Object>();
            model.put(ControllerConstants.USER_ID, pex.getUserId());
            model.put(ControllerConstants.USER_NAME, pex.getUsername());

            view = new ModelAndView(CHANGE_PWD_VIEW_NAME, model);

        } catch (ForcePasswordChangeException fex) {

            Map<String, Object> model = new HashMap<String, Object>();
            model.put(ControllerConstants.USER_ID, fex.getUserId());
            model.put(ControllerConstants.USER_NAME, fex.getUsername());
            model.put(ControllerConstants.FORCE_PWD_CHANGE, Boolean.TRUE);
            view = new ModelAndView(CHANGE_PWD_VIEW_NAME, model);

        }
        return view;
    }

    public void setMenuHandler(IArenaMenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Constant for name of change password view.
     */
    static final String CHANGE_PWD_VIEW_NAME = "changepwdRedirect";

    public void setMultiTenantSystem(boolean multiTenantSystem) {
        this.multiTenantSystem = multiTenantSystem;
    }

    private IArenaMenuHandler menuHandler;
    private IUserService userService;
    private boolean multiTenantSystem;
}
