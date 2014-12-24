/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web;

import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.web.utils.ResponseUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HomePageViewController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomePage homePage = ZynapWebUtils.getHomePage(request);

        if (homePage != null) {
            if (homePage.getData() != null) {
                ResponseUtils.writeToResponse(response, request, "uploadedHomePage.html", homePage.getData(), false);
            }
        }
        return null;
    }
}
