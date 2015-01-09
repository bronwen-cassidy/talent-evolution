/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.exception;

import com.zynap.domain.admin.LoginInfo;

/**
 * Exception which indicates that a user tried to log in with a password that has expired and must be changed
 *
 * @author  Andreas Andersson
 * @since   16/03/2004
 * @see com.zynap.exception.TalentStudioException
 */
public class PasswordExpiredException extends PasswordException {

    public PasswordExpiredException(LoginInfo currentInfo) {
        super(ERROR_MESSAGE, currentInfo);
    }

    private static final String ERROR_MESSAGE = "Password has expired";
}
