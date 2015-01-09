/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common.lookups;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.util.IPersistenceIdGenerator;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class HibernateLookupManagerDao extends HibernateDaoSupport implements ILookupManagerDao {

    public void setPersistenceIdGenerator(IPersistenceIdGenerator persistenceIdGenerator) {
        this.persistenceIdGenerator = persistenceIdGenerator;
    }

    /**
     * Get all modifiable lookup types. (lookup types which are not marked as system lookup types.)
     * <br> Includes inactive ones.
     *
     * @return List containing {@link com.zynap.talentstudio.common.lookups.LookupType} objects
     */
    public List findEditableLookupTypes() {
        return getHibernateTemplate().findByNamedQuery(GET_EDITABLE_TYPES_QUERY);
    }

    /**
     * Get all active lookup types.
     *
     * @return List containing {@link com.zynap.talentstudio.common.lookups.LookupType} objects
     */
    public List findActiveLookupTypes() {
        return getHibernateTemplate().findByNamedQuery(GET_ACTIVE_TYPES_QUERY);
    }

    /**
     * Get lookup type.
     * <br> Also returns the lookup values (including the inactive ones.)
     *
     * @param id The id
     * @return LookupType A {@link com.zynap.talentstudio.common.lookups.LookupType} object
     * @throws TalentStudioException
     */
    public LookupType findLookupType(String id) throws TalentStudioException {
        LookupType type = (LookupType) getHibernateTemplate().get(LookupType.class, id);
        if(type == null) throw new LookupTypeNotFoundException(LookupType.class, id);
        return type;
    }

    /**
     * Get the active lookup values for the specified lookup type.
     *
     * @param lookupTypeId The look up type id
     * @return List containing {@link com.zynap.talentstudio.common.lookups.LookupValue} objects
     */
    public List<LookupValue> findActiveLookupValues(String lookupTypeId) {
        //noinspection unchecked
        return getHibernateTemplate().findByNamedQuery(GET_ACTIVE_VALUES_BY_TYPE_QUERY, new Object[]{lookupTypeId});
    }

    /**
     * Add a lookup type.
     *
     * @param principalId User adding type
     * @param lookupType  The actual lookup
     * @throws com.zynap.exception.TalentStudioException
     *          Thrown if a duplication of names for a type is found
     */
    public void createLookupType(Long principalId, LookupType lookupType) throws TalentStudioException {
        lookupType.setTypeId(persistenceIdGenerator.generateStringId(lookupType.getLabel(), "da_sq"));
        getHibernateTemplate().save(lookupType);
    }

    /**
     * Update a lookup type.
     *
     * @param principalId
     * @param lookupType
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void updateLookupType(Long principalId, LookupType lookupType) throws TalentStudioException {
        getHibernateTemplate().update(lookupType);
    }

    /**
     * Adds a lookup value.
     *
     * @param principalId
     * @param lookupValue
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void createLookupValue(Long principalId, LookupValue lookupValue) throws TalentStudioException {
        lookupValue.setValueId(persistenceIdGenerator.generateStringId(lookupValue.getLabel()));
        getHibernateTemplate().save(lookupValue);
    }

    /**
     * Find a lookup value.
     * <br> Does not matter if lookup value is active or not.
     *
     * @param id
     * @return LookupValue
     * @throws TalentStudioException
     */
    public LookupValue findLookupValue(Long id) throws TalentStudioException {
        try {
            return (LookupValue) getHibernateTemplate().load(LookupValue.class, id);
        } catch (DataAccessException e) {
            throw new LookupValueNotFoundException(id, e);
        }
    }

    /**
     * Update a lookup value.
     *
     * @param principalId
     * @param lookupValue
     */
    public void updateLookupValue(Long principalId, LookupValue lookupValue) throws TalentStudioException {
        getHibernateTemplate().update(lookupValue);
    }

    /**
     * Deletes a lookup value.
     *
     * @param id
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void deleteLookupValue(Long id) throws TalentStudioException {
        LookupValue value = findLookupValue(id);
        if (value != null) {
            getHibernateTemplate().delete(value);
        }
    }

    /**
     * Find lookup value by type and value id.
     *
     * @param lookupValueId
     * @param lookupTypeId
     * @return LookupValue
     * @throws TalentStudioException
     */
    public LookupValue findLookupValue(String lookupValueId, String lookupTypeId) throws TalentStudioException {
        final List list = getHibernateTemplate().findByNamedQuery(GET_VALUE_BY_TYPE_AND_VALUE_QUERY, new Object[]{lookupValueId, lookupTypeId});
        if (list.isEmpty()) {
            throw new LookupValueNotFoundException(lookupValueId, null);
        } else {
            return (LookupValue) list.get(0);
        }
    }

    public void deleteLookupTypes(Collection deleteables) throws TalentStudioException {
        getHibernateTemplate().deleteAll(deleteables);
    }

    private IPersistenceIdGenerator persistenceIdGenerator;

    private static final String GET_VALUE_BY_TYPE_AND_VALUE_QUERY = "findByTypeAndValue";
    private static final String GET_ACTIVE_VALUES_BY_TYPE_QUERY = "findActiveValues";
    private static final String GET_ACTIVE_TYPES_QUERY = "findActiveTypes";
    private static final String GET_EDITABLE_TYPES_QUERY = "findEditableTypes";
}
