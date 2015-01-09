package com.zynap.exception;


/**
 * Created by IntelliJ IDEA.
 * User: ssoong
 * Date: 05-Apr-2004
 * Time: 11:12:28
 * Exception that indicates that a the value of a field being validated was not long enough.
 */
public class MinLengthRuleException extends TalentStudioException {

    public MinLengthRuleException(String offender) {
        super(null, offender, null);
    }

}
