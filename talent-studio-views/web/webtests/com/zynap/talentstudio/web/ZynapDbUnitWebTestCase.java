package com.zynap.talentstudio.web;

import org.dbunit.operation.DatabaseOperation;

import com.zynap.talentstudio.DBUnitUtils;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

import java.net.URL;

/**
 * User: amark
 * Date: 19-Sep-2005
 * Time: 16:09:49
 */
public abstract class ZynapDbUnitWebTestCase extends ZynapWebTestCase {

    public ZynapDbUnitWebTestCase() {

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
        DBUnitUtils.executeSetUp(dataSource, url, getSetUpOperation());

        // call set up last so permits are loaded for logged in user
        super.setUp();
    }

    protected void tearDown() throws Exception {

        super.tearDown();

        final URL url = getClass().getResource(getDataSetFileName());
        DBUnitUtils.executeTearDown(dataSource, url, getTearDownOperation());
    }

    /**
     * Return type of set up operation.
     * <br/> By default returns {@link DatabaseOperation.REFRESH} which inserts missing content and refreshes existing content with the data from the file.
     *
     * @return A {@link DatabaseOperation}
     * @throws Exception
     */
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.REFRESH;
    }

    /**
     * Return type of tear down operation.
     * <br/> By default returns {@link DatabaseOperation.DELETE} which removes all data added from the data file.
     *
     * @return A {@link DatabaseOperation}
     * @throws Exception
     */
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.DELETE;
    }

    protected static ClassPathXmlApplicationContext applicationContext;
    private DataSource dataSource;
}
