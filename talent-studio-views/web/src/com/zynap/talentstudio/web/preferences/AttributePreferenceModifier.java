package com.zynap.talentstudio.web.preferences;

import com.zynap.talentstudio.preferences.domain.DomainObjectPreference;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;

/**
 * User: amark
 * Date: 03-Dec-2004
 * Time: 13:01:53
 * Utility used by presentation tier to modify user preferences.
 */
public final class AttributePreferenceModifier {

    private AttributePreferenceModifier() {
    }

    public static void modifyAttributePreference(DomainObjectPreferenceCollection domainObjectPreferenceCollection,
                                                 String className, String attributeName, String expectedValue, boolean displayable) {
        
        DomainObjectPreference domainObjectPreference = domainObjectPreferenceCollection.get(className);
        domainObjectPreference.addOrUpdate(attributeName, expectedValue, displayable);
    }

    public static void modifyAttributePreference(DomainObjectPreferenceCollection domainObjectPreferenceCollection, String className, String attributeName, String displayName, String expectedValue, boolean displayable) {
        DomainObjectPreference domainObjectPreference = domainObjectPreferenceCollection.get(className);
        domainObjectPreference.addOrUpdate(attributeName, expectedValue, displayName, displayable);
    }

    public static void modifyAttributePreference(DomainObjectPreferenceCollection domainObjectPreferenceCollection,
                                                 String className, String attributeName, String expectedValue,
                                                 String formattingAttributeName, String formattingAttributeValue) {

        DomainObjectPreference domainObjectPreference = domainObjectPreferenceCollection.get(className);
        domainObjectPreference.addOrUpdateFormattingInfo(attributeName, expectedValue, formattingAttributeName, formattingAttributeValue);
    }

    public static void modifyAttributePreference(DomainObjectPreferenceCollection preferenceCollection,
                                                 String className, String attributeName,
                                                 String formattingAttributeName, String formattingAttributeValue) {

        modifyAttributePreference(preferenceCollection, className, attributeName, null, formattingAttributeName, formattingAttributeValue);
    }

    public static void modifyAttributePreference(DomainObjectPreferenceCollection preferenceCollection,
                                                 String className, String attributeName,
                                                 String[] formattingAttributeNames, String[] formattingAttributeValues) {

        for (int i = 0; i < formattingAttributeNames.length; i++) {
            modifyAttributePreference(preferenceCollection, className, attributeName, null, formattingAttributeNames[i], formattingAttributeValues[i]);
        }
    }
    public static void modifyAttributePreference(DomainObjectPreferenceCollection preferenceCollection, String className,
                                                 String attributeName, String expectedValue,
                                                 String[] formattingAttributeNames, String[] formattingAttributeValues) {
        for (int i = 0; i < formattingAttributeNames.length; i++) {
            modifyAttributePreference(preferenceCollection, className, attributeName, expectedValue, formattingAttributeNames[i], formattingAttributeValues[i]);
        }
    }
}
