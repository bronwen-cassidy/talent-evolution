/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.controller.admin;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class UserMultiController extends ZynapMultiActionController {

    public static Long getUserId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.USER_ID);
    }

    public ModelAndView viewUserDetailsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long userId = getUserId(request);
        User user = (User) getUserService().findById(userId);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(ControllerConstants.ARTEFACT, new UserWrapperBean(user));
        Collection<SecurityDomain> securityDomains = user.getSecurityDomains();
        securityDomains.size();
        model.put(ControllerConstants.USER_SECURITY_DOMAINS, securityDomains);
        model.put(ControllerConstants.SUPER_USER, ZynapWebUtils.isSuperUser(request));
        return new ModelAndView(DISPLAY_USER_VIEW, ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView deleteUserHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long userId = getUserId(request);
        User user = (User) getUserService().findById(userId);

        ModelAndView modelAndView;
        if (ZynapWebUtils.isConfirmed(request)) {
            try {
                getUserService().delete(user);
                return new ModelAndView(new ZynapRedirectView(LIST_USER_URL));
            } catch (DataIntegrityViolationException e) {
                logger.error(e);
                Map<String, Object> model = new HashMap<String, Object>();
                model.put(ControllerConstants.ARTEFACT, user);
                model.put(ControllerConstants.PREV_URL, cancelURL);
                model.put("dVError", "delete.user.error.message");
                return new ModelAndView(DELETE_USER_VIEW, ControllerConstants.MODEL_NAME, model);
            }
        } else if (ZynapWebUtils.isCancelled(request)) {
            modelAndView = new ModelAndView(new UserRedirectView(VIEW_USER_URL, user));
        } else {
            cancelURL = HistoryHelper.getBackURL(request);
            Map<String, Object> model = new HashMap<String, Object>();
            model.put(ControllerConstants.ARTEFACT, user);
            model.put(ControllerConstants.PREV_URL, cancelURL);
            modelAndView = new ModelAndView(DELETE_USER_VIEW, ControllerConstants.MODEL_NAME, model);
        }

        return modelAndView;
    }

    public ModelAndView deleteSubjectUserHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long userId = getUserId(request);
        User user = (User) getUserService().findById(userId);
        String previousURL = request.getParameter(ControllerConstants.PREV_URL);
        ModelAndView modelAndView;
        if (ZynapWebUtils.isConfirmed(request)) {
            try {
                getUserService().delete(user);
                return new ModelAndView(new UserRedirectView(previousURL, user));
            } catch (DataIntegrityViolationException e) {
                logger.error(e);
                Map<String, Object> model = new HashMap<String, Object>();
                model.put(ControllerConstants.ARTEFACT, user);
                model.put(ControllerConstants.PREV_URL, cancelURL);
                model.put("dVError", "delete.user.error.message");
                return new ModelAndView(DELETE_USER_VIEW, ControllerConstants.MODEL_NAME, model);
            }
        } else if (ZynapWebUtils.isCancelled(request)) {
            modelAndView = new ModelAndView(new UserRedirectView(cancelURL, user));
        } else {
            cancelURL = HistoryHelper.getBackURL(request);
            Map<String, Object> model = new HashMap<String, Object>();
            model.put(ControllerConstants.ARTEFACT, user);
            model.put(ControllerConstants.PREV_URL, cancelURL);
            modelAndView = new ModelAndView(DELETE_USER_VIEW, ControllerConstants.MODEL_NAME, model);
        }

        return modelAndView;
    }

    public IUserService getUserService() {
        return _userService;
    }

    public void setUserService(IUserService userService) {
        _userService = userService;
    }

    private IUserService _userService;

    private static final String LIST_USER_URL = "listuser.htm";
    private static final String VIEW_USER_URL = "viewuser.htm";

    private static final String DELETE_USER_VIEW = "confirmdeleteuser";
    private static final String DISPLAY_USER_VIEW = "viewuser";

    private String cancelURL;
    
}
