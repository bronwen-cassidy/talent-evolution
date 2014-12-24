package com.zynap.talentstudio.questionnaires;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * @author Hibernate CodeGenerator
 */
public final class WorkflowParticipantPK implements Serializable {

    /**
     * identifier field.
     */
    private Long subjectId;

    /**
     * identifier field.
     */
    private Long workflowId;

    /**
     * default constructor.
     */
    public WorkflowParticipantPK() {
    }

    /**
     * full constructor.
     */
    public WorkflowParticipantPK(Long subjectId, Long workflowId) {
        this.subjectId = subjectId;
        this.workflowId = workflowId;
    }

    public Long getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getWorkflowId() {
        return this.workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("subjectId", getSubjectId())
                .append("workflowId", getWorkflowId())
                .toString();
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if (!(other instanceof WorkflowParticipantPK)) return false;
        WorkflowParticipantPK castOther = (WorkflowParticipantPK) other;
        return new EqualsBuilder()
                .append(this.getSubjectId(), castOther.getSubjectId())
                .append(this.getWorkflowId(), castOther.getWorkflowId())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getSubjectId())
                .append(getWorkflowId())
                .toHashCode();
    }
}
