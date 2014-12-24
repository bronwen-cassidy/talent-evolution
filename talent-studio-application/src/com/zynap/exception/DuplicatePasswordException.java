package com.zynap.exception;

/**
 * Exception that indicates that the password supplied when changing a user's password has been used already.
 * User: amark
 * Date: 24-Mar-2005
 * Time: 18:24:40
 */
public class DuplicatePasswordException extends UniqueRuleException {

    public DuplicatePasswordException() {
        super("Previously used passwords cannot be reused");
    }
}
