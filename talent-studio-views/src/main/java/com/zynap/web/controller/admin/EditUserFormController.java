/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.controller.admin;

import com.zynap.domain.admin.User;
import com.zynap.exception.AlphaOnlyRuleException;
import com.zynap.exception.MaxLengthRuleException;
import com.zynap.exception.MinLengthRuleException;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.security.users.DuplicateUsernameException;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditUserFormController extends BaseUserFormController {

    public EditUserFormController() {
    }

    /**
     * Sets the active flag, and clears all access roles if required.
     *
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {

        UserWrapperBean userWrapperBean = (UserWrapperBean) command;
        userWrapperBean.setActive(RequestUtils.getBooleanParameter(request, ACTIVE_PARAM_KEY, false));

        // handle the case where all roles are unchecked
        String accessRolesIds = request.getParameter("accessRoleIds");
        if (!StringUtils.hasText(accessRolesIds)) {
            userWrapperBean.clearRoles();
        }
    }

    /**
     * Returns a UserWrapperBean containing the User.
     *
     * @param request
     * @return UserWrapperBean
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));

        Long userId = UserMultiController.getUserId(request);
        User user = (User) getUserService().findById(userId);
        UserWrapperBean userWrapperBean = new UserWrapperBean(user, getRoleManager().getActiveAccessRoles());
        userWrapperBean.setGroups(groupService.find(Group.TYPE_HOMEPAGE));
        return userWrapperBean;
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        final UserWrapperBean userAccount = (UserWrapperBean) command;
        User modifiedUser = userAccount.getModifiedUser();

        boolean success = false;
        try {
            getUserService().update(modifiedUser);
            success = true;
        } catch (DuplicateUsernameException e) {
            errors.rejectValue("loginInfo.username", "error.duplicate.username", e.getMessage());
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

        return new ModelAndView(new UserRedirectView(getCancelView(), modifiedUser));
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) {

        final UserWrapperBean userWrapperBean = (UserWrapperBean) command;
        final User user = userWrapperBean.getModifiedUser();
        return new ModelAndView(new UserRedirectView(getCancelView(), user));
    }
}
