package com.zynap.talentstudio.security;

/**
 * User: amark
 * Date: 15-Mar-2005
 * Time: 14:41:36
 * Test for {@link SecurityManager}.
 */

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.IOrganisationDao;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.security.roles.AccessRole;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.security.users.IUserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestSecurityManager extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        securityManager = (ISecurityManager) applicationContext.getBean("securityManager");
        userService = (IUserService) applicationContext.getBean("userService");
        roleManager = (IRoleManager) applicationContext.getBean("roleManager");
        organisationManagerDao = (IOrganisationDao) applicationContext.getBean("organisationUnitDao");
        permitManagerDao = (IPermitManagerDao) applicationContext.getBean("permitManDao");
    }

    /**
     * Test loading all domains.
     *
     * @throws Exception
     */
    public void testGetAllDomains() throws Exception {
        assertNotNull(securityManager.getAllDomains());
    }

    /**
     * Test that attempts to add a security domain with no label which is the only required field.
     *
     * @throws Exception
     */
    public void testCreateDomainNoLabel() throws Exception {

        try {
            SecurityDomain newSecurityDomain = new SecurityDomain();
            securityManager.createDomain(newSecurityDomain);
            fail("Incorrectly managed to add security domain with no label");
        } catch (Exception expected) {

        }
    }

    /**
     * Test that adds a security domain with no users or roles attached.
     *
     * @throws Exception
     */
    public void testCreateDomain() throws Exception {

        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domain1");
        newSecurityDomain.setActive(false);
        newSecurityDomain.setExclusive(true);

        final Area newArea = new Area();
        newArea.setLabel("area1");
        newSecurityDomain.setArea(newArea);

        securityManager.createDomain(newSecurityDomain);

        // check that active / exclusive flags are correct
        final SecurityDomain domain = securityManager.findDomain(newSecurityDomain.getId());
        assertFalse(domain.isActive());
        assertTrue(domain.isExclusive());

        // check that the users and roles collections are not null
        assertNotNull(domain.getUsers());
        assertNotNull(domain.getRoles());

        // check that the node protected by the domain is the expected one
        assertEquals(domain.getArea(), newArea);
    }

    /**
     * Create a security domain and then add roles to it.
     * <br> Then delete the security domain and check that the roles still exist.
     *
     * @throws Exception
     */
    public void testDomainWithRoles() throws Exception {

        // create domain with no roles
        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domain2");
        newSecurityDomain.setActive(false);
        newSecurityDomain.setExclusive(true);

        final Area newArea = new Area();
        newArea.setLabel("area2");
        newSecurityDomain.setArea(newArea);

        securityManager.createDomain(newSecurityDomain);

        // add roles and update
        final List resourceRoles = roleManager.getActiveResourceRoles();
        for (int i = 0; i < resourceRoles.size(); i++) {
            Role role = (Role) resourceRoles.get(i);
            newSecurityDomain.addRole(role);
        }
        securityManager.updateDomain(newSecurityDomain);
        assertEquals(resourceRoles.size(), newSecurityDomain.getRoles().size());

        // remove roles
        newSecurityDomain.getRoles().clear();
        securityManager.updateDomain(newSecurityDomain);

        // check that roles have been removed from domain
        assertTrue(newSecurityDomain.getRoles().isEmpty());

        // check that roles have not been deleted
        assertEquals(roleManager.getActiveResourceRoles(), resourceRoles);

        // delete domain
        securityManager.deleteDomain(newSecurityDomain.getId());

        // check that roles have not been deleted
        assertEquals(roleManager.getActiveResourceRoles(), resourceRoles);
    }

    /**
     * Test that adds a security domain with users.
     *
     * @throws Exception
     */
    public void testCreateDomainWithUsers() throws Exception {

        // create a new user
        CoreDetail coreDetail = new CoreDetail("mandy", "Grey", "Harper", "Mrs", "two@abc.com", "44444444");

        final String username = "another8";
        final String password = "22gotcha";
        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        User newUser = new User(loginInfo, coreDetail);
        userService.create(newUser);

        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domain3");

        final Area newArea = new Area();
        newArea.setLabel("area3");
        newSecurityDomain.setArea(newArea);
        newSecurityDomain.addUser(newUser);

        securityManager.createDomain(newSecurityDomain);

        // check that user is present
        final SecurityDomain domain = securityManager.findDomain(newSecurityDomain.getId());
        assertEquals(1, domain.getUsers().size());

        // remove user from domain and check is no longer present
        domain.getUsers().clear();
        securityManager.updateDomain(newSecurityDomain);
        assertEquals(0, domain.getUsers().size());

        // check that user still exists
        final IDomainObject foundUser = userService.findById(newUser.getId());
        assertNotNull(foundUser);
        assertEquals(newUser, foundUser);
    }

    /**
     * Change label and active and exclusive flags for domain.
     *
     * @throws Exception
     */
    public void testUpdateDomain() throws Exception {

        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domain4");
        newSecurityDomain.setActive(false);
        newSecurityDomain.setExclusive(true);

        final Area newArea = new Area();
        newArea.setLabel("area4");
        newSecurityDomain.setArea(newArea);

        securityManager.createDomain(newSecurityDomain);

        String newLabel = "domain5";
        newSecurityDomain.setLabel(newLabel);
        newSecurityDomain.setActive(true);
        newSecurityDomain.setExclusive(false);
        securityManager.updateDomain(newSecurityDomain);

        // check that fields have changed
        assertEquals(newSecurityDomain.getLabel(), newLabel);
        assertTrue(newSecurityDomain.isActive());
        assertFalse(newSecurityDomain.isExclusive());
    }

    /**
     * Test changing the area associated with a domain.
     * <br> In particular check that the old area is not deleted.
     *
     * @throws Exception
     */
    public void testChangeDomainArea() throws Exception {

        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domain6");
        newSecurityDomain.setActive(false);
        newSecurityDomain.setExclusive(true);

        final Area newArea = new Area();
        newArea.setLabel("area6");
        newSecurityDomain.setArea(newArea);

        securityManager.createDomain(newSecurityDomain);

        final Area replacementArea = new Area();
        replacementArea.setLabel("area7");
        newSecurityDomain.setArea(replacementArea);

        securityManager.updateDomain(newSecurityDomain);

        // check that the area protected by the domain is the expected one
        assertEquals(newSecurityDomain.getArea(), replacementArea);

        // check that the old area still exists
        Area foundArea = securityManager.findArea(newArea.getId());
        assertEquals(foundArea, newArea);
    }

    /**
     * Test deletion of a domain.
     * <br> In particular check that the area is not deleted when the domain is deleted.
     *
     * @throws Exception
     */
    public void testDeleteDomain() throws Exception {

        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domain8");
        newSecurityDomain.setActive(false);
        newSecurityDomain.setExclusive(true);

        final Area newArea = new Area();
        newArea.setLabel("area8");
        newSecurityDomain.setArea(newArea);

        securityManager.createDomain(newSecurityDomain);
        final Long domainId = newSecurityDomain.getId();

        // delete the domain and then check it cannot be found
        securityManager.deleteDomain(domainId);
        try {
            securityManager.findDomain(domainId);
            fail("Incorrectly found deleted domain");
        } catch (DomainObjectNotFoundException expected) {

        }

        // but check that the area is still there as areas are not deleted when security domains are removed
        Area foundArea = securityManager.findArea(newArea.getId());
        assertEquals(foundArea, newArea);
    }

    /**
     * Create a domain with one user and then delete the user.
     *
     * @throws Exception
     */
    public void testDeleteUserFromDomain() throws Exception {

        // create a new user
        CoreDetail coreDetail = new CoreDetail("firstName", "lastName", "givenName", "Mr", "email@host.com", "999999");

        final String username = "username";
        final String password = "password";
        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        User newUser = new User(loginInfo, coreDetail);
        userService.create(newUser);

        SecurityDomain newSecurityDomain = new SecurityDomain();
        newSecurityDomain.setLabel("domain9");

        final Area newArea = new Area();
        newArea.setLabel("area9");
        newSecurityDomain.setArea(newArea);
        newSecurityDomain.addUser(newUser);

        securityManager.createDomain(newSecurityDomain);

        newSecurityDomain.getUsers().clear();
        securityManager.updateDomain(newSecurityDomain);

        // check that security domain still exists and has no users
        assertTrue(securityManager.findDomain(newSecurityDomain.getId()).getUsers().isEmpty());
    }

    public void testDeleteAreaElement() throws Exception {
        Area area = new Area();
        area.setLabel("area2");
        area.setComments("comments");
        area.setActive(true);

        OrganisationUnit ou = organisationManagerDao.findByID(DEFAULT_ORG_UNIT_ID);
        area.getAreaElements().add(new AreaElement(null, ou, area, false));

        securityManager.createArea(area);

        Set<AreaElement> areaElements = securityManager.getAreaElements(area.getId());
        try {
            securityManager.removeAreaElements(area);
        } catch (Exception e) {
            fail("no exception epected but got " + e.getMessage());
        }
    }

    /**
     * Test adding an AccessRole to a security domain - should be rejected as security domains can only have resource roles.
     *
     * @throws Exception
     */
    public void testAddAccessRole() throws Exception {

        SecurityDomain newSecurityDomain = new SecurityDomain();
        try {
            newSecurityDomain.addRole(new AccessRole(null, "role1", "description", true, true, null));
            fail("Incorrectly added access role to security domain");
        } catch (IllegalArgumentException expected) {

        }
    }

    public void testCreateArea() throws Exception {

        Area area = new Area();
        area.setLabel("area1");
        area.setComments("comments");
        area.setActive(true);

        OrganisationUnit ou = organisationManagerDao.findByID(DEFAULT_ORG_UNIT_ID);
        area.getAreaElements().add(new AreaElement(null, ou, area, false));

        securityManager.createArea(area);
        assertTrue(0 != area.getId().intValue());
        assertEquals(1, area.getAreaElements().size());
        assertTrue(area.isActive());
    }

    public void testUpdateArea() throws Exception {

        Area area = new Area();
        area.setLabel("area2");
        area.setComments("comments");
        area.setActive(true);

        OrganisationUnit ou = organisationManagerDao.findByID(DEFAULT_ORG_UNIT_ID);
        area.getAreaElements().add(new AreaElement(null, ou, area, false));

        securityManager.createArea(area);
        assertEquals(1, area.getAreaElements().size());

        area.assignAreaElements(new HashSet<AreaElement>());
        area.setActive(false);
        securityManager.updateArea(area);
        assertFalse(area.isActive());
        assertEquals(0, area.getAreaElements().size());
    }

    public void testDeleteArea() throws Exception {

        Area area = new Area();
        area.setLabel("area9");
        area.setComments("comments");
        area.setActive(true);

        OrganisationUnit ou = organisationManagerDao.findByID(DEFAULT_ORG_UNIT_ID);
        area.getAreaElements().add(new AreaElement(null, ou, area, false));

        securityManager.createArea(area);

        final Long areaId = area.getId();
        securityManager.deleteArea(areaId);

        try {
            securityManager.findArea(areaId);
            fail("Incorrectly found deleted area");
        } catch (DomainObjectNotFoundException expected) {

        }
    }

    /**
     * Check admin user has access to view default position.
     * <br/> used to check that query for checking domain permits works.
     *
     * @throws Exception
     */
    public void testCheckAccess() throws Exception {

        final IPermit permit = permitManagerDao.getPermit(SecurityConstants.POSITION_CONTENT, SecurityConstants.VIEW_ACTION);
        final boolean access = securityManager.checkAccess(permit, getAdminUserPrincipal(), DEFAULT_POSITION_ID);
        assertTrue(access);
    }

    /**
     * Check admin user has access to view default position.
     * @throws Exception
     */
    public void testCheckNodeAccess() throws Exception {

        final IPermit permit = permitManagerDao.getPermit(SecurityConstants.POSITION_CONTENT, SecurityConstants.VIEW_ACTION);
        securityManager.checkAccess(permit, getAdminUserPrincipal(), DEFAULT_POSITION);
        assertTrue(DEFAULT_POSITION.isHasAccess());
    }

    /**
     * Test that you have no access to a system Node like the default position when you want to delete it.
     * @throws Exception
     */
    public void testDeleteSystemNodeAccess() throws Exception {

        final IPermit permit = permitManagerDao.getPermit(SecurityConstants.POSITION_CONTENT, SecurityConstants.DELETE_ACTION);
        securityManager.checkAccess(permit, getAdminUserPrincipal(), DEFAULT_POSITION);
        assertFalse(DEFAULT_POSITION.isHasAccess());
    }

    public void testFindDomain() throws Exception {
        try {
            securityManager.findDomain(new Long(-99));
            fail("Found non-existent domain");
        } catch (DomainObjectNotFoundException expected) {

        }
    }

    public void testGetAreaElements() throws Exception {
        try {
            Area area = new Area();
            area.setLabel("area2");
            area.setComments("comments");
            area.setActive(true);

            OrganisationUnit ou = organisationManagerDao.findByID(DEFAULT_ORG_UNIT_ID);
            area.getAreaElements().add(new AreaElement(null, ou, area, false));

            securityManager.createArea(area);

            Set<AreaElement> areaElements = securityManager.getAreaElements(area.getId());
            assertEquals(1, areaElements.size());

        } catch (DomainObjectNotFoundException expected) {

        }
    }

    public void testFindArea() throws Exception {
        try {
            securityManager.findArea(new Long(-99));
            fail("Found non-existent area");
        } catch (DomainObjectNotFoundException expected) {

        }
    }

    public void testIsDomainMember() throws Exception {
        assertTrue(securityManager.isDomainMember(ISecurityManager.ORPHANS_DOMAIN_ID, ADMINISTRATOR_USER_ID));        
        assertFalse(securityManager.isDomainMember(ISecurityManager.ORPHANS_DOMAIN_ID, new Long(-99)));
    }

    private IPermitManagerDao permitManagerDao;
    private ISecurityManager securityManager;
    private IUserService userService;
    private IRoleManager roleManager;
    private IOrganisationDao organisationManagerDao;
}