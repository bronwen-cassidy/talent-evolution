/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.account;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller that displays the user's own account - eg: username, first name, etc.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PersonalAccountController implements Controller {

    /**
     * Process the request.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @return a ModelAndView to render, or null if handled directly
     * @throws Exception in case of errors
     */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long id = ZynapWebUtils.getUserId(request);
        User user = (User) getUserService().findById(id);
        UserWrapperBean userWrapperBean = new UserWrapperBean(user);

        Map model = new HashMap();
        model.put(ControllerConstants.ARTEFACT, userWrapperBean);
        model.put(ControllerConstants.USER_ID, id);
        return new ModelAndView(MY_ACCOUNT_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    public IUserService getUserService() {
        return _userService;
    }

    public void setUserService(IUserService userService) {
        _userService = userService;
    }

    public IDynamicAttributeService getAttributeService() {
        return _dynamicAttributeService;
    }

    public void setAttributeService(IDynamicAttributeService dynamicAttributeService) {
        _dynamicAttributeService = dynamicAttributeService;
    }

    private IUserService _userService;
    private IDynamicAttributeService _dynamicAttributeService;

    static final String MY_ACCOUNT_VIEW = "viewmyaccount";
}
