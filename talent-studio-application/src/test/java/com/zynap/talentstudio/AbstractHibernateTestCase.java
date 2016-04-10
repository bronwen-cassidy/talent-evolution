/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.users.IUserService;

import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/applicationContext.xml"
        , "classpath:/spring/applicationContext-tx.xml"
        , "classpath:/config/spring/testApplicationContext-jdbc.xml"
        , "classpath:/config/spring/testApplicationContext-mail.xml"
        , "classpath:/config/spring/testApplicationContext-hibernate.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class AbstractHibernateTestCase extends ZynapTestCase {
    public AbstractHibernateTestCase() {
        
    }

    protected List<String> getConfigLocations() {

        String jdbcConfig = "classpath:config/spring/testApplicationContext-jdbc.xml";
        String hibernateConfig = "classpath:config/spring/testApplicationContext-hibernate.xml";
        String mailConfig = "classpath:config/spring/testApplicationContext-mail.xml";

        String applicationConfig = "classpath:spring/applicationContext.xml";
        String txConfig = "classpath:spring/applicationContext-tx.xml";
        String schConfig = "classpath:spring/applicationContext-scheduling.xml";

        List<String> configLocations = new ArrayList<String>();
        configLocations.add(jdbcConfig);
        configLocations.add(hibernateConfig);
        configLocations.add(applicationConfig);
        configLocations.add(mailConfig);
        configLocations.add(txConfig);
        configLocations.add(schConfig);

        return configLocations;
    }

    protected void setUp() throws Exception {
        super.setUp();

        template = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
        transactionManager = (PlatformTransactionManager) applicationContext.getBean("transactionManager");
        sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");

        createTransaction();

        UserSession newSession = new UserSession(getSysUserPrincipal(), getArenaMenuHandler());
        UserSessionFactory.setUserSession(newSession);
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        rollback();
        destroyTransaction();
        closeDbConnection();

        UserSessionFactory.setUserSession(null);
    }

    protected void createTransaction() throws HibernateException {
        session = sessionFactory.openSession();
        //session.setFlushMode(FlushMode.NEVER);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
    }

    private void closeDbConnection() throws SQLException {
        Connection con = DataSourceUtils.getConnection(template.getDataSource());
        if (con != null && !con.isClosed()) {
            logger.warn("Closing database connection");
            con.close();
        }
    }

    private void destroyTransaction() throws HibernateException {
        SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        session = holder.getSession();
        session.flush();
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        SessionFactoryUtils.releaseSession(session, sessionFactory);
    }

    protected final void commitAndStartNewTx() throws Exception {
        commit();
        destroyTransaction();
        createTransaction();
    }

    protected final void rollbackAndStartNewTx() throws Exception {
        rollback();
        destroyTransaction();
        createTransaction();
    }

    protected final User getAdminUser(IUserService userService) throws TalentStudioException {
        return (User) userService.findById(ADMINISTRATOR_USER_ID);
    }

    protected final void rollback() {
        transactionManager.rollback(transactionStatus);
    }

    protected final void commit() {
        transactionManager.commit(transactionStatus);
    }

    protected final Object getBean(String name) throws Exception {
        return applicationContext.getBean(name);
    }

    protected static ClassPathXmlApplicationContext applicationContext;
    protected JdbcTemplate template;
    protected Session session;

    protected SessionFactory sessionFactory;
    protected PlatformTransactionManager transactionManager;
    protected TransactionStatus transactionStatus;
}
