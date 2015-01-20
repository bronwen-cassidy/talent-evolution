/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.security;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.web.SessionConstants;
import com.zynap.domain.admin.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Jul-2008 17:01:04
 */
public class LoginDwrBean {

    public void loadUserPermits(HttpServletRequest request, Long userId) {
        try {
            userService.assignUserPermits(userId);
            final HttpSession session = request.getSession(false);
            if (session != null) {
                session.setAttribute(SessionConstants.PERMITS_DONE, Boolean.TRUE);
            }
        } catch (TalentStudioException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    public void loadUserPermits(String userName) {
        try {
            User user;
            try {
                user = userService.findByUserName(userName);
            } catch (TalentStudioException e) {
                // user not found nothing to do
                return;
            }
            if (user != null) {
                userService.assignUserPermits(user.getId());
            }
        } catch (TalentStudioException e) {
            logger.debug(e.getMessage());
        }
    }

    public void logOffUser(HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean checkPermitsLoaded(HttpServletRequest request) {
        if (request != null) {
            final HttpSession session = request.getSession(false);
            return session != null && session.getAttribute(SessionConstants.PERMITS_DONE) != null;
        }
        return false;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    private IUserService userService;
    private static final Log logger = LogFactory.getLog(LoginDwrBean.class);
}
