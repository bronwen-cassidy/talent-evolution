/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 22-Jul-2009 16:56:29
 * @version 0.1
 */
package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeDTO;

import java.util.Collection;
import java.util.List;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestAddDynamicAttributeController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        addDynamicAttributeController = (AddDynamicAttributeController) getBean("addDAController");
        setUserSession(ROOT_USER, mockRequest);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        addDynamicAttributeController = null;
        mockRequest = null;
    }

    public void testFormBackingObjectSubject() throws Exception {
        mockRequest.addParameter(ParameterConstants.ARTEFACT_TYPE, "S");
        final DynamicAttributeWrapper daWrapper = (DynamicAttributeWrapper) addDynamicAttributeController.formBackingObject(mockRequest);
        assertEquals("S", daWrapper.getArtefactType());
    }

    public void testOnBindPage0() throws Exception {
        mockRequest.addParameter(ParameterConstants.ARTEFACT_TYPE, "S");
        final DynamicAttributeWrapper daWrapper = (DynamicAttributeWrapper) addDynamicAttributeController.formBackingObject(mockRequest);
        mockRequest.addParameter("calculated", "on");
        String pageAttr = addDynamicAttributeController.getClass().getName() + ".PAGE." + "command";
        mockRequest.setAttribute(pageAttr, new Integer(0));
        addDynamicAttributeController.onBindInternal(mockRequest, daWrapper, getErrors(daWrapper));
        assertTrue(daWrapper.isCalculated());
    }

    public void testOnBindPage1() throws Exception {
        mockRequest.addParameter(ParameterConstants.ARTEFACT_TYPE, "S");

        final DynamicAttributeWrapper daWrapper = (DynamicAttributeWrapper) addDynamicAttributeController.formBackingObject(mockRequest);
        String pageAttr = addDynamicAttributeController.getClass().getName() + ".PAGE." + "command";
        mockRequest.setAttribute(pageAttr, new Integer(1));

        // active and searchable but not manadatory
        mockRequest.addParameter("active", "on");
        mockRequest.addParameter("searchable", "on");
        addDynamicAttributeController.onBindInternal(mockRequest, daWrapper, getErrors(daWrapper));
        assertTrue(daWrapper.isActive());
        assertTrue(daWrapper.isSearchable());
    }

    public void testOnBindAndValidateInternalTarget1() throws Exception {
        mockRequest.addParameter(ParameterConstants.ARTEFACT_TYPE, "S");
        final DynamicAttributeWrapper daWrapper = (DynamicAttributeWrapper) addDynamicAttributeController.formBackingObject(mockRequest);
        daWrapper.setCalculated(true);
        String pageAttr = addDynamicAttributeController.getClass().getName() + ".PAGE." + "command";
        mockRequest.setAttribute(pageAttr, new Integer(0));
        mockRequest.addParameter("_target1", "1");
        addDynamicAttributeController.onBindAndValidateInternal(mockRequest, daWrapper, getErrors(daWrapper), 0);
        final Collection<DynamicAttributeDTO> attributeDTOCollection = daWrapper.getAttributes();
        final List<ExpressionWrapper> expressions = daWrapper.getExpressions();
        assertTrue(attributeDTOCollection.size() > 0);
        // there will be expressions here too
        assertTrue(expressions.size() == 2);
    }


    private AddDynamicAttributeController addDynamicAttributeController;
}