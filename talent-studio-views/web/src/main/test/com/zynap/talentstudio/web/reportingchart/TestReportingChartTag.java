package com.zynap.talentstudio.web.reportingchart;

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionAssociation;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestReportingChartTag extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        lookupManager = (ILookupManager) applicationContext.getBean("lookupManager");
        reportingChartTag = new ReportingChartTag();
    }

    public void testGetPrimaryAssociatedPositions() throws Exception {

        Position targetPosition = new Position();
        Position primarySrcPosition = new Position();

        // add 1 primary and 1 secondary association
        Set targetAssociations = new HashSet();
        PositionAssociation primaryAssociation = new PositionAssociation(null, getPrimaryAssociationQualifier(), primarySrcPosition, targetPosition);
        targetAssociations.add(primaryAssociation);

        Position secondarySrcPosition = new Position();
        PositionAssociation secondaryAssociation = new PositionAssociation(null, getSecondaryAssociationQualifier(), secondarySrcPosition, targetPosition);
        targetAssociations.add(secondaryAssociation);

        // will return only 1 - the primary association
        targetPosition.setTargetAssociations(targetAssociations);
        final Collection primaryAssociatedPositions = reportingChartTag.getPrimaryAssociatedPositions(targetPosition);
        assertEquals(1, primaryAssociatedPositions.size());
        assertTrue(primaryAssociatedPositions.contains(primarySrcPosition));
    }

    public void testGetPrimaryAssociatedPositionsWithInactivePosition() throws Exception {

        Position targetPosition = new Position();
        Position srcPosition = new Position();
        srcPosition.setActive(false);

        // add 1 primary
        Set targetAssociations = new HashSet();
        PositionAssociation primaryAssociation = new PositionAssociation(null, getPrimaryAssociationQualifier(), srcPosition, targetPosition);
        targetAssociations.add(primaryAssociation);

        // should get none back as src is inactive
        targetPosition.setTargetAssociations(targetAssociations);
        final Collection primaryAssociatedPositions = reportingChartTag.getPrimaryAssociatedPositions(targetPosition);
        assertTrue(primaryAssociatedPositions.isEmpty());
    }

    public void testGetPrimaryAssociatedSubjects() throws Exception {

        Position targetPosition = new Position();
        Subject primarySrcSubject = new Subject();

        // add 1 primary and 1 secondary association
        Set subjectAssociations = new HashSet();
        SubjectAssociation primaryAssociation = new SubjectAssociation(null, getSubjectPrimaryAssociationQualifier(), primarySrcSubject, targetPosition);
        subjectAssociations.add(primaryAssociation);

        Subject secondarySrcSubject = new Subject();
        SubjectAssociation secondaryAssociation = new SubjectAssociation(null, getSubjectSecondaryAssociationQualifier(), secondarySrcSubject, targetPosition);
        subjectAssociations.add(secondaryAssociation);

        // will return only 1 - the primary association
        targetPosition.setSubjectAssociations(subjectAssociations);
        final Collection primaryAssociatedPositions = reportingChartTag.getPrimaryAssociatedSubjects(targetPosition);
        assertEquals(1, primaryAssociatedPositions.size());
        assertTrue(primaryAssociatedPositions.contains(primarySrcSubject));
    }

    public void testGetPrimaryAssociatedPositionsWithInactiveSubject() throws Exception {

        Position targetPosition = new Position();
        Subject srcSubject = new Subject();
        srcSubject.setActive(false);

        // add 1 primary
        Set subjectAssociations = new HashSet();
        SubjectAssociation primaryAssociation = new SubjectAssociation(null, getPrimaryAssociationQualifier(), srcSubject, targetPosition);
        subjectAssociations.add(primaryAssociation);

        // should get none back as src is inactive
        targetPosition.setSubjectAssociations(subjectAssociations);
        final Collection primaryAssociatedPositions = reportingChartTag.getPrimaryAssociatedSubjects(targetPosition);
        assertTrue(primaryAssociatedPositions.isEmpty());
    }

    private LookupValue getSubjectPrimaryAssociationQualifier() throws Exception {
        return (LookupValue) lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC).getLookupValues().iterator().next();
    }

    private LookupValue getSubjectSecondaryAssociationQualifier() throws Exception {
        return (LookupValue) lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC).getLookupValues().iterator().next();
    }

    private LookupValue getPrimaryAssociationQualifier() throws Exception {
        return (LookupValue) lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC).getLookupValues().iterator().next();
    }

    private LookupValue getSecondaryAssociationQualifier() throws Exception {
        return (LookupValue) lookupManager.findLookupType(ILookupManager.LOOKUP_TYPE_SECONDARY_POSITION_ASSOC).getLookupValues().iterator().next();
    }

    private ILookupManager lookupManager;
    private ReportingChartTag reportingChartTag;
}