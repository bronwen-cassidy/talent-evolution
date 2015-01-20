package com.zynap.talentstudio.web.utils;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.ServletRequestBindingException;

import javax.servlet.http.HttpServletRequest;

/**
 * User: amark
 * Date: 26-May-2005
 * Time: 13:39:25
 */
public final class RequestUtils {

    /**
     * Get the value of the specified parameter as a {@link java.lang.Long}.
     *
     * @param request current HTTP request
     * @param name the name of the parameter
     * @return The parameter value as a Long, or null if the parameter could not be parsed or found.
     */
    public static Long getLongParameter(HttpServletRequest request, String name) {
        try {
            return ServletRequestUtils.getLongParameter(request, name);
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Get the value of the specified parameter as a {@link java.lang.Long}.
     * <br> Throws MissingRequestParameterException if the parameter was not present.
     *
     * @param request current HTTP request
     * @param name the name of the parameter
     * @return The parameter value as a Long, or null if the parameter could not be parsed or found.
     */
    public static Long getRequiredLongParameter(HttpServletRequest request, String name) {

        final Long longParameter = getLongParameter(request, name);

        if (longParameter == null) {
            throw new MissingRequestParameterException(name);
        } else {
            return longParameter;
        }
    }

    public static String getStringParameter(HttpServletRequest request, String label, String defaultValue) {
        return ServletRequestUtils.getStringParameter(request, label, defaultValue);
    }

    public static boolean getBooleanParameter(HttpServletRequest request, String name, boolean defaultValue) {
        return ServletRequestUtils.getBooleanParameter(request, name, defaultValue);
    }

    public static int getIntParameter(HttpServletRequest request, String selectedColumnIndex, int defaultValue) {
        return ServletRequestUtils.getIntParameter(request, selectedColumnIndex, defaultValue);
    }

    public static String getRequiredStringParameter(HttpServletRequest request, String name) throws ServletRequestBindingException {
        return ServletRequestUtils.getRequiredStringParameter(request, name);
    }

    public static Long getLongParameter(HttpServletRequest request, String name, Long defaultValue) throws ServletRequestBindingException {
        Long result = ServletRequestUtils.getLongParameter(request, name);
        return result != null ? result : defaultValue;
    }

    public static Long getLongParameter(HttpServletRequest request, String name, int defaultValue) {
        try {
            return getLongParameter(request, name, new Long(defaultValue));
        } catch (ServletRequestBindingException e) {
            return new Long(defaultValue);
        }
    }

    public static int getRequiredIntParameter(HttpServletRequest request, String name) throws ServletRequestBindingException {
        return ServletRequestUtils.getRequiredIntParameter(request, name);
    }

    public static String[] getStringParameters(HttpServletRequest request, String name) {
        return ServletRequestUtils.getStringParameters(request, name);
    }

    public static long[] getLongParameters(HttpServletRequest request, String name) {
        return ServletRequestUtils.getLongParameters(request, name);
    }

    public static Long[] getAllLongParameters(HttpServletRequest request, String name) {
        long[] longParameters = ServletRequestUtils.getLongParameters(request, name);
        Long[] params = new Long[longParameters.length];
        for (int i = 0; i < longParameters.length; i++) {
            params[i] = new Long(longParameters[i]);
        }
        return params;
    }
}
