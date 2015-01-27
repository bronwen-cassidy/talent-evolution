package com.zynap.talentstudio;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface Findable {

    <E> E findById(Class clazz, Serializable id);

    <E> Collection<E> findAll(Class<E> clazz);

    <E> Collection<E> findAll(Class<E> clazz, String... orderBy);
    
}
