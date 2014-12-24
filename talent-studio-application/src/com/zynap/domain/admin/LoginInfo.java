/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain.admin;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.Auditable;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class LoginInfo extends ZynapDomainObject implements Serializable {

    public LoginInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null) username = username.trim();
        loginChange = (id == null) || (this.username != null && !this.username.equals(username));
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null) password = password.trim();
        passwordChange = loginChange = (id == null) || (this.password != null && !this.password.equals(password));
        this.password = password;
    }

    public void setEncryptedPassword(String password) {
        this.password = password;
        passwordChange = false;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        if (repeatedPassword != null) repeatedPassword = repeatedPassword.trim();
        this.repeatedPassword = repeatedPassword;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean getForcePasswordChange() {
        return forcePasswordChange;
    }

    public void setForcePasswordChange(boolean forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumberLoginAttempts() {
        return numberLoginAttempts;
    }

    public void setNumberLoginAttempts(int numberLoginAttempts) {
        this.numberLoginAttempts = numberLoginAttempts;
    }

    public Collection<PasswordHistory> getPasswordsHistory() {
        if (this.passwordsHistory == null) this.passwordsHistory = new LinkedHashSet<PasswordHistory>();
        return this.passwordsHistory;
    }

    public void setPasswordsHistory(Collection<PasswordHistory> passwordsHistory) {
        this.passwordsHistory = passwordsHistory;
    }

    public boolean logsIn() {
        return (StringUtils.hasText(username));
    }

    /**
     * Clear the _password (sets it to null.)
     */
    public void resetPassword() {
        password = null;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getLabel() {
        return username;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("username", getUsername())
                .append("password", getPassword())
                .append("expires", getExpires())
                .append("locked", isLocked())
                .append("forcePasswordChange", getForcePasswordChange())
                .append("numberLoginAttempts", getNumberLoginAttempts())
                .toString();
    }

    public void addPasswordHistory(PasswordHistory passwordHistory) {
        passwordHistory.setLoginInfo(this);
        getPasswordsHistory().add(passwordHistory);
    }

    public boolean isLoginChange() {
        return loginChange;
    }

    public boolean isPasswordChange() {
        return passwordChange;
    }

    public void incrementLoginAttempts() {
        numberLoginAttempts++;
    }

    public void resetNumberLoginAttempts() {
        numberLoginAttempts = 0;
    }

    public LoginInfo createAuditable() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(this.username);
        loginInfo.setPassword(this.password);
        loginInfo.setLocked(locked);        
        return loginInfo;
    }

    /**
     * the number of times you have tried to login and failed
     */
    private int numberLoginAttempts;

    /**
     * persistent field.
     */
    protected String username;

    /**
     * persistent field.
     */
    protected String password;

    /**
     * nullable persistent field
     */
    private Date expires;

    /**
     * persistent field.
     */
    private boolean locked;

    /**
     * persistent field.
     */
    private boolean forcePasswordChange;

    /**
     * persistent field.
     */
    private User user;

    /**
     * persistent field.
     */
    private Collection<PasswordHistory> passwordsHistory;

    /**
     * Field used to hold repeated password - non-persistent - only here for web tier use.
     */
    private String repeatedPassword;

    /**
     * Non-persistent field that indicates if either the username or password has changed.
     * <br/> Used to ensure that new usernames / passwords get validated.
     *
     * @see com.zynap.talentstudio.security.users.LoginInfoValidator#validateLoginInfo(User,com.zynap.talentstudio.security.users.IUserDao, boolean)
     */
    private boolean loginChange;

    /**
     * Non-persistent field that indicates if the password has changed.
     * <br/> Used to ensure that new passwords get validated and encrypted.
     *
     * @see com.zynap.talentstudio.security.users.LoginInfoValidator#validateNewUserNamePasswordRules(LoginInfo, com.zynap.talentstudio.security.users.IUserDao)
     */
    private boolean passwordChange;
}
