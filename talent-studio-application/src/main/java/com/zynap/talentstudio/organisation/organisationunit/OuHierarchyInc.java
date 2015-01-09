package com.zynap.talentstudio.organisation.organisationunit;

import com.zynap.talentstudio.organisation.OrganisationUnit;


/** @author Hibernate CodeGenerator */
public class OuHierarchyInc extends OuHierarchyBase {

    /** full constructor */
    public OuHierarchyInc(OuHierarchyPK comp_id, Integer hlevel, OrganisationUnit descendent, OrganisationUnit root) {
       super(comp_id,hlevel,descendent,root);
    }

    /** default constructor */
    public OuHierarchyInc() {
    }

    /** minimal constructor */
    public OuHierarchyInc(OuHierarchyPK comp_id) {
        super(comp_id);
    }

}
