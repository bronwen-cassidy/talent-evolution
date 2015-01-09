/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common.lookups;

import com.zynap.exception.TalentStudioException;

import java.util.List;

/**
 * Implementation of ILookupManager Interface.
 *
 * @author Andreas Andersson
 * @since 05/04/2004
 */
public class LookupManager implements ILookupManager {

    public void setLookupManagerDao(ILookupManagerDao lookupManagerDao) {
        this.lookupManagerDao = lookupManagerDao;
    }

    public ILookupManagerDao getLookupManagerDao() {
        return lookupManagerDao;
    }

    /**
     * Get all modifiable lookup types. (lookup types which are not marked as system lookup types.)
     * <br> Includes inactive ones.
     *
     * @return List containing {@link LookupType} objects
     */
    public List findEditableLookupTypes() {
        return getLookupManagerDao().findEditableLookupTypes();
    }

    /**
     * Get all active lookup types.
     *
     * @return List containing {@link LookupType} objects
     */
    public List findActiveLookupTypes() {
        return getLookupManagerDao().findActiveLookupTypes();
    }

    /**
     * Get lookup type.
     * <br> Also returns the lookup values (including the inactive ones.)
     *
     * @param id The id
     * @return LookupType A {@link LookupType} object
     */
    public LookupType findLookupType(String id) throws TalentStudioException {
        return getLookupManagerDao().findLookupType(id);
    }

    /**
     * Get the active lookup values for the specified lookup type.
     *
     * @param lookupTypeId The look up type id
     * @return List containing {@link LookupValue} objects
     */
    public List findActiveLookupValues(String lookupTypeId) {
        return getLookupManagerDao().findActiveLookupValues(lookupTypeId);
    }

    /**
     * Add a lookup type.
     *
     * @param principalId User adding type
     * @param lookupType The actual lookup
     * @throws com.zynap.exception.TalentStudioException Thrown if a duplication of names for a type is found
     */
    public void createLookupType(Long principalId, LookupType lookupType) throws TalentStudioException {
        getLookupManagerDao().createLookupType(principalId, lookupType);
    }

    /**
     * Update a lookup type.
     *
     * @param principalId
     * @param lookupType
     * @throws com.zynap.exception.TalentStudioException
     */
    public void updateLookupType(Long principalId, LookupType lookupType) throws TalentStudioException {
        getLookupManagerDao().updateLookupType(principalId, lookupType);
    }

    /**
     * Adds a lookup value.
     *
     * @param principalId
     * @param lookupValue
     * @throws com.zynap.exception.TalentStudioException
     */
    public void createLookupValue(Long principalId, LookupValue lookupValue) throws TalentStudioException {
        getLookupManagerDao().createLookupValue(principalId, lookupValue);
    }

    /**
     * Find a lookup value.
     * <br> Does not matter if lookup value is active or not.
     *
     * @param id
     * @return LookupValue
     */
    public LookupValue findLookupValue(Long id) throws TalentStudioException {
        return getLookupManagerDao().findLookupValue(id);
    }

    public LookupValue findLookupValue(String lookupValueId, String lookupTypeId) throws TalentStudioException {
        return getLookupManagerDao().findLookupValue(lookupValueId, lookupTypeId);
    }

    public LookupValue findSubjectAssociationQualifier(String type) throws TalentStudioException {
        try {
            return getLookupManagerDao().findLookupValue(type, ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC);
        } catch (LookupValueNotFoundException e) {
            return getLookupManagerDao().findLookupValue(type, ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC);
        }
    }

    public LookupValue findPositionAssociationQualifier(String type) throws TalentStudioException {
        try {
            return getLookupManagerDao().findLookupValue(type, ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC);
        } catch (LookupValueNotFoundException e) {
            return getLookupManagerDao().findLookupValue(type, ILookupManager.LOOKUP_TYPE_SECONDARY_POSITION_ASSOC);
        }
    }

    /**
     * Update a lookup value.
     *
     * @param principalId
     * @param lookupValue
     */
    public void updateLookupValue(Long principalId, LookupValue lookupValue) throws TalentStudioException {
        getLookupManagerDao().updateLookupValue(principalId, lookupValue);
    }

    /**
     * Deletes a lookup value.
     *
     * @param id
     * @throws com.zynap.exception.TalentStudioException
     */
    public void deleteLookupValue(Long id) throws TalentStudioException {
        getLookupManagerDao().deleteLookupValue(id);
    }

    private ILookupManagerDao lookupManagerDao;
}
