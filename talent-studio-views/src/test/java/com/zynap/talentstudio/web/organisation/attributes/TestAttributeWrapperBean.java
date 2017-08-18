package com.zynap.talentstudio.web.organisation.attributes;

/**
 * User: amark
 * Date: 09-Feb-2005
 * Time: 15:43:33
 */

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.listeners.CollectCreatedMocks;

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.util.IFormatter;
import com.zynap.talentstudio.web.organisation.attributes.validators.DateTimeAttributeValueValidator;
import com.zynap.talentstudio.web.organisation.attributes.validators.ErrorMessageHandler;
import com.zynap.talentstudio.web.organisation.attributes.validators.TimeAttributeValueValidator;

import org.springframework.beans.factory.BeanFactory;

import java.text.Normalizer;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAttributeWrapperBean {
	
	@Test
    public void testSetDate() throws Exception {
        
        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test1", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATE));
        String real = "2005-10-10";
        attributeWrapperBean.setDate(real);
        assertEquals(real, attributeWrapperBean.getValue());
    }

	@Test
    public void testSetNullOrEmptyDate() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test2", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATE));
        attributeWrapperBean.setDate(BLANK_VALUE);
        String date = attributeWrapperBean.getDate();
        assertEquals(BLANK_VALUE, date);
        String value = attributeWrapperBean.getValue();
        assertEquals(BLANK_VALUE, value);
    }

	@Test
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

	@Test
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

	@Test
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

	@Test
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

	@Test
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
    
    @Test
    public void testGetCurrencyValue() throws Exception {
    	
        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("currency", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_CURRENCY)); 
        attributeWrapperBean.setValue("12000.00");
        attributeWrapperBean.setCurrency("USD");
        final String value = attributeWrapperBean.getValue();
        assertEquals("12000.00", value);
    }

	@Test
	public void testGetCurrencyDisplayValue() throws Exception {

		AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("currency", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_CURRENCY));
		attributeWrapperBean.setValue("12000");
		attributeWrapperBean.setCurrency("USD");
		final String displayValue = attributeWrapperBean.getDisplayValue();
		assertEquals("USD12,000.00", displayValue);
	}

	@Test
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

	@Test
    public void testSetDateForATimeAttr() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test3", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_TIMESTAMP));
        String real = "2000-01-31";
        attributeWrapperBean.setDate(real);
        assertEquals(BLANK_VALUE, attributeWrapperBean.getValue());
    }

	@Test
    public void testSetNullOrEmptySetDateTime() throws Exception {

        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("test1", null, new DynamicAttribute("1111", DynamicAttribute.DA_TYPE_DATETIMESTAMP));
        attributeWrapperBean.setDate(BLANK_VALUE);
        attributeWrapperBean.setHour(BLANK_VALUE);
        attributeWrapperBean.setMinute(BLANK_VALUE);
        String dateTime = attributeWrapperBean.getDateTime();
        assertEquals(BLANK_VALUE, dateTime);
    }

	@Test
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

	@Test
    public void testGetDateTimeDateDisplayValue() throws Exception {

		final BeanFactory beanFactory = mock(BeanFactory.class);
		final FormatterFactory factory = new FormatterFactory("EN", "dd MMM yyyy");
		when(beanFactory.getBean("formatterFactory")).thenReturn(factory);
		factory.setBeanFactory(beanFactory);

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