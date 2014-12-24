package com.zynap.talentstudio.web.common.validation;

/**
 * User: amark
 * Date: 29-Jun-2005
 * Time: 13:43:49
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionAssociation;
import com.zynap.talentstudio.organisation.subjects.SubjectPrimaryAssociation;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class TestAssociationValidationHelper extends ZynapTestCase {

    public void testIsCompleteNoQualifier() throws Exception {
        ArtefactAssociationWrapperBean artefactAssociation = getNewArtefactAssociationWrapper();
        assertFalse(AssociationValidationHelper.isComplete(artefactAssociation, false));
    }

    public void testIsCompleteNoTarget() throws Exception {
        ArtefactAssociationWrapperBean artefactAssociation = getNewArtefactAssociationWrapper();
        artefactAssociation.setQualifierId(new Long(10));
        artefactAssociation.setTargetId(null);
        // pass in true as we are looking at the targetId i.e. a position to position association
        assertFalse(AssociationValidationHelper.isComplete(artefactAssociation, true));
    }

    public void testIsComplete() throws Exception {
        ArtefactAssociationWrapperBean artefactAssociation = getNewArtefactAssociationWrapper();
        artefactAssociation.setQualifierId(new Long(10));
        // no source or target is validation will fail regardless hence does not matter what is entered here
        assertTrue(AssociationValidationHelper.isComplete(artefactAssociation, false));
    }

    public void testIsRecursive() throws Exception {

        // need source and target to be same
        ArtefactAssociationWrapperBean artefactAssociation = getNewArtefactAssociationWrapper();
        artefactAssociation.getSource().setId(artefactAssociation.getTargetId());
        assertTrue(AssociationValidationHelper.isRecursive(artefactAssociation));
    }

    public void testIsRecursiveNoTarget() throws Exception {

        // set target to null - cannot be recursive in this case
        ArtefactAssociationWrapperBean artefactAssociation = getNewArtefactAssociationWrapper();
        artefactAssociation.setTargetId(null);
        assertFalse(AssociationValidationHelper.isRecursive(artefactAssociation));
    }

    public void testCheckExistsFalsePositionToPosition() throws Exception {
        List<ArtefactAssociationWrapperBean> associations = new ArrayList<ArtefactAssociationWrapperBean>();

        LookupValue qualifier = new LookupValue("122");
        qualifier.setId(new Long(122));

        associations.add(new ArtefactAssociationWrapperBean(new PositionAssociation(null, qualifier, new Position(new Long(-1)), DEFAULT_POSITION)));
        associations.add(new ArtefactAssociationWrapperBean(new PositionAssociation(null, qualifier, new Position(new Long(-2)), DEFAULT_POSITION)));
        associations.add(new ArtefactAssociationWrapperBean(new PositionAssociation(null, qualifier, new Position(new Long(-3)), DEFAULT_POSITION)));

        for (Iterator iterator = associations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean bean = (ArtefactAssociationWrapperBean) iterator.next();
            boolean exists = AssociationValidationHelper.checkExists(associations, bean, false);
            assertFalse(exists);
        }
    }

    public void testCheckExistsTruePositionToPosition() throws Exception {
        List<ArtefactAssociationWrapperBean> associations = new ArrayList<ArtefactAssociationWrapperBean>();

        LookupValue qualifier = new LookupValue("122");
        qualifier.setId(new Long(122));

        associations.add(new ArtefactAssociationWrapperBean(new PositionAssociation(null, qualifier, new Position(new Long(-1)), DEFAULT_POSITION)));
        associations.add(new ArtefactAssociationWrapperBean(new PositionAssociation(null, qualifier, new Position(new Long(-1)), DEFAULT_POSITION)));
        associations.add(new ArtefactAssociationWrapperBean(new PositionAssociation(null, qualifier, new Position(new Long(-1)), DEFAULT_POSITION)));

        for (Iterator iterator = associations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean bean = (ArtefactAssociationWrapperBean) iterator.next();
            boolean exists = AssociationValidationHelper.checkExists(associations, bean, false);
            assertTrue(exists);
        }
    }

    public void testCheckExistsFalsePositionToSubject() throws Exception {
        List<ArtefactAssociationWrapperBean> associations = new ArrayList<ArtefactAssociationWrapperBean>();
        CoreDetail coreDetail = new CoreDetail("Mr", "junit", "test");

        LookupValue qualifier = new LookupValue("122");
        qualifier.setId(new Long(122));
        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, new Subject(new Long(-1), coreDetail), new Position(new Long(-1)))));
        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, new Subject(new Long(-2), coreDetail), new Position(new Long(-2)))));
        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, new Subject(new Long(-3), coreDetail), new Position(new Long(-3)))));

        for (Iterator iterator = associations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean bean = (ArtefactAssociationWrapperBean) iterator.next();
            boolean exists = AssociationValidationHelper.checkExists(associations, bean, false);
            assertFalse(exists);
        }
    }

    /**
     * Test exists from the subjects side. When the node is the subject node
     *
     * @throws Exception
     */
    public void testCheckExistsFalseSubjectToPosition() throws Exception {
        List<ArtefactAssociationWrapperBean> associations = new ArrayList<ArtefactAssociationWrapperBean>();
        CoreDetail coreDetail = new CoreDetail("Mr", "junit", "test");

        LookupValue qualifier = new LookupValue("122");
        qualifier.setId(new Long(122));

        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, new Subject(new Long(-1), coreDetail), new Position(new Long(-1)))));
        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, new Subject(new Long(-2), coreDetail), new Position(new Long(-2)))));
        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, new Subject(new Long(-3), coreDetail), new Position(new Long(-3)))));

        for (Iterator iterator = associations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean bean = (ArtefactAssociationWrapperBean) iterator.next();
            boolean exists = AssociationValidationHelper.checkExists(associations, bean, true);
            assertFalse(exists);
        }
    }

    public void testCheckExistsTrueSubjectToPosition() throws Exception {
        List<ArtefactAssociationWrapperBean> associations = new ArrayList<ArtefactAssociationWrapperBean>();
        CoreDetail coreDetail = new CoreDetail("Mr", "junit", "test");

        LookupValue qualifier = new LookupValue("122");
        qualifier.setId(new Long(122));
        Subject node = new Subject(new Long(-1), coreDetail);

        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, node, new Position(new Long(-1)))));
        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, node, new Position(new Long(-1)))));
        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, node, new Position(new Long(-1)))));

        for (Iterator iterator = associations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean bean = (ArtefactAssociationWrapperBean) iterator.next();
            boolean exists = AssociationValidationHelper.checkExists(associations, bean, true);
            assertTrue(exists);
        }
    }

    public void testCheckExistsTruePositionToSubject() throws Exception {
        List<ArtefactAssociationWrapperBean> associations = new ArrayList<ArtefactAssociationWrapperBean>();

        CoreDetail coreDetail = new CoreDetail("Mr", "junit", "test");
        LookupValue qualifier = new LookupValue("122");
        qualifier.setId(new Long(122));

        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, new Subject(new Long(-1), coreDetail), new Position(new Long(-1)))));
        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, new Subject(new Long(-1), coreDetail), new Position(new Long(-2)))));
        associations.add(new ArtefactAssociationWrapperBean(new SubjectPrimaryAssociation(null, qualifier, new Subject(new Long(-1), coreDetail), new Position(new Long(-3)))));

        for (Iterator iterator = associations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean bean = (ArtefactAssociationWrapperBean) iterator.next();
            boolean exists = AssociationValidationHelper.checkExists(associations, bean, false);
            assertTrue(exists);
        }
    }

    private ArtefactAssociationWrapperBean getNewArtefactAssociationWrapper() {
        final PositionAssociation association = new PositionAssociation(null, new LookupValue("111"), DEFAULT_POSITION, new Position(new Long(-99)));
        return new ArtefactAssociationWrapperBean(association);
    }
}