package com.zynap.web.controller.admin;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

/**
 * User: amark
 * Date: 13-Jun-2005
 * Time: 17:05:04
 */
public abstract class BaseLookupTypeController extends ZynapDefaultFormController {

    public ILookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    /**
     * If this is a user defined lookup type, set active to false if the active parameter is not in the request.
     * 
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    public void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        LookupType lookupType = (LookupType) command;
        if (lookupType.isUserDefined()) lookupType.setActive(RequestUtils.getBooleanParameter(request, ParameterConstants.ACTIVE, false));
    }

    private ILookupManager lookupManager;
}
