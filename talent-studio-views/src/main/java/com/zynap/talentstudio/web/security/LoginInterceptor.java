package com.zynap.talentstudio.web.security;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.security.UserSessionFactory;

import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor that checks that user has logged in and redirects them to the login page if they have not.
 *
 * User: amark
 * Date: 25-Jan-2005
 * Time: 10:52:48
 */
public final class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * The logger.
     */
    private static final Log logger = LogFactory.getLog(LoginInterceptor.class);

    /**
     * Check that user has logged in.
     * <br> If not, redirect them to the login page and store the URL they originally requested as a request attribute so
     * it can be carried through and used later.
     * <br> Also gets the UserSession from the HttpSession and sets it on the {@link com.zynap.talentstudio.security.UserSessionFactory}.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return true if the user has already logged in.
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        if (userSession == null) {
            String url = request.getServletPath();
            logger.debug("Login required - URL being accessed is: " + url);

            // save url providing it is a GET request so we can redirect the user once they have logged in
            if (ZynapWebUtils.isGetRequest(request)) {
                url = ZynapWebUtils.appendQueryString(url, request);
                WebUtils.setSessionAttribute(request, SAVED_URL, url);
            }

            // redirect to login and return false so that the next component in the chain is not invoked
            response.sendRedirect(ZynapWebUtils.addContextPath(request, "/login.htm"));
            return false;

        } else {
            userSession.setLocale(request.getLocale());
            UserSessionFactory.setUserSession(userSession);

            // return true as it is ok to proceed
            return true;
        }
    }

    /**
     * Resets {@link com.zynap.talentstudio.security.UserSessionFactory} copy of the UserSession to null to ensure
     * that the one bound in the HttpSession gets used - ensures that changes to the one in the HttpSession are also applied to
     * the {@link com.zynap.talentstudio.security.UserSessionFactory} copy.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @param modelAndView The ModelAndView returned by the action that was invoked
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserSessionFactory.setUserSession(null);
    }

    /**
     * Constant for request attribute name that stores the URL originally requested.
     */
    public static final String SAVED_URL = "savedURL";
}
