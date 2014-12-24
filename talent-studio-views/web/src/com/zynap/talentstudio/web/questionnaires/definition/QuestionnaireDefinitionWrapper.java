/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires.definition;

import com.zynap.talentstudio.questionnaires.DefinitionDTO;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireWrapper;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class QuestionnaireDefinitionWrapper extends QuestionnaireWrapper {

    public QuestionnaireDefinitionWrapper() {
        this.questionnaireDefinition = new QuestionnaireDefinition();
    }

    public QuestionnaireDefinitionWrapper(QuestionnaireDefinition questionnaireDefinition) {
        init(questionnaireDefinition);
    }

    private void init(QuestionnaireDefinition questionnaireDefinition) {
        this.questionnaireDefinition = questionnaireDefinition;
        filterQuestionnaires();
    }

    /**
     * Add only questionnaires that are not performance review instances.
     */
    private void filterQuestionnaires() {
        Set<QuestionnaireWorkflow> questionnaireWorkflows = this.questionnaireDefinition.getQuestionnaireWorkflows();
        this.questionnaires = new HashSet<QuestionnaireWorkflow>();
        if (questionnaireWorkflows != null) {
            for (QuestionnaireWorkflow questionnaireWorkflow : questionnaireWorkflows) {
                if (questionnaireWorkflow.getPerformanceReview() == null) {
                    questionnaires.add(questionnaireWorkflow);
                }
            }
        }
    }

    public void setDefinitionBytes(byte[] values) {
        this.definitionBytes = values;
    }

    public byte[] getDefinitionBytes() {
        if(definitionBytes != null && definitionBytes.length > 0) {
            return definitionBytes;
        }
        return null;
    }

    public String getLabel() {
        return questionnaireDefinition.getLabel();
    }

    public void setLabel(String label) {
        questionnaireDefinition.setLabel(label);
    }

    public String getTitle() {
        return questionnaireDefinition.getTitle();
    }

    public void setTitle(String title) {
        questionnaireDefinition.setTitle(title);
    }

    public Long getId() {
        return questionnaireDefinition.getId();
    }

    public String getDescription() {
        return questionnaireDefinition.getDescription();
    }

    public void setDescription(String description) {
        questionnaireDefinition.setDescription(description);
    }

    public Long getWorkflowId() {
        return null;
    }

    public Long getSubjectId() {
        return null;
    }

    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    public QuestionnaireDefinition getQuestionnaireDefinition() {
        return questionnaireDefinition;
    }

    public Collection<QuestionnaireWorkflow> getQuestionnaires() {
        return questionnaires;
    }

    /**
     * Sets scope, description and label on QuestionnaireDefinition.
     * <br/> Only sets description and label if they are not null or empty.
     *
     * @param questionnaireDefinition the questionnaireDefinition the bean wraps
     */
    public void setState(QuestionnaireDefinition questionnaireDefinition) {

        final String description = getDescription();
        if (StringUtils.hasText(description)) {
            questionnaireDefinition.setDescription(description);
        }

        final String label = getLabel();
        if (StringUtils.hasText(label)) {
            questionnaireDefinition.setLabel(label);
        }
    }

    public void setQuestionnaireDefinition(QuestionnaireDefinition questionnaireDefinition) {
        init(questionnaireDefinition);
    }

    public boolean isHasQuestionnaires() {
        Set questionnaireWorkflows = questionnaireDefinition.getQuestionnaireWorkflows();
        return questionnaireWorkflows != null && !questionnaireWorkflows.isEmpty();
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setQuestionnaireDefinitions(Collection<DefinitionDTO> definitions) {
        this.questionnaireDefinitions = definitions;
    }

    public Collection getQuestionnaireDefinitions() {
        return questionnaireDefinitions;
    }

    public boolean isCloneable() {
        return questionnaireDefinitions != null && !questionnaireDefinitions.isEmpty();
    }

    public Long getSelectedDefinitionId() {
        return selectedDefinitionId;
    }

    public void setSelectedDefinitionId(Long selectedDefinitionId) {
        this.selectedDefinitionId = selectedDefinitionId;
    }

    public boolean isToClone() {
        return getDefinitionBytes() == null && selectedDefinitionId != null;
    }

    public Map getDisplayTagValues() {
        return displayTagValues;
    }

    public void addDisplayTagValue(String key, Object value) {
        displayTagValues.put(key, value);
    }

    private byte[] definitionBytes;
    private Collection<QuestionnaireWorkflow> questionnaires;
    private QuestionnaireDefinition questionnaireDefinition;
    private Long selectedDefinitionId;

    private String activeTab;
    private boolean editing;
    private Collection<DefinitionDTO> questionnaireDefinitions;
    private Map<String, Object> displayTagValues = new HashMap<String, Object>();
}
