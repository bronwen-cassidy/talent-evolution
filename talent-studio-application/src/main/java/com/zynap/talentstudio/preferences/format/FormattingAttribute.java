package com.zynap.talentstudio.preferences.format;

import java.io.Serializable;

/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 13:42:00
 * Represents a single formatting attribute associated with a domain object property
 * - for example the colour to be used to display it.
 */
public class FormattingAttribute implements Serializable {

	private static final long serialVersionUID = 3556221724434882435L;
    private String name;

    private String value;

    public FormattingAttribute() {
    }

    public FormattingAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString()
    {
        return value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormattingAttribute)) return false;

        final FormattingAttribute formattingAttribute = (FormattingAttribute) o;

        if (name != null ? !name.equals(formattingAttribute.name) : formattingAttribute.name != null) return false;
        if (value != null ? !value.equals(formattingAttribute.value) : formattingAttribute.value != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 29 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
