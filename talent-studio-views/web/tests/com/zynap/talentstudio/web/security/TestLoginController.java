/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security;

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.arenas.IArenaMenuHandler;
import com.zynap.talentstudio.security.users.IUserService;

import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestLoginController extends ZynapMockControllerTest {

    public TestLoginController() {
    }

    protected void setUp() throws Exception {
        super.setUp();
        /*
        <property name="commandClass" value="com.zynap.domain.admin.LoginInfo"/>
        <property name="validator" ref="loginValidator"/>
        <property name="formView" value="login"/>
        <property name="userService" ref="userService"/>
        <property name="menuHandler" ref="arenaMenuHandler"/>
        */
        loginController =  new LoginController();
        loginController.setCommandClass(LoginInfo.class);
        loginController.setValidator((Validator) getBean("loginValidator"));
        loginController.setUserService((IUserService) getBean("userService"));
        loginController.setMenuHandler((IArenaMenuHandler) getBean("arenaMenuHandler"));
        loginController.setFormView("login");

    }

    public void testOnSubmitOK() throws Exception {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(ROOT_USERNAME);
        loginInfo.setPassword(ROOT_PASSWORD);
        BindException errors = new BindException(loginInfo, ControllerConstants.COMMAND_NAME);
        ModelAndView view = loginController.onSubmit(mockRequest, mockResponse, loginInfo, errors);

        // check that there are no errors and that the view goes to the correct place and that the user session has been set correctly
        assertFalse(errors.hasErrors());
        assertNull(view);
        assertEquals(mockRequest.getContextPath(), mockResponse.getRedirectedUrl());
        assertNotNull(ZynapWebUtils.getUserSession(mockRequest));
    }

    public void testOnSubmitForcePasswordChange() throws Exception {

        final String username = "hahahaha";
        final String password = "gobble";

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        User user = new User(loginInfo, new CoreDetail("Mr", "Benny", "Bunting"));
        loginController.getUserService().create(user);

        // reset login info as the other one has the encrypted password now
        loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        // check that user is redirected to change password view and that user id and user name and force password attributes are in the view
        BindException errors = new BindException(loginInfo, ControllerConstants.COMMAND_NAME);
        ModelAndView view = loginController.onSubmit(mockRequest, mockResponse, loginInfo, errors);
        assertEquals(LoginController.CHANGE_PWD_VIEW_NAME, view.getViewName());

        final Map model = view.getModel();
        assertEquals(user.getId(), model.get(ControllerConstants.USER_ID));
        assertEquals(username, model.get(ControllerConstants.USER_NAME));
        assertEquals(Boolean.TRUE, model.get(ControllerConstants.FORCE_PWD_CHANGE));
    }

    public void testOnSubmitPasswordExpired() throws Exception {

        final String username = ROOT_USERNAME;
        final String password = ROOT_PASSWORD;

        User user = loginController.getUserService().findByUserName(username);

        // set expires to something daft
        LoginInfo loginInfo = user.getLoginInfo();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -30);
        loginInfo.setExpires(calendar.getTime());
        loginInfo.setForcePasswordChange(false);
        loginController.getUserService().update(user);

        LoginInfo newLoginInfo = new LoginInfo();
        newLoginInfo.setUsername(username);
        newLoginInfo.setPassword(password);

        // check that user is redirected to change password view and that user id and user name attributes are in the view
        // however check that the 
        BindException errors = new BindException(newLoginInfo, ControllerConstants.COMMAND_NAME);
        ModelAndView view = loginController.onSubmit(mockRequest, mockResponse, newLoginInfo, errors);
        assertEquals(LoginController.CHANGE_PWD_VIEW_NAME, view.getViewName());

        final Map model = view.getModel();
        assertEquals(user.getId(), model.get(ControllerConstants.USER_ID));
        assertEquals(user.getLoginInfo().getUsername(), model.get(ControllerConstants.USER_NAME));
        assertNull(model.get(ControllerConstants.FORCE_PWD_CHANGE));
    }

    public void testInactiveUserLogin() throws Exception {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername("benny1");
        loginInfo.setPassword("benny1");

        User user = new User(loginInfo, new CoreDetail("Mr", "Benny", "Bunting"));
        user.setActive(false);
        loginController.getUserService().create(user);

        // new users are required to change their pwd
        assertTrue(loginInfo.getForcePasswordChange());

        // the user is inactive so they cannot login
        BindException errors = new BindException(loginInfo, ControllerConstants.COMMAND_NAME);
        ModelAndView view = loginController.onSubmit(mockRequest, mockResponse, loginInfo, errors);

        // check that user is presented with the form page and that there are errors
        assertEquals(loginController.getFormView(), view.getViewName());
        assertEquals(1, errors.getGlobalErrorCount());

        // check that password was reset
        assertNull(loginInfo.getPassword());
    }

    public void testFormBackingObject() throws Exception {
        Object backingObject = loginController.formBackingObject(mockRequest);
        assertNotNull(backingObject);
        assertTrue(backingObject instanceof LoginInfo);
    }

    private LoginController loginController;
}