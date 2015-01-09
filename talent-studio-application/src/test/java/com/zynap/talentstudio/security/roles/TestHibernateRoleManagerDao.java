package com.zynap.talentstudio.security.roles;

/**
 * User: amark
 * Date: 18-Jan-2005
 * Time: 15:35:31
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;

import java.util.List;

public class TestHibernateRoleManagerDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {

        super.setUp();
        _hibernateRoleManagerDao = (HibernateRoleManagerDao) applicationContext.getBean("roleManDao");
    }

    public void testAddRole() throws Exception {

        Role accessRole = new AccessRole(null, TEST_ACCESS_ROLE_ID, "a test role", true, true, null);
        _hibernateRoleManagerDao.addRole(accessRole, PRINCIPAL_ID);

        Role newRole = _hibernateRoleManagerDao.findAccessRole(TEST_ACCESS_ROLE_ID);
        assertEquals(accessRole, newRole);
    }

    public void testDisableRole() throws Exception {

        Role accessRole = new AccessRole(null, TEST_ACCESS_ROLE_ID, "a test role", true, true, null);
        _hibernateRoleManagerDao.addRole(accessRole, PRINCIPAL_ID);

        // disable and check
        _hibernateRoleManagerDao.disableRole((Long) accessRole.getId(), PRINCIPAL_ID);
        Role foundRole = _hibernateRoleManagerDao.findAccessRole(TEST_ACCESS_ROLE_ID);
        assertEquals(false, foundRole.isActive());
    }

    public void testUpdateRole() throws Exception {

        Role resourceRole = new ResourceRole(null, TEST_RESOURCE_ROLE_ID, "a test role", true, true, null);
        _hibernateRoleManagerDao.addRole(resourceRole, PRINCIPAL_ID);

        String description = "new description";
        resourceRole.setDescription(description);
        _hibernateRoleManagerDao.updateRole(resourceRole, PRINCIPAL_ID);

        Role foundRole = _hibernateRoleManagerDao.findResourceRole(TEST_RESOURCE_ROLE_ID);
        assertEquals(description, foundRole.getDescription());
    }

    public void testFindNonExistentAccessRole() throws Exception {

        try {
            _hibernateRoleManagerDao.findAccessRole(TEST_ACCESS_ROLE_ID);
            fail("Role " + TEST_ACCESS_ROLE_ID + " was incorrectly found");
        } catch (RoleNotFoundException e) {
            // expected
        }
    }

    public void testFindAccessRole() throws Exception {

        Role newRole = new AccessRole(null, TEST_ACCESS_ROLE_ID, "a test role", true, true, null);
        _hibernateRoleManagerDao.addRole(newRole, PRINCIPAL_ID);

        Role foundRole = _hibernateRoleManagerDao.findAccessRole(TEST_ACCESS_ROLE_ID);
        assertNotNull(foundRole);
    }

    public void testFindResourceRole() throws Exception {

        Role resourceRole = new ResourceRole(null, TEST_RESOURCE_ROLE_ID, "a test role", true, true, null);
        _hibernateRoleManagerDao.addRole(resourceRole, PRINCIPAL_ID);

        Role foundRole = _hibernateRoleManagerDao.findResourceRole(TEST_RESOURCE_ROLE_ID);
        assertNotNull(foundRole);
    }

    public void testGetActiveAccessRoles() throws Exception {

        List activeAccessRoles = _hibernateRoleManagerDao.getActiveAccessRoles();

        for (int i = 0; i < activeAccessRoles.size(); i++) {
            AccessRole accessRole = (AccessRole) activeAccessRoles.get(i);
            _hibernateRoleManagerDao.disableRole((Long) accessRole.getId(), PRINCIPAL_ID);
        }

        // add an inactive access role - number of active access roles should not increase
        Role newRole = new AccessRole(null, TEST_ACCESS_ROLE_ID, "a test role", false, true, null);
        _hibernateRoleManagerDao.addRole(newRole, PRINCIPAL_ID);

        activeAccessRoles = _hibernateRoleManagerDao.getActiveAccessRoles();
        assertEquals(0, activeAccessRoles.size());
    }

    public void testGetActiveResourceRoles() throws Exception {

        List activeResourceRoles = _hibernateRoleManagerDao.getActiveResourceRoles();

        for (int i = 0; i < activeResourceRoles.size(); i++) {
            ResourceRole role = (ResourceRole) activeResourceRoles.get(i);
            _hibernateRoleManagerDao.disableRole((Long) role.getId(), PRINCIPAL_ID);
        }

        // add an access role and a resource role - number of active resource roles should now be 1
        Role resourceRole = new ResourceRole(null, "my test resrole", "a test role", true, true, null);
        _hibernateRoleManagerDao.addRole(resourceRole, PRINCIPAL_ID);

        Role newRole = new AccessRole(null, TEST_ACCESS_ROLE_ID, "a test role", true, true, null);
        _hibernateRoleManagerDao.addRole(newRole, PRINCIPAL_ID);

        activeResourceRoles = _hibernateRoleManagerDao.getActiveResourceRoles();
        assertEquals(1, activeResourceRoles.size());
    }

    public void testGetAllRoles() throws Exception {

        int origNumOfRoles = _hibernateRoleManagerDao.getAllRoles().size();
        int origNumOfActiveRoles = _hibernateRoleManagerDao.getAllActiveRoles().size();

        // add an inactive access role
        Role newRole = new AccessRole(null, TEST_ACCESS_ROLE_ID, "a test role", false, true, null);
        _hibernateRoleManagerDao.addRole(newRole, PRINCIPAL_ID);

        // check that number of roles has gone up but number of active roles should be unchanged
        assertEquals(origNumOfRoles + 1, _hibernateRoleManagerDao.getAllRoles().size());
        assertEquals(origNumOfActiveRoles, _hibernateRoleManagerDao.getAllActiveRoles().size());

    }

    public void testGetAllActiveRoles() throws Exception {

        Role newRole = new AccessRole(null, TEST_ACCESS_ROLE_ID, "a test role", true, true, null);
        _hibernateRoleManagerDao.addRole(newRole, PRINCIPAL_ID);

        boolean roleFound = false;
        List allActiveRoles = _hibernateRoleManagerDao.getAllActiveRoles();
        for (int i = 0; i < allActiveRoles.size(); i++) {
            Role role = (Role) allActiveRoles.get(i);
            if (role.getLabel().equals(TEST_ACCESS_ROLE_ID)) {
                roleFound = true;
            }
        }

        if (!roleFound) {
            fail("Role " + TEST_ACCESS_ROLE_ID + " not found");
        }
    }

    private static final String TEST_ACCESS_ROLE_ID = "testaccessrole";
    private static final String TEST_RESOURCE_ROLE_ID = "testresourcerole";
    private static final int PRINCIPAL_ID = 0;

    private HibernateRoleManagerDao _hibernateRoleManagerDao;
}