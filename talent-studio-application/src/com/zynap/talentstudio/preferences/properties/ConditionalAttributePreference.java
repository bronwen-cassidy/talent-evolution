package com.zynap.talentstudio.preferences.properties;

import com.zynap.talentstudio.preferences.format.FormattingInfo;

/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 11:06:09
 */
public class ConditionalAttributePreference extends AttributePreference {

    private static final long serialVersionUID = -8040943119742410134L;
    protected String expectedValue;

    public ConditionalAttributePreference() {
    }    

    public ConditionalAttributePreference(String expectedValue, boolean displayable) {
        this.expectedValue = expectedValue;
        setDisplayable(displayable);
    }

    public ConditionalAttributePreference(String expectedValue, String displayName, boolean displayable) {
        this(expectedValue, displayable);
        this.displayName = displayName;
    }

    public AttributeView apply(Object domainObject) throws Exception {
        if (isDisplayable()) {
            return new AttributeView(expectedValue, displayName, getFormattingInfo(domainObject, expectedValue));
        } else {
            return null;
        }
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    protected FormattingInfo getFormattingInfo(Object domainObject, String value) throws Exception {
        return value.equals(expectedValue) ? getFormattingInfo() : null;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConditionalAttributePreference)) return false;

        final ConditionalAttributePreference conditionalPropertyPref = (ConditionalAttributePreference) o;

        if (expectedValue != null ? !expectedValue.equals(conditionalPropertyPref.expectedValue) : conditionalPropertyPref.expectedValue != null) return false;

        return true;
    }

    public int hashCode() {
        return (expectedValue != null ? expectedValue.hashCode() : 0);
    }
}
