package com.zynap.web.beans;

import com.zynap.common.util.StringUtil;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Property editor for Boolean properties.
 *
 * <p>This is not meant to be used as system PropertyEditor but rather as
 * locale-specific Boolean editor within custom controller code, to parse
 * UI-caused boolean strings into Boolean properties of beans, and
 * evaluate them in the UI form.
 *
 * <p>In web MVC code, this editor will typically be registered with
 * binder.registerCustomEditor calls in an implementation of
 * BaseCommandController's initBinder method.
 *
 * A null value or empty string will be treated as false
 *
 * @author Andreas Andersson
 * @see org.springframework.validation.DataBinder#registerCustomEditor
 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder
 * @since 10.02.2004
 */
public class ZynapCustomBooleanEditor extends PropertyEditorSupport {

    public ZynapCustomBooleanEditor() {
    }

    public void setAsText(String text) throws IllegalArgumentException {

        if (text == null || !StringUtils.hasText(text)) {
            setValue(Boolean.FALSE);
        } else if (text.equalsIgnoreCase(Boolean.TRUE.toString()) || text.equalsIgnoreCase("on") || text.equalsIgnoreCase(StringUtil.TRUE)) {
            setValue(Boolean.TRUE);
        } else if (text.equalsIgnoreCase(Boolean.FALSE.toString()) || text.equalsIgnoreCase("off") || text.equalsIgnoreCase(StringUtil.FALSE)) {
            setValue(Boolean.FALSE);
        } else
            throw new IllegalArgumentException("Invalid Boolean value [" + text + "]");
    }

}


