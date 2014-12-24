package com.zynap.talentstudio.organisation.portfolio.search;

/**
 * User: amark
 * Date: 12-Jul-2006
 * Time: 12:08:49
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.organisation.portfolio.search.FormattingUtils;

public class TestFormattingUtils extends ZynapTestCase {

    public void testFormatDataSources() throws Exception {

        final String[] datasources = new String[]{"A", "B"};
        final String output = FormattingUtils.formatDataSources(datasources);
        assertEquals("A+B", output);
    }

    public void testFormatDataSource() throws Exception {

        final String[] datasources = new String[]{"ZZ"};
        final String output = FormattingUtils.formatDataSources(datasources);
        assertEquals(datasources[0], output);
    }

    public void testSplitDataSources() throws Exception {

        final String[] datasources = FormattingUtils.splitDataSources("ZZ+XX");
        assertEquals(2, datasources.length);
        assertEquals("ZZ", datasources[0]);
        assertEquals("XX", datasources[1]);
    }

    public void testSplitDataSource() throws Exception {

        final String[] datasources = FormattingUtils.splitDataSources("CVSDD");
        assertEquals(1, datasources.length);
        assertEquals("CVSDD", datasources[0]);
    }
}