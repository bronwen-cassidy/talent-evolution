package com.zynap.exception;


/**
 * Exception that indicates that a duplicate value for a field was supplied.
 *
 * User: ssoong
 * Date: 05-Apr-2004
 * Time: 11:06:36
 */
public class UniqueRuleException extends TalentStudioException {

    public UniqueRuleException(String message) {
        super(message);
    }

    public UniqueRuleException(String description, String offender, Throwable cause) {
        super(description, offender, cause);
    }
}
