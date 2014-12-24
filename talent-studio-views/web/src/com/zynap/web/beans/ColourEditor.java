/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.web.beans;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 23-Apr-2010 19:52:25
 */
public class ColourEditor extends PropertyEditorSupport {

    public void setAsText(String text) throws IllegalArgumentException {

            if (StringUtils.hasText(text)) {
                // we are getting this format ,#995566:jjhiui
                String newValue = text.indexOf("#") != -1 ? text.substring(text.indexOf("#") + 1, text.indexOf(":")) : text;
                if(newValue.startsWith(",")) {
                    newValue = newValue.substring(1);
                }
                setValue(newValue);
            }
        }

}
