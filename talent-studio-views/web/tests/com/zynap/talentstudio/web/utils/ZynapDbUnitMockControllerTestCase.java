package com.zynap.talentstudio.web.utils;

import org.dbunit.operation.DatabaseOperation;

import com.zynap.talentstudio.DBUnitUtils;

import javax.sql.DataSource;

import java.net.URL;

/**
 * User: amark
 * Date: 09-May-2006
 * Time: 13:31:04
 */
public abstract class ZynapDbUnitMockControllerTestCase extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        dataSource = (DataSource) applicationContext.getBean("dataSource");

        final URL url = getClass().getResource(getDataSetFileName());
        DBUnitUtils.executeSetUp(dataSource, url, DatabaseOperation.REFRESH);
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        final URL url = getClass().getResource(getDataSetFileName());
        DBUnitUtils.executeTearDown(dataSource, url, DatabaseOperation.DELETE);
    }

    protected abstract String getDataSetFileName() throws Exception;

    private DataSource dataSource;
}
