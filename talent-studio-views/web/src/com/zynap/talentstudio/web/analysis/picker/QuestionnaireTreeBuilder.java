/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.picker;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.questionnaires.AbstractQuestion;
import com.zynap.talentstudio.questionnaires.LineItem;
import com.zynap.talentstudio.questionnaires.MultiQuestionItem;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.questionnaires.QuestionGroup;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinitionModel;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.utils.tree.Leaf;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Jan-2008 14:32:07
 */

/**
 * builds sub-tree for appraisals and questionnaires.
 * <br/> Also handles finding question labels.
 */
public class QuestionnaireTreeBuilder {

    public QuestionnaireTreeBuilder() {
    }

    /**
     * Adds the questionnaires and appraisal branches.
     *
     * @param parent                   the subject branch always
     * @param questionnaireDefinitions the list of definitions to add
     */
    void addQuestionnaires(AnalysisAttributeBranch parent, Collection questionnaireDefinitions) {

        final String prefix = parent.getPrefix();

        final String appraisalBranchId = PopulationCriteriaBuilder.buildId(prefix, PopulationCriteriaBuilder.APPRAISALS_BRANCH_ID);
        AnalysisAttributeBranch appraisalBranch = new AnalysisAttributeBranch(appraisalBranchId, APPRAISALS_LABEL, Node.QUESTIONNAIRE_TYPE, false, false, prefix);

        final String questionnaireBranchId = PopulationCriteriaBuilder.buildId(prefix, PopulationCriteriaBuilder.QUESTIONNAIRE_BRANCH_ID);
        AnalysisAttributeBranch questionnaireBranch = new AnalysisAttributeBranch(questionnaireBranchId, QUESTIONNAIRES_LABEL, Node.QUESTIONNAIRE_TYPE, false, false, prefix);

        for (Iterator iterator = questionnaireDefinitions.iterator(); iterator.hasNext();) {
            QuestionnaireDefinition questionnaireDefinition = (QuestionnaireDefinition) iterator.next();

            final Set questionnaireWorkflows = questionnaireDefinition.getQuestionnaireWorkflows();
            for (Iterator workflowIterator = questionnaireWorkflows.iterator(); workflowIterator.hasNext();) {
                QuestionnaireWorkflow questionnaireWorkflow = (QuestionnaireWorkflow) workflowIterator.next();

                if (isPerformanceReview(questionnaireWorkflow)) {
                    addReviewWorkflows(appraisalBranch, questionnaireWorkflow.getPerformanceReview());
                } else {
                    addWorkflow(questionnaireBranch, questionnaireWorkflow, "");
                }
            }
        }
        if (appraisalBranch.isHasChildren() && !parent.hasBranch(appraisalBranch.getId())) parent.addChild(appraisalBranch);
        if (questionnaireBranch.isHasChildren() && !parent.hasBranch(questionnaireBranch.getId())) parent.addChild(questionnaireBranch);
    }

    /**
     * Updates questionnaire workflow groups and question information
     *
     * @param definition
     * @param roles
     * @param workflow
     * @param workflowBranch
     */
    void updateQuestionnaires(QuestionnaireDefinitionModel definition, List roles, QuestionnaireWorkflow workflow, AnalysisAttributeBranch workflowBranch) {

        if (!isPerformanceReview(workflow) || workflow.isQuestionnaireManager()) roles = null;
        final String prefix = workflowBranch.getPrefix();

        final List questionnaireGroups = definition.getQuestionGroups();
        for (Iterator iterator = questionnaireGroups.iterator(); iterator.hasNext();) {

            QuestionGroup questionnaireGroup = (QuestionGroup) iterator.next();
            final String groupLabel = questionnaireGroup.getLabel();
            final String groupId = workflowBranch.getId() + QUESTION_GROUP_PREFIX + questionnaireGroup.getId();
            AnalysisAttributeBranch groupBranch = new AnalysisAttributeBranch(groupId, groupLabel, prefix);

            final List questions = questionnaireGroup.getAbstractQuestions();
            addQuestions(groupBranch, workflow.getId(), questions, roles);
            // only display group if group has questions
            if (groupBranch.isHasChildren() || groupBranch.hasLeaves()) {
                workflowBranch.addChild(groupBranch);
            }
        }
    }

    private boolean isPerformanceReview(QuestionnaireWorkflow workflow) {
        return workflow.getPerformanceReview() != null;
    }

    private void addReviewWorkflows(AnalysisAttributeBranch appraisalBranch, PerformanceReview review) {

        final String id = appraisalBranch.getId() + APPRAISAL_WORKFLOW_PREFIX + String.valueOf(review.getId());
        // already set
        if (appraisalBranch.hasBranch(id)) return;

        final String prefix = appraisalBranch.getPrefix();
        final String label = review.getLabel();
        AnalysisAttributeBranch performanceReviewBranch = new AnalysisAttributeBranch(id, label, Node.QUESTIONNAIRE_TYPE, false, false, prefix);
        appraisalBranch.addChild(performanceReviewBranch);
        Set workflows = review.getQueWorkflows();
        for (Iterator iterator1 = workflows.iterator(); iterator1.hasNext();) {
            QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) iterator1.next();
            addQuestionnaireAppraisal(performanceReviewBranch, workflow);
        }
    }

    private void addQuestionnaireAppraisal(AnalysisAttributeBranch performanceReviewBranch, QuestionnaireWorkflow workflow) {

        if (workflow.isQuestionnaireManager()) {
            addWorkflow(performanceReviewBranch, workflow, MANAGER_LABEL_POSTFIX);
        } else {
            addWorkflow(performanceReviewBranch, workflow, OTHERS_LABEL_POSTFIX);
        }
    }

    private void addWorkflow(AnalysisAttributeBranch root, QuestionnaireWorkflow workflow, String postfix) {

        String prefix = root.getPrefix();
        final Long workflowId = workflow.getId();
        final String prefixedQuestionnaireId = root.getId() + QUESTIONNAIRE_PREFIX + workflowId;
        String populationLabel = "";
        if (workflow.getPopulation() != null) {
            populationLabel = " (" + workflow.getPopulation().getLabel() + ")";
        }
        final String label = workflow.getLabel() + postfix + populationLabel;
        AnalysisAttributeBranch workflowBranch = new AnalysisAttributeBranch(prefixedQuestionnaireId, label, Node.QUESTIONNAIRE_TYPE, false, false, prefix);
        root.addChild(workflowBranch);
    }

    protected void addQuestions(AnalysisAttributeBranch root, final Long questionnaireId, final List questions, List roles) {

        if (com.zynap.talentstudio.util.collections.CollectionUtils.hasElements(questions)) {
            for (Iterator questionIterator = questions.iterator(); questionIterator.hasNext();) {
                final AbstractQuestion baseQuestion = (AbstractQuestion) questionIterator.next();
                if (baseQuestion.isMultiQuestion()) {
                    addMultiQuestion(root, questionnaireId, (MultiQuestionItem) baseQuestion, roles);
                } else if (!baseQuestion.isNarrativeType()) {
                    addQuestion(root, questionnaireId, (QuestionAttribute) baseQuestion, roles);
                }
            }
        }
    }

    protected void addMultiQuestion(AnalysisAttributeBranch root, Long questionnaireId, MultiQuestionItem multiQuestion, List roles) {

        final List questionItems = multiQuestion.getLineItems();
        final String prefix = root.getPrefix();
        if (com.zynap.talentstudio.util.collections.CollectionUtils.hasElements(questionItems)) {
            // loop through line items and get questions
            for (Iterator iterator = questionItems.iterator(); iterator.hasNext();) {
                LineItem questionLineItem = (LineItem) iterator.next();
                AnalysisAttributeBranch lineBranch = new AnalysisAttributeBranch(root.getId() + LINE_ITEM_PREFIX + questionLineItem.getId(), questionLineItem.getLabel(), prefix);
                root.addChild(lineBranch);
                final List questions = questionLineItem.getQuestions();
                addQuestions(lineBranch, questionnaireId, questions, roles);
            }
        }
    }

    protected void addQuestion(AnalysisAttributeBranch root, Long questionnaireId, QuestionAttribute question, List roles) {

        int index = roles != null ? roles.size() : 1;
        final String prefix = root.getPrefix();
        Long roleId = null;
        String roleName = null;
        for (int i = 0; i < index; i++) {
            if (roles != null) {
                final LookupValue role = (LookupValue) roles.get(i);
                roleId = role.getId();
                roleName = role.getLabel();
            }

            final String questionId = question.getDynamicAttribute().getId().toString();
            final String id = PopulationCriteriaBuilder.buildId(prefix, AnalysisAttributeHelper.buildQuestionCriteriaId(questionId, questionnaireId, roleId));
            final String label = roles != null ? question.getLabel() + PopulationCriteriaBuilder.ROLE_SEPARATOR + roleName : question.getLabel();
            Leaf quesLeaf = new Leaf(id, label);
            root.addLeaf(quesLeaf);
        }
    }

    private static final String LINE_ITEM_PREFIX = "_l_";
    static final String QUESTION_GROUP_PREFIX = "_G_";
    static final String QUESTIONNAIRE_PREFIX = "_q_";
    static final String APPRAISAL_WORKFLOW_PREFIX = "_ap_";
    private static final String APPRAISALS_LABEL = "Appraisals";
    private static final String QUESTIONNAIRES_LABEL = "Questionnaires";
    private static final String MANAGER_LABEL_POSTFIX = " ( Manager )";
    private static final String OTHERS_LABEL_POSTFIX = " ( Others )";
}