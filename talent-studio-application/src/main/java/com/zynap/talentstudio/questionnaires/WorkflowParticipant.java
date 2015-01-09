package com.zynap.talentstudio.questionnaires;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * @author Hibernate CodeGenerator
 */
public final class WorkflowParticipant implements Serializable {

    /**
     * identifier field.
     */
    private WorkflowParticipantPK primaryKey;

    /**
     * default constructor.
     */
    public WorkflowParticipant() {
    }

    /**
     * Full constructor.
     * @param subjectId
     * @param workflowId
     */
    public WorkflowParticipant(Long subjectId, Long workflowId) {
        setPrimaryKey(new WorkflowParticipantPK(subjectId, workflowId));
    }

    public WorkflowParticipantPK getPrimaryKey() {
        return this.primaryKey;
    }

    public void setPrimaryKey(WorkflowParticipantPK primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("primaryKey", getPrimaryKey())
                .toString();
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if (!(other instanceof WorkflowParticipant)) return false;
        WorkflowParticipant castOther = (WorkflowParticipant) other;
        return new EqualsBuilder()
                .append(this.getPrimaryKey(), castOther.getPrimaryKey())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getPrimaryKey())
                .toHashCode();
    }
}
