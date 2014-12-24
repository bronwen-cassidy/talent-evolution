package com.zynap.talentstudio.web.organisation.attributes;

/**
 * User: amark
 * Date: 16-Nov-2005
 * Time: 10:04:17
 */

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeDTO;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.MissingRequestParameterException;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class TestDAMultiController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        daMultiController = (DAMultiController) getBean("dAMultiController");
    }

    public void testListSubjectAttributeHandler() throws Exception {

        final String nodeType = Node.SUBJECT_UNIT_TYPE_;

        final ModelAndView modelAndView = daMultiController.listSubjectAttributeHandler(mockRequest, mockResponse);
        assertEquals(DAMultiController.LIST_SUBJECT_DA_VIEW, modelAndView.getViewName());
        final Map model = getModel(modelAndView);
        assertEquals(nodeType, model.get(ControllerConstants.ARTEFACT_TYPE));
        final Collection das = (Collection) model.get(ControllerConstants.DYNAMIC_ATTRIBUTES);
        for (Iterator iterator = das.iterator(); iterator.hasNext();) {
            DynamicAttributeDTO dynamicAttribute = (DynamicAttributeDTO) iterator.next();
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
        }
    }

    public void testListPositionAttributeHandler() throws Exception {

        final String nodeType = Node.POSITION_UNIT_TYPE_;

        final ModelAndView modelAndView = daMultiController.listPositionAttributeHandler(mockRequest, mockResponse);
        assertEquals(DAMultiController.LIST_POSITION_DA_VIEW, modelAndView.getViewName());
        final Map model = getModel(modelAndView);
        assertEquals(nodeType, model.get(ControllerConstants.ARTEFACT_TYPE));
        final Collection das = (Collection) model.get(ControllerConstants.DYNAMIC_ATTRIBUTES);
        for (Iterator iterator = das.iterator(); iterator.hasNext();) {
            DynamicAttributeDTO dynamicAttribute = (DynamicAttributeDTO) iterator.next();
            assertEquals(nodeType, dynamicAttribute.getArtefactType());
        }
    }

    public void testViewAttributeHandlerNoId() throws Exception {
        try {
            daMultiController.viewAttributeHandler(mockRequest, mockResponse);
            fail("required attribute id parameter not supplied so must fail");
        } catch (MissingRequestParameterException expected) {
        }
    }

    public void testViewAttributeHandlerInvalidId() throws Exception {
        try {
            String id = "-890";
            mockRequest.addParameter(ParameterConstants.ATTR_ID, id);
            daMultiController.viewAttributeHandler(mockRequest, mockResponse);
            fail("Invalid attribute id parameter not supplied so must fail");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testViewAttributeHandler() throws Exception {

        final Collection allAttributes = daMultiController.getDynamicAttributeService().getAllAttributes(Node.POSITION_UNIT_TYPE_);
        final DynamicAttribute firstAttribute = (DynamicAttribute) allAttributes.iterator().next();
        final Long id = firstAttribute.getId();

        mockRequest.addParameter(ParameterConstants.ATTR_ID, id.toString());
        final ModelAndView modelAndView = daMultiController.viewAttributeHandler(mockRequest, mockResponse);
        assertEquals(DAMultiController.DISPLAY_DA_VIEW, modelAndView.getViewName());
        final Map model = getModel(modelAndView);
        final DynamicAttributeWrapper actual = (DynamicAttributeWrapper) model.get(ControllerConstants.DYNAMIC_ATTRIBUTE);
        assertEquals(id, actual.getId());
    }

    public void testDeleteAttributeHandlerNoId() throws Exception {
        try {
            daMultiController.deleteAttributeHandler(mockRequest, mockResponse);
            mockRequest.addParameter(ParameterConstants.ARTEFACT_TYPE, Node.POSITION_UNIT_TYPE_);
            fail("required attribute id parameter not supplied so must fail");
        } catch (ServletRequestBindingException expected) {
        }
    }

    public void testDeleteAttributeHandlerInvalidId() throws Exception {
        try {
            String id = "-890";
            mockRequest.addParameter(ParameterConstants.ATTR_ID, id);
            mockRequest.addParameter(ParameterConstants.ARTEFACT_TYPE, Node.POSITION_UNIT_TYPE_);
            daMultiController.deleteAttributeHandler(mockRequest, mockResponse);
            fail("Invalid attribute id parameter not supplied so must fail");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testDeleteAttributeHandler() throws Exception {

        final Collection allAttributes = daMultiController.getDynamicAttributeService().getAllAttributes(Node.POSITION_UNIT_TYPE_);
        final DynamicAttribute firstAttribute = (DynamicAttribute) allAttributes.iterator().next();
        final Long id = firstAttribute.getId();

        mockRequest.addParameter(ParameterConstants.ATTR_ID, id.toString());
        mockRequest.addParameter(ParameterConstants.ARTEFACT_TYPE, Node.POSITION_UNIT_TYPE_);

        final ModelAndView modelAndView = daMultiController.deleteAttributeHandler(mockRequest, mockResponse);
        assertEquals(DAMultiController.CONFIRM_DELETE_DA, modelAndView.getViewName());
        final Map model = getModel(modelAndView);
        assertNotNull(model.get(DAMultiController.USED_BY_NODE));
        final DynamicAttribute actual = (DynamicAttribute) model.get(ControllerConstants.DYNAMIC_ATTRIBUTE);
        assertEquals(firstAttribute, actual);
    }

    DAMultiController daMultiController;
}