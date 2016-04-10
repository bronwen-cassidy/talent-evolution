package com.zynap.talentstudio.web.utils.tree;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:51:01
 */

import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.ISecurityManager;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.web.organisation.subjects.SubjectSearchQueryWrapper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.collections.Transformer;

import org.springframework.validation.ValidationUtils;
import org.springframework.validation.BindException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public class SubjectPickerController extends PickerController {

    public void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    public ISecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(ISecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    //todo add a search func create a picker functionality

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        TreeWrapperBean treeWrapperBean = (TreeWrapperBean) super.formBackingObject(request);
        SubjectSearchQuery searchSubjectQuery = new SubjectSearchQuery();
        final SubjectSearchQueryWrapper queryWrapper = new SubjectSearchQueryWrapper(searchSubjectQuery);
        queryWrapper.setNodeType(Node.SUBJECT_UNIT_TYPE_);
        treeWrapperBean.setFilter(queryWrapper);
        return treeWrapperBean;
    }

    public void doSearch(TreeWrapperBean treeWrapperBean, HttpServletRequest request, BindException errors) throws TalentStudioException {

        SubjectSearchQueryWrapper queryWrapper = (SubjectSearchQueryWrapper) treeWrapperBean.getFilter();
        SubjectSearchQuery subjectSearchQuery = queryWrapper.getModifiedSubjectSearchQuery();


        String fieldFirstName = "filter.firstName";
        String fieldSecondName = "filter.secondName";

        Object firstNameValue = errors.getFieldValue(fieldFirstName);
        Object secondNameValue = errors.getFieldValue(fieldSecondName);
        if (firstNameValue == null && secondNameValue == null) {
            errors.rejectValue(fieldFirstName, "subject.search.required");
            errors.rejectValue(fieldSecondName, "subject.search.required");
            treeWrapperBean.setIsSearchRun(false);
            return;
        }
        treeWrapperBean.setIsSearchRun(true);
        final List subjects = getSubjectService().search(ZynapWebUtils.getUserId(request), subjectSearchQuery);
        treeWrapperBean.setResults(subjects);
    }


    protected void findBranchByLeafId(TreeWrapperBean treeWrapperBean, String leafId, HttpServletRequest request) throws TalentStudioException {
        OrganisationUnit orgunit = organisationUnitService.findOrgUnitBySubjectId(leafId);
        if (orgunit != null) {
            Branch branch = (Branch) treeWrapperBean.getBranches().get(orgunit.getId().toString());
            if (branch.getLeaves() == null || branch.getLeaves().size() == 0)
                TreeBuilderHelper.appendSubjectFromPositionLeaves(branch, orgunit.getPositions());
        } else {
            Branch branch = (Branch) treeWrapperBean.getBranches().get(ISecurityManager.ORPHANS_AREA_ID.toString());
            if (branch.getLeaves() == null || branch.getLeaves().size() == 0)
                updateBranch(branch, treeWrapperBean, request);
        }
    }

    protected void updateBranch(Branch branch, TreeWrapperBean treeWrapperBean, HttpServletRequest request) throws TalentStudioException {
        if (branch.getId().equals(ISecurityManager.ORPHANS_AREA_ID.toString())) {
            Area area = securityManager.findArea(ISecurityManager.ORPHANS_AREA_ID);
            TreeBuilderHelper.appendSubjectLeavesFromAreaElements(branch, area.getAssignedSubjects());
        } else {
            OrganisationUnit ou = organisationUnitService.findById(new Long(branch.getId()));
            TreeBuilderHelper.appendSubjectFromPositionLeaves(branch, ou.getPositions());
        }
    }

    protected TreeWrapperBean getTree(Map<String, Branch> branches, String popupId, HttpServletRequest request) throws TalentStudioException {
        List<OrganisationUnit> orgUnitTree;
        if (ZynapWebUtils.isMultiTenant(request)) {
            orgUnitTree = organisationUnitService.findValidParents(ZynapWebUtils.getUserId(request));
            org.apache.commons.collections.CollectionUtils.transform(orgUnitTree, new Transformer() {
                public Object transform(Object o) {
                    OrganisationUnit u = (OrganisationUnit) o;
                    u.setHasAccess(true);
                    return u;
                }
            });
        } else {
            orgUnitTree = organisationUnitService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID);
        }

        final List tree = TreeBuilderHelper.buildOrgUnitTree(orgUnitTree, branches);
        Branch orphansArea = new Branch();
        orphansArea.setId(ISecurityManager.ORPHANS_AREA_ID.toString());
        orphansArea.setLabel(securityManager.findArea(ISecurityManager.ORPHANS_AREA_ID).getLabel());
        tree.add(orphansArea);
        branches.put(orphansArea.getId(), orphansArea);
        return new TreeWrapperBean(tree, branches, popupId, getLeafIcon());
    }


    private IOrganisationUnitService organisationUnitService;
    private ISecurityManager securityManager;

}
