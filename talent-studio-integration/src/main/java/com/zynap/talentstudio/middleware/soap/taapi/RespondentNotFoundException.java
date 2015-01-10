package com.zynap.talentstudio.middleware.soap.taapi;

import com.zynap.exception.TalentStudioException;

/**
 * Created by bronwen.
 * Date: 07/05/12
 * Time: 12:10
 */
public class RespondentNotFoundException extends TalentStudioException {

    public RespondentNotFoundException() {
    }

    public RespondentNotFoundException(Throwable cause) {
        super(cause);
    }

    public RespondentNotFoundException(String message) {
        super(message);
    }

    public RespondentNotFoundException(String message, String key) {
        super(message, key);
    }

    public RespondentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RespondentNotFoundException(String description, String offender, Throwable cause) {
        super(description, offender, cause);
    }
}
