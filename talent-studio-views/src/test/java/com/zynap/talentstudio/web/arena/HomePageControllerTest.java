package com.zynap.talentstudio.web.arena;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.security.homepages.HomePage;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.HomePageViewController;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by bronwen.cassidy on 30/03/2016.
 *
 */
public class HomePageControllerTest extends ZynapDbUnitMockControllerTestCase {


    @Autowired
    private HomePageViewController homePageController;

    @Override
    protected String getDataSetFileName() {
        return "HomePageControllerTest.testHomePage.xml";
    }

    @Test
    public void testHomePage() {


        assertTrue(homePageController != null);


    }

    private byte[] getVTBytes() {
        String x = "<h1>Hello World</h1>" +
        "<a href=\"http://www.google.com?username=$test1&password=$test2\">" +
                "$attribtes.get('test1').label</a>";
        return x.getBytes();
    }
}