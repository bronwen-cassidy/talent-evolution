package com.zynap.talentstudio.web.security.area;

/**
 * User: amark
 * Date: 20-Mar-2005
 * Time: 14:48:38
 */

import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.CoreDetail;
import junit.framework.TestCase;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestAreaWrapperBean extends TestCase {

    protected void setUp() throws Exception {
        areaWrapperBean = new AreaWrapperBean(new Area(), new HashSet<AreaElement>());
    }

    public void testResetIds() throws Exception {

        areaWrapperBean.getArea().setId(new Long(10));
        final OrganisationUnit newOrganisationUnit = new OrganisationUnit();
        areaWrapperBean.addOrganisationUnit(newOrganisationUnit);

        areaWrapperBean.resetIds();

        assertNull(areaWrapperBean.getId());
        final AreaElement orgUnitAreaElement = areaWrapperBean.getOrganisationUnits().iterator().next();
        assertNull(orgUnitAreaElement.getId());
        assertEquals(newOrganisationUnit, orgUnitAreaElement.getNode());
    }

    public void testAddOrganisationUnit() throws Exception {

        final Subject subject = new Subject();
        areaWrapperBean.addSubject(subject);

        final OrganisationUnit newOrganisationUnit = new OrganisationUnit();
        areaWrapperBean.addOrganisationUnit(newOrganisationUnit);

        final Collection organisationUnits = areaWrapperBean.getOrganisationUnits();
        assertEquals(1, organisationUnits.size());
        final AreaElement orgUnitAreaElement = (AreaElement) organisationUnits.iterator().next();
        assertNull(orgUnitAreaElement.getId());
        assertEquals(newOrganisationUnit, orgUnitAreaElement.getNode());
    }

    public void testGetOrganisationUnits() throws Exception {

        final Position position = new Position();
        areaWrapperBean.addPosition(position);

        final OrganisationUnit newOrganisationUnit = new OrganisationUnit();
        areaWrapperBean.addOrganisationUnit(newOrganisationUnit);
        areaWrapperBean.addOrganisationUnit(null);

        final Collection organisationUnits = areaWrapperBean.getOrganisationUnits();
        assertEquals(1, organisationUnits.size());
    }

    public void testAddDuplicateOrganisationUnit() throws Exception {

        // add the same organisation unit twice
        final OrganisationUnit newOrganisationUnit = new OrganisationUnit();
        areaWrapperBean.addOrganisationUnit(newOrganisationUnit);
        areaWrapperBean.addOrganisationUnit(newOrganisationUnit);

        // check that the duplicate is not added
        final Collection organisationUnits = areaWrapperBean.getOrganisationUnits();
        assertEquals(1, organisationUnits.size());
    }

    public void testClearOrganisationUnits() throws Exception {

        final Position position = new Position();
        areaWrapperBean.addPosition(position);

        final OrganisationUnit newOrganisationUnit = new OrganisationUnit();
        areaWrapperBean.addOrganisationUnit(newOrganisationUnit);

        final Collection organisationUnits = areaWrapperBean.getOrganisationUnits();
        assertEquals(1, organisationUnits.size());

        areaWrapperBean.clearOrganisationUnits();
        final Collection newOrgUnits = areaWrapperBean.getOrganisationUnits();
        assertTrue(newOrgUnits.isEmpty());

        assertEquals(1, areaWrapperBean.getPositions().size());
    }

    public void testGetAssignedOrganisationUnits() throws Exception {

        Area area = new Area();
        Set assignedAreaElements = new HashSet();

        final AreaElement ouAreaElement1 = new AreaElement(new Long(2), new OrganisationUnit(new Long(2), "ou1"), area);
        assignedAreaElements.add(ouAreaElement1);

        final AreaElement ouAreaElement2 = new AreaElement(new Long(1), new OrganisationUnit(new Long(2), "ou2"), area);
        assignedAreaElements.add(ouAreaElement2);

        area.setAreaElements(assignedAreaElements);
        areaWrapperBean = new AreaWrapperBean(area, area.getAreaElements());

        assertEquals(2, areaWrapperBean.getAssignedOrganisationUnits().size());
    }

    public void testRemoveOrgUnitsWithExistingArea() throws Exception {

        final OrganisationUnit organisationUnit1 = new OrganisationUnit(new Long(1), "ou1");
        final OrganisationUnit organisationUnit2 = new OrganisationUnit(new Long(2), "ou2");

        // add two org units to an area
        Area area = new Area();
        area.addAreaElement(new AreaElement(organisationUnit1, true));
        area.addAreaElement(new AreaElement(organisationUnit2, true));

        // check area has correct number of elements
        assertEquals(2, area.getAreaElements().size());

        // check area wrapper has correct number of elements
        areaWrapperBean = new AreaWrapperBean(area, area.getAreaElements());
        assertEquals(2, areaWrapperBean.getAssignedOrganisationUnits().size());

        // add a new ou to the area wrapper and check size - does not change until setOrgUnits is invoked
        final OrganisationUnit organisationUnit3 = new OrganisationUnit(new Long(3), "ou3");
        areaWrapperBean.addOrganisationUnit(organisationUnit3);
        assertEquals(2, areaWrapperBean.getAssignedOrganisationUnits().size());

        // set org unit ids - there should now be 2 assigned org units - the original one and the new one
        areaWrapperBean.setOrgUnitIds(new Long[]{organisationUnit2.getId(), organisationUnit3.getId()});
        assertEquals(2, areaWrapperBean.getAssignedOrganisationUnits().size());

        // remove org units - should remove all the org units
        areaWrapperBean.removeOrgUnits();
        assertEquals(0, areaWrapperBean.getAssignedOrganisationUnits().size());

        areaWrapperBean.addOrganisationUnit(organisationUnit1);
        areaWrapperBean.addOrganisationUnit(organisationUnit2);
        areaWrapperBean.addOrganisationUnit(organisationUnit3);
        assertEquals(3, areaWrapperBean.getOrganisationUnits().size());
        assertEquals(0, areaWrapperBean.getAssignedOrganisationUnits().size());

        areaWrapperBean.setOrgUnitIds(new Long[]{organisationUnit3.getId()});
        assertEquals(1, areaWrapperBean.getAssignedOrganisationUnits().size());

        // check that the new organisation unit is the one that is present
        final AreaElement assignedAreaElement = areaWrapperBean.getAssignedOrganisationUnits().iterator().next();
        assertEquals(organisationUnit3, assignedAreaElement.getNode());
    }

    public void testRemoveOrgUnits() throws Exception {

        Area area = new Area();
        areaWrapperBean = new AreaWrapperBean(area, new HashSet<AreaElement>());

        final OrganisationUnit organisationUnit1 = new OrganisationUnit(new Long(1), "ou1");
        areaWrapperBean.addOrganisationUnit(organisationUnit1);

        final OrganisationUnit organisationUnit2 = new OrganisationUnit(new Long(2), "ou2");
        areaWrapperBean.addOrganisationUnit(organisationUnit2);

        assertEquals(0, areaWrapperBean.getAssignedOrganisationUnits().size());

        areaWrapperBean.setOrgUnitIds(new Long[]{organisationUnit1.getId()});
        assertEquals(1, areaWrapperBean.getAssignedOrganisationUnits().size());

        areaWrapperBean.removeOrgUnits();
        assertEquals(0, areaWrapperBean.getAssignedOrganisationUnits().size());
    }

    public void testRemovalOfAreaElements() {

        final OrganisationUnit organisationUnit = new OrganisationUnit(new Long(1), "ou");
        final Position position = new Position(new Long(2), "title");
        final Subject subject = new Subject(new Long(3), new CoreDetail("first", "last"));

        areaWrapperBean.addOrganisationUnit(organisationUnit);
        areaWrapperBean.addPosition(position);
        areaWrapperBean.addSubject(subject);

        assertEquals(1, areaWrapperBean.getOrganisationUnits().size());
        assertEquals(1, areaWrapperBean.getSubjects().size());
        assertEquals(1, areaWrapperBean.getPositions().size());

        areaWrapperBean.setOrgUnitIds(new Long[]{organisationUnit.getId()});
        areaWrapperBean.setPositionIds(new Long[]{position.getId()});
        areaWrapperBean.setSubjectIds(new Long[]{subject.getId()});
        assertEquals(3, areaWrapperBean.getModifiedArea().getAreaElements().size());

        areaWrapperBean.removeOrgUnits();
        assertEquals(2, areaWrapperBean.getModifiedArea().getAreaElements().size());
        assertEquals(1, areaWrapperBean.getAssignedPositions().size());
        assertEquals(1, areaWrapperBean.getAssignedSubjects().size());
    }

    public void testRemoveAssignedAreaElements() throws Exception {

        final OrganisationUnit organisationUnit = new OrganisationUnit(new Long(1), "ou");
        final OrganisationUnit organisationUnit2 = new OrganisationUnit(new Long(2), "ou");
        final Position position = new Position(new Long(3), "title");
        final Subject subject = new Subject(new Long(4), new CoreDetail("first", "last"));

        // add two org units to an area
        Area area = new Area();
        area.addAreaElement(new AreaElement(organisationUnit, true));
        area.addAreaElement(new AreaElement(organisationUnit2, true));
        area.addAreaElement(new AreaElement(position, true));
        area.addAreaElement(new AreaElement(subject, true));
        areaWrapperBean = new AreaWrapperBean(area, area.getAreaElements());

        // check that they are not in the search result collections
        assertEquals(0, areaWrapperBean.getOrganisationUnits().size());
        assertEquals(0, areaWrapperBean.getPositions().size());
        assertEquals(0, areaWrapperBean.getSubjects().size());

        // check that they are in the assigned collections
        assertEquals(2, areaWrapperBean.getAssignedOrganisationUnits().size());
        assertEquals(1, areaWrapperBean.getAssignedPositions().size());
        assertEquals(1, areaWrapperBean.getAssignedSubjects().size());

        // set ids shold remove 1 org unit
        areaWrapperBean.setOrgUnitIds(new Long[]{organisationUnit.getId()});
        assertEquals(1, areaWrapperBean.getAssignedOrganisationUnits().size());

        // set ids to blank should remove all orgunits
        areaWrapperBean.setOrgUnitIds(new Long[]{});
        assertEquals(0, areaWrapperBean.getAssignedOrganisationUnits().size());
    }

    AreaWrapperBean areaWrapperBean;
}