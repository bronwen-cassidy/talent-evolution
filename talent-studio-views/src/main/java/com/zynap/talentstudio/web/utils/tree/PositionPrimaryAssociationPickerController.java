package com.zynap.talentstudio.web.utils.tree;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:51:01
 */

import com.zynap.domain.UserSession;
import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.navigation.ZynapNavigator;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.web.organisation.positions.PositionSearchQueryWrapper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.collections.Transformer;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionPrimaryAssociationPickerController extends PickerController {

    /**
     * Overriden as needs to set source position id on TreeWrapperBean.
     *
     * @param request
     * @return TreeWrapperBean
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final String leafId = request.getParameter(POPUP_INITIAL_LEAF_PARAM);
        final String popupId = request.getParameter(POPUP_ID_PARAM);

        TreeWrapperBean wrapper = (TreeWrapperBean) ZynapWebUtils.getCommand(getClass().getName(), request);

        // if not found or popup id has changed, create new TreeWrapper
        if (wrapper == null || !popupId.equals(wrapper.getPopupId())) {
            Map branches = new HashMap();
            wrapper = getTree(branches, popupId, request);
        }

        // get the source position id - needed to get the available target positions which must exclude the source position and its children
        final String positionId = request.getParameter(POPUP_SOURCE_POSITION);
        wrapper.setPositionId(positionId);

        // find branch if necessary - IMPORTANT: needs to be called after source position id is set!!
        if (StringUtils.hasText(leafId)) {
            findBranchByLeafId(wrapper, leafId, request);
        }

        PositionSearchQuery searchPositionQuery = new PositionSearchQuery();
        final PositionSearchQueryWrapper queryWrapper = new PositionSearchQueryWrapper(searchPositionQuery);
        queryWrapper.setNodeType(Node.POSITION_UNIT_TYPE_);
        wrapper.setFilter(queryWrapper);
        return wrapper;
    }


    protected void findBranchByLeafId(TreeWrapperBean treeWrapperBean, String leafId, HttpServletRequest request) throws TalentStudioException {
        final OrganisationUnit orgunit = organisationUnitService.findOrgUnitByPositionId(leafId);
        final Long orgUnitId = orgunit.getId();

        final Branch branch = (Branch) treeWrapperBean.getBranches().get(orgUnitId.toString());
        if (branch != null && (branch.getLeaves() == null || branch.getLeaves().size() == 0)) {
            updateBranch(branch, treeWrapperBean, request);
        }
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
        final List subjects = getPositionService().search(ZynapWebUtils.getUserId(request), positionSearchQuery);
        treeWrapperBean.setResults(subjects);
    }

    protected void updateBranch(Branch branch, TreeWrapperBean treeWrapperBean, HttpServletRequest request) throws TalentStudioException {
        final Long orgUnitId = new Long(branch.getId());
        final Long userId = ZynapWebUtils.getUserId(request);
        Long positionId = treeWrapperBean.getPositionId();
        List<Position> positions = getPositionService().findAvailableParentsForPosition(orgUnitId, positionId, userId);
        if(ZynapWebUtils.isMultiTenant(request) && orgUnitId.equals(ZynapWebUtils.getUserOrgUnitId(request))) {
            //add in the users position
            Position p = getPositionService().findUsersPosition(userId, orgUnitId);
            if(p != null && !p.getId().equals(positionId)) {
                positions.add(p);
            }
        }
        TreeBuilderHelper.appendPositionLeaves(branch, positions, displayAssociation);
    }

    public void setOrganisationUnitService(IOrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    protected TreeWrapperBean getTree(Map<String, Branch> branches, String popupId, HttpServletRequest request) throws TalentStudioException {
        List<OrganisationUnit> hierarchy;
        if (ZynapWebUtils.isMultiTenant(request)) {
            hierarchy = organisationUnitService.findValidParents(ZynapWebUtils.getUserId(request));
            // add in the users org unit
            OrganisationUnit orgUnit = organisationUnitService.findOrgUnitByUser(ZynapWebUtils.getUserId(request).toString());
            if(!hierarchy.contains(orgUnit)) hierarchy.add(0, orgUnit);

            org.apache.commons.collections.CollectionUtils.transform(hierarchy, new Transformer() {
                public Object transform(Object o) {
                    OrganisationUnit u = (OrganisationUnit) o;
                    u.setHasAccess(true);
                    return u;
                }
            });
        } else {
            hierarchy = organisationUnitService.findOrgUnitTree(OrganisationUnit.ROOT_ORG_UNIT_ID);
        }

        return new TreeWrapperBean(TreeBuilderHelper.buildOrgUnitTree(hierarchy, branches), branches, popupId, getLeafIcon());
    }

    public void setDisplayAssociation(boolean displayAssociation) {
        this.displayAssociation = displayAssociation;
    }


    private IOrganisationUnitService organisationUnitService;
    protected boolean displayAssociation;

    public static final String POPUP_SOURCE_POSITION = "sourcePositionId";
}
