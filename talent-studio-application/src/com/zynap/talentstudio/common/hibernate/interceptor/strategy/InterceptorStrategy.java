package com.zynap.talentstudio.common.hibernate.interceptor.strategy;

import com.zynap.exception.TalentStudioException;
import com.zynap.domain.Auditable;

import java.io.Serializable;

/**
 * User: amark
 * Date: 18-Aug-2006
 * Time: 12:10:56
 *
 * Interface that can be implemented to provide support for modifications to persistent objects before they are sent to the db.
 */
public interface InterceptorStrategy {

    /**
     * Called on deletion.
     *
     * @param entity The entity being deleted
     * @param id The id of the entity being deleted
     * @param state
     * @param propertyNames @throws TalentStudioException
     */
    void onDelete(Auditable entity, Serializable id, Object[] state, String[] propertyNames) throws TalentStudioException;

    /**
     * Called on create or update.
     * <br/> Returns true if the entity state has been modified.
     *
     * @param entity The entity being deleted
     * @param id The id of the entity being deleted
     * @return true or false
     * @throws TalentStudioException
     */
    boolean onSave(Auditable entity, Serializable id) throws TalentStudioException;

    boolean onModify(Auditable entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames) throws TalentStudioException;
}
