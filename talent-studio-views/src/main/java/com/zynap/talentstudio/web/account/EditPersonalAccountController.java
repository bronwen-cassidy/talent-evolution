/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.account;

import com.zynap.domain.admin.User;
import com.zynap.exception.AlphaOnlyRuleException;
import com.zynap.exception.MaxLengthRuleException;
import com.zynap.exception.MinLengthRuleException;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.security.users.IUserService;

import com.zynap.web.controller.ZynapDefaultFormController;

import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller that displays the user's own account - eg: username, first name, etc so it can be edited.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditPersonalAccountController extends ZynapDefaultFormController {

    /**
     * Get the form backing object.
     *
     * @param request
     * @return UserWrapperBean
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long id = ZynapWebUtils.getUserId(request);

        UserWrapperBean userWrapperBean;
        User user = (User) getUserService().findById(id);
        userWrapperBean = new UserWrapperBean(user);

        setFormView(EDIT_MY_ACCOUNT_VIEW);

        return userWrapperBean;
    }

    /**
     * Get list of titles required by view.
     *
     * @param request current HTTP request
     * @return Map containing globally used reference data
     */
    protected Map referenceData(HttpServletRequest request) {

        Map<String, Object> refData = new HashMap<String, Object>();
        List titles = getLookupManager().findActiveLookupValues(ILookupManager.LOOKUP_TYPE_TITLE);
        refData.put(ControllerConstants.TITLES, titles);

        return refData;
    }

    /**
     * Process the request.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param command The form backing object
     * @param errors The errors
     * @return a ModelAndView to render, or null if handled directly
     * @throws Exception in case of errors
     */
    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        UserWrapperBean userWrapperBean = (UserWrapperBean) command;

        boolean success = false;
        try {

            User user = userWrapperBean.getModifiedUser();
            getUserService().update(user);

            success = true;
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("loginInfo.username", "error.duplicate.username", e.getMessage());
        } catch (MinLengthRuleException e) {
            errors.rejectValue("loginInfo." + e.getOffender(), "error.minlength.rule.violated", e.getMessage());
        } catch (MaxLengthRuleException e) {
            errors.rejectValue("loginInfo." + e.getOffender(), "error.maxlength.rule.violated", e.getMessage());
        } catch (AlphaOnlyRuleException e) {
            errors.rejectValue("loginInfo." + e.getOffender(), "error.alpha.rule.violated", e.getMessage());
        }

        if (!success) {
            return showForm(request, response, errors);
        }

        return new ModelAndView(new ZynapRedirectView(getSuccessView()));
    }

	public ILookupManager getLookupManager() {
        return _lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        _lookupManager = lookupManager;
    }

    public IDynamicAttributeService getDynamicAttributeService() {
        return _dynamicAttributeService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        _dynamicAttributeService = dynamicAttributeService;
    }

    public IUserService getUserService() {
        return _userService;
    }

    public void setUserService(IUserService userService) {
        _userService = userService;
    }

    private IDynamicAttributeService _dynamicAttributeService;
    private IUserService _userService;
    private ILookupManager _lookupManager;

	public static final String EDIT_MY_ACCOUNT_VIEW = "editmyaccount";
}
