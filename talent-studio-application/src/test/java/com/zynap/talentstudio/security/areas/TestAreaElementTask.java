/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.security.areas;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 08-Dec-2009 15:40:31
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.security.ISecurityManager;


public class TestAreaElementTask extends ZynapDatabaseTestCase {

    @Override
    protected String getDataSetFileName() {
        return "area-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        areaElementTask = new AreaElementTask();
        areaElementTask.setAnalysisService((IAnalysisService) getBean("analysisService"));
        areaElementTask.setPopulationEngine((IPopulationEngine) getBean("populationEngine"));
        areaElementTask.setSecurityManager((ISecurityManager) getBean("securityManager"));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRun() throws Exception {
        try {
            areaElementTask.run();
        } catch (Exception e) {
            fail("not exception expected but got: " + e.getMessage());
        }
    }

    private AreaElementTask areaElementTask;
}