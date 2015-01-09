/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search.oracle;

import com.zynap.talentstudio.organisation.portfolio.search.ExternalSearchException;
import com.zynap.talentstudio.organisation.portfolio.search.IField;
import com.zynap.talentstudio.organisation.portfolio.search.IGateway;
import com.zynap.talentstudio.organisation.portfolio.search.IResultMapper;
import com.zynap.talentstudio.organisation.portfolio.search.ISearchQuery;
import com.zynap.talentstudio.organisation.portfolio.search.ISearchResult;
import com.zynap.talentstudio.organisation.portfolio.search.ISearcher;
import com.zynap.talentstudio.organisation.portfolio.search.IMapper;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 04-Oct-2007 11:17:30
 */
public class OracleGateway implements IGateway {

    /**
     * Execute a search with fields given in the fields and parameters from query.
     *
     * @param query  - contains all the query parameters
     * @param fields
     * @return ISearchResult   - {@link com.zynap.talentstudio.organisation.portfolio.search.ISearchResult} object contining the results
     */
    public ISearchResult executeSearch(ISearchQuery query, IField[] fields) throws ExternalSearchException {
        IResultMapper resultMapper = searcher.executeTextQuery(query, fields);
        return mapper.map(resultMapper);
    }

    /**
     * Execute a query to return documents considered similar to the one referenced by the doc id parameter.
     *
     * @param docId
     * @param datasources
     * @param fields
     * @return ISearchResult   - {@link com.zynap.talentstudio.organisation.portfolio.search.SearchResult} object contining the results
     */
    public ISearchResult executeSearch(int docId, String datasources, IField[] fields) throws ExternalSearchException {
        return null;
    }

    /**
     * Check version.
     * <br/> Return true if expected version matches actual version of 3rd party software.
     *
     * @return true or false
     * @throws com.zynap.talentstudio.organisation.portfolio.search.ExternalSearchException
     *
     */
    public boolean checkVersion() throws ExternalSearchException {
        return true;
    }

    public void setSearcher(ISearcher searcher) {
        this.searcher = searcher;
    }

    public void setMapper(IMapper mapper) {
        this.mapper = mapper;
    }

    private ISearcher searcher;
    private IMapper mapper;
}
