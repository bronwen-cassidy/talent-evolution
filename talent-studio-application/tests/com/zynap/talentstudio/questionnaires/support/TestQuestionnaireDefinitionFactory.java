/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

package com.zynap.talentstudio.questionnaires.support;

import junit.framework.TestCase;

import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeReference;
import com.zynap.talentstudio.questionnaires.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestQuestionnaireDefinitionFactory extends TestCase {

    public void testParse() throws Exception {
        QuestionnaireDefinition questionnaireDefinition = QuestionnaireDefinitionFactory.parse(getBytes());
        checkQuestions(questionnaireDefinition);
        checkModelQuestions(questionnaireDefinition);
    }

    public void testParseValidation() throws Exception {
        try {
            QuestionnaireDefinitionFactory.parse(getBytes());
        } catch (InvalidXmlDefinitionException e) {
            fail("No exception expected but got " + e.getMessage());
        }
    }

    public void testParseQuestionNoName() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/EmptyQuestionName.xml";
        byte[] readBytes = parseQueDefinition(testFile);
        try {
            QuestionnaireDefinitionFactory.parse(readBytes);
            fail("should have thrown an IllegalEmptyQuestionLabelException");
        } catch (IllegalEmptyQuestionLabelException e) {
            // ok expected
        }
    }

    public void testParseQuestionsOrderedCorrectly() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/xml/GradPerfReview.xml";
        byte[] readBytes = parseQueDefinition(testFile);
        try {
            QuestionnaireDefinition x = QuestionnaireDefinitionFactory.parse(readBytes);
            final QuestionnaireDefinitionModel definitionModel = x.getQuestionnaireDefinitionModel();
            final List<QuestionGroup> questionGroups = definitionModel.getQuestionGroups();
            assertEquals("Status", questionGroups.get(0).getLabel());
            assertEquals("Meeting Details", questionGroups.get(1).getLabel());
            assertEquals("Specialism Competencies", questionGroups.get(2).getLabel());
            assertEquals("Work Performance Objectives", questionGroups.get(3).getLabel());
            assertEquals("Conduct", questionGroups.get(4).getLabel());
            assertEquals("Academic Attainment", questionGroups.get(5).getLabel());
            assertEquals("Personal Development Plan and Continuing Professional Development", questionGroups.get(6).getLabel());
            assertEquals("Additional Comments", questionGroups.get(7).getLabel());
            assertEquals("Outcomes", questionGroups.get(8).getLabel());
        } catch (Exception e) {
            fail("No Exception expected but got " + e.getMessage());
        }
    }
    
    public void testParseQuestionAnswerParentIds() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/xml/LinkedAnswers.xml";
        byte[] readBytes = parseQueDefinition(testFile);
        try {
            QuestionnaireDefinition x = QuestionnaireDefinitionFactory.parse(readBytes);
            final QuestionnaireDefinitionModel definitionModel = x.getQuestionnaireDefinitionModel();
            final List<QuestionGroup> questionGroups = definitionModel.getQuestionGroups();
            // 2 groups
            assertEquals(2, questionGroups.size());
            
            // get the questions
            for (QuestionGroup questionGroup : questionGroups) {
                List<QuestionAttribute> questions = questionGroup.getQuestions();
                for (QuestionAttribute question : questions) {
                    if ("First Requires".equals(question.getLabel())) {
                        DynamicAttribute dynamicAttribute = question.getDynamicAttribute();
                        if(dynamicAttribute.getRefersToType() != null) {
                            List<LookupValue> lookupValues = dynamicAttribute.getRefersToType().getLookupValues();
                            for (LookupValue lookupValue : lookupValues) {
                                assertNotNull(lookupValue.getRequires());
                            }
                        }
                    }
                }
            }
            
            List<DynamicAttribute> dynamicAttributes = x.getDynamicAttributes();
            for (DynamicAttribute dynamicAttribute : dynamicAttributes) {
                if(dynamicAttribute.getRefersToType() != null) {
                    List<LookupValue> lookupValues = dynamicAttribute.getRefersToType().getLookupValues();
                    for (LookupValue lookupValue : lookupValues) {
                        assertNotNull(lookupValue.getLinkId());
                    }
                }
            }

        } catch (Exception e) {
            fail("no exception expected but got " + e.getMessage());
        }
    }

    public void testParseNoGroup() throws Exception {
        XmlBuilder builder = new XmlBuilder();
        builder.addElement("questionnaire");
        builder.addAttributes(new String[]{"name", "title"}, new String[]{"test", "testing"});
        builder.closeElement("questionnaire");
        String xml = builder.getXml();
        try {
            QuestionnaireDefinitionFactory.parse(xml.getBytes());
            fail("Should have thrown an exception, No Group!! ");
        } catch (InvalidXmlDefinitionException e) {
            // ok expected
        }
    }

    public void testParseAccessNoGroup() throws Exception {
        XmlBuilder builder = new XmlBuilder();
        builder.addElement("questionnaire");
        builder.addAttributes(new String[]{"name", "title"}, new String[]{"test", "testing"});
        builder.addElement("access");
        builder.addAttributesAndClose(new String[]{"name"}, new String[]{"admin"});
        builder.closeElement("questionnaire");
        String xml = builder.getXml();
        try {
            QuestionnaireDefinitionFactory.parse(xml.getBytes());
            fail("Should have thrown an exception, No Group!! ");
        } catch (InvalidXmlDefinitionException e) {
            // ok expected
        }
    }

    public void testParseNoAccessHasGroup() throws Exception {
        XmlBuilder builder = new XmlBuilder();
        builder.addElement("questionnaire");
        builder.addAttributes(new String[]{"name", "title"}, new String[]{"test", "testing"});
        builder.addElement("group");
        builder.addAttributes(new String[]{"name"}, new String[]{"this is my group name"});
        builder.addElement("question");
        builder.addAttributes(new String[]{"type", "name", "title", "id"}, new String[]{"Select", "select question", "this is my group name", "bennyId"});
        builder.closeElement("question");
        builder.closeElement("group");
        builder.closeElement("questionnaire");
        String xml = builder.getXml();
        try {
            QuestionnaireDefinitionFactory.parse(xml.getBytes());
        } catch (InvalidXmlDefinitionException e) {
            fail("Should not have thrown an exception ");
        }
    }

    public void testParseGroupNoName() throws Exception {
        XmlBuilder builder = new XmlBuilder();
        builder.addElement("questionnaire");
        builder.addAttributes(new String[]{"name", "title"}, new String[]{"test", "testing"});
        builder.addElement("group>");
        builder.addElement("question");
        builder.addAttributes(new String[]{"type", "name", "title"}, new String[]{"Select", "select question", "this is my group name"});
        builder.closeElement("question");
        builder.closeElement("group");
        builder.closeElement("questionnaire");
        String xml = builder.getXml();
        try {
            QuestionnaireDefinitionFactory.parse(xml.getBytes());
            fail("Should have thrown an exception a group without a name");
        } catch (InvalidXmlDefinitionException e) {
        }
    }

    public void testParseQuestionUnknownType() throws Exception {
        XmlBuilder builder = new XmlBuilder();
        builder.addElement("questionnaire");
        builder.addAttributes(new String[]{"name", "title"}, new String[]{"test", "testing"});
        builder.addElement("group");
        builder.addAttributes(new String[]{"name"}, new String[]{"this is my group name"});
        builder.addElement("question");
        builder.addAttributes(new String[]{"type", "name", "title"}, new String[]{"Uploads", "select question", "this is my group name"});
        builder.closeElement("question");
        builder.closeElement("group");
        builder.closeElement("questionnaire");
        String xml = builder.getXml();
        try {
            QuestionnaireDefinitionFactory.parse(xml.getBytes());
            fail("Should have thrown an exception a group without a name");
        } catch (InvalidXmlDefinitionException e) {
        }
    }

    public void testQuestionnaireDefinitionSelfEvaluation() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/EmptyGroupName.xml";
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        byte[] allbytes = new byte[20000];
        int numread = testFileUrl.read(allbytes);
        byte[] readBytes = new byte[numread];
        System.arraycopy(allbytes, 0, readBytes, 0, numread);
        testFileUrl.close();
        try {
            QuestionnaireDefinitionFactory.parse(readBytes);
            fail("IllegalEmptyGroupLabelException expected");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalEmptyGroupLabelException);
        }
    }

    public void testQuestionnaireDefinitionNarrativies() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/xml/GradGeneralCompetenciesv2.xml";
        byte[] readBytes = parseQueDefinition(testFile);
        try {
            final QuestionnaireDefinition definition = QuestionnaireDefinitionFactory.parse(readBytes);
            final QuestionnaireDefinitionModel definitionModel = definition.getQuestionnaireDefinitionModel();
            final List<AbstractQuestion> questions = definitionModel.getQuestions();

            AbstractQuestion abstractQuestion = questions.get(1);
            assertTrue(abstractQuestion.isNarrativeType());
            assertEquals("Lead a small project team including selection of team members, project initiation and planning, through to delivery and post evaluation, with supportive feedback to individuals and the team.", ((QuestionAttribute) abstractQuestion).getNarrative());
            // questions and multiQuestions in the correct order. Question index 3 is a multi question
            abstractQuestion = questions.get(3);
            assertTrue(abstractQuestion.isNarrativeType());
            assertEquals("Lead a small project team including selection of team members, project initiation and planning, through to delivery and post evaluation, with supportive feedback to individuals and the team.", ((QuestionAttribute) abstractQuestion).getNarrative());

        } catch (Exception e) {
            fail("No Exception expected but got " + e.getMessage());
        }
    }

    private byte[] parseQueDefinition(String testFile) throws IOException {
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        byte[] allbytes = new byte[30000];
        int numread = testFileUrl.read(allbytes);
        byte[] readBytes = new byte[numread];
        System.arraycopy(allbytes, 0, readBytes, 0, numread);
        testFileUrl.close();
        return readBytes;
    }

    public void testParseAllQuestionsSumsAndCustomQuestions() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/xml/AllQuestions.xml";
        byte[] readBytes =parseQueDefinition(testFile);
        QuestionnaireDefinition questionnaireDefinition = QuestionnaireDefinitionFactory.parse(readBytes);
        checkQuestions(questionnaireDefinition);
        checkModelQuestions(questionnaireDefinition);
    }

    public void testParse_Sums_Enums() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/xml/AssessmentofPotential_48.xml";
        byte[] readBytes =parseQueDefinition(testFile);
        QuestionnaireDefinition questionnaireDefinition = QuestionnaireDefinitionFactory.parse(readBytes);
        //todo assert sums and enums
        assertNotNull(questionnaireDefinition);
    }

    public void testQuetionnaireDefinitionUniqueGroupNames() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/DuplicateGroupNames.xml";
        byte[] readBytes =parseQueDefinition(testFile);
        try {
            QuestionnaireDefinitionFactory.parse(readBytes);
            fail("should have thrown an xsd validation exception");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SchemaValidationFailedException expected) {
            //expected
        } catch (Exception any) {
            any.printStackTrace();
        }
    }

    public void testQuetionnaireDefinitionEmptyNarrative() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/EmptyNarrative.xml";
        byte[] readBytes =parseQueDefinition(testFile);
        try {
            QuestionnaireDefinitionFactory.parse(readBytes);
            fail("should have thrown an xsd validation exception");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SchemaValidationFailedException expected) {
            //expected
        } catch (Exception any) {
            any.printStackTrace();
        }
    }

    public void testQuetionnaireDefinitionInvalidMappingSource() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/InvalidMappingSource.xml";
        byte[] readBytes = parseQueDefinition(testFile);
        try {
            QuestionnaireDefinitionFactory.parse(readBytes);
            fail("should have thrown an exception");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SchemaValidationFailedException expected) {
            //expected
        } catch (Exception any) {
            any.printStackTrace();
        }
    }

    public void testQuestionnaireBlankAnswer() throws Exception {
        byte[] readBytes =parseQueDefinition("com/zynap/talentstudio/questionnaires/xml/2011Competencies.xml");
        try {
            QuestionnaireDefinitionFactory.parse(readBytes);
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (SchemaValidationFailedException expected) {
            fail(expected.getMessage());
        } catch (Exception any) {
            fail(any.getMessage());
        }
    }

    private List checkQuestions(QuestionnaireDefinition questionnaireDefinition) {

        QuestionnaireDefinitionModel questionnaireDefinitionModel = questionnaireDefinition.getQuestionnaireDefinitionModel();

        List questions = questionnaireDefinitionModel.getQuestions();
        assertNotNull(questions);

        int expectedSortOrder = 0;
        for (int j = 0; j < questions.size(); j++) {
            AbstractQuestion baseQuestion = (AbstractQuestion) questions.get(j);
            if (baseQuestion instanceof QuestionAttribute) {
                QuestionAttribute question = (QuestionAttribute) baseQuestion;

                final DynamicAttribute dynamicAttribute = question.getDynamicAttribute();

                if (question.isSum() || question.isEnumMapping()) {
                    checkQuestionMappings(dynamicAttribute, question);
                }

                if (!question.isNarrativeType()) {
                    assertNotNull(dynamicAttribute.getLabel());
                    assertNotNull(dynamicAttribute.getExternalRefLabel());
                    assertFalse(dynamicAttribute.getUniqueNumber().doubleValue() == 0);

                    if (dynamicAttribute.isSelectionType()) {
                        LookupType refersToType = dynamicAttribute.getRefersToType();
                        List lookupValues = refersToType.getLookupValues();
                        for (int k = 0; k < lookupValues.size(); k++) {
                            LookupValue lookupValue = (LookupValue) lookupValues.get(k);
                            assertEquals(expectedSortOrder, lookupValue.getSortOrder());
                            expectedSortOrder++;
                        }
                    }
                }

            }
            // reset the sort order in time for the next question
            expectedSortOrder = 0;
        }
        return questions;
    }

    private void checkQuestionMappings(DynamicAttribute dynamicAttribute, QuestionAttribute question) {
        /* a dynamic attribute that is a sum or an enum mapping always has children, but may or may not have references */
        assertFalse(dynamicAttribute.getChildren().isEmpty());

        List references = dynamicAttribute.getReferences();

        for (int i = 0; i < references.size(); i++) {

            DynamicAttributeReference questionReference = (DynamicAttributeReference) references.get(i);
            /* assert only those relevant to enum mappings*/
            if (question.isEnumMapping()) {

                assertNotNull(questionReference.getLookupValue().getValueId());
                // returns all children recursively
                List children = questionReference.getChildren();
                for (int k = 0; k < children.size(); k++) {
                    QuestionReference child = (QuestionReference) children.get(k);
                    assertNotNull(child.getParentQuestion());
                    // each child should have a reference
                    assertNotNull("children mappings should all have a reference", child.getReferenceQuestion());
                }
            }

            DynamicAttribute reference = questionReference.getReferenceDa();

            /* assert the references have been tied up */
            assertNotNull(questionReference);
            assertNotNull(reference);
            assertFalse(reference.getReferences().isEmpty());

            /* test the parent information for a reference has been tied up*/
            DynamicAttribute parent = questionReference.getParentDa();
            assertNotNull(parent);
            assertFalse(parent.getChildren().isEmpty());
        }

        List children = dynamicAttribute.getChildren();
        for (Iterator iterator = children.iterator(); iterator.hasNext();) {
            DynamicAttributeReference dynamicAttributeReference = (DynamicAttributeReference) iterator.next();
            assertNotNull(dynamicAttributeReference.getParentDa());
            assertNotNull(dynamicAttributeReference.getReferenceDa());
            if (question.isEnumMapping() && dynamicAttributeReference.getChildren().isEmpty()) {
                LookupValue lookupValue = dynamicAttributeReference.getLookupValue();
                assertNotNull(lookupValue);
                assertNotNull(lookupValue.getValueId());
            }
        }
    }

    private List checkModelQuestions(QuestionnaireDefinition questionnaireDefinition) {

        QuestionnaireDefinitionModel questionnaireDefinitionModel = questionnaireDefinition.getQuestionnaireDefinitionModel();

        List questions = questionnaireDefinitionModel.getQuestions();
        assertNotNull(questions);

        int expectedSortOrder = 0;
        for (int j = 0; j < questions.size(); j++) {
            AbstractQuestion baseQuestion = (AbstractQuestion) questions.get(j);
            if (baseQuestion instanceof QuestionAttribute) {
                QuestionAttribute question = (QuestionAttribute) baseQuestion;
                if (question.isNarrativeType()) continue;
                final DynamicAttribute dynamicAttribute = question.getDynamicAttribute();

                if (question.isSum() || question.isEnumMapping()) {
                    checkQuestionModelMappings(dynamicAttribute, question);
                }

                assertNotNull(dynamicAttribute.getLabel());
                assertNotNull(dynamicAttribute.getExternalRefLabel());
                assertFalse(dynamicAttribute.getUniqueNumber().doubleValue() == 0);


                if (dynamicAttribute.isSelectionType()) {
                    LookupType refersToType = dynamicAttribute.getRefersToType();
                    List lookupValues = refersToType.getLookupValues();
                    for (int k = 0; k < lookupValues.size(); k++) {
                        LookupValue lookupValue = (LookupValue) lookupValues.get(k);
                        assertEquals(expectedSortOrder, lookupValue.getSortOrder());
                        expectedSortOrder++;
                    }
                }
            }
            // reset the sort order in time for the next question
            expectedSortOrder = 0;
        }
        return questions;
    }

    private void checkQuestionModelMappings(DynamicAttribute dynamicAttribute, QuestionAttribute question) {

        assertFalse(dynamicAttribute.getChildren().isEmpty());

        List references = question.getDynamicAttribute().getChildren();
        assertFalse(references.isEmpty());

        for (int i = 0; i < references.size(); i++) {
            DynamicAttributeReference questionReference = (DynamicAttributeReference) references.get(i);
            /* assert only those relevant to enum mappings*/
            if (question.isEnumMapping()) {
                /**
                 * multi-level enum mappings do not need a lable on the lookupValue for the parent levels only the lowest levels
                 */
                if (questionReference.getChildren() == null || questionReference.getChildren().isEmpty()) {
                    assertNotNull(questionReference.getLookupValue().getLabel());
                }
                // returns all children recursively
                List<DynamicAttributeReference> children = questionReference.getChildren();
                if (children != null) {
                    for (DynamicAttributeReference child : children) {
                        assertNotNull(child.getParent());
                        // each child should have a reference
                        assertNotNull("children mappings should all have a reference", child.getReferenceDa());
                    }
                }
            }

            DynamicAttribute reference = questionReference.getReferenceDa();

            /* assert the references have been tied up */
            assertNotNull(questionReference);
            assertNotNull(reference);
            assertFalse(reference.getReferences().isEmpty());

            /* test the parent information for a reference has been tied up*/
            DynamicAttribute parent = questionReference.getParentDa();
            assertNotNull(parent);
            assertFalse(parent.getChildren().isEmpty());
        }
    }

    private byte[] getBytes() {
        return TestQuestionnaireDefinition.getMultiSelectXmlDef().trim().getBytes();
    }

    private class XmlBuilder {

        public XmlBuilder() {
            xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml>");
        }

        public void addElement(String element) {
            String xmlElem = "<" + element + " ";
            xml.append(xmlElem);
        }

        public void addAttributes(String[] names, String[] values) {
            appendAttrs(names, values);
            xml.append(">");
        }

        public String getXml() {
            xml.append("</xml>");
            return xml.toString();
        }

        public void closeElement(String elementName) {
            xml.append("</").append(elementName).append(">");
        }

        public void addAttributesAndClose(String[] names, String[] values) {
            appendAttrs(names, values);
            xml.append("/>");
        }

        private void appendAttrs(String[] names, String[] values) {
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                xml.append(name).append("=\"").append(values[i]).append("\" ");
            }
        }

        private StringBuffer xml;
    }
}