package com.zynap.talentstudio.web.organisation;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 04-May-2005
 * Time: 09:51:01
 */

import com.zynap.domain.UserSession;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.arenas.Arena;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.util.collections.DomainObjectCollectionHelper;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class BrowseNodeController extends DefaultWizardFormController {

    /**
     * If back request this is not a form submission.
     */
    protected final boolean isFormSubmission(HttpServletRequest request) {

        final boolean historyBackRequest = HistoryHelper.isHistoryBackRequest(request);
        return !historyBackRequest && super.isFormSubmission(request);
    }

    /**
     * Get form backing object - will attempt to recover it from the UserSession object.
     * <br> If the form backing object was found in the UserSession, check to see if we are returning from a delete - if so,
     * remove the node that was deleted from the BrowseNodeWrapper command object.
     * <br> If it was being used in a search, check to see if the pageChange request parameter is present.
     * <br> - if it is it will check for a parameter starting with the DISPLAY_TAG_PREFIX and will set the page number on the command object to this.
     *
     * @param request current HTTP request
     * @return instance of {@link com.zynap.talentstudio.web.organisation.BrowseNodeWrapper}
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final UserSession userSession = UserSessionFactory.getUserSession();
        BrowseNodeWrapper browseNodeWrapper = recoverFormBackingObject(request);
        if (browseNodeWrapper != null) {

            final Long deletedNodeId = RequestUtils.getLongParameter(request, ControllerConstants.DELETED_NODE_ID);
            if (deletedNodeId != null) {
                browseNodeWrapper.removeNode(deletedNodeId);
                updateNodeInfo(browseNodeWrapper, userSession);
            }

            if (browseNodeWrapper.isSearching()) {

                setInitialPage(SEARCH_FORM);
                determineSearchResultsPageNumber(request, browseNodeWrapper);
            }
        }

        return browseNodeWrapper;
    }

    /**
     * Determine which search results page we are on.
     *
     * @param request           the servlet http request
     * @param browseNodeWrapper the facade for the node browse functionality
     */
    private void determineSearchResultsPageNumber(HttpServletRequest request, BrowseNodeWrapper browseNodeWrapper) {

        // if we already know the name of the page number parameter and there is a value for it in the request save the new value
        final Map.Entry pageNumberParameter = browseNodeWrapper.getPageNumberParameter();
        if (pageNumberParameter != null) {
            final String currentPageNumber = request.getParameter(pageNumberParameter.getKey().toString());
            if (StringUtils.hasText(currentPageNumber)) pageNumberParameter.setValue(currentPageNumber);
        }

        // if page change parameter is present, this is the first time we are doing this so save the parameter name and the parameter value
        else if (StringUtils.hasText(request.getParameter(ControllerConstants.PAGE_CHANGE))) {
            final Map displayTagParams = ZynapWebUtils.getParametersStartingWith(request, ControllerConstants.DISPLAY_TAG_PREFIX);
            if (!displayTagParams.isEmpty()) {
                final Map.Entry pageNumber = (Map.Entry) displayTagParams.entrySet().iterator().next();
                browseNodeWrapper.setPageNumberParameter(pageNumber);
            }
        }
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        Map<String, Object> refData = new HashMap<String, Object>();
        final Long userId = ZynapWebUtils.getUserId(request);

        refData.put(ParameterConstants.ALLOW_DELETE, getAllowDelete(request));
        ArtefactViewQuestionnaireHelper artefactViewQuestionnaireHelper = new ArtefactViewQuestionnaireHelper(getPopulationEngine());
        artefactViewQuestionnaireHelper.setPositionService(positionService);
        artefactViewQuestionnaireHelper.setSubjectService(subjectService);
        artefactViewQuestionnaireHelper.setUserId(userId);
        artefactViewQuestionnaireHelper.setDynamicAttributeService(dynamicAttributeService);
        refData.put("displayHelper", artefactViewQuestionnaireHelper);
        return refData;

    }

    protected void setDisplayInfo(BrowseNodeWrapper browseNodeWrapper, HttpServletRequest request) throws TalentStudioException {

        Report execSummaryReport = displayConfigService.findDisplayConfigReport(browseNodeWrapper.getNodeType(), DisplayConfig.EXECUTIVE_SUMMARY_TYPE);
        List viewConfigs = displayConfigService.findUserDisplayItems(browseNodeWrapper.getNodeType(), DisplayConfig.VIEW_TYPE, ZynapWebUtils.getUserId(request));

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        String currentArena = request.getParameter(ControllerConstants.DISPLAY_CONFIG_KEY);

        boolean exists = false;
        if (currentArena != null) {
            // find a matching arenaId in case we got something like REPORTS as the displayConfigKey
            // also verify that you have not manually entered this key and that you have access/permission for that arena
            Collection arenas = userSession.getArenas();
            for (Iterator iterator = arenas.iterator(); iterator.hasNext();) {
                Arena arena = (Arena) iterator.next();
                if (arena.getArenaId().equals(currentArena)) {
                    exists = true;
                    break;
                }
            }
        }
        if (!exists) currentArena = userSession.getCurrentArenaId();

        DisplayContentWrapper displayContentWrapper = new DisplayContentWrapper();
        displayContentWrapper.setExecutiveSummaryReport(execSummaryReport);
        displayContentWrapper.setViewDisplayConfigItems(viewConfigs, currentArena);
        browseNodeWrapper.setContentView(displayContentWrapper);
    }

    protected final ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    public final void setInitialPage(int i) {
        super.setInitialPage(i);
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        BrowseNodeWrapper wrapper = (BrowseNodeWrapper) command;
        final UserSession userSession = UserSessionFactory.getUserSession();

        Long nodeId = getNodeId(request);

        int target = getTargetPage(request, page);
        switch (target) {
            case COMBO_CHANGE:
                if (nodeId != null) {
                    wrapper.setNodeId(nodeId);
                    updateNodeInfo(wrapper, userSession);
                }
                break;
            case BUTTON_PRESS:
                wrapper.applyNextItemId();
                updateNodeInfo(wrapper, userSession);
                break;
            case SEARCH_FORM:
                wrapper.getFilter().clearAdvancedCriteriaIfNeeded();
                wrapper.resetPageNumber();
                doSearch(wrapper, request);
                // when searching we must clear the browse tab of any previous persons information
                wrapper.setNodeWrapper(null);
                wrapper.setActiveSearchTab(BrowseNodeWrapper._SEARCH_RESULTS_TAB);
                // if only one result show the information page
                final List currentNodes = wrapper.getCurrentNodes();
                if(currentNodes != null && currentNodes.size() == 1) {
                    Node currNode = (Node) currentNodes.get(0);
                    wrapper.setNodeId(currNode.getId());
                    updateNodeInfo(wrapper, userSession);
                    wrapper.setActiveSearchTab(BrowseNodeWrapper._SEARCH_INFO_TAB);
                }

                break;

            case SEARCH_DETAILS:
                if (nodeId != null) {
                    wrapper.setNodeId(nodeId);
                    updateNodeInfo(wrapper, userSession);
                    wrapper.setActiveSearchTab(BrowseNodeWrapper._SEARCH_INFO_TAB);
                }
                break;
            case SEARCH_BUTTON_PRESS:
                wrapper.applyNextItemId();
                updateNodeInfo(wrapper, userSession);
                wrapper.setActiveSearchTab(BrowseNodeWrapper._SEARCH_INFO_TAB);
                break;
        }
    }

    protected final void updateNodeInfo(BrowseNodeWrapper wrapper, UserSession userSession) throws TalentStudioException {
        Node node;
        try {
            Long id = wrapper.getNodeId();
            if (id == null) {
                
                NodeWrapperBean nodeWrapper = wrapper.getNodeWrapper();
                // nothing to do if the node wrapper is null
                if (nodeWrapper == null) {
                    return;
                }

                id = nodeWrapper.getId();
            }

            node = findNodeById(id);
            updateNodeInfo(node, wrapper, userSession);

        } catch (DomainObjectNotFoundException ex) {
            findNext(wrapper, userSession);
        }
    }

    private void findNext(BrowseNodeWrapper wrapper, UserSession userSession) throws TalentStudioException {
        Node node = findNextOrPreviousValidNode(wrapper);
        if (node != null) {
            wrapper.setNodeId(node.getId());
            updateNodeInfo(wrapper, userSession);
        } else {
            wrapper.setNodeWrapper(null);
        }
    }

    protected void updateNodeInfo(final Node currentNode, BrowseNodeWrapper wrapper, UserSession userSession) throws TalentStudioException {
        if (currentNode != null) {
            // given the fact that this node was returned in the list of artefacts we must have access so set flag to true
            // fetch a fresh current node
            currentNode.setHasAccess(true);
            checkNodeAssociationAccess(currentNode);

            final NodeWrapperBean nodeWrapperBean = createNodeWrapper(currentNode, wrapper);
            wrapper.setNodeWrapper(nodeWrapperBean);
            wrapper.updateCurrentNodes(nodeWrapperBean);

        } else
            wrapper.setNodeWrapper(null);
    }

    protected BrowseNodeWrapper recoverFormBackingObject(HttpServletRequest request) throws TalentStudioException {

        BrowseNodeWrapper wrapper = (BrowseNodeWrapper) HistoryHelper.recoverCommand(request, BrowseNodeWrapper.class);
        if (wrapper != null) {
            updateNodeInfo(wrapper, UserSessionFactory.getUserSession());
            return wrapper;
        }

        return null;
    }

    protected abstract void doSearch(BrowseNodeWrapper command, HttpServletRequest request) throws TalentStudioException;

    protected abstract Node findNodeById(Long id) throws TalentStudioException;

    protected abstract NodeWrapperBean createNodeWrapper(Node node, BrowseNodeWrapper wrapper);

    /**
     * Check access to artefacts linked to the current node by artefact associations.
     *
     * @param node The Node
     */
    protected abstract void checkNodeAssociationAccess(Node node);

    public List getAllowDeleteArenas() {
        return allowDeleteArenas;
    }

    public void setAllowDeleteArenas(List allowDeleteArenas) {
        this.allowDeleteArenas = allowDeleteArenas;
    }

    protected Boolean getAllowDelete(HttpServletRequest request) {
        return ZynapWebUtils.checkForArena(getAllowDeleteArenas(), request);
    }

    public IAnalysisService getAnalysisService() {
        return analysisService;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    private final Long getNodeId(HttpServletRequest request) {
        return RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);
    }

    private final Node findNextOrPreviousValidNode(final BrowseNodeWrapper wrapper) throws TalentStudioException {
        Node node = null;
        if (wrapper.getNodeId() == null) return null;
        final List currentNodes = wrapper.getCurrentNodes();
        if (currentNodes != null) {
            Node currentNode = (Node) DomainObjectCollectionHelper.findById(currentNodes, wrapper.getNodeId());
            int index = currentNodes.indexOf(currentNode);
            List<Node> toRemove = new ArrayList<Node>();
            if (index > -1) {
                toRemove.add(currentNode);
                for (int i = index + 1; i < currentNodes.size(); i++) {
                    try {
                        Long id = ((Node) currentNodes.get(i)).getId();
                        node = findNodeById(id);
                        break;
                    } catch (DomainObjectNotFoundException ex) {
                        toRemove.add((Node) currentNodes.get(i));
                    }
                }
                if (node == null) {
                    for (int i = index - 1; i >= 0; i--) {
                        try {
                            Long id = ((Node) currentNodes.get(i)).getId();
                            node = findNodeById(id);
                            break;
                        } catch (DomainObjectNotFoundException ex) {
                            toRemove.add((Node) currentNodes.get(i));
                        }
                    }
                }
            }
            currentNodes.removeAll(toRemove);
        }

        return node;
    }

    public IDynamicAttributeService getDynamicAttributeService() {
        return dynamicAttributeService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public ISubjectService getSubjectService() {
        return subjectService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public IPositionService getPositionService() {
        return positionService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public IPopulationEngine getPopulationEngine() {
        return populationEngine;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public IPortfolioService getPortfolioService() {
        return portfolioService;
    }

    public void setPortfolioService(IPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    public IDisplayConfigService getDisplayConfigService() {
        return displayConfigService;
    }

    private IDynamicAttributeService dynamicAttributeService;
    protected ISubjectService subjectService;
    protected IPositionService positionService;
    protected IPopulationEngine populationEngine;
    private IPortfolioService portfolioService;
    protected IDisplayConfigService displayConfigService;
    private IAnalysisService analysisService;
    private List allowDeleteArenas;

    protected static final int BROWSE_INITIAL_PAGE = 0;
    protected static final int COMBO_CHANGE = 1;
    protected static final int BUTTON_PRESS = 2;
    protected static final int SEARCH_FORM = 3;
    protected static final int SEARCH_DETAILS = 4;
    protected static final int SEARCH_BUTTON_PRESS = 5;
}
