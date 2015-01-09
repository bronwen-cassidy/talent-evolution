/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.common.groups;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 27-Nov-2007 15:39:30
 * @version 0.1
 */

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.security.homepages.HomePage;

import java.util.List;
import java.util.Set;


public class TestGroupService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "groups_testdata.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        groupService = (IGroupService) applicationContext.getBean("groupService");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFind() throws Exception {
        try {
            final List<Group> list = groupService.find(Group.TYPE_HOMEPAGE);
            assertNotNull(list);
            for (Group group : list) {
                final Set<HomePage> pageSet = group.getHomePages();
                assertNotNull(pageSet);
            }
        } catch (Exception e) {
            fail("no exception expected");
        }
    }

    public void testFindById_NonExistent() throws Exception {
        try {
            groupService.findById(new Long(-2));
        } catch (TalentStudioException e) {
            assertTrue(e instanceof DomainObjectNotFoundException);
        }
    }

    public void testFindById() throws Exception {
        final Group group = groupService.findById(new Long(21));
        final Set<HomePage> pageSet = group.getHomePages();
        assertEquals(7, pageSet.size());
    }

    public void testFindHomePages() throws Exception {
        final List<HomePage> homePageList = groupService.findHomePages(new Long(21));
    }


    private IGroupService groupService;
}