/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation;

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestOrganisationUnitService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        organisationUnitService = (IOrganisationUnitService) applicationContext.getBean("organisationUnitService");
    }

    public void testCreate() throws Exception {

        final OrganisationUnit created = new OrganisationUnit(null, "Miner");
        created.setParent(new OrganisationUnit(DEFAULT_ORG_UNIT_ID));
        organisationUnitService.create(created);

        final OrganisationUnit actual = organisationUnitService.findById(created.getId());
        assertEquals(created.getId(), actual.getId());
        assertEquals(created.getLabel(), actual.getLabel());
        assertEquals(created.getParent().getId(), actual.getParent().getId());
    }

    public void testFindByID() throws Exception {
        final OrganisationUnit defaultOrgUnit = organisationUnitService.findById(DEFAULT_ORG_UNIT_ID);
        assertNotNull(defaultOrgUnit);
    }

    public void testFindByInvalidID() throws Exception {
        try {
            organisationUnitService.findById(new Long(-99));
            fail("Exception expected as org unit does not exist");
        } catch (TalentStudioException expected) {
        }
    }

    public void testGetDefaultOrgUnit() throws Exception {
        final OrganisationUnit defaultOrgUnit = getDefaultOrgUnit();
        assertNotNull(defaultOrgUnit);
        assertTrue(defaultOrgUnit.isDefault());
    }

    public void testUpdate() throws Exception {

        String newLabel = "board";

        final OrganisationUnit defaultOrgUnit = getDefaultOrgUnit();
        defaultOrgUnit.setActive(false);
        defaultOrgUnit.setLabel(newLabel);

        organisationUnitService.update(defaultOrgUnit);

        final OrganisationUnit newOrgUnit = organisationUnitService.findById(defaultOrgUnit.getId());
        assertFalse(newOrgUnit.isActive());
        assertEquals(newLabel, newOrgUnit.getLabel());
        assertTrue(newOrgUnit.isDefault());
    }

    public void testUpdateMerge() throws Exception {

        final OrganisationUnit salesOrgUnit = new OrganisationUnit(null, "Sales");
        salesOrgUnit.setParent(new OrganisationUnit(DEFAULT_ORG_UNIT_ID));
        organisationUnitService.create(salesOrgUnit);

        final OrganisationUnit marketingOrgUnit = new OrganisationUnit(null, "Marketing");
        marketingOrgUnit.setParent(new OrganisationUnit(DEFAULT_ORG_UNIT_ID));
        organisationUnitService.create(marketingOrgUnit);
        organisationUnitService.updateMerge(salesOrgUnit, marketingOrgUnit);

        organisationUnitService.findById(salesOrgUnit.getId());

        // check marketing org unit has been deleted
        try {            
            organisationUnitService.findById(marketingOrgUnit.getId());
            fail("Incorrectly found deleted org unit");
        } catch (DomainObjectNotFoundException expected) {

        }
    }


    


    public void testSearch() throws Exception {

        final OrgUnitSearchQuery query = new OrgUnitSearchQuery();
        final Collection orgUnits = organisationUnitService.search(ADMINISTRATOR_USER_ID, query);
        assertNotNull(orgUnits);
        assertTrue(orgUnits.contains(getDefaultOrgUnit()));
    }

    public void testSearchWithQuotes() throws Exception {

        final OrganisationUnit created = new OrganisationUnit(null, "O'Reil\"ly% Vets");
        created.setParent(new OrganisationUnit(DEFAULT_ORG_UNIT_ID));
        organisationUnitService.create(created);

        final OrgUnitSearchQuery query = new OrgUnitSearchQuery();
        query.setLabel("o're");
        final Collection orgUnits = organisationUnitService.search(ADMINISTRATOR_USER_ID, query);
        assertNotNull(orgUnits);
        assertTrue(orgUnits.contains(created));
        assertFalse(orgUnits.contains(getDefaultOrgUnit()));
    }

    private OrganisationUnit getDefaultOrgUnit() throws TalentStudioException {
        return organisationUnitService.findById(DEFAULT_ORG_UNIT_ID);
    }    

    private IOrganisationUnitService organisationUnitService;
}