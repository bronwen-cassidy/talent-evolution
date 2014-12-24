package com.zynap.talentstudio.web;

import org.dbunit.operation.DatabaseOperation;

import com.zynap.talentstudio.DBUnitUtils;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

import java.net.URL;

/**
 * User: amark
 * Date: 22-Jun-2006
 * Time: 12:50:11
 */
public abstract class ZynapDbUnitWebGenTestCase extends ZynapWebGenTestCase {

    public ZynapDbUnitWebGenTestCase() {
        if (applicationContext == null) {
            String jdbcConfig = "classpath:config/spring/testApplicationContext-jdbc.xml";
            applicationContext = new ClassPathXmlApplicationContext(jdbcConfig);
            applicationContext.refresh();
        }
    }

    protected abstract String getDataSetFileName();

    public void setUp() throws Exception {

        dataSource = (DataSource) applicationContext.getBean("dataSource");

        final URL url = getClass().getResource(getDataSetFileName());
        DBUnitUtils.executeSetUp(dataSource, url, DatabaseOperation.REFRESH);

        // call set up last so permits are loaded for logged in user
        super.setUp();
    }

    protected void tearDown() throws Exception {

        super.tearDown();

        final URL url = getClass().getResource(getDataSetFileName());
        DBUnitUtils.executeTearDown(dataSource, url, DatabaseOperation.DELETE);
    }

    protected static ClassPathXmlApplicationContext applicationContext;
    private DataSource dataSource;
}
