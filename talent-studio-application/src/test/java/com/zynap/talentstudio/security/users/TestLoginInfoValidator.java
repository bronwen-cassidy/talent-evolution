package com.zynap.talentstudio.security.users;

/**
 * User: amark
 * Date: 27-Sep-2005
 * Time: 14:38:15
 */

import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.PasswordHistory;
import com.zynap.domain.admin.User;
import com.zynap.exception.AlphaOnlyRuleException;
import com.zynap.exception.DuplicatePasswordException;
import com.zynap.exception.ForcePasswordChangeException;
import com.zynap.exception.InvalidPasswordException;
import com.zynap.exception.LoginAttemptsExceededException;
import com.zynap.exception.MaxLengthRuleException;
import com.zynap.exception.MinLengthRuleException;
import com.zynap.exception.PasswordExpiredException;
import com.zynap.exception.TalentStudioException;
import com.zynap.exception.UserLoginFailedException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.security.rules.Config;
import com.zynap.talentstudio.security.rules.IConfigRuleService;
import com.zynap.talentstudio.security.rules.Rule;

import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.Collection;

public class TestLoginInfoValidator extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        userDao = (IUserDao) applicationContext.getBean("hibUserManDao");
        configRuleService = (IConfigRuleService) applicationContext.getBean("configRuleService");
        userService = (IUserService) applicationContext.getBean("userService");
    }

    public void testValidateLoginInfo_DuplicateUsername() throws Exception {
        // create and add a user to the database
        User user = createUser("testtest", "testpaswd");
        LoginInfo loginInfo = user.getLoginInfo();
        // create another user using the same username
        LoginInfo newLoginInfo = new LoginInfo();
        newLoginInfo.setPassword("asderwfdd");
        newLoginInfo.setUsername(loginInfo.getUsername());
        User newuser = new User(newLoginInfo, user.getCoreDetail());
        try {
            LoginInfoValidator.validateLoginInfo(newuser, userDao, true);
            fail("Should have thrown DuplicateUserNameException");
        } catch (TalentStudioException e) {
            assertTrue(e instanceof DuplicateUsernameException);
        }
    }

    public void testValidateChangePasswordUniquePasswordRuleViolation() throws Exception {

        final String oldPassword = "oldpwd";
        final String encryptedOldPassword = userDao.encrypt(oldPassword);

        // create login info with password history
        final LoginInfo loginInfo = createLoginInfo(oldPassword);
        loginInfo.addPasswordHistory(new PasswordHistory(encryptedOldPassword, loginInfo));

        // set unique password count to 2 - should disallow reuse of password
        final Rule passwdRule = getRule(Config.PASSWORD_CONFIG_ID, Rule.PASSWORD_NUMBER_OF_UNIQUE);
        passwdRule.setValue("2");

        try {
            // attempt to reuse the old password again
            LoginInfoValidator.validateChangePassword(oldPassword, oldPassword, loginInfo, userDao);
            fail("Expected failure as duplicate password was used");
        } catch (DuplicatePasswordException expected) {
        }

        // now add a PasswordHistory entry for a second password
        final String newPassword = "newpwd";
        final String encryptedNewPassword = userDao.encrypt(newPassword);
        loginInfo.addPasswordHistory(new PasswordHistory(encryptedNewPassword, loginInfo));

        try {
            // attempt to use the old password again - will still fail
            LoginInfoValidator.validateChangePassword(oldPassword, oldPassword, loginInfo, userDao);
            fail("Expected failure as duplicate password was used");
        } catch (DuplicatePasswordException expected) {
        }

        try {
            // attempt to use the new password again - should fail as it is already in the password history
            LoginInfoValidator.validateChangePassword(newPassword, oldPassword, loginInfo, userDao);
            fail("Expected failure as duplicate password was used");
        } catch (DuplicatePasswordException expected) {
        }

        // use a third password - should succeed
        final String otherPassword = "3rdpasswd";
        LoginInfoValidator.validateChangePassword(otherPassword, oldPassword, loginInfo, userDao);
    }

    public void testValidateChangePasswordCyclePasswords() throws Exception {

        final String newPassword = "newpwd";

        final String oldPassword = "oldpwd";
        final String encryptedOldPassword = userDao.encrypt(oldPassword);

        // create login info with password history
        final LoginInfo loginInfo = createLoginInfo(oldPassword);
        loginInfo.addPasswordHistory(new PasswordHistory(encryptedOldPassword, loginInfo));

        // set unique password count to 1 - should disallow reuse of password
        final Rule passwdRule = getRule(Config.PASSWORD_CONFIG_ID, Rule.PASSWORD_NUMBER_OF_UNIQUE);
        passwdRule.setValue("1");

        // attempt to reuse old password again should fail
        try {
            LoginInfoValidator.validateChangePassword(oldPassword, oldPassword, loginInfo, userDao);
            fail("Expected failure as duplicate password was used");
        } catch (DuplicatePasswordException expected) {
        }

        // try with new password - add to login info
        final PasswordHistory newPasswordHistory = LoginInfoValidator.validateChangePassword(newPassword, oldPassword, loginInfo, userDao);
        loginInfo.addPasswordHistory(newPasswordHistory);

        // loop trying new and old passwords in alternate order
        for (int i = 0; i < 7; i++) {
            final String temp = (i % 2 == 0) ? oldPassword : newPassword;
            final PasswordHistory passwordHistory = LoginInfoValidator.validateChangePassword(temp, oldPassword, loginInfo, userDao);
            loginInfo.addPasswordHistory(passwordHistory);
        }
    }

    public void testValidateChangePasswordCheckOldPassword() throws Exception {

        final LoginInfo loginInfo = new LoginInfo();
        loginInfo.setPassword("oldpassword");

        final String newPassword = "newpassword";
        final String oldPassword = "oldpassword2";

        try {
            LoginInfoValidator.validateChangePassword(newPassword, oldPassword, loginInfo, userDao);
            fail("Expected failure as incorrect old password was provided");
        } catch (InvalidPasswordException expected) {
        }
    }

    public void testValidateChangePasswordMinLength() throws Exception {

        final Rule rule = getRule(Config.PASSWORD_CONFIG_ID, Rule.PASSWORD_MIN_LENGTH);

        final int minLength = Integer.parseInt(rule.getValue());
        final String newPassword = StringUtils.repeat("a", minLength - 1);
        final String oldPassword = "oldpassword";

        final LoginInfo loginInfo = createLoginInfo(oldPassword);

        try {
            LoginInfoValidator.validateChangePassword(newPassword, oldPassword, loginInfo, userDao);
            fail("Expected failure as password is too short");
        } catch (MinLengthRuleException expected) {
        }
    }

    public void testValidateChangePasswordMaxLength() throws Exception {

        final Rule rule = getRule(Config.PASSWORD_CONFIG_ID, Rule.PASSWORD_MAX_LENGTH);

        final int maxLength = Integer.parseInt(rule.getValue());
        final String newPassword = StringUtils.repeat("a", maxLength + 1);
        final String oldPassword = "oldpassword";

        final LoginInfo loginInfo = createLoginInfo(oldPassword);

        try {
            LoginInfoValidator.validateChangePassword(newPassword, oldPassword, loginInfo, userDao);
            fail("Expected failure as password is too long");
        } catch (MaxLengthRuleException expected) {
        }
    }

    public void testValidateChangePasswordAlphaNumeric() throws Exception {

        // enable alpha only rule
        final Config config = configRuleService.findById(Config.PASSWORD_CONFIG_ID);
        final Rule rule = config.getRule(Rule.PASSWORD_ALPHA_ONLY);
        rule.setValue("T");
        configRuleService.update(config);

        final String newPassword = StringUtils.repeat("-", 6);
        final String oldPassword = "oldpassword";

        final LoginInfo loginInfo = createLoginInfo(oldPassword);

        try {
            LoginInfoValidator.validateChangePassword(newPassword, oldPassword, loginInfo, userDao);
            fail("Expected failure as password is not alpha only");
        } catch (AlphaOnlyRuleException expected) {
        }
    }

    public void testValidateChangePasswordAlphaNumericOk() throws Exception {

        // enable alpha only rule
        final Config config = configRuleService.findById(Config.PASSWORD_CONFIG_ID);
        final Rule rule = config.getRule(Rule.PASSWORD_ALPHA_ONLY);
        rule.setValue("T");
        configRuleService.update(config);

        final String newPassword = StringUtils.repeat("1", 6);
        final String oldPassword = "oldpassword";

        final LoginInfo loginInfo = createLoginInfo(oldPassword);
        LoginInfoValidator.validateChangePassword(newPassword, oldPassword, loginInfo, userDao);
    }

    public void testValidateChangePassword() throws Exception {

        final String newPassword = "newpwd";
        final String oldPassword = "oldpwd";

        final LoginInfo loginInfo = createLoginInfo(oldPassword);
        final PasswordHistory passwordHistory = LoginInfoValidator.validateChangePassword(newPassword, oldPassword, loginInfo, userDao);
        assertSame(loginInfo, passwordHistory.getLoginInfo());

        final String newEncryptedPassword = userDao.encrypt(newPassword);
        final Collection passwordsHistory = loginInfo.getPasswordsHistory();
        assertEquals(0, passwordsHistory.size());
        assertEquals(newEncryptedPassword, passwordHistory.getPasswordChanged());
    }

    public void testValidateLoginPasswordInvalidUser() throws Exception {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername("username");
        loginInfo.setPassword("password");

        try {
            LoginInfoValidator.validateLoginPassword(loginInfo, userDao);
            fail("Expected failure as user does not exist");
        } catch (UserLoginFailedException expected) {
        }
    }

    public void testValidateMaxLoginPasswordMaxLoginAttempts() throws Exception {

        String username = "username";
        String password = "password";

        User user = createUser(username, password);

        // set force pwd change to skip this validation
        LoginInfo loginInfo = user.getLoginInfo();

        Config maxFailedLoginAttemptsConfig = userDao.findConfig(Config.AUTHORISATION_CONFIG_ID);
        Rule maxFailedLoginAttemptsRule = maxFailedLoginAttemptsConfig.getRule(Rule.MAX_NUMBER_FAILED_LOGIN_ATTEMPTS);
        //VARCHAR in DB but already validated as int before being committed, so no need to catch NumberFormatException
        int maxFailedLoginAttempts = Integer.parseInt(maxFailedLoginAttemptsRule.getValue());
        int attempts = 1;

        //block should throw exception when attempts = maxFailedLoginAttempts
        for (; attempts < maxFailedLoginAttempts + 10; attempts++) {
            loginInfo.setUsername("username");
            loginInfo.setPassword("HacKerzR00l");

            try {
                loginInfo = LoginInfoValidator.validateLoginPassword(loginInfo, userDao);
                fail("Should have failed login");
            } catch (UserLoginFailedException expected) {
                assertTrue(attempts < maxFailedLoginAttempts);
            } catch (LoginAttemptsExceededException expected) {
                //test that number of user login attempts equals number set by administrator
                assertTrue(attempts >= maxFailedLoginAttempts);                
                assertEquals(true, loginInfo.isLocked());
            }
        }
    }

    public void testValidateLoginPasswordForceChange() throws Exception {

        final String username = "username";
        final String password = "password";

        final User user = createUser(username, password);

        // new users are required to change pwd on first login
        final LoginInfo currentLoginInfo = user.getLoginInfo();
        assertTrue(currentLoginInfo.getForcePasswordChange());

        // create new LoginInfo as password has been encrypted on the original one
        LoginInfo newLoginInfo = new LoginInfo();
        try {
            newLoginInfo.setUsername(username);
            newLoginInfo.setPassword(password);

            LoginInfoValidator.validateLoginPassword(newLoginInfo, userDao);
            fail("Expected failure as user must change their password");
        } catch (ForcePasswordChangeException expected) {

            // force password change is not reset until the user changes their password
            assertTrue(currentLoginInfo.getForcePasswordChange());
        }
    }

    public void testValidateLoginPasswordExpiryDate() throws Exception {

        final String username = "username";
        final String password = "password";

        final User user = createUser(username, password);

        // set force pwd change to skip this validation
        final LoginInfo currentLoginInfo = user.getLoginInfo();
        currentLoginInfo.setForcePasswordChange(false);

        // set expiry date to current date to check that the fact that the password has expired results in the exception
        Calendar calendar = Calendar.getInstance();
        currentLoginInfo.setExpires(calendar.getTime());

        try {

            // create new LoginInfo as password has been encrypted on the original one
            LoginInfo newLoginInfo = new LoginInfo();
            newLoginInfo.setUsername(username);
            newLoginInfo.setPassword(password);

            LoginInfoValidator.validateLoginPassword(newLoginInfo, userDao);
            fail("Expected failure as user's password has expired");
        } catch (PasswordExpiredException expected) {
            // check that expiry date has been set to next date (should be old date + interval held in password expiry rule)
            final Rule passwordExpiresRule = getRule(Config.PASSWORD_CONFIG_ID, Rule.PASSWORD_EXPIRED_RULE);
            calendar.add(Calendar.DATE, Integer.parseInt(passwordExpiresRule.getValue()));
            assertEquals(calendar.getTime(), currentLoginInfo.getExpires());
        }
    }

    public void testValidateLoginPassword() throws Exception {

        final String username = "username";
        final String password = "password";

        final User user = createUser(username, password);

        // set force pwd change to skip this validation
        final LoginInfo currentLoginInfo = user.getLoginInfo();
        currentLoginInfo.setForcePasswordChange(false);

        // create new LoginInfo as password has been encrypted on the original one
        LoginInfo newLoginInfo = new LoginInfo();
        newLoginInfo.setUsername(username);
        newLoginInfo.setPassword(password);

        final LoginInfo encryptedLoginInfo = LoginInfoValidator.validateLoginPassword(newLoginInfo, userDao);
        assertSame(currentLoginInfo, encryptedLoginInfo);
        assertEquals(userDao.encrypt(password), encryptedLoginInfo.getPassword());
        assertNotNull(encryptedLoginInfo.getExpires());
        assertFalse(encryptedLoginInfo.getForcePasswordChange());
    }

    public void testValidateLoginInfoOnEdit() throws Exception {

        final String username = "username";
        final String password = "password";

        final User user = createUser(username, password);

        // passwd should now be encrypted
        final Collection passwordsHistory = user.getLoginInfo().getPasswordsHistory();
        assertEquals(1, passwordsHistory.size());
        assertEquals(userDao.encrypt(password), ((PasswordHistory) passwordsHistory.iterator().next()).getPasswordChanged());

        LoginInfoValidator.validateLoginInfo(user, userDao, false);
        assertEquals(1, passwordsHistory.size());

        // passwd should now be encrypted
        assertEquals(userDao.encrypt(password), user.getLoginInfo().getPassword());
    }

    public void testValidateLoginInfoOnCreate() throws Exception {

        final String username = "username";
        final String password = "password";

        final User user = createUser(username, password);

        // passwd should now be encrypted
        final Collection passwordsHistory = user.getLoginInfo().getPasswordsHistory();
        assertEquals(1, passwordsHistory.size());
        assertEquals(userDao.encrypt(password), ((PasswordHistory) passwordsHistory.iterator().next()).getPasswordChanged());

        // passwd should now be encrypted
        assertEquals(userDao.encrypt(password), user.getLoginInfo().getPassword());
    }

    public void testValidateLoginInfoUsernameMinLength() throws Exception {

        final String username = "username";
        final String password = "password";

        final User user = createUser(username, password);

        // change user name
        final Rule rule = getRule(Config.USERNAME_CONFIG_ID, Rule.USERNAME_MIN_LENGTH);
        int minLength = Integer.parseInt(rule.getValue());
        final String newUsername = StringUtils.repeat("a", minLength - 1);
        user.getLoginInfo().setUsername(newUsername);

        try {
            LoginInfoValidator.validateLoginInfo(user, userDao, false);
            fail("Expected failure as username is too short");
        } catch (MinLengthRuleException expected) {
        }
    }

    public void testValidateLoginInfoUsernameMaxLength() throws Exception {

        final String username = "username";
        final String password = "password";

        final User user = createUser(username, password);

        // change user name
        final Rule rule = getRule(Config.USERNAME_CONFIG_ID, Rule.USERNAME_MAX_LENGTH);
        int maxLength = Integer.parseInt(rule.getValue());
        final String newUsername = StringUtils.repeat("a", maxLength + 1);
        user.getLoginInfo().setUsername(newUsername);

        try {
            LoginInfoValidator.validateLoginInfo(user, userDao, false);
            fail("Expected failure as username is too long");
        } catch (MaxLengthRuleException expected) {
        }
    }

    public void testValidateLoginInfoUsernameAlphaNumeric() throws Exception {

        final String username = "username";
        final String password = "password";

        final User user = createUser(username, password);

        // test a series of valid usernames
        final Rule rule = getRule(Config.USERNAME_CONFIG_ID, Rule.USERNAME_ALPHA_ONLY);
        rule.setValue("T");

        String[] userNames = new String[]{"fredfr", "111111", "11fred1", "@,_._.@", "11@,_.11", "1@,.1fr"};
        for (int i = 0; i < userNames.length; i++) {

            String userName = userNames[i];
            user.getLoginInfo().setUsername(userName);

            LoginInfoValidator.validateLoginInfo(user, userDao, false);
        }
    }

    public void testValidateLoginInfoInvalidUsername() throws Exception {

        final String username = "username";
        final String password = "password";

        final User user = createUser(username, password);

        // test a series of valid usernames
        final Rule rule = getRule(Config.USERNAME_CONFIG_ID, Rule.USERNAME_ALPHA_ONLY);
        rule.setValue("T");

        String[] userNames = new String[]{"fred^", "11(111", "abd&\"q", "@$_.f.@", "11@{_.11", "1@,.1+r"};
        for (int i = 0; i < userNames.length; i++) {

            String userName = userNames[i];
            user.getLoginInfo().setUsername(userName);

            try {
                LoginInfoValidator.validateLoginInfo(user, userDao, false);
                fail("Expected failure as username must be alpha numberic");
            } catch (AlphaOnlyRuleException expected) {
            }
        }
    }

    private User createUser(final String username, final String password) throws TalentStudioException {
        CoreDetail coreDetail = new CoreDetail("Angus", "Mark", "Gus", "Mr", "afm@zynap.com", "");

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);

        // create a new user
        User user = new User(loginInfo, coreDetail);
        userService.create(user);

        return user;
    }

    private Rule getRule(Long passwordConfigId, Long ruleId) {
        Config passwordConfig = userDao.findConfig(passwordConfigId);

        return passwordConfig.getRule(ruleId);
    }

    private LoginInfo createLoginInfo(final String oldPassword) {
        final LoginInfo loginInfo = new LoginInfo();
        final String oldEncryptedPassword = userDao.encrypt(oldPassword);
        loginInfo.setPassword(oldEncryptedPassword);

        return loginInfo;
    }

    private IUserDao userDao;

    private IConfigRuleService configRuleService;

    private IUserService userService;
}