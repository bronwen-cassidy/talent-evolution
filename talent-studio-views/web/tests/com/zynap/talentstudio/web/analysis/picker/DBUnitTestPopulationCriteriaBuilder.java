/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.picker;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 06-Jul-2009 08:02:32
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireHelper;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.talentstudio.web.utils.tree.Leaf;
import com.zynap.talentstudio.web.analysis.populations.CriteriaWrapperBean;
import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.exception.TalentStudioException;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class DBUnitTestPopulationCriteriaBuilder extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();

        IDynamicAttributeService dynamicAttributeService = (IDynamicAttributeService) getBean("dynamicAttrService");
        IQuestionnaireService questionnaireService = (IQuestionnaireService) getBean("questionnaireService");
        final IQueWorkflowService questionnaireWorkflowService = (IQueWorkflowService) getBean("queWorkflowService");
        final IQueDefinitionService questionnaireDefinitionService = (IQueDefinitionService) getBean("questionnaireDefinitionService");
        final IPerformanceReviewService performanceReviewService = (IPerformanceReviewService) getBean("performanceReviewService");

        QuestionnaireHelper questionnaireHelper = new QuestionnaireHelper();
        questionnaireHelper.setDynamicAttributeService(dynamicAttributeService);
        questionnaireHelper.setQuestionnaireService(questionnaireService);
        questionnaireHelper.setQuestionnaireWorkflowService(questionnaireWorkflowService);
        questionnaireHelper.setQuestionnaireDefinitionService(questionnaireDefinitionService);

        populationCriteriaBuilder = new PopulationCriteriaBuilder();
        populationCriteriaBuilder.setDynamicAttributeService(dynamicAttributeService);
        populationCriteriaBuilder.setQuestionnaireHelper(questionnaireHelper);
        populationCriteriaBuilder.setQuestionnaireWorkflowService(questionnaireWorkflowService);
        populationCriteriaBuilder.setQuestionnaireDefinitionService(questionnaireDefinitionService);
        populationCriteriaBuilder.setPerformanceReviewService(performanceReviewService);

        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/test.xml");
        populationCriteriaBuilder.afterPropertiesSet();
    }

    public void testSetAttributeLabels() throws Exception {

        testLabel(IPopulationEngine.P_POS_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".subject.coreDetail.firstName", "First Name", " / Current Holders / First Name");
        testLabel(IPopulationEngine.P_POS_TYPE_, "sourceAssociations.target.title", "Title", " / Reports To / Title");
        testLabel(IPopulationEngine.P_POS_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".qualifier.label", "Type", " / Current Holders / Primary Associations / Type");

        testLabel(IPopulationEngine.P_SUB_TYPE_, "subjectSecondaryAssociations.position." + IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".subject.coreDetail.name", "Full Name", " / Succession Positions / Current Holders / Full Name");
        testLabel(IPopulationEngine.P_SUB_TYPE_, "subjectPrimaryAssociations.qualifier.label", "Type", " / Current Jobs / Primary Associations / Type");
        // association attributes
        testLabel(IPopulationEngine.P_SUB_TYPE_, "subjectPrimaryAssociations.position.subjectSecondaryAssociations.comments", "Comments", " / Current Jobs / Successors / Succession Associations / Comments");
        testLabel(IPopulationEngine.P_SUB_TYPE_, "subjectSecondaryAssociations.qualifier.label", "Type", " / Succession Positions / Succession Associations / Type");
        // questionnaire attributes
        testLabel(IPopulationEngine.P_POS_TYPE_, "subjectSecondaryAssociations.subject.1_2_419", "Exceptional", " / Successors / Appraisals / Q1 2006 / Q1 2006 ( Others ) (All People) / Exceptional");
        testLabel(IPopulationEngine.P_SUB_TYPE_, "1_2_419", "Exceptional", " / Appraisals / Q1 2006 / Q1 2006 ( Others ) (All People) / Exceptional");

    }

    public void testBuildCollection() throws Exception {

        final String artefactType = IPopulationEngine.P_SUB_TYPE_;

        final AnalysisAttributeCollection collection = populationCriteriaBuilder.buildCollection();
        assertNotNull(collection);

        final Map qualifierAttributes = collection.getQualifierAttributes();
        assertFalse(qualifierAttributes.isEmpty());
        for (Iterator iterator = qualifierAttributes.values().iterator(); iterator.hasNext();) {
            AttributeWrapperBean qualifierAttributeWrapper = (AttributeWrapperBean) iterator.next();
            assertEquals("qualifier.label", qualifierAttributeWrapper.getId());
            assertEquals("Type", qualifierAttributeWrapper.getLabel());

            final DynamicAttribute dynamicAttribute = qualifierAttributeWrapper.getAttributeDefinition();
            assertNotNull(dynamicAttribute);
            assertTrue(dynamicAttribute.isSelectionType());
            assertFalse(dynamicAttribute.getActiveLookupValues().isEmpty());
        }

        final AnalysisAttributeCollection viewCollection = populationCriteriaBuilder.buildCollection(artefactType, "ADD");
        assertNotNull(viewCollection);
        assertTrue(viewCollection.getQuestionnaireDefinitions().isEmpty());
    }

    public void testAfterPropertiesSet() throws Exception {

        // check that useSearchableExtendedAttributesOnly has been set and that type has been set from extendedattributes tag
        assertTrue(populationCriteriaBuilder.useSearchableExtendedAttributesOnly);

        // check that extended attributes types were set
        assertEquals(DynamicAttribute.DA_TYPE_STRUCT, populationCriteriaBuilder.extendedAttributeTypes[0]);

        // check that includeDerivedAttributes has been set from derivedattributes tag
        assertTrue(populationCriteriaBuilder.includeDerivedAttributes);

        // check that includeDynamicLineItemAttributes has been set
        assertTrue(populationCriteriaBuilder.includeDynamicLineItemAttributes);

        // check that qualifier attribute has been set
        final List qualifierAttributes = populationCriteriaBuilder.getQualifierAttributes();
        assertEquals(1, qualifierAttributes.size());
        final AttributeWrapperBean qualifierAttribute = (AttributeWrapperBean) qualifierAttributes.get(0);
        assertEquals("Type", qualifierAttribute.getLabel());
        assertEquals(AnalysisAttributeHelper.QUALIFIER_LABEL_ATTR, qualifierAttribute.getId());
    }

    public void testUpdateBranchWithQuestionnaires() throws Exception {

        final String artefactType = IPopulationEngine.P_SUB_TYPE_;
        final List tree = populationCriteriaBuilder.getTree(artefactType);

        // check leaves on top branch are set
        final AnalysisAttributeBranch branch = (AnalysisAttributeBranch) tree.get(0);
        populationCriteriaBuilder.updateBranch(branch, null);

        // will be 5 children - portfolioItems, current jobs, succession positions, appraisals and questionnaires
        final List children = branch.getChildren();
        assertEquals(5, children.size());

        final AnalysisAttributeBranch questionnaireBranch = (AnalysisAttributeBranch) children.get(3);
        checkQuestionnaireLeaves(questionnaireBranch);

        final AnalysisAttributeBranch appraisalBranch = (AnalysisAttributeBranch) children.get(2);
        checkAppraisalLeaves(appraisalBranch);
    }

    public void testUpdateBranch() throws Exception {

        final String artefactType = IPopulationEngine.P_SUB_TYPE_;
        final List tree = populationCriteriaBuilder.getTree(artefactType);

        // check leaves on top branch are set
        final AnalysisAttributeBranch branch = (AnalysisAttributeBranch) tree.get(0);
        populationCriteriaBuilder.updateBranch(branch, null);

        // check branch type, etc
        assertEquals(artefactType, branch.getType());
        assertTrue(branch.isRoot());

        checkLeaves(branch, null);

        // check leaf id matches prefixes
        final AnalysisAttributeBranch child = (AnalysisAttributeBranch) branch.getChildren().get(0);
        assertEquals(IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".position", child.getPrefix());
        assertFalse(child.isRoot());

        populationCriteriaBuilder.updateBranch(child, null);

        checkLeaves(child, child.getPrefix());
    }

    public void testUpdateBranchAssociations() throws Exception {
        final String artefactType = IPopulationEngine.P_SUB_TYPE_;
        final List tree = populationCriteriaBuilder.getTree(artefactType);
        final AnalysisAttributeBranch branch2 = populationCriteriaBuilder.findBranchByLeafId(tree, "subjectPrimaryAssociations.position.subjectSecondaryAssociations.comments", null);
        populationCriteriaBuilder.updateBranch(branch2, null);
        assertNotNull(branch2);
        assertEquals(2, branch2.getLeaves().size());
    }

    public void testUpdateBranchSecondaryAssociations() throws Exception {
        final String artefactType = IPopulationEngine.P_SUB_TYPE_;
        final List tree = populationCriteriaBuilder.getTree(artefactType);
        final AnalysisAttributeBranch branch2 = populationCriteriaBuilder.findBranchByLeafId(tree, "subjectSecondaryAssociations.comments", null);
        populationCriteriaBuilder.updateBranch(branch2, null);
        assertNotNull(branch2);
        assertEquals(2, branch2.getLeaves().size());

        final AnalysisAttributeBranch branch3 = populationCriteriaBuilder.findBranchByLeafId(tree, "subjectSecondaryAssociations.comments", null);
        populationCriteriaBuilder.updateBranch(branch3, null);
        assertNotNull(branch3);
        assertEquals(2, branch3.getLeaves().size());
    }

    public void testUpdatePositionBranchSecondaryAssociations() throws Exception {
        final String artefactType = IPopulationEngine.P_POS_TYPE_;
        final List tree = populationCriteriaBuilder.getTree(artefactType);
        final AnalysisAttributeBranch branch2 = populationCriteriaBuilder.findBranchByLeafId(tree, "subjectPrimaryAssociations.subject.subjectSecondaryAssociations.comments", null);
        populationCriteriaBuilder.updateBranch(branch2, null);
        assertNotNull(branch2);
        assertEquals(2, branch2.getLeaves().size());

        final AnalysisAttributeBranch branch3 = populationCriteriaBuilder.findBranchByLeafId(tree, "subjectSecondaryAssociations.comments", null);
        populationCriteriaBuilder.updateBranch(branch3, null);
        assertNotNull(branch3);
        assertEquals(2, branch3.getLeaves().size());
    }

    public void testFindBranchByLeafId() throws Exception {

        findBranchByLeafId(IPopulationEngine.P_POS_TYPE_, "sourceAssociations.target", "organisationUnit.id");
        findBranchByLeafId(IPopulationEngine.P_POS_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".subject", "coreDetail.firstName");
        findBranchByLeafId(IPopulationEngine.P_POS_TYPE_, "", "title");
        findBranchByLeafId(IPopulationEngine.P_POS_TYPE_, "subjectSecondaryAssociations.subject.subjectPrimaryAssociations.position", "title");
        findBranchByLeafId(IPopulationEngine.P_POS_TYPE_, "subjectSecondaryAssociations.subject", "1_2_409");

        findBranchByLeafId(IPopulationEngine.P_SUB_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".position.subjectSecondaryAssociations.subject", "user.loginInfo.username");
        findBranchByLeafId(IPopulationEngine.P_SUB_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".position", "organisationUnit.label");
        findBranchByLeafId(IPopulationEngine.P_SUB_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR, "qualifier.label");
        findBranchByLeafId(IPopulationEngine.P_SUB_TYPE_, "subjectSecondaryAssociations.position.subjectPrimaryAssociations", "subjectSecondaryAssociations.position.subjectPrimaryAssociations", "qualifier.label");
        findBranchByLeafId(IPopulationEngine.P_SUB_TYPE_, "", "1_2_409");
    }

     public void testFindOrgBranchByLeafId() throws Exception {
         findOrgBranchByLeafId(IPopulationEngine.P_SUB_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".position.organisationUnit", "label");
         findOrgBranchByLeafId(IPopulationEngine.P_POS_TYPE_, "sourceAssociations.target.organisationUnit", "id");
         findOrgBranchByLeafId(IPopulationEngine.P_POS_TYPE_, "sourceAssociations.target.organisationUnit", "1234");
    }

    public void testFindBranch() throws Exception {

        findBranch(IPopulationEngine.P_POS_TYPE_, "sourceAssociations.target");
        findBranch(IPopulationEngine.P_POS_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".subject");
        findBranch(IPopulationEngine.P_POS_TYPE_, IPopulationEngine.POSITION_ATTR);
        findBranch(IPopulationEngine.P_POS_TYPE_, "subjectSecondaryAssociations.subject.subjectPrimaryAssociations.position");
        // subject branches
        findBranch(IPopulationEngine.P_SUB_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".position.subjectSecondaryAssociations.subject");
        findBranch(IPopulationEngine.P_SUB_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".position");
        // portfolio branch
        findBranch(IPopulationEngine.P_SUB_TYPE_, "portfolioItems");
        findBranch(IPopulationEngine.P_POS_TYPE_, "portfolioItems");
        // organisation unit branch
        findBranch(IPopulationEngine.P_POS_TYPE_, "subjectPrimaryAssociations.subject.subjectSecondaryAssociations.position.organisationUnit");
        // questionnaire branch
        findBranch(IPopulationEngine.P_SUB_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".position.subjectSecondaryAssociations.subject");
    }

    public void testSetAttributeLabel_Organsiation() throws Exception {
        String expected = " / Current Holders / Succession Positions / Organisation";
        String attribute = "subjectPrimaryAssociations.subject.subjectSecondaryAssociations.position.organisationUnit";
        Column column = new Column();
        column.setAttributeName(attribute);
        ColumnWrapperBean wrapperBean = new ColumnWrapperBean(column);
        wrapperBean.setIsOrgunitBranch(true);
        wrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute("Organisation", DynamicAttribute.DA_TYPE_TEXTFIELD)));
        populationCriteriaBuilder.setAttributeLabel(null, IPopulationEngine.P_POS_TYPE_, wrapperBean);
        assertEquals(expected, wrapperBean.getAttributeLabel());

        attribute = "subjectSecondaryAssociations.position.OrganisationUnit";
        column = new Column();
        column.setAttributeName(attribute);
        wrapperBean = new ColumnWrapperBean(column);
        wrapperBean.setIsOrgunitBranch(true);
        wrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute("Organisation", DynamicAttribute.DA_TYPE_TEXTFIELD)));
        expected = " / Succession Positions / Organisation";

        populationCriteriaBuilder.setAttributeLabel(null, IPopulationEngine.P_SUB_TYPE_, wrapperBean);
        assertEquals(expected, wrapperBean.getAttributeLabel());
    }

    private void findBranch(String artefactType, String id) {

        final List tree = populationCriteriaBuilder.getTree(artefactType);
        final AnalysisAttributeBranch found = populationCriteriaBuilder.findBranch(tree, id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    private void findBranchByLeafId(String artefactType, String prefix, String attributeName) throws Exception {
        findBranchByLeafId(artefactType, prefix, prefix, attributeName);
    }

    private void findBranchByLeafId(String artefactType, String expectedPrefix, String prefix, String attributeName) throws Exception {
        final List tree = populationCriteriaBuilder.getTree(artefactType);

        final String leafId = StringUtils.hasText(prefix) ? prefix + AnalysisAttributeHelper.DELIMITER + attributeName : attributeName;
        final AnalysisAttributeBranch found = populationCriteriaBuilder.findBranchByLeafId(tree, leafId, null);
        assertNotNull(found);
        assertEquals(expectedPrefix, found.getPrefix());
    }

    private void findOrgBranchByLeafId(String artefactType, String prefix, String attributeName) throws Exception {
        final List tree = populationCriteriaBuilder.getTree(artefactType);

        final String leafId = StringUtils.hasText(prefix) ? prefix + AnalysisAttributeHelper.DELIMITER + attributeName : attributeName;
        final AnalysisAttributeBranch found = populationCriteriaBuilder.findOrgBranchByLeafId(tree, leafId);
        assertNotNull(found);
        assertEquals(prefix, found.getPrefix());
    }

    private void checkLeaves(final AnalysisAttributeBranch branch, String rootPrefix) {

        assertTrue(branch.hasLeaves());

        final String nodeType = branch.getType();
        final boolean includeQualifierAttributes = branch.includeQualifierAttributes();

        final List<FormAttribute> allAttributes = new ArrayList<FormAttribute>();

        // include qualifier attributes if necesary
        if (includeQualifierAttributes) allAttributes.addAll(populationCriteriaBuilder.getQualifierAttributes());

        AttributeSet found = populationCriteriaBuilder.getAttributeSet(nodeType, null);
        allAttributes.addAll(populationCriteriaBuilder.copyAttributeWrapperBeans(found));

        final List<FormAttribute> extendedAttributes = populationCriteriaBuilder.getExtendedAttributes(nodeType, found);
        allAttributes.addAll(extendedAttributes);

        allAttributes.addAll(populationCriteriaBuilder.getDerivedAttributes(nodeType));

        // compare leaves and attributes
        Leaf qualifierLeaf = null;

        // count of proper leaves - excludes separator leaves
        int numberOfLeaves = 0;
        final Iterator attributeIterator = allAttributes.iterator();
        final List leaves = branch.getLeaves();
        for (Iterator iterator = leaves.iterator(); iterator.hasNext();) {
            Leaf leaf = (Leaf) iterator.next();

            if (!leaf.isSeparator()) {
                final AttributeWrapperBean attribute = (AttributeWrapperBean) attributeIterator.next();

                // check label
                assertEquals(attribute.getLabel(), leaf.getLabel());

                // if leaf id is qualifier save for later
                if (AnalysisAttributeHelper.isQualifierAttribute(leaf.getId())) {
                    qualifierLeaf = leaf;
                } else {

                    // if not check prefix is correct
                    if (StringUtils.hasText(rootPrefix)) {
                        assertEquals(rootPrefix + AnalysisAttributeHelper.DELIMITER + attribute.getId(), leaf.getId());
                    } else {
                        assertEquals(attribute.getId(), leaf.getId());
                    }
                }

                numberOfLeaves++;
            }
        }

        // check number of attributes matches number of leaves excluding separators
        assertEquals(allAttributes.size(), numberOfLeaves);

        if (includeQualifierAttributes) {

            // check qualifier leaf was found and that label and id are correct
            assertNotNull(qualifierLeaf);
            assertEquals("Type", qualifierLeaf.getLabel());

        } else {

            // if branch does not specify to include qualifiers, must be null
            assertNull(qualifierLeaf);
        }
    }

    private void testLabel(final String artefactType, final String attribute, final String title, final String expected) throws TalentStudioException {

        final PopulationCriteria populationCriteria = new PopulationCriteria();
        populationCriteria.setAttributeName(attribute);
        final CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(populationCriteria);
        criteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute(title, DynamicAttribute.DA_TYPE_TEXTFIELD)));
        populationCriteriaBuilder.setAttributeLabel(artefactType, artefactType, criteriaWrapperBean);
        assertEquals(expected, criteriaWrapperBean.getAttributeLabel());
    }

    private void checkAppraisalLeaves(AnalysisAttributeBranch appraisalBranch) {

        final List children = appraisalBranch.getChildren();
        for (int i = 0; i < children.size(); i++) {
            AnalysisAttributeBranch childBranch = (AnalysisAttributeBranch) children.get(i);
            checkAppraisalLeaves(childBranch);
        }

        checkQuestionLeaves(appraisalBranch, true);
    }

    private void checkQuestionnaireLeaves(AnalysisAttributeBranch questionnaireBranch) {

        final List children = questionnaireBranch.getChildren();
        for (int i = 0; i < children.size(); i++) {
            AnalysisAttributeBranch childBranch = (AnalysisAttributeBranch) children.get(i);
            checkQuestionnaireLeaves(childBranch);
        }

        checkQuestionLeaves(questionnaireBranch, false);
    }

    private void checkQuestionLeaves(AnalysisAttributeBranch questionnaireBranch, boolean appraisal) {

        final List leaves = questionnaireBranch.getLeaves();
        for (int i = 0; i < leaves.size(); i++) {

            final Leaf leaf = (Leaf) leaves.get(i);
            final String id = leaf.getId();
            final String label = leaf.getLabel();
            assertNotNull(id);
            assertNotNull(label);
            final AnalysisParameter analysisParameter = AnalysisAttributeHelper.splitQuestionCriteriaId(id);
            assertNotNull(analysisParameter.getName());
            assertNotNull(analysisParameter.getQuestionnaireWorkflowId());

            if (appraisal && label.indexOf(PopulationCriteriaBuilder.ROLE_SEPARATOR) > 0) {
                final String role = analysisParameter.getRole();
                assertNotNull("Invalid appraisal question id: " + id, role);
            }
        }
    }

    private PopulationCriteriaBuilder populationCriteriaBuilder;
}