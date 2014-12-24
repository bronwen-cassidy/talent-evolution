/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires.definition;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 21-Aug-2006 14:53:06
 * @version 0.1
 */

import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;

public class TestAddQuestionnaireDefinitionController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();        
        addQuestionnaireDefinitionController = (AddQuestionnaireDefinitionController) getBean("addQuestionnaireDefinitionController");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOnSubmitInternal() throws Exception {
        QuestionnaireDefinitionWrapper wrapper = (QuestionnaireDefinitionWrapper) addQuestionnaireDefinitionController.formBackingObject(mockRequest);
        String testFile = "com/zynap/talentstudio/web/questionnaires/definition/allQuestionsTest.xml";
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        byte[] allbytes = new byte[20000];
        int numBytes = testFileUrl.read(allbytes);
        byte[] readBytes = new byte[numBytes];
        System.arraycopy(allbytes, 0, readBytes, 0, numBytes);
        wrapper.setDefinitionBytes(readBytes);
        Errors errors = getErrors(wrapper);
        addQuestionnaireDefinitionController.onBindAndValidateInternal(mockRequest, wrapper, errors);
        assertFalse(errors.hasErrors());
        ModelAndView modelAndView = addQuestionnaireDefinitionController.onSubmitInternal(mockRequest, mockResponse, wrapper, errors);
        assertNotNull(modelAndView.getView());
        assertEquals("viewquestionnairedefinition.htm", ((ZynapRedirectView) modelAndView.getView()).getUrl());
    }

    public void testOnSubmitInternalInvalidXml() throws Exception {
        QuestionnaireDefinitionWrapper wrapper = (QuestionnaireDefinitionWrapper) addQuestionnaireDefinitionController.formBackingObject(mockRequest);
        String testFile = "com/zynap/talentstudio/web/questionnaires/definition/InvalidLineItemTest.xml";
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        byte[] allbytes = new byte[20000];
        int numBytes = testFileUrl.read(allbytes);
        byte[] readBytes = new byte[numBytes];
        System.arraycopy(allbytes, 0, readBytes, 0, numBytes);
        wrapper.setDefinitionBytes(readBytes);
        Errors errors = getErrors(wrapper);
        addQuestionnaireDefinitionController.onBindAndValidateInternal(mockRequest, wrapper, errors);
        assertFalse(errors.hasErrors());
        ModelAndView modelAndView = addQuestionnaireDefinitionController.onSubmitInternal(mockRequest, mockResponse, wrapper, errors);
        assertEquals("addquestionnairedefinition", modelAndView.getViewName());
        assertTrue(errors.hasErrors());
        assertEquals(1, errors.getErrorCount());
        assertEquals("error.lineitem.unsupported.question.type", errors.getFieldError("definitionBytes").getCode());
    }

    private AddQuestionnaireDefinitionController addQuestionnaireDefinitionController;
}