package com.zynap.talentstudio.web.security.domain;

/**
 * User: amark
 * Date: 16-Mar-2005
 * Time: 16:30:36
 */


import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.domain.UserSession;
import com.zynap.domain.UserPrincipal;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.ArrayList;

public class TestAddDomainController extends AbstractDomainControllerTest {

    public void setUp() throws Exception {
        super.setUp();

        setUserSession(ROOT_USER, mockRequest);
        addDomainController = (AddDomainController) applicationContext.getBean("addSecurityDomainController");
    }

    public void testFormBackingObject() throws Exception {

        final SecurityDomainWrapperBean securityDomainWrapperBean = getFormBackingObject();
        assertNull(securityDomainWrapperBean.getModifiedSecurityDomain().getId());
    }

    public void testReferenceData() throws Exception {

        final int pageNumber = 1;

        final SecurityDomainWrapperBean securityDomainWrapperBean = getFormBackingObject();

        final BindException errors = getErrors(addDomainController, securityDomainWrapperBean);
        final Map refData = addDomainController.referenceData(mockRequest, securityDomainWrapperBean, errors, pageNumber);
        final String expectedTitle = AddDomainController.MESSAGE_KEY + Integer.toString(pageNumber);
        assertEquals(expectedTitle, refData.get(ControllerConstants.TITLE).toString());
    }

    public void testValidation() throws Exception {

        final SecurityDomainWrapperBean securityDomainWrapperBean = getFormBackingObject();

        // set no values and check that the validator indicates that "label" is a required field
        final SecurityDomainValidator validator = (SecurityDomainValidator) addDomainController.getValidator();
        final BindException errors = getErrors(addDomainController, securityDomainWrapperBean);
        validator.validate(securityDomainWrapperBean, errors);
        assertTrue(errors.hasErrors());

        final String fieldName = "label";
        final FieldError error = (FieldError) errors.getFieldErrors(fieldName).iterator().next();
        assertNotNull(error);

        // set a value for ""label" and check validation succeeds
        DataBinder binder = new DataBinder(securityDomainWrapperBean, addDomainController.getCommandName());
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(fieldName, "securitydomainname");
        binder.bind(propertyValues);

        final Errors binderErrors = getErrors(binder);
        validator.validate(securityDomainWrapperBean, binderErrors);
        assertFalse(binderErrors.hasErrors());
        assertFalse(binderErrors.hasFieldErrors(fieldName));
    }

    public void testCommandNameAndType() {
        assertEquals(ControllerConstants.COMMAND_NAME, addDomainController.getCommandName());
        assertEquals(SecurityDomainWrapperBean.class, addDomainController.getCommandClass());
    }

    public void testProcessFinish() throws Exception {

        final SecurityDomainWrapperBean securityDomainWrapperBean = getFormBackingObject();

        securityDomainWrapperBean.setLabel("domain2");
        securityDomainWrapperBean.setActive(false);
        securityDomainWrapperBean.setExclusive(true);

        final Area newArea = new Area();
        newArea.setLabel("area2");
        securityDomainWrapperBean.setNode(newArea);

        final BindException errors = getErrors(addDomainController, securityDomainWrapperBean);
        final ModelAndView modelAndView = addDomainController.processFinish(mockRequest, mockResponse, securityDomainWrapperBean, errors);

        // check view is correct
        final ZynapRedirectView view = (ZynapRedirectView) modelAndView.getView();
        assertEquals(addDomainController.getSuccessView(), view.getUrl());

        // check that static attribute with new domain id is present
        final Long domainId = (Long) view.getStaticAttributes().get(ParameterConstants.DOMAIN_ID);
        assertEquals(securityDomainWrapperBean.getModifiedSecurityDomain().getId(), domainId);
    }

    private SecurityDomainWrapperBean getFormBackingObject() throws Exception {
        return (SecurityDomainWrapperBean) addDomainController.formBackingObject(mockRequest);
    }

    private AddDomainController addDomainController;
}