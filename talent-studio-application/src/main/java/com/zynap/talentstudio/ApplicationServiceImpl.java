/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-May-2010 11:42:30
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {


    @Override
    public <E> E findById(Class clazz, Serializable id) {
        return null;
    }

    @Override
    public <E> Collection<E> findAll(Class<E> clazz) {
        return null;
    }

    @Override
    public <E> Collection<E> findAll(Class<E> clazz, String... orderBy) {
        return null;
    }

    @Override
    public <E> Serializable save(Class clazz, E e) {
        return null;
    }

    @Override
    public <E> void delete(Class<E> clazz, E e) {

    }

    @Override
    public void deleteById(Class clazz, Serializable id) {

    }
    
    private DataAccessor dataAccessor; 
}
