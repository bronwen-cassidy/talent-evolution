package com.zynap.talentstudio;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.sql.DataSource;

import java.net.URL;

/**
 * User: amark
 * Date: 20-Oct-2005
 * Time: 16:28:09
 */
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class ZynapDatabaseTestCase extends AbstractHibernateTestCase {

    public ZynapDatabaseTestCase() {
        super();
    }

    protected abstract String getDataSetFileName();

    protected void setUp() throws Exception {
        super.setUp();

        dataSource = (DataSource) applicationContext.getBean("dataSource");

        final URL url = getClass().getResource(getDataSetFileName());
        DBUnitUtils.executeSetUp(dataSource, url, getSetUpOperation());
    }

    protected void tearDown() throws Exception {

        super.tearDown();

        final URL url = getClass().getResource(getDataSetFileName());
        DBUnitUtils.executeTearDown(dataSource, url, getTearDownOperation());
    }

    /**
     * Return type of set up operation.
     * <br/> By default returns {@link DatabaseOperation#REFRESH} which inserts missing content and refreshes existing content with the data from the file.
     *
     * @return A {@link DatabaseOperation}
     * @throws Exception
     */
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.REFRESH;
    }

    /**
     * Return type of tear down operation.
     * <br/> By default returns {@link DatabaseOperation#DELETE} which removes all data added from the data file.
     *
     * @return A {@link DatabaseOperation}
     * @throws Exception
     */
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.DELETE;
    }

    private DataSource dataSource;
}
