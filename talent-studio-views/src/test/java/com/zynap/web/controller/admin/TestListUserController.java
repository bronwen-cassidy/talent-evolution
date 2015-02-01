/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.controller.admin;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.UserSearchQuery;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.security.admin.UserSearchQueryWrapper;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestListUserController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        listUserController = (ListUserController) applicationContext.getBean("listUserController");
        wrapper = new UserSearchQueryWrapper(new UserSearchQuery());

        UserPrincipal principal = getSysUserPrincipal();
        UserSession userSession = new UserSession(principal, getArenaMenuHandler());
        ZynapWebUtils.setUserSession(mockRequest, userSession);
    }

    public void testFormBackingObject() throws Exception {
        Object backingObject = listUserController.formBackingObject(mockRequest);
        assertTrue(backingObject instanceof UserSearchQueryWrapper);
    }

    public void testOnSubmitNoSearchParam() throws Exception {
        mockRequest.addParameter(ParameterConstants.SEARCH_STARTED_PARAM, "NO");
        ModelAndView result = listUserController.onSubmitInternal(mockRequest, mockResponse, wrapper, new BindException(wrapper, ControllerConstants.COMMAND_NAME));
        assertNotNull(result.getModel().get(ControllerConstants.USERS));
    }

    public void testOnSubmitSearchParam() throws Exception {
        mockRequest.addParameter(ParameterConstants.SEARCH_STARTED_PARAM, "YES");
        wrapper.setFirstName("sys");
        wrapper.setActive("active");
        ModelAndView result = listUserController.onSubmitInternal(mockRequest, mockResponse, wrapper, new BindException(wrapper, ControllerConstants.COMMAND_NAME));
        Collection results = (Collection) result.getModel().get(ControllerConstants.USERS);
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    ListUserController listUserController;
    UserSearchQueryWrapper wrapper;
}