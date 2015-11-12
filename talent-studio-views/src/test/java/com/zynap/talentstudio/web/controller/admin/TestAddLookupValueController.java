package com.zynap.talentstudio.web.controller.admin;

/**
 * User: amark
 * Date: 06-Jan-2006
 * Time: 17:12:29
 */

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;

public class TestAddLookupValueController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        setUserSession(new UserSession(getAdminUserPrincipal(), getArenaMenuHandler()), mockRequest);
        addLookupValueController = (AddLookupValueController) getBean("addLookupValueController");
    }

    public void testFormBackingObject() throws Exception {

        final String typeId = "TEST";
        final String label = "label";

        final LookupValue lookupValue = getFormBackingObject(typeId, label);

        assertEquals(lookupValue.getTypeId(), typeId);
        assertEquals(lookupValue.getLookupType().getLabel(), label);
    }

    public void testOnSubmit() throws Exception {

        final String typeId = "TEST";
        final String label = "label";

        final LookupValue lookupValue = getFormBackingObject(typeId, label);
        lookupValue.setLabel("test lookup value");
        lookupValue.setDescription("description");

        final ModelAndView modelAndView = addLookupValueController.onSubmitInternal(mockRequest, mockResponse, lookupValue, getErrors(lookupValue));
        RedirectView redirectView = (RedirectView) modelAndView.getView();
        assertEquals(addLookupValueController.getSuccessView(), redirectView.getUrl());
        assertEquals(typeId, redirectView.getStaticAttributes().get(ParameterConstants.LOOKUP_TYPE_ID));
    }

    public void testOnBindAndValidate() throws Exception {

        final String typeId = "TEST";
        final String label = "label";

        // make active
        final LookupValue lookupValue = getFormBackingObject(typeId, label);
        lookupValue.setActive(true);

        final Errors errors = getErrors(lookupValue);
        addLookupValueController.onBindAndValidateInternal(mockRequest, lookupValue, errors);

        // since this is a non-system lookup value ot should be made inactive
        assertFalse(lookupValue.isActive());

        // make into an active system defined lookup value - cannot be made inactive
        lookupValue.setSystem(true);
        lookupValue.setActive(true);
        addLookupValueController.onBindAndValidateInternal(mockRequest, lookupValue, errors);
        assertTrue(lookupValue.isActive());
    }

    private LookupValue getFormBackingObject(final String typeId, final String label) throws ServletException {
        mockRequest.addParameter(ParameterConstants.LOOKUP_TYPE_ID, typeId);
        mockRequest.addParameter(AddLookupValueController.LOOKUP_TYPE_LABEL, label);

        final LookupValue lookupValue = (LookupValue) addLookupValueController.formBackingObject(mockRequest);

        // user added lookup values cannot be system
        assertFalse(lookupValue.isSystem());

        return lookupValue;
    }

    private AddLookupValueController addLookupValueController;
}