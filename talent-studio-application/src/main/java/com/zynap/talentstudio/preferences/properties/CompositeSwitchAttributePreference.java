package com.zynap.talentstudio.preferences.properties;

import java.util.HashMap;
import java.util.Map;


/**
 * User: amark
 * Date: 30-Jun-2005
 * Time: 09:55:55
 *
 * Class that supports nesting of conditions by containing SwitchAttributePreferences.
 */
public class CompositeSwitchAttributePreference extends AttributePreference implements ConditionBasedPreference {

	private static final long serialVersionUID = -3010138794584586478L;

    public CompositeSwitchAttributePreference() {
    }

    public CompositeSwitchAttributePreference(String attributeName, boolean displayable) {
        super(attributeName, displayable);
    }

    public void add(String expectedValue, SwitchAttributePreference switchAttributePreference) {
        switchAttributePreferences.put(expectedValue, switchAttributePreference);
    }

    public AttributeView apply(Object domainObject) throws Exception {

        if (displayable) {

            try {
                AttributeView value = invoke(domainObject);
                if (value != null) {
                    AttributePreference matchingPreference = get(value.getExpectedValue());
                    if (matchingPreference != null) {
                        return matchingPreference.apply(domainObject);
                    }
                }
            } catch (Exception ignored) {
            }

            final AttributePreference defaultPreference = getDefault();
            if (defaultPreference != null) return defaultPreference.apply(domainObject);
        }

        return null;
    }

    public AttributePreference getDefault() {
        return get(DEFAULT);
    }

    public AttributePreference get(String key) {
        return switchAttributePreferences.get(key);
    }

    public Map<String, SwitchAttributePreference> getSwitchAttributePreferences() {
        return switchAttributePreferences;
    }

    public void setSwitchAttributePreferences(Map<String, SwitchAttributePreference> switchAttributePreferences) {
        this.switchAttributePreferences = switchAttributePreferences;
    }

    private Map<String, SwitchAttributePreference> switchAttributePreferences = new HashMap<String, SwitchAttributePreference>();
}
