package com.zynap.talentstudio.web.common.exceptions;

import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;

import javax.servlet.http.HttpSession;

/**
 * User: amark
 * Date: 05-May-2006
 * Time: 12:26:47
 *
 * Exception that indicates that a controller received an invalid request.
 */
public final class InvalidSubmitException extends TalentStudioRuntimeException {

    public InvalidSubmitException(HttpSession session, Object command, String currentURI, boolean formSubmission, String controllerClassName) {
        super("Invalid submit for: " + currentURI
                + "\n, form submission: " + formSubmission
                + "\n, controller: " + controllerClassName
                + "\n, command: " + (command != null ? command.getClass().getName() : "null")
                + "\n, session: " + (session != null ? session.getId() + ", new: " + session.isNew() : "null"));
    }
}
