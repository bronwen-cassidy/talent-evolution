/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 19-Nov-2007 16:25:26
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

public class TestQuestionnaireWorkflowWrapperBean extends ZynapMockControllerTest {

    public void setUp() throws Exception {
        super.setUp();
        setUserSession(ADMINISTRATOR_USER_ID, mockRequest);
        controller = (AddQuestionnaireWorkflowController) getBean("addQuestionnaireWorkflowController");
    }

    public void testGetModifiedQuestionnaireWorkflow() throws Exception {
        QuestionnaireWorkflowWrapperBean wrapper = (QuestionnaireWorkflowWrapperBean) controller.formBackingObject(mockRequest);
        final String expectedDescription = "description";
        final String expectedLabel = "my label";

        wrapper.setDescription(expectedDescription);
        wrapper.setLabel(expectedLabel);
        wrapper.setPopulationId(new Long(-2));
        final QuestionnaireWorkflow workflow = wrapper.getModifiedQuestionnaireWorkflow();
        assertNotNull(workflow.getPopulation());
        assertEquals(expectedDescription, workflow.getDescription());
        assertEquals(expectedLabel, workflow.getLabel());
    }

    public void testIsNotificationBased() throws Exception {
        //final QuestionnaireWorkflow workflow = questionnaireWorkflowWrapperBean.getModifiedQuestionnaireWorkflow();
    }

    private AddQuestionnaireWorkflowController controller;
}