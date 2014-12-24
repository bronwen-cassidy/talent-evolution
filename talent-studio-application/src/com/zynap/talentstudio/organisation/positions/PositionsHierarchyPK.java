package com.zynap.talentstudio.organisation.positions;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/** @author Hibernate CodeGenerator */
public class PositionsHierarchyPK implements Serializable {

    /** identifier field */
    private Long rootId;

    /** identifier field */
    private Long id;

    /** full constructor */
    public PositionsHierarchyPK(Long rootId, Long id) {
        this.rootId = rootId;
        this.id = id;
    }

    /** default constructor */
    public PositionsHierarchyPK() {
    }

    /** 
     *                @hibernate.property
     *                 column="ROOT_ID"
     *                 length="22"
     *             
     */
    public Long getRootId() {
        return this.rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    /** 
     *                @hibernate.property
     *                 column="ID"
     *                 length="22"
     *             
     */
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
        if ( !(other instanceof PositionsHierarchyPK) ) return false;
        PositionsHierarchyPK castOther = (PositionsHierarchyPK) other;
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
