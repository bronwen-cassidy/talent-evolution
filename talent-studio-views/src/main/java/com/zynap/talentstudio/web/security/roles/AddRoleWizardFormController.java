/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.roles;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.talentstudio.security.roles.AccessRole;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.ResourceRole;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.validation.admin.RoleValidator;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

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
public class AddRoleWizardFormController extends DefaultWizardFormController {

    public AddRoleWizardFormController() {
        setAllowDirtyBack(true);
    }

    /**
     * Return new RoleWrapper as backing object.s
     *
     * @param request
     * @return Object of type {@link RoleWrapperBean}
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {        
        return new RoleWrapperBean(null, null);
    }

    /**
     * Bind certain fields regardless.
     *
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
        RoleWrapperBean roleWrapperBean = (RoleWrapperBean) command;
        switch (getCurrentPage(request)) {
            case SELECT_ROLE_TYPE_IDX:
                String roleType = roleWrapperBean.getType();
                Role role;
                List activeRolePermits;
                if (Role.isResourceRole(roleType)) {
                    role = new ResourceRole();
                    activeRolePermits = getRoleManager().getActiveDomainObjectPermits();
                } else {
                    role = new AccessRole();
                    activeRolePermits = getRoleManager().getActiveAccessPermits();
                }
                roleWrapperBean.setRole(role);
                roleWrapperBean.setAllPermits(activeRolePermits);
                roleWrapperBean.initSelectedPermitIds();
                break;
            case ADD_ROLE_IDX:
                roleWrapperBean.setActive(RequestUtils.getBooleanParameter(request, ParameterConstants.ACTIVE, false));
                break;
            case ADD_PERMISSIONS_IDX:
                if (RequestUtils.getStringParameters(request, "selectedPermitIds").length == 0) roleWrapperBean.setSelectedPermitIds(new String[0]);
                break;
        }
    }

    /**
     * Callback for custom post-processing in terms of binding and validation.
     * <br> Currently sets role type on wrapper and gets active permits for permit assignment page.
     *
     * @param request current HTTP request
     * @param command bound command
     * @param errors Errors instance for additional custom validation
     * @param page current wizard page
     * @throws Exception
     */
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        RoleWrapperBean roleWrapperBean = (RoleWrapperBean) command;
        RoleValidator validator = (RoleValidator) getValidator();
        switch (page) {
            case ADD_ROLE_IDX:
                validator.validate(roleWrapperBean, errors);
                break;
        }
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        RoleWrapperBean roleWrapperBean = (RoleWrapperBean) command;
        String roleWizardPartMessage;

        switch (page) {
            case SELECT_ROLE_TYPE_IDX:
                roleWizardPartMessage = "roletype.";
                break;
            default:
                roleWizardPartMessage = (roleWrapperBean.isResourceRole() ? "resource." : "access.");
                break;
        }

        Map refData = new HashMap();
        refData.put(ControllerConstants.TITLE, "add.role.wizard." + roleWizardPartMessage + page);
        refData.put(ADDING, Boolean.TRUE);
        refData.put(NEXT_PAGE, String.valueOf(page + 1));
        refData.put(PREVIOUS_PAGE, String.valueOf(page - 1));
        return refData;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        final UserPrincipal principal = userSession.getUserPrincipal();
        RoleWrapperBean roleWrapperBean = (RoleWrapperBean) command;
        Role role = roleWrapperBean.getModifiedRole();

        // ensure that new role is not a system role
        role.setIsSystem(false);

        try {
            getRoleManager().create(role, principal.getUserId().intValue());
        } catch (DataAccessException e) {
            errors.rejectValue("label", "error.role.already.exists", e.getMessage());
            errors.reject(e.getMessage(), e.getMessage());
            return showPage(request, errors, ADD_ROLE_IDX);
        }

        return new ModelAndView("viewroleRedirect", ParameterConstants.ROLE_ID, role.getId());
    }

    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return new ModelAndView("listroleRedirect");
    }

    public IRoleManager getRoleManager() {
        return _roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        _roleManager = roleManager;
    }

    private IRoleManager _roleManager;
    private static final int ADD_ROLE_IDX = 1;
    private static final int ADD_PERMISSIONS_IDX = 2;
    private static final int SELECT_ROLE_TYPE_IDX = 0;
    private static final String ADDING = "adding";
    private static final String NEXT_PAGE = "nextPage";
    private static final String PREVIOUS_PAGE = "previousPage";
}
