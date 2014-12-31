/**
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Rule factory to create the questionnaireDefinition
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Jul-2005 02:29:17
 */
public class QuestionnaireGroup implements Serializable {

    private static final long serialVersionUID = 5265469791035663823L;

    /**
     * Method extracts the dynamicAttributes from each of the questions and from the each of any multiquestions' child questions.
     *
     * @param questionnaireDefinition the source definition
     * @return List of dynamicAttributes
     * @throws InvalidXmlDefinitionException if the xml is invalid
     */
    final List<DynamicAttribute> getDynamicAttributes(QuestionnaireDefinition questionnaireDefinition) throws InvalidXmlDefinitionException {

        List<DynamicAttribute> attributes = new ArrayList<DynamicAttribute>();

        for (Iterator iterator = questions.iterator(); iterator.hasNext();) {

            BaseQuestion question = (BaseQuestion) iterator.next();
            Collection<DynamicAttribute> qAttributes = question.getDynamicAttributes(questionnaireDefinition);
            attributes.addAll(qAttributes);
        }
        return attributes;
    }

    /**
     * Add question if not status question.
     * <br/> set question group on question.
     *
     * @param question the question
     */
    public void addQuestion(Question question) {

        // ignore status question
        if (!(Question.TYPE_STATUS.equals(question.getType()))) {
            question.setQuestionnaireGroup(this);
            question.setSortOrder(questions.size());
            questions.add(question);
        }
    }

    /**
     * Add multiquestion.
     * <br/> Sets questionnaire group on question.
     *
     * @param multiQuestion the multiQuestion
     */
    public void addMultiQuestion(MultiQuestion multiQuestion) {
        multiQuestion.setQuestionnaireGroup(this);
        questions.add(multiQuestion);
    }

    /**
     * Get questions.
     *
     * @return List of Questions and/or MultiQuestions.
     */
    public List<BaseQuestion> getQuestions() {
        return questions;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) throws IllegalEmptyGroupLabelException {
        if(!StringUtils.hasText(label)) throw new IllegalEmptyGroupLabelException("A group cannot have an empty or non-existant label", "error.illegal.empty.group.name", new Object[0]);
        this.label = label;
    }

    /**
     * add new narrative for each description.
     *
     * @param description represents a narrative
     */
    public void setDescription(String description) {
        Narrative narrative = new Narrative(description);
        questions.add(narrative);
    }

    public boolean isEmpty() {
        return questions.isEmpty();
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void mapQuestions(Map<String, BaseQuestion> allQuestionsMap) {
        for (int i = 0; i < questions.size(); i++) {
            BaseQuestion questionBase = questions.get(i);
            if(questionBase instanceof MultiQuestion) {
                ((MultiQuestion)questionBase).mapQuestions(allQuestionsMap);
            }
            else if (questionBase instanceof Question) {
                Question question = (Question) questionBase;
                allQuestionsMap.put(question.getTextId(), question);
            }
        }
    }

    public String toString() {
        return label;
    }

    private List<BaseQuestion> questions = new ArrayList<BaseQuestion>();
    private String label;
    private String title;
    private boolean displayable = true;
}
