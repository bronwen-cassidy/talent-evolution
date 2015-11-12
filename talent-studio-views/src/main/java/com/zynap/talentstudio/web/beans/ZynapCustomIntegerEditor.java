package com.zynap.talentstudio.web.beans;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Property editor for integer or int properties.
 *
 * @author Andreas Andersson
 * @since 10.02.2004
 * @see org.springframework.validation.DataBinder#registerCustomEditor
 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder
 * @see org.springframework.web.bind.BindInitializer#initBinder
 */
public class ZynapCustomIntegerEditor extends PropertyEditorSupport {

    public ZynapCustomIntegerEditor() {
    }

    public void setAsText(String text) throws IllegalArgumentException {

        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            final String trimmedText = text.trim();
            try {
                setValue(new Integer(trimmedText));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number value [" + text + "]");
            }
        }
    }

    public String getAsText() {
        return getValue() != null ? super.getAsText() : "";
    }
}


