/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 05-Jun-2008 09:37:43
 * @version 0.1
 */
package com.zynap.talentstudio.web.security;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.web.history.CommandHistory;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import javax.servlet.http.HttpSession;

import java.util.List;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestSecurityInterceptor extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        securityInterceptor = (SecurityInterceptor) getBean("securityInterceptor");
    }

    protected String getDataSetFileName() throws Exception {
        return "security-test-data.xml";
    }

    public void testPreHandleAllowAccessUrl() throws Exception {
        String url = "/talentarena/homepage.htm";
        mockRequest.setServletPath(url);
        assertTrue(securityInterceptor.preHandle(mockRequest, mockResponse, null));
    }

    public void testPreHandleAccessPermit() throws Exception {
        IUserService userService = (IUserService) getBean("userService");
        IPermitManagerDao permitManagerDao = (IPermitManagerDao) getBean("permitManDao");
        List<IPermit> accessPermits = (List<IPermit>) permitManagerDao.getAccessPermits(ADMINISTRATOR_USER_ID);
        setUserSession((User) userService.findById(ADMINISTRATOR_USER_ID), mockRequest, accessPermits);
        String url = "/orgbuilder/addorganisation.htm";
        mockRequest.setServletPath(url);
        assertTrue(securityInterceptor.preHandle(mockRequest, mockResponse, null));
    }

    public void testPreHandleAccessPermitDenied() throws Exception {
        IUserService userService = (IUserService) getBean("userService");
        IPermitManagerDao permitManagerDao = (IPermitManagerDao) getBean("permitManDao");
        List<IPermit> accessPermits = (List<IPermit>) permitManagerDao.getAccessPermits(NO_ORG_BUILDER_USER_ID);
        setUserSession((User) userService.findById(NO_ORG_BUILDER_USER_ID), mockRequest, accessPermits);
        String url = "/orgbuilder/addorganisation.htm";
        mockRequest.setServletPath(url);
        assertFalse(securityInterceptor.preHandle(mockRequest, mockResponse, null));
    }

    public void testPreHandleDomainPermitsAllowed() throws Exception {
        IUserService userService = (IUserService) getBean("userService");
        IPermitManagerDao permitManagerDao = (IPermitManagerDao) getBean("permitManDao");
        List<IPermit> accessPermits = (List<IPermit>) permitManagerDao.getAccessPermits(BOSS_USER_ID);
        setUserSession((User) userService.findById(BOSS_USER_ID), mockRequest, accessPermits);
        // the boss viewing a subordinate (allowed)
        String url = "/orgbuilder/viewsubject.htm";
        mockRequest.setServletPath(url);
        mockRequest.addParameter("command.node.id", AGATHA_SUBJECT_ID.toString());
        MockSecurityService mockService = new MockSecurityService();
        mockService.setCheckAccess(true);
        securityInterceptor.setSecurityManager(mockService);
        assertTrue(securityInterceptor.preHandle(mockRequest, mockResponse, null));
    }

    public void testPreHandleDomainPermitsAllowedCommand() throws Exception {
        IUserService userService = (IUserService) getBean("userService");
        IPermitManagerDao permitManagerDao = (IPermitManagerDao) getBean("permitManDao");
        List<IPermit> accessPermits = (List<IPermit>) permitManagerDao.getAccessPermits(BOSS_USER_ID);
        setUserSession((User) userService.findById(BOSS_USER_ID), mockRequest, accessPermits);
        // the boss viewing a subordinate (allowed)
        String url = "/orgbuilder/viewsubject.htm";
        mockRequest.setServletPath(url);
        mockRequest.addParameter("history_back", "true");
        HttpSession mockSession = mockRequest.getSession();
        Subject command = new Subject(AGATHA_SUBJECT_ID, null);
        mockSession.setAttribute("command", command);
        CommandHistory history = new CommandHistory();
        history.saveCommand(url, command);
        mockSession.setAttribute("COMMAND_HISTORY", history);
        // we are not testing the domain permits
        MockSecurityService mockService = new MockSecurityService();
        mockService.setCheckAccess(true);
        securityInterceptor.setSecurityManager(mockService);
        //mockRequest.addParameter("command.node.id", AGATHA_SUBJECT_ID.toString());
        assertTrue(securityInterceptor.preHandle(mockRequest, mockResponse, null));
    }

    public void testPreHandleDomainPermitsNotAllowed() throws Exception {
        IUserService userService = (IUserService) getBean("userService");
        IPermitManagerDao permitManagerDao = (IPermitManagerDao) getBean("permitManDao");
        List<IPermit> accessPermits = (List<IPermit>) permitManagerDao.getAccessPermits(AGATHA_USER_ID);
        setUserSession((User) userService.findById(AGATHA_USER_ID), mockRequest, accessPermits);
        // the boss viewing a subordinate (allowed)
        String url = "/orgbuilder/viewsubject.htm";
        mockRequest.setServletPath(url);
        mockRequest.addParameter("command.node.id", BOSS_SUBJECT_ID.toString());
        MockSecurityService mockService = new MockSecurityService();
        mockService.setCheckAccess(false);
        securityInterceptor.setSecurityManager(mockService);
        assertFalse(securityInterceptor.preHandle(mockRequest, mockResponse, null));
    }

    private SecurityInterceptor securityInterceptor;
    private final Long NO_ORG_BUILDER_USER_ID = new Long(-134);
    private final Long BOSS_USER_ID = new Long(-132);
    private final Long BOSS_SUBJECT_ID = new Long(-32);
    private final Long AGATHA_USER_ID = new Long(-135);
    private final Long AGATHA_SUBJECT_ID = new Long(-35);
}