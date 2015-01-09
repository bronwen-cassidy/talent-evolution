/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;

import java.io.Serializable;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IFinder {

    /**
     * Find by id.
     *
     * @param id the id of the object
     * @return IDomainObject the instance created by the query executed
     */
    <T> T findByID(Serializable id) throws TalentStudioException;

    /**
     * Finds all objects.
     *
     * @return Collection of {@link IDomainObject} instances
     */
    <T> List<T> findAll() throws TalentStudioException;

    /**
     * Find an object with the given identifier of given class type
     *
     * @param domainObjectClass
     * @param id
     * @return The object of type specified in parameter domainObjectClass or null if not found.
     * @throws DomainObjectNotFoundException
     */
    Object findByID(final Class domainObjectClass, Serializable id) throws TalentStudioException;
}
