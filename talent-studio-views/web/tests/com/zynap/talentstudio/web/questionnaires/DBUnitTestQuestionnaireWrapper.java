package com.zynap.talentstudio.web.questionnaires;
/**
 * Class or Interface description.
 *
 * @author syeoh
 * @since 11-Jan-2007 10:08:10
 * @version 0.1
 */

import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.ZynapDatabaseTestCase;

public class DBUnitTestQuestionnaireWrapper extends ZynapDatabaseTestCase {

    QuestionnaireWrapper questionnaireWrapper;

    protected String getDataSetFileName() {
        return "test-questionnaires.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        questionnaireWorkflowService = (IQueWorkflowService) applicationContext.getBean("queWorkflowService");
    }

    public void testIsWritePermission() throws Exception {
        for (int i = 0; i < TOTAL_CASES; i++) {
            final Long workflowId = new Long(i);
            QuestionnaireWorkflow questWF = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(workflowId);
            Questionnaire quest = new Questionnaire();
            quest.setQuestionnaireWorkflow(questWF);
            QuestionnaireWrapper queWrapper = new QuestionnaireWrapper();
            queWrapper.setQuestionnaire(quest);
            switch (i) {
                case 0:
                    queWrapper.setMyPortfolio(true);
                    assertFalse(queWrapper.isWritePermission());
                    queWrapper.setMyPortfolio(false);
                    assertFalse(queWrapper.isWritePermission());
                    break;
                case 1:
                    queWrapper.setMyPortfolio(true);
                    assertFalse(queWrapper.isWritePermission());
                    queWrapper.setMyPortfolio(false);
                    assertTrue(queWrapper.isWritePermission());
                    break;
                case 2:
                    queWrapper.setMyPortfolio(true);
                    assertTrue(queWrapper.isWritePermission());
                    queWrapper.setMyPortfolio(false);
                    assertFalse(queWrapper.isWritePermission());
                    break;
                case 3:
                    queWrapper.setMyPortfolio(true);
                    assertTrue(queWrapper.isWritePermission());
                    queWrapper.setMyPortfolio(false);
                    assertTrue(queWrapper.isWritePermission());
                    break;
            }
        }
    }

    private static final int TOTAL_CASES = 4;

    private IQueWorkflowService questionnaireWorkflowService;
}