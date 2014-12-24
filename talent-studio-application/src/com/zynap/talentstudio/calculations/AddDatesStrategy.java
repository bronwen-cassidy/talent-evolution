/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.calculations;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

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
public class AddDatesStrategy extends DateStrategy {

    public Object execute(Number value1, Expression expression1, Date value2, int format) {
        DateTime dt2 = new DateTime(value2);
        switch (expression1.getFormat()) {
            case YEARS:
                return dt2.plusYears(value1.intValue()).toDate();
            case MONTHS:
                return dt2.plusMonths(value1.intValue()).toDate();
            case DAYS:
                return dt2.plusDays(value1.intValue()).toDate();
            default:
               return dt2.plusYears(value1.intValue()).toDate();
        }
    }

    /**
     * Adds two Dates!! value1 is converted to a mutable date and then the days, months and years are added from value2.
     * @param value1 the first date which has it's fields added to
     * @param value2 the date from which the fields are read from
     * @param format this is ignored in this case as the format is a date format
     * @return a new Date object representing the concatenation of the 2 dates
     */
    public Object execute(Date value1, Date value2, int format) {
        DateTime dt1 = new DateTime(value1);
        DateTime dt2 = new DateTime(value2);
        MutableDateTime mutableDateTime = dt1.toMutableDateTime();
        mutableDateTime.addDays(dt2.getDayOfMonth());
        mutableDateTime.addMonths(dt2.getMonthOfYear());
        mutableDateTime.addYears(dt2.getYear());
        return mutableDateTime.toDate();
    }

    public Object execute(Number value1, Number value2) {
        return value1.intValue() + value2.intValue();
    }

    public static final int NO_FORMAT = 0;
    public static final int YEARS = 1;
    public static final int MONTHS = 2;
    public static final int DAYS = 3;
    public static final int WEEKYEARS = 4;
    public static final int WEEKS = 5;
    public static final int HOURS = 101;
    public static final int MINUTES = 102;
    public static final int SECONDS = 103;
    public static final int DATE = 104;
}
