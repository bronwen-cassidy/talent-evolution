/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.objectives;

/**
 * Test class to just hit the hibernate query methods, validating the queries are correctly
 * formatted
 *
 * @author bcassidy
 * @since 12-Mar-2007 17:34:31
 * @version 0.1
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.Collection;
import java.util.List;

public class TestHibernateObjectiveDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernateObjectiveDao = (HibernateObjectiveDao) getBean("objectiveDao");
    }

    public void testFindOrgUnitObjectives() throws Exception {
        ObjectiveSet result = hibernateObjectiveDao.findOrgUnitObjectives(DEFAULT_ORG_UNIT_ID);
        assertNull(result);
    }

    public void testFindCurrentSubjectObjectives() throws Exception {
        final List<Objective> subjectApprovedObjectives = hibernateObjectiveDao.findCurrentSubjectObjectives(new Long(1399));
        assertNotNull(subjectApprovedObjectives);
    }

    public void testFindAssessorsObjectives() throws Exception {
        List<Objective> objectives = hibernateObjectiveDao.findAssessorsObjectives(new Long(61));
        assertNotNull(objectives);
    }

    public void testGetArchivedObjectiveSets() throws Exception {
        List<ObjectiveSet> objectiveSets = hibernateObjectiveDao.getArchivedObjectiveSets(new Long(61));
        assertNotNull(objectiveSets);
    }

    public void testFindCurrentObjectiveSet() throws Exception {
        try {
            hibernateObjectiveDao.findCurrentObjectiveSet(new Long(814));
        } catch (Exception e) {
            fail("no exception expected but got " + e.getMessage());
        }
    }

    public void testHasArchivedObjectiveSets() throws Exception {
        assertFalse(hibernateObjectiveDao.hasArchivedObjectiveSets(new Long(2950)));
    }

    private HibernateObjectiveDao hibernateObjectiveDao;
}
