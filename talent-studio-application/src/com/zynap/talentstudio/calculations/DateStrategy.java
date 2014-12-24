/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.calculations;

import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.DateCalculation;

import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 10-Jul-2007 13:29:06
 */
public abstract class DateStrategy {

    public Object execute(Object value1, Expression expression1, Object value2, int format) {

        if(value1 instanceof Date && value2 instanceof Date) {
            return execute((Date)value1, (Date)value2, format);

        } else if(value1 instanceof Number && value2 instanceof Date) {
            return execute((Number)value1, expression1, (Date) value2, format);

        } else if(value1 instanceof Date && value2 instanceof Number) {
            return execute((Number) value2, expression1, (Date)value1, format);

        } else if(value1 instanceof Number && value2 instanceof Number) {
            if(DateCalculation.MONTHS == expression1.getFormat()) {
                // value two needs to be represented as a fraction so 3 months actually becomes 0.3 months
                // if value = 36 months is
                // months will always be an integer value between 0 and 12
                value2 = (((Number) value2).doubleValue() / 10);
            }
            return execute((Number)value1, ((Number) value2));
            
        }
        return null;
    }

    public abstract Object execute(Number value1, Expression expression1, Date value2, int format);

    public abstract Object execute(Date value1, Date value2, int format);

    public abstract Object execute(Number value1, Number value2);
}
