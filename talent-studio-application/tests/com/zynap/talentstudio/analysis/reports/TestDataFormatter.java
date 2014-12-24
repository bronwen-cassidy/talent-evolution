package com.zynap.talentstudio.analysis.reports;

/**
 * User: amark
 * Date: 28-Feb-2006
 * Time: 15:25:24
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

public class TestDataFormatter extends ZynapTestCase {

    public void testNullValueToNumber() throws Exception {
        assertTrue(Double.isNaN(DataFormatter.valueToNumber(null)));
    }

    public void testValueToNumber() throws Exception {
        double value = -10.09;

        final AttributeValue attributeValue = AttributeValue.create(Double.toString(value), DynamicAttribute.DA_TYPE_NUMBER_O);
        assertEquals(value, DataFormatter.valueToNumber(attributeValue));
    }

    public void testDynamicLineItemValueToNumber() throws Exception {

        double value1 = 1.2;
        double value2 = 2.5;

        final AttributeValue attributeValue = AttributeValue.create(Double.toString(value1), DynamicAttribute.DA_TYPE_NUMBER_O);
        attributeValue.addValue(new NodeExtendedAttribute(Double.toString(value1), attributeValue.getDynamicAttribute()), true);
        attributeValue.addValue(new NodeExtendedAttribute(Double.toString(value2), attributeValue.getDynamicAttribute()), true);
        attributeValue.addValue(new NodeExtendedAttribute(Double.toString(value2), attributeValue.getDynamicAttribute()), true);

        assertEquals((value1 + value1) + (value2 + value2), DataFormatter.valueToNumber(attributeValue));
    }

    public void testFormatValue() throws Exception {

        double value = 135.89;

        final AttributeValue attributeValue = AttributeValue.create(Double.toString(value), DynamicAttribute.DA_TYPE_NUMBER_O);
        assertEquals(Double.toString(value), DataFormatter.formatValue(attributeValue));
    }

    public void testFormatNullValue() throws Exception {
        final String output = DataFormatter.formatValue(null);
        assertEquals(ReportConstants.BLANK_VALUE, output);
    }

    public void testFormatToTwoDecimalPlaces() throws Exception {
        testFormatting("22.09000000000", "22.09", 2);
    }

    public void testFormatWholeNumbers() throws Exception {
        testFormatting("22", "22.00", 2);
    }

    public void testFormatUnspecifiedDecimalPlaces() throws Exception {
        testFormatting("-20.0897", "-20.0897", -1);
    }

    public void testFormatNullValueToTwoDecimalPlaces() throws Exception {
        final String output = DataFormatter.formatValue((Number) null, 2);
        assertEquals(ReportConstants.BLANK_VALUE, output);
    }

    public void testFormatValueNullObjectAndColumn() throws Exception {
        final String output = DataFormatter.formatValue(new Column(), null);
        assertEquals(ReportConstants.BLANK_VALUE, output);
    }

    public void testFormatInfinity() throws Exception {
        String output = DataFormatter.formatValue(new Double(Double.POSITIVE_INFINITY), 2);
        assertEquals(ReportConstants.NaN, output);

        output = DataFormatter.formatValue(new Double(Double.NEGATIVE_INFINITY), 2);
        assertEquals(ReportConstants.NaN, output);
    }

    public void testFormatNaN() throws Exception {
        String output = DataFormatter.formatValue(new Double(Double.NaN), 2);
        assertEquals(ReportConstants.NaN, output);
    }

    private void testFormatting(String actual, String expected, int numberOfDecimalPlaces) {
        String formattedValue = DataFormatter.formatValue(new Double(actual), numberOfDecimalPlaces);
        assertEquals(expected, formattedValue);
    }
}