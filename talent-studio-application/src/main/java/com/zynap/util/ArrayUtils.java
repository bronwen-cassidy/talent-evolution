package com.zynap.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.*;


/**
 * A collection of utilities for converting strings to arrays or lists, and vice versa.
 *
 * @author Angus Mark
 */
public class ArrayUtils {

    /**
     * Takes an Object[] and determines if this array has values.
     *
     * @param objects
     * @return true if the array is null, or length is equal 0, false for all other cases.
     */
    public static boolean isEmpty(Object[] objects) {
        if (objects == null) return true;
        if (objects.length == 0) return true;
        return false;
    }

    /**
     * Takes a byte[] and determines if this array has values.
     *
     * @param bytes
     * @return true if the array is null, or length is equal 0, false for all other cases.
     */
    public static boolean isEmpty(byte[] bytes) {
        if (bytes == null) return true;
        if (bytes.length == 0) return true;
        return false;
    }

    /**
     * Method given a string[] chekcs the values are not empty strings.
     *
     * @param values
     * @return true if any one of the items has a valid value, false if the array is null, size of 0, or all of the values
     *         have empty or white space as values.
     */
    public static boolean hasText(String[] values) {
        if (isEmpty(values)) return false;
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (StringUtils.hasText(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes a long[] and determines if this array has values.
     *
     * @param longValues
     * @return true if the array is null, or length is equal 0, false for all other cases.
     */
    public static boolean isEmpty(long[] longValues) {
        if (longValues == null) return true;
        if (longValues.length == 0) return true;
        return false;
    }

    /**
     * Converts long array to Long array.
     *
     * @param longParameters
     * @return Long[]
     */
    public static Long[] convert(long[] longParameters) {
        if (isEmpty(longParameters)) return new Long[0];

        Long[] result = new Long[longParameters.length];

        for (int i = 0; i < longParameters.length; i++) {
            long longParameter = longParameters[i];
            result[i] = new Long(longParameter);
        }
        return result;
    }

    /**
     * Check if array contains text to find (case insensitive.)
     *
     * @param array
     * @param textToFind
     * @return true or false
     */
    public static boolean contains(String[] array, String textToFind) {

        if (textToFind != null) {
            for (int i = 0; i < array.length; i++) {
                String item = array[i];
                if (textToFind.equalsIgnoreCase(item)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Converts a delimited String into an array.
     *
     * @param stringRep The delimited String representation of the array
     * @param delimiter The delimiter used in stringRep
     * @param componentType The type of the array components.  This can be a primative,
     * a String or any class that contains a contructor taking a single String parameter
     * @return an array
     */
    public static Object stringToArray(String stringRep, String delimiter, Class componentType) {
        Object array;

        String[] tokens = stringRep.split(delimiter);
        int length = tokens.length;

        if (componentType.equals(String.class)) {
            array = tokens;
        } else {
            array = Array.newInstance(componentType, length);

            if (componentType.isPrimitive()) {
                if (componentType.equals(Integer.TYPE)) {
                    for (int i = 0; i < length; i++) {
                        Array.setInt(array, i, Integer.parseInt(tokens[i]));
                    }
                } else if (componentType.equals(Double.TYPE)) {
                    for (int i = 0; i < length; i++) {
                        Array.setDouble(array, i, Double.parseDouble(tokens[i]));
                    }
                } else if (componentType.equals(Float.TYPE)) {
                    for (int i = 0; i < length; i++) {
                        Array.setFloat(array, i, Float.parseFloat(tokens[i]));
                    }
                } else if (componentType.equals(Short.TYPE)) {
                    for (int i = 0; i < length; i++) {
                        Array.setShort(array, i, Short.parseShort(tokens[i]));
                    }
                } else if (componentType.equals(Byte.TYPE)) {
                    for (int i = 0; i < length; i++) {
                        Array.setByte(array, i, Byte.parseByte(tokens[i]));
                    }
                } else if (componentType.equals(Long.TYPE)) {
                    for (int i = 0; i < length; i++) {
                        Array.setLong(array, i, Long.parseLong(tokens[i]));
                    }
                } else if (componentType.equals(Character.TYPE)) {
                    for (int i = 0; i < length; i++) {
                        Array.setChar(array, i, tokens[i].charAt(0));
                    }
                } else if (componentType.equals(Boolean.TYPE)) {
                    for (int i = 0; i < length; i++) {
                        Array.setBoolean(array, i, new Boolean(tokens[i]).booleanValue());
                    }
                }
            } else {
                try {
                    Constructor con = componentType.getConstructor(signature);
                    for (int i = 0; i < length; i++) {
                        Object element = con.newInstance(tokens[i]);
                        Array.set(array, i, element);
                    }
                } catch (Exception e) {
                    logger.error("Failed to instantiate array component of type: " + componentType, e);
                    array = null;  // return null
                }
            }
        }

        return array;
    }

    /**
     * Converts a delimited String into a List.
     *
     * @param stringRep The delimited String representation of the List
     * @param delimiter The delimiter used in stringRep
     * @param componentType The type of the List components.  This can be a primative,
     * a String or any class that contains a contructor taking a single String parameter.
     * Primatives will be promoted to their container Object type
     * @return an array
     */
    public static List<String> stringToList(String stringRep, String delimiter, Class componentType) {
        String[] tokens = stringRep.split(delimiter);
        int length = tokens.length;

        List list;

        if (componentType.equals(String.class)) {
            list = Arrays.asList(tokens);
        } else if (componentType.equals(Character.TYPE)) {
            list = new ArrayList(length);
            for (int i = 0; i < length; i++) {
                list.add(new Character(tokens[i].charAt(0)));
            }
        } else {
            try {
                Constructor con;
                if (componentType.isPrimitive())
                    con = (Constructor) primitiveWrapperConstructors.get(componentType);
                else
                    con = componentType.getConstructor(signature);

                list = new ArrayList(length);
                for (int i = 0; i < length; i++) {
                    Object element = con.newInstance(tokens[i]);
                    list.add(element);
                }
            } catch (Exception e) {
                logger.error("Failed to instantiate array component of type: " + componentType, e);
                list = null;  // return null
            }
        }

        return list;
    }

    /**
     * Converts an array into a delimited String.
     *
     * @param array the array to convert
     * @param delimiter The delimiter used in the String representation
     * @return a delimited String representation of the array
     */
    public static String arrayToString(Object array, String delimiter) {
        return arrayToString(array, delimiter, null);
    }

    /**
     * Builds a string from the given array.
     * <br/>
     * The returned value is delimited by the given delimiter and if the enclosingCharacter is not null will
     * wrap each of the values in the given character.
     *
     * @param array the array of Objects to convert
     * @param delimiter the delimiter to use to separate the values
     * @param enclosingCharacter the character to use to enclose the values if not null
     * @return String
     */
    public static String arrayToString(Object array, String delimiter, String enclosingCharacter) {
        int length = Array.getLength(array);

        if (length == 0)
            return "";

        StringBuffer sb = new StringBuffer(2 * length - 1);

        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(delimiter);
            }
            if (enclosingCharacter != null) {
                sb.append(enclosingCharacter).append(Array.get(array, i)).append(enclosingCharacter);
            } else {
                sb.append(Array.get(array, i));
            }
        }

        return sb.toString();
    }

    /**
     * Builds a string from the given list.
     * <br/>
     * The string result is delimited by the given delimiter and if the enclosing character is not null wraps each of the values
     * with the enclosingCharacter parameter.
     *
     * @param list the list <tt> instanceof List</tt> to convert
     * @param delimiter the delimiter to use to separate the values with
     * @param enclosingCharacter the character to wrap each of the values with
     * @return String
     */
    public static String listToString(List list, String delimiter, String enclosingCharacter) {
        int size = list.size();

        if (size == 0)
            return "";

        StringBuffer sb = new StringBuffer(2 * size - 1);

        boolean first = true;
        for (Iterator i = list.iterator(); i.hasNext();) {
            if (!first) {
                sb.append(delimiter);
            }
            if (enclosingCharacter != null) {
                sb.append(enclosingCharacter).append(i.next()).append(enclosingCharacter);
            } else {
                sb.append(i.next());
            }
            first = false;
        }
        return sb.toString();
    }

    /**
     * Converts a List into a delimited String.
     *
     * @param list the List to convert
     * @param delimiter The delimiter used in the String representation
     * @return a delimited String representation of the array
     */
    public static String listToString(List list, String delimiter) {
        return listToString(list, delimiter, null);
    }

    /**
     * Copy the element in the src array at the specified position to the specified position
     * in the destination array.
     * <br> The componentType is used to cast the element correctly.
     * <br> Handles all primitives and Objects.
     *
     * @param src The src array.
     * @param srcPos The position in the src array to copy from.
     * @param dest The destination array.
     * @param destPos The position in the destination array to copy to.
     * @param componentType The component type being copied.
     */
    public static void copy(Object src, int srcPos, Object dest, int destPos, Class componentType) {
        if (componentType.isPrimitive()) {
            if (componentType.equals(Integer.TYPE)) {
                int value = Array.getInt(src, srcPos);
                Array.setInt(dest, destPos, value);
            } else if (componentType.equals(Double.TYPE)) {
                double value = Array.getDouble(src, srcPos);
                Array.setDouble(dest, destPos, value);
            } else if (componentType.equals(Float.TYPE)) {
                float value = Array.getFloat(src, srcPos);
                Array.setFloat(dest, destPos, value);
            } else if (componentType.equals(Short.TYPE)) {
                short value = Array.getShort(src, srcPos);
                Array.setShort(dest, destPos, value);
            } else if (componentType.equals(Byte.TYPE)) {
                byte value = Array.getByte(src, srcPos);
                Array.setByte(dest, destPos, value);
            } else if (componentType.equals(Long.TYPE)) {
                long value = Array.getLong(src, srcPos);
                Array.setLong(dest, destPos, value);
            } else if (componentType.equals(Character.TYPE)) {
                char value = Array.getChar(src, srcPos);
                Array.setChar(dest, destPos, value);
            } else if (componentType.equals(Boolean.TYPE)) {
                boolean value = Array.getBoolean(src, srcPos);
                Array.setBoolean(dest, destPos, value);
            }
        } else {
            Object value = Array.get(src, srcPos);
            Array.set(dest, destPos, value);
        }
    }

    /**
     * The Logger.
     */
    private static final Log logger = LogFactory.getLog(ArrayUtils.class);

    /**
     * Signature for constructor for object instantiation.
     */
    private static final Class[] signature = {String.class};

    /**
     * A Map from primative Class object to the Constructor of the
     * corresponding primative wrapper Class.
     */
    private static final Map primitiveWrapperConstructors = new HashMap();

    static {
        try {
            primitiveWrapperConstructors.put(Integer.TYPE, Integer.class.getConstructor(signature));
            primitiveWrapperConstructors.put(Double.TYPE, Double.class.getConstructor(signature));
            primitiveWrapperConstructors.put(Float.TYPE, Float.class.getConstructor(signature));
            primitiveWrapperConstructors.put(Short.TYPE, Short.class.getConstructor(signature));
            primitiveWrapperConstructors.put(Byte.TYPE, Byte.class.getConstructor(signature));
            primitiveWrapperConstructors.put(Long.TYPE, Long.class.getConstructor(signature));
            primitiveWrapperConstructors.put(Boolean.TYPE, Boolean.class.getConstructor(signature));
        } catch (Exception e) {
            // This should never happen
            throw new IllegalStateException(e.getClass() + " : " + e.getMessage());
        }
    }

    /**
     * Constructor with private access to prevent instantiation.
     */
    private ArrayUtils() {
    }
}
