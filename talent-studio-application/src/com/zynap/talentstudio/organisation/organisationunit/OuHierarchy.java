package com.zynap.talentstudio.organisation.organisationunit;

import com.zynap.talentstudio.organisation.OrganisationUnit;

import java.io.Serializable;


/** @author Hibernate CodeGenerator */
public class OuHierarchy extends OuHierarchyBase implements Serializable {

    /** full constructor */
    public OuHierarchy(OuHierarchyPK comp_id, Integer hlevel, OrganisationUnit descendent, OrganisationUnit root) {
       super(comp_id,hlevel,descendent,root);
    }

    /** default constructor */
    public OuHierarchy() {
    }

    /** minimal constructor */
    public OuHierarchy(OuHierarchyPK comp_id) {
        super(comp_id);
    }

}
