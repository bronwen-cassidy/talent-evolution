/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils.controller;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.beans.ZynapCustomBooleanEditor;
import com.zynap.talentstudio.web.beans.ZynapCustomDoubleEditor;
import com.zynap.talentstudio.web.beans.ZynapCustomIntegerEditor;
import com.zynap.talentstudio.web.beans.ZynapCustomLongEditor;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ControllerUtils {

    /**
     * Register common custom editors for binder.
     *
     * @param binder The ServletRequestDataBinder
     */
    public static void registerCommonEditors(ServletRequestDataBinder binder) {

        // register custom binders
        binder.registerCustomEditor(boolean.class, new ZynapCustomBooleanEditor());
        binder.registerCustomEditor(Integer.class, new ZynapCustomIntegerEditor());
        binder.registerCustomEditor(Long.class, new ZynapCustomLongEditor());
        binder.registerCustomEditor(Double.class, new ZynapCustomDoubleEditor());
    }

    /**
     * Is this a search form submission.
     *
     * @param request
     * @return True if the request contains contains the {@link ParameterConstants#SEARCH_STARTED_PARAM} or the {@link ParameterConstants#HAS_RESULTS} parameter.
     */
    public static boolean isSearchFormSubmission(HttpServletRequest request) {
        return (StringUtils.hasText(request.getParameter(ParameterConstants.HAS_RESULTS)) || isSearchStarted(request));
    }

    /**
     * If this request the start of a search.
	 * Returns True if the request contains the {@link ParameterConstants#SEARCH_STARTED_PARAM} parameter
	 * and the value equals "yes" (case insensitive)
     *
     * @param request
     * @return boolean
     */
    public static boolean isSearchStarted(HttpServletRequest request) {
        String searchInitiated = request.getParameter(ParameterConstants.SEARCH_STARTED_PARAM);
        return (StringUtils.hasText(searchInitiated) && searchInitiated.equalsIgnoreCase("yes"));
    }

    public static Integer extractIndex(String name) {
        return new Integer(String.valueOf(name.substring(name.indexOf("[") + 1, name.indexOf("]"))));
    }

}
