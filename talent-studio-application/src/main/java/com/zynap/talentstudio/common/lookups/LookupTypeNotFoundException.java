/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common.lookups;

import com.zynap.exception.DomainObjectNotFoundException;

import java.io.Serializable;

/**
 * Thrown when a LookupType is not found.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class LookupTypeNotFoundException extends DomainObjectNotFoundException {

    /**
     * Constructor.
     *
     * @param id The id of the domain object not found
     * @param cause The exception
     */
    public LookupTypeNotFoundException(Serializable id, Throwable cause) {
        super(LookupType.class, id, cause);
    }


    public LookupTypeNotFoundException(Class domainObjectClass, Serializable id) {
        super(domainObjectClass, id);
    }
}