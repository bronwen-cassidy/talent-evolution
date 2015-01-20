package com.zynap.talentstudio.web.history;

import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.utils.tree.TreeWrapperBean;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * User: amark
 * Date: 26-Jul-2005
 * Time: 09:58:17
 */
public final class HistoryHelper {

    /**
     * Save the current request into history.
     * <br/> Saves the URL and its parameters and binds the token into the request.
     *
     * @param request
     */
    public static void save(HttpServletRequest request) {

        String requestURI = ZynapWebUtils.getCurrentURI(request);        
        save(request, requestURI);
    }

    public static void save(HttpServletRequest request, Map parameters) {

        String requestURI = ZynapWebUtils.getCurrentURI(request);
        save(request, requestURI, parameters);
    }

    /**
     * Save the current request into history.
     * <br/> Saves the URL and its parameters and binds the token into the request.
     *
     * @param request
     * @param url     the given url to save
     */
    public static void save(HttpServletRequest request, String url, Map parameters) {

        History history = getHistory(request);

        String token = getOrCreateToken(request);

        // remove any parameters referring to back or save requests
        final Map parameterMap = new HashMap(request.getParameterMap());
        parameterMap.putAll(parameters);
        parameterMap.remove(ParameterConstants.DISABLE_COMMAND_DELETION);
        parameterMap.remove(COMMAND_BACK_PARAM);

        history.addURL(token, new SavedURL(url, parameterMap));

        // bind token into request so it can be used by subsequent servlets, etc
        addTokenRequestAttribute(request, token);
    }

    /**
     * Save the current request into history.
     * <br/> Saves the URL and its parameters and binds the token into the request.
     *
     * @param request
     * @param url     the given url to save
     */
    public static void save(HttpServletRequest request, String url) {

        History history = getHistory(request);

        String token = getOrCreateToken(request);

        // remove any parameters referring to back or save requests
        final Map parameterMap = new HashMap(request.getParameterMap());
        parameterMap.remove(ParameterConstants.DISABLE_COMMAND_DELETION);
        parameterMap.remove(COMMAND_BACK_PARAM);

        history.addURL(token, new SavedURL(url, parameterMap));

        // bind token into request so it can be used by subsequent servlets, etc
        addTokenRequestAttribute(request, token);
    }

    /**
     * Is the request a request to go back using history.
     *
     * @param request
     * @return true or false
     */
    public static boolean isHistoryBackRequest(HttpServletRequest request) {
        return StringUtils.hasText(request.getParameter(COMMAND_BACK_PARAM));
    }

    /**
     * Build url to use for cancel / submit actions in controllers that use history.
     *
     * @param request
     * @return the previous URL with the parameter that indicates that this is a history back request appended.
     */
    public static String getBackURL(HttpServletRequest request) {
        final String previousURI = ZynapWebUtils.getPreviousURI(request);
        return appendHistoryBackParam(previousURI);
    }

    /**
     * Get current URL saved for history.
     *
     * @param request The HttpServletRequest
     * @return SavedURL
     */
    public static SavedURL getCurrentURL(HttpServletRequest request) {

        History history = getHistory(request);
        String token = getCurrentToken(request);
        return history.getURL(token);
    }

    public static SavedURL getLastUrl(HttpServletRequest request) {
        return getHistory(request).getLastUrl();
    }

    /**
     * Append parameter that indicates that this is a history back request to the URL.
     *
     * @param url
     * @return The url with the history back parameter appended
     */
    public static String appendHistoryBackParam(final String url) {
        return ZynapWebUtils.buildURL(url, COMMAND_BACK_PARAM, Boolean.TRUE);
    }

    /**
     * Add the history back parameter to a Map of parameters.
     * @param parameters
     */
    public static void appendHistoryBackParam(Map parameters) {
        parameters.put(HistoryHelper.COMMAND_BACK_PARAM, Boolean.TRUE);
    }

    /**
     * Save history - saves current request URI into request for later use
     * and saves command object if required.
     *
     * @param request
     */
    public static void saveHistory(HttpServletRequest request) {

        // check to see if the request was a "back" request and remove the last URL accordingly
        final History history = getHistory(request);
        if (isHistoryBackRequest(request)) {
            if (history != null) {
                final String currentToken = getCurrentToken(request);
                history.removeLastURL(currentToken);
            }
        }

        // save current uri in session so that tag libs can access this - required due to problems caused by Spring
        ZynapWebUtils.saveCurrentURI(request);

        // save command object if necessary
        if (needToSaveCommand(request)) {
            saveCommand(request);
        }

        if (ZynapWebUtils.checkURLChange(request)) {

            // remove the previous back url as it is no longer required
            if (history != null) {
                final String currentToken = getCurrentToken(request);
                final String currentRequestURI = ZynapWebUtils.getCurrentURI(request);
                history.removeLastURL(currentToken, currentRequestURI);
            }

            // remove command objects bound into request by Spring - do this regardless of saving command objects
            ZynapWebUtils.removeCommandFromSession(request);
        }
    }

    /**
     * Clear history.
     * <br/> Clears command history and token-based history.
     *
     * @param request
     */
    public static void clearHistory(HttpServletRequest request) {

        // only clear command history if not saving and not a back and url has changed
        if (ZynapWebUtils.checkURLChange(request)) {
            if (!needToSaveCommand(request) && !isHistoryBackRequest(request) && !needToLeaveCommand(request))
                getCommandHistory(request).reset();
        }

        // clear all history other than that for the current token
        final String token = getCurrentToken(request);
        getHistory(request).clear(token);
    }

    /**
     * Recover the command object of the specified class for the current URI.
     * <br/> Returns null if there was none, or if it was not an instance of the specified Class.
     *
     * @param request The HttpServletRequest
     * @param expectedClass The expected class
     * @return Object, or null
     */
    public static Object recoverCommand(HttpServletRequest request, Class expectedClass) {

        Object command = null;
        if (isHistoryBackRequest(request) || needToUpdateCommand(request)) {
            command = getCommandHistory(request).recoverCommand();
        }

        return command != null && command.getClass().isAssignableFrom(expectedClass) ? command : null;
    }

    /**
     * Get the last command object.
     *
     * @return the last command object, or null.
     */
    public static Object getLastCommand(HttpServletRequest request) {
        return getCommandHistory(request).getLastCommand();
    }

    public static String getSuccessViewURL(HttpServletRequest request) {

        // check to see if there are any objects in the command history first
        // if none found try the token-based history
        SavedURL lastURL = HistoryHelper.getLastURL(request);
        if (lastURL == null) {
            lastURL = HistoryHelper.getCurrentURL(request);
        }

        String url = null;
        Map parameters = new HashMap();
        if (lastURL != null) {
            url = lastURL.getURL();
            parameters = lastURL.getParameters();
        }

        // add back parameter
        HistoryHelper.appendHistoryBackParam(parameters);

        // append context path
        url = ZynapWebUtils.addContextPath(request, url);

        return ZynapWebUtils.buildURL(url, parameters);
    }

    /**
     * Get the last URL.
     *
     * @return the last URL, or null.
     */
    public static SavedURL getLastURL(HttpServletRequest request) {
        return getCommandHistory(request).getLastURL();
    }

    /**
     * Remove last URL if there is one.
     * @param request
     */
    public static void removeLastURL(HttpServletRequest request) {
        getCommandHistory(request).recoverCommand();
    }

    /**
     * Get the SavedURL at the specified index in the CommandHistory.
     *
     * @param index
     * @param request
     * @return the Url, or null.
     */
    public static SavedURL getCommandURL(int index, HttpServletRequest request) {
        return getCommandHistory(request).getSavedURL(index);
    }

    /**
     * Add the history token parameter to the url as part of the query string if present.
     *
     * @param request The HttpServletRequest
     * @param url The URL
     * @param isUri Is this a URI that the token is being appended to (Should encoding take place)
     * @return The URL
     */
    public static String addTokenToURL(HttpServletRequest request, String url, boolean isUri) {

        String token = getCurrentToken(request);
        if (StringUtils.hasText(token) && hasNoToken(url)) {

            Map parameters = new HashMap();
            parameters.put(TOKEN_KEY, token);

            url = ZynapWebUtils.buildURL(url, parameters, isUri);
        }

        return url;
    }

    /**
     * Get current token from request (either as attribute or parameter.)
     *
     * @param request The HttpServletRequest
     * @return The current token, or null of there isn't one
     */
    public static String getCurrentToken(ServletRequest request) {
        String token = (String) request.getAttribute(TOKEN_KEY);
        if (!StringUtils.hasText(token)) {
            token = request.getParameter(TOKEN_KEY);
        }

        return token;
    }

    /**
     * Get token request parameter and add it as an attribute.
     *
     * @param request The HttpServletRequest
     */
    public static void copyToken(HttpServletRequest request) {
        String token = request.getParameter(TOKEN_KEY);
        if (StringUtils.hasText(token)) {
            addTokenRequestAttribute(request, token);
        }
    }

    /**
     * Get the current token, if there isn't one create a new one.
     *
     * @param request
     * @return a history token
     */
    private static String getOrCreateToken(HttpServletRequest request) {

        String token = getCurrentToken(request);
        if (!StringUtils.hasText(token)) {
            token = getNewToken();
        }

        return token;
    }

    /**
     * Get a new token.
     *
     * @return A new token
     */
    private static String getNewToken() {
        return Integer.toString(random.nextInt());
    }

    /**
     * Does the request indicate that we need to update the command object.
     *
     * @param request
     * @return true or false
     */
    private static boolean needToUpdateCommand(HttpServletRequest request) {
        return ParameterConstants.UPDATE_COMMAND.equals(request.getParameter(ParameterConstants.DISABLE_COMMAND_DELETION));
    }

    /**
     * Does the request indicate that we need to save the command object.
     *
     * @param request
     * @return true or false
     */
    private static boolean needToSaveCommand(HttpServletRequest request) {
        return (ParameterConstants.SAVE_COMMAND.equals(request.getParameter(ParameterConstants.DISABLE_COMMAND_DELETION))
                && !isHistoryBackRequest(request) && !ZynapWebUtils.isWizardRequest(request)) || needToUpdateCommand(request);
    }

    /**
     * Does the request indicate that we need to leave the command object alone.
     *
     * @param request
     * @return true or false
     */
    private static boolean needToLeaveCommand(HttpServletRequest request) {
        return ParameterConstants.LEAVE_COMMAND.equals(request.getParameter(ParameterConstants.DISABLE_COMMAND_DELETION));
    }

    /**
     * Get or create instance of <code>com.zynap.talentstudio.web.history.CommandHistory</code> from session.
     *
     * @param request The HttpServletRequest
     * @return CommandHistory
     */
    private static CommandHistory getCommandHistory(HttpServletRequest request) {
        return (CommandHistory) WebUtils.getOrCreateSessionAttribute(request.getSession(), COMMAND_HISTORY, CommandHistory.class);
    }

    /**
     * Saves any command objects bound by Spring into the <code>com.zynap.talentstudio.web.history.CommandHistory</code>
     * object against the previous URI.
     *
     * @param request The HttpServletRequest
     */
    private static void saveCommand(HttpServletRequest request) {

        String previousUri = ZynapWebUtils.getPreviousURI(request);

        Object command = null;
        Enumeration atts = request.getSession().getAttributeNames();
        while (atts.hasMoreElements()) {
            String attName = (String) atts.nextElement();
            if (attName.indexOf(ControllerConstants.COMMAND_OBJECT_NAME_SUFFIX) > 0) {

                command = request.getSession().getAttribute(attName);
                if (!(command instanceof TreeWrapperBean)) {
                    getCommandHistory(request).saveCommand(previousUri, command);
                    break;
                }
            }
        }

        if (command != null) {
            Enumeration parameters = request.getParameterNames();
            while (parameters.hasMoreElements()) {
                String attName = (String) parameters.nextElement();
                if (attName.startsWith(ParameterConstants.PREFIX_COMMAND_PARAMETER)) {
                    try {
                        BeanUtils.setProperty(command, attName.substring(ParameterConstants.PREFIX_COMMAND_PARAMETER.length()), request.getParameter(attName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Get or create instance of <code>com.zynap.talentstudio.web.history.History</code> from session.
     *
     * @param request The HttpServletRequest
     * @return History
     */
    private static History getHistory(HttpServletRequest request) {
        return (History) WebUtils.getOrCreateSessionAttribute(request.getSession(), NAV_HISTORY, History.class);
    }

    /**
     * Check for history token parameter in url (querystring).
     *
     * @param url The URL
     * @return true if the url does not contain the history token parameter.
     */
    private static boolean hasNoToken(String url) {
        return StringUtils.countOccurrencesOf(url, TOKEN_KEY) == 0;
    }

    /**
     * Add the token to the request as an attribute.
     *
     * @param request The HttpServletRequest
     * @param token The token.
     */
    private static void addTokenRequestAttribute(HttpServletRequest request, String token) {
        request.setAttribute(TOKEN_KEY, token);
    }

    /**
     * Build redirect view with history back parameter appended.
     *
     * @param url
     * @return ZynapRedirectView
     */
    public static ZynapRedirectView buildRedirectView(final String url) {
        return new ZynapRedirectView(appendHistoryBackParam(url));
    }

    /**
     * An instance of <code>java.util.Random</code> used to generate the random token associated with the url stored in the history.
     */
    private static final Random random = new Random();

    /**
     * Constant for request parameter / attribute name for history token.
     */
    public static final String TOKEN_KEY = "historyToken";

    /**
     * request parameter that indicates that this is a back request.
     */
    public static final String COMMAND_BACK_PARAM = "history_back";

    /**
     * Constant for name of session attribute that holds the list of command objects stored.
     */
    private static final String COMMAND_HISTORY = "COMMAND_HISTORY";

    /**
     * Constant for session attribute name that holds the user's history.
     */
    private static final String NAV_HISTORY = "navHistory";
}
