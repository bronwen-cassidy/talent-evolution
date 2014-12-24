/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 10-Oct-2007 14:18:05
 * @version 0.1
 */

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.organisation.positions.Position;

import java.util.List;
import java.util.Set;


public class TestHibernateOrganisationDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernateOrganisationDao = (IOrganisationDao) applicationContext.getBean("organisationUnitDao");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFindOrgUnitTree() throws Exception {
        try {
            hibernateOrganisationDao.findOrgUnitTree(DEFAULT_ORG_UNIT_ID);
        } catch (TalentStudioException e) {
            fail("No Exception expected but got: " + e.getMessage());
        }
    }

    public void testFindOrgUnitTree_PositionsOrdered() throws Exception {
        List<OrganisationUnit> orgUnits = hibernateOrganisationDao.findOrgUnitTree(DEFAULT_ORG_UNIT_ID);
        for(OrganisationUnit orgUnit : orgUnits) {
            Set<Position> positions = orgUnit.getPositions();
            Position previous = null;
            for(Position position : positions) {
                if(previous == null) {
                    previous = position;
                } else {
                    assertTrue(previous.getLabel().compareTo(position.getLabel()) <= 0);
                }
            }
        }

    }

    private IOrganisationDao hibernateOrganisationDao;
}