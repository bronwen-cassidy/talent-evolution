/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;


import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IGateway {

    /**
     * Execute a search with fields given in the fields and parameters from query.
     *
     * @param query - contains all the query parameters
     * @param fields
     * @return ISearchResult   - {@link com.zynap.talentstudio.organisation.portfolio.search.ISearchResult} object contining the results
     */
    ISearchResult executeSearch(ISearchQuery query, IField[] fields) throws ExternalSearchException;

    /**
     * Execute a query to return documents considered similar to the one referenced by the doc id parameter.
     *
     * @param docId
     * @param datasources
     * @param fields
     * @return ISearchResult   - {@link com.zynap.talentstudio.organisation.portfolio.search.SearchResult} object contining the results
     */
    ISearchResult executeSearch(int docId, String datasources, IField[] fields) throws ExternalSearchException;

    /**
     * Check version.
     * <br/> Return true if expected version matches actual version of 3rd party software.
     *
     * @return true or false
     * @throws ExternalSearchException
     */
    boolean checkVersion() throws ExternalSearchException;
}
