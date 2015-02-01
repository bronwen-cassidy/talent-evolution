/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.domain.admin.User;

import java.util.List;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 19-Jul-2006 13:40:15
 */
public class MergeOrgUnitWrapperBean extends OrganisationUnitWrapperBean {

    public MergeOrgUnitWrapperBean(OrganisationUnit one, OrganisationUnit two) {
        super(one);
        defunctOrganisationUnit = two;
        label = organisationUnit != null ? organisationUnit.getLabel() : null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public OrganisationUnit getMergedOrganisationUnit(User user) {
        OrganisationUnit unit = getModifiedOrganisationUnit(user);
        unit.setLabel(label);
        return unit;
    }

    public OrganisationUnit getDefunctOrganisationUnit() {
        return defunctOrganisationUnit;
    }

    /**
     * @return the list of the two organisation units that the merge is to use.
     */
    public List getMergedFrom() {
        List result = new ArrayList();
        if(organisationUnit != null) result.add(organisationUnit);
        if(defunctOrganisationUnit != null) result.add(defunctOrganisationUnit);
        return result;
    }

    private OrganisationUnit defunctOrganisationUnit;
    private String label;
}
