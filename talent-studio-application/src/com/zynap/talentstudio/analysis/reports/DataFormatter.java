package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.organisation.attributes.AttributeValue;

import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Class used by Jasper based reports to format data correctly.
 *
 * User: jsueiras
 * Date: 27-Jan-2006
 * Time: 16:43:49
 */
public final class DataFormatter {

    /**
     * Format value based on column definition.
     *
     * @param column
     * @param object
     * @return the value, or the colour if the column is meant to be displayed as colours, or ReportConstants.BLANK_VALUE if object is null.
     */
    public static String formatValue(Column column, Object object) {
        if (object == null) return ReportConstants.BLANK_VALUE;

        if (column.isColorDisplayable()) {
            return getColorCellValue(column, object);
        } else {
            return formatValue(object);
        }
    }

    /**
     * Format number to specified number of decimal places.
     *
     * @param number
     * @param decimalPlaces
     * @return The number's original value or the number formatted to the number of decimal places or ReportConstants.BLANK_VALUE if number is null.
     */
    public static String formatValue(Number number, Integer decimalPlaces) {
        return formatValue(number, decimalPlaces.intValue());
    }

    /**
     * Format number to specified number of decimal places.
     *
     * @param number
     * @param decimalPlaces
     * @return The number's original value or the number formatted to the number of decimal places, or ReportConstants.BLANK_VALUE if number is null.
     */
    public static String formatValue(Number number, int decimalPlaces) {
        if (number == null) return ReportConstants.BLANK_VALUE;

        final double doubleValue = number.doubleValue();
        if (Double.isInfinite(doubleValue) || Double.isNaN(doubleValue)) {
            return ReportConstants.NaN;
        }

        if (decimalPlaces == -1) return String.valueOf(number);

        StringBuffer buffer = new StringBuffer("0");
        if (decimalPlaces > 0) {
            buffer.append(".");
        }
        
        for (int i = 0; i < decimalPlaces; i++) {
            buffer.append("0");
        }

        NumberFormat f = new DecimalFormat(buffer.toString());
        return f.format(number);
    }

    /**
     * Format object value.
     * <br/> If the object is null returns ReportConstants.BLANK_VALUE.
     * <br/> If the object is an AttributeValue returns attributeValue.getDisplayValue().
     * <br/> Otherwise returns object.toString().
     *
     * @param object
     * @return String
     */
    public static String formatValue(Object object) {
        if (object == null) return ReportConstants.BLANK_VALUE;

        if (object instanceof AttributeValue) {
            final AttributeValue attributeValue = (AttributeValue) object;
            return attributeValue.getDisplayValue();
        }

        return object.toString();
    }

    /**
     * Convert object value to double (used for functions.)
     *
     * @param o
     * @return object's value as double, or Double.NAN if object is null
     */
    public static double valueToNumber(Object o) {
        if (o == null) return Double.NaN;
        if (!StringUtils.hasText(o.toString())) return Double.NaN;
        final String s = formatValue(o);
        // tokenize string based on "," in case it has multiple values (this happens with numeric fields from a dynamic line item)
        double value = 0;
        boolean hasValue = false;
        final String[] strings = StringUtils.commaDelimitedListToStringArray(s);
        for (int i = 0; i < strings.length; i++) {
            final String string = strings[i];
            if (StringUtils.hasText(string) && Report.hasValue(string)) {
                hasValue = true;
                value += new Double(string).doubleValue();
            }
        }
        if(!hasValue) {
            value = Double.NaN;
        }
        // format to n decimal places (to consider)
        return value;
    }

    /**
     * Get colour value.
     * @param column
     * @param value
     * @return String
     */
    public static String getColorCellValue(Column column, Object value) {
        if (value instanceof AttributeValue) {
            AttributeValue attributeValue = (AttributeValue) value;
            return column.getColumnDisplayImageValue(attributeValue.getSelectionLookupValue());
        }
        if (value != null && Report.hasNoValue(value.toString())) {
            return null;
        }
        return value != null ? value.toString() : null;
    }

    public static final String EMPTY_VALUE = null;
}
