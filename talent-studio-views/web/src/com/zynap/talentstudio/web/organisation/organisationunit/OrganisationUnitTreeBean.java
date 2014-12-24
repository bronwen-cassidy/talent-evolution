/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.organisationunit;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.web.utils.tree.TreeBuilderHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author acalderwood
 * @version 0.1
 * @since 11-Jan-2007 13:08:47
 */

public class OrganisationUnitTreeBean {

    public List getOrganisationUnits() {
        try {
            return TreeBuilderHelper.buildOrgUnitTree(
                    organisationManager.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID));
        } catch (TalentStudioException e) {
            return new ArrayList();
        }
    }

    public List getOrganisationUnitsExcludingBranch(long branchId) {
        Long orgUnitIdAsLong = new Long(branchId);
        try {
            OrganisationUnit searchOrgUnit = organisationManager.findByID(orgUnitIdAsLong);
            List orgUnits = organisationManager.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID);
            List orgUnitsExcludingBranch = removeDescendents(orgUnits, searchOrgUnit);
            return TreeBuilderHelper.buildOrgUnitTree(orgUnitsExcludingBranch);
        } catch (TalentStudioException e) {
            return new ArrayList();
        }
    }

    private List removeDescendents(List list, OrganisationUnit currentNode) {
        list.remove(currentNode);
        for (Iterator it = currentNode.getChildren().iterator(); it.hasNext();) {
            OrganisationUnit child = (OrganisationUnit)it.next();
            removeDescendents(list, child);
        }
        return list;
    }

    public void setOrganisationManager(IOrganisationUnitService organisationManager) {
        this.organisationManager = organisationManager;
    }

    public IOrganisationUnitService getOrganisationManager() {
        return organisationManager;
    }

    private IOrganisationUnitService organisationManager;
    private IPositionService positionManager;
}
