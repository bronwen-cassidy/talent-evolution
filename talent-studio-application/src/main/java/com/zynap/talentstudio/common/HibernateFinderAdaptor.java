/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class HibernateFinderAdaptor extends HibernateDaoSupport implements IFinder {

    public abstract Class getDomainObjectClass();

    /**
     * Find domain object by id.
     * <br> Uses the <code>getDomainObjectClass()</code> class as the entity class.
     *
     * @param id The id of the domain object to be loaded.
     * @return IDomainObject
     * @throws com.zynap.exception.TalentStudioException if the object was not found.
     */
    public final <T> T findById(Serializable id) throws TalentStudioException {
        return findById(getDomainObjectClass(), id);
    }

    /**
     *  Find domain object by class and id.
     *
     * @param domainObjectClass The class the object corresponds to.
     * @param id The id of the domain object to be loaded.
     * @return Object
     * @throws DomainObjectNotFoundException
     */
    public final <T> T findById(final Class domainObjectClass, Serializable id) throws TalentStudioException {
        try {
            return (T) getHibernateTemplate().load(domainObjectClass, id);
        } catch (HibernateObjectRetrievalFailureException e) {
            throw new DomainObjectNotFoundException(domainObjectClass, id, e);
        } catch(DataAccessException e) {
            throw new DomainObjectNotFoundException(domainObjectClass, id, e);
        }
    }

    /**
     * Finds all objects.
     *
     * @return Collection of {@link com.zynap.domain.IDomainObject } instances
     */
    public <T> List<T> findAll() throws TalentStudioException {
        final Set<T> uniqueResults = new LinkedHashSet<T>(getHibernateTemplate().loadAll(getDomainObjectClass()));
        return new ArrayList<T>(uniqueResults);
    }
}
