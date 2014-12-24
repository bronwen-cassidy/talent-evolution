package com.zynap.talentstudio.workflow;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 06-Jul-2005
 * Time: 08:40:59
 * To change this template use File | Settings | File Templates.
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;

public class TestWorkflowBasicJdbcCommander extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        workflowBasicJdbcCommander = (WorkflowBasicJdbcCommander) applicationContext.getBean("workFlowCommander");
    }

    public void testCreateQuestionnaireProcess() throws Exception {
        try {
            createAndStartWorkFlow();
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception expected");
        }
    }

    private void createAndStartWorkFlow() throws Exception {
    }


    WorkflowBasicJdbcCommander workflowBasicJdbcCommander;

    public static final String USER_NAME_IDENTIFIER = ROOT_USERNAME;
    public static final String DUMMY_WORKFLOW_ID = "-44";

}