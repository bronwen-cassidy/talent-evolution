/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.web.listener;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.web.SessionConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * HttpSessionListener implementation that records when a user's session ended.
 *
 * @author Andreas Andersson
 * @since 17/03/2004
 */
public class ZynapSessionListener implements HttpSessionListener {


    /**
     * The logger.
     */
    private final static Log logger = LogFactory.getLog(ZynapSessionListener.class);

    /**
     * Method invoked when a session is created.
     *
     * @param event The HttpSessionEvent
     */
    public void sessionCreated(HttpSessionEvent event) {
        final String sessionId = event.getSession().getId();
        logger.debug("Session created: " + sessionId);
    }

    /**
     * Method invoked when a session is destroyed.
     * <br> Records when the user's session ended.
     *
     * @param event The HttpSessionEvent
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        final HttpSession session = event.getSession();
        //Long id = (Long) session.getAttribute(SessionConstants.USER_SESSION_ID);
        Long userId = (Long) session.getAttribute(SessionConstants.USER_ID);
        String sessionId = session.getId();
        logger.debug("Session destroyed: " + sessionId);

        try {
            getUserService(event).logOutUser(userId);
            getQuestionnaireService(event).unlockQuestionnaires(userId);
        } catch (Exception e) {
            logger.error("session update exception occuring in sessionDestroyed", e);
        }
    }

    private IUserService getUserService(HttpSessionEvent event) {
        WebApplicationContext context = getWebApplicationContext(event);
        return (IUserService) context.getBean("userService");
    }

    private WebApplicationContext getWebApplicationContext(HttpSessionEvent event) {
        try {
            return WebApplicationContextUtils.getRequiredWebApplicationContext(event.getSession().getServletContext());
        } catch (IllegalStateException e) {
            logger.error("WebApplicationContext failed to load due to " + e.getMessage());
            throw e;
        }
    }

    private IQuestionnaireService getQuestionnaireService(HttpSessionEvent event) {
        WebApplicationContext context = getWebApplicationContext(event);
        return (IQuestionnaireService) context.getBean("questionnaireService");    
    }
}