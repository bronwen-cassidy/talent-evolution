package com.zynap.talentstudio.analysis;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 22-Mar-2005
 * Time: 08:37:56
 */
public class BaseDerivedAttribute implements Serializable {

    public BaseDerivedAttribute() {
    }

    public BaseDerivedAttribute(Long qualifierId, Long nodeId, Long associationNumber) {
        this.qualifierId = qualifierId;
        this.nodeId = nodeId;
        this.associationNumber = associationNumber;
    }

    public Long getQualifierId() {
        return this.qualifierId;
    }

    public void setQualifierId(Long qualifierId) {
        this.qualifierId = qualifierId;
    }

    public Long getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getAssociationNumber() {
        return this.associationNumber;
    }

    public void setAssociationNumber(Long associationNumber) {
        this.associationNumber = associationNumber;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("qualifierId", getQualifierId())
                .append("nodeId", getNodeId())
                .append("associationNumber", getAssociationNumber())
                .toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass() != this.getClass())) return false;

        final BaseDerivedAttribute baseDerivedAttribute = (BaseDerivedAttribute) o;

        if (nodeId != null ? !nodeId.equals(baseDerivedAttribute.nodeId) : baseDerivedAttribute.nodeId != null) return false;
        if (qualifierId != null ? !qualifierId.equals(baseDerivedAttribute.qualifierId) : baseDerivedAttribute.qualifierId != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (qualifierId != null ? qualifierId.hashCode() : 0);
        result = 29 * result + (nodeId != null ? nodeId.hashCode() : 0);
        return result;
    }


    /**
     * persistent field.
     */
    protected Long qualifierId;

    /**
     * persistent field.
     */
    protected Long nodeId;

    /**
     * persistent field.
     */
    protected Long associationNumber;
}
