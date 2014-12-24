/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.common.util.MapUtil;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;
import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.search.ExternalSearchException;
import com.zynap.talentstudio.organisation.portfolio.search.IField;
import com.zynap.talentstudio.organisation.portfolio.search.ISearchManager;
import com.zynap.talentstudio.organisation.portfolio.search.SearchResult;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.organisation.BrowseNodeWrapper;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Controller that handles portfolio document searches.
 *
 * @author amark
 */
public class DocumentSearchController extends DefaultWizardFormController {

    public final void setDataSources(Map dataSources) {
        this.dataSources = dataSources;
    }

    public final void setSearchManager(ISearchManager searchManager) {
        this.searchManager = searchManager;
    }

    public final void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public final void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public final void setPortfolioService(IPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    protected final Object formBackingObject(HttpServletRequest request) throws Exception {

        DocumentSearchWrapper documentSearchWrapper = (DocumentSearchWrapper) HistoryHelper.recoverCommand(request, DocumentSearchWrapper.class);
        if (documentSearchWrapper == null) {
            documentSearchWrapper = getNewFormBackingObject(request);
        }

        return documentSearchWrapper;
    }

    protected DocumentSearchWrapper getNewFormBackingObject(HttpServletRequest request) throws Exception {

        DocumentSearchWrapper documentSearchWrapper;
        documentSearchWrapper = new DocumentSearchWrapper();
        documentSearchWrapper.setDataSources(dataSources);
        Collection contentTypes = portfolioService.getAllContentTypes(SecurityConstants.VIEW_ACTION);
        documentSearchWrapper.setContentTypes(contentTypes);
        documentSearchWrapper.setSearchQuery(new DocumentSearchQuery());
        documentSearchWrapper.setDisplayDataSources(true);
        return documentSearchWrapper;
    }

    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

        DocumentSearchWrapper wrapper = (DocumentSearchWrapper) command;
        final IField[] iFields = getSelectedContentTypes(wrapper);

        switch (page) {

            case SEARCH_INDEX:
                handleSearch(wrapper, iFields, errors);
                break;

            case DATASOURCE_SELECTED:
                handleSelectedDataSource(wrapper);
                break;

            case BUTTON_PRESS:
                final Long nextItemId = wrapper.getNextItemId();
                if (nextItemId != null) findPortfolioItem(nextItemId, wrapper);
                break;

            case BROWSE_INDEX:
                Long id = RequestUtils.getLongParameter(request, ParameterConstants.ITEM_ID, null);
                handleBrowse(id, wrapper);
                break;

            case SEARCH_AGAIN_INDEX:
                int docId = RequestUtils.getRequiredIntParameter(request, DOC_ID);
                try {
                    SearchResult similarResults = (SearchResult) searchManager.search(docId, iFields, wrapper.getSearchQuery().getSources());
                    setResults(similarResults.getHits(), wrapper);
                    wrapper.setActiveTab(BrowseNodeWrapper._SEARCH_RESULTS_TAB);
                } catch (ExternalSearchException e) {
                    wrapper.clearResults();
                    errors.reject(e.getKey(), e.getMessage());
                }
                break;
        }

        return null;
    }

    private void handleBrowse(Long id, DocumentSearchWrapper wrapper) {
        if (id != null) {
            findPortfolioItem(id, wrapper);
            wrapper.setNodeId(id);
            wrapper.setActiveTab(BrowseNodeWrapper._SEARCH_INFO_TAB);
        }
    }

    private void handleSelectedDataSource(DocumentSearchWrapper wrapper) {
        String datasource = wrapper.getSelectedSources();
        if (datasource.indexOf('+') != -1) {
            wrapper.setContentTypes(portfolioService.getAllContentTypes(SecurityConstants.VIEW_ACTION));
        } else {
            // subject or position
            String sourceId = (String) MapUtil.getKeys(dataSources, wrapper.getSelectedSources())[0];
            wrapper.setContentTypes(portfolioService.getContentTypes(sourceId, SecurityConstants.VIEW_ACTION));
        }
        wrapper.setActiveTab(DocumentSearchWrapper.SEARCH_CRITERIA_TAB);
    }

    private void handleSearch(DocumentSearchWrapper wrapper, IField[] iFields, Errors errors) {
        // clear results only if doing normal search and there are errors (errors imply validation errors)
        if (errors.hasErrors()) {
            wrapper.clearResults();
        } else {
            try {
                SearchResult result = (SearchResult) searchManager.search(wrapper.getSearchQuery(), iFields);
                setResults(result.getHits(), wrapper);
                wrapper.setActiveTab(BrowseNodeWrapper._SEARCH_RESULTS_TAB);
            } catch (ExternalSearchException e) {
                wrapper.clearResults();
                wrapper.setActiveTab(DocumentSearchWrapper.SEARCH_CRITERIA_TAB);
                errors.reject(e.getKey(), e.getMessage());
            }
        }
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        DocumentSearchWrapper wrapper = (DocumentSearchWrapper) command;

        int target = getTargetPage(request, page);
        switch (target) {

            case FORM_INDEX:
                // if sub search selected check that at least 1 item has been selected - if not force back to search results tab
                if (isSubSearch(request)) {
                    if (!StringUtils.hasText(request.getParameter("selectedResults"))) {
                        errors.reject("documentsearch.subsearch.requirements", "You must select at least one item for the sub search.");
                        wrapper.setActiveTab(BrowseNodeWrapper._SEARCH_RESULTS_TAB);
                    } else {
                        wrapper.setActiveTab(DocumentSearchWrapper.SEARCH_CRITERIA_TAB);
                    }
                }

                break;

            case SEARCH_INDEX:

                // clear selected content types
                if (!StringUtils.hasText(request.getParameter("selectedContent"))) {
                    wrapper.setSelectedContent(new String[0]);
                }
                // clear selected portfolio items
                if (!StringUtils.hasText(request.getParameter("selectedItems"))) {
                    wrapper.setSelectedItems(new Long[0]);
                }

                DocumentSearchValidator validator = (DocumentSearchValidator) getValidator();
                validator.validate(wrapper, errors);

                // redirect to criteria tab if errors present
                if (errors.hasErrors()) {
                    wrapper.clearResults();
                    wrapper.setActiveTab(DocumentSearchWrapper.SEARCH_CRITERIA_TAB);
                }
                break;
        }
    }

    protected final ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    private void findPortfolioItem(Long itemId, DocumentSearchWrapper wrapper) {
        try {
            // get item and check artefact security
            PortfolioItem item = (PortfolioItem) portfolioService.findAndCheckArtefactAccess(itemId);
            PortfolioItemWrapperBean bean = new PortfolioItemWrapperBean(item, portfolioService.findItemFile(item.getId()));            
            wrapper.setItemWrapper(bean);
        } catch (TalentStudioException e) {
            wrapper.setItemWrapper(null);
        }
    }

    private void setResults(List searchResults, DocumentSearchWrapper wrapper) {
        if (searchResults != null) {
            SearchResult filteredResults = new SearchResult();

            for (Iterator it = searchResults.iterator(); it.hasNext();) {
                SearchResult hit = (SearchResult) it.next();
                try {
                    PortfolioItem item = (PortfolioItem) portfolioService.findById(hit.getItemId());
                    if (wrapper.getNode() == null || !item.getContentType().getType().equals(wrapper.getNode().getNodeType())) {
                        filteredResults.addSingleResult(hit);
                    }
                } catch (TalentStudioException e) {
                    e.printStackTrace();
                }
            }
            // sort results by artefact and filter by security
            ArrayList<Long> itemIds = new ArrayList<Long>();
            Map mappedResults = PortfolioItemHelper.mapResults(filteredResults.getHits(), itemIds);
            wrapper.setMappedResults(mappedResults);
            wrapper.setCurrentNodes(itemIds);

            // set first item on wrapper
            if (!itemIds.isEmpty()) {
                final Long firstItemId = itemIds.get(0);
                findPortfolioItem(firstItemId, wrapper);
            } else {
                wrapper.setItemWrapper(null);
            }
        } else {
            wrapper.clearResults();
        }
    }

    private IField[] getSelectedContentTypes(DocumentSearchWrapper wrapper) {
        final List<IField> selectedContentTypes = wrapper.getSelectedContentTypes();
        return selectedContentTypes.toArray(new IField[selectedContentTypes.size()]);
    }

    private boolean isSubSearch(HttpServletRequest request) {
        return StringUtils.hasText(request.getParameter(SUB_SEARCH_PARAM));
    }

    protected ISearchManager searchManager;

    protected IPortfolioService portfolioService;

    protected ISubjectService subjectService;

    protected IPositionService positionService;

    protected Map dataSources;

    private static final int FORM_INDEX = 0;
    private static final int SEARCH_INDEX = 1;
    private static final int DATASOURCE_SELECTED = 2;
    private static final int BROWSE_INDEX = 3;
    private static final int SEARCH_AGAIN_INDEX = 4;
    private static final int BUTTON_PRESS = 5;

    protected static final String DOC_ID = "docId";

    public static final String SUB_SEARCH_PARAM = "subSearch";
}
