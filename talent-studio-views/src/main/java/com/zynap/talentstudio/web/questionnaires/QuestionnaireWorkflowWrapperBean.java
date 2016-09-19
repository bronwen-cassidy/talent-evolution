/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.questionnaires.DefinitionDTO;
import com.zynap.talentstudio.util.IDomainUtils;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class QuestionnaireWorkflowWrapperBean implements Serializable {

    public QuestionnaireWorkflowWrapperBean(QuestionnaireWorkflow questionnaire) {
        
        this.questionnaireWorkflow = questionnaire;
        this.questionnaireDefinition = questionnaire.getQuestionnaireDefinition();
        this.populationId = questionnaire.getPopulation() != null ? questionnaire.getPopulation().getId() : null;

        Group group = questionnaireWorkflow.getGroup();
        if(group == null) {
            group = new Group();
        }
        this.groupId = group.getId();        
    }

    public QuestionnaireDefinition getQuestionnaireDefinition() {        
        return questionnaireDefinition;
    }

    public String getLabel() {
        return questionnaireWorkflow.getLabel();
    }

    public void setLabel(String label) {
        this.questionnaireWorkflow.setLabel(label);
    }

    public void setStartDate(Date value) {
        questionnaireWorkflow.setStartDate(value);
    }

    public void setExpiryDate(Date value) {
        questionnaireWorkflow.setExpiryDate(value);
    }

    public Date getStartDate() {
        return questionnaireWorkflow.getStartDate();
    }

    public Date getExpiryDate() {
        return questionnaireWorkflow.getExpiryDate();
    }

    public String getStatus() {
        return questionnaireWorkflow.getStatus();
    }

    public void setStatus(String value) {
        questionnaireWorkflow.setStatus(value);
    }

    public Long getId() {
        return questionnaireWorkflow.getId();
    }

    public void setDescription(String description) {
        questionnaireWorkflow.setDescription(description);
    }

    public String getDescription() {
        return questionnaireWorkflow.getDescription();
    }

    public String getInfoForm() {
        return infoForm;
    }

    public void setInfoForm(String infoForm) {
        this.infoForm = infoForm;
    }

    public Long getPopulationId() {
        return populationId;
    }

    public void setPopulationId(Long populationId) {
        this.populationId = populationId;
    }

    public Collection getPopulations() {
        return populations;
    }

    public void setPopulations(Collection populations) {
        this.populations = populations;
    }

    public QuestionnaireWorkflow getModifiedQuestionnaireWorkflow() {

        final PopulationDto population = (PopulationDto) IDomainUtils.findDomainObject(populations, populationId);
        questionnaireWorkflow.setPopulation(new Population(population.getId()));
        questionnaireWorkflow.setHrUserId(hrUserId);
        assignGroup();
        return questionnaireWorkflow;
    }

    private void assignGroup() {
        if(StringUtils.hasText(groupLabel)) {
            questionnaireWorkflow.setGroup(new Group(null, groupLabel, Group.TYPE_QUESTIONNAIRE));
        } else if (groupId != null) {
            questionnaireWorkflow.setGroup(findGroup(groupId));
        } else {  // no label, no groupId
            questionnaireWorkflow.setGroup(null);
        }
    }

    public QuestionnaireWorkflow getUpdatedQuestionnaireWorkflow() {
        assignGroup();
        return questionnaireWorkflow;
    }

    private Group findGroup(Long groupId) {
        for(Group group : groups) {
            if(group.getId().equals(groupId)) {
                return group;
            }
        }
        return null;
    }


    public boolean useInfoForm() {
        return questionnaireWorkflow.isInfoForm();
    }

    public boolean isNotificationBased() {
        return questionnaireWorkflow.isNotificationBased();
    }

    public boolean isManagerWrite() {
        return questionnaireWorkflow.isManagerWrite();
    }

    public void setManagerWrite(boolean managerWrite) {
        questionnaireWorkflow.setManagerWrite(managerWrite);
    }

    public boolean isManagerRead() {
        return questionnaireWorkflow.isManagerRead();
    }

    public void setManagerRead(boolean managerRead) {
        questionnaireWorkflow.setManagerRead(managerRead);
    }


    public boolean isIndividualWrite() {
        return questionnaireWorkflow.isIndividualWrite();
    }

    public void setIndividualWrite(boolean individualWrite) {
        questionnaireWorkflow.setIndividualWrite(individualWrite);
    }

    public boolean isIndividualRead() {
        return questionnaireWorkflow.isIndividualRead();
    }

    public void setIndividualRead(boolean individualRead) {
        questionnaireWorkflow.setIndividualRead(individualRead);
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public boolean isComplete()                                                                     {
        return QuestionnaireWorkflow.STATUS_COMPLETED.equals(questionnaireWorkflow.getStatus());
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setDefinitions(List<DefinitionDTO> definitions) {
        this.definitions = definitions;
    }

    public List<DefinitionDTO> getDefinitions() {
        return definitions;
    }

    public Long getHrUserId() {
        return hrUserId;
    }

    public void setHrUserId(Long hrUserId) {
        this.hrUserId = hrUserId;
    }

    public String getHrUserLabel() {
        return hrUserLabel;
    }

    public void setHrUserLabel(String hrUserLabel) {
        this.hrUserLabel = hrUserLabel;
    }

    private String infoForm;
    private Long populationId;
    private final QuestionnaireWorkflow questionnaireWorkflow;
    private final QuestionnaireDefinition questionnaireDefinition;
    private Collection populations;
    private Long hrUserId;
    private String hrUserLabel;
    // TS-2299 
    private boolean sendEmail;
    private String groupLabel;
    private Long groupId;
    private List<Group> groups;
    private List<DefinitionDTO> definitions;
}
