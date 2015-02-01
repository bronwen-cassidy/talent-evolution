package com.zynap.talentstudio.web.utils;

import org.dbunit.operation.DatabaseOperation;

import com.zynap.talentstudio.DBUnitUtils;

import javax.sql.DataSource;

import java.net.URL;

/**
 * User: amark
 * Date: 15-Sep-2006
 * Time: 12:34:57
 */
public abstract class ZynapDBunitMockTagLibTest extends ZynapMockTagLibTest {

    protected void setUp() throws Exception {
        super.setUp();

        dataSource = (DataSource) getBean("dataSource");

        final URL url = getClass().getResource(getDataSetFileName());
        DBUnitUtils.executeSetUp(dataSource, url, DatabaseOperation.REFRESH);
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        final URL url = getClass().getResource(getDataSetFileName());
        DBUnitUtils.executeTearDown(dataSource, url, DatabaseOperation.DELETE);
    }

    protected abstract String getDataSetFileName();

    private DataSource dataSource;
}
