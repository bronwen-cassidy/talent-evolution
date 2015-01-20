/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 08-Apr-2009 11:09:16
 * @version 0.1
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.domain.UserSession;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.MissingRequestParameterException;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestAddSubjectPortfolioItemController extends ZynapDbUnitMockControllerTestCase {


    protected void setUp() throws Exception {
        super.setUp();
        addSubjectPortfolioItemController = (AddSubjectPortfolioItemController) getBean("subjectPortfolioController");
        final UserSession userSession = new UserSession(getAdminUserPrincipal(), getArenaMenuHandler());
        setUserSession(userSession, mockRequest);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        addSubjectPortfolioItemController = null;
    }

    protected String getDataSetFileName() throws Exception {
        return "test-subject-data.xml";
    }

    public void testFormBackingObject() throws Exception {
        try {
            addSubjectPortfolioItemController.formBackingObject(mockRequest);
            fail("Operation not allowed");
        } catch (MissingRequestParameterException mpe) {
            //missing command.node.id
        }
        try {
            mockRequest.setParameter(ParameterConstants.NODE_ID_PARAM, NOT_SUBJECT_NODE_ID.toString());
            addSubjectPortfolioItemController.formBackingObject(mockRequest);
            fail("Subject does not exit");
        } catch (DomainObjectNotFoundException don) {

        } catch (Throwable t) {
            
        }
        mockRequest.setParameter(ParameterConstants.NODE_ID_PARAM, SUBJECT_NODE_ID.toString());
        PortfolioItemWrapper itemWrapper = (PortfolioItemWrapper) addSubjectPortfolioItemController.formBackingObject(mockRequest);
        assertNotNull(itemWrapper);
        assertEquals(addSubjectPortfolioItemController.isMyPortfolio(), false);
    }

    public void testOnBindAndValidate() throws Exception {
        mockRequest.setParameter(ParameterConstants.NODE_ID_PARAM, SUBJECT_NODE_ID.toString());
        PortfolioItemWrapper itemWrapper = (PortfolioItemWrapper) addSubjectPortfolioItemController.formBackingObject(mockRequest);
        itemWrapper.setUploadedText("uploadtext");
        BindException bindingException = new BindException(itemWrapper, "command");
        addSubjectPortfolioItemController.onBindAndValidate(mockRequest, itemWrapper, bindingException);
        assertEquals(bindingException.getErrorCount(), 3);

        itemWrapper.setUploadedText("uploadtext");
        itemWrapper.setContentTypeId("ADF");
        itemWrapper.setContentSubType("TEXT");
        bindingException = new BindException(itemWrapper, "command");
        addSubjectPortfolioItemController.onBindAndValidate(mockRequest, itemWrapper, bindingException);
        assertEquals(bindingException.getErrorCount(), 1);
        FieldError fieldError = (FieldError) bindingException.getFieldErrors().get(0);
        assertEquals("label", fieldError.getField());


        itemWrapper.setUploadedText("uploadtext");
        itemWrapper.setContentTypeId("ADF");
        itemWrapper.setContentSubType("TEXT");
        itemWrapper.setLabel("Ilabel");
        bindingException = new BindException(itemWrapper, "command");
        addSubjectPortfolioItemController.onBindAndValidate(mockRequest, itemWrapper, bindingException);
        assertEquals(bindingException.getErrorCount(), 0);


    }

    public void testOnSubmit() throws Exception {
        mockRequest.setParameter(ParameterConstants.NODE_ID_PARAM, SUBJECT_NODE_ID.toString());
        PortfolioItemWrapper itemWrapper = (PortfolioItemWrapper) addSubjectPortfolioItemController.formBackingObject(mockRequest);

        itemWrapper.setUploadedText("uploadtext");
        itemWrapper.setContentTypeId("ADF");
        itemWrapper.setContentSubType("TEXT");
        itemWrapper.setLabel("Ilabel");
        BindException bindingException = new BindException(itemWrapper, "command");
        addSubjectPortfolioItemController.onSubmit(mockRequest, mockResponse, itemWrapper, bindingException);
        //its my portfolio
        assertEquals(itemWrapper.getModifiedItem().getSecurityAttribute().isIndividualWrite(), false);


    }



    private AddSubjectPortfolioItemController addSubjectPortfolioItemController;
    private Long SUBJECT_NODE_ID = new Long(10);
    private Long NOT_SUBJECT_NODE_ID = new Long(-24);

}