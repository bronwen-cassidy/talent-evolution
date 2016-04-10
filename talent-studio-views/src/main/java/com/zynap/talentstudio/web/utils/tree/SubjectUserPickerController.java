package com.zynap.talentstudio.web.utils.tree;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:51:01
 */

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.security.areas.Area;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.List;

public class SubjectUserPickerController extends PickerController {

    public void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    protected void findBranchByLeafId(TreeWrapperBean treeWrapperBean, String leafId, HttpServletRequest request) throws TalentStudioException {
        
        OrganisationUnit orgunit = organisationUnitService.findOrgUnitBySubjectUserId(leafId);
        if (orgunit != null) {
            Branch branch = (Branch) treeWrapperBean.getBranches().get(orgunit.getId().toString());
            if (branch.getLeaves() == null || branch.getLeaves().size() == 0)
                TreeBuilderHelper.appendSubjectUserFromPositionLeaves(branch, orgunit.getPositions());
        } else {
            Branch branch = (Branch) treeWrapperBean.getBranches().get(ISecurityManager.ORPHANS_AREA_ID.toString());
            if (branch.getLeaves() == null || branch.getLeaves().size() == 0)
                updateBranch(branch, treeWrapperBean, request);
        }

    }

    protected void updateBranch(Branch branch, TreeWrapperBean treeWrapperBean, HttpServletRequest request) throws TalentStudioException {
        if (branch.getId().equals(ISecurityManager.ORPHANS_AREA_ID.toString())) {
            Area area = securityManager.findArea(ISecurityManager.ORPHANS_AREA_ID);
            TreeBuilderHelper.appendSubjectUserLeavesFromAreaElements(branch, area.getAssignedSubjects());
        } else if (branch.getId().equals(USERS_BRANCH_ID)) {
            TreeBuilderHelper.appendUserLeaves(branch, userService.findSystemUsers());
        } else {
            OrganisationUnit ou = organisationUnitService.findById(new Long(branch.getId()));
            TreeBuilderHelper.appendSubjectUserFromPositionLeaves(branch, ou.getPositions());
        }
    }

    protected TreeWrapperBean getTree(Map<String, Branch> branches, String popupId, HttpServletRequest request) throws TalentStudioException {
        final List tree = TreeBuilderHelper.buildOrgUnitTree(organisationUnitService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID), branches);
        // add a user with no subjects branch
        Branch usersBranch = new Branch(USERS_BRANCH_ID, "Users");
        tree.add(usersBranch);
        branches.put(usersBranch.getId(), usersBranch);
        //TreeBuilderHelper.appendUserLeaves(usersBranch, userService.findSystemUsers());

        // orphaned subject users
        Branch orphansArea = new Branch(ISecurityManager.ORPHANS_AREA_ID.toString(), securityManager.findArea(ISecurityManager.ORPHANS_AREA_ID).getLabel());
        tree.add(orphansArea);
        branches.put(orphansArea.getId(), orphansArea);

        // return the wrapper
        return new TreeWrapperBean(tree, branches, popupId, getLeafIcon());
    }

    public boolean isDisplayAssociation() {
        return displayAssociation;
    }

    public void setDisplayAssociation(boolean displayAssociation) {
        this.displayAssociation = displayAssociation;
    }

    public ISecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    private IOrganisationUnitService organisationUnitService;
    private ISecurityManager securityManager;
    private IUserService userService;

    private boolean displayAssociation;

    private static final String USERS_BRANCH_ID = "USERS";
}
