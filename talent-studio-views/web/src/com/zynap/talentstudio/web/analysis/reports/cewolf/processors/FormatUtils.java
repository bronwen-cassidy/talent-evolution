/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-Mar-2006 11:25:39
 */
public class FormatUtils {

    public static final NumberFormat applyPattern(NumberFormat numberFormat, int decimalPlaces) {

        if (decimalPlaces > 0) {
            String format = buildStringFormat(decimalPlaces).toString();
            ((DecimalFormat) numberFormat).applyPattern(format);
        }

        // todo return new instead
        return numberFormat;
    }

    public static NumberFormat applyPercentPattern(NumberFormat percentFormat, int decimalPlaces) {

        if (decimalPlaces > 0) {
            StringBuffer buffer = buildStringFormat(decimalPlaces);
            ((DecimalFormat)percentFormat).applyLocalizedPattern(buffer.append("%").toString());
        }

        return percentFormat;
    }

    private static StringBuffer buildStringFormat(int decimalPlaces) {

        StringBuffer buffer = new StringBuffer("0");
        if (decimalPlaces > 0) {
            buffer.append(".");
        }
        for (int i = 0; i < decimalPlaces; i++) {
            buffer.append("0");
        }

        return buffer;
    }
}
