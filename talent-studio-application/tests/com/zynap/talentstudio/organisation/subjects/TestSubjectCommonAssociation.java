package com.zynap.talentstudio.organisation.subjects;

/**
 * User: amark
 * Date: 09-Feb-2006
 * Time: 12:17:34
 */

import com.zynap.talentstudio.ZynapTestCase;

public class TestSubjectCommonAssociation extends ZynapTestCase {

    public void testSubjectPrimaryAssociation() throws Exception {

        SubjectCommonAssociation subjectAssociation = new SubjectPrimaryAssociation();
        assertFalse(subjectAssociation.isSecondary());
        assertTrue(subjectAssociation.isPrimary());
    }

    public void testSubjectSecondaryAssociation() throws Exception {

        SubjectCommonAssociation subjectAssociation = new SubjectSecondaryAssociation();
        assertTrue(subjectAssociation.isSecondary());
        assertFalse(subjectAssociation.isPrimary());
    }

    SubjectAssociation subjectAssociation;
}