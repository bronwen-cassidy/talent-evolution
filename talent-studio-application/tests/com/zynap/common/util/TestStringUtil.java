package com.zynap.common.util;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import junit.framework.TestCase;

public class TestStringUtil extends TestCase {

    public void testContainsAny() throws Exception {
        boolean expected = true;
        String testValue = "Have\nToday";
        boolean actual = StringUtil.containsAny(testValue, new String[] {"\n", "\r\n", "\""});
        assertEquals(expected, actual);
    }

    public void testContainsAnyQuotes() throws Exception {
        boolean expected = true;
        String testValue = "Have\"Today";
        boolean actual = StringUtil.containsAny(testValue, new String[] {"\""});
        assertEquals(expected, actual);
    }

    public void testContainsAnyNull() throws Exception {
        boolean expected = false;
        String testValue = "Have\"Today";
        boolean actual = StringUtil.containsAny(testValue, new String[] {null});
        assertEquals(expected, actual);
    }

    public void testContainsAnyNothing() throws Exception {
        boolean expected = false;
        String testValue = "Have\"Today";
        boolean actual = StringUtil.containsAny(testValue, new String[] {""});
        assertEquals(expected, actual);
    }       

    public void testConvertToBoolean() throws Exception {
        assertTrue(StringUtil.convertToBoolean("t"));
        assertFalse(StringUtil.convertToBoolean("f"));
        assertTrue(StringUtil.convertToBoolean("T"));
        assertFalse(StringUtil.convertToBoolean("F"));
     }
}