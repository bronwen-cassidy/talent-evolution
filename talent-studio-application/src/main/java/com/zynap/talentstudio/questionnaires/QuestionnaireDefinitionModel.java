/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.talentstudio.questionnaires.support.ITreeElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class QuestionnaireDefinitionModel implements Serializable, ITreeElement {

    private static final long serialVersionUID = -8990939404873739691L;


    public QuestionnaireDefinitionModel() {
    }

    public QuestionnaireDefinitionModel(QuestionnaireDefinition definition) {
        this.questionnaireDefinition = definition;
        this.queDefinitionId = definition.getId();
    }

    public QuestionnaireDefinition getQuestionnaireDefinition() {
        return questionnaireDefinition;
    }

    public void setQuestionnaireDefinition(QuestionnaireDefinition questionnaireDefinition) {
        this.questionnaireDefinition = questionnaireDefinition;
    }

    public Long getQueDefinitionId() {
        return queDefinitionId;
    }

    public void setQueDefinitionId(Long queDefinitionId) {
        this.queDefinitionId = queDefinitionId;
    }

    /**
     * Hibernate getter method to get the groups defined for the questionnaire model.
     *
     * @return questionGroups
     */
    public List<QuestionGroup> getQuestionGroups() {
        return questionGroups != null ? questionGroups : new ArrayList<QuestionGroup>();
    }

    public void setQuestionGroups(List<QuestionGroup> questionGroups) {
        this.questionGroups = questionGroups;
    }

    public void addQuestionGroup(QuestionGroup questionGroup) {
        if(questionGroups == null) questionGroups = new ArrayList<QuestionGroup>();
        questionGroup.setQuestionnaireDefinitionModel(this);
        questionGroups.add(questionGroup);
    }

    /**
     * Get full list of question for definition.
     * <br/> Do not rely on this method to get an ordered list of questions back.
     * Not a hibernate method.
     *
     * @return List of AbstractQuestion objects
     */
    public List<AbstractQuestion> getQuestions() {
        
        List<AbstractQuestion> questions = new ArrayList<AbstractQuestion>();

        for (Iterator iterator = questionGroups.iterator(); iterator.hasNext();) {
            QuestionGroup questionGroup = (QuestionGroup) iterator.next();
            questions.addAll(questionGroup.getAbstractQuestions());
        }

        return questions;
    }

    public QuestionGroup getQuestionGroup(String groupName) {
        for (Iterator iterator = questionGroups.iterator(); iterator.hasNext();) {
            QuestionGroup questionGroup = (QuestionGroup) iterator.next();
            if(questionGroup.getLabel().equals(groupName)) return questionGroup;
        }
        return null;
    }


    public String getLabel() {
        return questionnaireDefinition.getLabel();
    }

    public Collection<ITreeElement> getChildren() {
        Collection<ITreeElement> all = new ArrayList<ITreeElement>(getQuestionGroups());
        all.addAll(getQuestions());
        return all;
    }

    public String getElementId() {
        return null;
    }

    private QuestionnaireDefinition questionnaireDefinition;
    private Long queDefinitionId;
    private List<QuestionGroup> questionGroups;
}
