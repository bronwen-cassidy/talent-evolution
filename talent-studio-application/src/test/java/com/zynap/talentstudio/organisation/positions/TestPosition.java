package com.zynap.talentstudio.organisation.positions;

/**
 * User: amark
 * Date: 17-Jun-2005
 * Time: 14:31:28
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.ArtefactAssociationHelper;
import com.zynap.talentstudio.organisation.subjects.SubjectPrimaryAssociation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestPosition extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        lookupManager = (ILookupManager) applicationContext.getBean("lookupManager");
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

    public void testGetPrimaryAssociation() throws Exception {

        final LookupValue primaryAssociationQualifier = getPrimaryAssociationQualifier();

        Set sourceAssociations = new HashSet();
        ArtefactAssociation primaryAssociation1 = new PositionAssociation();
        primaryAssociation1.setQualifier(primaryAssociationQualifier);
        sourceAssociations.add(primaryAssociation1);

        Position position = new Position();
        position.setSourceAssociations(sourceAssociations);

        final PositionAssociation actual = ArtefactAssociationHelper.getPrimaryAssociation(position.getSourceAssociations());
        assertNotNull(actual);
        assertEquals(primaryAssociation1, actual);
    }

    public void testGetPrimaryAssociationNoneProvided() throws Exception {

        final LookupValue secondaryAssociationQualifier = getSecondaryAssociationQualifier();

        Set sourceAssociations = new HashSet();
        ArtefactAssociation secondaryAssociation1 = new PositionAssociation();
        secondaryAssociation1.setQualifier(secondaryAssociationQualifier);
        sourceAssociations.add(secondaryAssociation1);

        Position position = new Position();
        position.setSourceAssociations(sourceAssociations);

        final PositionAssociation actual = ArtefactAssociationHelper.getPrimaryAssociation(position.getSourceAssociations());
        assertNull(actual);
    }

    public void testGetSecondaryAssociations() throws Exception {

        final LookupValue secondaryAssociationQualifier = getSecondaryAssociationQualifier();

        Set sourceAssociations = new HashSet();
        ArtefactAssociation secondaryAssociation1 = new PositionAssociation();
        secondaryAssociation1.setQualifier(secondaryAssociationQualifier);
        sourceAssociations.add(secondaryAssociation1);

        Position position = new Position();
        position.setSourceAssociations(sourceAssociations);

        final Collection secondaryAssociations = ArtefactAssociationHelper.getSecondaryAssociations(position.getSourceAssociations());
        assertEquals(1, secondaryAssociations.size());
        assertTrue(secondaryAssociations.contains(secondaryAssociation1));
    }

    public void testGetSubjectPrimary() throws Exception {

        final LookupValue subjectPrimaryAssociationQualifier = getSubjectPrimaryAssociationQualifier();

        Set subjectPrimaryAssociations = new HashSet();
        ArtefactAssociation secondaryAssociation1 = new SubjectPrimaryAssociation();
        secondaryAssociation1.setQualifier(subjectPrimaryAssociationQualifier);
        subjectPrimaryAssociations.add(secondaryAssociation1);

        Position position = new Position();
        position.setSubjectPrimaryAssociations(subjectPrimaryAssociations);

        final Collection subPrimaryAssociations = position.getSubjectPrimaryAssociations();
        assertEquals(1, subPrimaryAssociations.size());
        assertTrue(subPrimaryAssociations.contains(secondaryAssociation1));
    }

    public void testGetSecondaryAssociationsNoneProvided() throws Exception {

        final LookupValue primaryAssociationQualifier = getPrimaryAssociationQualifier();

        Set sourceAssociations = new HashSet();
        ArtefactAssociation primaryAssociation1 = new PositionAssociation();
        primaryAssociation1.setQualifier(primaryAssociationQualifier);
        sourceAssociations.add(primaryAssociation1);

        Position position = new Position();
        position.setSourceAssociations(sourceAssociations);

        final Collection secondaryAssociations = ArtefactAssociationHelper.getSecondaryAssociations(position.getSourceAssociations());
        assertNotNull(secondaryAssociations);
        assertTrue(secondaryAssociations.isEmpty());
    }

    private ILookupManager lookupManager;
}