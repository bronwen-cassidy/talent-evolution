/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.roles;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class RoleMultiController extends ZynapMultiActionController {

    public static final Long getRoleId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.ROLE_ID);
    }

    public IRoleManager getRoleManager() {
        return _roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        _roleManager = roleManager;
    }


    /**
     * Get the Role data based on the role id,
     * or the Users who have the Role based on the value of the <code>com.zynap.talentstudio.web.SessionConstants.TAB</code> request parameter.
     *
     * @param request
     * @param response
     * @return ModelAndView
     * @throws javax.servlet.ServletException
     */
    public ModelAndView viewRoleInfoHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long id = getRoleId(request);
        Map model = new HashMap();
        Role role = getRoleManager().findRole(id);
        List activeRolePermits = (role.isResourceRole() ? getRoleManager().getActiveDomainObjectPermits() : getRoleManager().getActiveAccessPermits());
        RoleWrapperBean roleWrapper = new RoleWrapperBean(role, activeRolePermits);
        model.put(ROLE, roleWrapper);
        return new ModelAndView("viewrole", ControllerConstants.MODEL_NAME, model);
    }

    /**
     * Get all roles (active and inactive.)
     *
     * @param request
     * @param response
     * @return ModelAndView
     * @throws javax.servlet.ServletException
     */
    public ModelAndView listRolesHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        List roles = getRoleManager().getAllRoles();

        Map model = new HashMap();
        model.put(ROLES, roles);
        return new ModelAndView("listrole", ControllerConstants.MODEL_NAME, model);
    }

    /**
     * Disable the selected role.
     *
     * @param request
     * @param response
     * @return ModelAndView
     * @throws ServletRequestBindingException
     * @throws TalentStudioException
     */
    public ModelAndView deleteRoleHandler(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, TalentStudioException {

        Long roleId = getRoleId(request);
        int principalId = (ZynapWebUtils.getUserSession(request)).getId().intValue();

        Map model = new HashMap();
        model.put(ParameterConstants.ROLE_ID, roleId);
        model.put(ControllerConstants.LABEL, getRoleManager().findRole(roleId).getLabel());

        if (!ZynapWebUtils.isConfirmed(request)) {
            return new ModelAndView("confirmdeleterole", ControllerConstants.MODEL_NAME, model);
        }

        getRoleManager().disable(roleId, principalId);

        return new ModelAndView("listroleRedirect", ControllerConstants.MODEL_NAME, model);
    }

    public IUserService getUserService() {
        return _userService;
    }

    public void setUserService(IUserService userService) {
        _userService = userService;
    }

    private IRoleManager _roleManager;
    private IUserService _userService;
    public static final String ROLES = "roles";
    public static final String ROLE = "role";
}
