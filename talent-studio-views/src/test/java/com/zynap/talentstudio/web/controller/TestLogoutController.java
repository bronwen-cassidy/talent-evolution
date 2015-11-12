package com.zynap.talentstudio.web.controller;

/**
 * User: amark
 * Date: 18-Nov-2005
 * Time: 17:22:10
 */

import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

public class TestLogoutController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        logoutController = (LogoutController) applicationContext.getBean("logoutController");
    }

    public void testHandleRequest() throws Exception {

        // test that request is redirected correctly
        final ModelAndView modelAndView = logoutController.handleRequest(mockRequest, mockResponse);

        // model must be null
        assertNull(modelAndView);

        // should have a redirect
        assertEquals(mockRequest.getContextPath(), mockResponse.getRedirectedUrl());

        // check session has been invalidated
        final MockHttpSession session2 = (MockHttpSession) mockRequest.getSession(false);
        assertTrue(session2 == null);
    }

    LogoutController logoutController;
}