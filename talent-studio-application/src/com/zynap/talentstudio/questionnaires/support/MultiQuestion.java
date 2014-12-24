/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MultiQuestion extends BaseQuestion {

    private static final long serialVersionUID = 8286754771662339341L;

    public String getLabel() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    /**
     * Add line item.
     * <br/> Ensures that only one dynamic line item can be added.
     *
     * @param questionLineItem
     * @throws InvalidXmlDefinitionException
     */
    public void addLineItem(QuestionLineItem questionLineItem) throws InvalidXmlDefinitionException {

        if (!questionItems.isEmpty()) {
            throw new InvalidMultiQuestionException("Multi question can only contain 1 line item", MULTIPLE_DYNAMIC_LINE_ITEM_ERROR, getLabel());
            // check first element - if it is dynamic then no more line items are allowed.
//            QuestionLineItem lineItem = questionItems.get(0);
//            if (lineItem.isDynamicLineItem()) {
//                throw new InvalidMultiQuestionException("Multi question can only contain 1 dynamic line item", MULTIPLE_DYNAMIC_LINE_ITEM_ERROR, getLabel());
//            }
//
//            // if line item to be added is dynamic then
//            if (questionLineItem.isDynamicLineItem()) {
//                throw new InvalidMultiQuestionException("Multi question can only contain 1 dynamic line item", MULTIPLE_DYNAMIC_LINE_ITEM_ERROR, getLabel());
//            }
        }

        questionItems.add(questionLineItem);
    }

    public void addQuestion(Question question) {
        question.setMultiQuestion(this);
        questions.add(question);
    }

    public List<QuestionLineItem> getQuestionItems() {
        return questionItems;
    }

    public List<BaseQuestion> getQuestions() {
        return questions;
    }

    public void mapQuestions(Map<String, BaseQuestion> allQuestionsMap) {
        for (int i = 0; i < questions.size(); i++) {
            Question question = (Question) questions.get(i);
            allQuestionsMap.put(question.getTextId(), question);
        }
    }

    /**
     * Method is called after all digester processing has been done.
     * <br/> Clones the questions and the dynamic attributes.
     *
     * @param questionnaireDefinition the definition
     * @return List of dynamicAttributes
     * @throws InvalidXmlDefinitionException
     */
    final List<DynamicAttribute> getDynamicAttributes(QuestionnaireDefinition questionnaireDefinition) throws InvalidXmlDefinitionException {

        List<DynamicAttribute> attributes = new ArrayList<DynamicAttribute>();
        List<BaseQuestion> newQuestions = new ArrayList<BaseQuestion>();

        int i = 0;

        for (Iterator itemIterator = questionItems.iterator(); itemIterator.hasNext();) {
            QuestionLineItem questionLineItem = (QuestionLineItem) itemIterator.next();

            for (Iterator iterator = questions.iterator(); iterator.hasNext();) {

                Question question = (Question) iterator.next();
                questionLineItem.addQuestion(question);
                DynamicAttribute dynamicAttribute = question.getDynamicAttribute();
                dynamicAttribute.setQuestionnaireDefinition(questionnaireDefinition);
                dynamicAttribute.setQuestionnaireDefinitionId(questionnaireDefinition.getId());
                dynamicAttribute.setDescription(questionLineItem.getLabel() + " - " + dynamicAttribute.getDescription());
                attributes.add(dynamicAttribute);
                newQuestions.add(question);
//                
//                Question clonedQuestion = (Question) question.clone();
//                clonedQuestion.setTextId(clonedQuestion.getTextId() + "_" + i);
//                questionLineItem.addQuestion(clonedQuestion);
//                DynamicAttribute dynamicAttribute = clonedQuestion.getDynamicAttribute();
//                dynamicAttribute.setQuestionnaireDefinition(questionnaireDefinition);
//                dynamicAttribute.setQuestionnaireDefinitionId(questionnaireDefinition.getId());
//                dynamicAttribute.setDescription(questionLineItem.getLabel() + " - " + dynamicAttribute.getDescription());
//                attributes.add(dynamicAttribute);
//                newQuestions.add(clonedQuestion);
            }
            i++;
        }
        this.questions = newQuestions;
        return attributes;
    }

    public boolean isMultiQuestion() {
        return true;
    }

    public QuestionnaireGroup getQuestionnaireGroup() {
        return this.questionnaireGroup;
    }

    public void setQuestionnaireGroup(QuestionnaireGroup questionnaireGroup) {
        this.questionnaireGroup = questionnaireGroup;
    }

    private String name;
    private List<QuestionLineItem> questionItems = new ArrayList<QuestionLineItem>();
    private List<BaseQuestion> questions = new ArrayList<BaseQuestion>();
    private QuestionnaireGroup questionnaireGroup;

    private static final String MULTIPLE_DYNAMIC_LINE_ITEM_ERROR = "error.multiple.dynamic.lineitem";
}
