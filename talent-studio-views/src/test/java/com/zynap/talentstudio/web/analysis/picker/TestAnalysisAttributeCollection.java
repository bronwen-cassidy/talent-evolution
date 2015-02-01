package com.zynap.talentstudio.web.analysis.picker;

/**
 * User: amark
 * Date: 08-Feb-2006
 * Time: 17:55:19
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;

import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireHelper;

public class TestAnalysisAttributeCollection extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        IDynamicAttributeService dynamicAttributeService = (IDynamicAttributeService) getBean("dynamicAttrService");
        IQuestionnaireService questionnaireService = (IQuestionnaireService) getBean("questionnaireService");
        final IQueWorkflowService questionnaireWorkflowService = (IQueWorkflowService) getBean("queWorkflowService");
        final IQueDefinitionService questionnaireDefinitionService = (IQueDefinitionService) getBean("questionnaireDefinitionService");
        QuestionnaireHelper questionnaireHelper = new QuestionnaireHelper();
        questionnaireHelper.setDynamicAttributeService(dynamicAttributeService);
        questionnaireHelper.setQuestionnaireService(questionnaireService);
        questionnaireHelper.setQuestionnaireWorkflowService(questionnaireWorkflowService);
        questionnaireHelper.setQuestionnaireDefinitionService(questionnaireDefinitionService);
        
        PopulationCriteriaBuilder populationCriteriaBuilder = new PopulationCriteriaBuilder();
        populationCriteriaBuilder.setDynamicAttributeService(dynamicAttributeService);
        populationCriteriaBuilder.setQuestionnaireWorkflowService(questionnaireWorkflowService);
        populationCriteriaBuilder.setQuestionnaireDefinitionService(questionnaireDefinitionService);
        populationCriteriaBuilder.setQuestionnaireHelper(questionnaireHelper);
        populationCriteriaBuilder.setSourceFile("com/zynap/talentstudio/web/analysis/picker/test.xml");
        populationCriteriaBuilder.afterPropertiesSet();

        analysisAttributeCollection = populationCriteriaBuilder.buildCollection();
    }

    public void testFindAttributeDefinition() throws Exception {

        findAttributeDefinition("title", "Title");
        findAttributeDefinition("coreDetail.contactEmail", "Email");
        findAttributeDefinition("organisationUnit.label", "Organisation Unit");
        findAttributeDefinition("user.loginInfo.username", "Username");

        findAttributeDefinition("subjectPrimaryAssociations.position.title", "Title");
        findAttributeDefinition("subjectPrimaryAssociations.subject.coreDetail.name", "Full Name");

        findAttributeDefinition("subjectPrimaryAssociations.position.subjectPrimaryAssociations.subject.user.loginInfo.username", "Username");
        findAttributeDefinition("subjectPrimaryAssociations.position.parent.organisationUnit.label", "Organisation Unit");

        findAttributeDefinition("subjectPrimaryAssociations.qualifier.label", "Type");
        findAttributeDefinition("subjectSecondaryAssociations.qualifier.label", "Type");
        findAttributeDefinition("targetAssociations.qualifier.label", "Type");
        findAttributeDefinition("sourceAssociations.qualifier.label", "Type");

        findAttributeDefinition("subjectPrimaryAssociations.position.subjectPrimaryAssociations.qualifier.label", "Type");
        findAttributeDefinition("subjectSecondaryAssociations.subject.subjectPrimaryAssociations.qualifier.label", "Type");

        findAttributeDefinition("portfolioItems.lastModifiedBy.label", "Last Modified By");
        findAttributeDefinition("portfolioItems.label", "Title");
    }

    private void findAttributeDefinition(final String attributeName, final String expectedLabel) {
        Column column = new Column();

        column.setAttributeName(attributeName);
        AnalysisAttributeWrapperBean analysisAttributeWrapperBean = new ColumnWrapperBean(column);

        analysisAttributeCollection.setAttributeDefinition(analysisAttributeWrapperBean);
        final AttributeWrapperBean attributeDefinition = analysisAttributeWrapperBean.getAttributeDefinition();
        assertNotNull(attributeDefinition);
        assertFalse(analysisAttributeWrapperBean.isInvalid());
        assertEquals(expectedLabel, attributeDefinition.getLabel());
    }

    AnalysisAttributeCollection analysisAttributeCollection;
}