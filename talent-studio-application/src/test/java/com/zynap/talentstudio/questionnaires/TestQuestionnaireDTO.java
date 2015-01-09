package com.zynap.talentstudio.questionnaires;

/**
 * User: amark
 * Date: 03-Oct-2006
 * Time: 09:58:53
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.common.lookups.LookupValue;

import java.util.Date;

public class TestQuestionnaireDTO extends ZynapTestCase {

    public void testEquals() throws Exception {

        final Long id = new Long(-111);
        final Long workflowId = new Long(-999);
        final String label = "test1";
        final LookupValue role = null;

        QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO(workflowId, label, QuestionnaireWorkflow.TYPE_INFO_FORM, QuestionnaireWorkflow.STATUS_OPEN, new Date(), null, true, true);
        QuestionnaireDTO questionnaireDTO2 = new QuestionnaireDTO(id, workflowId, label, QuestionnaireWorkflow.TYPE_INFO_FORM, QuestionnaireWorkflow.STATUS_OPEN, new Date(), true, true, null, role);

        assertFalse(questionnaireDTO == questionnaireDTO2);
        assertEquals(questionnaireDTO, questionnaireDTO2);
    }

    public void testCompareTo() {

        final Long id = new Long(-111);
        final Long workflowId = new Long(-999);
        final String label = "test1";
        final LookupValue role = null;

        QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO(workflowId, label, QuestionnaireWorkflow.TYPE_INFO_FORM, QuestionnaireWorkflow.STATUS_OPEN, new Date(), null, true, true);
        QuestionnaireDTO questionnaireDTO2 = new QuestionnaireDTO(id, workflowId, label, QuestionnaireWorkflow.TYPE_INFO_FORM, QuestionnaireWorkflow.STATUS_OPEN, new Date(), true, true, null, role);
        assertEquals(0, questionnaireDTO.compareTo(questionnaireDTO2));
        assertEquals(0, questionnaireDTO2.compareTo(questionnaireDTO));
    }

    public void testCompareToByRole() {

        final Long id = new Long(-111);
        final Long workflowId = new Long(-999);
        final String label = "test1";

        final LookupValue peerRole = new LookupValue("peer", "Peer", "desc", "roles");
        peerRole.setSortOrder(0);
        final LookupValue internalCustomerRole = new LookupValue("internalcustomer", "Internal Customer", "desc", "roles");
        internalCustomerRole.setSortOrder(10);

        QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO(id, workflowId, label, QuestionnaireWorkflow.TYPE_MANAGER_APPRAISAL, QuestionnaireWorkflow.STATUS_OPEN, new Date(), true, true, null, null);
        QuestionnaireDTO questionnaireDTO2 = new QuestionnaireDTO(id, workflowId, label, QuestionnaireWorkflow.TYPE_EVALUATOR_APPRAISAL, QuestionnaireWorkflow.STATUS_OPEN, new Date(), true, true, null, peerRole);

        // manager versus peer
        assertTrue(questionnaireDTO.compareTo(questionnaireDTO2) < 0);
        assertTrue(questionnaireDTO2.compareTo(questionnaireDTO) > 0);

        questionnaireDTO = new QuestionnaireDTO(id, workflowId, label, QuestionnaireWorkflow.TYPE_EVALUATOR_APPRAISAL, QuestionnaireWorkflow.STATUS_OPEN, new Date(), true, true, null, peerRole);
        questionnaireDTO2 = new QuestionnaireDTO(id, workflowId, label, QuestionnaireWorkflow.TYPE_EVALUATOR_APPRAISAL, QuestionnaireWorkflow.STATUS_OPEN, new Date(), true, true, null, internalCustomerRole);

        // peer versus internal customer
        assertTrue(questionnaireDTO.compareTo(questionnaireDTO2) < 0);
        assertTrue(questionnaireDTO2.compareTo(questionnaireDTO) > 0);
    }
}