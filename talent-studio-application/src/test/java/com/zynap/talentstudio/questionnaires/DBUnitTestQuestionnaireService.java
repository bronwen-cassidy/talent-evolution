/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 01-Jun-2006 17:03:06
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.common.lookups.LookupValue;

import org.apache.commons.lang.ArrayUtils;

import java.util.*;

public class DBUnitTestQuestionnaireService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-reviews.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        questionnaireService = (IQuestionnaireService) applicationContext.getBean("questionnaireService");
    }

    public void testGetPersonalPortfolioQuestionnaires() throws Exception {

        final Collection<QuestionnaireDTO> personalQuestionnaires = questionnaireService.getPersonalPortfolioQuestionnaires(TARGET_SUBJECT_ID, USER_ID);
        checkQuestionnaires(personalQuestionnaires);

        // if you get all the questionnaires and remove the personal ones you should be left with the manager's questionnaire
        final Collection<QuestionnaireDTO> questionnaires = questionnaireService.getPortfolioQuestionnaires(TARGET_SUBJECT_ID);
        questionnaires.removeAll(personalQuestionnaires);
        assertEquals(1, questionnaires.size());
        final QuestionnaireDTO managerQuestionnaireDTO = questionnaires.iterator().next();
        // individual read is false for this questionnaire therefore individual does not get to see it whereas the manager does
        assertEquals("Info Form 3 (Expired)", managerQuestionnaireDTO.getLabel());        
    }

    public void testGetPortfolioQuestionnaires() throws Exception {

        final Collection questionnaires = questionnaireService.getPortfolioQuestionnaires(TARGET_SUBJECT_ID);
        checkQuestionnaires(questionnaires);
    }

    private void checkQuestionnaires(final Collection questionnaires) {

        assertFalse(questionnaires.isEmpty());

        int expectedNumberOfWorkflows = 5;

        final Set<Long> workflowIds = new HashSet<Long>();

        QuestionnaireDTO previous = null;
        for (Iterator iterator = questionnaires.iterator(); iterator.hasNext();) {

            final QuestionnaireDTO questionnaireDTO = (QuestionnaireDTO) iterator.next();
            final Long id = questionnaireDTO.getId();
            final Long workflowId = questionnaireDTO.getWorkflowId();
            assertNotNull(workflowId);

            // store workflow ids as we go along (use a Set to ensure that they are unique)
            workflowIds.add(workflowId);

            assertNotNull(questionnaireDTO.getLabel());
            assertNotNull(questionnaireDTO.getWorkflowType());

            if (questionnaireDTO.isNotificationBased()) {
                assertNotNull(id);
                assertTrue(ArrayUtils.contains(QUESTIONNAIRE_WORKFLOW_IDS, workflowId));
            } else {
                assertNull(id);

                if (workflowId.equals(EXPIRED_INFOFORM_ID)) {
                    assertTrue(questionnaireDTO.getCompleteByDate() != null);
                    assertTrue(questionnaireDTO.getCompleteByDate().before(new Date()));
                    assertNull(questionnaireDTO.getCompletedDate());
                } else {
                    assertTrue(ArrayUtils.contains(INFOFORM_WORKFLOW_IDS, workflowId));
                }
            }

            // should be ordered by workflow id first and then by roleName
            if (previous != null) {

                final Long previousWorkflowId = previous.getWorkflowId();
                assertTrue(previousWorkflowId.longValue() <= workflowId.longValue());

                final LookupValue prevRole = previous.getRole();
                final LookupValue role = questionnaireDTO.getRole();
                if (prevRole != null && role != null) {
                    assertTrue(prevRole.getSortOrder() < role.getSortOrder());
                }
            }

            previous = questionnaireDTO;
        }

        // the number of unique workflow ids must match the number of questionnaires + the number of infoforms exactly
        assertEquals(expectedNumberOfWorkflows, workflowIds.size());
    }

    private IQuestionnaireService questionnaireService;

    private static final Long TARGET_SUBJECT_ID = new Long(-33);
    private static final Long USER_ID = new Long(-44);
    private static final Long EXPIRED_INFOFORM_ID = new Long(17);

    private static final Long[] QUESTIONNAIRE_WORKFLOW_IDS = new Long[]{new Long(5), new Long(6), new Long(14)};
    private static final Long[] INFOFORM_WORKFLOW_IDS = new Long[]{new Long(15), new Long(16), EXPIRED_INFOFORM_ID};
}