/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.objectives;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 15-May-2008 16:46:57
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;

public class DBUnitTestExpiryTask extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        objectiveService = (IObjectiveService) getBean("objectiveService");
        expiryTask = new ExpiryTask();
        expiryTask.setObjectiveService(objectiveService);
    }

    public void testRun() throws Exception {
        try {
            expiryTask.run();
        } catch (Exception e) {
            fail("no exception expected");
        }
    }

    public void testRunNotCurrent() throws Exception {
        try {
            expiryTask.run();
            commitAndStartNewTx();

            ObjectiveSet objectiveSet = (ObjectiveSet) objectiveService.findById(new Long(-1));
            assertEquals(true, objectiveSet.isArchived());
            // attempt to archive when no objective sets are current
            expiryTask.run();
        } catch (Exception e) {
            fail("no exception expected but got: " + e.getMessage());
        }
    }

    private ExpiryTask expiryTask;
    private IObjectiveService objectiveService;
}