/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.util;

import com.zynap.domain.IDomainObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Collection;

/**
 * Class that contains utility methods for IDomainObjects.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class IDomainUtils {

    /**
     * Check that a domain object is valid - returns true if it is not null and has a valid id.
     *
     * @param domainObject an IDomainObject
     * @return true or false
     * @see #isValidId
     */
    public static final boolean hasValidId(IDomainObject domainObject) {
        return domainObject != null && isValidId(domainObject.getId());
    }

    /**
     * Check that an id is valid - returns true if it is not null and not equals to {@link IDomainObject.UNASSIGNED_VALUE}.
     *
     * @param id The id
     * @return true or false
     */
    public static final boolean isValidId(Long id) {
        return id != null && !id.equals(IDomainObject.UNASSIGNED_VALUE);
    }

    /**
     * Find domain object.
     *
     * @param domainObjects
     * @param id
     * @return IDomainObject or null
     */
    public static IDomainObject findDomainObject(Collection domainObjects, final Long id) {

        return (IDomainObject) CollectionUtils.find(domainObjects, new Predicate() {
            public boolean evaluate(Object object) {
                IDomainObject iDomainObject = (IDomainObject) object;
                return id.equals(iDomainObject.getId());
            }
        });
    }
}
