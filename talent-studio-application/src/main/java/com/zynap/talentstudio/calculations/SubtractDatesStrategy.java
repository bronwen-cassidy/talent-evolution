/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.calculations;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import static com.zynap.talentstudio.calculations.DateCalculation.*;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.DateStrategy;

import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 10-Jul-2007 13:28:01
 */
public class SubtractDatesStrategy extends DateStrategy {

    public Object execute(Number value1, Expression expression1, Date value2, int format) {

        DateTime dt2 = new DateTime(value2);

        switch (expression1.getFormat()) {
            case YEARS:
                return dt2.minusYears(value1.intValue()).toDate();
            case MONTHS:
                return dt2.minusMonths(value1.intValue()).toDate();
            case DAYS:
                return dt2.minusDays(value1.intValue()).toDate();
            default:
               return dt2.minusYears(value1.intValue()).toDate(); 
        }
    }

    /**
     * Represents a duration
     * @param value1 a date
     * @param value2 another date
     * @param format an int constant representing what we are interested in eg years? months? days?
     * @return returns a number or a string that can be represented as a double
     */
    public Object execute(Date value1, Date value2, int format) {

        DateTime dt1 = new DateTime(value1);
        DateTime dt2 = new DateTime(value2);

        switch (format) {
            case YEARS: {
                Period period1 = new Period(dt2, dt1, PeriodType.years());
                return period1.getYears();
            }
            case MONTHS: {
                Period period1 = new Period(dt2, dt1, PeriodType.months());
                return period1.getMonths();
            }
            case DAYS: {
                Period period1 = new Period(dt2, dt1, PeriodType.days());
                return period1.getDays();
            }
            default: {
                // handles years, months and days
                Period period1 = new Period(dt2, dt1, PeriodType.yearMonthDay());
                Double years = new Double(period1.getYears());

                if(format == YEARS_MONTHS_DAYS)
                    return years + new Double(period1.getMonths())/10 + new Double(period1.getDays())/100;
                else if(format == YEARS_MONTHS) {
                    return years + new Double(period1.getMonths())/10;
                } else {
                    //if(years <= 0) return new Double(period1.getMonths())/10;
                    //return years;
                    return new Integer(years.intValue());
                }
            }
        }
    }

    public Object execute(Number value1, Number value2) {
        return value1.intValue() - value2.intValue();
    }
}
