/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.admin;

import com.zynap.domain.admin.User;
import com.zynap.exception.AlphaOnlyRuleException;
import com.zynap.exception.MaxLengthRuleException;
import com.zynap.exception.MinLengthRuleException;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.security.users.DuplicateUsernameException;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.controller.admin.BaseUserFormController;
import com.zynap.web.validation.admin.UserValidator;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class AddUserFormController extends BaseUserFormController {

    public AddUserFormController() {
    }

    /**
     * Validates passwords and sets the active flag.
     *
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {

        // validate password - core details have already been validated by this stage
        final UserValidator validator = (UserValidator) getValidator();
        validator.validatePassword(command, errors);

        UserWrapperBean userWrapperBean = (UserWrapperBean) command;
        userWrapperBean.setActive(RequestUtils.getBooleanParameter(request, ACTIVE_PARAM_KEY, false));
    }

    /**
     * This method is called before the form is shown and is used to populate the
     * request with information that the form may need.
     *
     * @param request
     * @return Map
     * @throws ServletException
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map<String, Object> refData = super.referenceData(request, command, errors);
        refData.put(SET_PASSWORD, Boolean.TRUE);
        return refData;
    }

    /**
     * Returns a UserWrapperBean containing the User.
     *
     * @param request
     * @return UserWrapperBean
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        UserWrapperBean wrapperBean = new UserWrapperBean(new User(), getRoleManager().getActiveAccessRoles());
        wrapperBean.setGroups(groupService.find(Group.TYPE_HOMEPAGE));
        return wrapperBean;
    }

    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        UserWrapperBean userWrapperBean = (UserWrapperBean) command;
        final User newUser = userWrapperBean.getModifiedUser();
        boolean success = false;
        try {
            getUserService().create(newUser);
            success = true;
        } catch (DuplicateUsernameException e) {
            errors.rejectValue("loginInfo.username", "error.duplicate.username", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage(), e);
            errors.rejectValue("loginInfo.username", "error.duplicate.username", e.getMessage());
        } catch (MinLengthRuleException e) {
            errors.rejectValue("loginInfo." + e.getOffender(), "error.minlength.rule.violated", e.getMessage());
        } catch (MaxLengthRuleException e) {
            errors.rejectValue("loginInfo." + e.getOffender(), "error.maxlength.rule.violated", e.getMessage());
        } catch (AlphaOnlyRuleException e) {
            errors.rejectValue("loginInfo." + e.getOffender(), "error.alpha.rule.violated", e.getMessage());
        }

        if (!success) {
            // Need to reset ids to ensure that persistence layer still sees them as new unsaved objects
            userWrapperBean.resetIds();
            // clear the password fields
            userWrapperBean.clearPasswordFields();
            return showForm(request, response, errors);
        }

        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.USER_ID, newUser.getId()));
    }

    public static final String SET_PASSWORD = "add";
}
