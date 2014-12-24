/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio;

import com.zynap.domain.QueryParameter;
import com.zynap.talentstudio.organisation.portfolio.search.ISearchQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * This class gathers information from a form when needing to external
 * for artefacts matching a number of selected items from a portdolio.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DocumentSearchQuery implements ISearchQuery {

    public DocumentSearchQuery() {
        parameters = new HashMap<String, QueryParameter>();
    }

    public void setSummaryType(String summary) {
        parameters.put(SUMMARY, new QueryParameter(summary, QueryParameter.STRING));
        summaryType = summary;
    }

    public String getSummaryType() {
        return summaryType;
    }

    public void setMaxResults(int maxHits) {
        parameters.put(MAXIMUM_RESULTS, new QueryParameter(new Integer(maxHits), QueryParameter.NUMBER));
        maxResults = maxHits;
    }

    public int getMaxResults() {
        return maxResults;
    }

    /**
     * Get all parameters that have been mapped.
     *
     * @return parameters - formatted as key="Text" value=new QueryParameter(_queryText, QueryParameter.STRING));
     */
    public Map<String, QueryParameter> getParameters() {
        return parameters;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    /**
     * Get the free text to be used for the search (can be null.)
     *
     * @return _freeText
     */
    public String getQueryText() {
        return freeText;
    }

    public void setQueryText(String queryText) {
        freeText = queryText;
    }

    /**
     * Get the list of documents to be used to search by example (as a "+" delimited string.)
     *
     * @return exampleDocumentIds
     */
    public String getExampleDocumentIds() {
        return exampleDocumentIds;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getThreashold() {
        return threashold;
    }

    public void setThreashold(int threashold) {
        parameters.put(THRESHOLD, new QueryParameter(new Integer(threashold), QueryParameter.NUMBER));
        this.threashold = threashold;
    }

    public void setQueryResults(Map searchResults) {
        this.searchResults = searchResults;
    }

    public Map getSearchResults() {
        return searchResults;
    }

    public void setExampleDocumentIds(String exampleDocumentIds) {
        this.exampleDocumentIds = exampleDocumentIds;
    }

    /**
     * The extra parameters to include in the search.
     */
    private Map<String, QueryParameter> parameters;

    /**
     * The result threshold.
     */
    private int threashold;

    /**
     * The list of data sources for the search query ("+" delimited string if more than 1 datasource selected).
     */
    private String sources;

    /**
     * The type of query - Either "Suggest" or "Query".
     */
    private String queryType;

    /**
     * todo is this used
     */
    private int documentId;

    /**
     * The type of summary to be returned with the query - can be set to summary, concept, none.
     */
    private String summaryType;

    /**
     * The max number of results to return - default is 20.
     */
    private int maxResults = 20;

    /**
     * The search free text (can be null.)
     */
    private String freeText;

    /**
     * A "+" delimited string containing the ids of the portfolio items to be used to search by example.
     */
    private String exampleDocumentIds;

    /**
     * The results.
     */
    private Map searchResults;

    public static final String THRESHOLD = "MinScore";
    public static final String SUMMARY = "Summary";
    public static final String MAXIMUM_RESULTS = "MaxResults";
}
