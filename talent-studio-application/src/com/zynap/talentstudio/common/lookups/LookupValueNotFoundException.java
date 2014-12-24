/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common.lookups;

import com.zynap.exception.DomainObjectNotFoundException;

import java.io.Serializable;

/**
 * Thrown when a LookupValue is not found.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class LookupValueNotFoundException extends DomainObjectNotFoundException {

    /**
     * Constructor.
     *
     * @param id The id of the domain object not found
     * @param cause The exception
     */
    public LookupValueNotFoundException(Serializable id, Throwable cause) {
        super(LookupValue.class, id, cause);
    }
}