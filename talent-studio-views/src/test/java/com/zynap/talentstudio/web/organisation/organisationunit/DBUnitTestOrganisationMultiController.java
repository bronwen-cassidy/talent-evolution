/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 12-Mar-2010 20:52:29
 * @version 0.1
 */
package com.zynap.talentstudio.web.organisation.organisationunit;

import com.zynap.talentstudio.web.organisation.OrganisationUnitWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import org.springframework.web.servlet.ModelAndView;

import java.util.Iterator;
import java.util.Map;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class DBUnitTestOrganisationMultiController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        organisationMultiController = (OrganisationMultiController) getBean("organisationMultiController");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        organisationMultiController = null;
    }

    @Override
    protected String getDataSetFileName() throws Exception {
        return "test-ou-data.xml";
    }

    public void testViewOrganisationHandler() throws Exception {
        mockRequest.addParameter("id", "-24");
        mockRequest.setRequestURI("https://www.zynaphosting.com/somecompany/orgbuilder/vieworganisation.htm?historyToken=752132673&id=5");
        ModelAndView modelAndView = organisationMultiController.viewOrganisationHandler(mockRequest, mockResponse);
        Map model = (Map) modelAndView.getModel().get("model");
        OrganisationUnitWrapperBean organisationUnit = (OrganisationUnitWrapperBean) model.get(OrganisationMultiController.ORGANISATION_UNIT);

        assertEquals(6, organisationUnit.getWrappedDynamicAttributes().size());
        // in the org arena we include modification buttons
        assertEquals(Boolean.TRUE, model.get("includeButtons"));
        // get the display value
        for (Iterator<FormAttribute> iterator = organisationUnit.getWrappedDynamicAttributes().iterator(); iterator.hasNext();) {
            AttributeWrapperBean formAttribute = (AttributeWrapperBean) iterator.next();
            try {
                formAttribute.getDisplayValue();
            } catch (Exception e) {
                fail("no exception expected but got: " + e.getMessage());
            }
        }
    }

    private OrganisationMultiController organisationMultiController;
}