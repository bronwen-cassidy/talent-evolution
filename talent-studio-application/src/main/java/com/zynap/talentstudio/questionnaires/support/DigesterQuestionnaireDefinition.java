/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires.support;

import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;

import java.io.Serializable;
import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DigesterQuestionnaireDefinition implements Serializable {

    private static final long serialVersionUID = 3557094222256038824L;

    public DigesterQuestionnaireDefinition() {
        questionnaireDefinition = new QuestionnaireDefinition();
    }

    public void addQuestionnaireGroup(QuestionnaireGroup group) {
        if (!group.isEmpty()) {
            questionnaireGroups.add(group);
            group.mapQuestions(allQuestionsMap);
        }
    }

    public List getQuestionnaireGroups() {
        return questionnaireGroups;
    }

    /**
     * Get QuestionnaireGroup with matching name.
     *
     * @param groupName get a group with the given unique name
     * @return QuestionnaireGroup or null
     */
    public QuestionnaireGroup getQuestionnaireGroup(String groupName) {
        if (questionnaireGroups != null) {
            for (Object questionnaireGroup : questionnaireGroups) {
                QuestionnaireGroup group = (QuestionnaireGroup) questionnaireGroup;
                final String label = group.getLabel();
                if (label != null && label.equals(groupName)) {
                    return group;
                }
            }
        }

        return null;
    }

    /**
     * @return the questionnaire defintion that has been all tied up together.
     * @throws InvalidXmlDefinitionException when the questionnaire xml is invalid
     */
    public QuestionnaireDefinition getQuestionnaireDefinition() throws InvalidXmlDefinitionException {
        List allQuestions = getQuestions();
        assignReferences(allQuestions);
        assignDynamicAttributes();
        return questionnaireDefinition;
    }

    public List getDefinitionDynamicAttributes() {
        return questionnaireDefinition.getDynamicAttributes();
    }

    public void setLabel(String label) {
        questionnaireDefinition.setLabel(label);
    }

    public void setTitle(String title) {
        questionnaireDefinition.setTitle(title);
    }

    public String getTitle() {
        return questionnaireDefinition.getTitle();
    }

    /**
     * Get full list of question for definition.
     * <br/> Do not rely on this method to get an ordered list of questions back.
     *
     * @return List of BaseQuestion objects
     */
    public List getQuestions() {
        List<BaseQuestion> questions = new ArrayList<BaseQuestion>();

        for (Object questionnaireGroup1 : questionnaireGroups) {
            QuestionnaireGroup questionnaireGroup = (QuestionnaireGroup) questionnaireGroup1;
            questions.addAll(questionnaireGroup.getQuestions());
        }

        return questions;
    }

    private void assignDynamicAttributes() throws InvalidXmlDefinitionException {
        for (Object questionnaireGroup1 : questionnaireGroups) {
            QuestionnaireGroup questionnaireGroup = (QuestionnaireGroup) questionnaireGroup1;
            questionnaireDefinition.getDynamicAttributes().addAll(questionnaireGroup.getDynamicAttributes(questionnaireDefinition));
        }
    }

    private void assignReferences(List allQuestions) throws InvalidQuestionReferenceException {
        for (Object allQuestion : allQuestions) {
            final BaseQuestion baseQuestion = (BaseQuestion) allQuestion;
            if (baseQuestion instanceof Question) {
                Question question = (Question) baseQuestion;
                if (question.isEnumMapping() || question.isSum()) {
                    List references = question.getReferences();
                    for (Object reference1 : references) {
                        QuestionReference questionReference = (QuestionReference) reference1;
                        String referenceId = questionReference.getReferenceId();
                        Question reference = (Question) allQuestionsMap.get(referenceId);
                        if(reference == null) throw new InvalidQuestionReferenceException("", "reference.not.found", new Object[] {referenceId, question.getLabel()});
                        questionReference.setReferenceQuestion(reference);
                    }
                }
            }
        }
    }

    private QuestionnaireDefinition questionnaireDefinition;
    private List<QuestionnaireGroup> questionnaireGroups = new ArrayList<QuestionnaireGroup>();
    private Map<String, BaseQuestion> allQuestionsMap = new HashMap<String, BaseQuestion>();
}
