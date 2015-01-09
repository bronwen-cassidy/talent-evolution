package com.zynap.talentstudio.questionnaires;

/**
 * User: amark
 * Date: 02-Nov-2006
 * Time: 10:34:42
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.performance.PerformanceReview;

import org.apache.commons.lang.ArrayUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class TestExpiryTask extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-expiry-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();

        expiryTask = new ExpiryTask();
        IQueWorkflowService questionnaireWorkflowService = (IQueWorkflowService) getBean("queWorkflowService");
        questionnaireDefinitionService = (IQueDefinitionService) getBean("questionnaireDefinitionService");
        expiryTask.setQuestionnaireWorkflowService(questionnaireWorkflowService);

    }

    public void testRun() throws Exception {
        expiryTask.run();
        commitAndStartNewTx();
        final Collection definitions = questionnaireDefinitionService.findAll();
        for (Iterator iterator = definitions.iterator(); iterator.hasNext();) {
            final QuestionnaireDefinition definition = (QuestionnaireDefinition) iterator.next();

            // check that specified workflows have been marked as completed
            final Set questionnaireWorkflows = definition.getQuestionnaireWorkflows();
            assertFalse(questionnaireWorkflows.isEmpty());
            for (Iterator workflowIterator = questionnaireWorkflows.iterator(); workflowIterator.hasNext();) {
                final QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) workflowIterator.next();
                final Long id = workflow.getId();
                final PerformanceReview performanceReview = workflow.getPerformanceReview();

                final boolean completed = QuestionnaireWorkflow.STATUS_COMPLETED.equals(workflow.getStatus());
                if (ArrayUtils.contains(EXPIRED_WORKFLOW_IDS, id) || ArrayUtils.contains(COMPLETED_WORKFLOW_IDS, id)) {

                    // check performance review as well
                    if (performanceReview != null) {
                        assertTrue("Performance review " + performanceReview.getId() + " should be completed", QuestionnaireWorkflow.STATUS_COMPLETED.equals(performanceReview.getStatus()));
                    }

                    assertTrue("Workflow " + id + " should be completed", completed);

                } else {

                    // check performance review as well
                    if (performanceReview != null) {
                        assertFalse("Performance review " + performanceReview.getId() + " should not be completed", QuestionnaireWorkflow.STATUS_COMPLETED.equals(performanceReview.getStatus()));
                    }

                    assertFalse("Workflow " + id + " should not be completed", completed);
                }
            }
        }
    }

    private ExpiryTask expiryTask;
    private IQueDefinitionService questionnaireDefinitionService;

    private static final Long[] EXPIRED_WORKFLOW_IDS = new Long[]{new Long(8), new Long(10), new Long(20), new Long(22)};
    private static final Long[] COMPLETED_WORKFLOW_IDS = new Long[]{new Long(6), new Long(9), new Long(12), new Long(15), new Long(18), new Long(21)};
}