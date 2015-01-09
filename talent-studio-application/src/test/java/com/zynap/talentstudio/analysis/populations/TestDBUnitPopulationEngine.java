/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.populations;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 21-Aug-2009 12:12:28
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.organisation.Node;

import java.util.List;

public class TestDBUnitPopulationEngine extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-pop-queries-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        populationEngine = (IPopulationEngine) getBean("populationEngine");
        analysisService = (IAnalysisService) getBean("analysisService");
    }

    /**
     * Input fields:
     * pop_id -123
     * expected results 3
     * @throws Exception
     */
    public void testFindWithGTDates() throws Exception {
        final Population population = (Population) analysisService.findById(new Long(-123));
        final List<? extends Node> nodes = populationEngine.find(population, ROOT_USER_ID);
        assertEquals(3, nodes.size());
    }

    /**
     * Input fields:
     * pop_id 323
     * expected results 2
     * @throws Exception
     */
    public void testFindWithGTDateTime() throws Exception {
        final Population population = (Population) analysisService.findById(new Long(323));
        final List<? extends Node> nodes = populationEngine.find(population, ROOT_USER_ID);
        assertEquals(2, nodes.size());
    }

    /**
     * Input fields:
     * pop_id -126
     * expected results 2
     * @throws Exception
     */
    public void testFindWithGTTime() throws Exception {
        final Population population = (Population) analysisService.findById(new Long(-126));
        final List<? extends Node> nodes = populationEngine.find(population, ROOT_USER_ID);
        assertEquals(2, nodes.size());
    }

    private IPopulationEngine populationEngine;
    private IAnalysisService analysisService;

}