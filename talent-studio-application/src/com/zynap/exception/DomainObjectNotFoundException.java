package com.zynap.exception;

import java.io.Serializable;


/**
 * User: amark
 * Date: 14-Mar-2005
 * Time: 14:09:34
 * Exception that indicates that a domain object could not be found.
 */
public class DomainObjectNotFoundException extends TalentStudioException {

    public DomainObjectNotFoundException(Class domainObjectClass, Serializable id) {
        this(domainObjectClass, id, null);
    }

    /**
     * Constructor.
     *
     * @param domainObjectClass The Class of the domain object not found
     * @param id The id of the domain object not found
     * @param cause The exception
     */
    public DomainObjectNotFoundException(Class domainObjectClass, Serializable id, Throwable cause) {
        super("Failed to find instance of " + domainObjectClass + " with id: " + id, cause);
        this.id = id;
    }

    public DomainObjectNotFoundException(Class domainObjectClass, String field, Serializable value) {
        super("Failed to find instance of " + domainObjectClass + " with " + field + ": " + value);
        this.id = value;
    }

    public Serializable getId() {
        return id;
    }

    private Serializable id;
}
