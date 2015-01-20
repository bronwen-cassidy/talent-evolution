/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.ContentType;
import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;
import com.zynap.talentstudio.organisation.portfolio.search.IField;
import com.zynap.talentstudio.organisation.portfolio.search.SearchResult;
import com.zynap.talentstudio.web.common.AbstractBrowseWrapper;
import com.zynap.talentstudio.web.utils.SelectionNodeHelper;
import com.zynap.util.ArrayUtils;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DocumentSearchWrapper extends AbstractBrowseWrapper {

    public DocumentSearchWrapper() {
        setActiveTab(SEARCH_CRITERIA_TAB);
    }

    public DocumentSearchWrapper(DocumentSearchQuery documentSearchFilter) {
        this();
        this.searchQuery = documentSearchFilter;
    }

    public DocumentSearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(DocumentSearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void setDataSources(Map dataSources) {
        this.dataSources = new ArrayList<SelectionNode>();
        for (Iterator iterator = dataSources.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            this.dataSources.add(new SelectionNode(entry.getValue(), entry.getKey(), null));
        }
    }

    public Collection<SelectionNode> getDataSources() {
        return this.dataSources;
    }

    /**
     * Used for spring binding.
     *
     * @param sources The data sources for the search.
     */
    public void setSelectedSources(final String sources) {
        // set sources on document filter
        getSearchQuery().setSources(sources);

        // set selected sources types to true
        SelectionNodeHelper.enableSelections(dataSources, sources);
    }

    /**
     * Required by spring binding.
     *
     * @return null always
     */
    public String getSelectedSources() {
        return getSearchQuery().getSources();
    }

    public void setContentTypes(Collection contentTypes) {
        this.contentTypes = new LinkedList<SelectionNode>();
        for (Iterator iterator = contentTypes.iterator(); iterator.hasNext();) {
            ContentType contentType = (ContentType) iterator.next();
            SelectionNode selectionNode = new SelectionNode(contentType.getId(), contentType.getLabel(), IField.CONTENT_ID);
            selectionNode.setSelected(true);
            this.contentTypes.add(selectionNode);
        }
    }

    public Collection<SelectionNode> getContentTypes() {
        return contentTypes;
    }

    /**
     * Set all content types to be deselected.
     * <br> Then take list of content type ids if not empty, find the matching ones in the Collection of content types and flag them as selected.
     *
     * @param selected The array of selected content type ids
     */
    public void setSelectedContent(String[] selected) {
        this.selectedContentTypes = new ArrayList<IField>();

        // set selected content types to true
        SelectionNodeHelper.enableSelections(contentTypes, selected, selectedContentTypes);
    }   

    /**
     * Required by spring binding.
     *
     * @return new String array of size 0 always
     */
    public String[] getSelectedContent() {
        return new String[0];
    }

    public int getThreashold() {
        return getSearchQuery().getThreashold();
    }

    public void setThreashold(int threashold) {
        getSearchQuery().setThreashold(threashold);
    }

    public String getQueryText() {
        return getSearchQuery().getQueryText();
    }

    public void setQueryText(String queryText) {
        getSearchQuery().setQueryText(queryText);
    }

    public void setMaxResults(int maxHits) {
        getSearchQuery().setMaxResults(maxHits);
    }

    public int getMaxResults() {
        return getSearchQuery().getMaxResults();
    }

    public void setSummaryType(String summary) {
        getSearchQuery().setSummaryType(summary);
    }

    public String getSummaryType() {
        return getSearchQuery().getSummaryType();
    }

    public void setMappedResults(Map mappedResults) {
        this.mappedResults = mappedResults;
    }

    public Map getMappedResults() {
        return mappedResults;
    }

    public List<IField> getSelectedContentTypes() {
        return selectedContentTypes != null ? selectedContentTypes : new ArrayList<IField>();
    }

    public void clearResults() {
        setCurrentNodes(null);
        setMappedResults(null);
        setItemWrapper(null);
    }

    public void setItemWrapper(PortfolioItemWrapperBean itemWrapper) {
        this.itemWrapper = itemWrapper;
    }

    public PortfolioItemWrapperBean getItemWrapper() {
        return itemWrapper;
    }

    public Long getNodeId() {
        return itemId;
    }

    public void setNodeId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getFirst() {
        return getId(0);
    }

    public Long getLast() {
        return getId(currentNodes.size() - 1);
    }

    public int getCurrentItemIndex() {
        if (itemWrapper != null) {
            if (currentNodes != null && !currentNodes.isEmpty()) {
                return getIndex();
            }
        }
        return -1;
    }

    public Long getCurrentIndex() {
        if (itemWrapper != null && currentNodes != null) {
            int index = getIndex();
            return new Long(index + 1);
        }
        return null;
    }

    public int getIndex() {
        return currentNodes.indexOf(itemWrapper.getItem().getId());
    }

    /**
     * Method required by jsps to decide whether or not to include combo of search results.
     *
     * @return true always
     */
    public boolean isSearching() {
        return true;
    }

    public Long getNextItemId() {
        applyNextItemId();
        return getNodeId();
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    /**
     * Set all portfolio items to be deselected.
     * <br> Then take list of portfolio item ids if not empty, find the matching ones in the Collection of portfolio items and flag them as selected.
     * <br> Then build a "+" delimited string of the ids and store them in the searchQuery to use in the search.
     *
     * @param items The array of selected portfolio item ids
     */
    public void setSelectedItems(Long[] items) {
        if (this.portfolioItems != null) {
            if (!ArrayUtils.isEmpty(items)) {
                String selectedItems = ArrayUtils.arrayToString(items, "+");
                searchQuery.setExampleDocumentIds(selectedItems);
                SelectionNodeHelper.enableSelections(getPortfolioItems(), items);
            } else {
                searchQuery.setExampleDocumentIds(null);
            }
        }
    }

    /**
     * Required for spring binding.
     *
     * @return new array of type Long and size 0 always
     */
    public Long[] getSelectedItems() {
        return new Long[0];
    }

    /**
     * For each id in resultIds array, get the matching SearchResult and build a SelectionNode with it and add it to the portfolio items Collection.
     *
     * @param resultIds The selected result ids
     */
    public void setSelectedResults(final Long[] resultIds) {
        // clear in order not to append the selected documents to previously selected documents.
        getPortfolioItems().clear();

        class SelectedSearchResultClosure implements Closure {

            public void execute(Object input) {
                Collection searchResults = (Collection) input;
                for (Iterator iterator = searchResults.iterator(); iterator.hasNext();) {
                    SearchResult result = (SearchResult) iterator.next();
                    final Long itemId = result.getItemId();
                    if (org.apache.commons.lang.ArrayUtils.contains(resultIds, itemId)) {
                        getPortfolioItems().add(new SelectionNode(itemId, result.getContentTitle(), true));
                    }
                }
            }
        }

        if (mappedResults != null) {
            CollectionUtils.forAllDo(mappedResults.values(), new SelectedSearchResultClosure());
        }
    }

    /**
     * Required for spring binding.
     *
     * @return new array of type Long and size 0 always
     */
    public Long[] getSelectedResults() {
        return new Long[0];
    }

    /**
     * Create a Collection of {@link com.zynap.talentstudio.common.SelectionNode} objects - each one wraps a PortfolioItem.
     *
     * @param portfolioItems The Collection of portfolio items
     */
    public void setPortfolioItems(Collection portfolioItems) {
        this.portfolioItems = SelectionNodeHelper.createSelections(portfolioItems);
    }

    /**
     * Check if there are portfolio items.
     *
     * @return True if the Collection of portfolio items is not null and not empty.
     */
    public boolean isNotEmpty() {
        return (portfolioItems != null && !portfolioItems.isEmpty());
    }

    /**
     * Get the portfolio items available to use to search by example.
     *
     * @return Collection of {@link com.zynap.talentstudio.common.SelectionNode} objects - each one wraps a PortfolioItem.
     */
    public Collection<SelectionNode> getPortfolioItems() {
        if (portfolioItems == null) portfolioItems = new HashSet<SelectionNode>();
        return portfolioItems;
    }

    public int countPortfolioItemsSelected() {
        int count = 0;
        final Collection<SelectionNode> selectionNodes = getPortfolioItems();
        for(SelectionNode selectionNode : selectionNodes) {
            if(selectionNode.isSelected()) count++;
        }
        return count;
    }

    public boolean isDisplayDataSources() {
        return displayDataSources;
    }

    public void setDisplayDataSources(boolean displayDataSources) {
        this.displayDataSources = displayDataSources;
    }

    public Long getId(int pos) {
        if (currentNodes != null && !currentNodes.isEmpty())
            return (Long) currentNodes.get(pos);

        return null;
    }

    private DocumentSearchQuery searchQuery;

    /**
     * The content types selected by the user for the query (can be null / empty.)
     */
    private List<IField> selectedContentTypes;

    /**
     * The full list of availablt content types.
     */
    private Collection<SelectionNode> contentTypes;

    /**
     * The results - are in a Map keyed on artefact id.
     */
    private Map mappedResults;

    /**
     * The Collection of datasources available for the search.
     */
    private Collection<SelectionNode> dataSources;

    /**
     * The id of the current item (when browsing.)
     */
    private Long itemId;

    /**
     * The object that wraps the currently displayed portfolio item when browsing.
     */
    private PortfolioItemWrapperBean itemWrapper;

    /**
     * the node the portfolio items belong to when searching from a Node portfolio - not applicable when doing a "free text" document search, rather than a search from an artefact portfolio.
     */
    private Node node;

    /**
     * The list of portfolio items available to use to search by example.
     * <br> Actually a Collection of {@link SelectionNode} objects  - each one wraps a PortfolioItem.
     */
    private Collection<SelectionNode> portfolioItems;

    /**
     * Indicates whether or not to display the list of datasources.
     */
    private boolean displayDataSources;

    public static final String SEARCH_CRITERIA_TAB = "criteria";
}
