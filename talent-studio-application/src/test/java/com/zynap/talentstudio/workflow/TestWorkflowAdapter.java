/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.workflow;

/**
 * Class or Interface description.
 *
 * @author jsuiras
 * @version $Revision: $
 *          $Id: $
 */
import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.List;

public class TestWorkflowAdapter extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        workflowAdapter = (WorkflowAdapter) applicationContext.getBean("workFlowAdapter");
    }

    public void testGetNotifications() throws Exception {
        List workList = workflowAdapter.getNotifications(new Long(1), true);
        assertTrue(workList.isEmpty());
        assertNotNull(workList);
    }

    // todo dbimport, export! more tests
    public void testGetNotificationsManager() throws Exception {
        List workList = workflowAdapter.getNotifications(new Long(-132), true);
        assertTrue(workList.isEmpty());
    }

    IWorkflowAdapter workflowAdapter;
}