/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.calculations;

import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.DateStrategy;
import com.zynap.talentstudio.calculations.SubtractDatesStrategy;
import com.zynap.talentstudio.calculations.AddDatesStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 10-Jul-2007 13:22:11
 */
public class CalculationFactory {

    private CalculationFactory() {
        // no instantiation
    }

    public static CalculationFactory getInstance() {

        if (instance == null) {
            instance = new CalculationFactory();
        }
        return instance;
    }

    public Object evaluateDate(Object value1, Expression expression1, Object value2, String operator, int format) {
        return dateStrategies.get(operator).execute(value1, expression1, value2, format);
    }

    private static CalculationFactory instance = null;

    private static Map<String, DateStrategy> dateStrategies = new HashMap<String, DateStrategy>(2);

    static {
        dateStrategies.put("-", new SubtractDatesStrategy());
        dateStrategies.put("+", new AddDatesStrategy());
    }
}
