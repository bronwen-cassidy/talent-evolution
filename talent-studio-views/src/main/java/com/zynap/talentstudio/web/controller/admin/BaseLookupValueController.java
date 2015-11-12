package com.zynap.talentstudio.web.controller.admin;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

/**
 * User: amark
 * Date: 14-Jun-2005
 * Time: 14:14:22
 */
public abstract class BaseLookupValueController extends ZynapDefaultFormController {

    public ILookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    /**
     * Set active to false if the lookup value is a user defined one and the active parameter is not in the request.
     *
     * @param request
     * @param command
     * @param errors
     */
    public final void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) {
        LookupValue lookupValue = (LookupValue) command;
        if (!lookupValue.isSystem()) lookupValue.setActive(RequestUtils.getBooleanParameter(request, ParameterConstants.ACTIVE, false));
    }

    private ILookupManager lookupManager;
}