package com.zynap.domain.admin;

/**
 * User: amark
 * Date: 11-Mar-2005
 * Time: 09:24:17
 */

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class LoginInfoTest {

    @Test
    public void testNoLoginChange() throws Exception {

        // check that a new login info with no username/pwd does not require password or login change
        // - only the setUsername / setPassword fields cause these flags to be set
        LoginInfo loginInfo = new LoginInfo();
        assertFalse(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());
    }

    @Test
    public void testIsLoginChange() throws Exception {

        // check that setting username counts as a login change
        // - important as loginChange must be true for validation and encryption to be invoked
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername("fred");
        assertTrue(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());
    }

    @Test
    public void testUseSameUserName() throws Exception {

        // with a new login info (that has no id) setting the username even to the same value is still a login change
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(loginInfo.getUsername());
        assertTrue(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());

        // once the login info has an id then using the same username is not a login change
        loginInfo = new LoginInfoWrapper(0L, "username", "password");
        loginInfo.setUsername(loginInfo.getUsername());
        assertFalse(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());

        // but setting it to a different value is a login change
        loginInfo.setUsername("newusername");
        assertTrue(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());
    }

    @Test
    public void testSetNullOrEmptyUserName() throws Exception {

        // check that setting username to null counts as a login change
        // - important as loginChange must be true for validation and encryption to be invoked
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(null);
        assertTrue(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());

        // check that setting username to empty string counts as a login change
        // - important as loginChange must be true for validation and encryption to be invoked
        loginInfo = new LoginInfo();
        loginInfo.setUsername("");
        assertTrue(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());
    }

    @Test
    public void testIsPasswordChange() throws Exception {

        // check that password change and login change are true after changing password
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setPassword("pwd");
        assertTrue(loginInfo.isLoginChange());
        assertTrue(loginInfo.isPasswordChange());
    }

    @Test
    public void testSetNullOrEmptyPassword() throws Exception {

        // check that password change and login change are true after changing password to null
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setPassword(null);
        assertTrue(loginInfo.isLoginChange());
        assertTrue(loginInfo.isPasswordChange());

        // check that password change and login change are true after changing password to empty string
        loginInfo = new LoginInfo();
        loginInfo.setPassword(null);
        assertTrue(loginInfo.isLoginChange());
        assertTrue(loginInfo.isPasswordChange());
    }

    @Test
    public void testUseSamePassword() throws Exception {

        // with a new login info (that has no id) setting the password even to the same value is still a login change and a password change
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setPassword(loginInfo.getPassword());
        assertTrue(loginInfo.isLoginChange());
        assertTrue(loginInfo.isPasswordChange());

        // once the login info has an id then using the same password is not a login change and a password change
        loginInfo = new LoginInfoWrapper(0L, "username", "password");
        loginInfo.setPassword(loginInfo.getPassword());
        assertFalse(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());

        // but setting it to a different value is a login change and a password change
        loginInfo.setPassword("newpwd");
        assertTrue(loginInfo.isLoginChange());
        assertTrue(loginInfo.isPasswordChange());
    }

    @Test
    public void testEncryptedPassword() throws Exception {

        // change the password and check that the password and login change are true
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setPassword("");
        assertTrue(loginInfo.isLoginChange());
        assertTrue(loginInfo.isPasswordChange());

        // check that setting the encrypted password indicates that the password change has changed to false
        loginInfo.setEncryptedPassword("encryptedpasswd");
        assertTrue(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());

        // try the same but when editing a logininfo - ie: login info has id
        loginInfo = new LoginInfoWrapper(0L, "username", "password");
        loginInfo.setPassword(null);
        assertTrue(loginInfo.isLoginChange());
        assertTrue(loginInfo.isPasswordChange());

        loginInfo.setEncryptedPassword("encryptedpasswd");
        assertTrue(loginInfo.isLoginChange());
        assertFalse(loginInfo.isPasswordChange());
    }

    private class LoginInfoWrapper extends LoginInfo {
        public LoginInfoWrapper(Long id, String userName, String pwd) {
            setId(id);
            this.username = userName;
            this.password = pwd;
        }
    }
}
