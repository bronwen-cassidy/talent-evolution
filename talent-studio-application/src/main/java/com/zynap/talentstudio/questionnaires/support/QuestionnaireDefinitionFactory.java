/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.zynap.common.util.DefaultXmlErrorHandler;
import com.zynap.common.util.StringUtil;
import com.zynap.common.util.XmlConstants;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.LineItem;
import com.zynap.talentstudio.questionnaires.MultiQuestionItem;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.questionnaires.QuestionGroup;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinitionModel;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.digester.xmlrules.FromXmlRuleSet;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;

import org.springframework.util.StringUtils;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class QuestionnaireDefinitionFactory {

    public static QuestionnaireDefinition parse(byte[] xmlData) throws Exception {

        String rulesFile = "digester/rules.xml";
        URL rulesURL = QuestionnaireDefinitionFactory.class.getClassLoader().getResource(rulesFile);
        URL schemaURL = QuestionnaireDefinitionFactory.class.getClassLoader().getResource("digester/questionnaire.xsd");
        if(schemaURL == null) throw new IOException("cannot proceed without the schema");
        
        DigesterQuestionnaireDefinition digesterQuestionnaireDefinition;
        try {
            SAXParserFactoryImpl f = new SAXParserFactoryImpl();
            f.setValidating(true);
            f.setNamespaceAware(true);
            javax.xml.parsers.SAXParser p = f.newSAXParser();
            XMLReader xmlReader = p.getXMLReader();

            xmlReader.setProperty(XmlConstants.SCHEMA_ATTRIBUTE_KEY, XmlConstants.XML_SCHEMA_VALUE);
            xmlReader.setProperty(XmlConstants.SCHEMA_SOURCE_ATTRIBUTE_KEY, schemaURL.toString());

            RuleSet ruleSet = new FromXmlRuleSet(rulesURL);
            Digester digester = new Digester(p);
            digester.addRuleSet(ruleSet);
            digester.setSchema(schemaURL.toString());
            digester.setErrorHandler(new DefaultXmlErrorHandler());

            xmlReader.setContentHandler(digester);
            xmlReader.setEntityResolver(digester);

            String questionnaireDef = new String(xmlData).trim();
            digesterQuestionnaireDefinition = (DigesterQuestionnaireDefinition) digester.parse(new InputSource(new StringReader(questionnaireDef)));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXParseException e) {
            // check the nested exception is one of ours
            Exception exception = e.getException();
            if(exception instanceof InvalidXmlDefinitionException) {
                throw exception;
            }
            throw new InvalidXmlDefinitionException(e.getMessage(), e, e.getLineNumber(), e.getColumnNumber());
        } catch (SAXException e) {
            throw new SchemaValidationFailedException("Xml does not match the schema: " + e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } 

        QuestionnaireDefinition questionnaireDefinition = digesterQuestionnaireDefinition.getQuestionnaireDefinition();

        QuestionnaireDefinitionModel questionnaireDefinitionModel = new QuestionnaireDefinitionModel();
        questionnaireDefinitionModel.setQuestionnaireDefinition(questionnaireDefinition);
        
        createQuestionnaireModel(questionnaireDefinitionModel, digesterQuestionnaireDefinition);
        return questionnaireDefinition;
    }

    public static void createQuestionnaireModel(QuestionnaireDefinitionModel questionnaireDefinitionModel, DigesterQuestionnaireDefinition digesterQuestionnaireDefinition) throws IllegalEmptyQuestionLabelException {

        if (digesterQuestionnaireDefinition != null) {

            for (Object o : digesterQuestionnaireDefinition.getQuestionnaireGroups()) {

                QuestionnaireGroup questionnaireGroup = (QuestionnaireGroup) o;
                QuestionGroup questionGroup = new QuestionGroup(questionnaireGroup.getLabel(), questionnaireGroup.isDisplayable());
                List questions = questionnaireGroup.getQuestions();
                createModel(questions, questionGroup);
                questionnaireDefinitionModel.addQuestionGroup(questionGroup);
            }

            questionnaireDefinitionModel.getQuestionnaireDefinition().setQuestionnaireDefinitionModel(questionnaireDefinitionModel);
        }
    }

    private static void createModel(List questions, QuestionGroup questionGroup) throws IllegalEmptyQuestionLabelException {

        for (Object question : questions) {
            BaseQuestion baseQuestion = (BaseQuestion) question;
            if (baseQuestion instanceof MultiQuestion) {
                addMultiQuestion(baseQuestion, questionGroup);

            } else if (baseQuestion instanceof Narrative) {
                QuestionAttribute questionAttribute = createNarrative(baseQuestion);
                questionGroup.addQuestion(questionAttribute);

            } else if (baseQuestion instanceof Question) {
                QuestionAttribute questionAttribute = createQuestion(baseQuestion);
                questionGroup.addQuestion(questionAttribute);
            }
        }
    }

    private static void addMultiQuestion(BaseQuestion multiQuestionBase, QuestionGroup questionGroup) throws IllegalEmptyQuestionLabelException {

        MultiQuestion multiQuestion = (MultiQuestion) multiQuestionBase;
        List questionItems = multiQuestion.getQuestionItems();
        MultiQuestionItem multiQuestionItem = new MultiQuestionItem(multiQuestion.getName());

        QuestionAttribute questionAttribute;

        for (Object questionItem : questionItems) {
            QuestionLineItem questionLineItem = (QuestionLineItem) questionItem;
            LineItem lineItem = new LineItem(questionLineItem.getLabel(), questionLineItem.isDynamicLineItem(), questionLineItem.isCanDisable());
            List lineItemQuestions = questionLineItem.getQuestions();
            for (Object lineItemQuestion : lineItemQuestions) {
                BaseQuestion question = (BaseQuestion) lineItemQuestion;
                if (question instanceof Narrative) {
                    questionAttribute = createNarrative(question);
                } else {
                    questionAttribute = createQuestion(question);
                }
                lineItem.addQuestion(questionAttribute);
            }
            multiQuestionItem.addLineItem(lineItem);
        }
        questionGroup.addMultiQuestion(multiQuestionItem);
    }

    private static QuestionAttribute createQuestion(BaseQuestion baseQuestion) throws IllegalEmptyQuestionLabelException {
        Question question = (Question) baseQuestion;

        if(!StringUtils.hasText(question.getLabel())) {
            throw new IllegalEmptyQuestionLabelException("Questions must have a name attribute that is non empty", "error.empty.question.label.field", new Object[] {question.getTextId()});
        }

        DynamicAttribute attribute = question.getDynamicAttribute();
        
        if(attribute.getExternalRefLabel() == null) {
            attribute.setExternalRefLabel(StringUtil.clean(attribute.getLabel().trim().toLowerCase()));
            attribute.setLabel(question.getLabel());
            if(attribute.getUniqueNumber() == null) {
                attribute.setUniqueNumber(generateUniqueNumber());
            }
        }
        return new QuestionAttribute(attribute, question.getLength(), question.getTarget(), 
                question.getTextId(), question.getTitle(), question.getType(), question.isManagerWrite(), question.isHidden(), question.getSortOrder());
    }

    private static QuestionAttribute createNarrative(BaseQuestion baseQuestion) {
        Narrative narrative = (Narrative) baseQuestion;
        return new QuestionAttribute(narrative.getDescription());
    }

    public static Double generateUniqueNumber() {

        double value;
        try {
            value = Math.abs(java.security.SecureRandom.getInstance("SHA1PRNG").nextDouble());
        } catch (NoSuchAlgorithmException e) {
            value = System.currentTimeMillis();
        }

        return value;
    }
}
