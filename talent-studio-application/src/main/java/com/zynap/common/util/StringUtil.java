/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.common.util;

import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class StringUtil {

    /**
     * Check that there are no characters invalid for XML in the String.
     *
     * @param xmlString
     * @return true or false
     */
    public static String validateXml(String xmlString) {
        StringBuffer result = new StringBuffer();
        char[] characters = xmlString.toCharArray();
        for (int j = 0; j < _illegalXmlChars.length; j++) {
            char illegalChar = _illegalXmlChars[j];
            for (int i = 0; i < characters.length; i++) {
                char character = characters[i];
                if (character != illegalChar) {
                    result.append(character);
                } else {
                    // we have an illegal character, append a space instead
                    result.append(_replaceString);
                }
            }
        }
        return result.toString();
    }

    /**
     * Check if value passed in contains any of the Strings in the array.
     *
     * @param value
     * @param containingValues
     * @return true or false
     */
    public static boolean containsAny(String value, String[] containingValues) {
        for (int i = 0; i < containingValues.length; i++) {
            String conatainingValue = containingValues[i];
            if (conatainingValue == null) return false;
            if (!org.apache.commons.lang.StringUtils.containsNone(value, conatainingValue.toCharArray())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a String containing the invocation stack trace of an exception
     *
     * @param ex the exception you want the stack trace of
     * @return the invocation stack trace of an exception
     */
    public static String getStackTraceAsString(Throwable ex) {
        // We let Java print the stack trace to a buffer in memory to be able to
        // get it as String:
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PrintStream buffer = new PrintStream(baos);
            ex.printStackTrace(buffer);
            buffer.close();
        } catch (Exception willNeverBeThrown) {
        }

        return baos.toString();
    }

    /**
     * Convert String parameters to boolean - we currently use "T" for true and "F" for false.
     * <br> Returns false if parameter is null.
     *
     * @param parameter The parameter
     * @return True if parameter equals "T" or "t"
     */
    public static boolean convertToBoolean(String parameter) {
        return TRUE.equalsIgnoreCase(parameter);
    }

    /**
     * Convert boolean to {@link #TRUE} or {@link #FALSE}.
     *
     * @param value
     * @return {@link #TRUE} or {@link #FALSE}
     */
    public static String convertToString(boolean value) {
        return value ? TRUE : FALSE;
    }

    public static boolean equalsAny(String target, char[] matchTokens) {
        if (target == null) return false;
        if (matchTokens == null) return false;
        for (int i = 0; i < matchTokens.length; i++) {
            char matchToken = matchTokens[i];
            String token = new Character(matchToken).toString();
            if (target.equals(token)) {
                return true;
            }
        }
        return false;
    }

    public static boolean notEqualsAny(String target, char[] tokens) {
        return !equalsAny(target, tokens);
    }

    public static String clean(String input) {
        if(!StringUtils.hasText(input)) return input;
        return StringUtils.deleteAny(input, "&?!-<>\\/ ");
    }
    
    public static <E> String listToDelimitedString(Collection<E> items, String delimiter) {
    	return wrappedListToDelimitedString(items, delimiter, "");
    }

	public static <E> String wrappedListToDelimitedString(Collection<E> items, String delimiter, String wrapper) {
		StringBuilder builder = new StringBuilder();
		int index = 1;
		for (E item : items) {
			builder.append(wrapper).append(item).append(wrapper);
			if(index++ < items.size()) builder.append(delimiter);
		}
		return builder.toString();
	}

    public static Long[] convertToLongArray(String[] input) {
        if(input.length < 1) return new Long[0];
        Long[] results = new Long[input.length];
        for (int i = 0; i < input.length; i++) {
            String item = input[i];
            try {
                results[i] = Long.valueOf(item);
            } catch (NumberFormatException e) {
                i--;
            }
        }
        return results;
    }

    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";

    private static final char[] _illegalXmlChars = new char[]{0xb};
    private static final String _replaceString = " ";

    public static final String TRUE = "T";
    public static final String FALSE = "F";
}
