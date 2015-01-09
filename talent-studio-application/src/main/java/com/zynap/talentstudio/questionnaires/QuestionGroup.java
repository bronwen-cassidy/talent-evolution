/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.questionnaires.support.ITreeElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 25-Jan-2007 17:18:31
 */
public class QuestionGroup extends ZynapDomainObject implements ITreeElement {

    private static final long serialVersionUID = 5333004687899487958L;

    /**
     * Required default constructor
     */
    public QuestionGroup() {
    }

    public QuestionGroup(String label, boolean displayable) {
        this.label = label;
        this.displayable = displayable;
    }

    public QuestionGroup(Long id, String label) {
        this.id = id;
        this.label = label == null ? "" : label;
    }

    public QuestionnaireDefinitionModel getQuestionnaireDefinitionModel() {
        return questionnaireDefinitionModel;
    }

    public void setQuestionnaireDefinitionModel(QuestionnaireDefinitionModel questionnaireDefinitionModel) {
        this.questionnaireDefinitionModel = questionnaireDefinitionModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }

    /**
     * Hibernate getter for all {@link com.zynap.talentstudio.questionnaires.QuestionAttribute} objects.
     *
     * Narratives are also questionAttributes, but without a linked dynamicttribute.
     * @return QuestionAttribute objects
     */
    public List<QuestionAttribute> getQuestions() {
        return questions;
    }

    /**
     * Hibernate setter for the {@link com.zynap.talentstudio.questionnaires.QuestionAttribute} items for this group.
     *
     * @param questions all questionAttributes (which includes the narrative)
     */
    public void setQuestions(List<QuestionAttribute> questions) {
        this.questions = questions;
    }

    /**
     * Hibernate getter for all {@link com.zynap.talentstudio.questionnaires.MultiQuestionItem } objects.
     *
     * @return the multiQuestionItems.
     */
    public List<MultiQuestionItem> getMultiQuestions() {
        return multiQuestions;
    }

    /**
     * Hibernate setter for all {@link com.zynap.talentstudio.questionnaires.MultiQuestionItem} objects.
     *
     * @param multiQuestions the multiQuestions in this group.
     */
    public void setMultiQuestions(List<MultiQuestionItem> multiQuestions) {
        this.multiQuestions = multiQuestions;
    }

    /**
     * Used by the tree builder pickers, returns all {@link com.zynap.talentstudio.questionnaires.AbstractQuestion } objects.
     *
     * @return AbstractQuestions which include all QuestionAttributes and MultiQuestions
     */
    public Collection<AbstractQuestion> getChildren() {
        return getAbstractQuestions();
    }

    public String getElementId() {
        return null;
    }

    public void addQuestion(QuestionAttribute questionAttribute) {
        if(questions == null) questions = new ArrayList<QuestionAttribute>();
        questionAttribute.setQuestionGroup(this);
        questions.add(questionAttribute);
    }

    public void addMultiQuestion(MultiQuestionItem multiQuestionItem) {
        if(multiQuestions == null) multiQuestions = new ArrayList<MultiQuestionItem>();
        multiQuestionItem.setGroup(this);
        multiQuestions.add(multiQuestionItem);
    }


    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("label: ").append(label);
        result.append(" displayable: ").append(displayable);
        return result.toString();
    }

    /**
     * Gets both the questions in this group and any multiquestions.
     * The list is composed of {@link com.zynap.talentstudio.questionnaires.AbstractQuestion } objects
     * of which {@link com.zynap.talentstudio.questionnaires.MultiQuestionItem } objects and
     * {@link com.zynap.talentstudio.questionnaires.QuestionAttribute} extend.
     *
     * @return a list of {@link com.zynap.talentstudio.questionnaires.AbstractQuestion} objects.
     */
    public List<AbstractQuestion> getAbstractQuestions() {
        List<AbstractQuestion> result = new ArrayList<AbstractQuestion>();
        if(questions != null && !questions.isEmpty()) result.addAll(questions);
        if(multiQuestions != null && !multiQuestions.isEmpty()) result.addAll(multiQuestions);
        return result;
    }

    /**
     * Gets all {@link com.zynap.talentstudio.questionnaires.QuestionAttribute} objects within this group.
     * This method is an accumulative call to collect all the questions from both the group and the groups multi-questions,
     * and hence the lineItems.
     *
     * @return a list of all the question attributes on the group and any within the multiquestions/line-items of this group.
     */
    public List<QuestionAttribute> collectAllQuestions() {
        List<QuestionAttribute> result = new ArrayList<QuestionAttribute>();
        if(questions != null) result.addAll(questions);
        if(multiQuestions != null) {
            for(MultiQuestionItem multiQuestionItem : multiQuestions) {
                result.addAll(multiQuestionItem.getQuestions());
            }            
        }
        return result;
    }

    private String title;
    private boolean displayable;

    private List<QuestionAttribute> questions;
    private List<MultiQuestionItem> multiQuestions;
    private QuestionnaireDefinitionModel questionnaireDefinitionModel;
}
