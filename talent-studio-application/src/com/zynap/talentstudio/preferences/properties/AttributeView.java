package com.zynap.talentstudio.preferences.properties;

import com.zynap.talentstudio.preferences.format.FormattingInfo;

import java.io.Serializable;

/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 12:16:25
 */
public class AttributeView implements Serializable {

    public AttributeView(String expectedValue, String displayValue, FormattingInfo formattingInfo) {
        this.expectedValue = expectedValue;
        this.formattingInfo = formattingInfo;
        this.displayValue = displayValue;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public FormattingInfo getFormattingInfo() {
        return formattingInfo;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttributeView)) return false;

        final AttributeView attributeView = (AttributeView) o;

        if (!formattingInfo.equals(attributeView.formattingInfo)) return false;
        if (!expectedValue.equals(attributeView.expectedValue)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = expectedValue.hashCode();
        result = 29 * result + formattingInfo.hashCode();
        return result;
    }


    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("AttributeView[");
        stringBuffer.append("\r\n value=" + expectedValue);
        stringBuffer.append("\r\n formattingInfo=" + formattingInfo);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }

    private static final long serialVersionUID = 890050835618062215L;
    private String expectedValue;
    private String displayValue;

    private FormattingInfo formattingInfo;
}
