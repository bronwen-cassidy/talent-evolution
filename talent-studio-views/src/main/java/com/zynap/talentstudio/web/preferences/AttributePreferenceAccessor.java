package com.zynap.talentstudio.web.preferences;

import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.preferences.properties.AttributeView;
import com.zynap.talentstudio.preferences.properties.ConditionBasedPreference;

/**
 * User: amark
 * Date: 03-Dec-2004
 * Time: 13:08:13
 * Utility used by presentation tier to access user preferences.
 */
public final class AttributePreferenceAccessor {

    private AttributePreferenceAccessor() {
    }

    public static AttributeView getAttributePreference(DomainObjectPreferenceCollection domainObjectPreferenceCollection,
                                                       Object domainObject, String attributeName) {
        if (domainObject != null) {
            try {
                return domainObjectPreferenceCollection.getPropertyValue(domainObject, attributeName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static AttributeView getAttributePreference(DomainObjectPreferenceCollection domainObjectPreferenceCollection,
                                                       Object domainObject, String attributeName, String expectedValue) {
        if (domainObject != null) {
            try {
                final ConditionBasedPreference preference = getPreference(domainObjectPreferenceCollection, domainObject.getClass(), attributeName, expectedValue);
                if (preference != null) return preference.apply(domainObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static AttributePreference get(DomainObjectPreferenceCollection domainObjectPreferenceCollection,
                                          Object domainObject, String attributeName) {
        return domainObjectPreferenceCollection.get(domainObject.getClass().getName()).get(attributeName);
    }

    public static AttributePreference get(DomainObjectPreferenceCollection domainObjectPreferenceCollection,
                                          Class domainObjectClass, String attributeName) {
        return domainObjectPreferenceCollection.get(domainObjectClass.getName()).get(attributeName);
    }

    public static ConditionBasedPreference getPreference(DomainObjectPreferenceCollection domainObjectPreferenceCollection,
                                                                   Class domainObjectClass, String attributeName, String expectedValue) {

        return (ConditionBasedPreference) domainObjectPreferenceCollection.get(domainObjectClass.getName()).get(attributeName, expectedValue);
    }
}
