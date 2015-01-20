/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.arena;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ArenaValidator implements Validator {

    public boolean supports(Class clazz) {
        
        return ArenaWrapperBean.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        ArenaWrapperBean wrapperBean = (ArenaWrapperBean) obj;

        final String label = wrapperBean.getLabel();
        if (!StringUtils.hasText(label)) {
            errors.rejectValue("label", "error.label.required", "Label is a required field.");
        }

        final int sessionTimeout = wrapperBean.getSessionTimeout();
        if (sessionTimeout <= 0) {
            errors.rejectValue("sessionTimeout", "field.is.numeric", "Please enter a number greater than zero.");
        }

        if (wrapperBean.isHideable()) {

            boolean noValue = checkDisplayConfigItems(wrapperBean);
            if (noValue) {
                errors.reject("error.arena.displayconfigitems.required", "Please select default artefact views.");
            }

        } else {

            // ensure that a non-hideable arena cannot be made inactive
            if (!wrapperBean.isActive()) {
                errors.rejectValue("active", "error.arena.required", "This arena cannot be disabled.");
            }
        }
    }

    /**
     * Check if arena has default views.
     *
     * @param wrapperBean
     * @return false if none of the display config item wrappers have an assigned item id.
     */
    private boolean checkDisplayConfigItems(ArenaWrapperBean wrapperBean) {
        boolean noValue = false;
        for (Iterator iterator = wrapperBean.getArenaDisplayConfigItems().iterator(); iterator.hasNext();) {
            ArenaDisplayConfigItemWrapper itemWrapper = (ArenaDisplayConfigItemWrapper) iterator.next();
            if (itemWrapper.getSelectedItemId() == null) {
                noValue = true;
                break;
            }
        }

        return noValue;
    }
}
