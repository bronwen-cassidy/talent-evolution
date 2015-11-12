/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.session;

import com.zynap.talentstudio.web.listener.ZynapSessionListener;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 10-May-2006 11:58:31
 */
public class ZynapSessionAttributeListener implements HttpSessionAttributeListener {

    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        final String message = " has been added to the session ";
        logState(httpSessionBindingEvent, message);
    }

    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
        final String message = " has been removed from the session ";
        logState(httpSessionBindingEvent, message);
    }

    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
        final String message = " has been replaced in the session ";
        logState(httpSessionBindingEvent, message);
    }

    private void logState(HttpSessionBindingEvent httpSessionBindingEvent, String message) {
        final Object value = httpSessionBindingEvent.getValue();
        if(value instanceof RunReportWrapperBean) {
            logger.error(MARKER + value.toString() + message + MARKER);
        }
    }

    private final static Log logger = LogFactory.getLog(ZynapSessionListener.class);
    private static final String MARKER = "\n*************************************************************************\n";
}
