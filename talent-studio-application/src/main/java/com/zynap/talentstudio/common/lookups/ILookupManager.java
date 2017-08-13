/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common.lookups;

import com.zynap.exception.TalentStudioException;

import java.util.List;

/**
 * Interface to be implemented by any class that provides logic to retrieve lookup data.
 *
 * @author Andreas Andersson
 * @since 05/04/2004
 */
public interface ILookupManager {


    String LOOKUP_TYPE_TITLE = "TITLE";
    String LOOKUP_TYPE_CURRENCY = "CURRENCY";
    String LOOKUP_TYPE_DA = "DATYPE";
    String LOOKUP_TYPE_CLASSIFICATION = "CLASSIFICATION";
    String LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC = "POSITIONSUBJECTASSOC";
    String LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC = "SECONDARYSUBJECTPOSASSOC";
    String LOOKUP_TYPE_PRIMARY_POSITION_ASSOC = "PRIMARY";
    String LOOKUP_TYPE_SECONDARY_POSITION_ASSOC = "SECONDARY";

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
     */
    public LookupType findLookupType(String id) throws TalentStudioException;

    /**
     * Get the active lookup values for the specified lookup type.
     *
     * @param lookupTypeId The look up type id
     * @return List containing {@link com.zynap.talentstudio.common.lookups.LookupValue} objects
     */
    List findActiveLookupValues(String lookupTypeId);

    /**
     * Add a lookup type.
     *
     * @param principalId User adding type
     * @param lookupType The actual lookup
     * @throws TalentStudioException Thrown if a duplication of names for a type is found
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
     */
    LookupValue findLookupValue(Long id) throws TalentStudioException;


    /**
     * Find a lookup value.
     * <br> Does not matter if lookup value is active or not.
     *
     * @param lookupTypeId
     * @param lookupValueId
     * @return LookupValue
     */

    LookupValue findLookupValue(String lookupTypeId, String lookupValueId) throws TalentStudioException;

    /**
     * Find a lookup value matching a subject association type.
     * <br> .
     *
     * @param type
     * @return LookupValue
     */
    LookupValue findSubjectAssociationQualifier(String type) throws TalentStudioException;


    /**
     * Find a lookup value matching a subject association type.
     * <br> .
     *
     * @param type
     * @return LookupValue
     */
    LookupValue findPositionAssociationQualifier(String type) throws TalentStudioException;

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
}
