/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

import com.zynap.domain.UserSession;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserPassword;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.arenas.ArenaMenuHandler;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.web.validation.ChangePwdValidator;

import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Iterator;

public class TestChangePwdController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {

        super.setUp();
        
        changePwdValidator = (ChangePwdValidator) applicationContext.getBean("changePwdValidator");
        changePwdController = new ChangePwdController();
        changePwdController.setCommandClass(UserPassword.class);
        changePwdController.setValidator(changePwdValidator);
        changePwdController.setFormView("changepwd");
        changePwdController.setCancelView("login.htm");
        changePwdController.setUserService((IUserService) applicationContext.getBean("userService"));
        changePwdController.setMenuHandler((ArenaMenuHandler) applicationContext.getBean("arenaMenuHandler"));

        IRoleManager roleManager = (IRoleManager) applicationContext.getBean("roleManager");

        // create user for testing purposes
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(USER_NAME);
        loginInfo.setPassword(PASSWORD);

        CoreDetail coreDetail = new CoreDetail("Mrs", "Bandera", "Nadara");

        Collection roles = roleManager.getActiveAccessRoles();
        testUser = new User(loginInfo, coreDetail);
        for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
            Role role = (Role) iterator.next();
            testUser.addRole(role);
        }

        userService = (IUserService) applicationContext.getBean("userService");
        userService.create(testUser);
    }

    public void testOnSubmit() throws Exception {

        // set force password to false to make sure that change pwd is accepted
        testUser.getLoginInfo().setForcePasswordChange(false);
        userService.update(testUser);

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(USER_NAME);
        loginInfo.setPassword(PASSWORD);

        UserPassword userPassword = new UserPassword();
        userPassword.setUserId(testUser.getId());
        Errors errors = populatePassword(userPassword, USER_NAME, PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
        ModelAndView view = changePwdController.onSubmit(mockRequest, mockResponse, userPassword, (BindException) errors);

        // check that user is redirected to root of webapp
        assertNull(view);
        assertEquals(mockRequest.getContextPath(), mockResponse.getRedirectedUrl());

        // check that user session is created correctly
        UserSession userSession = ZynapWebUtils.getUserSession(mockRequest);
        assertNotNull(userSession);
        assertEquals(testUser.getId(), userSession.getId());
    }

    /**
     * Test change pwd for new user.
     * <br> New users are obliged to change the password but do not have any password history so the validation is different.
     *
     * @throws Exception
     */
    public void testNewUser() throws Exception {

        assertTrue(testUser.getLoginInfo().getForcePasswordChange());

        // set force password to false to make sure that change pwd is accepted
        testUser.getLoginInfo().setForcePasswordChange(false);
        userService.update(testUser);

        mockRequest.addParameter(ControllerConstants.USER_ID, testUser.getId().toString());
        mockRequest.addParameter(ControllerConstants.USER_NAME, testUser.getLoginInfo().getUsername());
        UserPassword userPassword = (UserPassword) changePwdController.formBackingObject(mockRequest);
        assertEquals(USER_NAME, userPassword.getUsername());
        assertEquals(testUser.getId(), userPassword.getUserId());

        // should be no binding errors
        Errors errors = populatePassword(userPassword, USER_NAME, PASSWORD, PASSWORD, PASSWORD);
        assertFalse(errors.hasErrors());

        // should now have errors as old password cannot be the same as new password if user has no password history
        ModelAndView view = changePwdController.onSubmit(mockRequest, mockResponse, userPassword, (BindException) errors);
        assertTrue(errors.hasFieldErrors(NEW_PWD_FIELD_NAME));
        assertEquals(changePwdController.getFormView(), view.getViewName());

        errors = populatePassword(userPassword, USER_NAME, PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
        assertFalse(errors.hasErrors());
        view = changePwdController.onSubmit(mockRequest, mockResponse, userPassword, (BindException) errors);
        assertNull(view);
        assertEquals(mockRequest.getContextPath(), mockResponse.getRedirectedUrl());

        // change password again - should fail as we are using the same password again
        errors = populatePassword(userPassword, USER_NAME, NEW_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
        view = changePwdController.onSubmit(mockRequest, mockResponse, userPassword, (BindException) errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors(NEW_PWD_FIELD_NAME));
        assertEquals(changePwdController.getFormView(), view.getViewName());

        // use a 3rd password - should work
        errors = populatePassword(userPassword, USER_NAME, NEW_PASSWORD, NEW_PASSWORD2, NEW_PASSWORD2);
        assertFalse(errors.hasErrors());
        mockResponse.setCommitted(false);
        view = changePwdController.onSubmit(mockRequest, mockResponse, userPassword, (BindException) errors);
        assertFalse(errors.hasErrors());

        // check that user is redirected to root of webapp        
        assertNull(view);
        assertEquals(mockRequest.getContextPath(), mockResponse.getRedirectedUrl());
    }

    /**
     * Get the form backing object and check that its details are correct.
     *
     * @throws Exception
     */
    public void testFormBackingObject() throws Exception {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(USER_NAME);
        loginInfo.setPassword(PASSWORD);

        mockRequest.addParameter(ControllerConstants.USER_ID, testUser.getId().toString());
        mockRequest.addParameter(ControllerConstants.USER_NAME, testUser.getLoginInfo().getUsername());
        UserPassword userPassword = (UserPassword) changePwdController.formBackingObject(mockRequest);
        assertEquals(USER_NAME, userPassword.getUsername());
        assertEquals(testUser.getId(), userPassword.getUserId());
    }

    private Errors populatePassword(UserPassword pwdObject, String userName, String oldPassword, String newPassword, String repeatedPassword) {

        DataBinder binder = new DataBinder(pwdObject, ControllerConstants.COMMAND_NAME);
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue(USERNAME_FIELD_NAME, userName);
        values.addPropertyValue(OLD_PWD_FIELD_NAME, oldPassword);
        values.addPropertyValue(NEW_PWD_FIELD_NAME, newPassword);
        values.addPropertyValue(RPT_NEW_PWD_FIELD_NAME, repeatedPassword);
        binder.bind(values);
        BindingResult errors = binder.getBindingResult();
        changePwdValidator.validate(pwdObject, errors);
        try {
            binder.close();
        } catch (BindException e) {
            e.printStackTrace();
            fail("Should not have got a binding exception");
        }
        return new BindException(errors);
    }

    private ChangePwdController changePwdController;
    private ChangePwdValidator changePwdValidator;
    private IUserService userService;
    private User testUser;

    private static final String USER_NAME = "pincher3";
    private static final String PASSWORD = "pincher3";
    private static final String NEW_PASSWORD = "bandalos7";
    private static final String NEW_PASSWORD2 = "bandalos8";

    private static final String USERNAME_FIELD_NAME = "username";
    private static final String OLD_PWD_FIELD_NAME = "oldPassword";
    private static final String NEW_PWD_FIELD_NAME = "newPassword";
    private static final String RPT_NEW_PWD_FIELD_NAME = "newPasswordAgain";
}
