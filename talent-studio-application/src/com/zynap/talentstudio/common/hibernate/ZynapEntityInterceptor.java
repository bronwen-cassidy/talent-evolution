/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.common.hibernate;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.type.Type;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.hibernate.interceptor.InterceptorStrategyFactory;
import com.zynap.talentstudio.common.hibernate.interceptor.strategy.InterceptorStrategy;
import com.zynap.domain.Auditable;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Jun-2006 13:10:58
 */
public class ZynapEntityInterceptor implements Interceptor, BeanFactoryAware {

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Called just before an object is initialized. The interceptor may change the <tt>state</tt>, which will
     * be propagated to the persistent object. Note that when this method is called, <tt>entity</tt> will be
     * an empty uninitialized instance of the class.
     *
     * @return <tt>true</tt> if the user modified the <tt>state</tt> in any way.
     */
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        return false;
    }

    /**
     * Called when an object is detected to be dirty, during a flush. The interceptor may modify the detected
     * <tt>currentState</tt>, which will be propagated to both the database and the persistent object.
     * Note that not all flushes end in actual synchronization with the database, in which case the
     * new <tt>currentState</tt> will be propagated to the object, but not necessarily (immediately) to
     * the database. It is strongly recommended that the interceptor <b>not</b> modify the <tt>previousState</tt>.
     *
     * @return <tt>true</tt> if the user modified the <tt>currentState</tt> in any way.
     */
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        try {
            if (entity instanceof Auditable) {
                return getEntityInterceptorStrategy(entity).onModify((Auditable) entity, id, currentState, previousState, propertyNames);
            }
        } catch (TalentStudioException e) {
            throw new CallbackException(e);
        }
        return false;
    }

    /**
     * Called before an object is saved. The interceptor may modify the <tt>state</tt>, which will be used for
     * the SQL <tt>INSERT</tt> and propagated to the persistent object.
     *
     * @return <tt>true</tt> if the user modified the <tt>state</tt> in any way.
     */
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        try {
            if (entity instanceof Auditable) {
                return getEntityInterceptorStrategy(entity).onSave((Auditable) entity, id);
            }
        } catch (TalentStudioException e) {
            throw new CallbackException(e);
        }
        return false;
    }

    /**
     * Called before an object is deleted. It is not recommended that the interceptor modify the <tt>state</tt>.
     */
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {

        try {
            if (entity instanceof Auditable) {
                getEntityInterceptorStrategy(entity).onDelete((Auditable) entity, id, state, propertyNames);
            }
        } catch (TalentStudioException e) {
            throw new CallbackException(e);
        }
    }

    /**
     * Called before a flush
     */
    public void preFlush(Iterator entities) throws CallbackException {

    }

    /**
     * Called after a flush that actually ends in execution of the SQL statements required to synchronize
     * in-memory state with the database.
     */
    public void postFlush(Iterator entities) throws CallbackException {

    }

    /**
     * Called when a transient entity is passed to <tt>saveOrUpdate()</tt>. The return value determines
     * <ul>
     * <li><tt>Boolean.TRUE</tt> - the entity is passed to <tt>save()</tt>, resulting in an <tt>INSERT</tt>
     * <li><tt>Boolean.FALSE</tt> - the entity is passed to <tt>update()</tt>, resulting in an <tt>UPDATE</tt>
     * <li><tt>null</tt> - Hibernate uses the <tt>unsaved-value</tt> mapping to determine if the object is unsaved
     * </ul>
     *
     * @param entity a transient entity
     * @return Boolean or <tt>null</tt> to choose default behaviour
     */
    public Boolean isUnsaved(Object entity) {
        return null;
    }

    /**
     * Called from <tt>flush()</tt>. The return value determines whether the entity is updated
     * <ul>
     * <li>an array of property indices - the entity is dirty
     * <li>and empty array - the entity is not dirty
     * <li><tt>null</tt> - use Hibernate's default dirty-checking algorithm
     * </ul>
     *
     * @param entity a persistent entity
     * @return array of dirty property indices or <tt>null</tt> to choose default behaviour
     */
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return null;
    }

    /**
     * Instantiate the entity class. Return <tt>null</tt> to indicate that Hibernate should use
     * the default constructor of the class. The identifier property of the returned instance
     * should be initialized with the given identifier.
     *
     * @param clazz a mapped class
     * @param id the identifier of the new instance
     * @return an instance of the class, or <tt>null</tt> to choose default behaviour
     */
    public Object instantiate(Class clazz, Serializable id) throws CallbackException {
        return null;
    }

    /**
     * Get InterceptorStrategy implementation for specific class.
     *
     * @param entity
     * @return InterceptorStrategy
     */
    InterceptorStrategy getEntityInterceptorStrategy(Object entity) {

        if (strategyFactory == null) {
            strategyFactory = (InterceptorStrategyFactory) beanFactory.getBean("interceptorStrategyFactory");
        }

        return strategyFactory.getInterceptorStrategy(entity.getClass().getName());
    }

    /**
     * Factory used to load {@link com.zynap.talentstudio.common.hibernate.interceptor.strategy.InterceptorStrategy} objects based on class name.
     */
    private InterceptorStrategyFactory strategyFactory;

    /**
     * Bean factory.
     */
    private BeanFactory beanFactory;
}
