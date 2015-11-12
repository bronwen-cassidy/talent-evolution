package com.zynap.talentstudio.web.controller.admin;

/**
 * User: amark
 * Date: 25-Apr-2005
 * Time: 15:58:45
 */

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;
import com.zynap.talentstudio.web.security.admin.AddUserFormController;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.security.roles.AccessRole;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.util.collections.DomainObjectCollectionHelper;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

public class TestEditUserFormController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        editUserFormController = (EditUserFormController) applicationContext.getBean("editUserController");
    }

    public void testReferenceData() throws Exception {
        mockRequest.addParameter(ParameterConstants.ARTEFACT_ID, ADMINISTRATOR_USER_ID.toString());
        UserWrapperBean bean = (UserWrapperBean) editUserFormController.formBackingObject(mockRequest);
        Map refData = editUserFormController.referenceData(mockRequest, bean, null);
        assertNotNull(refData.get(ControllerConstants.TITLES));
        assertNull(refData.get(AddUserFormController.SET_PASSWORD));
    }

    public void testOnSubmit() throws Exception {

        // test that you can modify the administrator
        mockRequest.addParameter(ParameterConstants.USER_ID, ADMINISTRATOR_USER_ID.toString());
        UserWrapperBean userWrapperBean = (UserWrapperBean) editUserFormController.formBackingObject(mockRequest);
        assertFalse(userWrapperBean.isRoot());

        final String newUsername = "testusername";
        userWrapperBean.getLoginInfo().setUsername(newUsername);
        userWrapperBean.setActive(false);

        // check that are redirected to success view and that they have the user id in the view and that there are no errors
        Errors errors = getErrors(userWrapperBean);
        final ModelAndView modelAndView = editUserFormController.onSubmitInternal(mockRequest, mockResponse, userWrapperBean, errors);

        final ZynapRedirectView redirectView = getRedirectView(modelAndView);
        assertEquals(editUserFormController.getSuccessView(), redirectView.getUrl());
        assertEquals(ADMINISTRATOR_USER_ID, redirectView.getStaticAttributes().get(ParameterConstants.USER_ID));
        assertFalse(errors.hasErrors());

        // check that user object has been modified
        userWrapperBean = (UserWrapperBean) editUserFormController.formBackingObject(mockRequest);
        assertFalse(userWrapperBean.isActive());
        assertEquals(newUsername, userWrapperBean.getLoginInfo().getUsername());
    }

    public void testOnBindAndValidate() throws Exception {

        // check that if the parameters for active and for roles are not in the request that the user is disabled
        // and the roles cleared
        mockRequest.addParameter(ParameterConstants.USER_ID, ADMINISTRATOR_USER_ID.toString());
        UserWrapperBean userWrapperBean = (UserWrapperBean) editUserFormController.formBackingObject(mockRequest);

        editUserFormController.onBindAndValidateInternal(mockRequest, userWrapperBean, getErrors(userWrapperBean));
        User modifiedUser = userWrapperBean.getModifiedUser();
        AccessRole homeRole = (AccessRole) DomainObjectCollectionHelper.findById(userWrapperBean.getAccessRoles(), Role.HOME_ROLE_ID);

        assertFalse(modifiedUser.isActive());
        assertTrue(modifiedUser.getUserRoles().size() == 1);
        assertTrue(modifiedUser.getUserRoles().contains(homeRole));

        // add user active parameter and check that user is now active
        mockRequest.addParameter(EditUserFormController.ACTIVE_PARAM_KEY, Boolean.TRUE.toString());
        editUserFormController.onBindAndValidateInternal(mockRequest, userWrapperBean, getErrors(userWrapperBean));
        modifiedUser = userWrapperBean.getModifiedUser();
        assertTrue(modifiedUser.isActive());
    }

    public void testFormBackingObject() throws Exception {

        // check that form backing object returns the expected user
        mockRequest.addParameter(ParameterConstants.USER_ID, ADMINISTRATOR_USER_ID.toString());
        UserWrapperBean userWrapperBean = (UserWrapperBean) editUserFormController.formBackingObject(mockRequest);
        assertEquals(ADMINISTRATOR_USER_ID, userWrapperBean.getId());

        // check that the list of access roles in the wrapper contains all the active access roles
        // by subtracting them from each other and checking that none are left
        final List<Role> activeAccessRoles = editUserFormController.getRoleManager().getActiveAccessRoles();
        activeAccessRoles.removeAll(userWrapperBean.getAccessRoles());
        assertTrue(activeAccessRoles.isEmpty());
    }

    private EditUserFormController editUserFormController;
}