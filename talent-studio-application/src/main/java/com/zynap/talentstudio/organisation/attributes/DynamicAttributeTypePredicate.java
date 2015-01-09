/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import org.apache.commons.collections.Predicate;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 31-Aug-2006 15:07:17
 */
public class DynamicAttributeTypePredicate implements Predicate {

    public DynamicAttributeTypePredicate(String[] types) {
        this.types = types;
    }

    public boolean evaluate(Object object) {
        DynamicAttribute attribute = (DynamicAttribute) object;
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            if(attribute.typeMatches(type)) return true;
        }
        return false;
    }

    private String[] types;
}
