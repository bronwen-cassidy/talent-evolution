package com.zynap.talentstudio.web.security;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import org.springframework.mock.web.MockHttpSession;

public class TestLoginInterceptor extends ZynapMockControllerTest {

    protected void setUp() throws Exception {

        super.setUp();

        UserSessionFactory.setUserSession(null);
        mockRequest.setServletPath("test.htm");

        loginInterceptor = new LoginInterceptor();
    }

    public void testPreHandle() throws Exception {

        // test without UserSession - must fail (indicating that user must login) but request is a GET so it will be saved
        mockRequest.setMethod("GET");
        boolean success = loginInterceptor.preHandle(mockRequest, mockResponse, null);
        assertEquals("LoginInterceptor did not reject request without UserSession", false, success);

        // check that saved URL was bound into the session
        String savedURL = (String) mockRequest.getSession().getAttribute(LoginInterceptor.SAVED_URL);
        assertNotNull(savedURL);

        // check that UserSession has not been set in UserSessionFactory
        try {
            UserSessionFactory.getUserSession();
            fail("UserSession incorrectly set in UserSessionFactory");
        } catch (IllegalStateException expected) {

        }

        // set UserSession - should now allow the request
        mockRequest.setSession(new MockHttpSession());
        UserSession newUserSession = new UserSession(getAdminUserPrincipal(), getArenaMenuHandler());
        ZynapWebUtils.setUserSession(mockRequest, newUserSession);
        success = loginInterceptor.preHandle(mockRequest, mockResponse, null);
        assertEquals("LoginInterceptor incorrectly rejected a request with a UserSession", true, success);

        // check that UserSession is set in UserSessionFactory
        UserSession userSession = UserSessionFactory.getUserSession();
        assertEquals(newUserSession, userSession);

        savedURL = (String) mockRequest.getSession().getAttribute(LoginInterceptor.SAVED_URL);
        assertNull(savedURL);
    }

    public void testPreHandleWithPost() throws Exception {

        // test without UserSession - must fail (indicating that user must login) but request is a POST so it will not be saved
        mockRequest.setMethod("POST");
        boolean success = loginInterceptor.preHandle(mockRequest, mockResponse, null);
        assertEquals("LoginInterceptor did not reject request without UserSession", false, success);

        // check that saved URL was not bound into the session
        String savedURL = (String) mockRequest.getSession().getAttribute(LoginInterceptor.SAVED_URL);
        assertNull(savedURL);
    }

    public void testPostHandle() throws Exception {

        mockRequest.setMethod("GET");
        assertFalse(loginInterceptor.preHandle(mockRequest, mockResponse, null));

        // check that saved URL was bound into the session
        String savedURL = (String) mockRequest.getSession().getAttribute(LoginInterceptor.SAVED_URL);
        assertNotNull(savedURL);

        // check that saved url is still bound into the sessions
        loginInterceptor.postHandle(mockRequest, mockResponse, null, null);
        String postHandleURL = (String) mockRequest.getSession().getAttribute(LoginInterceptor.SAVED_URL);
        assertNotNull(savedURL);
        assertEquals(savedURL, postHandleURL);

        // check that UserSession is now null in UserSessionFactory
        try {
            UserSessionFactory.getUserSession();
            fail("UserSession not reset in UserSessionFactory");
        } catch (IllegalStateException expected) {

        }
    }

    private LoginInterceptor loginInterceptor;
}