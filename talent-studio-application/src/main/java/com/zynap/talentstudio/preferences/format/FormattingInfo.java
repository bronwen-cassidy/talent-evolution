package com.zynap.talentstudio.preferences.format;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 13:44:01
 * Contains multiple FormattingAttribute elements that contain the additional information used to display the entity
 * property such as foregroundColour, etc.
 */
public class FormattingInfo implements Serializable {

    private static final long serialVersionUID = -4846093483554741875L;

    public FormattingInfo() {
    }

    public FormattingInfo(Map<String, FormattingAttribute> formattingAttributes) {
        this.formattingAttributes = formattingAttributes;
    }

    public Map<String, FormattingAttribute> getFormattingAttributes() {
        return formattingAttributes;
    }

    public void setFormattingAttributes(Map<String, FormattingAttribute> formattingAttributes) {
        this.formattingAttributes = formattingAttributes;
    }

    public void add(FormattingAttribute formattingAttribute) {
        formattingAttributes.put(formattingAttribute.getName(), formattingAttribute);
    }

    public FormattingAttribute get(String key) {
        return formattingAttributes.get(key);
    }

    public String getValue(String key) {
        return get(key).getValue();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[").append(getClass().getName());
        stringBuffer.append("\r\nFormattingAttributes").append(formattingAttributes);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }

    public void addOrUpdate(String name, String value) {
        FormattingAttribute formattingAttribute = get(name);
        if (formattingAttribute == null) {
            formattingAttribute = new FormattingAttribute(name, value);
            add(formattingAttribute);
        } else {
            formattingAttribute.setValue(value);
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormattingInfo)) return false;

        final FormattingInfo formattingInfo = (FormattingInfo) o;

        if (formattingAttributes != null ? !formattingAttributes.equals(formattingInfo.formattingAttributes) : formattingInfo.formattingAttributes != null)
            return false;

        return true;
    }

    public int hashCode() {
        return (formattingAttributes != null ? formattingAttributes.hashCode() : 0);
    }

    private Map<String, FormattingAttribute> formattingAttributes = new HashMap<String, FormattingAttribute>();
}
