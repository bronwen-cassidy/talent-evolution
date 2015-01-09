package com.zynap.exception;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 03-Feb-2004
 * Time: 17:00:45
 * To change this template use Options | File Templates.
 */
public class UserLoginFailedException extends TalentStudioException {

    public UserLoginFailedException() {
        super();
    }

    public UserLoginFailedException(String message) {
        super(message);
    }

}
