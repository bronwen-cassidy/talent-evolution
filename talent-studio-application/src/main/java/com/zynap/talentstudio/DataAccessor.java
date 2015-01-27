package com.zynap.talentstudio;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 */
public interface DataAccessor extends Serializable {

    <E> E findById(Class clazz, Serializable id);
    <E> void delete (Class clazz, E e);

    void deleteById(Class clazz, Serializable id);

    <E> void save(Class clazz, E e);

    <E> Collection<E> findAll(Class<E> clazz);

    <E> Collection<E> findAll(Class<E> clazz, String... orderBy);
}
