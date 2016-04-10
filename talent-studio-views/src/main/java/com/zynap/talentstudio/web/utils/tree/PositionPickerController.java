package com.zynap.talentstudio.web.utils.tree;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:51:01
 */

import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.web.organisation.positions.PositionSearchQueryWrapper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.collections.Transformer;

import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PositionPickerController extends PickerController {


    public void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }


    protected void findBranchByLeafId(TreeWrapperBean treeWrapperBean, String leafId, HttpServletRequest request) throws TalentStudioException {
        OrganisationUnit orgunit = organisationUnitService.findOrgUnitByPositionId(leafId);
        Branch branch = (Branch) treeWrapperBean.getBranches().get(orgunit.getId().toString());
        if (branch.getLeaves() == null || branch.getLeaves().size() == 0) {
            Set<Position> positions = orgunit.getPositions();
            TreeBuilderHelper.appendPositionLeaves(branch, positions, displayAssociation);
        }
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        TreeWrapperBean treeWrapperBean = (TreeWrapperBean) super.formBackingObject(request);

        PositionSearchQuery searchPositionQuery = new PositionSearchQuery();
        final PositionSearchQueryWrapper queryWrapper = new PositionSearchQueryWrapper(searchPositionQuery);
        queryWrapper.setNodeType(Node.POSITION_UNIT_TYPE_);
        treeWrapperBean.setFilter(queryWrapper);
        return treeWrapperBean;
    }

    protected void updateBranch(Branch branch, TreeWrapperBean treeWrapperBean, HttpServletRequest request) throws TalentStudioException {
        OrganisationUnit ou = organisationUnitService.findById(new Long(branch.getId()));
        TreeBuilderHelper.appendPositionLeaves(branch, ou.getPositions(), displayAssociation);
    }

    protected TreeWrapperBean getTree(Map<String, Branch> branches, String popupId, HttpServletRequest request) throws TalentStudioException {
        List<OrganisationUnit> units;
        if (ZynapWebUtils.isMultiTenant(request)) {
            units = organisationUnitService.findValidParents(ZynapWebUtils.getUserId(request));
            OrganisationUnit unit = organisationUnitService.findOrgUnitByUser(ZynapWebUtils.getUserId(request).toString());
            if(!units.contains(unit)) units.add(unit);

            org.apache.commons.collections.CollectionUtils.transform(units, new Transformer() {
                public Object transform(Object o) {
                    OrganisationUnit u = (OrganisationUnit) o;
                    u.setHasAccess(true);
                    return u;
                }
            });

        } else {
            units = organisationUnitService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID);
        }
        return new TreeWrapperBean(TreeBuilderHelper.buildOrgUnitTree(units, branches), branches, popupId, getLeafIcon());
    }

    public void setDisplayAssociation(boolean displayAssociation) {
        this.displayAssociation = displayAssociation;
    }

    public void doSearch(TreeWrapperBean treeWrapperBean, HttpServletRequest request, BindException errors) throws TalentStudioException {
        PositionSearchQueryWrapper queryWrapper = (PositionSearchQueryWrapper) treeWrapperBean.getFilter();
        PositionSearchQuery positionSearchQuery = queryWrapper.getModifiedPositionSearchQuery();

        String fieldTitle = "filter.title";

        Object titleValue = errors.getFieldValue(fieldTitle);
        if (titleValue == null) {
            errors.rejectValue(fieldTitle, "position.search.required");
            treeWrapperBean.setIsSearchRun(false);
            return;
        }

        treeWrapperBean.setIsSearchRun(true);
        final List positions = getPositionService().search(ZynapWebUtils.getUserId(request), positionSearchQuery);
        treeWrapperBean.setResults(positions);
    }


    protected IOrganisationUnitService organisationUnitService;
    protected boolean displayAssociation;

}
