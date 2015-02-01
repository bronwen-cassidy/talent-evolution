/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 08-Apr-2009 09:50:11
 * @version 0.1
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.talentstudio.web.utils.MissingRequestParameterException;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.domain.UserSession;
import com.zynap.web.mocks.MockErrors;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.dao.DataIntegrityViolationException;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestAddPositionPortfolioItemController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        addPositionPortfolioItemController = (AddPositionPortfolioItemController) getBean("positionPortfolioController");
        final UserSession userSession = new UserSession(getAdminUserPrincipal(), getArenaMenuHandler());
        setUserSession(userSession, mockRequest);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        addPositionPortfolioItemController = null;
    }

    protected String getDataSetFileName() throws Exception {
        return "test-subject-data.xml";
    }

    public void testFormBackingObject() throws Exception {
        try {
            addPositionPortfolioItemController.formBackingObject(mockRequest);
            fail("Operation not allowed");
        } catch (MissingRequestParameterException mpe) {
            //missing command.node.id
        }
        mockRequest.setParameter(ParameterConstants.NODE_ID_PARAM, POSITION_NODE_ID.toString());
        PortfolioItemWrapper itemWrapper = (PortfolioItemWrapper) addPositionPortfolioItemController.formBackingObject(mockRequest);
        assertNotNull(itemWrapper);
        assertEquals(addPositionPortfolioItemController.isMyPortfolio(),false);
    }

    public void testOnBindAndValidate() throws Exception {
        mockRequest.setParameter(ParameterConstants.NODE_ID_PARAM, POSITION_NODE_ID.toString());
        PortfolioItemWrapper itemWrapper = (PortfolioItemWrapper) addPositionPortfolioItemController.formBackingObject(mockRequest);
        itemWrapper.setUploadedText("uploadtext");
        BindException bindingException = new BindException(itemWrapper, "command");
        addPositionPortfolioItemController.onBindAndValidate(mockRequest, itemWrapper, bindingException);
        assertEquals(bindingException.getErrorCount(), 3);

        itemWrapper.setUploadedText("uploadtext");
        itemWrapper.setContentTypeId("ADF");
        itemWrapper.setContentSubType("TEXT");
        bindingException = new BindException(itemWrapper, "command");
        addPositionPortfolioItemController.onBindAndValidate(mockRequest, itemWrapper, bindingException);
        assertEquals(bindingException.getErrorCount(), 1);
        FieldError fieldError = (FieldError) bindingException.getFieldErrors().get(0);
        assertEquals("label", fieldError.getField());


        itemWrapper.setUploadedText("uploadtext");
        itemWrapper.setContentTypeId("ADF");
        itemWrapper.setContentSubType("TEXT");
        itemWrapper.setLabel("Ilabel");
        bindingException = new BindException(itemWrapper, "command");
        addPositionPortfolioItemController.onBindAndValidate(mockRequest, itemWrapper, bindingException);
        assertEquals(bindingException.getErrorCount(), 0);


    }

    public void testOnSubmit() throws Exception {
        mockRequest.setParameter(ParameterConstants.NODE_ID_PARAM, POSITION_NODE_ID.toString());
        PortfolioItemWrapper itemWrapper = (PortfolioItemWrapper) addPositionPortfolioItemController.formBackingObject(mockRequest);

        itemWrapper.setUploadedText("uploadtext");
        itemWrapper.setContentTypeId("ADF");
        itemWrapper.setContentSubType("TEXT");
        itemWrapper.setLabel("Ilabel");
        BindException bindingException = new BindException(itemWrapper, "command");
        addPositionPortfolioItemController.onSubmit(mockRequest, mockResponse, itemWrapper, bindingException);
        //its not my portfolio
        assertEquals(itemWrapper.getModifiedItem().getSecurityAttribute().isIndividualWrite(),false);


    }


    private AddPositionPortfolioItemController addPositionPortfolioItemController;
    private Long POSITION_NODE_ID = new Long(7);
}