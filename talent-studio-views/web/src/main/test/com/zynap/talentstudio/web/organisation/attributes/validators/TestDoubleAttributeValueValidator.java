package com.zynap.talentstudio.web.organisation.attributes.validators;

import junit.framework.TestCase;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

/**
 * Created by bronwen.
 * Date: 19/04/12
 * Time: 17:56
 */
public class TestDoubleAttributeValueValidator extends TestCase {

    public void setUp() throws Exception {
       doubleAttribute = new DynamicAttribute("tt", DynamicAttribute.DA_TYPE_DOUBLE);
    }

    public void testCheckValueType() throws Exception {
        DoubleAttributeValueValidator validator = new DoubleAttributeValueValidator();
        doubleAttribute.setDecimalPlaces(2);
        AttributeWrapperBean bean = new AttributeWrapperBean(doubleAttribute);
        bean.setValue("1234.22");
        boolean val = validator.checkValueType(bean);
        assertTrue(val);
    }
    public void testCheckValueTypeFail() throws Exception {
        DoubleAttributeValueValidator validator = new DoubleAttributeValueValidator();
        doubleAttribute.setDecimalPlaces(2);
        AttributeWrapperBean bean = new AttributeWrapperBean(doubleAttribute);
        bean.setValue("1234.222");
        boolean val = validator.checkValueType(bean);
        assertFalse(val);
    }
    public void testCheckValueTypeZero() throws Exception {
        DoubleAttributeValueValidator validator = new DoubleAttributeValueValidator();
        doubleAttribute.setDecimalPlaces(2);
        AttributeWrapperBean bean = new AttributeWrapperBean(doubleAttribute);
        bean.setValue("1234");
        boolean val = validator.checkValueType(bean);
        assertTrue(val);
    }

    private DynamicAttribute doubleAttribute;
}
