package com.zynap.talentstudio.organisation.organisationunit;

import com.zynap.talentstudio.organisation.OrganisationUnit;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Aug-2005
 * Time: 10:01:06
 * To change this template use File | Settings | File Templates.
 */
public class OuHierarchyBase implements Serializable {
    /** full constructor */
    public OuHierarchyBase(OuHierarchyPK comp_id, Integer hlevel, OrganisationUnit descendent, OrganisationUnit root) {
        this.compositeId = comp_id;
        this.hlevel = hlevel;
        this.descendent = descendent;
        this.root = root;
    }

    /** default constructor */
    public OuHierarchyBase() {
    }

    /** minimal constructor */
    public OuHierarchyBase(OuHierarchyPK comp_id) {
        this.compositeId = comp_id;
    }



    public OuHierarchyPK getCompositeId() {
        return this.compositeId;
    }

    public void setCompositeId(OuHierarchyPK compositeId) {
        this.compositeId = compositeId;
    }

    public Integer getHlevel() {
        return this.hlevel;
    }

    public void setHlevel(Integer hlevel) {
        this.hlevel = hlevel;
    }

    public OrganisationUnit getDescendent() {
        return this.descendent;
    }

    public void setDescendent(OrganisationUnit OrganisationUnitById) {
        this.descendent = OrganisationUnitById;
    }

    public OrganisationUnit getRoot() {
        return this.root;
    }

    public void setRoot(OrganisationUnit OrganisationUnitByRootId) {
        this.root = OrganisationUnitByRootId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("compositeId", getCompositeId())
            .append("hlevel", getHlevel())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof OuHierarchyInc) ) return false;
        OuHierarchyInc castOther = (OuHierarchyInc) other;
        return new EqualsBuilder()
            .append(this.getCompositeId(), castOther.getCompositeId())
            .append(this.getHlevel(), castOther.getHlevel())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCompositeId())
            .append(getHlevel())
            .toHashCode();
    }

    /** identifier field */
    protected OuHierarchyPK compositeId;
    /** nullable persistent field */
    protected Integer hlevel;
    /** nullable persistent field */
    protected OrganisationUnit descendent;
    /** nullable persistent field */
    protected OrganisationUnit root;
}
