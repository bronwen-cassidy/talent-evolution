package com.zynap.talentstudio.web.organisation.attributes;

/**
 * User: amark
 * Date: 09-Feb-2005
 * Time: 15:43:33
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import com.zynap.talentstudio.web.organisation.attributes.validators.DateTimeAttributeValueValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.ErrorMessageHandler;
import com.zynap.talentstudio.web.organisation.attributes.validators.TimeAttributeValueValidator;

public class TestAttributeWrapperBean extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSetDate() throws Exception {
        
        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test1", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATE));
        String real = "2005-10-10";
        attributeWrapperBean.setDate(real);
        assertEquals(real, attributeWrapperBean.getValue());
    }

    public void testSetNullOrEmptyDate() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test2", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATE));
        attributeWrapperBean.setDate(BLANK_VALUE);
        String date = attributeWrapperBean.getDate();
        assertEquals(BLANK_VALUE, date);
        String value = attributeWrapperBean.getValue();
        assertEquals(BLANK_VALUE, value);
    }

    public void testSetDateTime() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test3", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATETIMESTAMP));
        String real = "2000-01-31";
        String timeHrs = "00";
        String timeMins = "00";
        attributeWrapperBean.setDate(real);
        attributeWrapperBean.setHour(timeHrs);
        attributeWrapperBean.setMinute(timeMins);
        String expected = real + DynamicAttribute.DATE_TIME_DELIMITER + timeHrs + DynamicAttribute.TIME_DELIMITER + timeMins;
        assertEquals(expected, attributeWrapperBean.getValue());
    }

    public void testSetDateAndHourNotMinute() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test3", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATETIMESTAMP));
        String real = "2000-01-31";
        String timeHrs = "00";
        String timeMins = BLANK_VALUE;
        attributeWrapperBean.setDate(real);
        attributeWrapperBean.setHour(timeHrs);
        attributeWrapperBean.setMinute(timeMins);
        assertEquals("This now should fail validation", "2000-01-31 00:", attributeWrapperBean.getValue());
        DateTimeAttributeValueValidator validator = new DateTimeAttributeValueValidator();
        final ErrorMessageHandler errorMessageHandler = validator.validate(attributeWrapperBean, null);
        assertNotNull("ErrorHandler is null should have had errors", errorMessageHandler);
        assertNotNull("ErrorHandler has no error key, should have as errors should be detected.", errorMessageHandler.getErrorKey());
    }

    public void testSetDateAndMinuteNotHour() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test3", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATETIMESTAMP));
        String real = "2000-01-31";
        String timeHrs = BLANK_VALUE;
        String timeMins = "10";
        attributeWrapperBean.setDate(real);
        attributeWrapperBean.setHour(timeHrs);
        attributeWrapperBean.setMinute(timeMins);
        assertEquals("Invalid time only the minutes set should fail validation", "2000-01-31 :10", attributeWrapperBean.getValue());
        DateTimeAttributeValueValidator validator = new DateTimeAttributeValueValidator();
        final ErrorMessageHandler errorMessageHandler = validator.validate(attributeWrapperBean, null);
        assertNotNull("ErrorHandler is null should have had errors", errorMessageHandler);
        assertNotNull("ErrorHandler has no error key, should have as errors should be detected.", errorMessageHandler.getErrorKey());

    }

    public void testSetTime() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test3", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_TIMESTAMP));
        String timeHrs = "12";
        String timeMins = "10";
        attributeWrapperBean.setHour(timeHrs);
        attributeWrapperBean.setMinute(timeMins);
        assertEquals("12:10", attributeWrapperBean.getValue());
        TimeAttributeValueValidator validator = new TimeAttributeValueValidator();
        final ErrorMessageHandler errorMessageHandler = validator.validate(attributeWrapperBean, null);
        assertNull("errors should have been null as there should have been no errors in the time set", errorMessageHandler);
    }

    public void testSetTimeNoMinutes() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test3", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_TIMESTAMP));
        String timeHrs = "12";
        String timeMins = BLANK_VALUE;
        attributeWrapperBean.setHour(timeHrs);
        attributeWrapperBean.setMinute(timeMins);
        assertEquals("12:", attributeWrapperBean.getValue());
        TimeAttributeValueValidator validator = new TimeAttributeValueValidator();
        final ErrorMessageHandler errorMessageHandler = validator.validate(attributeWrapperBean, null);
        assertNotNull("ErrorHandler is null should have had errors no minutes on the time provided", errorMessageHandler);
        assertNotNull("ErrorHandler has no error key, should have as errors should be detected.", errorMessageHandler.getErrorKey());
    }

    public void testSetTimeNoHours() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test3", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_TIMESTAMP));
        String timeHrs = BLANK_VALUE;
        String timeMins = "12";
        attributeWrapperBean.setHour(timeHrs);
        attributeWrapperBean.setMinute(timeMins);
        assertEquals(":12", attributeWrapperBean.getValue());
        TimeAttributeValueValidator validator = new TimeAttributeValueValidator();
        final ErrorMessageHandler errorMessageHandler = validator.validate(attributeWrapperBean, null);
        assertNotNull("ErrorHandler is null should have had errors no minutes on the time provided", errorMessageHandler);
        assertNotNull("ErrorHandler has no error key, should have as errors should be detected.", errorMessageHandler.getErrorKey());
    }

    public void testSetDateForATimeAttr() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test3", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_TIMESTAMP));
        String real = "2000-01-31";
        attributeWrapperBean.setDate(real);
        assertEquals(BLANK_VALUE, attributeWrapperBean.getValue());
    }

    public void testSetNullOrEmptySetDateTime() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test1", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATETIMESTAMP));
        attributeWrapperBean.setDate(BLANK_VALUE);
        attributeWrapperBean.setHour(BLANK_VALUE);
        attributeWrapperBean.setMinute(BLANK_VALUE);
        String dateTime = attributeWrapperBean.getDateTime();
        assertEquals(BLANK_VALUE, dateTime);
    }

    public void testClearValue() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test1", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATETIMESTAMP));

        String date = "2000-01-31";
        String timeHrs = "01";
        String timeMins = "10";
        attributeWrapperBean.setDate(date);
        attributeWrapperBean.setHour(timeHrs);
        attributeWrapperBean.setMinute(timeMins);
        attributeWrapperBean.setValue("foobar");

        attributeWrapperBean.clearValue();
        assertEquals(BLANK_VALUE, attributeWrapperBean.getValue());
        assertEquals(BLANK_VALUE, attributeWrapperBean.getDate());
        assertEquals(BLANK_VALUE, attributeWrapperBean.getMinute());
        assertEquals(BLANK_VALUE, attributeWrapperBean.getHour());
        assertEquals(BLANK_VALUE, attributeWrapperBean.getDateTime());
        assertEquals(BLANK_VALUE, attributeWrapperBean.getDisplayValue());
        assertEquals(BLANK_VALUE, attributeWrapperBean.getDateTimeDateDisplayValue());
    }

    public void testGetDateTimeDateDisplayValue() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test1", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATETIMESTAMP));

        String date = "2000-01-31";
        String timeHrs = "01";
        String timeMins = "10";
        attributeWrapperBean.setDate(date);
        attributeWrapperBean.setHour(timeHrs);
        attributeWrapperBean.setMinute(timeMins);

        assertEquals("31 Jan 2000 01:10", attributeWrapperBean.getDisplayValue());
        assertEquals("31 Jan 2000", attributeWrapperBean.getDateTimeDateDisplayValue());
    }

    private static final String BLANK_VALUE = "";
}