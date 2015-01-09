/**
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 16-Jul-2005 00:46:23
 * @version 0.1
 */

import junit.framework.TestCase;
import org.xml.sax.InputSource;

import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeReference;
import com.zynap.talentstudio.questionnaires.support.DigesterQuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.support.QuestionnaireDefinitionFactory;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class TestQuestionnaireDefinition extends TestCase {

    public void testQuestionnaireDefinition() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getXmlDefOne())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertEquals("Career Profile Talent Identifier", questionnaireDefinition.getLabel());
    }

    public void testQuestionnaireDefinitionNameTitle() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getXmlDefOne())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertEquals("Career Profile Talent Identifier", questionnaireDefinition.getLabel());
        assertEquals("PVC", questionnaireDefinition.getTitle());
    }

    public void testQuestionnaireDefinitionExtendedAttribute() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getXmlDefOne())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertNotNull(questionnaireDefinition.getDynamicAttributes());
    }

    public void testQuestionnaireDefinitionExtendedAttributeLabel() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getXmlDefOne())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertEquals(1, questionnaireDefinition.getDynamicAttributes().size());
        assertTrue(questionnaireDefinition.getDynamicAttributes().iterator().next().getLabel().indexOf("Track Record") != -1);
    }

    public void testQuestionnaireDefinitionExtendedAttributeDescription() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getXmlDefOne())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertEquals(1, questionnaireDefinition.getDynamicAttributes().size());
        assertEquals("Track Record", questionnaireDefinition.getDynamicAttributes().iterator().next().getDescription());
    }

    public void testQuestionnaireDefinitionExtendedAttributeType() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getXmlDefOne())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertEquals(1, questionnaireDefinition.getDynamicAttributes().size());
        assertTrue(questionnaireDefinition.getDynamicAttributes().iterator().next().isSelectionType());
    }

    public void testQuestionnaireDefinitionExtendedAttributeDescriptionRefersTo() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getXmlDefOne())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertEquals(1, questionnaireDefinition.getDynamicAttributes().size());
        DynamicAttribute dynamicAttribute = questionnaireDefinition.getDynamicAttributes().iterator().next();
        assertNotNull(dynamicAttribute.getRefersToType());
    }

    public void testQuestionnaireDefinitionExtendedAttributeTextType() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getTextAttrXmlDef())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        List dynamicAttributes = questionnaireDefinition.getDynamicAttributes();
        assertEquals(3, dynamicAttributes.size());
        for (Iterator iterator = dynamicAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            assertTrue(dynamicAttribute.isTextType());
        }
    }

    public void testQuestionnaireDefinitionExtendedAttributeDescriptionRefersToValues() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getXmlDefOne())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertEquals(1, questionnaireDefinition.getDynamicAttributes().size());
        DynamicAttribute dynamicAttribute = questionnaireDefinition.getDynamicAttributes().iterator().next();
        LookupType refersToType = dynamicAttribute.getRefersToType();
        assertNotNull(refersToType.getLookupValues());
        assertFalse(refersToType.getLookupValues().isEmpty());
    }

    public void testQuestionnaireDefinitionMultiQuestion() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getMultiSelectXmlDef())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        List dynamicAttributes = questionnaireDefinition.getDynamicAttributes();
        assertEquals(4, dynamicAttributes.size());
        for (Iterator iterator = dynamicAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            if ("Organisation".equals(dynamicAttribute.getLabel())) assertNull(dynamicAttribute.getRefersToType());
            if ("Location".equals(dynamicAttribute.getLabel())) assertNotNull(dynamicAttribute.getRefersToType());
        }
    }

    public void testQuestionnaireDefinitionMixedQuestion() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(getMixedQuestionXmlDef())));
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        List dynamicAttributes = questionnaireDefinition.getDynamicAttributes();
        assertEquals(4, dynamicAttributes.size());
    }

    public void testQuestionnaireDefinitionAssessmentofPotential() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        String testFile = "com/zynap/talentstudio/questionnaires/xml/AssesmentOfPotential.xml";
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(testFileUrl);
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertNotNull(questionnaireDefinition);
        assertEquals(28, questionnaireDefinition.getDynamicAttributes().size());
    }

    public void testQuestionnaireDefinitionSelfEvaluation() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        String testFile = "com/zynap/talentstudio/questionnaires/support/EmptyGroupName.xml";
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        try {
            digester.parse(testFileUrl);
            fail("IllegalEmptyGroupLabelException expected");
        } catch (Exception e) {
            // expected
        }
    }

    public void testQuestionnaireDefinitionLineItem() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        String testFile = "com/zynap/talentstudio/questionnaires/xml/Cascading Objectives.xml";
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(testFileUrl);
        QuestionnaireDefinition questionnaireDefinition = digesterDefinition.getQuestionnaireDefinition();
        assertNotNull(questionnaireDefinition);
    }

    public void testQuestionnaireDefinitionMultipleNarratives() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/xml/Mentor-Coach Evaluation.xml";
        final QuestionnaireDefinition definition = createDefinition(testFile);
        final QuestionnaireDefinitionModel questionnaireDefinitionModel = definition.getQuestionnaireDefinitionModel();
        List groups = questionnaireDefinitionModel.getQuestionGroups();
        for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
            QuestionGroup questionnaireGroup = (QuestionGroup) iterator.next();
            List questions = questionnaireGroup.getQuestions();
            for (Iterator iterator1 = questions.iterator(); iterator1.hasNext();) {
                QuestionAttribute question = (QuestionAttribute) iterator1.next();
                if(question.isNarrativeType()) {
                    assertNotNull(question.getNarrative());
                }
            }
        }
    }

    public void testQuestionnaireDefinitionMappings() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/xml/lastUpdatedQs.xml";
        final QuestionnaireDefinition definition = createDefinition(testFile);
        final QuestionnaireDefinitionModel questionnaireDefinitionModel = definition.getQuestionnaireDefinitionModel();

        List questions = questionnaireDefinitionModel.getQuestions();
        // get the 2 level mapping question
        QuestionAttribute question = (QuestionAttribute) questions.get(13);
        DynamicAttribute dynamicAttribute = question.getDynamicAttribute();
        final LookupType lookupType = dynamicAttribute.getRefersToType();
        assertNotNull(lookupType);
        assertEquals(6, lookupType.getLookupValues().size());
        // get the references
        DynamicAttributeReference dar = dynamicAttribute.getChildren().get(0);
        DynamicAttributeReference child = (DynamicAttributeReference) dar.getChildren().get(0);
        final LookupValue childLookupValue = child.getLookupValue();
        assertNotNull(childLookupValue);

        String expectedLabel = "Poor Performer";
        assertEquals(expectedLabel, childLookupValue.getLabel());
        assertNotNull(childLookupValue.getLookupType());
        assertEquals(expectedLabel, child.getLabel());
    }

    public void testQuestionnaireDefinitionMugShot() throws Exception {
        String rulesFile = "digester/rules.xml";
        URL rulesURL = ClassLoader.getSystemResource(rulesFile);
        String testFile = "com/zynap/talentstudio/questionnaires/xml/Photo.xml";
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        Digester digester = DigesterLoader.createDigester(rulesURL);
        DigesterQuestionnaireDefinition digesterDefinition = (DigesterQuestionnaireDefinition) digester.parse(testFileUrl);
        assertNotNull(digesterDefinition);
    }

    public void testGetDynamicAttributes() throws Exception {
        String testFile = "com/zynap/talentstudio/questionnaires/xml/AssesmentOfPotential.xml";
        
        QuestionnaireDefinition definition = createDefinition(testFile);
        List attributes = definition.getDynamicAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) attributes.get(i);
            assertEquals(definition.getId(), dynamicAttribute.getQuestionnaireDefinitionId());
        }
    }

    private QuestionnaireDefinition createDefinition(String testFile) throws Exception {
        InputStream testFileUrl = ClassLoader.getSystemResourceAsStream(testFile);
        byte[] bytes = new byte[10000];
        final int i = testFileUrl.read(bytes);
        byte[] result = new byte[i];
        System.arraycopy(bytes, 0, result, 0, i);
        return QuestionnaireDefinitionFactory.parse(result);
    }

    private String getXmlDefOne() {
        return new String("<?xml version='1.0'?>" +
                "<xml>" +
                "<questionnaire name=\"Career Profile Talent Identifier\" title=\"PVC\">" +
                "<group name=\"Track Record - performance in last three years\">" +

                "<question type=\"Select\" name=\"Track Record\" title=\"Track Record\">" +
                "<answer value=\"1\" text=\"Poor\"/>" +
                "<answer value=\"2\" text=\"Good\"/>" +
                "<answer value=\"3\" text=\"Exceptional\"/>" +
                "</question>" +

                "<narrative>An assessment of performance over the last three years.</narrative>" +
                "</group>" +
                "</questionnaire>" +
                "</xml>");
    }

    private String getTextAttrXmlDef() {
        return new String("<?xml version='1.0'?>" +
                "<xml>" +
                "<questionnaire name=\"Career Profile Talent Identifier\" title=\"PVC\">" +
                "<group name=\"Languages\">" +
                "<question type=\"Text\" name=\"First Language\" title=\" \">" +
                "</question>" +
                "<question type=\"Text\" name=\"Second Language\" title=\" \">" +
                "</question>" +
                "<question type=\"Text\" name=\"Other Language\" title=\" \">" +
                "</question>" +
                "</group>" +
                "</questionnaire>" +
                "</xml>");
    }

    public static String getMultiSelectXmlDef() {
        return new String("<?xml version='1.0'?>" +
                "<xml>" +
                "<questionnaire name=\"Career Profile Talent Identifier\" title=\"PVC\">" +
                "<group name=\"Languages\">" +
                "<multi-question name=\"\">" +
                "<line-item>This is a tests</line-item>" +
                "<question type=\"Organisation\" name=\"Organisation\" title=\"Organisation\" id=\"one\"/>" +
                "<question type=\"Text\" name=\"Position\" title=\"Position\" id=\"two\"/>" +
                "<question type=\"Select\" name=\"Function\" title=\"test\" id=\"three\">" +
                "<answer value=\"Sales\" text=\"Sales\"/>" +
                "<answer value=\"Marketing\" text=\"Marketing\"/>" +
                "<answer value=\"IS\" text=\"IS\"/>" +
                "<answer value=\"HR\" text=\"HR\"/>" +
                "<answer value=\"Finance\" text=\"Finance\"/>" +
                "<answer value=\"Operations\" text=\"Operations\"/>" +
                "<answer value=\"R and D\" text=\"R and D\"/>" +
                "<answer value=\"General Management\" text=\"General Management\"/>" +
                "<answer value=\"Other\" text=\"Other\"/>" +
                "</question>" +
                "<question type=\"Select\" name=\"Location\" title=\" \" id=\"four\">" +
                "<answer value=\"Any Region\" text=\"Any Region\"/>" +
                "<answer value=\"Europe Main\" text=\"Europe Main\"/>" +
                "<answer value=\"Europe Area I\" text=\"Europe Area I\"/>" +
                "<answer value=\"Europe Area II\" text=\"Europe Area II\"/>" +
                "<answer value=\"LAMEA\" text=\"LAMEA\"/>" +
                "<answer value=\"Asia Pacific\" text=\"Asia Pacific\"/>" +
                "<answer value=\"Japan\" text=\"Japan\"/>" +
                "<answer value=\"United States\" text=\"United States\"/>" +
                "<answer value=\"Central Functions\" text=\"Central Functions\"/>" +
                "</question>" +
                "</multi-question>" +
                "</group>" +
                "</questionnaire>" +
                "</xml>");
    }

    private String getMixedQuestionXmlDef() {
        return new String("<?xml version='1.0'?>" +
                "<xml>" +
                "<questionnaire name=\"Career Profile Talent Identifier\" title=\"PVC\">" +
                "<group name=\"Languages\">" +
                "<multi-question name=\"\">" +
                "<line-item></line-item>" +
                "<question type=\"Text\" name=\"Position\" title=\"Position\"/>" +
                "<question type=\"Select\" name=\"Function\" title=\" \">" +
                "<answer value=\"Sales\" text=\"Sales\"/>" +
                "<answer value=\"Marketing\" text=\"Marketing\"/>" +
                "<answer value=\"IS\" text=\"IS\"/>" +
                "<answer value=\"HR\" text=\"HR\"/>" +
                "<answer value=\"Finance\" text=\"Finance\"/>" +
                "<answer value=\"Operations\" text=\"Operations\"/>" +
                "<answer value=\"R and D\" text=\"R and D\"/>" +
                "<answer value=\"General Management\" text=\"General Management\"/>" +
                "<answer value=\"Other\" text=\"Other\"/>" +
                "</question>" +
                "</multi-question>" +
                "<question type=\"TextBox\" name=\"Key Development Areas\" title=\" \">" +
                "</question>" +
                "<question type=\"TextBox\" name=\"Comments\" title=\" \">" +
                "</question>" +
                "</group>" +
                "</questionnaire>" +
                "</xml>");
    }

}