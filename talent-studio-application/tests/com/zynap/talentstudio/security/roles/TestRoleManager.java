package com.zynap.talentstudio.security.roles;

/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.LinkedHashSet;
import java.util.List;

public class TestRoleManager extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        _roleManager = (IRoleManager) applicationContext.getBean("roleManager");
    }

    public void testFullAccessRoleAttributes() throws Exception {

        // set booleans to true so as to check properly
        Role role = new AccessRole(null, TEST_ACCESS_ROLE_ID, Boolean.TRUE, Boolean.TRUE, "description", true, true, Boolean.TRUE, null);
        _roleManager.create(role, PRINCIPAL_ID);

        Role foundRole = _roleManager.findRole(role.getId());
        assertEquals(Boolean.TRUE, foundRole.getIsSubDefault());
        assertEquals(Boolean.TRUE, foundRole.getIsOunitDefault());
        assertEquals(Boolean.TRUE, foundRole.getIsPositionDefault());
        assertEquals(true, foundRole.getIsSystem());
        assertEquals(true, foundRole.isActive());
    }

    public void testDisableNonExistentRole() throws Exception {

        try {
            _roleManager.disable(new Long(-1), PRINCIPAL_ID);
            fail("Incorrectly managed to disable a non-existent role");
        } catch (RoleNotFoundException expected) {
            // expected
        }
    }

    public void testUpdateNonExistentRole() throws Exception {

        try {
            Role newRole = new AccessRole(null, TEST_ACCESS_ROLE_ID, "a test role", true, true, null);
            _roleManager.update(newRole, PRINCIPAL_ID);
            fail("Incorrectly managed to update non-existent role");
        } catch (Exception expected) {
            // expected
        }
    }

    public void testAddPermitsToRole() throws Exception {

        Role newAccessRole = new AccessRole(null, TEST_ACCESS_ROLE_ID, "a test role", true, true, null);
        _roleManager.create(newAccessRole, PRINCIPAL_ID);

        List permits = _roleManager.getActiveAccessPermits();
        newAccessRole.setPermits(new LinkedHashSet(permits));

        _roleManager.update(newAccessRole, PRINCIPAL_ID);
        assertEquals(permits.size(), newAccessRole.getPermits().size());
    }

    public void testRemovePermitsFromRole() throws Exception {

        Role newAccessRole = new ResourceRole(null, TEST_RESOURCE_ROLE_ID, "a test role", true, true, null);
        List permits = _roleManager.getActiveAccessPermits();
        newAccessRole.setPermits(new LinkedHashSet(permits));
        _roleManager.create(newAccessRole, PRINCIPAL_ID);

        newAccessRole.clearPermits();
        _roleManager.update(newAccessRole, PRINCIPAL_ID);
        assertEquals(0, newAccessRole.getPermits().size());
    }

    private IRoleManager _roleManager;

    private static final String TEST_ACCESS_ROLE_ID = "testaccessrole";
    private static final String TEST_RESOURCE_ROLE_ID = "testresourcerole";
    private static final int PRINCIPAL_ID = 0;
}
