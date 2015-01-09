/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation;

import com.zynap.talentstudio.ZynapDatabaseTestCase;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestOrganisationUnitHierarchy extends ZynapDatabaseTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        organisationUnitService = (IOrganisationUnitService) getBean("organisationUnitService");
    }

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    public void testFindOrgUnitByPositionId() throws Exception {
        final OrganisationUnit orgUnit = organisationUnitService.findOrgUnitByPositionId(HEAD_OF_ACCOUNTS_ID.toString());
        assertEquals(ACCOUNTS_OU_ID, orgUnit.getId());
        assertTrue(orgUnit.isActive());
    }

    public void testFindInactiveOrgUnitByPositionId() throws Exception {
        final OrganisationUnit orgUnit = organisationUnitService.findOrgUnitByPositionId(HEAD_OF_MARKETING_ID.toString());
        assertEquals(MARKETING_OU_ID, orgUnit.getId());
        assertFalse(orgUnit.isActive());
    }

    public void testFindOrgUnitBySubjectId() throws Exception {

        OrganisationUnit orgUnit = organisationUnitService.findOrgUnitBySubjectId(HEAD_OF_MARKETING_HOLDER_ID.toString());
        assertEquals(MARKETING_OU_ID, orgUnit.getId());

        orgUnit = organisationUnitService.findOrgUnitBySubjectId(HEAD_OF_ACCOUNTS_HOLDER_ID.toString());
        assertEquals(ACCOUNTS_OU_ID, orgUnit.getId());
    }

    public void testFindOrgUnitByUserId() throws Exception {
        OrganisationUnit orgUnit = organisationUnitService.findOrgUnitBySubjectUserId(HEAD_OF_MARKETING_HOLDER_USER_ID.toString());
        assertEquals(MARKETING_OU_ID, orgUnit.getId());
    }

    public void testFindValidParentsDefaultOrgUnit() throws Exception {
        final List validParents = organisationUnitService.findValidParents(DEFAULT_ORG_UNIT_ID, ROOT_USER_ID);
        assertTrue(validParents.isEmpty());
    }

    public void testFindValidParentsBottomOrgUnit() throws Exception {
        final Long orgUnitId = JAVA_OU_ID;

        //noinspection unchecked
        final List<OrganisationUnit> validParents = organisationUnitService.findValidParents(orgUnitId, ROOT_USER_ID);
        assertFalse(validParents.isEmpty());

        // check org units are all active
        assertActive(validParents);

        // check that all other org units are present
        //noinspection unchecked
        final List<OrganisationUnit> all = organisationUnitService.findAllSecure();
        for (OrganisationUnit organisationUnit: all) {
            if (!organisationUnit.getId().equals(orgUnitId)) {
                assertTrue(validParents.contains(organisationUnit));
            }
        }
    }

    public void testFindValidParents() throws Exception {
        final Long orgUnitId = IT_OU_ID;

        //noinspection unchecked
        final List<OrganisationUnit> validParents = organisationUnitService.findValidParents(orgUnitId, ROOT_USER_ID);
        assertFalse(validParents.isEmpty());

        // check org units are all active
        assertActive(validParents);

        // IT is child of default org unit
        // default org unit and accounts are the only available parent
        assertEquals(2, validParents.size());
        assertEquals(DEFAULT_ORG_UNIT_ID, validParents.get(0).getId());
        assertEquals(validParents.get(1).getId(), ACCOUNTS_OU_ID);

        checkOrder(validParents);
    }

    public void testFindAll() throws Exception {
        //noinspection unchecked
        final List<OrganisationUnit> all = organisationUnitService.findAllSecure();
        assertTrue(all.contains(organisationUnitService.findByID(DEFAULT_ORG_UNIT_ID)));

        // check org units are all active
        assertActive(all);
    }

    public void testFindHierarchy() throws Exception {
        final List hierarchy = organisationUnitService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID);
        final int size = hierarchy.size();
        assertTrue(size > 0);

        // first one will be default
        OrganisationUnit firstOrgUnit = (OrganisationUnit) hierarchy.get(0);
        assertEquals(DEFAULT_ORG_UNIT_ID, firstOrgUnit.getId());
        assertTrue(firstOrgUnit.isDefault());

        // check all other org units are active and are not default ones
        for (int i = 1; i < size; i++) {
            OrganisationUnit orgUnit = (OrganisationUnit) hierarchy.get(i);
            assertFalse(orgUnit.isDefault());
            assertTrue(orgUnit.isActive());
        }

        // check that last child org unit is the Java one and has no children
        final OrganisationUnit lastOrgUnit = (OrganisationUnit) hierarchy.get(size - 1);
        assertTrue(lastOrgUnit.getChildren().isEmpty());
        assertEquals(JAVA_OU_ID, lastOrgUnit.getId());

        checkOrder(hierarchy);
    }

    public void testFindOrgUnitTree() throws Exception {
        // for the administrator the secure hierarchy must be the same as the real hierarchy
        List secureHierarchy = organisationUnitService.findSecureOrgUnitTree(ADMINISTRATOR_USER_ID);
        final List hierarchy = organisationUnitService.findOrgUnitTree(DEFAULT_ORG_UNIT_ID);
        assertEquals(hierarchy, secureHierarchy);
    }

    private void checkOrder(List orgUnits) {

        Long[] expected = {DEFAULT_ORG_UNIT_ID, ACCOUNTS_OU_ID, IT_OU_ID, ENGINEERING_OU_ID, DEVELOPMENT_OU_ID, JAVA_OU_ID};

        int count = 0;
        for (Iterator iterator = orgUnits.iterator(); iterator.hasNext(); count++) {
            OrganisationUnit organisationUnit = (OrganisationUnit) iterator.next();
            assertEquals(expected[count], organisationUnit.getId());
        }
    }

    private void assertActive(Collection<OrganisationUnit> orgUnits) throws Exception {
        for (OrganisationUnit organisationUnit : orgUnits) {
            assertTrue(organisationUnit.getLabel(), organisationUnit.isActive());
        }
    }

    private IOrganisationUnitService organisationUnitService;

    private static final Long MARKETING_OU_ID = (long) 2;
    private static final Long ACCOUNTS_OU_ID = (long) 3;
    private static final Long IT_OU_ID = (long) 4;
    private static final Long ENGINEERING_OU_ID = (long) 5;
    private static final Long JAVA_OU_ID = (long) 9;
    private static final Long DEVELOPMENT_OU_ID = (long) 10;

    private static final Long HEAD_OF_MARKETING_ID = (long) 6;
    private static final Long HEAD_OF_ACCOUNTS_ID = (long) 7;

    private static final Long HEAD_OF_MARKETING_HOLDER_ID = (long) -33;
    private static final Long HEAD_OF_ACCOUNTS_HOLDER_ID = (long) -34;

    private static final Long HEAD_OF_MARKETING_HOLDER_USER_ID = (long) -133;
}