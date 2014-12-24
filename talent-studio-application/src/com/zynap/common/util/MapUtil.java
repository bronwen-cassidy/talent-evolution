/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class MapUtil {

    /**
     * Given a known value in a map searches for all keys that have the particular value.
     *
     * @param target
     * @param value
     * @return Object[] all the found keys in the map that match the given value.
     */
    public static Object[] getKeys(Map target, Object value) {
        List<Object> result = new ArrayList<Object>();
        if(target == null) return null;
        if(target.isEmpty()) return null;
        for (Iterator iterator = target.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object entryValue = entry.getValue();

            if((entryValue == null && value != null) || (entryValue != null && value == null)) {
                continue;
            }

            if(value == entryValue) {
                result.add(entry.getKey());
            }
            else if(entryValue.equals(value)) {
                result.add(entry.getKey());
            }
        }
        return result.toArray(new Object[result.size()]);
    }
}
