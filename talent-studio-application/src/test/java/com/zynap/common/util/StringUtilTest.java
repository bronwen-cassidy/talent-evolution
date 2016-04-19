package com.zynap.common.util;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class StringUtilTest {

    @Test
    public void testContainsAny() throws Exception {
        String testValue = "Have\nToday";
        boolean actual = StringUtil.containsAny(testValue, new String[] {"\n", "\r\n", "\""});
        assertEquals(true, actual);
    }

    @Test
    public void testContainsAnyQuotes() throws Exception {
        String testValue = "Have\"Today";
        boolean actual = StringUtil.containsAny(testValue, new String[] {"\""});
        assertEquals(true, actual);
    }

    @Test
    public void testContainsAnyNull() throws Exception {
        String testValue = "Have\"Today";
        boolean actual = StringUtil.containsAny(testValue, new String[] {null});
        assertEquals(false, actual);
    }

    @Test
    public void testContainsAnyNothing() throws Exception {
        String testValue = "Have\"Today";
        boolean actual = StringUtil.containsAny(testValue, new String[] {""});
        assertEquals(false, actual);
    }

    @Test
    public void testConvertToBoolean() throws Exception {
        assertTrue(StringUtil.convertToBoolean("t"));
        assertFalse(StringUtil.convertToBoolean("f"));
        assertTrue(StringUtil.convertToBoolean("T"));
        assertFalse(StringUtil.convertToBoolean("F"));
     }
}