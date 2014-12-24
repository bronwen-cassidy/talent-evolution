/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.users;


import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.PasswordHistory;
import com.zynap.domain.admin.User;
import com.zynap.domain.UserSession;
import com.zynap.domain.UserPrincipal;
import com.zynap.exception.*;
import com.zynap.talentstudio.security.rules.Config;
import com.zynap.talentstudio.security.rules.Rule;
import com.zynap.talentstudio.security.UserSessionFactory;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class LoginInfoValidator implements Serializable {

    /**
     * Check that username is not already in use.
     * <br> If the username is not in use, or belongs to the same user when editing, check that
     * the username and password are of the correct length and have the right type of characters.
     *
     * @param user    The user
     * @param userDao The IUserDao (required to load password rules)
     * @param create  If true we are adding a new user so add the password history
     * @throws TalentStudioException
     */
    public static void validateLoginInfo(User user, IUserDao userDao, boolean create) throws TalentStudioException {

        LoginInfo newLoginInfo = user.getLoginInfo();

        if (create) {
            // check for duplicates
            LoginInfo loginInfo = userDao.getLoginInfo(newLoginInfo.getUsername());
            if (loginInfo != null) throw new DuplicateUsernameException("Duplicate user name: " + newLoginInfo.getUsername());

            // validate the username and password
            validateNewUserNamePasswordRules(newLoginInfo, userDao);
            final String encryptedPassword = newLoginInfo.getPassword();
            newLoginInfo.addPasswordHistory(new PasswordHistory(encryptedPassword, newLoginInfo));

        } else {
            // if not creating only validate the new username
            if (newLoginInfo.isLoginChange()) {
                Config usernameConfig = userDao.findConfig(Config.USERNAME_CONFIG_ID);
                validateUserName(usernameConfig, newLoginInfo.getUsername());
                // check for duplicates
                LoginInfo loginInfo = userDao.getLoginInfo(newLoginInfo.getUsername());
                if (loginInfo != null && !(loginInfo.getId().equals(newLoginInfo.getId())))
                    throw new DuplicateUsernameException("Duplicate user name: " + newLoginInfo.getUsername());
            }
        }
    }

    /**
     * Takes the raw loginInfo and returns a loaded loginInfo with the password encrypted.
     * <p/>
     * This method validates the loginInfo for expiry, force changes and valid usernames
     * and passwords. It updates the loginInfo by resetting the force password change to false,
     * and resets the expiry date according to rule settings
     *
     * @param suppliedInfo
     * @return loginInfo loaded with an encrypted password
     * @throws TalentStudioException
     */
    public static LoginInfo validateLoginPassword(LoginInfo suppliedInfo, IUserDao userDao) throws TalentStudioException {

        //suppliedInfo - the LoginInfo credentials supplied
        //currentInfo - the full LoginInfo based on loginInfo
        //correctInfo - the full LoginInfo based on the user only

        LoginInfo correctInfo = userDao.getLoginInfo(suppliedInfo.getUsername());
        // check if user exists
        if (correctInfo == null) {
            throw new UserLoginFailedException("Login info not found for username " + suppliedInfo.getUsername());
        }

        if (correctInfo.isLocked()) {
            throw new LoginAttemptsExceededException("Maximum failed login attempts reached. Please contact your system administrator.");
        }

        String password = suppliedInfo.getPassword();
        String encryptedPassword = userDao.encrypt(password);

        // required for audits they need to know who is modifying
        UserSessionFactory.setUserSession(new UserSession(new UserPrincipal(correctInfo.getUser())));

        // if null indicates that user name and / or password are incorrect
        LoginInfo currentInfo = userDao.getLoginInfo(suppliedInfo.getUsername(), encryptedPassword);

        if (currentInfo == null) {

            Config maxFailedLoginAttemptsConfig = userDao.findConfig(Config.AUTHORISATION_CONFIG_ID);
            Rule maxFailedLoginAttemptsRule = maxFailedLoginAttemptsConfig.getRule(Rule.MAX_NUMBER_FAILED_LOGIN_ATTEMPTS);

            int maxFailedLoginAttempts = Integer.parseInt(maxFailedLoginAttemptsRule.getValue());

            correctInfo.incrementLoginAttempts();
            int failedLoginAttempts = correctInfo.getNumberLoginAttempts();

            if (failedLoginAttempts < maxFailedLoginAttempts) {
                userDao.update(correctInfo);
                throw new UserLoginFailedException("Login info not found for username " + correctInfo.getUsername());
            } else {
                correctInfo.setLocked(true);
                userDao.update(correctInfo);
                throw new LoginAttemptsExceededException("Maximum failed login attempts reached. Please contact your system administrator.");
            }
        }

        //
        if (currentInfo.getForcePasswordChange()) {
            throw new ForcePasswordChangeException(currentInfo);
        }

        Config passwordConfig = userDao.findConfig(Config.PASSWORD_CONFIG_ID);
        if (currentInfo.getExpires() != null) {

            Calendar loginInfoExpiresDate = Calendar.getInstance();
            loginInfoExpiresDate.setTime(currentInfo.getExpires());
            Calendar now = Calendar.getInstance();
            if (now.after(loginInfoExpiresDate)
                    || (loginInfoExpiresDate.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && loginInfoExpiresDate.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)
                    && loginInfoExpiresDate.get(Calendar.YEAR) == now.get(Calendar.YEAR))) {

                Rule passwordExpiresRule = passwordConfig.getRule(Rule.PASSWORD_EXPIRED_RULE);
                // the value is the number of days to reset the password
                loginInfoExpiresDate.add(Calendar.DATE, Integer.parseInt(passwordExpiresRule.getValue()));
                currentInfo.setExpires(loginInfoExpiresDate.getTime());
                userDao.update(currentInfo);

                throw new PasswordExpiredException(currentInfo);
            }
        }

        return currentInfo;
    }

    /**
     * Validates a password on change change password usecases, returns the passwordHistory object created.
     * <br> The old password can be left blank if the password is being changed by an administrator.
     *
     * @param newPassword
     * @param oldPassword
     * @param currentLoginInfo The current login info containing the current encrypted password and the password history
     * @return the encrypted password
     * @throws TalentStudioException
     */
    public static PasswordHistory validateChangePassword(final String newPassword, final String oldPassword,
                                                         LoginInfo currentLoginInfo, IUserDao userDao) throws TalentStudioException {

        // check old password matches
        if (StringUtils.hasText(oldPassword)) {
            String oldEncryptedPassword = userDao.encrypt(oldPassword);
            if (!(oldEncryptedPassword.equals(currentLoginInfo.getPassword()))) {
                throw new InvalidPasswordException("Old password is incorrect");
            }
        }

        // validate the password
        Config passwordConfig = userDao.findConfig(Config.PASSWORD_CONFIG_ID);
        validatePassword(passwordConfig, newPassword);

        final String newEncryptedPassword = userDao.encrypt(newPassword);

        // determine if this is a duplicate password
        boolean duplicate = false;

        int start = 0;
        Collection passwordHistories = currentLoginInfo.getPasswordsHistory();
        if (passwordHistories != null && !passwordHistories.isEmpty()) {

            // build list with only the passwords not the PasswordHistory objects
            List<String> list = new ArrayList<String>();
            for (Iterator iterator = passwordHistories.iterator(); iterator.hasNext();) {
                PasswordHistory passwordHistory = (PasswordHistory) iterator.next();
                list.add(passwordHistory.getPasswordChanged());
            }

            // find last instance of password - if returns zero or greater then we have found a duplicate
            start = list.lastIndexOf(newEncryptedPassword);
            duplicate = (start >= 0);
        }

        // if it check how many passwords have been used since - if the number is less
        if (duplicate) {
            start = (passwordHistories.size() - ++start);

            Rule numberOfUnique = passwordConfig.getRule(Rule.PASSWORD_NUMBER_OF_UNIQUE);
            int uniqueCount = Integer.parseInt(numberOfUnique.getValue());

            if (start < uniqueCount) {
                throw new DuplicatePasswordException();
            }
        }

        // all checks completed update expiry date unless it is the system user
        if (currentLoginInfo.getExpires() != null) {
            Rule passwordExpiresRule = passwordConfig.getRule(Rule.PASSWORD_EXPIRED_RULE);
            // the value is the number of days to reset the password
            Calendar loginInfoExpiresDate = Calendar.getInstance();
            loginInfoExpiresDate.add(Calendar.DATE, Integer.parseInt(passwordExpiresRule.getValue()));
            currentLoginInfo.setExpires(loginInfoExpiresDate.getTime());
        }
        return new PasswordHistory(newEncryptedPassword, currentLoginInfo);
    }

    /**
     * Validates the username and passwords on creation. Set the new
     * encryptedpassword onto the loginInfo and also set the current value of force password change
     * on the login info. Note this method is to be called on add only. The methds sets the
     * expiry date on the loginInfo
     *
     * @param loginInfo the supplied login info to validate
     * @throws TalentStudioException
     */
    private static void validateNewUserNamePasswordRules(LoginInfo loginInfo, IUserDao userDao) throws TalentStudioException {

        // nothings changed nothing to validate
        if (!loginInfo.isLoginChange()) return;

        // check user name
        Config usernameConfig = userDao.findConfig(Config.USERNAME_CONFIG_ID);
        validateUserName(usernameConfig, loginInfo.getUsername());

        Config passwordConfig = userDao.findConfig(Config.PASSWORD_CONFIG_ID);

        // is this a new user
        LoginInfo current = userDao.getLoginInfo(loginInfo.getUsername());

        if (current != null) {
            // oops we have a duplicate username problem
            throw new DuplicateUsernameException("Username already in use");

        } else {

            // we are doing add need to set some rules on the loginInfo force change
            loginInfo.setForcePasswordChange("T".equals(passwordConfig.getRule(Rule.PASSWORD_FORCE_CHANGE).getValue()));

            // expiry date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Rule passwordExpiresRule = passwordConfig.getRule(Rule.PASSWORD_EXPIRED_RULE);

            // the value is the number of days to reset the password
            calendar.add(Calendar.DATE, Integer.parseInt(passwordExpiresRule.getValue()));
            loginInfo.setExpires(calendar.getTime());
        }

        if (loginInfo.isPasswordChange()) {
            String password = loginInfo.getPassword();
            validatePassword(passwordConfig, password);
            loginInfo.setEncryptedPassword(userDao.encrypt(password));
        }
    }

    private static void validateUserName(Config usernameConfig, String username) throws TalentStudioException {

        final String attributeName = "username";

        Rule minLengthRule = usernameConfig.getRule(Rule.USERNAME_MIN_LENGTH);
        minLength(attributeName, username, minLengthRule);

        Rule maxLengthRule = usernameConfig.getRule(Rule.USERNAME_MAX_LENGTH);
        maxLength(attributeName, username, maxLengthRule);

        Rule alphaOnly = usernameConfig.getRule(Rule.USERNAME_ALPHA_ONLY);
        alphaOnly(attributeName, username, alphaOnly);
    }

    private static void validatePassword(Config passwordConfig, String newPassword) throws TalentStudioException {

        final String attributeName = "password";

        Rule minLengthRule = passwordConfig.getRule(Rule.PASSWORD_MIN_LENGTH);
        minLength(attributeName, newPassword, minLengthRule);

        Rule maxLengthRule = passwordConfig.getRule(Rule.PASSWORD_MAX_LENGTH);
        maxLength(attributeName, newPassword, maxLengthRule);

        Rule alphaOnly = passwordConfig.getRule(Rule.PASSWORD_ALPHA_ONLY);
        alphaOnly(attributeName, newPassword, alphaOnly);
    }

    private static void alphaOnly(String attributeName, String value, Rule alphaOnly) throws AlphaOnlyRuleException {
        if ("T".equals(alphaOnly.getValue())) {
            if (StringUtils.hasText(value) && !value.matches(ALPHA_PATTERN)) {
                throw new AlphaOnlyRuleException(attributeName);
            }
        }
    }

    private static void maxLength(String attributeName, String value, Rule maxLengthRule) throws MaxLengthRuleException {
        if (value.length() > Integer.parseInt(maxLengthRule.getValue())) {
            throw new MaxLengthRuleException(attributeName);
        }
    }

    private static void minLength(String attributeName, String value, Rule minLengthRule) throws MinLengthRuleException {
        if (value.length() < Integer.parseInt(minLengthRule.getValue())) {
            throw new MinLengthRuleException(attributeName);
        }
    }

    /**
     * Regexp pattern used to check that password or username only contain letters, numbers and the following characters '@' and ',' and '_' and '.'
     */
    private static final String ALPHA_PATTERN = new String("[\\w@,\\.]*");
}
