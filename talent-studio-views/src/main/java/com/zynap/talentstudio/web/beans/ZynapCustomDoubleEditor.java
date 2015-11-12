package com.zynap.talentstudio.web.beans;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 18-Feb-2005
 * Time: 16:18:07
 * To change this template use File | Settings | File Templates.
 */
public class ZynapCustomDoubleEditor extends PropertyEditorSupport {

    public ZynapCustomDoubleEditor() {
    }

    public void setAsText(String text) throws IllegalArgumentException {

        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            final String trimmedText = text.trim();
            try {
                setValue(new Double(trimmedText));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number value [" + text + "]");
            }
        }
    }

    public String getAsText() {
        return getValue() != null ? super.getAsText() : "";
    }
}


