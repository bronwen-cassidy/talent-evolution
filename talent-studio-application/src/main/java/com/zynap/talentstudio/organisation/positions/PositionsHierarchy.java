package com.zynap.talentstudio.organisation.positions;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/** 
 *        @hibernate.class
 *         table="POSITIONS_HIERARCHY"
 *         dynamic-update="true"
 *         dynamic-insert="true"
 *     
*/
public class PositionsHierarchy implements Serializable {

    /** identifier field */
    private PositionsHierarchyPK comp_id;

    /** nullable persistent field */
    private Integer hlevel;

    /** nullable persistent field */
    private Position descendent;

    /** nullable persistent field */
    private Position root;

    /** full constructor */
    public PositionsHierarchy(PositionsHierarchyPK comp_id, Integer hlevel,  Position positionById, Position positionByRootId) {        this.comp_id = comp_id;
        this.hlevel = hlevel;
        this.descendent = positionById;
        this.root = positionByRootId;
    }

    /** default constructor */
    public PositionsHierarchy() {
    }

    /** minimal constructor */
    public PositionsHierarchy(PositionsHierarchyPK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *         
     */
    public PositionsHierarchyPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(PositionsHierarchyPK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.property
     *             column="HLEVEL"
     *             length="5"
     *         
     */
    public Integer getHlevel() {
        return this.hlevel;
    }

    public void setHlevel(Integer hlevel) {
        this.hlevel = hlevel;
    }

    /**
     *            @hibernate.many-to-one
     *             update="false"
     *             insert="false"
     *         
     *            @hibernate.column
     *             name="ID"
     *         
     */
    public Position getDescendent() {
        return this.descendent;
    }

    public void setDescendent(Position descendent) {
        this.descendent = descendent;
    }

    /** 
     *            @hibernate.many-to-one
     *             update="false"
     *             insert="false"
     *         
     *            @hibernate.column
     *             name="ROOT_ID"
     *         
     */
    public Position getRoot() {
        return this.root;
    }

    public void setRoot(Position root) {
        this.root = root;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .append("hlevel", getHlevel())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof PositionsHierarchy) ) return false;
        PositionsHierarchy castOther = (PositionsHierarchy) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .append(this.getHlevel(), castOther.getHlevel())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .append(getHlevel())
            .toHashCode();
    }

}
