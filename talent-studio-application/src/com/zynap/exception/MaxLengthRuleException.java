package com.zynap.exception;

/**
 * Created by IntelliJ IDEA.
 * User: ssoong
 * Date: 05-Apr-2004
 * Time: 11:14:48
 * Exception that indicates that a the value of a field being validated was too long.
 */
public class MaxLengthRuleException extends TalentStudioException {

    public MaxLengthRuleException(String offender) {
        super(null, offender, null);
    }

}
