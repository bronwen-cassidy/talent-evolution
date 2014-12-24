package com.zynap.talentstudio.preferences.domain;

import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.preferences.properties.AttributeView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 14:03:05
 * Contains all the preferences for the domain objects used in a given view for a given user.
 */
public class DomainObjectPreferenceCollection implements Serializable {

    private static final long serialVersionUID = 5362416312470420049L;

    public AttributeView getPropertyValue(Object domainObject, String attributeName) throws Exception {
        DomainObjectPreference domainObjectPreference = get(domainObject.getClass().getName());
        AttributePreference attributePreference = domainObjectPreference.get(attributeName);
        return attributePreference.apply(domainObject);
    }

    public Map getDomainObjectPrefs() {
        return domainObjectPrefs;
    }

    public void setDomainObjectPrefs(Map<String, DomainObjectPreference> domainObjectPrefs) {
        this.domainObjectPrefs = domainObjectPrefs;
    }

    public void add(String className, DomainObjectPreference domainObjectPreference) {
        domainObjectPrefs.put(className, domainObjectPreference);
    }

    public DomainObjectPreference get(String className) {
        return domainObjectPrefs.get(className);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObjectPreferenceCollection)) return false;

        final DomainObjectPreferenceCollection domainObjectPreferenceCollection = (DomainObjectPreferenceCollection) o;

        if (domainObjectPrefs != null ? !domainObjectPrefs.equals(domainObjectPreferenceCollection.domainObjectPrefs) : domainObjectPreferenceCollection.domainObjectPrefs != null) return false;

        return true;
    }

    public int hashCode() {
        return 29 * (domainObjectPrefs != null ? domainObjectPrefs.hashCode() : 0);
    }

    private Map<String, DomainObjectPreference> domainObjectPrefs = new HashMap<String, DomainObjectPreference>();
}
