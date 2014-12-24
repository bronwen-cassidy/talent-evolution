package com.zynap.util.resource;

import com.zynap.util.ArrayUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Array;
import java.util.*;


/**
 * This class provides simple access to application parameters that we
 * want to be configurable.
 *
 * @author Angus Mark
 */
public class PropertiesManager {
    /**
     * The Logger.
     */
    private static final Log logger = LogFactory.getLog(PropertiesManager.class);

    /**
     * The standard properties bundle class.
     */
    private static final String STANDARD_PROPERTIES;

    /**
     * The PropertiesManager instance for a specific class c
     * expects to use a ResourceBundle whos name is given by
     * c.getName + EXTENSION
     */
    private static final String EXTENSION = "Resources";

    /**
     * A Map of the instances.
     */
    private static Map instanceMap = Collections.synchronizedMap(new HashMap());


    static {
        String systemPropertyName = PropertiesManager.class.getName() + ".standardproperties";
        STANDARD_PROPERTIES = System.getProperty(systemPropertyName, "Standard");

        logger.info("PropertiesManager using standard properties file: " + STANDARD_PROPERTIES);
    }

    /**
     * The underlying ResourceBundle.
     */
    private ResourceBundle bundle;


    /**
     * Provides Access to the standard PropertiesManager instance.
     * Don't need to synchronize this method because we are only caching.
     *
     * @return the PropertiesManager instance
     */
    public static PropertiesManager getInstance() {
        return getInstance(STANDARD_PROPERTIES);
    }


    /**
     * Provides Access to the PropertiesManager instance associated with relatedClass.
     * Don't need to synchronize this method because we are only caching.
     *
     * @param relatedClass the class
     * @return the PropertiesManager instance
     */
    public static PropertiesManager getInstance(Class relatedClass) {
        PropertiesManager pm = (PropertiesManager) instanceMap.get(relatedClass);

        if (pm == null) {
            pm = new PropertiesManager(relatedClass);
            instanceMap.put(relatedClass, pm);
        }

        return pm;
    }


    /**
     * Provides Access to the PropertiesManager instance associated with bundleClass.
     * Don't need to synchronize this method because we are only caching.
     *
     * @param bundleClass the class
     * @return the PropertiesManager instance
     */
    public static PropertiesManager getInstance(String bundleClass) {
        PropertiesManager pm = (PropertiesManager) instanceMap.get(bundleClass);

        if (pm == null) {
            pm = new PropertiesManager(bundleClass);
            instanceMap.put(bundleClass, pm);
        }

        return pm;
    }


    /**
     * Private constructor prevents additional instances of the PropertiesManager
     * from being created.
     *
     * @param bundleClass the class for the ResourceBundle
     */
    private PropertiesManager(String bundleClass) {
        try {
            bundle = ResourceBundle.getBundle(bundleClass);
        } catch (Exception e) {
            logger.error("Cannot find ResourceBundle " + bundleClass);
        }
    }

    /**
     * Private constructor prevents additional instances of the PropertiesManager
     * from being created.
     *
     * @param relatedClass the related class for the ResourceBundle
     */
    private PropertiesManager(Class relatedClass) {
        String bundleClass = relatedClass.getName() + EXTENSION;
        try {
            bundle = ResourceBundle.getBundle(bundleClass,
                    Locale.getDefault(),
                    relatedClass.getClassLoader());
        } catch (Exception e) {
            logger.error("Cannot find ResourceBundle " + bundleClass);
        }
    }

    /**
     * Gets the property identified by key from the standard resources.
     *
     * @param key the name of the property
     * @return the property value as a String
     */
    public String getString(String key) {
        return getString(key, true);
    }


    /**
     * Gets the property identified by key from the resources associated with
     * relatedClass.
     *
     * e.g. For the class com.mypackage.MyClass the associated resources class
     * will be com.mypackage.MyClassResources.
     *
     * If the property cannot be obtained then defaultValue is used.
     *
     * @param key the name of the property
     * @param defaultValue the value to return if the property was not found
     * @return the property value as a String
     */
    public String getString(String key, String defaultValue) {
        String value = getString(key, false);

        if (value == null)
            value = defaultValue;

        return value;
    }


    /**
     * Gets the property identified by key from the standard resources.
     *
     * @param key the name of the property
     * @return the property value as a Double
     */
    public Double getDouble(String key) {
        return (Double) getDouble(key, true);
    }


    /**
     * Gets the property identified by key from the resources with class name
     * bundleClass.
     *
     * If the property cannot be obtained then defaultValue is used.
     *
     * @param key the name of the property
     * @param defaultValue the value to return if the property was not found
     * @return the property value as a Double
     */
    public Double getDouble(String key, double defaultValue) {
        Double value = (Double) getDouble(key, false);

        if (value == null)
            value = new Double(defaultValue);

        return value;
    }


    /**
     * Gets the property identified by key from the standard resources.
     *
     * @param key the name of the property
     * @return the property value as a Double
     */
    public Object getProperty(String key) {
        return getProperty(key, true, true);
    }


    /**
     * Gets the property identified by key from the resources with class name
     * bundleClass.
     *
     * If the property cannot be obtained then defaultValue is used.
     *
     * @param key the name of the property
     * @param defaultValue the value to return if the property was not found
     * @return the property value as a Double
     */
    public Object getProperty(String key, Object defaultValue) {
        Object value = getProperty(key, true, false);

        if (value == null)
            value = defaultValue;

        return value;
    }


    /**
     * Gets an array identified by key from the resources identified
     * by bundleClass.
     *
     * The format in the properties file is:-
     *
     * arrayName.0 = 1, 2, 3, 4
     * arrayName.0 = 5, 6, 7, 8
     *
     * This will be retrieved as a 2D array of the type specified
     * by componentType.
     *
     * If the properties file has only one row in it then a 1D array will
     * be returned.
     *
     * @param key the name of the property
     * @param componentType the type of component to return - ie: String, Integer, etc
     * @return an array as appropriate
     */
    public Object getArray(String key, Class componentType) {
        Object array = null;

        List rows = new LinkedList();

        for (int rowIndex = 0; ; rowIndex++) {
            String row = getString(key + "." + rowIndex, false); // Don't warn if not found
            if (row == null) {
                // A 1D array does not have to have a 0 index on its only row
                if (rowIndex == 0) {
                    row = getString(key, false); // Don't warn if not found
                    if (row == null)
                        break;
                } else {
                    break;
                }
            }

            Object rowArray = ArrayUtils.stringToArray(row, ",", componentType);

            if (rowArray == null) {
                logger.warn("Cannot get row number " + rowIndex +
                        " of array " + key + " in bundle " + bundle);
            }

            rows.add(rowArray);
        }

        int rowCount = rows.size();

        if (rowCount == 1) {
            array = rows.get(0);
        } else {
            int[] dimensions = {rowCount, 0};
            array = Array.newInstance(componentType, dimensions);
            for (int i = 0; i < rowCount; i++) {
                Array.set(array, i, rows.get(i));
            }
        }

        return array;
    }

    /**
     * Get the underlying ResourceBundle wraped by this PropertiesManager.
     *
     * @return A ResourceBundle
     */
    public ResourceBundle getResourceBundle() {
        return bundle;
    }

    /**
     * Get a Properties object containing the entries of this
     * PropertiesManager's underlying resource bundle.
     *
     * @return A Properties Object
     */
    public Properties getProperties() {
        Properties props = new Properties();

        for (Enumeration e = bundle.getKeys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            props.setProperty(key, bundle.getString(key));
        }

        return props;
    }

    /**
     * Helper that looks up a given entry in the ResourceBundle.
     *
     * @param key The key to the resource
     * @param expected boolean that indicates whether to warn if the
     * resource cannot be found
     * @return The resource or <code>null</code> if <code>key</code>
     *         does not identify a valid resource String
     */
    private String getString(String key, boolean expected) {
        String value = null;

        try {
            value = bundle.getString(key);
        } catch (Exception e) {
            if (expected)
                logger.warn("Cannot get property " + key + " for bundle " + bundle);
        }

        return value;
    }


    /**
     * Gets the property identified by key from the resources with class name
     * bundleClass.
     *
     * @param key the name of the property
     * @param expected if expected is true and property is not found will log warning
     * @return the property value as a Double if possibe or a string
     */
    private Object getDouble(String key, boolean expected) {
        Object value = null;

        String valueStr = getString(key, expected);
        if (valueStr != null) {
            // Try to convert valueStr to a Double otherwise just return the string
            try {
                value = Double.valueOf(valueStr.trim());
            } catch (NumberFormatException e) {
                logger.warn("Cannot get property " + key + " for bundle " + bundle +
                        " as a number because of: " + e);
            }
        }

        return value;
    }

    /**
     * Gets the property identified by key from the resources with class name bundleClass.
     *
     * @param key the name of the property
     * @param returnString if true will return a string - otherwise a double
     * @param expected if expected and not found, will log warning
     * @return the property value as a Double if possible, or as a string
     */
    private Object getProperty(String key, boolean returnString, boolean expected) {
        Object value = null;

        String valueStr = getString(key, null);
        if (valueStr != null) {
            // Try to convert valueStr to a Double otherwise just return the string
            try {
                value = Double.valueOf(valueStr.trim());
            } catch (NumberFormatException e) {
                if (returnString)
                    value = valueStr;
                else
                    logger.warn("Cannot get property " + key + " for bundle " + bundle +
                            " as a number because of: " + e);
            }
        } else if (expected) {
            logger.warn("Cannot get property " + key + " for bundle " + bundle);
        }


        return value;
    }

}
