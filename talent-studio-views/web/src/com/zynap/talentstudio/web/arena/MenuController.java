/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.arena;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MenuController implements Controller {

    /**
     * Gets the menu section id from the request and get the menu items for the section to be displayed in the JSP.
     *
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserSession userSession = ZynapWebUtils.getUserSession(request);

        String menuId = getMenuSectionId(request);
        if (StringUtils.hasText(menuId)) {
            userSession.setCurrentMenuSectionId(menuId);
        }

        Collection menuItems = userSession.getMenuItems();
        return new ModelAndView("menu", "menuitems", menuItems);
    }

    /**
     * Get menu section id from request.
     * <br> First checks the request parameters, if not found checks the request attributes.
     *
     * @param request
     * @return the menu section id or null
     */
    private String getMenuSectionId(HttpServletRequest request) {
        String menuId = request.getParameter(ParameterConstants.MENU_PARAM) != null
                ? request.getParameter(ParameterConstants.MENU_PARAM)
                : (String) request.getAttribute(ParameterConstants.MENU_PARAM);

        return menuId;
    }
}
