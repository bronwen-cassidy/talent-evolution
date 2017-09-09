/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common;

import net.sf.hibernate.NonUniqueObjectException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;

import org.springframework.dao.DataAccessException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class HibernateCrudAdaptor extends HibernateFinderAdaptor implements IModifiable {

    /**
     * Persists a new object.
     *
     * @param domainObject
     */
    public final IDomainObject create(IDomainObject domainObject) {
    	
        getHibernateTemplate().save(domainObject);
	    getHibernateTemplate().flush();
        return domainObject;
    }

    /**
     * Updates the given domainObject.
     *
     * @param domainObject
     */
    public void update(IDomainObject domainObject) {
        try {
            getHibernateTemplate().update(domainObject);
        } catch (DataAccessException e) {
            final Throwable throwable = e.getCause();
            if(throwable instanceof NonUniqueObjectException) {
                getHibernateTemplate().evict(domainObject);
                getHibernateTemplate().update(domainObject);
	            getHibernateTemplate().flush();
            } else throw e;
        }
    }

    /**
     * Deletes the object with the given primary key from the database.
     *
     * @param domainObject the object to be deleted
     * @throws com.zynap.exception.TalentStudioException
     */
    public void delete(IDomainObject domainObject) throws TalentStudioException {
        getHibernateTemplate().delete(domainObject);
	    getHibernateTemplate().flush();
    }

	/**
	 * Does a delete when there are referenced objects
	 *
	 * @param domainObject - the object being deleted
	 */
	@Override
	public void mergeDelete(IDomainObject domainObject) {
		getHibernateTemplate().evict(domainObject);
		getHibernateTemplate().delete(domainObject);
		getHibernateTemplate().flush();
	}
}
