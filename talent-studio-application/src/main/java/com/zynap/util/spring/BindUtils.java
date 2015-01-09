package com.zynap.util.spring;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.validation.DataBinder;

/**
 * User: amark
 * Date: 17-Mar-2006
 * Time: 16:35:37
 */
public final class BindUtils {

    private BindUtils() {
    }

    public static MutablePropertyValues createPropertyValues(String[] propertyNames, Object[] propertyValues) {

        MutablePropertyValues pvs = new MutablePropertyValues();
        for (int i = 0; i < propertyNames.length; i++) {
            addPropertyValue(pvs, propertyNames[i], propertyValues[i]);
        }

        return pvs;
    }

    public static void addPropertyValue(MutablePropertyValues pvs, final String propertyName, final Object propertyValue) {
        pvs.addPropertyValue(new PropertyValue(propertyName, propertyValue));
    }

    public static DataBinder createBinder(Object o, String commandName) {
        return new DataBinder(o, commandName);
    }
}
