package com.zynap.talentstudio.web.security;

/**
 * User: amark
 * Date: 29-Apr-2005
 * Time: 12:24:49
 */

import junit.framework.TestCase;

import com.zynap.domain.admin.User;

import java.util.Collection;
import java.util.HashSet;

public class TestUserHelper extends TestCase {

    public void testRemoveRootUsers() throws Exception {

        Collection users = new HashSet();

        // add root user
        final User rootUser = new User();
        rootUser.setRoot(true);
        rootUser.setId(new Long(0));
        users.add(rootUser);

        // add normal user
        final User newUser = new User();
        newUser.setId(new Long(1));
        users.add(newUser);

        // check that there is only one user and that is not the root user
        final Collection appUsers = UserHelper.removeRootUsers(users);
        assertEquals(1, appUsers.size());
        assertEquals(newUser, appUsers.iterator().next());
    }

    public void testRetainRootUsers() throws Exception {

        final User rootUser = new User();
        rootUser.setRoot(true);
        rootUser.setId(new Long(0));

        final User newUser = new User();
        newUser.setId(new Long(1));

        final User newUser2 = new User();
        newUser2.setId(new Long(2));

        // current user has 1 root and 2 normal - newUser and newuser2
        Collection currentUsers = new HashSet();
        currentUsers.add(rootUser);
        currentUsers.add(newUser);
        currentUsers.add(newUser2);

        final User newUser3 = new User();
        newUser3.setId(new Long(3));

        Collection newUsers = new HashSet();
        newUsers.add(newUser2);
        newUsers.add(newUser3);

        // check that the root user has been copied into the new users collection
        UserHelper.retainRootUsers(currentUsers, newUsers);
        assertEquals(3, newUsers.size());
        assertFalse(newUsers.contains(newUser));

        // check that root user, newuser2 and newuser3 are all in the collection
        assertTrue(newUsers.contains(rootUser));
        assertTrue(newUsers.contains(newUser2));
        assertTrue(newUsers.contains(newUser3));
    }
}