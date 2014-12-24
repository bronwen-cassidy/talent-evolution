/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search.oracle;
/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 17-Dec-2008 10:28:32
 * @version 0.1
 */

import junit.framework.TestCase;
import oracle.search.query.webservice.client.CustomAttribute;
import oracle.search.query.webservice.client.OracleSearchResult;
import oracle.search.query.webservice.client.ResultElement;

import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;
import com.zynap.talentstudio.organisation.portfolio.search.IField;
import com.zynap.talentstudio.organisation.portfolio.search.IResultMapper;
import com.zynap.talentstudio.organisation.portfolio.search.ISearchResult;

public class TestOracleSearcher extends TestCase {


    protected void setUp() throws Exception {
        super.setUp();
        oracleSearcher = new OracleSearcher();
        //niky host
        //oracleSearcher.setHost("170.170.1.34");
        oracleSearcher.setHost("s603004nj3el02.uspswy6.savvis.net");
        oracleSearcher.setPort(7777);
        oracleSearcher.setTimeout(60);
        oracleSearcher.setRetries(2);
        oracleSearcher.setVersion("10.1.8.2");

        fields[0] = new SelectionNode("FPSC");

    }

    public void testExecuteTextQuery() throws Exception {


        final DocumentSearchQuery searchQuery = new DocumentSearchQuery();
        searchQuery.setSources("POSITION_GROUP+POSITION_GROUP");
        searchQuery.setQueryText("a");
        searchQuery.setMaxResults(10);

        IResultMapper resultMapper = oracleSearcher.executeTextQuery(searchQuery, fields);
        assertNotNull(resultMapper);
        OracleSearchResult oSearchResult = (OracleSearchResult) resultMapper.getResults();
        assertNotNull(oSearchResult);
        assertEquals(oSearchResult.getResultElements().length > 1, true);
    }

    public void testExecuteTextQueryMultitable() throws Exception {


        final DocumentSearchQuery searchQuery = new DocumentSearchQuery();
        searchQuery.setSources("POSITION_GROUP+POSITION_GROUP");
        searchQuery.setQueryText("Young&&&&&");
        searchQuery.setMaxResults(100);

        IResultMapper resultMapper = oracleSearcher.executeTextQuery(searchQuery, fields);
        assertNotNull(resultMapper);
        OracleSearchResult oSearchResult = (OracleSearchResult) resultMapper.getResults();
        assertNotNull(oSearchResult);
        assertEquals(oSearchResult.getResultElements().length < 1, true);
    }

    public void testExecuteTextQueryTablePos() throws Exception {


        final DocumentSearchQuery searchQuery = new DocumentSearchQuery();
        searchQuery.setSources("POSITION_GROUP");
        searchQuery.setQueryText("a");
        searchQuery.setMaxResults(100);

        IResultMapper resultMapper = oracleSearcher.executeTextQuery(searchQuery, fields);
        assertNotNull(resultMapper);
        OracleSearchResult oSearchResult = (OracleSearchResult) resultMapper.getResults();
        assertNotNull(oSearchResult);
        assertEquals(oSearchResult.getResultElements().length > 1, true);
    }
    public void testExecuteTextQueryTableSub() throws Exception {


        final DocumentSearchQuery searchQuery = new DocumentSearchQuery();
        searchQuery.setSources("POSITION_GROUP");
        searchQuery.setQueryText("a");
        searchQuery.setMaxResults(100);

        IResultMapper resultMapper = oracleSearcher.executeTextQuery(searchQuery, fields);
        assertNotNull(resultMapper);
        OracleResultMapper mapper = (OracleResultMapper)resultMapper;
        final ISearchResult result = mapper.mapResults();
        OracleSearchResult oSearchResult = (OracleSearchResult) resultMapper.getResults();
        assertNotNull(oSearchResult);
        assertEquals(oSearchResult.getResultElements().length > 1, true);
    }

    public void testExecuteTextQueryDb() throws Exception {


        final DocumentSearchQuery searchQuery = new DocumentSearchQuery();
        searchQuery.setSources("POSITION_GROUP+POSITION_GROUP");
        searchQuery.setQueryText("a");
        searchQuery.setMaxResults(100);

        IResultMapper resultMapper = oracleSearcher.executeTextQuery(searchQuery, fields);
        assertNotNull(resultMapper);
        OracleSearchResult oSearchResult = (OracleSearchResult) resultMapper.getResults();
        assertNotNull(oSearchResult);
        final ResultElement[] resultElements = oSearchResult.getResultElements();
        assertEquals(resultElements.length > 1, true);
        for (ResultElement resultElement : resultElements) {
            final CustomAttribute[] customAttributes = resultElement.getCustomAttributes();
            assertNotNull(customAttributes);
            assertEquals(customAttributes.length > 1, true);
        }
    }

    private OracleSearcher oracleSearcher;
    private IField[] fields = new SelectionNode[8];
}