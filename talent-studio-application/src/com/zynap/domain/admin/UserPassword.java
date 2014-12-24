/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.domain.admin;

/**
 * Domain object used mainly for change password flow
 *
 * @author  Andreas Andersson
 * @since   18/03/2004
 * @version $Revision: $
 *          $Id: $
 */
public class UserPassword extends LoginInfo {

    private Long userId;
    private String oldPassword;
    private String newPassword;
    private String newPasswordAgain;
    private boolean forceChange = false;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordAgain() {
        return newPasswordAgain;
    }

    public void setNewPasswordAgain(String newPasswordAgain) {
        this.newPasswordAgain = newPasswordAgain;
    }

    public boolean isForceChange() {
        return forceChange;
    }

    public void setForceChange(boolean forceChange) {
        this.forceChange = forceChange;
    }

    public String getLabel() {
        return null;
    }
}
