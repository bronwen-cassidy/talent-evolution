/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.util;

import freemarker.core.ArithmeticEngine;
import freemarker.template.TemplateException;

/**
 * Class or Interface description.
 *
 * @author acalderwood
 * @version 0.1
 * @since 08-Dec-2006 09:26:23
 */

public class MathsUtils {

    private static ArithmeticEngine.ConservativeEngine calculator = new ArithmeticEngine.ConservativeEngine();

    /**
     * Adds two numbers, performing widening conversions to the wrapper as needed.
     * Note that the result will be narrowed to an Integer if this can be done without loss of precision.
     * E.g. add(new Double(10.0), new Double(2.0)) returns an Integer whose value is 12.
     * @param first a Number
     * @param second the Number to be added to first
     * @return the result of 'first + second'
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static Number add(Number first, Number second) throws BadArithmeticException {
        try {
            return removeZeroFraction(calculator.add(first, second));
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }

    /**
     * Subtracts one number from another, performing widening conversions to the wrapper as needed.
     * Note that the result will be narrowed to an Integer if this can be done without loss of precision.
     * E.g. subtract(new Double(10.0), new Double(2.0)) returns an Integer whose value is 8.
     * @param first a Number
     * @param second the Number to be subtracted from first
     * @return the result of 'first - second'
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static Number subtract(Number first, Number second) throws BadArithmeticException {
        try {
            return removeZeroFraction(calculator.subtract(first, second));
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }

    /**
     * Multiplies two numbers, performing widening conversions to the wrapper as needed.
     * Note that the result will be narrowed to an Integer if this can be done without loss of precision.
     * E.g. multiply(new Double(10.0), new Double(2.0)) returns an Integer whose value is 20.
     * @param first a Number
     * @param second the Number to be multiplied with first
     * @return the result of 'first x second'
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static Number multiply(Number first, Number second) throws BadArithmeticException {
        try {
            return removeZeroFraction(calculator.multiply(first, second));
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }

    /**
     * Divides two numbers, performing widening conversions to the wrapper as needed.
     * Note that the result will be narrowed to an Integer if this can be done without loss of precision.
     * E.g. divide(new Double(10.0), new Double(2.0)) returns an Integer whose value is 5.
     * @param first a Number
     * @param second the Number to be multiplied with first
     * @return the result of 'first x second'
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static Number divide(Number first, Number second) throws BadArithmeticException {
        try {
            return removeZeroFraction(calculator.divide(first, second));
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }


    /**
     * Tests that the primitive wrapped within a Number is greater than another.
     * @param first a Number
     * @param second a test Number 
     * @return true if first is greater than second, otherwise false.
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static boolean greaterThan(Number first, Number second) throws BadArithmeticException {
        try {
            return calculator.compareNumbers(first, second) == 1;
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }

    /**
     * Tests that the primitive wrapped within a Number is greater than or equal to another.
     * @param first a Number
     * @param second a test Number
     * @return true if first is greater than or equal to second, otherwise false.
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static boolean greaterThanOrEqualTo(Number first, Number second) throws BadArithmeticException {
        try {
            return calculator.compareNumbers(first, second) >= 0;
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }

    /**
     * Tests that the primitive wrapped within a Number is less than another.
     * @param first a Number
     * @param second a test Number
     * @return true if first is less than second, otherwise false.
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static boolean lessThan(Number first, Number second) throws BadArithmeticException {
        try {
            return calculator.compareNumbers(first, second) == -1;
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }

    /**
     * Tests that the primitive wrapped within a Number is less than or equal to another.
     * @param first a Number
     * @param second a test Number 
     * @return true if first is less than or equal to second, otherwise false.
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static boolean lessThanOrEqualTo(Number first, Number second) throws BadArithmeticException {
        try {
            return calculator.compareNumbers(first, second) == -1;
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }

    /**
     * Tests that the primitive contents of the Number classes are equal, e.g. Double(1.0) is equals to Integer(1)
     * @param first a Number
     * @param second a test Number 
     * @return true if the Numbers are equal, otherwise false.
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static boolean equalTo(Number first, Number second) throws BadArithmeticException {
        try {
            return calculator.compareNumbers(first, second) == 0;
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }

    public static Number add(String first, String second) throws BadArithmeticException {
        try {
            Number one = calculator.toNumber(first);
            Number two = calculator.toNumber(second);
            return calculator.add(one, two);
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }

    /**
     * Narrows the wrapper class of a Number to an Integer if this can be done without loss of precision.
     * E.g. removeZeroFraction(new Double(10.0)) returns an Integer whose value is 10,
     * removeZeroFraction(new Double(10.5)) returns a Double whose value is 10.5.
     * @param num any subclass of the Number class
     * @return If the number can be safely cast without loss of precision, an Integer, otherwise a Double
     * @throws BadArithmeticException if the parameters are not of the Number class
     */
    public static Number removeZeroFraction(Number num) throws BadArithmeticException {
        try {
            if ((calculator.modulus(num, new Integer(1))).doubleValue() == 0.0) {
                return new Integer(num.intValue());
            } else {
                return num;
            }
        } catch (TemplateException te) {
            throw new BadArithmeticException(te.getMessage());
        }
    }
}
