package com.zynap.talentstudio.preferences.domain;

import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.preferences.properties.ConditionBasedPreference;
import com.zynap.talentstudio.preferences.properties.ConditionalAttributePreference;
import com.zynap.talentstudio.preferences.properties.SwitchAttributePreference;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 14:11:31
 * Represents the display preferences for all the displaybale attributes of a domain object.
 */
public class DomainObjectPreference implements Serializable {

    private static final long serialVersionUID = 4842807764563209277L;

    public Map<String, AttributePreference> getAttributePrefs() {
        return attributePrefs;
    }

    public void setAttributePrefs(Map<String, AttributePreference> attributePrefs) {
        this.attributePrefs = attributePrefs;
    }

    public AttributePreference get(String key) {
        return attributePrefs.get(key);
    }

    public AttributePreference get(String key, String expectedValue) {
        final AttributePreference attributePreference = get(key);
        if (attributePreference instanceof ConditionBasedPreference) {
            return ((ConditionBasedPreference) attributePreference).get(expectedValue);
        }

        return null;
    }

    public void add(AttributePreference attributePreference) {
        attributePrefs.put(attributePreference.getAttributeName(), attributePreference);
    }

    public AttributePreference addOrUpdate(String attributeName, String expectedValue, boolean displayable) {
        AttributePreference pref = get(attributeName);
        if (pref == null) {
            if (StringUtils.hasText(expectedValue)) {
                pref = new SwitchAttributePreference(attributeName, true);
                ((SwitchAttributePreference) pref).add(new ConditionalAttributePreference(expectedValue, displayable));
            } else {
                pref = new AttributePreference(attributeName, displayable);
            }

            add(pref);
        } else {
            if (pref instanceof SwitchAttributePreference) {
                ((SwitchAttributePreference) pref).addOrUpdateConditionalPropertyPref(expectedValue, displayable);
            } else {
                pref.setDisplayable(displayable);
            }
        }

        return pref;
    }

    public AttributePreference addOrUpdate(String attributeName, String expectedValue, String displayName, boolean displayable) {
        AttributePreference pref = get(attributeName);
        if (pref == null) {
            if (StringUtils.hasText(expectedValue)) {
                pref = new SwitchAttributePreference(attributeName, true);
                ((SwitchAttributePreference) pref).add(new ConditionalAttributePreference(expectedValue, displayName, displayable));
            } else {
                pref = new AttributePreference(attributeName, displayName, displayable);
            }

            add(pref);
        } else {
            if (pref instanceof SwitchAttributePreference) {
                ((SwitchAttributePreference) pref).addOrUpdateConditionalPropertyPref(expectedValue, displayName, displayable);
            } else {
                pref.setDisplayable(displayable);
                pref.setDisplayName(displayName);
            }
        }

        return pref;
    }

    public void addOrUpdateFormattingInfo(String attributeName, String expectedValue, String formattingAttributeName, String formattingAttributeValue) {
        AttributePreference attributePreference = addOrUpdate(attributeName, expectedValue, true);
        attributePreference.addOrUpdateFormattingInfo(expectedValue, formattingAttributeName, formattingAttributeValue);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObjectPreference)) return false;

        final DomainObjectPreference domainObjectPreference = (DomainObjectPreference) o;

        if (!attributePrefs.equals(domainObjectPreference.attributePrefs)) return false;

        return true;
    }

    public int hashCode() {
        return attributePrefs.hashCode();
    }

    private Map<String, AttributePreference> attributePrefs = new LinkedHashMap<String, AttributePreference>();
}
