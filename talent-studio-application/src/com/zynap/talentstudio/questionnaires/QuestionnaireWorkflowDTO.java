/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 12-Nov-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Nov-2007 12:06:38
 */
public class QuestionnaireWorkflowDTO {

    public QuestionnaireWorkflowDTO(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public QuestionnaireWorkflowDTO(Long id, String label, String workflowType, String groupLabel, Date startDate,
                                    Date lastRepublishedDate, Date expiryDate, String status, String definitionLabel,
                                    Long definitionId, String populationLabel) {
        this(id, label);
        this.workflowType = workflowType;
        this.groupLabel = groupLabel;
        this.expiryDate = expiryDate;
        this.startDate = startDate;
        this.status = status;
        this.definitionLabel = definitionLabel;
        this.definitionId = definitionId;
        population = populationLabel;
        this.lastRepublishedDate=lastRepublishedDate;
    }

    public String getPopulation() {
        return population;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDefinitionLabel() {
        return definitionLabel;
    }

    public Long getDefinitionId() {
        return definitionId;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public boolean isInfoForm() {
        return QuestionnaireWorkflow.TYPE_INFO_FORM.equals(workflowType);
    }

    public boolean isCompleted() {
        return QuestionnaireWorkflow.STATUS_COMPLETED.equals(status);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionnaireWorkflowDTO)) return false;

        QuestionnaireWorkflowDTO that = (QuestionnaireWorkflowDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }

    public Date getLastRepublishedDate() {
        return lastRepublishedDate;
    }

    public void setLastRepublishedDate(Date lastRepublishedDate) {
        this.lastRepublishedDate = lastRepublishedDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    private Long id;
    private String label;
    private String groupLabel;
    private Date expiryDate;
    private String status;
    private String definitionLabel;
    private Long definitionId;
    private String population;
    private String workflowType;
    private Date lastRepublishedDate;
    private Date startDate;
}
