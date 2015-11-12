/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.controller.admin;

import com.zynap.talentstudio.common.groups.IGroupService;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

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
public abstract class BaseUserFormController extends ZynapDefaultFormController {

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        List titles = getLookupManager().findActiveLookupValues(ILookupManager.LOOKUP_TYPE_TITLE);
        refData.put(ControllerConstants.TITLES, titles);
        refData.put(ControllerConstants.SUPER_USER, ZynapWebUtils.isSuperUser(request));
        return refData;
    }

    public ILookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public IRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    private ILookupManager lookupManager;
    private IUserService userService;
    private IRoleManager roleManager;
    protected IGroupService groupService;

    public static final String ACTIVE_PARAM_KEY = "active";
}
