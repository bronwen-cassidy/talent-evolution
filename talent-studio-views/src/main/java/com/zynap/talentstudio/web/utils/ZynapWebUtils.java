/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils;

import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.exceptions.InvalidSubmitException;
import com.zynap.talentstudio.web.SessionConstants;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Help methods for web related things...
 *
 * @author Andreas Andersson
 * @since 23/04/2004
 */
public final class ZynapWebUtils {

    public static Long getUserOrgUnitRootId(HttpServletRequest request) {
        return getUserSession(request).getUserOrgUnitRootId();
    }

    public static void setMenuSections(Collection menuSections, HttpServletRequest request) {
        if (menuSections != null) {
            request.setAttribute(ParameterConstants.MENUS_ATTR_KEY, menuSections);
        }
    }

    public static boolean isSuperUser(HttpServletRequest request) {
        final UserSession userSession = getUserSession(request);
        if(userSession != null) {
            return userSession.hasSuperUserPriviledges();
        }
        return false;
    }

	public static Locale getUserLocale(HttpServletRequest request) {
		return getUserSession(request) != null ? getUserSession(request).getLocale() : null;
	}

	/**
     * The logger.
     */
    private static final Log logger = LogFactory.getLog(ZynapWebUtils.class);

    /**
     * Turn special characters into escaped characters conforming to JavaScript.
     *
     * @param input the input string
     * @return string with characters escaped and surrounded by single quotes, or empty string (not null.)
     */
    public static String javaScriptEscape(String input) {

        String output = "";
        if (StringUtils.hasText(input)) {
            output = "'" + StringEscapeUtils.escapeJavaScript(input) + "'";
        }

        return output;
    }

    /**
     * Removes any command objects bound into the request by Spring's controller framework.
     *
     * @param request The HttpServletRequest
     */
    public static void removeCommandFromSession(HttpServletRequest request) {

        final String commandSuffix = "." + ControllerConstants.COMMAND_NAME;

        Enumeration atts = request.getSession().getAttributeNames();
        while (atts.hasMoreElements()) {
            String attName = (String) atts.nextElement();
            if (attName.indexOf(commandSuffix) > 0)
                request.getSession().removeAttribute(attName);
        }
    }

    /**
     * Save the previous URL as a session attribute.
     *
     * @param request The HttpServletRequest
     */
    public static void savePreviousURI(HttpServletRequest request) {
        request.getSession().setAttribute(PREVIOUS_URI_PARAM, request.getRequestURI());
    }

    /**
     * Get the previous URL from the session.
     *
     * @param request The HttpServletRequest
     * @return The url stored in the session attribute, or null
     */
    public static String getPreviousURI(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(PREVIOUS_URI_PARAM);
    }

    /**
     * Save the current URL as a request attribute.
     *
     * @param request The HttpServletRequest
     */
    public static void saveCurrentURI(HttpServletRequest request) {
        request.setAttribute(CURRENT_REQUEST_URI_ATTR, getRequestURIWithoutContextPath(request));
    }

    /**
     * Get the URL from the request.
     *
     * @param request The HttpServletRequest
     * @return The url stored in the request attribute, or null
     */
    public static String getCurrentURI(HttpServletRequest request) {
        return (String) request.getAttribute(CURRENT_REQUEST_URI_ATTR);
    }

    /**
     * Check if the request is going to the same URI as last time.
     *
     * @param request
     * @return true if the previous URI is not the same as the current URI.
     */
    public static boolean checkURLChange(HttpServletRequest request) {
        String previousUri = getPreviousURI(request);
        return (previousUri != null && !previousUri.equals(request.getRequestURI()));
    }

    /**
     * Check if there is a pattern in the list of urls that matches the request servlet path.
     *
     * @param urls    The list of urls
     * @param request
     * @return true if there is an entry that matches
     */
    public static boolean urlMatches(Collection urls, HttpServletRequest request) {

        boolean matches = false;

        final String requestUrl = request.getServletPath();

        for (Iterator iterator = urls.iterator(); iterator.hasNext();) {
            String url = (String) iterator.next();
            matches = urlMatches(url, requestUrl);
            // stop if matches found
            if (matches) break;
        }

        return matches;
    }

    public static boolean urlMatches(String url, String requestUrl) {
        
        Matcher matcher = Pattern.compile(url, Pattern.CASE_INSENSITIVE).matcher(requestUrl);
        return matcher.matches();
    }

    /**
     * Determine if this is a wizard request.
     *
     * @param request
     * @return true if the url has not changed and the request is a POST.
     */
    public static boolean isWizardRequest(HttpServletRequest request) {
        return !checkURLChange(request) && isPostRequest(request);
    }

    /**
     * Check if request is a cancel request.
     *
     * @param request
     * @return True if the request contains a value for the parameter {@link com.zynap.talentstudio.web.common.ParameterConstants#CANCEL_PARAMETER}
     */
    public static boolean isCancelled(HttpServletRequest request) {
        String cancel = request.getParameter(ParameterConstants.CANCEL_PARAMETER);
        return StringUtils.hasText(cancel);
    }

    /**
     * Check user agent header to see if this is an IE browser (regardless of version.)
     *
     * @param request
     * @return true if this is an internet explorer browser, orherwise false.
     */
    public static boolean isInternetExplorer(HttpServletRequest request) {
        return request.getHeader("user-agent").contains("MSIE");
    }

    /**
     * Check if request is a confirmation request.
     *
     * @param request
     * @return True if the request contains a value for the parameter {@link com.zynap.talentstudio.web.common.ParameterConstants#CONFIRM_PARAMETER}
     */
    public static boolean isConfirmed(HttpServletRequest request) {
        String confirmation = request.getParameter(ParameterConstants.CONFIRM_PARAMETER);
        return StringUtils.hasText(confirmation);
    }

    /**
     * Remove query string from url.
     *
     * @param url The url
     * @return The url with no query string, or null
     */
    public static String removeQueryString(String url) {

        if (StringUtils.hasText(url)) {

            final int pos = url.indexOf(QUERY_STRING_SEPARATOR);
            if (pos != -1) {
                return url.substring(0, pos);
            } else {
                return url;
            }
        }

        return null;
    }

    /**
     * Get name of arena from request URI.
     * <br> Takes the text between the last "/" characters in the URI.
     *
     * @param requestURI
     * @return String
     */
    public static String getArenaName(String requestURI) {
        int end = requestURI.lastIndexOf(PATH_SEPARATOR);
        requestURI = requestURI.substring(0, end);
        int begin = requestURI.lastIndexOf(PATH_SEPARATOR);
        return requestURI.substring(begin + 1);
    }

    /**
     * Check if the specified arena is in the list.
     * <br> Used by {@link com.zynap.talentstudio.web.organisation.BrowseNodeController} to check which arena certain tabs and buttons can be displayed in.
     *
     * @param arenaNames The list of arena names
     * @param request    The request (to get the current arena name from)
     * @return Boolean.TRUE if the current arena name is contained in the list of arena names.
     */
    public static Boolean checkForArena(List arenaNames, HttpServletRequest request) {
        return new Boolean(arenaNames != null && arenaNames.indexOf(getArenaName(request.getRequestURI())) > -1);
    }

    /**
     * Get current request URI and remove context path.
     *
     * @param request
     * @return The current request URI without the context path, or null
     */
    public static String getRequestURIWithoutContextPath(HttpServletRequest request) {
        String url = null;

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        try {
            url = requestURI.substring(requestURI.indexOf(contextPath)).substring(contextPath.length());
        } catch (Exception e) {
            logger.error("Failed to get URI from request because of: " + e);
        }

        return url;
    }

    /**
     * Get referer and remove context path.
     *
     * @param request
     * @return The referer without the context path, or null
     */
    public static String getReferrerWithoutContextPath(HttpServletRequest request) {

        String url = null;

        String referer = getReferer(request);
        String contextPath = request.getContextPath();
        try {
            if (StringUtils.hasText(referer) && StringUtils.hasText(contextPath)) {
                url = referer.substring(referer.indexOf(contextPath)).substring(contextPath.length());
            }
        } catch (Exception e) {
            logger.error("Failed to get referer from request because of: " + e);
        }

        return url;
    }

    /**
     * Get the referer header from the request.
     *
     * @param request
     * @return The referer
     */
    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("referer");
    }

    /**
     * Get command object from session.
     * <br> Only currently works
     * for {@link com.zynap.talentstudio.web.common.DefaultWizardFormController}
     * and {@link com.zynap.talentstudio.web.controller.ZynapDefaultFormController} subclasses.
     *
     * @param request The HttpServletRequest
     * @return The session attribute used by the controllers - default name is {@link ControllerConstants#COMMAND_NAME}
     */
    public static final Object getCommand(HttpServletRequest request) {

        final String controllerClassName = (String) request.getSession().getAttribute(ParameterConstants.CONTROLLER_NAME);
        return getCommand(controllerClassName, request);
    }

    public static Object getCommand(final String controllerClassName, HttpServletRequest request) {
        if (StringUtils.hasText(controllerClassName)) {

            final String commandAttributeName = controllerClassName + ControllerConstants.COMMAND_OBJECT_NAME_SUFFIX;
            return request.getSession().getAttribute(commandAttributeName);
        } else {
            return null;
        }
    }

    /**
     * Check if the request is a get request.
     *
     * @param request The HttpServletRequest
     * @return True if this is a GET request
     */
    public static boolean isGetRequest(HttpServletRequest request) {
        return GET_METHOD.equals(request.getMethod());
    }

    /**
     * Check if the request is a post request.
     *
     * @param request The HttpServletRequest
     * @return True if this is a POST request
     */
    public static boolean isPostRequest(HttpServletRequest request) {
        return POST_METHOD.equals(request.getMethod());
    }

    /**
     * Get the user id from the {@link UserSession} bound in the current HttpSession.
     *
     * @param request The HttpRequest
     * @return The user id
     */
    public static Long getUserId(HttpServletRequest request) {
        final UserSession userSession = getUserSession(request);
        if(userSession == null) throw new InvalidSubmitException(request.getSession(), null, request.getRequestURI(), false, null);
        return userSession.getId();
    }

    /**
     * Get the home page for the logged in user for the current arena.
     *
     * @param request the request where the userPrincipal is stored.
     * @return the home page for the current arena and the current user.
     */
    public static HomePage getHomePage(HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        return userSession != null ? userSession.getHomePage() : null;
    }

    public static Group getUserGroup(HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        return userSession != null ? userSession.getUserGroup() : null;
    }

    /**
     * Get the user from the {@link UserSession} bound in the current HttpSession.
     *
     * @param request The HttpRequest
     * @return The user
     */
    public static User getUser(HttpServletRequest request) {
        return getUserSession(request).getUser();
    }

    public static boolean isMultiTenant(HttpServletRequest request) {
        return getUserSession(request).isMultiTenant();
    }

    /**
     * Get the parameter value.
     * <br> First, attempts to get it from the request as a parameter.
     * <br> If not found in the request, tries in the request as an attribute.
     * <br> Last, tries in the session as an attribute.
     * <br> If it is still not found returns null
     *
     * @param request       current HTTP request
     * @param parameterName The name of the parameter
     * @return The value of the content type parameter, or null
     */
    public static String getValue(HttpServletRequest request, String parameterName) {

        String parameterValue = request.getParameter(parameterName);

        if (!StringUtils.hasText(parameterValue)) {
            parameterValue = (String) request.getAttribute(parameterName);
        }

        if (!StringUtils.hasText(parameterValue)) {
            parameterValue = (String) WebUtils.getSessionAttribute(request, parameterName);
        }

        return StringUtils.hasText(parameterValue) ? parameterValue : null;
    }

    /**
     * Return a map containing all parameters with the given prefix.
     * Maps single values to String and multiple values to String array.
     * <br> Unlike the spring WebUtils method does not remove the prefix from the parameter name.
     * <br> Returns an empty Map if none found.
     *
     * @param request
     * @param prefix
     * @return Map
     */
    public static Map getParametersStartingWith(ServletRequest request, String prefix) {
        Enumeration parameterNames = request.getParameterNames();
        Map<String, Object> params = new HashMap<String, Object>();
        while (parameterNames != null && parameterNames.hasMoreElements()) {
            String paramName = (String) parameterNames.nextElement();
            if (paramName.startsWith(prefix)) {
                String[] values = request.getParameterValues(paramName);
                if(values == null) continue;
                if (values.length > 1) {
                    params.put(paramName, values);
                } else {
                    params.put(paramName, values[0]);
                }
            }
        }

        return params;
    }

    public static Map<String, Object> getParametersStartingWith(Map<String, Object> map, String prefix) {

        Map<String, Object> params = new HashMap<String, Object>();
        if (map == null) return params;
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final String key = entry.getKey();
            if(key.equals(prefix)) {
                params.put(key, entry.getValue());
            }
        }
        return params;
    }

    public static Map<String, Object> getParametersEndingWith(ServletRequest request, String suffix) {
        Map<String, Object> params = new HashMap<String, Object>();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames != null && parameterNames.hasMoreElements()) {
            String paramName = (String) parameterNames.nextElement();
            if (paramName.endsWith(suffix)) {
                String[] values = request.getParameterValues(paramName);
                if(values != null) params.put(paramName, values);                
            }
        }
        return params;
    }

    public static Map<String, Object> getParametersStartingWithEndingWith(HttpServletRequest request, String prefix, String suffix) {
        Map<String, Object> values = new HashMap<String, Object>();
        Map<String, Object> results = getParametersEndingWith(request, suffix);
        for (Map.Entry<String, Object> entry : results.entrySet()) {
            if(entry.getKey().startsWith(prefix)) {
                values.put(entry.getKey(), entry.getValue());
            }
        }
        return values;
    }

    /**
     * Bind the UserSession into the HttpSession.
     *
     * @param request     The HttpServletRequest
     * @param userSession The UserSession
     */
    public static void setUserSession(HttpServletRequest request, UserSession userSession) {
        request.getSession().setAttribute(SessionConstants.USER_SESSION, userSession);
        request.getSession().setAttribute(SessionConstants.USER_SESSION_ID, userSession.getUserSessionId());
        request.getSession().setAttribute(SessionConstants.USER_ID, userSession.getId());
    }

    /**
     * Get the UserSession from the HttpSession.
     *
     * @param request The HttpServletRequest
     * @return UserSession
     */
    public static UserSession getUserSession(HttpServletRequest request) {
        return (UserSession) WebUtils.getSessionAttribute(request, SessionConstants.USER_SESSION);
    }

    /**
     * Append query string to url.
     *
     * @param url     The url
     * @param request The HttpServletRequest
     * @return The url
     */
    public static String appendQueryString(String url, HttpServletRequest request) {
        return appendQueryString(url, request, null);
    }

    /**
     * Append query string to url excluding the specified parameter.
     *
     * @param url                The url
     * @param request            The HttpServletRequest
     * @param parameterToExclude The name of the parameter to exclude
     * @return The url
     */
    public static String appendQueryString(String url, HttpServletRequest request, String parameterToExclude) {

        String queryString = request.getQueryString();
        if (StringUtils.hasText(queryString)) {
            Map parameters = parseQueryString(queryString);

            if (StringUtils.hasText(parameterToExclude)) {
                parameters.remove(parameterToExclude);
            }

            return buildURL(url, parameters, false);
        } else {
            return url;
        }
    }

    /**
     * Parse query string into a map of parameters.
     *
     * @param queryString The query string
     * @return Map
     */
    public static Map parseQueryString(String queryString) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        if (StringUtils.hasText(queryString)) {
            StringTokenizer stringTokenizer = new StringTokenizer(queryString, PARAMETER_SEPARATOR);
            while (stringTokenizer.hasMoreTokens()) {
                String parameter = stringTokenizer.nextToken();

                int pos = parameter.indexOf(VALUE_SEPARATOR);
                if (pos >= 0) {
                    String name = parameter.substring(0, pos);
                    String value = parameter.substring(pos + 1);

                    parameters.put(name, value);
                }
            }
        }

        return parameters;
    }

    /**
     * Add context path to URL if not already present.
     *
     * @param request The HttpServletRequest
     * @param url     The url
     * @return The url, or the url with the context path added
     */
    public static String addContextPath(HttpServletRequest request, String url) {

        String contextPath = request.getContextPath();
        if (!url.startsWith(contextPath))
            url = contextPath + url;

        return url;
    }

    public static Long getUserOrgUnitId(HttpServletRequest request) {
        return getUserSession(request).getOrganisationUnitId();
    }

    public static String prependFullUrlPath(HttpServletRequest request, String url) {
        String path = addContextPath(request, url);
        // add the server path
        String server = request.getRemoteHost();
        String scheme = request.getScheme();
        String port = request.getServerPort() != 80 ? ":" + request.getServerPort() : "";
        path = scheme + "://" + server + port + path ;
        return path;
    }

    /**
     * Convenence method to build a URL with only 1 parameter.
     * <br/> Uses the "&" not the "&amp;" separator.
     *
     * @param url        The url
     * @param paramName
     * @param paramValue
     * @return The url
     */
    public static String buildURL(String url, String paramName, Object paramValue) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(paramName, paramValue);
        return buildURL(url, parameters, false);
    }

    /**
     * Build url with parameters as query string.
     * <br>Always encodes the parameter separator (&) into &amp;
     * <br/> Suitable for use in JSPs - if you use to build a URL for redirection the parameters will not be read
     * as servlet engines cannot split a request using the &amp; separator.
     *
     * @param url        The url
     * @param parameters The parameters
     * @return The url
     */
    public static String buildURL(String url, Map parameters) {
        return buildURL(url, parameters, true);
    }

    /**
     * Build url with parameters as query string.
     * <br/> The isUri parameter indicates which separator to use "&" or "&amp;".
     *
     * @param url        The url
     * @param parameters The parameters
     * @param isUri      Boolean indicating if URI being created and encoding should be performed.
     * @return The url
     */
    public static String buildURL(String url, Map parameters, boolean isUri) {

        StringBuffer stringBuffer = new StringBuffer();

        if (StringUtils.hasText(url)) {
            String paramSep = isUri ? ENCODED_PARAMETER_SEPARATOR : PARAMETER_SEPARATOR;
            stringBuffer.append(url);

            StringBuffer queryStringBuffer = new StringBuffer();
            for (Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String parameterName = (String) entry.getKey();
                if (StringUtils.hasText(parameterName) && url.indexOf(parameterName + VALUE_SEPARATOR) < 0) {
                    queryStringBuffer.append(parameterName);
                    Object parameterValue = entry.getValue();

                    if (parameterValue != null) {
                        queryStringBuffer.append(VALUE_SEPARATOR);
                        if (parameterValue instanceof String[]) {
                            queryStringBuffer.append(StringUtils.arrayToCommaDelimitedString((Object[]) parameterValue));
                        } else {
                            queryStringBuffer.append(parameterValue.toString());
                        }
                    }

                    if (iterator.hasNext()) {
                        queryStringBuffer.append(paramSep);
                    }
                }
            }

            if (queryStringBuffer.length() > 0) {
                int pos = url.indexOf(QUERY_STRING_SEPARATOR);
                if (pos == -1) {
                    stringBuffer.append(QUERY_STRING_SEPARATOR);
                } else {
                    stringBuffer.append(paramSep);
                }

                stringBuffer.append(queryStringBuffer.toString());
            }
        }

        return stringBuffer.toString();
    }

    /**
     * Get throwable from request (useful for error pages.)
     *
     * @param request The HttpRequest
     * @return The throwable, or null
     */
    public static Throwable getException(HttpServletRequest request) {
        return (Throwable) request.getAttribute(PageContext.EXCEPTION);
    }

    /**
     * Get throwable from request bound by Spring Framework (useful for error pages.)
     *
     * @param request The HttpRequest
     * @return The throwable, or null
     */
    public static Throwable getSpringException(HttpServletRequest request) {
        return (Throwable) request.getAttribute(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE);
    }

    public static final String PATH_SEPARATOR = "/";
    public static final String QUERY_STRING_SEPARATOR = "?";
    public static final String PARAMETER_SEPARATOR = "&";
    public static final String ENCODED_PARAMETER_SEPARATOR = "&amp;";
    public static final String VALUE_SEPARATOR = "=";

    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";
    /**
     * Constant for request attribute name for current URI.
     */
    private static final String CURRENT_REQUEST_URI_ATTR = "currentRequestURI";
    /**
     * Constant for request attribute name for previous URI.
     */
    private static final String PREVIOUS_URI_PARAM = "PREVIOUS_URI_VALUE";

}
