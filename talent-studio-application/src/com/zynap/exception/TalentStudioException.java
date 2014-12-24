package com.zynap.exception;

/**
 * Created by IntelliJ IDEA.
 * User: aandersson
 * Date: 03-Feb-2004
 * Time: 17:03:11
 * To change this template use Options | File Templates.
 */
public class TalentStudioException extends Exception {

    public TalentStudioException() {
    }

    public TalentStudioException(Throwable cause) {
        t = cause;
    }

    public TalentStudioException(String message) {
        super(message);
    }

    public TalentStudioException(String message, String key) {
        super(message);
        this.key = key;
    }

    public TalentStudioException(String message, Throwable cause) {
        super(message);
        t = cause;
    }    

    public TalentStudioException(String description, String offender, Throwable cause) {
        super(description, cause);
        this.offender = offender;
    }

    public Throwable getCause() {
        return t;
    }

    public String getOffender() {
        return offender;
    }

    public String getKey() {
        return key;
    }

    private String offender;
    private Throwable t;
    private String key;
}
