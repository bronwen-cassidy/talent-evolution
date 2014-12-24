package com.zynap.talentstudio.organisation.attributes;

import com.zynap.exception.TalentStudioException;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 29-Jun-2005
 * Time: 16:19:00
 * To change this template use File | Settings | File Templates.
 */
public class InvalidDynamicAttributeException extends TalentStudioException {

    public InvalidDynamicAttributeException() {
    }

    public InvalidDynamicAttributeException(Throwable cause) {
        super(cause);
    }

    public InvalidDynamicAttributeException(String message) {
        super(message);
    }

    public InvalidDynamicAttributeException(String message, String key) {
        super(message, key);
    }

    public InvalidDynamicAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDynamicAttributeException(String description, String offender, Throwable cause) {
        super(description, offender, cause);
    }
}
