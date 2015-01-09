/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain.admin;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class PasswordHistory implements Serializable {

    public PasswordHistory() {
    }

    public PasswordHistory(String passwordChanged, LoginInfo loginInfo) {
        this.passwordChanged = passwordChanged;
        this.loginInfo = loginInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(String passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("passwordChanged", getPasswordChanged())
                .toString();
    }

    public final boolean equals(Object object) {
        if (object.getClass() != this.getClass())
            return false;
        else {
            PasswordHistory zobject = (PasswordHistory) object;
            if (this.getId() == null || zobject.getId() == null) return (this == object);
            return (this.getId().longValue() == zobject.getId().longValue());
        }
    }

    public final int hashCode() {
        return id == null ? super.hashCode(): id.hashCode();
    }

    private Long id;
    private String passwordChanged;
    private LoginInfo loginInfo;
}
