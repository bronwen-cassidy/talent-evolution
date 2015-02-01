package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.UserSession;
import com.zynap.domain.orgbuilder.ISearchConstants;
import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionDto;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.organisation.positions.PositionSearchQueryWrapper;
import com.zynap.talentstudio.web.organisation.positions.PositionWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.common.groups.Group;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 11:06:30
 */
public class BrowsePositionController extends BrowseNodeController {

    protected BrowseNodeWrapper recoverFormBackingObject(HttpServletRequest request) throws TalentStudioException {

            PositionBrowseNodeWrapper wrapper = (PositionBrowseNodeWrapper) HistoryHelper.recoverCommand(request, PositionBrowseNodeWrapper.class);
            if (wrapper != null) {
                updateNodeInfo(wrapper, UserSessionFactory.getUserSession());
                return wrapper;
            }
            return null;
        }


    /**
     * Get the backing object used by this wizard.
     *
     * @param request
     * @return instance of {@link com.zynap.talentstudio.web.organisation.BrowseNodeWrapper}
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        BrowseNodeWrapper browseNodeWrapper = (BrowseNodeWrapper) super.formBackingObject(request);
        UserSession userSession = ZynapWebUtils.getUserSession(request);
        if (browseNodeWrapper == null) {
            if (getInitialPage(request) == BROWSE_INITIAL_PAGE) {

                final PositionSearchQueryWrapper queryWrapper = createBrowseQueryWrapper(userSession);
                final PositionSearchQuery query = queryWrapper.getModifiedPositionSearchQuery();
                final Long userId = userSession.getId();

                final List<PositionDto> results = fetchPositions(query, userId);
                browseNodeWrapper = new PositionBrowseNodeWrapper(queryWrapper, results, null);
                browseNodeWrapper.setNodeType(Node.POSITION_UNIT_TYPE_);
                setDisplayInfo(browseNodeWrapper, request);

                // when browsing we always show the first result as their is no list of results page
                if (!results.isEmpty()) {
                    final PositionDto firstPositionDto = results.get(0);
                    Position firstPosition = positionService.findByID(firstPositionDto.getId());
                    updateNodeInfo(firstPosition, browseNodeWrapper, userSession);
                }
            } else {

                PositionSearchQueryWrapper queryWrapper = new PositionSearchQueryWrapper(new PositionSearchQuery());
                browseNodeWrapper = new PositionBrowseNodeWrapper(queryWrapper, null, null);
                browseNodeWrapper.setSearching(true);
                browseNodeWrapper.setNodeType(Node.POSITION_UNIT_TYPE_);
                setDisplayInfo(browseNodeWrapper, request);
            }
        } else {

            if (RequestUtils.getLongParameter(request, ControllerConstants.DELETED_NODE_ID) != null) {
                // check if a node has been deleted, need refresh the list or requery
                final Long userId = userSession.getId();
                PositionSearchQuery searchQuery;

                if (getInitialPage(request) == BROWSE_INITIAL_PAGE) {
                    final PositionSearchQueryWrapper queryWrapper = createBrowseQueryWrapper(userSession);
                    searchQuery = queryWrapper.getModifiedPositionSearchQuery();
                } else {
                    searchQuery = (PositionSearchQuery) browseNodeWrapper.getFilter().getNodeSearchQuery();
                }

                final List<PositionDto> results = fetchPositions(searchQuery, userId);
                browseNodeWrapper.setCurrentNodes(results);

                if (results.isEmpty()) browseNodeWrapper.setActiveSearchTab(BrowseNodeWrapper._SEARCH_RESULTS_TAB);

                else {
                    final int itemIndex = browseNodeWrapper.getCurrentItemIndex();
                    if (itemIndex > -1 && itemIndex < results.size()) {
                        Position firstPositionDto = results.get(itemIndex);
                        Position firstPosition = positionService.findByID(firstPositionDto.getId());
                        updateNodeInfo(firstPosition, browseNodeWrapper, userSession);
                    }
                }
            }
        }
        return browseNodeWrapper;
    }

    private PositionSearchQueryWrapper createBrowseQueryWrapper(UserSession userSession) {
        final Long organisationId = userSession.getNavigator().getOrganisationUnitId();
        final PositionSearchQueryWrapper queryWrapper = new PositionSearchQueryWrapper(organisationId);

        // set active to true as browse only returns active positions
        queryWrapper.setActive(ISearchConstants.ACTIVE);
        return queryWrapper;
    }

    private List<PositionDto> fetchPositions(PositionSearchQuery query, Long userId) throws TalentStudioException {
        return positionService.searchPositions(userId, query);
    }

    protected void doSearch(BrowseNodeWrapper command, HttpServletRequest request) throws TalentStudioException {
        final PositionSearchQueryWrapper queryWrapper = (PositionSearchQueryWrapper) command.getFilter();
        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        final Long userId = userSession.getId();
        final List<PositionDto> positions = fetchPositions(queryWrapper.getModifiedPositionSearchQuery(), userId);
        command.setCurrentNodes(positions);
    }

    protected Node findNodeById(Long id) throws TalentStudioException {
        return getPositionService().findByID(id);
    }

    protected NodeWrapperBean createNodeWrapper(Node node, BrowseNodeWrapper wrapper) {
        return new PositionWrapperBean((Position) node);
    }

    /**
     * Check access to artefacts linked to the current node by artefact associations.
     *
     * @param node The Node
     */
    protected void checkNodeAssociationAccess(Node node) {
        if (node != null) {
            positionService.checkAccess((Position) node);
        }
    }

    public Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map model = super.referenceData(request, command, errors, page);
        final Group userGroup = ZynapWebUtils.getUserGroup(request);
        model.put(ControllerConstants.POPULATIONS, getAnalysisService().findAllByGroup(IPopulationEngine.P_POS_TYPE_, ZynapWebUtils.getUserId(request), userGroup));
        model.put(POSITION_SERVICE_PARAM, positionService);
        return model;
    }

    private static final String POSITION_SERVICE_PARAM = "positionService";
}