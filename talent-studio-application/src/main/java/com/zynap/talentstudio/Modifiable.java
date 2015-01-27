package com.zynap.talentstudio;

import java.io.Serializable;

/**
 *
 */
public interface Modifiable {
    
    <E> Serializable save(Class clazz, E e);

    <E> void delete(Class<E> clazz, E e);

    void deleteById(Class clazz, Serializable id);
}
