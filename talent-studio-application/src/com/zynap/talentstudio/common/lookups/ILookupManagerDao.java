/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common.lookups;

import com.zynap.exception.TalentStudioException;

import java.util.Collection;
import java.util.List;


/**
 * Interface to be implemented by any class that can read up
 * existing lookup data
 *
 * @author Andreas Andersson
 * @since 02/03/2004
 */
public interface ILookupManagerDao {

    /**
     * Get all modifiable lookup types. (lookup types which are not marked as system lookup types.)
     * <br> Includes inactive ones.
     *
     * @return List containing {@link com.zynap.talentstudio.common.lookups.LookupType} objects
     */
    List findEditableLookupTypes();

    /**
     * Get all active lookup types.
     *
     * @return List containing {@link com.zynap.talentstudio.common.lookups.LookupType} objects
     */
    public List findActiveLookupTypes();

    /**
     * Get lookup type.
     * <br> Also returns the lookup values (including the inactive ones.)
     *
     * @param id The id
     * @return LookupType A {@link com.zynap.talentstudio.common.lookups.LookupType} object
     * @throws TalentStudioException
     */
    public LookupType findLookupType(String id) throws TalentStudioException;

    /**
     * Get the active lookup values for the specified lookup type.
     *
     * @param lookupTypeId The look up type id
     * @return List containing {@link com.zynap.talentstudio.common.lookups.LookupValue} objects
     */
    List<LookupValue> findActiveLookupValues(String lookupTypeId);

    /**
     * Add a lookup type.
     *
     * @param principalId User adding type
     * @param lookupType The actual lookup
     * @throws com.zynap.exception.TalentStudioException Thrown if a duplication of names for a type is found
     */
    void createLookupType(Long principalId, LookupType lookupType) throws TalentStudioException;

    /**
     * Update a lookup type.
     *
     * @param principalId
     * @param lookupType
     * @throws TalentStudioException
     */
    void updateLookupType(Long principalId, LookupType lookupType) throws TalentStudioException;

    /**
     * Adds a lookup value.
     *
     * @param principalId
     * @param lookupValue
     * @throws TalentStudioException
     */
    void createLookupValue(Long principalId, LookupValue lookupValue) throws TalentStudioException;

    /**
     * Find a lookup value.
     * <br> Does not matter if lookup value is active or not.
     *
     * @param id
     * @return LookupValue
     * @throws TalentStudioException
     */
    LookupValue findLookupValue(Long id) throws TalentStudioException;

    /**
     * Update a lookup value.
     *
     * @param principalId
     * @param lookupValue
     */
    void updateLookupValue(Long principalId, LookupValue lookupValue) throws TalentStudioException;

    /**
     * Deletes a lookup value.
     *
     * @param id
     * @throws TalentStudioException
     */
    void deleteLookupValue(Long id) throws TalentStudioException;

    /**
     * Find lookup value by type and value id.
     *
     * @param lookupValueId
     * @param lookupTypeId
     * @return LookupValue
     * @throws TalentStudioException
     */
    LookupValue findLookupValue(String lookupValueId, String lookupTypeId) throws TalentStudioException;

    /**
     * Delete all lookup types within the collection.
     *
     * @param deleteables
     * @throws TalentStudioException
     */
    void deleteLookupTypes(Collection deleteables) throws TalentStudioException;
}
