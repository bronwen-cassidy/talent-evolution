/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeReference;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.questionnaires.support.QuestionnaireDefinitionFactory;
import com.zynap.talentstudio.security.users.IUserService;

import org.springframework.dao.DataAccessException;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 04-Jul-2007 11:24:47
 */
public class TestAbstractQuestionnaireService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();

        userService = (IUserService) getBean("userService");
        analysisService = (IAnalysisService) getBean("analysisService");
        questionnaireService = (IQuestionnaireService) getBean("questionnaireService");
        questionnaireDefinitionService = (IQueDefinitionService) getBean("questionnaireDefinitionService");
        questionnaireWorkflowService = (IQueWorkflowService) getBean("queWorkflowService");
        subjectService = (ISubjectService) getBean("subjectService");
    }

    protected QuestionnaireDefinition getDefinition() throws Exception {
        return questionnaireDefinitionService.findDefinition(QUE_DEFINITION_ID);
    }

    protected QuestionnaireWorkflow buildWorkflow(final User user) throws Exception {

        QuestionnaireDefinition definition = getDefinition();
        QuestionnaireWorkflow questionnaireWorkflow = new QuestionnaireWorkflow();
        questionnaireWorkflow.setLabel(definition.getLabel());
        questionnaireWorkflow.setUserId(user.getId());
        questionnaireWorkflow.setQuestionnaireDefinition(definition);

        final Population allSubjectsPopulation = (Population) analysisService.findById(IPopulationEngine.ALL_PEOPLE_POPULATION_ID);
        questionnaireWorkflow.setPopulation(allSubjectsPopulation);

        return questionnaireWorkflow;
    }

    protected QuestionnaireDefinition createDefinition(String testFile) throws Exception {

        final InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        final byte[] xmlBytes = new byte[25000];
        final int numBytes = testFileUrl.read(xmlBytes);
        final byte[] readBytes = new byte[numBytes];
        System.arraycopy(xmlBytes, 0, readBytes, 0, numBytes);
        final QuestionnaireDefinition questionnaireDefinition = QuestionnaireDefinitionFactory.parse(readBytes);
        questionnaireDefinitionService.create(questionnaireDefinition);
        assertNotNull(questionnaireDefinition.getId());

        assertDynamicAttributes(questionnaireDefinition);

        return questionnaireDefinition;
    }

    /**
     * Check that dynamic attributes are unique and that lookup types and lookup values are initialised correctly.
     *
     * @param definition the definition for which you would like to assert the dynamic attributes of
     */
    protected void assertDynamicAttributes(final QuestionnaireDefinition definition) {

        final Set<DynamicAttribute> attributes = new HashSet<DynamicAttribute>();
        final List<DynamicAttribute> dynamicAttributes = definition.getDynamicAttributes();
        for (DynamicAttribute dynamicAttribute : dynamicAttributes) {
            assertDynamicAttribute(dynamicAttribute, definition);

            final String str = dynamicAttribute.toString();
            assertFalse("Duplicate dynamic attribute: " + str, attributes.contains(dynamicAttribute));
            attributes.add(dynamicAttribute);

            if (dynamicAttribute.isSelectionType()) {

                final LookupType lookupType = dynamicAttribute.getRefersToType();
                assertLookupType(lookupType);

            } else if (dynamicAttribute.isMappingType()) {

                final LookupType lookupType = dynamicAttribute.getRefersToType();
                assertLookupType(lookupType);

                assertChildren(dynamicAttribute, dynamicAttribute.getChildren(), str, definition, attributes, lookupType);
                assertTrue(dynamicAttribute.getReferences().isEmpty());

            } else if (dynamicAttribute.isSumType()) {

                assertChildren(dynamicAttribute, dynamicAttribute.getChildren(), str, definition, attributes);
                assertReferences(dynamicAttribute, dynamicAttribute.getReferences(), definition, attributes);
            }
        }
    }

    protected void assertChildren(DynamicAttribute dynamicAttribute, List children, String str, QuestionnaireDefinition definition, Set attributes) {

        assertFalse("Dynamic attribute has no children: " + str, children.isEmpty());
        for (Object aChildren : children) {
            final DynamicAttributeReference attributeReference = (DynamicAttributeReference) aChildren;

            assertNotNull(attributeReference.getId());
            assertNull(attributeReference.getLabel());
            assertNull(attributeReference.getLowerBound());
            assertNull(attributeReference.getUpperBound());

            final DynamicAttribute parentDa = assertParentDA(attributeReference, definition);
            assertEquals(dynamicAttribute, parentDa);

            assertReferenceDA(attributeReference, definition, attributes);
            assertTrue(attributeReference.getChildren().isEmpty());
        }
    }

    protected void assertReferences(DynamicAttribute dynamicAttribute, List references, QuestionnaireDefinition definition, Set attributes) {

        for (Object reference : references) {

            final DynamicAttributeReference attributeReference = (DynamicAttributeReference) reference;
            assertNotNull(attributeReference.getId());
            final List children = attributeReference.getChildren();
            for (Object aChildren : children) {
                final DynamicAttributeReference child = (DynamicAttributeReference) aChildren;
                assertDynamicAttribute(child.getParentDa(), definition);
                assertDynamicAttribute(child.getReferenceDa(), definition);
                assertNotNull(child.getParent());
                assertTrue(child.getChildren().isEmpty());
            }

            assertNull(attributeReference.getParent());

            final DynamicAttribute parentDa = assertParentDA(attributeReference, definition);
            assertNotNull(parentDa);

            final DynamicAttribute referenceDa = assertReferenceDA(attributeReference, definition, attributes);
            assertEquals(dynamicAttribute, referenceDa);
        }
    }

    protected void assertChildren(DynamicAttribute dynamicAttribute, final List children, final String str, final QuestionnaireDefinition definition, final Set attributes, final LookupType lookupType) {

        assertFalse("Dynamic attribute has no children: " + str, children.isEmpty());
        for (Object aChildren : children) {
            final DynamicAttributeReference attributeReference = (DynamicAttributeReference) aChildren;

            assertNotNull(attributeReference.getId());
            assertNotNull(attributeReference.getLabel());

            assertTrue(attributeReference.getLowerBound() != null || attributeReference.getUpperBound() != null);

            final DynamicAttribute parentDa = assertParentDA(attributeReference, definition);
            assertEquals(dynamicAttribute, parentDa);

            assertReferenceDA(attributeReference, definition, attributes);

            final List childrenReferences = attributeReference.getChildren();
            final LookupValue lookupValue = attributeReference.getLookupValue();
            if (childrenReferences.isEmpty()) {
                assertNotNull(lookupValue);
                assertEquals(lookupType, lookupValue.getLookupType());
            } else {
                assertNull(lookupValue);
                assertChildren(dynamicAttribute, childrenReferences, str, definition, attributes, lookupType);
            }
        }
    }

    protected void assertDynamicAttribute(final DynamicAttribute dynamicAttribute, final QuestionnaireDefinition definition) {

        assertNotNull(dynamicAttribute.getId());
        assertNotNull(dynamicAttribute.getType());
        assertNotNull(dynamicAttribute.getUniqueNumber());
        assertNotNull(dynamicAttribute.getExternalRefLabel());
        assertEquals(definition.getId(), dynamicAttribute.getQuestionnaireDefinitionId());
        assertEquals(Node.QUESTIONNAIRE_TYPE, dynamicAttribute.getArtefactType());
    }

    protected void assertLookupType(final LookupType lookupType) {

        assertNotNull(lookupType.getId());
        assertNotNull(lookupType.getLabel());
        final Collection<LookupValue> lookupValues = lookupType.getLookupValues();
        for (LookupValue lookupValue : lookupValues) {
                    assertNotNull("Lookup value has no id: " + lookupValue.toString(), lookupValue.getId());
            assertNotNull("Lookup value has no label: " + lookupValue.toString(), lookupValue.getLabel());
        }
    }

    protected DynamicAttribute assertReferenceDA(final DynamicAttributeReference attributeReference, QuestionnaireDefinition definition, Set attributes) {

        final DynamicAttribute referenceDa = attributeReference.getReferenceDa();
        assertDynamicAttribute(referenceDa, definition);

        assertTrue("Invalid type: " + referenceDa.getType(), referenceDa.isSumType() || referenceDa.isNumericType() || referenceDa.isSelectionType());
        assertTrue("Dynamic attribute not found: " + referenceDa.toString(), attributes.contains(referenceDa));

        return referenceDa;
    }

    protected DynamicAttribute assertParentDA(final DynamicAttributeReference attributeReference, QuestionnaireDefinition definition) {

        final DynamicAttribute parentDa = attributeReference.getParentDa();
        assertDynamicAttribute(parentDa, definition);

        return parentDa;
    }

    protected boolean subjectHasInfoForm(Long subjectId, Long queId) throws Exception {
        Collection<QuestionnaireDTO> questionnaires = questionnaireService.getPortfolioQuestionnaires(subjectId);
        for (QuestionnaireDTO questionnaireDTO : questionnaires) {
                       Long questionnaireId = questionnaireDTO.getWorkflowId();
            if (questionnaireId.equals(queId)) return true;
        }
        return false;
    }

    public void testLineItems() throws Exception {

        final String testFile = "com/zynap/talentstudio/questionnaires/xml/AssesmentOfPotential.xml";
        final QuestionnaireDefinition questionnaireDefinition = createDefinition(testFile);

        final QuestionnaireDefinition actual = questionnaireDefinitionService.findDefinition(questionnaireDefinition.getId());
        assertNotNull(actual);
    }

    public void testFindDefinitionInvalidId() throws Exception {
        try {
            questionnaireDefinitionService.findDefinition((long) -1);
            fail("Exception not thrown looking for non-existent questionnaire definition");
        } catch (DataAccessException expected) {            
        }
    }

    public void testFindReportableDefinitions() throws Exception {
        final Collection questionnaireDefinitions = questionnaireDefinitionService.findReportableDefinitions(new String[]{DynamicAttribute.DA_TYPE_STRUCT});
        assertNotNull(questionnaireDefinitions);
    }

    public void testFindQuestionnaireWorkflowInvalidId() throws Exception {
        try {
            questionnaireWorkflowService.findById((long) -10);
            fail("Exception not thrown looking for non-existent questionnaire definition");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testPublishWorkflow() throws Exception {
        final User user = getAdminUser(userService);

        QuestionnaireWorkflow questionnaireWorkflow = buildWorkflow(user);
        questionnaireWorkflow.setWorkflowType(QuestionnaireWorkflow.TYPE_QUESTIONNAIRE);
        questionnaireWorkflowService.create(questionnaireWorkflow);

        questionnaireWorkflowService.startWorkflow(questionnaireWorkflow);
        assertEquals(QuestionnaireWorkflow.STATUS_PUBLISHED, questionnaireWorkflow.getStatus());
        assertTrue(questionnaireWorkflow.hasProcess());

        questionnaireWorkflowService.delete(questionnaireWorkflow);
    }

    public void testCreateInfoForm() throws Exception {
        final User user = getAdminUser(userService);
        QuestionnaireWorkflow questionnaireWorkflow = buildWorkflow(user);

        questionnaireWorkflow.setWorkflowType(QuestionnaireWorkflow.TYPE_INFO_FORM);
        questionnaireWorkflowService.create(questionnaireWorkflow);
        assertEquals(QuestionnaireWorkflow.STATUS_NEW, questionnaireWorkflow.getStatus());
        assertEquals(QuestionnaireWorkflow.TYPE_INFO_FORM, questionnaireWorkflow.getWorkflowType());
    }

    public void testPublishInfoForm() throws Exception {
        final User user = getAdminUser(userService);
        QuestionnaireWorkflow questionnaireWorkflow = buildWorkflow(user);
        questionnaireWorkflow.setWorkflowType(QuestionnaireWorkflow.TYPE_INFO_FORM);
        questionnaireWorkflowService.create(questionnaireWorkflow);

        questionnaireWorkflowService.startWorkflow(questionnaireWorkflow);
        assertEquals(QuestionnaireWorkflow.STATUS_PUBLISHED, questionnaireWorkflow.getStatus());
        assertFalse(questionnaireWorkflow.hasProcess());

        questionnaireService.delete(questionnaireWorkflow);
    }

    protected IUserService userService;
    protected IAnalysisService analysisService;
    protected IQuestionnaireService questionnaireService;
    protected IQueDefinitionService questionnaireDefinitionService;
    protected IQueWorkflowService questionnaireWorkflowService;
    protected ISubjectService subjectService;

    protected static final Long QUE_DEFINITION_ID = (long) -21;
    protected static final Long QUESTIONNAIRE_WORKFLOW_ID = (long) -1;
}
