package com.zynap.talentstudio.analysis;

/**
 * User: amark
 * Date: 05-Oct-2005
 * Time: 13:15:33
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.Questionnaire;

public class TestAnalysisAttributeHelper extends ZynapTestCase {

    public void testSplitQuestionCriteriaId() throws Exception {
        AnalysisParameter attribute = AnalysisAttributeHelper.splitQuestionCriteriaId(null);
        assertNull(attribute);

        attribute = AnalysisAttributeHelper.splitQuestionCriteriaId("2_1_3");
        assertEquals(new Long(1), attribute.getQuestionnaireWorkflowId());
        assertEquals("2", attribute.getName());
        assertEquals("3", attribute.getRole());

        attribute = AnalysisAttributeHelper.splitQuestionCriteriaId("subject.test.2_1");
        assertEquals(new Long(1), attribute.getQuestionnaireWorkflowId());
        assertEquals("subject.test.2", attribute.getName());
        assertNull(attribute.getRole());
    }

    public void testGetAssociationCollectionPrefix() throws Exception {
        String name = "subjectPrimaryAssociations.subject.39";
        final String collectionPrefix = AnalysisAttributeHelper.getCollectionPrefix(name);
        assertEquals("subjectPrimaryAssociations", collectionPrefix);
    }

    public void testGetNestedAssociationCollectionPrefix() throws Exception {
        String name = "subjectPrimaryAssociations.subject.subjectSecondaryAssociations.position.39";
        final String collectionPrefix = AnalysisAttributeHelper.getCollectionPrefix(name);
        assertEquals("subjectPrimaryAssociations", collectionPrefix);
    }

    public void testGetCollectionPrefix() throws Exception {
        String name = "sourceAssociations.target.organisationUnit.label";
        final String collectionPrefix = AnalysisAttributeHelper.getCollectionPrefix(name);
        assertEquals("sourceAssociations", collectionPrefix);
    }

    public void testBuildQuestionCriteriaId() throws Exception {

        final String questionId = "parent.test";
        final Long workflowId = new Long(9);
        final Long roleId = new Long(25);
        final Long nullRoleId = null;

        String output = AnalysisAttributeHelper.buildQuestionCriteriaId(null, null, nullRoleId);
        assertNull(output);

        output = AnalysisAttributeHelper.buildQuestionCriteriaId(questionId, workflowId, roleId);
        assertEquals("parent.test_9_25", output);

        output = AnalysisAttributeHelper.buildQuestionCriteriaId(questionId, workflowId, nullRoleId);
        assertEquals("parent.test_9", output);
    }

    public void testBuildQuestionCriteriaIdUsingAnalysisParameter() throws Exception {

        String output = AnalysisAttributeHelper.buildQuestionCriteriaId(null);
        assertNull(output);

        AnalysisParameter attribute = AnalysisAttributeHelper.getAttributeFromName("subject.test.1_2");
        output = AnalysisAttributeHelper.buildQuestionCriteriaId(attribute);
        assertEquals("subject.test.1_2", output);

        attribute = AnalysisAttributeHelper.getAttributeFromName("1_2_3");
        output = AnalysisAttributeHelper.buildQuestionCriteriaId(attribute);
        assertEquals("1_2_3", output);
    }

    public void testBuildQuestionCriteriaIdUsingDynamicAttribute() throws Exception {

        final DynamicAttribute dynamicAttribute = new DynamicAttribute(new Long(89), "number", DynamicAttribute.DA_TYPE_NUMBER);
        final Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestionnaireWorkflowId(new Long(78));
        String output = AnalysisAttributeHelper.buildQuestionCriteriaId(dynamicAttribute, questionnaire);
        assertEquals("89_78", output);
    }

    public void testGetAttributeFromName() throws Exception {
        AnalysisParameter attribute = AnalysisAttributeHelper.getAttributeFromName(null);
        assertNotNull(attribute);

        assertNull(attribute.getName());
        assertNull(attribute.getQuestionnaireWorkflowId());
        assertNull(attribute.getRole());

        attribute = AnalysisAttributeHelper.getAttributeFromName("2_1_3");
        assertEquals(new Long(1), attribute.getQuestionnaireWorkflowId());
        assertEquals("2", attribute.getName());
        assertEquals("3", attribute.getRole());

        attribute = AnalysisAttributeHelper.getAttributeFromName("1");
        assertEquals("1", attribute.getName());
        assertNull(attribute.getQuestionnaireWorkflowId());
        assertNull(attribute.getRole());
    }

    public void testGetName() throws Exception {

        String name = AnalysisAttributeHelper.getName(null);
        assertNull(name);

        AnalysisParameter attribute = AnalysisAttributeHelper.getAttributeFromName("subject.test.1_2");
        name = AnalysisAttributeHelper.getName(attribute);
        assertEquals("subject.test.1_2", name);

        attribute = AnalysisAttributeHelper.getAttributeFromName("parent.title");
        name = AnalysisAttributeHelper.getName(attribute);
        assertEquals("parent.title", name);
    }

    public void testIsPersonMandatoryCore() throws Exception {

        String[] optionalValues = new String[]{AnalysisAttributeHelper.PERSON_TITLE_ATTR, AnalysisAttributeHelper.PREF_NAME_ATTR};
        for (int i = 0; i < optionalValues.length; i++) {
            String testValue = optionalValues[i];
            assertFalse(AnalysisAttributeHelper.isPersonMandatoryCore(testValue));
        }

        String[] mandatoryValues = new String[]{AnalysisAttributeHelper.FIRST_NAME_ATTR, AnalysisAttributeHelper.SECOND_NAME_ATTR};
        for (int i = 0; i < mandatoryValues.length; i++) {
            String testValue = mandatoryValues[i];
            assertTrue(AnalysisAttributeHelper.isPersonMandatoryCore(testValue));
        }
    }

    public void testIsPositionMandatoryCore() throws Exception {

        String[] optionalValues = new String[]{"39", "sourceDerivedAttributes[12]", "1_2_peer"};
        for (int i = 0; i < optionalValues.length; i++) {
            String testValue = optionalValues[i];
            assertFalse(AnalysisAttributeHelper.isPositionMandatoryCore(testValue));
        }

        String[] mandatoryValues = new String[]{AnalysisAttributeHelper.POSITION_TITLE_ATTR, AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, AnalysisAttributeHelper.ORG_UNIT_LABEL_ATTR};
        for (int i = 0; i < mandatoryValues.length; i++) {
            String testValue = mandatoryValues[i];
            assertTrue(AnalysisAttributeHelper.isPositionMandatoryCore(testValue));
        }
    }

    public void testRemovePrefix() throws Exception {

        String output = AnalysisAttributeHelper.removePrefix(null);
        assertEquals("", output);

        String prefix = "title";
        output = AnalysisAttributeHelper.removePrefix(prefix);
        assertEquals(prefix, output);

        prefix = "organisationUnit.label";
        output = AnalysisAttributeHelper.removePrefix(prefix);
        assertEquals(prefix, output);

        prefix = "parent.organisationUnit.label";
        output = AnalysisAttributeHelper.removePrefix(prefix);
        assertEquals("organisationUnit.label", output);

        prefix = "subjectPrimaryAssociations.subject.coreDetail.title";
        output = AnalysisAttributeHelper.removePrefix(prefix);
        assertEquals("coreDetail.title", output);

        prefix = "subjectPrimaryAssociations.subject.user.loginInfo.username";
        output = AnalysisAttributeHelper.removePrefix(prefix);
        assertEquals("user.loginInfo.username", output);

        prefix = "subjectPrimaryAssociations.position.comments";
        output = AnalysisAttributeHelper.removePrefix(prefix);
        assertEquals("comments", output);
    }

    public void testGetPrefix() throws Exception {

        String output = AnalysisAttributeHelper.getPrefix(null);
        assertEquals("", output);

        String prefix = "title";
        output = AnalysisAttributeHelper.getPrefix(prefix);
        assertEquals("", output);

        prefix = "organisationUnit.label";
        output = AnalysisAttributeHelper.getPrefix(prefix);
        assertEquals("", output);

        prefix = "parent.organisationUnit.label";
        output = AnalysisAttributeHelper.getPrefix(prefix);
        assertEquals(IPopulationEngine.PARENT_ATTR, output);

        prefix = "subjectPrimaryAssociations.subject.coreDetail.title";
        output = AnalysisAttributeHelper.getPrefix(prefix);
        assertEquals("subjectPrimaryAssociations.subject", output);

        prefix = "subjectPrimaryAssociations.subject.user.loginInfo.username";
        output = AnalysisAttributeHelper.getPrefix(prefix);
        assertEquals("subjectPrimaryAssociations.subject", output);

        prefix = "subjectPrimaryAssociations.position.comments";
        output = AnalysisAttributeHelper.getPrefix(prefix);
        assertEquals("subjectPrimaryAssociations.position", output);

        prefix = "subjectPrimaryAssociations.position.subjectSecondaryAssociations.subject.coreDetail.firstName";
        output = AnalysisAttributeHelper.getPrefix(prefix);
        assertEquals("subjectPrimaryAssociations.position.subjectSecondaryAssociations.subject", output);
    }

    public void testIsCoreDetailAttribute() throws Exception {
        assertFalse(AnalysisAttributeHelper.isCoreDetailAttribute(null));
        assertFalse(AnalysisAttributeHelper.isCoreDetailAttribute(""));

        assertTrue(AnalysisAttributeHelper.isCoreDetailAttribute(AnalysisAttributeHelper.FIRST_NAME_ATTR));

        String name = "subjectPrimaryAssociations.position.subjectSecondaryAssociations.subject.coreDetail.firstName";
        assertTrue(AnalysisAttributeHelper.isCoreDetailAttribute(name));
    }

    public void testIsRelatedUserAttribute() throws Exception {
        assertFalse(AnalysisAttributeHelper.isRelatedUserAttribute(null));
        assertFalse(AnalysisAttributeHelper.isRelatedUserAttribute(""));

        assertTrue(AnalysisAttributeHelper.isRelatedUserAttribute(AnalysisAttributeHelper.USER_PREFIX));

        String name = "subjectPrimaryAssociations.position.subjectSecondaryAssociations.subject.user.loginInfo.username";
        assertTrue(AnalysisAttributeHelper.isRelatedUserAttribute(name));
    }

    public void testIsOrgUnitAttribute() throws Exception {
        assertFalse(AnalysisAttributeHelper.isOrgUnitAttribute(null));
        assertFalse(AnalysisAttributeHelper.isOrgUnitAttribute(""));

        assertTrue(AnalysisAttributeHelper.isOrgUnitAttribute(AnalysisAttributeHelper.ORG_UNIT_LABEL_ATTR));

        String name = "subjectPrimaryAssociations.position.organisationUnit.label";
        assertTrue(AnalysisAttributeHelper.isOrgUnitAttribute(name));
    }

    public void testIsPositionLinkAttribute() throws Exception {
        assertFalse(AnalysisAttributeHelper.isPositionLinkAttribute(null));
        assertFalse(AnalysisAttributeHelper.isPositionLinkAttribute(""));

        assertTrue(AnalysisAttributeHelper.isPositionLinkAttribute(AnalysisAttributeHelper.POSITION_TITLE_ATTR));

        String name = "subjectPrimaryAssociations.position." + AnalysisAttributeHelper.POSITION_TITLE_ATTR;
        assertTrue(AnalysisAttributeHelper.isPositionLinkAttribute(name));
    }

    public void testIsQualifierAttribute() throws Exception {
        assertFalse(AnalysisAttributeHelper.isQualifierAttribute(null));
        assertFalse(AnalysisAttributeHelper.isQualifierAttribute(""));

        assertTrue(AnalysisAttributeHelper.isQualifierAttribute(AnalysisAttributeHelper.QUALIFIER_LABEL_ATTR));

        String name = "subjectPrimaryAssociations." + AnalysisAttributeHelper.QUALIFIER_LABEL_ATTR;
        assertTrue(AnalysisAttributeHelper.isQualifierAttribute(name));
        assertTrue(AnalysisAttributeHelper.isQualifierAttribute("qualifier."));
    }

    public void testIsPersonLinkAttribute() throws Exception {
        assertFalse(AnalysisAttributeHelper.isPersonLinkAttribute(null));
        assertFalse(AnalysisAttributeHelper.isPersonLinkAttribute(""));

        String name = "subjectSecondaryAssociations.subject." + AnalysisAttributeHelper.PERSON_FULL_NAME_ATTR;
        assertTrue(AnalysisAttributeHelper.isPersonLinkAttribute(name));

        String name2 = "subjectSecondaryAssociations.subject." + AnalysisAttributeHelper.SECOND_NAME_ATTR;
        assertTrue(AnalysisAttributeHelper.isPersonLinkAttribute(name2));

        String name3 = "subjectSecondaryAssociations.subject." + AnalysisAttributeHelper.FIRST_NAME_ATTR;
        assertFalse(AnalysisAttributeHelper.isPersonLinkAttribute(name3));
    }

    public void testSupportsLink() throws Exception {
        assertFalse(AnalysisAttributeHelper.supportsLink(null));
        assertFalse(AnalysisAttributeHelper.supportsLink(AnalysisAttributeHelper.PERSON_TITLE_ATTR));
        assertFalse(AnalysisAttributeHelper.supportsLink(AnalysisAttributeHelper.FIRST_NAME_ATTR));

        assertTrue(AnalysisAttributeHelper.supportsLink(AnalysisAttributeHelper.POSITION_TITLE_ATTR));
        assertTrue(AnalysisAttributeHelper.supportsLink(AnalysisAttributeHelper.PERSON_FULL_NAME_ATTR));
    }

    public void testSplitQuestionnaireAttributeName() throws Exception {

        final AnalysisParameter analysisParameter = AnalysisAttributeHelper.getAttributeFromName("subjectPrimaryAssociations.subject.subjectSecondaryAssociations.position.1_2");
        final String[] output = AnalysisAttributeHelper.splitQuestionnaireAttributeName(analysisParameter);
        assertEquals(5, output.length);
        assertEquals("subjectPrimaryAssociations", output[0]);
        assertEquals("subject", output[1]);
        assertEquals("subjectSecondaryAssociations", output[2]);
        assertEquals("position", output[3]);
        assertEquals("1", output[4]);
    }

    public void testRemovePrefixAssociationQualifier() throws Exception {
        final String qualifierAttribute = "qualifier.label";
        final String result = AnalysisAttributeHelper.removePrefix(qualifierAttribute);
        assertEquals(qualifierAttribute, result);
    }

    public void testRemovePrefixAssociationComments() throws Exception {
        final String qualifierAttribute = "subjectSecondaryAssociations.comments";
        final String result = AnalysisAttributeHelper.removePrefix(qualifierAttribute);
        assertEquals("comments", result);
    }
}
