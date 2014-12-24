/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.display.tag;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 22-Jun-2007 13:11:59
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.web.display.support.ArtefactViewModelBuilder;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectCommonAssociation;
import com.zynap.talentstudio.organisation.subjects.SubjectPrimaryAssociation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestAbstractViewTag extends TestCase {

    public void testCreateCell() throws Exception {
        Subject node = createSubjectNode();

        Column column = new Column("Manager Name", "subjectPrimaryAssociations.position.parent.subjectPrimaryAssociations.subject.coreDetail.firstName", "TEXT");
        final Cell cell = ArtefactViewModelBuilder.createCell(node, column, null, null);

        assertTrue(cell instanceof Cell.ListCell);
        final List list = ((Cell.ListCell) cell).getValues();

        assertEquals(1, list.size());
        Cell cell1 = (Cell) list.get(0);
        final String actual = cell1.getValue();
        assertEquals("manager", actual);
    }

    public void testCreateCell_NoPosition() throws Exception {
        Subject node = createSubjectNode();
        node.setSubjectAssociations(new HashSet<SubjectCommonAssociation>());
        node.setSubjectPrimaryAssociations(new ArrayList<SubjectPrimaryAssociation>());

        Column column = new Column("Manager Name", "subjectPrimaryAssociations.position.parent.subjectPrimaryAssociations.subject.coreDetail.firstName", "TEXT");
        final Cell cell = ArtefactViewModelBuilder.createCell(node, column, null, null);

        assertTrue(cell instanceof Cell.ListCell);
        final List list = ((Cell.ListCell) cell).getValues();
        assertEquals(0, list.size());
        
        final String actual = cell.getValue();
        assertEquals("-", actual);    
    }

    public void testCreateCell_NotNested() throws Exception {
        Subject node = createSubjectNode();

        Column column = new Column("Org Unit", "subjectPrimaryAssociations.position.organisationUnit.label", "TEXT");
        final Cell cell = ArtefactViewModelBuilder.createCell(node, column, null, null);

        assertTrue(cell instanceof Cell.ListCell);
        final List list = ((Cell.ListCell) cell).getValues();

        assertEquals(1, list.size());
        Cell cell1 = (Cell) list.get(0);
        final String actual = cell1.getValue();
        assertEquals("My Org Unit", actual);
    }

    public void testCreateCell_PositionTitle() throws Exception {
        Subject node = createSubjectNode();

        Column column = new Column("job", "subjectPrimaryAssociations.position.title", "TEXT");
        final Cell cell = ArtefactViewModelBuilder.createCell(node, column, null, null);

        assertTrue(cell instanceof Cell.ListCell);
        final List list = ((Cell.ListCell) cell).getValues();

        assertEquals(1, list.size());
        Cell cell1 = (Cell) list.get(0);
        final String actual = cell1.getValue();
        assertEquals("test position", actual);
    }

    public void testCreateCell_DefaultCell() throws Exception {
        Subject node = createSubjectNode();

        Column column = new Column("First Name", "coreDetail.firstName", "TEXT");
        final Cell cell = ArtefactViewModelBuilder.createCell(node, column, null, null);
        final String actual = cell.getValue();
        assertEquals("xxxxxxx", actual);
    }

    public void testCreateCell_PositionNode() throws Exception {
        Subject subject = createSubjectNode();
        List<Position> nodes = subject.getPrimaryPositions();

        Column column = new Column("Holder", "subjectPrimaryAssociations.subject.coreDetail.name", "TEXT");
        final Cell cell = ArtefactViewModelBuilder.createCell(nodes.iterator().next(), column, null, null);

        assertTrue(cell instanceof Cell.ListCell);
        final List list = ((Cell.ListCell) cell).getValues();

        assertEquals(1, list.size());
        Cell cell1 = (Cell) list.get(0);
        final String actual = cell1.getValue();
        assertEquals("xxxxxxx xxxxxxxx", actual);
    }

    private Subject createSubjectNode() {
        Subject node = new Subject(new CoreDetail("xxxxxxx", "xxxxxxxx"));
        OrganisationUnit shared = new OrganisationUnit(new Long(-2), "My Org Unit");

        Position job = new Position(new Long(12), "test position", shared);
        SubjectPrimaryAssociation association = new SubjectPrimaryAssociation(PRIMARY_ASSOCIATION_QUALIFIER, node, job);
        List<SubjectPrimaryAssociation> assocs = new ArrayList<SubjectPrimaryAssociation>();
        assocs.add(association);
        node.setSubjectPrimaryAssociations(assocs);
        job.setSubjectPrimaryAssociations(assocs);

        job.setSubjectAssociations(job.getSubjectAssociations());


        Position jobParent = new Position(new Long(0), "happy", shared);
        Subject boss = new Subject(new CoreDetail("manager", "manager"));

        SubjectPrimaryAssociation manager = new SubjectPrimaryAssociation(PRIMARY_ASSOCIATION_QUALIFIER, boss, jobParent);
        List<SubjectPrimaryAssociation> managers = new ArrayList<SubjectPrimaryAssociation>();
        managers.add(manager);
        jobParent.setSubjectPrimaryAssociations(managers);
        job.setParent(jobParent);
        return node;
    }

    private static final LookupValue PRIMARY_ASSOCIATION_QUALIFIER = new LookupValue("ACTING", "Acting", "description", new LookupType(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC));
}