package com.zynap.talentstudio.web.security.domain;

/**
 * User: amark
 * Date: 20-Mar-2005
 * Time: 12:40:20
 */

import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.domain.UserSession;
import com.zynap.domain.UserPrincipal;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

public class TestEditDomainController extends AbstractDomainControllerTest {

    public void setUp() throws Exception {
        super.setUp();

        editDomainController = (EditDomainController) applicationContext.getBean("editSecurityDomainController");

        newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domainLabel");
        newSecurityDomain.setActive(false);
        newSecurityDomain.setExclusive(true);

        final Area newArea = new Area();
        newArea.setLabel("newAreaLabel");
        newSecurityDomain.setArea(newArea);

        securityManager.createDomain(newSecurityDomain);
        setUserSession(new UserSession(new UserPrincipal(ROOT_USER, new ArrayList())), mockRequest);
    }

    public void testFormBackingObject() throws Exception {
        mockRequest.addParameter(ParameterConstants.DOMAIN_ID, newSecurityDomain.getId().toString());
        final SecurityDomainWrapperBean formBackingObject = (SecurityDomainWrapperBean) editDomainController.formBackingObject(mockRequest);
        assertNotNull(formBackingObject);
        assertEquals(newSecurityDomain.getId(), formBackingObject.getId());
    }

    public void testProcessFinish() throws Exception {

        final SecurityDomainWrapperBean securityDomainWrapperBean = new SecurityDomainWrapperBean(newSecurityDomain);        
        final BindException errors = getErrors(editDomainController, securityDomainWrapperBean);

        final ModelAndView modelAndView = editDomainController.processFinish(mockRequest, mockResponse, securityDomainWrapperBean, errors);

        // check view is correct
        final ZynapRedirectView view = getRedirectView(modelAndView);
        assertEquals(editDomainController.getSuccessView(), view.getUrl());

        // check that static attribute with new domain id is present
        final Long domainId = (Long) view.getStaticAttributes().get(ParameterConstants.DOMAIN_ID);
        assertEquals(securityDomainWrapperBean.getModifiedSecurityDomain().getId(), domainId);

    }

    public void testCommandNameAndType() {
        assertEquals(ControllerConstants.COMMAND_NAME, editDomainController.getCommandName());
        assertEquals(SecurityDomainWrapperBean.class, editDomainController.getCommandClass());
    }

    public void testProcessCancel() throws Exception {

        final SecurityDomainWrapperBean securityDomainWrapperBean = new SecurityDomainWrapperBean(newSecurityDomain);
        final BindException errors = getErrors(editDomainController, securityDomainWrapperBean);

        final ModelAndView modelAndView = editDomainController.processCancel(mockRequest, mockResponse, securityDomainWrapperBean, errors);

        // check view is correct
        final ZynapRedirectView view = getRedirectView(modelAndView);
        assertEquals(editDomainController.getSuccessView(), view.getUrl());

        // check that static attribute with new domain id is present
        final Long domainId = (Long) view.getStaticAttributes().get(ParameterConstants.DOMAIN_ID);
        assertEquals(securityDomainWrapperBean.getModifiedSecurityDomain().getId(), domainId);
    }

    /**
     * Check that leaving the active and exclusive fields blank means that they are set to false.
     * @throws Exception
     */
    public void testOnBindAndValidate() throws Exception {

        final SecurityDomainWrapperBean securityDomainWrapperBean = new SecurityDomainWrapperBean(newSecurityDomain);
        securityDomainWrapperBean.setActive(true);
        securityDomainWrapperBean.setExclusive(true);

        final BindException errors = getErrors(editDomainController, securityDomainWrapperBean);
        mockRequest.setAttribute(editDomainController.getClass().getName() + ".PAGE." + "command", new Integer(BaseDomainController.CORE_VIEW_IDX));
        editDomainController.onBind(mockRequest, securityDomainWrapperBean, errors);

        assertFalse(securityDomainWrapperBean.isActive());
        assertFalse(securityDomainWrapperBean.isExclusive());
    }

    EditDomainController editDomainController;
    private SecurityDomain newSecurityDomain;
}