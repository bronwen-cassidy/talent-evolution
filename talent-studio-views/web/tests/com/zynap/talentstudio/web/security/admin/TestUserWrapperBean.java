/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.security.admin;

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;

import java.util.Collection;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestUserWrapperBean extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        IRoleManager roleManager = (IRoleManager) getBean("roleManager");
        allRoles = roleManager.getActiveAccessRoles();

        LoginInfo loginInfo = new LoginInfo();
        CoreDetail coreDetail = new CoreDetail();
        User user = new User(loginInfo, coreDetail);

        userWrapperBean = new UserWrapperBean(user, allRoles);
        assertTrue(userWrapperBean.getUserRoles().isEmpty());
    }

    public void testSetAccessRoleIds() throws Exception {

        // use first role
        Role selectedRole = (Role) allRoles.iterator().next();
        userWrapperBean.setAccessRoleIds(new Long[]{selectedRole.getId()});

        // check that correct number of roles are present
        final Collection accessRoles = userWrapperBean.getAccessRoles();
        assertEquals(allRoles.size(), accessRoles.size());

        // check that the correct role is marked as active
        for (Iterator iterator = accessRoles.iterator(); iterator.hasNext();) {
            Role accessRole = (Role) iterator.next();
            if (accessRole.equals(selectedRole)) {
                assertTrue(accessRole.isAssigned());
            } else {
                assertFalse(accessRole.isAssigned());
            }
        }

        // check role order
        checkRoleOrder(accessRoles);
    }

    public void testGetAccessRoleIds() throws Exception {
        userWrapperBean = new UserWrapperBean(null, null);
        final Long[] accessRoleIds = userWrapperBean.getAccessRoleIds();
        assertEquals(0, accessRoleIds.length);
    }

    public void testResetIds() throws Exception {

        final Long id = new Long(-1);

        // assign ids
        final User modifiedUser = userWrapperBean.getModifiedUser();
        final LoginInfo loginInfo = modifiedUser.getLoginInfo();
        final CoreDetail coreDetail = modifiedUser.getCoreDetail();

        modifiedUser.setId(id);
        loginInfo.setId(id);
        coreDetail.setId(id);

        // reset ids
        userWrapperBean.resetIds();

        // check ids are now null
        assertNull(modifiedUser.getId());
        assertNull(loginInfo.getId());
        assertNull(coreDetail.getId());
    }

    public void testClearRoles() throws Exception {

        // build array
        Long[] roleIds = new Long[allRoles.size()];
        int i = 0;
        for (Iterator iterator = allRoles.iterator(); iterator.hasNext(); i++) {
            Role accessRole = (Role) iterator.next();
            roleIds[i] = accessRole.getId();
        }

        // make all roles active and check they are
        userWrapperBean.setAccessRoleIds(roleIds);
        for (Iterator iterator = userWrapperBean.getAccessRoles().iterator(); iterator.hasNext();) {
            Role accessRole = (Role) iterator.next();
            assertTrue(accessRole.isAssigned());
        }

        // clear roles
        userWrapperBean.clearRoles();

        // check now all unassigned
        for (Iterator iterator = userWrapperBean.getAccessRoles().iterator(); iterator.hasNext();) {
            Role accessRole = (Role) iterator.next();
            assertFalse(accessRole.isAssigned());
        }
    }

    public void testGetUserRoles() throws Exception {
        final Collection userRoles = userWrapperBean.getUserRoles();
        assertNotNull(userRoles);
    }

    public void testGetAssignedUserRoles() throws Exception {

        // create new user
        LoginInfo loginInfo = new LoginInfo();
        CoreDetail coreDetail = new CoreDetail();
        User newUser = new User(loginInfo, coreDetail);

        // add first role
        final Iterator iterator = allRoles.iterator();
        Role selectedRole1 = (Role) iterator.next();
        newUser.addRole(selectedRole1);
        Role selectedRole2 = (Role) iterator.next();
        newUser.addRole(selectedRole2);

        // create new UserWrapper and check user roles are correct
        UserWrapperBean newUserWrapperBean = new UserWrapperBean(newUser);

        final Collection userRoles = newUserWrapperBean.getUserRoles();
        assertEquals(2, userRoles.size());
        assertTrue(userRoles.contains(selectedRole1));
        assertTrue(userRoles.contains(selectedRole2));
    }

    public void testGetModifiedUser() throws Exception {

        // build array of roles to assign
        final int numberOfRoles = 2;
        final Iterator iterator = allRoles.iterator();
        final Long[] roleIds = new Long[numberOfRoles];
        for (int i = 0; i < numberOfRoles; i++) {
            final Role role = (Role) iterator.next();
            roleIds[i] = role.getId();
        }

        // assign roles
        userWrapperBean.setAccessRoleIds(roleIds);

        // set first name, last name and username
        final String firstName = "firstname";
        final String secondName = "secondname";
        final String username = "username";

        userWrapperBean.setFirstName(firstName);
        userWrapperBean.setSecondName(secondName);
        userWrapperBean.getLoginInfo().setUsername(username);

        final User modifiedUser = userWrapperBean.getModifiedUser();

        // check fields have been set
        assertNull(modifiedUser.getId());
        assertEquals(firstName, modifiedUser.getCoreDetail().getFirstName());
        assertEquals(secondName, modifiedUser.getCoreDetail().getSecondName());
        assertEquals(username, modifiedUser.getLoginInfo().getUsername());

        // check roles have been assigned
        final Collection assignedRoles = modifiedUser.getUserRoles();

        //The home role must be included
        assertEquals(numberOfRoles + 1, assignedRoles.size());
    }

    public void testGetAccessRoles() throws Exception {
        final Collection accessRoles = userWrapperBean.getAccessRoles();
        assertNotNull(accessRoles);
        assertEquals(allRoles.size(), accessRoles.size());
    }

    /**
     * Check roles are in alphabetical order.
     *
     * @param roles
     */
    private void checkRoleOrder(Collection roles) {

        Role prevRole = null;
        for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
            Role role = (Role) iterator.next();

            if (prevRole != null) {
                final String label = role.getLabel();
                final String prevLabel = prevRole.getLabel();
                final int i = prevLabel.compareTo(label);
                assertTrue(i < 0);
            }

            prevRole = role;
        }
    }


    private UserWrapperBean userWrapperBean;
    private Collection allRoles;
}