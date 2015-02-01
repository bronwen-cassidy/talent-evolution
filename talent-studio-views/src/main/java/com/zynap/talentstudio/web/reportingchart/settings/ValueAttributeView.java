/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart.settings;

import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.common.lookups.LookupValue;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ValueAttributeView implements Serializable, Comparable {

    public ValueAttributeView(String attributeName, String fgColor, String bgColor) {
        this(attributeName);
        this.fgColor = fgColor;
        this.bgColor = bgColor;
    }

    public ValueAttributeView(String attributeName) {
        this.attributeName = attributeName;
        this.expectedValue = attributeName;
    }

    public ValueAttributeView(String attributeName, String displayValue, boolean displayable) {
        this(attributeName);
        this.expectedValue = attributeName;
        this.displayable = displayable;
        this.displayValue = displayValue;
    }

    public ValueAttributeView(String attributeName, boolean displayed) {
        this(attributeName);
        displayable = displayed;
    }

    public ValueAttributeView(String attributeName, String displayValue) {
        this(attributeName);
        this.displayValue = displayValue;
    }

    public ValueAttributeView(String attributeName, String displayValue, boolean displayed, Long key, ArrayList<LookupValue> attributeValues) {
        this(attributeName, displayed);
        this.key = key;
        this.values = attributeValues;
        this.displayValue = displayValue;
    }

    public ValueAttributeView(String attributeName, String fgValue, String bgValue, String displayName, boolean displayable) {
        this(attributeName, fgValue, bgValue);
        this.displayValue = displayName;
        this.displayable = displayable;
    }

    public ValueAttributeView(Long key, String attributeName, String fgColor, String bgColor, String expectedValue, boolean displayable) {
        this(attributeName,fgColor, bgColor);
        this.expectedValue = expectedValue;
        this.displayValue = expectedValue;
        this.displayable = displayable;
        this.key = key;
    }

    public ValueAttributeView(String attributeName, String fgColor, String bgColor, String displayName, String expectedValue, boolean displayable) {
        this(attributeName,fgColor, bgColor);
        this.expectedValue = expectedValue;
        this.displayValue = displayName;
        this.displayable = displayable;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getFgColor() {
        return fgColor;
    }

    public void setFgColor(String fgColor) {
        this.fgColor = fgColor;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getColor() {
        return bgColor + ":" + fgColor;
    }

    /**
     * Value being input from jsp is value,#000999,white.
     * The first value is the expected value of the attribute preference, the second is the background color,
     * the third is the foreground color
     *
     * @param color
     */
    public void setColor(String color) {
        String[] allvalues = StringUtils.delimitedListToStringArray(color, ",");
        String[] colors = StringUtils.delimitedListToStringArray(allvalues[1], ":");        
        bgColor = colors[0];
        fgColor = colors[1];
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }

    public Collection getValues() {
        return values;
    }

    public int compareTo(Object o) {
        ValueAttributeView x = (ValueAttributeView) o;
        return getAttributeName().compareTo(x.getAttributeName());
    }

    public boolean isDefault() {
        return !StringUtils.hasText(attributeName) || "fixed color".equals(attributeName) || AttributePreference.DEFAULT.equals(attributeName);
    }

    private Long key;
    private String attributeName;
    private String displayValue;
    private String fgColor;
    private String bgColor;
    private String expectedValue;
    private boolean displayable;
    private Collection values;
}
