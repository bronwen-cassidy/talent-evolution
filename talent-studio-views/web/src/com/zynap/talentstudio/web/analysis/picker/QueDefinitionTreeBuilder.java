/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.picker;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.AbstractQuestion;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinitionModel;
import com.zynap.talentstudio.questionnaires.QuestionGroup;
import com.zynap.talentstudio.web.utils.tree.Leaf;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 05-Oct-2009 09:25:34
 */
public class QueDefinitionTreeBuilder extends QuestionnaireTreeBuilder {

    public void buildQuestionnaireBranch(AnalysisAttributeBranch root, QuestionnaireDefinition definition, Long questionnaireId) {

        final QuestionnaireDefinitionModel definitionModel = definition.getQuestionnaireDefinitionModel();
        final List questionnaireGroups = definitionModel.getQuestionGroups();
        final String prefix = root.getPrefix();
        for (Iterator iterator = questionnaireGroups.iterator(); iterator.hasNext();) {

            QuestionGroup questionnaireGroup = (QuestionGroup) iterator.next();
            final String groupLabel = questionnaireGroup.getLabel();
            final String groupId = QUESTION_GROUP_PREFIX + questionnaireGroup.getId();
            AnalysisAttributeBranch groupBranch = new AnalysisAttributeBranch(groupId, groupLabel, prefix);

            final List<AbstractQuestion> questions = questionnaireGroup.getAbstractQuestions();
            addQuestions(groupBranch, questionnaireId , questions, null);
            // only display group if group has questions
            if (groupBranch.isHasChildren() || groupBranch.hasLeaves()) {
                root.addChild(groupBranch);
            }
        }
    }

    protected void addQuestion(AnalysisAttributeBranch root, Long questionnaireId, QuestionAttribute question, List roles) {
        if (question.getDynamicAttribute() == null) return;

        final DynamicAttribute attribute = question.getDynamicAttribute();

        if (attributeTypes.isEmpty() || attributeTypes.contains(attribute.getType())) {
            final String questionId = question.getDynamicAttribute().getId().toString();
            Leaf quesLeaf = new Leaf(questionId, question.getLabel());
            root.addLeaf(quesLeaf);
        }
    }

    public void setAttributeTypes(List<String> attributeTypes) {
        this.attributeTypes = attributeTypes;
    }

    private List<String> attributeTypes = new ArrayList<String>();
}
