/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestQuestionnaireService extends TestAbstractQuestionnaireService {


    public void testFindQuestionnaireByWorkflow() throws Exception {
        final Questionnaire questionnaire = questionnaireService.findQuestionnaireByWorkflow((long) 1, ROOT_USER_ID, (long) 0, null);
        assertNull(questionnaire);
    }

    public void testLoadQuestionnaire() throws Exception {
        try {
            questionnaireService.loadQuestionnaire((long) -50, new Long(0));
            fail("Exception not thrown looking for non-existent questionnaire");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testGetPortfolioQuestionnaires() throws Exception {
        final Long subjectId = (long) -80;
        final Collection questionnaires = questionnaireService.getPortfolioQuestionnaires(subjectId);
        assertNotNull(questionnaires);
    }

    public void testGetPersonalPortfolioQuestionnaires() throws Exception {
        final Long subjectId = (long) 31;
        final Collection questionnaires = questionnaireService.getPersonalPortfolioQuestionnaires(subjectId, (long) 23);
        assertNotNull(questionnaires);
    }

    public void testFindQuestionnaires() throws Exception {
        final Collection questionnaires = questionnaireService.findQuestionnaires(QUESTIONNAIRE_WORKFLOW_ID, (long) -1);
        assertNotNull(questionnaires);
    }


    public void testCreateOrUpdateQuestionnaire() throws Exception {

        final QuestionnaireWorkflow questionnaireWorkflow = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(QUESTIONNAIRE_WORKFLOW_ID);
        final Long subjectId = (long) -3;

        Questionnaire questionnaire = new Questionnaire(null, questionnaireWorkflow, null);
        questionnaire.setSubjectId(subjectId);
        questionnaireWorkflow.getQuestionnaireDefinition().getDynamicAttributes().size();
        questionnaireService.createOrUpdateQuestionnaire(questionnaire);
        final Long newId = questionnaire.getId();
        assertNotNull(newId);

        // do update and check id is not modified
        questionnaire.setLabel("Test 2");
        questionnaireService.createOrUpdateQuestionnaire(questionnaire);
        assertEquals(newId, questionnaire.getId());
    }

    public void testCloseWorkflow() throws Exception {

        final User user = getAdminUser(userService);
        QuestionnaireWorkflow questionnaireWorkflow = buildWorkflow(user);

        questionnaireWorkflow.setWorkflowType(QuestionnaireWorkflow.TYPE_QUESTIONNAIRE);
        questionnaireWorkflowService.create(questionnaireWorkflow);
        assertEquals(QuestionnaireWorkflow.STATUS_NEW, questionnaireWorkflow.getStatus());

        questionnaireWorkflowService.startWorkflow(questionnaireWorkflow);
        assertTrue(questionnaireWorkflow.hasProcess());
        assertEquals(QuestionnaireWorkflow.STATUS_PUBLISHED, questionnaireWorkflow.getStatus());

        // todo we cannot test closing the workflow as this is a stored procedure that needs to commit before we can see it :-(

//        questionnaireWorkflowService.closeWorkflow(questionnaireWorkflow);
//        assertFalse(questionnaireWorkflow.hasProcess());
//        assertEquals(QuestionnaireWorkflow.STATUS_COMPLETED, questionnaireWorkflow.getStatus());

    }

    public void testDeleteWorkflow() throws Exception {
        final QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(QUESTIONNAIRE_WORKFLOW_ID);
        questionnaireWorkflowService.delete(workflow);
    }    
}