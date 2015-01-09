/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;

import com.zynap.exception.TalentStudioException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class NoSubjectForUserException extends TalentStudioException {

    public NoSubjectForUserException() {
    }

    public NoSubjectForUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSubjectForUserException(Throwable cause) {
        super(cause);
    }

    public NoSubjectForUserException(String message) {
        super(message);
    }

    public NoSubjectForUserException(String message, String key) {
        super(message, key);
    }

    public NoSubjectForUserException(String description, String offender, Throwable cause) {
        super(description, offender, cause);
    }
}
