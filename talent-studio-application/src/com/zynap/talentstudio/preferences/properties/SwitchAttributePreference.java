package com.zynap.talentstudio.preferences.properties;

import java.util.HashMap;
import java.util.Map;


/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 11:06:09
 * Class that holds several preferences each of which only applies to one value of the attribute.
 * <br> Eg: If the value of the attribute equals "male" then a different preference is returned.
 * <br> if there is a preference but it has been disabled, then no preference is considered.
 * <br> If there is no preference the default preference will be used.
 */
public final class SwitchAttributePreference extends AttributePreference implements ConditionBasedPreference {

    private static final long serialVersionUID = 5100369806734667832L;

    public SwitchAttributePreference() {
    }

    public SwitchAttributePreference(String attributeName, boolean displayable) {
        super(attributeName, displayable);
    }

    public AttributeView apply(Object domainObject) throws Exception {

        if (displayable) {

            AttributeView value;
            try {
                value = invoke(domainObject);
                if (value != null) {
                    AttributePreference matchingPreference = get(value.getExpectedValue());
                    if (matchingPreference != null) {
                        return matchingPreference.apply(domainObject);
                    }
                }
            } catch (Exception ignored) {
            }

            return getDefaultAttributePreference("");
        }

        return null;
    }

    private AttributeView getDefaultAttributePreference(String value) {
        AttributeView defaultAttributeView = null;

        AttributePreference conditionalPropertyPref = getDefault();
        if (conditionalPropertyPref != null) {
            defaultAttributeView = new AttributeView(value, conditionalPropertyPref.getDisplayName(), conditionalPropertyPref.getFormattingInfo());
        }

        return defaultAttributeView;
    }

    public Map<String, ConditionalAttributePreference> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, ConditionalAttributePreference> conditions) {
        this.conditions = conditions;
    }

    public void add(ConditionalAttributePreference condition) {
        conditions.put(condition.getExpectedValue(), condition);
    }

    public ConditionalAttributePreference addOrUpdateConditionalPropertyPref(String expectedValue, boolean displayable) {
        ConditionalAttributePreference conditionalPropertyPref = (ConditionalAttributePreference) get(expectedValue);
        if (conditionalPropertyPref == null) {
            conditionalPropertyPref = new ConditionalAttributePreference(expectedValue, displayable);
            add(conditionalPropertyPref);
        } else {
            conditionalPropertyPref.setDisplayable(displayable);
        }
        return conditionalPropertyPref;
    }

    public ConditionalAttributePreference addOrUpdateConditionalPropertyPref(String expectedValue, String displayValue, boolean displayable) {
        ConditionalAttributePreference conditionalPropertyPref = (ConditionalAttributePreference) get(expectedValue);
        if (conditionalPropertyPref == null) {
            conditionalPropertyPref = new ConditionalAttributePreference(expectedValue, displayValue, displayable);
            add(conditionalPropertyPref);
        } else {
            conditionalPropertyPref.setDisplayable(displayable);
            conditionalPropertyPref.setDisplayName(displayValue);
        }
        return conditionalPropertyPref;
    }

    public void addOrUpdateFormattingInfo(String expectedValue, String formattingAttributeName, String formattingAttributeValue) {
        ConditionalAttributePreference conditionalPropertyPref = (ConditionalAttributePreference) get(expectedValue);
        if (conditionalPropertyPref == null) {
            conditionalPropertyPref = new ConditionalAttributePreference(expectedValue, true);
        }

        conditionalPropertyPref.addOrUpdateFormattingInfo(expectedValue, formattingAttributeName, formattingAttributeValue);
    }

    public void addOrUpdateFormattingInfo(String expectedValue, String displayValue, String formattingAttributeName, String formattingAttributeValue) {
        ConditionalAttributePreference conditionalPropertyPref = (ConditionalAttributePreference) get(expectedValue);
        if (conditionalPropertyPref == null) {
            conditionalPropertyPref = new ConditionalAttributePreference(expectedValue, true);
            conditionalPropertyPref.setDisplayName(displayValue);
        }
        conditionalPropertyPref.addOrUpdateFormattingInfo(expectedValue, formattingAttributeName, formattingAttributeValue);
    }

    public AttributePreference get(String key) {
        return conditions.get(key);
    }

    public AttributePreference getDefault() {
        return get(DEFAULT);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SwitchAttributePreference)) return false;
        if (!super.equals(o)) return false;

        final SwitchAttributePreference switchPropertyPref = (SwitchAttributePreference) o;

        if (conditions != null ? !conditions.equals(switchPropertyPref.conditions) : switchPropertyPref.conditions != null) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (conditions != null ? conditions.hashCode() : 0);
        return result;
    }

    private Map<String, ConditionalAttributePreference> conditions = new HashMap<String, ConditionalAttributePreference>();
}
