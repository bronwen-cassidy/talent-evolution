/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.security.users;

import com.zynap.exception.TalentStudioException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-Jun-2007 12:58:00
 */
public class DuplicateUsernameException extends TalentStudioException {


    public DuplicateUsernameException(String message) {
        super(message);
    }

    public DuplicateUsernameException(String message, String key) {
        super(message, key);
    }

    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateUsernameException(String description, String offender, Throwable cause) {
        super(description, offender, cause);
    }
}
