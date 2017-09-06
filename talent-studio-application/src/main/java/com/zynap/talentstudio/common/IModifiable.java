/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IModifiable {

    /**
     * Persist.
     *
     * @param domainObject
     * @throws TalentStudioException
     */
    IDomainObject create(IDomainObject domainObject) throws TalentStudioException;

    /**
     * Update.
     *
     * @param domainObject
     * @throws TalentStudioException
     */
    void update(IDomainObject domainObject) throws TalentStudioException;

    /**
     * Delete.
     *
     * @param domainObject the primary key for the object to be deleted
     * @throws TalentStudioException
     */
    void delete(IDomainObject domainObject) throws TalentStudioException;

	/**
	 * Does a delete when there are referenced objects
	 * @param domainObject - the object being deleted
	 */
	void mergeDelete(IDomainObject domainObject);
}
