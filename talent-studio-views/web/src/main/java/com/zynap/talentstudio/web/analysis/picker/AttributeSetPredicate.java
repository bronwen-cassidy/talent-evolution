/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.picker;

import org.apache.commons.collections.Predicate;

import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Jan-2008 14:20:40
 */
public class AttributeSetPredicate implements Predicate {

    private final String type;

    private final String viewType;

    AttributeSetPredicate(String type, String viewType) {
        this.type = type;
        this.viewType = viewType;
    }

    public boolean evaluate(Object object) {
        AttributeSet attributeSet = (AttributeSet) object;

        // check if type (position or subject) matches
        boolean found = attributeSet.getType().equals(type);

        // then check view type if provided (if provided both must be true)
        if (found && StringUtils.hasText(viewType)) {
            found = viewType.equals(attributeSet.getViewType());
        }
        return found;
    }

}
