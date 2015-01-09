package com.zynap.talentstudio.organisation.organisationunit;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/** @author Hibernate CodeGenerator */
public class OuHierarchyPK implements Serializable {

    /** identifier field */
    private Long rootId;

    /** identifier field */
    private Long id;

    /** full constructor */
    public OuHierarchyPK(Long rootId, Long id) {
        this.rootId = rootId;
        this.id = id;
    }

    /** default constructor */
    public OuHierarchyPK() {
    }

    public Long getRootId() {
        return this.rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("rootId", getRootId())
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof OuHierarchyPK) ) return false;
        OuHierarchyPK castOther = (OuHierarchyPK) other;
        return new EqualsBuilder()
            .append(this.getRootId(), castOther.getRootId())
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRootId())
            .append(getId())
            .toHashCode();
    }

}
