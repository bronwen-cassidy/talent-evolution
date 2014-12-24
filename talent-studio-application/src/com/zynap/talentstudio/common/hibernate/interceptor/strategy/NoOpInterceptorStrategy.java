package com.zynap.talentstudio.common.hibernate.interceptor.strategy;

import com.zynap.exception.TalentStudioException;
import com.zynap.domain.Auditable;

import java.io.Serializable;

/**
 * User: amark
 * Date: 18-Aug-2006
 * Time: 12:16:36
 *
 * Default do-nothing implementation of InterceptorStrategy.
 */
public class NoOpInterceptorStrategy implements InterceptorStrategy {

    public void onDelete(Auditable entity, Serializable id, Object[] state, String[] propertyNames) throws TalentStudioException {
    }

    public boolean onSave(Auditable entity, Serializable id) throws TalentStudioException {
        return false;
    }

    public boolean onModify(Auditable entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames) throws TalentStudioException {
        return false;
    }
}
