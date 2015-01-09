/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.common.groups;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 28-Nov-2007 14:25:15
 * @version 0.1
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.exception.TalentStudioException;

import java.util.List;


public class TestHibernateGroupDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernateGroupDao = (IGroupDao) applicationContext.getBean("groupDao");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFind() throws Exception {
        try {
            final List<Group> groupList = hibernateGroupDao.find(Group.TYPE_QUESTIONNAIRE);
            assertNotNull(groupList);
        } catch (TalentStudioException e) {
            fail("No Exception Expected");
        }
    }

    private IGroupDao hibernateGroupDao;
}