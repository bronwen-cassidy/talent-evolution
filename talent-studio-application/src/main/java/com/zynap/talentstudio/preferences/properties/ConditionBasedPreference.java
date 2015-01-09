package com.zynap.talentstudio.preferences.properties;

/**
 * User: amark
 * Date: 30-Jun-2005
 * Time: 11:52:33
 */
public interface ConditionBasedPreference {

    public AttributePreference getDefault();

    public AttributePreference get(String key);

    public AttributeView apply(Object domainObject) throws Exception;    
}
