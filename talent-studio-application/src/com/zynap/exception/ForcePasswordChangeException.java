/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.exception;

import com.zynap.domain.admin.LoginInfo;

/**
 * Exception which indicates that the system must force the user to change their password.
 *
 * @author Andreas Andersson
 * @see com.zynap.exception.TalentStudioException
 * @since 16/03/2004
 */
public class ForcePasswordChangeException extends PasswordException {

    public ForcePasswordChangeException(LoginInfo currentInfo) {
        super(ERROR_MESSAGE, currentInfo);
    }

    private static final String ERROR_MESSAGE = "Password must be changed before you can log in";
}

