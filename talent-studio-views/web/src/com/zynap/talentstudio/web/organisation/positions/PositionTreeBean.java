/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.web.organisation.organisationunit.OrganisationUnitTreeBean;
import com.zynap.talentstudio.web.utils.tree.TreeBuilderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author acalderwood
 * @version 0.1
 * @since 11-Jan-2007 13:08:47
 */

public class PositionTreeBean extends OrganisationUnitTreeBean {

    public List getPositions(long organisationUnitId) {
        Long orgUnitIdAsLong = new Long(organisationUnitId);
        try {
            OrganisationUnit searchOrgUnit = organisationManager.findByID(orgUnitIdAsLong);
            List orgUnitList = new ArrayList();
            orgUnitList.add(searchOrgUnit);
            return TreeBuilderHelper.buildPositionsTree(orgUnitList);
        } catch (TalentStudioException e) {
            return new ArrayList();
        }
    }

    public void setOrganisationManager(IOrganisationUnitService organisationManager) {
        this.organisationManager = organisationManager;
    }

    public IOrganisationUnitService getOrganisationManager() {
        return organisationManager;
    }

    private IOrganisationUnitService organisationManager;
}
