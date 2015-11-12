/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper tag for creating a new tab section.
 * The defined tags object is put up in the request scope
 *
 * @author Andreas Andersson
 * @since 08/03/2004
 */
public class TabHelper {

    public static PageTabs createTabs(HttpServletRequest req, String defaultTab, String pageUrl, String tabName, boolean javascript) {

        String url = createUrl(req, pageUrl, javascript);
        PageTabs tabs = new PageTabs(url, tabName);

        String tabParameter = req.getParameter(tabName);
        if (!javascript && StringUtils.hasText(tabParameter)) {
            tabs.setActiveTab(tabParameter);
        } else {
            tabs.setActiveTab(defaultTab);
        }

        return tabs;
    }

    public static String createUrl(HttpServletRequest req, String pageUrl, boolean javascript) {

        String url;
        if (StringUtils.hasText(pageUrl)) {
            url = pageUrl;
        } else {
            url = req.getRequestURI();
        }
        if (!javascript) {
            // append query string but ensure that you exclude the ParameterConstants.TAB parameter.
            url = ZynapWebUtils.appendQueryString(url, req, ParameterConstants.TAB);
            return HistoryHelper.addTokenToURL(req, url, true);
        }
        else {
            return url;
        }
    }


}
