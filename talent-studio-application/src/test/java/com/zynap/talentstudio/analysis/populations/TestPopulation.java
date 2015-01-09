/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.populations;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 24-Mar-2009 08:53:28
 * @version 0.1
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.ArrayList;

public class TestPopulation extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        population = new Population(new Long(43), "S", "Test", "PUBLIC", "Test Description", null, new ArrayList<PopulationCriteria>());
        populationEngine = (IPopulationEngine) getBean("populationEngine");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        population = null;
    }

    public void testWrapCriteria() throws Exception {
        final String eq = IPopulationEngine.EQ;
        final String op = IPopulationEngine.OP_TYPE_;
        population.addPopulationCriteria(new PopulationCriteria(new Long(1), op, "12", "1234", null, eq, population));
        population.addPopulationCriteria(new PopulationCriteria(new Long(2), op, "23", "1244", AND_LT_BKT, eq, population));
        population.addPopulationCriteria(new PopulationCriteria(new Long(3), op, "13", "1254", OR, eq, population));
        population.addPopulationCriteria(new PopulationCriteria(new Long(4), op, "all", "active", ") AND (", eq, population));
        addRightBracketCriteria();
        
        population.wrapCriteria();
        try {
            populationEngine.find(population, ROOT_USER_ID, 0, 25, 25);
        } catch (Throwable e) {
            fail("Should not have failed: " + e.getMessage());
        }
    }

    private void addRightBracketCriteria() {
        population.addPopulationCriteria(new PopulationCriteria(new Long(5), IPopulationEngine.RIGHT_BRCKT_, null, "", null, null, population));
    }

    private Population population;
    private IPopulationEngine populationEngine;
    private static final String AND = IPopulationEngine.AND;
    private static final String AND_LT_BKT = " AND (";
    private static final String OR = IPopulationEngine.OR;
}