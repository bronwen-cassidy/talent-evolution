/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 20-Oct-2006 09:59:23
 * @version 0.1
 */

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.permits.IPermit;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class TestDBUnitOrganisationUnitService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "organisation-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        organisationUnitService = (IOrganisationUnitService) getBean("organisationUnitService");
    }

    public void testFindOrgUnitTree() throws Exception {

        List list = organisationUnitService.findSecureOrgUnitTree(IT_USER_ID);
        assertEquals(ALL_ORGUNIT_IDS.length, list.size());

        for (Object aList : list) {
            final OrganisationUnit organisationUnit = (OrganisationUnit) aList;
            final Long id = organisationUnit.getId();
            assertTrue("OU not found: " + id, ArrayUtils.contains(ALL_ORGUNIT_IDS, id));
            assertTrue("Org unit should have been active", organisationUnit.isActive());
            if (organisationUnit.getId().equals(ENG_ORGUNIT_ID)
                    || organisationUnit.getId().equals(DEV_ORGUNIT_ID)
                    || organisationUnit.getId().equals(JAVA_ORGUNIT_ID)) {
                assertTrue(organisationUnit.isHasAccess());
            } else {
                assertFalse(organisationUnit.isHasAccess());
            }
        }
    }

    public void testFindAllSecure() {
        UserSessionFactory.setUserSession(new UserSession(new UserPrincipal(new User(IT_USER_ID, "username", "firstName", "lastName"), new ArrayList<IPermit>())));
        
        final List allSecure = organisationUnitService.findAllSecure();
        Long[] expected = new Long[]{ENG_ORGUNIT_ID, DEV_ORGUNIT_ID, JAVA_ORGUNIT_ID};
        assertOrgUnitsPresent(expected, allSecure);
    }

    public void testFindHierarchy() throws Exception {
        final OrganisationUnit current = organisationUnitService.findOrgUnitBySubjectUserId(IT_USER_ID.toString());

        final List secureTree = organisationUnitService.findOrgUnitTree(current.getId());
        Long[] expected = new Long[]{IT_ORGUNIT_ID, ENG_ORGUNIT_ID, DEV_ORGUNIT_ID, JAVA_ORGUNIT_ID};
        assertOrgUnitsPresent(expected, secureTree);
    }

    public void testFindValidParentsOwnOrgUnit() throws Exception {
        final List list = organisationUnitService.findValidParents(IT_ORGUNIT_ID, IT_USER_ID);
        assertTrue(list.isEmpty());
    }

    public void testFindValidParentsChildOrgUnit() throws Exception {

        Long[] expected = new Long[]{ENG_ORGUNIT_ID, DEV_ORGUNIT_ID};

        final List list = organisationUnitService.findValidParents(JAVA_ORGUNIT_ID, IT_USER_ID);
        assertOrgUnitsPresent(expected, list);
    }

    public void testFindValidParents() throws Exception {

        Long[] expected = new Long[]{ENG_ORGUNIT_ID, DEV_ORGUNIT_ID, JAVA_ORGUNIT_ID};

        final List list = organisationUnitService.findValidParents(IT_USER_ID);
        assertOrgUnitsPresent(expected, list);
    }

    public void testFindValidParentsAdministrator() throws Exception {

        final List list = organisationUnitService.findValidParents(ADMINISTRATOR_USER_ID);
        assertOrgUnitsPresent(ALL_ORGUNIT_IDS, list);
    }

    private void assertOrgUnitsPresent(Long[] expected, List list) {
        assertEquals(expected.length, list.size());

        for (Object aList : list) {
            final OrganisationUnit organisationUnit = (OrganisationUnit) aList;
            final Long id = organisationUnit.getId();
            assertTrue(organisationUnit.isActive());
            assertTrue("OU not found: " + id, ArrayUtils.contains(expected, id));
        }
    }

    public void testDeleteOrgUnitCascade() throws Exception {
        try {
            organisationUnitService.deleteOrgUnitCascade(organisationUnitService.findById(IT_ORGUNIT_ID));
            //commitAndStartNewTx();
        } catch (Exception e) {            
            fail();
        }
    }

    public void testDeleteOrgUnitCascadePosition() throws Exception {
        IPositionService positionService = (IPositionService) getBean("positionService");
        try {
            positionService.deletePosition(new Long(-7));
            //commitAndStartNewTx();
        } catch (Exception e) {
            //e.printStackTrace();
            fail("exception generated! deleting a position" + e);
        }
    }

    private IOrganisationUnitService organisationUnitService;

    private static final Long IT_USER_ID = (long) -135;

    private static final Long MARKETING_ORGUNIT_ID = (long) -22;
    private static final Long ACCOUNT_ORGUNIT_ID = (long) -23;
    private static final Long IT_ORGUNIT_ID = (long) -24;
    private static final Long ENG_ORGUNIT_ID = (long) -25;
    private static final Long JAVA_ORGUNIT_ID = (long) -9;
    private static final Long DEV_ORGUNIT_ID = (long) -10;

    private static final Long[] ALL_ORGUNIT_IDS = new Long[]{DEFAULT_ORG_UNIT_ID, MARKETING_ORGUNIT_ID, ACCOUNT_ORGUNIT_ID, IT_ORGUNIT_ID, ENG_ORGUNIT_ID, DEV_ORGUNIT_ID, JAVA_ORGUNIT_ID};
}