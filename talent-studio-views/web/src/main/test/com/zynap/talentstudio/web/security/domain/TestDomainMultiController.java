package com.zynap.talentstudio.web.security.domain;

/**
 * User: amark
 * Date: 15-Mar-2005
 * Time: 11:34:13
 */

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.MissingRequestParameterException;

import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class TestDomainMultiController extends AbstractDomainControllerTest {

    public void setUp() throws Exception {
        super.setUp();

        domainMultiController = (DomainMultiController) applicationContext.getBean("securityDomainMultiController");
    }

    public void testListDomainHandler() throws Exception {
        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("securityLabel");
        newSecurityDomain.setActive(false);
        newSecurityDomain.setExclusive(true);
        final User user = new User(ADMINISTRATOR_USER_ID, "administrator", "admin", "strator");
        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername("administrator");
        loginInfo.setPassword("password");
        user.setLoginInfo(loginInfo);
        user.getSecurityDomains().add(newSecurityDomain);
        newSecurityDomain.addUser(user);

        securityManager.createDomain(newSecurityDomain);
        userService.update(user);

        User userDb=userService.getUserById(user.getId());
        Collection domains=userDb.getSecurityDomains();
        assertNotNull(domains);

        assertFalse(domains.isEmpty());
    }

    public void testSecurityDomainsInfoHandler() throws Exception {
        setUserSession(new Long(1), mockRequest);
        final ModelAndView modelAndView = domainMultiController.listDomainHandler(mockRequest, mockResponse);
        assertEquals("listsecuritydomain", modelAndView.getViewName());
        final Map model = getModel(modelAndView);
        final Collection domains = (Collection) model.get(DomainMultiController.SECURITY_DOMAINS);
        assertNotNull(domains);
        // there will always be 2 default domains installed
        assertFalse(domains.isEmpty());
    }

    public void testViewDomainHandler() throws Exception {

        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domainLabel");
        newSecurityDomain.setActive(false);
        newSecurityDomain.setExclusive(true);

        final Area newArea = new Area();
        newArea.setLabel("newAreaLabel");
        newSecurityDomain.setNode(newArea);

        // add root and all other users to security domain
        final User rootUser = (User) userService.findById(ROOT_USER_ID);
        newSecurityDomain.addUser(rootUser);

        final Collection appUsers = userService.getAppUsers();
        for (Iterator iterator = appUsers.iterator(); iterator.hasNext();) {
            User user = (User) iterator.next();
            newSecurityDomain.addUser(user);
        }


        securityManager.createDomain(newSecurityDomain);
        final Long domainId = newSecurityDomain.getId();

        mockRequest.addParameter(ParameterConstants.DOMAIN_ID, domainId.toString());
        final ModelAndView modelAndView = domainMultiController.viewDomainHandler(mockRequest, mockResponse);
        assertEquals("viewsecuritydomain", modelAndView.getViewName());

        final Map model = getModel(modelAndView);
        final SecurityDomain foundDomain = (SecurityDomain) model.get(DomainMultiController.SECURITY_DOMAIN);
        assertEquals(newSecurityDomain, foundDomain);

        // check that root user is not in collection of users - number of users should be exactly the number of app users
        final Collection users = (Collection) model.get(ControllerConstants.USERS);
        assertNotNull(users);
        assertFalse(users.contains(rootUser));
        assertEquals(appUsers.size(), users.size());
    }

    public void testViewDomainHandlerNoId() throws Exception {
        try {
            domainMultiController.viewDomainHandler(mockRequest, mockResponse);
            fail("View domain with no id provided succeeded");
        } catch (MissingRequestParameterException expected) {

        }
    }

    public void testDeleteDomainHandler() throws Exception {

        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domainLabel");
        newSecurityDomain.setActive(false);
        newSecurityDomain.setExclusive(true);

        final Area newArea = new Area();
        newArea.setLabel("newAreaLabel");
        newSecurityDomain.setNode(newArea);

        securityManager.createDomain(newSecurityDomain);
        final Long domainId = newSecurityDomain.getId();

        // try without confirming deletion
        mockRequest.addParameter(ParameterConstants.DOMAIN_ID, domainId.toString());
        ModelAndView modelAndView = domainMultiController.deleteDomainHandler(mockRequest, mockResponse);
        assertEquals("confirmdeletesecuritydomain", modelAndView.getViewName());

        // check that request model contains the security domain
        Map model = getModel(modelAndView);
        final SecurityDomain securityDomain = (SecurityDomain) model.get(DomainMultiController.SECURITY_DOMAIN);
        assertEquals(newSecurityDomain, securityDomain);
        assertNotNull(securityDomain.getId());
        assertNotNull(securityDomain.getLabel());

        // confirm and try - should work this time
        mockRequest.addParameter(ParameterConstants.CONFIRM_PARAMETER, Boolean.TRUE.toString());
        modelAndView = domainMultiController.deleteDomainHandler(mockRequest, mockResponse);
        assertEquals("listsecuritydomainRedirect", modelAndView.getViewName());

        // check that there is no model upon success
        model = getModel(modelAndView);
        assertNull(model);

        try {
            securityManager.findDomain(domainId);
            fail("Incorrectly found deleted domain");
        } catch (DomainObjectNotFoundException expected) {

        }
    }

    public void testDeleteDomainHandlerNoId() throws Exception {
        try {

            mockRequest.addParameter(ParameterConstants.CONFIRM_PARAMETER, Boolean.TRUE.toString());
            domainMultiController.deleteDomainHandler(mockRequest, mockResponse);
            fail("Delete domain with no id provided succeeded");
        } catch (MissingRequestParameterException expected) {

        }
    }

    private DomainMultiController domainMultiController;
}