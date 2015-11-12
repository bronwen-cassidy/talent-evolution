/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio;

import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.BeansException;

import java.util.HashMap;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Jul-2009 14:28:12
 */
public class MockBeanFactory implements BeanFactory {

    public Object getBean(String name) throws BeansException {
        return beans.get(name);
    }

    @Override
    public <T> T getBean(Class<T> aClass) throws BeansException {
        return null;
    }

    public void addBean(String name, Object value) {
        beans.put(name, value);
    }

    public Object getBean(String name, Class requiredType) throws BeansException {
        return null;
    }

    public Object getBean(String name, Object[] args) throws BeansException {
        return null;
    }

    public boolean containsBean(String name) {
        return false;
    }

    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException {
        return false;
    }

    public Class getType(String name) throws NoSuchBeanDefinitionException {
        return null;
    }

    public String[] getAliases(String name) {
        return new String[0];
    }

    private Map<String, Object> beans = new HashMap<String, Object>();
}
