package com.zynap.talentstudio.web.analysis.picker;

/**
 * User: amark
 * Date: 01-Feb-2006
 * Time: 11:27:16
 */

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.web.analysis.populations.CriteriaWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireHelper;

import java.util.List;

public class TestPopulationCriteriaBuilder extends AbstractHibernateTestCase {

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
    }

    public void testSetAttributeLabelsWithDocuments() throws Exception {
        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/test.xml");
        populationCriteriaBuilder.afterPropertiesSet();
        populationCriteriaBuilder.buildCollection();

        testLabel(IPopulationEngine.P_POS_TYPE_, IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR + ".subject.coreDetail.firstName", "First Name", " / Current Holders / First Name");
        testLabel(IPopulationEngine.P_SUB_TYPE_, "portfolioItems.lastModifiedBy.label", "Last Modified By", " / Documents / Last Modified By");
        testLabel(IPopulationEngine.P_SUB_TYPE_, "coreDetail.firstName", "First Name", " / First Name");
    }

    public void testBuildCollectionIncludesDocuments() throws Exception {

        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/test.xml");
        populationCriteriaBuilder.afterPropertiesSet();

        final String artefactType = IPopulationEngine.P_SUB_TYPE_;
        final AnalysisAttributeCollection collection = populationCriteriaBuilder.buildCollection();
        assertNotNull(collection);
        final List<AnalysisAttributeBranch> branches = populationCriteriaBuilder.getTree(artefactType);
        final String documentBranchId = "portfolioItems";
        final AnalysisAttributeBranch branch = populationCriteriaBuilder.findBranch(branches, documentBranchId);
        assertNotNull(branch);
    }

    public void testBuildCollectionExcludesDocuments() throws Exception {

        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/test_tree.xml");
        populationCriteriaBuilder.afterPropertiesSet();

        final String artefactType = IPopulationEngine.P_SUB_TYPE_;
        final AnalysisAttributeCollection collection = populationCriteriaBuilder.buildCollection();
        assertNotNull(collection);
        final List<AnalysisAttributeBranch> branches = populationCriteriaBuilder.getTree(artefactType);
        final String documentBranchId = "portfolioItems";
        final AnalysisAttributeBranch branch = populationCriteriaBuilder.findBranch(branches, documentBranchId);
        assertNull(branch);

    }

    public void testAfterPropertiesSet() throws Exception {

        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/test.xml");
        populationCriteriaBuilder.afterPropertiesSet();
        // after properties set method creates the ability to build a collection
        final AnalysisAttributeCollection attributeCollection = populationCriteriaBuilder.buildCollection();
        assertNotNull(attributeCollection);
    }

    public void testUpdateBranch() throws Exception {
        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/test.xml");
        populationCriteriaBuilder.afterPropertiesSet();

        final String artefactType = IPopulationEngine.P_SUB_TYPE_;
        final List tree = populationCriteriaBuilder.getTree(artefactType);
        final AnalysisAttributeBranch branch = populationCriteriaBuilder.findBranchByLeafId(tree, "portfolioItems.lastModifiedBy.label", null);
        populationCriteriaBuilder.updateBranch(branch, null);
        assertNotNull(branch);
        assertEquals(4, branch.getLeaves().size());
    }

    public void testFindBranchByLeafIdDocumentUser() throws Exception {
        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/test.xml");
        populationCriteriaBuilder.afterPropertiesSet();
        final AnalysisAttributeBranch branch = populationCriteriaBuilder.findBranchByLeafId(IPopulationEngine.P_SUB_TYPE_, "portfolioItems.lastModifiedBy.label", null);
        assertNotNull(branch);
        assertEquals("portfolioItems", branch.getId());
    }

    public void testFindBranchByLeafId() throws Exception {
        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/testOU.xml");
        populationCriteriaBuilder.afterPropertiesSet();
        populationCriteriaBuilder.buildCollection();
        AnalysisAttributeBranch branch = populationCriteriaBuilder.findBranchByLeafId(IPopulationEngine.P_POS_TYPE_, "subjectSecondaryAssociations.subject.subjectPrimaryAssociations.position.title", null);
        assertNotNull(branch);        
    }

   
    public void testSetAttributeLabelsOrgUnitsPopulations() throws Exception {
        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/testPops.xml");
        populationCriteriaBuilder.afterPropertiesSet();
        populationCriteriaBuilder.buildCollection();

        final String expected = " / Current Jobs / Organisation Unit";
        final String attributeName = "subjectPrimaryAssociations.position.organisationUnit.id";

        testLabel(IPopulationEngine.P_SUB_TYPE_, attributeName, "Organisation Unit", expected);
        testLabel(IPopulationEngine.P_POS_TYPE_, "organisationUnit.id", "Organisation Unit", " / Organisation Unit");
    }

    private void testLabel(final String artefactType, final String attribute, final String title, final String expected) throws TalentStudioException {

        final PopulationCriteria populationCriteria = new PopulationCriteria();
        populationCriteria.setAttributeName(attribute);
        final CriteriaWrapperBean criteriaWrapperBean = new CriteriaWrapperBean(populationCriteria);
        criteriaWrapperBean.setAttributeDefinition(new AttributeWrapperBean(new DynamicAttribute(title, DynamicAttribute.DA_TYPE_TEXTFIELD)));
        populationCriteriaBuilder.setAttributeLabel(artefactType, artefactType, criteriaWrapperBean);
        assertEquals(expected, criteriaWrapperBean.getAttributeLabel());
    }

    public void testSetAttributeLabelOrgUnit() throws Exception {
        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/testOU.xml");
        populationCriteriaBuilder.afterPropertiesSet();
        final AnalysisAttributeCollection attributeCollection = populationCriteriaBuilder.buildCollection();

        final String attributeName = "subjectPrimaryAssociations.position.organisationUnit.label";
        final String expected = " / Current Jobs / Organisation Unit / Label";

        Column column = new Column("test", attributeName, "TEXT");
        ColumnWrapperBean wrapperBean = new ColumnWrapperBean(column);
        wrapperBean.setIsOrgunitBranch(true);
        attributeCollection.setAttributeDefinition(wrapperBean);
        populationCriteriaBuilder.setAttributeLabel(null, IPopulationEngine.P_SUB_TYPE_, wrapperBean);
        assertEquals(expected, wrapperBean.getAttributeLabel());
    }

    private PopulationCriteriaBuilder populationCriteriaBuilder;
}
