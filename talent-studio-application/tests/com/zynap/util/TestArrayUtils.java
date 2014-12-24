package com.zynap.util;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TestArrayUtils extends TestCase {

    public void testArrayToStringLengthOne() throws Exception {
        String expected = "hello";
        String[] test = new String[] {expected};

        String actual = ArrayUtils.arrayToString(test, "+");
        assertEquals(expected, actual);
    }

    public void testArrayToStringFieldObject() throws Exception {
        Object objects[] = new Object[] {new Long(12), new Double(2.0), Boolean.TRUE};
        String expected="12,2.0,true";
        String actual = ArrayUtils.arrayToString(objects, ",");
        assertEquals(expected, actual);
    }

    public void testArrayToStringNotArray() throws Exception {
        String array = "Test";
        try {
            ArrayUtils.arrayToString(array, ":");
            fail("Should have thrown an illegal argument exception");
        } catch (Exception e) {
            // expected
        }
    }

    public void testArrayToStringPrimitives() throws Exception {
        int[] values = new int[] {1, 2, 3, 4, 5};
        String expected = "1:2:3:4:5";
        String actual = ArrayUtils.arrayToString(values, ":");
        assertEquals(expected, actual);
    }

    public void testIsEmpty() throws Exception {
        Object[] objectsNull = null;
        Object[] objectsEmpty = new Object[0];
        assertTrue(ArrayUtils.isEmpty(objectsNull));
        assertTrue(ArrayUtils.isEmpty(objectsEmpty));
    }

    public void testHasText() throws Exception {
        String[] text = {"not", "empty"};
        String[] empty = {"", ""};
        assertTrue(ArrayUtils.hasText(text));
        assertFalse(ArrayUtils.hasText(empty));
    }

    public void testContains() throws Exception {
        String toFind = "text to find";
        String notFind = "text not to find";
        String[] arrayToTest = {"text to find", "other", "text"};
        assertTrue(ArrayUtils.contains(arrayToTest, toFind));
        assertFalse(ArrayUtils.contains(arrayToTest, notFind));
    }

    public void testListToString() throws Exception {
        List testList = new ArrayList();
        testList.add("test1");
        testList.add("test2");
        String expected = "test1test2";
        String actual = ArrayUtils.listToString(testList, "");
        assertEquals(expected, actual);
      }
}