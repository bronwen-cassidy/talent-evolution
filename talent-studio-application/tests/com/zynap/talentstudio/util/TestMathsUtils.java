/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.util;

import com.zynap.talentstudio.ZynapTestCase;

/**
 * Class or Interface description.
 *
 * @author acalderwood
 * @version 0.1
 * @since 08-Dec-2006 10:21:23
 */

public class TestMathsUtils extends ZynapTestCase {

    private Integer int_minus_10, int_minus_1, int_0, int_1, int_10, int_20, int_200;
    private Double dbl_minus_10, dbl_minus_1, dbl_1, dbl_1_point_5, dbl_10, dbl_20, dbl_200;

    protected void setUp() throws Exception {

        super.setUp();
        int_minus_10 = new Integer(-10);
        int_minus_1 = new Integer(-1);
        int_0 = new Integer(0);
        int_1 = new Integer(1);
        int_10 = new Integer(10);
        int_20 = new Integer(20);
        int_200 = new Integer(200);

        dbl_minus_10 = new Double(-10.0);
        dbl_minus_1 = new Double(-1.0);
        dbl_1 = new Double(1.0);
        dbl_1_point_5 = new Double(1.5);
        dbl_10 = new Double(10.0);
        dbl_20 = new Double(20.0);
        dbl_200 = new Double(200.0);
    }

    public void testAdd() throws Exception {
        assertEquals(MathsUtils.add(int_10, int_10), int_20);
        assertEquals(MathsUtils.add(int_10, dbl_10), int_20);
        assertEquals(int_0, MathsUtils.add(int_10, int_minus_10));
    }

    public void testSubtract() throws Exception {
        assertEquals(int_10, MathsUtils.subtract(int_20, int_10));
        assertEquals(int_10, MathsUtils.subtract(int_20, dbl_10));
        assertEquals(int_20, MathsUtils.subtract(int_10, int_minus_10));
    }

    public void testMultiply() throws Exception {
        assertEquals(int_200, MathsUtils.multiply(int_20, int_10));
        assertEquals(int_200, MathsUtils.multiply(int_20, dbl_10));
        assertEquals(int_minus_10, MathsUtils.multiply(int_10, dbl_minus_1));
    }

    public void testDivide() throws Exception {
        assertEquals(int_20, MathsUtils.divide(int_200, int_10));
        assertEquals(int_20, MathsUtils.divide(int_200, dbl_10));
        assertEquals(int_minus_10, MathsUtils.divide(int_10, dbl_minus_1));
    }

    public void testGreaterThan() throws Exception {
        assertTrue(MathsUtils.greaterThan(int_1, int_0));
        assertFalse(MathsUtils.greaterThan(int_0, int_1));
        assertFalse(MathsUtils.greaterThan(int_1, int_1));
    }

    public void testEqualTo() throws Exception {
        assertTrue(MathsUtils.equalTo(int_1, int_1));
        assertTrue(MathsUtils.equalTo(int_1, dbl_1));
        assertFalse(MathsUtils.equalTo(int_1, int_10));
    }

    public void testLessThan() throws Exception {
        assertTrue(MathsUtils.lessThan(int_0, int_1));
        assertFalse(MathsUtils.lessThan(int_1, int_0));
        assertFalse(MathsUtils.lessThan(int_1, int_1));
    }

    public void testRemoveZeroFraction() throws Exception {
        assertTrue(int_1.toString().equals(MathsUtils.removeZeroFraction(dbl_1).toString()));
        assertFalse(int_1.toString().equals(MathsUtils.removeZeroFraction(dbl_1_point_5).toString()));
    }
}
