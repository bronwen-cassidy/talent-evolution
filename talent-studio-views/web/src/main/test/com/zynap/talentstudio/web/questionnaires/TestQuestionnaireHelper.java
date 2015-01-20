package com.zynap.talentstudio.web.questionnaires;

/**
 * User: amark
 * Date: 08-Sep-2006
 * Time: 12:08:33
 */

import com.zynap.talentstudio.questionnaires.*;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionnaireHelper extends ZynapDbUnitMockControllerTestCase {

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();

        questionnaireHelper = (QuestionnaireHelper) getBean("questionnaireHelper");
        IQueDefinitionService questionnaireService = (IQueDefinitionService) getBean("questionnaireDefinitionService");
        questionnaireDefinition = questionnaireService.findDefinition(QUE_DEFINITION_ID);
    }

    public void testSetQuestionnaireState() throws Exception {

        // initialise wrappers
        final Questionnaire questionnaire = null;
        final QuestionnaireWrapper questionnaireWrapper = new QuestionnaireWrapper();
        questionnaireHelper.setQuestionnaireState(questionnaireWrapper, questionnaireDefinition, questionnaire);

        // check groups
        final QuestionnaireDefinitionModel digesterQuestionnaireDefinition = questionnaireDefinition.getQuestionnaireDefinitionModel();
        final List questionnaireGroups = digesterQuestionnaireDefinition.getQuestionGroups();

        final List questionnaireGroupWrappers = questionnaireWrapper.getQuestionnaireGroups();
        assertEquals(questionnaireGroups.size(), questionnaireGroupWrappers.size());
        for (int i = 0; i < questionnaireGroupWrappers.size(); i++) {
            final QuestionGroupWrapper questionGroupWrapper = (QuestionGroupWrapper) questionnaireGroupWrappers.get(i);
            final QuestionGroup questionnaireGroup = (QuestionGroup) questionnaireGroups.get(i);

            // compare labels
            assertEquals(questionGroupWrapper.getLabel(), questionnaireGroup.getLabel());

            // check questions against formattributes
            final List groupFormAttributes = questionGroupWrapper.getWrappedDynamicAttributes();
            final List questionsNarratives = questionnaireGroup.getAbstractQuestions();
            if (!questionsNarratives.isEmpty()) {

                final AbstractQuestion baseQuestion = (AbstractQuestion) questionsNarratives.get(0);
                if (baseQuestion.isMultiQuestion()) {
                    final MultiQuestionItem multiQuestion = (MultiQuestionItem) baseQuestion;
                    final List childQuestions = multiQuestion.getQuestions();
                    final QuestionAttribute childQuestion = (QuestionAttribute) childQuestions.get(0);
                    if (childQuestion.isDynamic()) {

                        // check dynamiclineitemwrapper
                        assertEquals(1, groupFormAttributes.size());
                        final LineItemWrapper dynamicLineItemWrapper = (LineItemWrapper) groupFormAttributes.get(0);
                        checkFormAttributes(childQuestions, dynamicLineItemWrapper.getQuestionWrappers());

                    } else {

                        // check lineitemwrapper
                        assertEquals(1, groupFormAttributes.size());
                        final LineItemWrapper lineItemWrapper = (LineItemWrapper) groupFormAttributes.get(0);
                        assertEquals(1, lineItemWrapper.getNumberOfLineItems());
                        checkFormAttributes(childQuestions, lineItemWrapper.getQuestionWrappers());
                    }
                } else {

                    // check normal questions
                    checkFormAttributes(questionsNarratives, groupFormAttributes);
                }
            } else {

                // other list must be empty as well
                assertEquals(0, groupFormAttributes.size());
            }
        }

        // number of dynamicAttributes and wrapped dynamic attributes must be the same
        final List dynamicAttributes = questionnaireDefinition.getDynamicAttributes();
        final List wrappedDynamicAttributes = questionnaireWrapper.getWrappedDynamicAttributes();
        assertEquals(dynamicAttributes.size(), wrappedDynamicAttributes.size());
    }

    public void testFilterQuestionnaireDefinitions() throws Exception {        
        List<QuestionnaireDefinition> questionnaireDefinitions = new ArrayList<QuestionnaireDefinition>(1);
        questionnaireDefinitions.add(questionnaireDefinition);
        List filteredDefinitions = questionnaireHelper.filterQuestionnaireDefinitions(questionnaireDefinitions, new String[] {"NUMBER","SUM"}, true);
        QuestionnaireDefinition filtered = (QuestionnaireDefinition) filteredDefinitions.get(0);
        QuestionnaireDefinitionModel questionnaireDefinitionModel = filtered.getQuestionnaireDefinitionModel();
        List groups = questionnaireDefinitionModel.getQuestionGroups();
        assertEquals("only 2 groups had questions, therefore should have only been 2 groups returned", 2, groups.size());
        QuestionGroup sumGroup = (QuestionGroup) groups.get(0);
        assertEquals("Only one sum attribute therefore expected only one question in the group", 1, sumGroup.getQuestions().size());
    }

    /**
     * Check count and labels of original questions / narratives vs. wrappers.
     *
     * @param questionsNarratives all questions
     * @param groupFormAttributes the form attributes of the wrapper
     */
    private void checkFormAttributes(final List questionsNarratives, final List groupFormAttributes) {

        assertEquals(questionsNarratives.size(), groupFormAttributes.size());
        for (int j = 0; j < questionsNarratives.size(); j++) {
            final AbstractQuestion baseQuestion = (AbstractQuestion) questionsNarratives.get(j);
            final FormAttribute formAttribute = (FormAttribute) groupFormAttributes.get(j);
            assertEquals(formAttribute.getLabel(), baseQuestion.getLabel());

            if (formAttribute.isEditable()) {
                QuestionAttributeWrapperBean attributeWrapperBean = (QuestionAttributeWrapperBean) formAttribute;
                if (attributeWrapperBean.isPartOfMultiQuestion()) {
                    assertEquals(((QuestionAttribute) baseQuestion).getLineItem().getLabel(), attributeWrapperBean.getLineItemLabel());
                }
            }
        }
    }

    private QuestionnaireDefinition questionnaireDefinition;
    private QuestionnaireHelper questionnaireHelper;

    private static final Long QUE_DEFINITION_ID = new Long(82);
}