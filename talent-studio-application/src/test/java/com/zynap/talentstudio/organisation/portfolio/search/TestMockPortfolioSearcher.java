package com.zynap.talentstudio.organisation.portfolio.search;

/**
 * User: amark
 * Date: 18-Jul-2006
 * Time: 14:50:48
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;
import com.zynap.talentstudio.organisation.portfolio.search.IField;
import com.zynap.talentstudio.organisation.portfolio.search.IResultMapper;
import com.zynap.talentstudio.organisation.portfolio.search.MockPortfolioSearcher;

public class TestMockPortfolioSearcher extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        mockPortfolioSearcher = (MockPortfolioSearcher) getBean("mockAutonomySearcher");
    }

    public void testExecuteTextQuery() throws Exception {

        IField[] fields = null;
        final IResultMapper resultMapper = mockPortfolioSearcher.executeTextQuery(new DocumentSearchQuery(), fields);
        assertNotNull(resultMapper);
        assertNotNull(resultMapper.getResults());
    }

    private MockPortfolioSearcher mockPortfolioSearcher;
}