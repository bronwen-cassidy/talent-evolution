package com.zynap.talentstudio;

import org.apache.log4j.Logger;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 */

public class HibernateDataAccessorImpl extends HibernateDaoSupport implements DataAccessor {

    @Override
    public <E> E findById(Class clazz, Serializable id) {
        return null;
    }

    @Override
    public <E> void delete(Class clazz, E e) {

    }

    @Override
    public void deleteById(Class clazz, Serializable id) {

    }

    @Override
    public <E> void save(Class clazz, E e) {

    }

    @Override
    public <E> Collection<E> findAll(Class<E> clazz) {
        return null;
    }

    @Override
    public <E> Collection<E> findAll(Class<E> clazz, String... orderBy) {
        return null;
    }
    
    private static final Logger logger = Logger.getLogger(HibernateDataAccessorImpl.class);
}
