/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.roles;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
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
public class EditRoleWizardFormController extends DefaultWizardFormController {

    public EditRoleWizardFormController() {
        setAllowDirtyBack(true);
    }

    /**
     * Form backing object consists of an empty subject object except for the default
     * dynamic attributePreferences.
     *
     * @param request
     * @return Object of type {@link RoleWrapperBean}
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long roleId = RoleMultiController.getRoleId(request);
        Role role = getRoleManager().findRole(roleId);
        List activeRolePermits = role.isResourceRole() ? getRoleManager().getActiveDomainObjectPermits() : getRoleManager().getActiveAccessPermits();
        return new RoleWrapperBean(role, activeRolePermits);
    }

    /**
     * Create a reference data map for the given request, consisting of
     * bean name/bean instance pairs as expected by ModelAndView.
     * <p/>
     * Subclasses can override this to set reference data used in the view.</p>
     *
     * @param request current HTTP request
     * @param command form object with request parameters bound onto it
     * @param errors  validation errors holder
     * @param page    current wizard page
     * @return Map with reference data entries, or null if none
     * @throws Exception in case of invalid state or arguments
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        RoleWrapperBean roleWrapperBean = (RoleWrapperBean) command;

        Map<String, Object> refData = new HashMap<String, Object>();
        String message = "edit.role.wizard." + (roleWrapperBean.isResourceRole() ? "resource." : "access.");        
        refData.put(ControllerConstants.TITLE, message + page);
        refData.put(ADDING, Boolean.FALSE);
        refData.put(NEXT_PAGE, String.valueOf(page + 1));
        refData.put(PREVIOUS_PAGE, String.valueOf(page - 1));
        return refData;
    }

    /**
     * Bind values regardless of back.
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
        RoleWrapperBean roleWrapperBean = (RoleWrapperBean) command;

        switch (getCurrentPage(request)) {
            case EDIT_ROLE_IDX:
                roleWrapperBean.setActive(RequestUtils.getBooleanParameter(request, ParameterConstants.ACTIVE, false));
                break;
            case EDIT_PERMISSIONS_IDX:
                if (RequestUtils.getStringParameters(request, "selectedPermitIds").length == 0) roleWrapperBean.setSelectedPermitIds(new String[0]);
                break;
        }
    }

    /**
     * Validate only if not going back.
     * @param request
     * @param command
     * @param errors
     * @param page
     * @throws Exception
     */
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        RoleWrapperBean roleWrapperBean = (RoleWrapperBean) command;
        RoleValidator validator = (RoleValidator) getValidator();
        switch (page) {
            case EDIT_ROLE_IDX:
                validator.validate(roleWrapperBean, errors);
                break;
        }
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        final UserPrincipal principal = userSession.getUserPrincipal();
        RoleWrapperBean roleWrapperBean = (RoleWrapperBean) command;
        Role role = roleWrapperBean.getModifiedRole();

        try {
            getRoleManager().update(role, principal.getUserId().intValue());
        } catch (DataAccessException e) {
            errors.rejectValue("label", "error.role.already.exists", e.getMessage());
            errors.reject(e.getMessage(), e.getMessage());
            return showPage(request, errors, EDIT_ROLE_IDX);
        }

        return new ModelAndView("viewroleRedirect", ParameterConstants.ROLE_ID, role.getId());
    }

    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ZynapRedirectView view = new ZynapRedirectView(getCancelView());
        view.addStaticAttribute(ParameterConstants.ROLE_ID, ((RoleWrapperBean) command).getId());

        return new ModelAndView(view);
    }

    public IRoleManager getRoleManager() {
        return _roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        _roleManager = roleManager;
    }

    private IRoleManager _roleManager;
    private static final int EDIT_ROLE_IDX = 0;
    private static final int EDIT_PERMISSIONS_IDX = 1;

    private static final String ADDING = "adding";
    private static final String NEXT_PAGE = "nextPage";
    private static final String PREVIOUS_PAGE = "previousPage";
}
