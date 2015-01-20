package com.zynap.talentstudio.web.security.admin;

/**
 * User: amark
 * Date: 14-Mar-2005
 * Time: 13:44:30
 * Test for {@link com.zynap.web.controller.admin.UserMultiController}
 */

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.MissingRequestParameterException;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.web.controller.admin.UserMultiController;

import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class TestUserMultiController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        _userMultiController = (UserMultiController) applicationContext.getBean("multiUserController");

        UserPrincipal principal = getAdminUserPrincipal();
        UserSession userSession = new UserSession(principal, getArenaMenuHandler());
        ZynapWebUtils.setUserSession(mockRequest, userSession);
    }

    /**
     * Test what the controller does when an id that does not correspond to any user is provided when attempting to load a user's details.
     *
     * @throws Exception
     */
    public void testViewUserDetailsHandlerInvalidId() throws Exception {

        try {
            mockRequest.addParameter(ParameterConstants.USER_ID, "-999");
            _userMultiController.viewUserDetailsHandler(mockRequest, mockResponse);
            fail("Attempt to view user with invalid id provided succeeded");
        } catch (DomainObjectNotFoundException expected) {

        }
    }

    /**
     * Test what the controller does when no id is provided when attempting to load a user's details.
     *
     * @throws Exception
     */
    public void testViewUserDetailsHandlerNoId() throws Exception {
        try {
            _userMultiController.viewUserDetailsHandler(mockRequest, mockResponse);
            fail("Attempt to view user with no id provided succeeded");
        } catch (MissingRequestParameterException expected) {
            // expected as no id was supplied in the request
        }
    }

    /**
     * Test what the controller does when an id that does not correspond to any user is provided when attempting to delete a user.
     *
     * @throws Exception
     */
    public void testDeleteUserHandlerInvalidId() throws Exception {

        try {
            mockRequest.addParameter(ParameterConstants.USER_ID, "-999");
            _userMultiController.deleteUserHandler(mockRequest, mockResponse);
            fail("Attempt to delete user with invalid id provided succeeded");
        } catch (DomainObjectNotFoundException expected) {

        }
    }

    /**
     * Test what the controller does when no id is provided when attempting to delete a user.
     *
     * @throws Exception
     */
    public void testDeleteUserHandlerNoId() throws Exception {
        try {
            _userMultiController.deleteUserHandler(mockRequest, mockResponse);
            fail("Attempt to delete user with no id provided succeeded");
        } catch (MissingRequestParameterException expected) {
            // expected as no id was supplied in the request
        }
    }

    public void testDeleteUserHandler() throws Exception {

        // add a user
        AddUserFormController _addUserFormController = (AddUserFormController) applicationContext.getBean("addUserFormController");
        UserWrapperBean userWrapperBean = (UserWrapperBean) _addUserFormController.formBackingObject(mockRequest);
        userWrapperBean.setTitle("Mr");
        userWrapperBean.setFirstName("Fred");
        userWrapperBean.setSecondName("Bloggs");
        userWrapperBean.setPrefGivenName("Ralph");

        final LoginInfo loginInfo = userWrapperBean.getLoginInfo();
        final String password = "freddie";
        final String username = "freddie";
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);
        loginInfo.setRepeatedPassword(password);

        Validator _userValidator = (Validator) applicationContext.getBean("userValidator");
        final BindException errors = new BindException(userWrapperBean, "user");
        _userValidator.validate(userWrapperBean, errors);
        assertFalse(errors.hasErrors());
        _addUserFormController.onSubmit(mockRequest, mockResponse, userWrapperBean, errors);

        // delete the user - check that are redirected to confirmation view first and that the user is in the model
        final User newUser = userWrapperBean.getModifiedUser();
        final Long userId = userWrapperBean.getId();
        mockRequest.addParameter(ParameterConstants.USER_ID, userId.toString());
        final ModelAndView modelAndView = _userMultiController.deleteUserHandler(mockRequest, mockResponse);
        assertEquals("confirmdeleteuser", modelAndView.getViewName());
        final Map modelAttrs = getModel(modelAndView);
        assertEquals(newUser, modelAttrs.get(ControllerConstants.ARTEFACT));

        // set the confirmation parameter and try again - check that the view was as expected
        mockRequest.addParameter(ParameterConstants.CONFIRM_PARAMETER, Boolean.TRUE.toString());
        final ModelAndView successModelAndView = _userMultiController.deleteUserHandler(mockRequest, mockResponse);
        assertEquals("listuser.htm", getRedirectView(successModelAndView).getUrl());

        // try to find the user - expect an exception here as the user no longer exists
        try {
            _userMultiController.viewUserDetailsHandler(mockRequest, mockResponse);
            fail("Attempt to find deleted user succeeded");
        } catch (DomainObjectNotFoundException expected) {

        }
    }

    UserMultiController _userMultiController;
}