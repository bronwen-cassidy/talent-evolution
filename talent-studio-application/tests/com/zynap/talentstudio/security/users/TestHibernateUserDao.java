package com.zynap.talentstudio.security.users;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserSearchQuery;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.organisation.OrganisationUnit;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TestHibernateUserDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernateUserDao = (HibernateUserDao) applicationContext.getBean("hibUserManDao");
    }

    public void testEncrypt() throws Exception {
        String password = "puppyone";
        String actual = hibernateUserDao.encrypt(password);
        assertNotSame(password, actual);
    }

    //public void testAssignUserPermits() throws Exception {
    //    hibernateUserDao.assignUserPermits(new Long(0));
    //    commit();
    //    System.out.println("pause");
    //}

    public void testSearch() throws Exception {
        UserSearchQuery query = new UserSearchQuery();
        query.setPrefName("zyn");
        query.setFirstName("ad");
        Collection results = hibernateUserDao.search(ADMINISTRATOR_USER_ID, query);

        // searching as administrator should never return root users or self
        boolean found = false;
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            User user = (User) iterator.next();
            assertFalse(user.isRoot());
            assertFalse(user.getId().equals(ADMINISTRATOR_USER_ID));
        }
        assertFalse(found);
    }

    public void testGetLoginInfo() throws Exception {
        LoginInfo info = hibernateUserDao.getLoginInfo(ROOT_USER_ID);
        assertNotNull(info);
    }

    public void testGetLoginInfoByUserName() throws Exception {
        LoginInfo info = hibernateUserDao.getLoginInfo(ROOT_USERNAME);
        assertNotNull(info);
    }

    public void testGetDomainObjectClass() throws Exception {
        assertEquals(User.class, hibernateUserDao.getDomainObjectClass());
    }

    public void testFindByInvalidUserName() throws Exception {
        try {
            hibernateUserDao.findByUserName("foo");
            fail("Incorrectly found non-existent user");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testFindBySubjectId() throws Exception {
        try {
            hibernateUserDao.findBySubjectId(new Long(44));
            fail("should have thrown an exception");
        } catch (DomainObjectNotFoundException e) {
            // expected
        }
    }

    public void testFindByUserName() throws Exception {
        hibernateUserDao.findByUserName(ROOT_USERNAME);
    }

    public void testGetAppUsers() throws Exception {
        final Collection appUsers = hibernateUserDao.getAppUsers();
        assertNotNull(appUsers);

        // check that the administrator was found but that the root user was not
        boolean administratorFound = false;
        for (Iterator iterator = appUsers.iterator(); iterator.hasNext();) {
            User user = (User) iterator.next();
            assertFalse(user.isRoot());

            if (!administratorFound)
                administratorFound = user.getLoginInfo().getUsername().equals("administrator");
        }

        assertTrue(administratorFound);
    }

    public void testGetUserDefaultOrganisationUnit() throws Exception {
        try {
            OrganisationUnit ou = hibernateUserDao.getUserDefaultOrganisationUnit(ROOT_USERNAME);
            assertEquals(ou.getId(), OrganisationUnit.ROOT_ORG_UNIT_ID);

        } catch (TalentStudioException ex) {
        }
    }

    public void testSaveSessionLog() throws Exception {
        hibernateUserDao.create(new SessionLog("dfdsfasdfasfdxxxs", "local", IUserService.STATUS_OPEN, new Date(), new User(new Long(0))));
    }

    public void testListAppUsers() throws Exception {
        List<UserDTO> list = hibernateUserDao.listAppUsers();
        assertFalse(list.contains(new UserDTO(new Long(0))));
        assertFalse(list.contains(new UserDTO(new Long(1))));
        assertFalse(list.contains(new UserDTO(new Long(2))));
    }

    private HibernateUserDao hibernateUserDao;
}
