package com.zynap.talentstudio.security.users;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.domain.IDomainObject;
import com.zynap.domain.UserPrincipal;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.PasswordHistory;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserPassword;
import com.zynap.domain.admin.UserSearchQuery;
import com.zynap.domain.orgbuilder.ISearchConstants;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.DuplicatePasswordException;
import com.zynap.exception.ForcePasswordChangeException;
import com.zynap.exception.LoginAttemptsExceededException;
import com.zynap.exception.TalentStudioException;
import com.zynap.exception.UserLoginFailedException;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.audit.SessionLog;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.security.roles.IRoleManagerDao;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.security.rules.Config;
import com.zynap.talentstudio.security.rules.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TestUserService extends ZynapDatabaseTestCase {

    protected String getDataSetFileName() {
        return "user-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        userService = (IUserService) applicationContext.getBean("userService");
        roleManagerDao = (IRoleManagerDao) applicationContext.getBean("roleManDao");
        userDao = (IUserDao) applicationContext.getBean("hibUserManDao");
    }

    /**
     * Test find by subject throws correct exception.
     *
     * @throws Exception
     */
    public void testFindBySubjectId() throws Exception {

        try {
            userService.findBySubjectId((long) -1);
            fail("Find should fail for nonexistent subject");
        } catch (DomainObjectNotFoundException expected) {
            assertNotNull(expected);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void testFindBySubjectIdSuccess() throws Exception {

        try {
            userService.findBySubjectId((long) -32);
        } catch (Exception e) {
            fail("Find should find subject");
        }
    }

    /**
     * Test that getting for user without subject returns default org unit.
     *
     * @throws Exception
     */
    public void testGetUserDefaultOrganisationUnit() throws Exception {

        final OrganisationUnit organisationUnit = userService.getUserDefaultOrganisationUnit("foo");
        assertEquals(DEFAULT_ORG_UNIT_ID, organisationUnit.getId());
    }

    public void testGetAppUsers() throws Exception {

        final Collection appUsers = userService.getAppUsers();

        final User administrator = userService.getUserById(ADMINISTRATOR_USER_ID);
        assertTrue(appUsers.contains(administrator));
        final User root = userService.getUserById(ROOT_USER_ID);
        assertFalse(appUsers.contains(root));
    }

    public void testFindByUserName() throws Exception {

        Long userId = ADMINISTRATOR_USER_ID;
        final User user = userService.getUserById(userId);

        final User found = userService.findByUserName(user.getUserName());
        assertEquals(user, found);
    }

    public void testGetUserById() throws Exception {

        Long userId = ADMINISTRATOR_USER_ID;
        final User user = userService.getUserById(userId);
        assertEquals(userId, user.getId());
    }

    /**
     * Do search for inactive users.
     *
     * @throws Exception
     */
    public void testSearchInactive() throws Exception {

        UserSearchQuery userSearchQuery = new UserSearchQuery();
        userSearchQuery.setActive(ISearchConstants.INACTIVE);
        final Collection results = userService.search(ADMINISTRATOR_USER_ID, userSearchQuery);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    /**
     * Test search is case insensitive.
     *
     * @throws Exception
     */
    public void testSearchCaseInsensitive() throws Exception {

        User user = buildUser();
        userService.create(user);

        UserSearchQuery userSearchQuery = new UserSearchQuery();
        userSearchQuery.setUsername("ANOTHER");
        final Collection results = userService.search(ADMINISTRATOR_USER_ID, userSearchQuery);
        assertNotNull(results);

        // check contains added user
        assertTrue(results.contains(user));
    }

    /**
     * Test that search never returns root users and never returns the user doing the search.
     *
     * @throws Exception
     */
    public void testSearch() throws Exception {

        final Long userId = ADMINISTRATOR_USER_ID;

        UserSearchQuery userSearchQuery = new UserSearchQuery();
        final Collection results = userService.search(userId, userSearchQuery);
        assertNotNull(results);

        // check no root users returned
        for (Object result : results) {
            User user = (User) result;
            assertFalse(user.isRoot());
        }

        // check does not contain user doing the search
        assertFalse(results.contains(userService.findById(userId)));
    }

    /**
     * Test search works with special characters.
     *
     * @throws Exception
     */
    public void testSearchSpecialCharacters() throws Exception {

        UserSearchQuery userSearchQuery = new UserSearchQuery();
        userSearchQuery.setFirstName("!\"�$%^&**()_-+=|\\<,>./?:;@'~#}]{[�`");
        userSearchQuery.setPrefName("!\"�$%^&**()_-+=|\\<,>./?:;@'~#}]{[�`");
        userSearchQuery.setSecondName("!\"�$%^&**()_-+=|\\<,>./?:;@'~#}]{[�`");
        userSearchQuery.setUsername("!\"�$%^&**()_-+=|\\<,>./?:;@'~#}]{[�`");
        final Collection results = userService.search(ADMINISTRATOR_USER_ID, userSearchQuery);
        assertNotNull(results);
    }

    public void testDeleteUser() throws Exception {
        IDomainObject user = userService.findById(-44L);
        try {
            userService.delete(user);
        } catch (TalentStudioException e) {
            fail("no exception expected " + e.getMessage());
        }
    }

    public void testCreate() throws Exception {
        User user = buildUser();
        Collection accessRoles = roleManagerDao.getActiveAccessRoles();
        for (Object accessRole : accessRoles) {
            Role role = (Role) accessRole;
            user.addRole(role);
        }
        userService.create(user);
        User actual = (User) userService.findById(user.getId());
        assertNotNull(actual.getLoginInfo());
        assertFalse(actual.getUserRoles().isEmpty());
        assertEquals(user.getCoreDetail().getFirstName(), actual.getCoreDetail().getFirstName());
    }

    public void testCreateUserModifyRoles() throws Exception {
        User user = buildUser();

        user.addRole(roleManagerDao.findRole((long) 1));
        userService.create(user);

        // check user password history - should still be 1
        User actual = (User) userService.findById(user.getId());
        assertEquals(1, actual.getLoginInfo().getPasswordsHistory().size());

        actual.addRole(roleManagerDao.findRole((long) 4));
        userService.update(actual);
        User real = (User) userService.findById(actual.getId());
        assertEquals(2, real.getUserRoles().size());

        // check user password history - should still be 1
        assertEquals(1, real.getLoginInfo().getPasswordsHistory().size());
    }

    /**
     * The tests scenario is create a user, login the user, change the users password at first login
     * assert passwordHistories are 2, change the users password again assert passwordHistories are 3
     *
     * @throws Exception
     */
    public void testChangePasswordPasswordHistoryCorrect() throws Exception {


        String username = "test.test";

        String oldPassword = "test.test";
        String changedPassword = "test.1234";


        User user = (User) userService.findById(-133L);
        final LoginInfo original = user.getLoginInfo();
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(original.getUsername());
        loginInfo.setPassword(original.getPassword());
        // does the basic user creation, login catch forcePasswordChange, change password

        // login the user, change password
        User created = (User) userService.findById(user.getId());

        // check user password history
        assertEquals(1, created.getLoginInfo().getPasswordsHistory().size());

        // first login
        try {
            loginInfo.setPassword(oldPassword);
            userService.logInUser(loginInfo, getMockSessionId(), "localhost");
            fail("should have thrown a forcePassword change exception");
        } catch (ForcePasswordChangeException e11) {

            User updated = (User) userService.findById(created.getId());

            // check user password history
            assertEquals(1, updated.getLoginInfo().getPasswordsHistory().size());

            UserPassword updatedUserPassword = new UserPassword();
            updatedUserPassword.setNewPassword(changedPassword);
            updatedUserPassword.setNewPasswordAgain(changedPassword);
            updatedUserPassword.setOldPassword(oldPassword);
            updatedUserPassword.setUsername(username);

            // change the password so this user can login
            userService.changePassword(updatedUserPassword);

            // check user password history - should now be 2
            assertEquals(2, updated.getLoginInfo().getPasswordsHistory().size());

            try {
                LoginInfo newLoginInfo = new LoginInfo();
                newLoginInfo.setUser(updated);
                newLoginInfo.setUsername(username);
                newLoginInfo.setPassword(changedPassword);
                userService.logInUser(newLoginInfo, getMockSessionId(), "127.0.0.1");

            } catch (TalentStudioException e1) {
                fail("we should have successfully logged in but instead got the exception " + e1.getMessage());
            }
        }

        User expected = (User) userService.findById(user.getId());

        String newPassword = "freddie";
        UserPassword userPassword = createUserPassword(expected, changedPassword, newPassword);

        try {
            userService.changePassword(userPassword);

            expected = (User) userService.findById(user.getId());
            final Collection passwordsHistory = expected.getLoginInfo().getPasswordsHistory();
            assertEquals("Expected 3 password history objects", 3, passwordsHistory.size());

            Collection<String> encryptedPasswords = new ArrayList<>();
            encryptedPasswords.add(userDao.encrypt(oldPassword));
            encryptedPasswords.add(userDao.encrypt(newPassword));
            encryptedPasswords.add(userDao.encrypt(changedPassword));

            for (Object aPasswordsHistory : passwordsHistory) {
                PasswordHistory passwordHistory = (PasswordHistory) aPasswordsHistory;
                final String passwordChanged = passwordHistory.getPasswordChanged();

                assertTrue(encryptedPasswords.contains(passwordChanged));
            }

        } catch (DuplicatePasswordException e) {
            fail("No exception expected");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Wrong exception: " + e.getMessage());
        } 
    }

    public void testLogInUserOK() throws Exception {

        UserPrincipal userPrincipal = loginRootUser();
        assertNotNull(userPrincipal);
    }


    public void testChangePasswordResetsFailedLoginAttempts() throws Exception {

        final String username = "username";
        final String oldPassword = "2HackIt";
        final String newPassword = "butUDidnt";

        CoreDetail coreDetail = new CoreDetail("Alastair", "Calderwood", "Ali", "Mr.", "acalderwood@zynap.com", "07957 499173");
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(oldPassword);
        User user = new User(loginInfo, coreDetail);
        userService.create(user);

        Config maxFailedLoginAttemptsConfig = userDao.findConfig(Config.AUTHORISATION_CONFIG_ID);
        Rule maxFailedLoginAttemptsRule = maxFailedLoginAttemptsConfig.getRule(Rule.MAX_NUMBER_FAILED_LOGIN_ATTEMPTS);
        int maxFailedLoginAttempts = Integer.parseInt(maxFailedLoginAttemptsRule.getValue());

        int attempts = 0;
        try {
            // this block throws an exception when attempts = maxFailedLoginAttempts
            for (; attempts < maxFailedLoginAttempts + 10; attempts++) {
                LoginInfoValidator.validateLoginPassword(loginInfo, userDao);
            }
            fail("Expected failure as maximum number of failed login attempts reached");
        } catch (UserLoginFailedException expected) {
            assertTrue(attempts < maxFailedLoginAttempts);

        } catch (LoginAttemptsExceededException expected) {
            // we're not testing if the number of failed login attempts is correct as that's tested in TestLoginInfoValidator
            // just making sure that it's not 0
            assert (loginInfo.getNumberLoginAttempts() > 0);
            assertEquals(true, loginInfo.isLocked());
            UserPassword userPassword = createUserPassword(user, oldPassword, newPassword);
            loginInfo = userService.changePassword(userPassword);
            // after changing the password, number of failed login attempts should be reset back to 0
            assertEquals(0, loginInfo.getNumberLoginAttempts());
            assertEquals(false, loginInfo.isLocked());
        }

    }

    public void testLogInUserSessionLogUpdated() throws Exception {

        UserPrincipal userPrincipal = loginRootUser();
        User user = (User) userService.findById((long) 0);
        Collection sessionLogs = user.getSessionLogs();
        assertFalse(sessionLogs.isEmpty());
        assertNotNull(userPrincipal);
    }

    private UserPrincipal loginRootUser() throws TalentStudioException {
        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(ROOT_USERNAME);
        loginInfo.setPassword(ROOT_PASSWORD);

        return userService.logInUser(loginInfo, getMockSessionId(), "localhost");
    }

    public void testLogInUserIncorrectPassword() throws Exception {
        final String username = ROOT_USERNAME;
        final String password = "penny";
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        try {
            userService.logInUser(loginInfo, getMockSessionId(), "localhost");
            fail("should have thrown a login failed exception");
        } catch (UserLoginFailedException e) {
            // ok expected
        } catch (Exception e) {
            fail("wrong exception should have thrown a UserLoginFailedException");
        }
    }

    public void testLogInUserForcePasswordChange() throws Exception {

        final String password = "gotvha11";
        final String username = "bandy33";
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        loginInfo.setForcePasswordChange(true);
        CoreDetail coreDetail = new CoreDetail("Mr", "Henry", "Barry");
        User user = new User(loginInfo, coreDetail);
        userService.create(user);

        // login the user should get a password change
        try {
            // create a new login info to login as the previous one now has the encrypted password
            loginInfo = new LoginInfo();
            loginInfo.setUsername(username);
            loginInfo.setPassword(password);
            userService.logInUser(loginInfo, getMockSessionId(), "localhost");
            fail("should have thrown a ForcePasswordChangeException");
        } catch (ForcePasswordChangeException e) {
            // ok expected
        } catch (Exception e) {
            e.printStackTrace();
            fail("wrong exception thrown expected ForcePasswordChangeException: " + e.getClass().getName());
        }
    }

    public void testLogInUserPasswordExpired() throws Exception {
        final String password = "gotvha11";
        final String username = "bandy33";

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);
        loginInfo.setForcePasswordChange(false);
        loginInfo.setExpires(new Date());

        CoreDetail coreDetail = new CoreDetail("Mr", "Henry", "Barry");
        User user = new User(loginInfo, coreDetail);
        userService.create(user);

        // login the user should get a password change
        try {
            // create a new login info to login as the previous one now has the encrypted password
            loginInfo = new LoginInfo();
            loginInfo.setUsername(username);
            loginInfo.setPassword(password);
            userService.logInUser(loginInfo, getMockSessionId(), "localhost");
            fail("should have thrown a PasswordExpiredException");
        } catch (ForcePasswordChangeException e) {
            // ok expected
        } catch (Exception e) {
            e.printStackTrace();
            fail("wrong exception thrown expected ForcePasswordChangeException: " + e.getClass().getName());
        }
    }

    public void testLogOffUser() throws Exception {
        final String username = "bandy333";
        final String password = "gotvha11";
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);
        loginInfo.setExpires(new Date());

        CoreDetail coreDetail = new CoreDetail("Mr", "Henry", "Barry");
        User user = new User(loginInfo, coreDetail);
        userService.create(user);
        User oldUser = (User) userService.findById(user.getId());
        oldUser.getLoginInfo().setForcePasswordChange(false);
        userService.update(oldUser);

        // create a new login info to login as the previous one now has the encrypted password
        loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        final String sessionId = getMockSessionId();
        UserPrincipal up = userService.logInUser(loginInfo, sessionId, "127.0.0.1");
        final Long id = up.getSessionLog().getId();
        userService.logOutUser(user.getId());
        SessionLog log = userService.getSessionLog(id);
        assertTrue(log.getLoggedOutDate() != null);
    }

    /**
     * Test lookup with invalid id.
     *
     * @throws Exception
     */
    public void testFindById() throws Exception {
        try {
            userService.findById((long) -99);
            fail("Incorrectly managed to find user with invalid id");
        } catch (DomainObjectNotFoundException expected) {
            assertNotNull(expected);
        }
    }

    public void testFindSystemUsers() throws Exception {
        final List<UserDTO> users = userService.findSystemUsers();
        assertNotNull(users);
        assertFalse(users.contains(new UserDTO(0L)));
        assertFalse(users.contains(new UserDTO(1L)));
        assertFalse(users.contains(new UserDTO(2L)));
    }

    private User buildUser() {
        CoreDetail coreDetail = new CoreDetail("mandy", "Grey", "Harper", "Mrs", "two@abc.com", "44444444");

        final String username = "another8";
        final String password = "22gotcha";
        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        return new User(loginInfo, coreDetail);
    }

    private String getMockSessionId() {

        long date = System.currentTimeMillis();
        String rand = String.valueOf(date);
        if (rand.length() > 200) {
            rand = rand.substring(1, 200);
        }
        return rand;
    }

    private UserPassword createUserPassword(User expected, String changedPassword, String newPassword) {
        UserPassword userPassword = new UserPassword();
        LoginInfo expectedLoginInfo = expected.getLoginInfo();
        userPassword.setEncryptedPassword(expectedLoginInfo.getPassword());
        userPassword.setOldPassword(changedPassword);

        userPassword.setNewPassword(newPassword);
        userPassword.setRepeatedPassword(newPassword);
        userPassword.setUsername(expectedLoginInfo.getUsername());
        return userPassword;
    }

    private IUserService userService;
    private IRoleManagerDao roleManagerDao;
    private IUserDao userDao;
}