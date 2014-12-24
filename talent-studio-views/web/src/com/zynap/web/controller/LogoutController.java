/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class LogoutController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // clear any locked questionnaires for this user
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect(request.getContextPath());
        return null;
    }
}
