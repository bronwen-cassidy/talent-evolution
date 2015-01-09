package com.zynap.exception;



/**
 * Created by IntelliJ IDEA.
 * User: ssoong
 * Date: 05-Apr-2004
 * Time: 11:16:01
 * Exception that indicates that a the value of a field being validated contained non-alphabetical characters.
 */
public class AlphaOnlyRuleException extends TalentStudioException {

    public AlphaOnlyRuleException(String offender) {
        super(null, offender, null);
    }

}
