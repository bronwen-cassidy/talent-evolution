/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import junit.framework.TestCase;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.InvalidDynamicAttributeException;
import com.zynap.talentstudio.organisation.attributes.validators.definition.DateTimeRangeValidator;
import com.zynap.talentstudio.organisation.attributes.validators.definition.TimeRangeValidator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestDynamicAttributeWrapper extends TestCase {

    public void testSetMinTime() throws Exception {
        DynamicAttribute attribute = new DynamicAttribute("test label", DynamicAttribute.DA_TYPE_TIMESTAMP);
        wrapper = new DynamicAttributeWrapper(attribute);
        wrapper.setMinHour("");
        wrapper.setMinMinute("");
        TimeRangeValidator validator = new TimeRangeValidator();
        try {
            validator.validate(wrapper.getModifiedAttributeDefinition());
        } catch (InvalidDynamicAttributeException e) {
            fail("validation should have succeeded");
        }
    }

    public void testSetMaxTime() throws Exception {
        DynamicAttribute attribute = new DynamicAttribute("test label", DynamicAttribute.DA_TYPE_TIMESTAMP);
        wrapper = new DynamicAttributeWrapper(attribute);
        wrapper.setMaxHour("");
        wrapper.setMaxMinute("");
        TimeRangeValidator validator = new TimeRangeValidator();
        try {
            validator.validate(wrapper.getModifiedAttributeDefinition());
        } catch (InvalidDynamicAttributeException e) {
            fail("validation should have succeeded");
        }
    }

    public void testSetMinMaxTime() throws Exception {
        DynamicAttribute attribute = new DynamicAttribute("test label", DynamicAttribute.DA_TYPE_TIMESTAMP);
        wrapper = new DynamicAttributeWrapper(attribute);
        wrapper.setMinHour("");
        wrapper.setMinMinute("");
        wrapper.setMaxHour("");
        wrapper.setMaxMinute("");
        TimeRangeValidator validator = new TimeRangeValidator();
        try {
            validator.validate(wrapper.getModifiedAttributeDefinition());
        } catch (InvalidDynamicAttributeException e) {
            fail("validation should have succeeded");
        }
    }

    public void testSetMinDateTimeEmpty() throws Exception {
        DynamicAttribute attribute = new DynamicAttribute("test label", DynamicAttribute.DA_TYPE_DATETIMESTAMP);
        wrapper = new DynamicAttributeWrapper(attribute);
        wrapper.setMinHour("");
        wrapper.setMinMinute("");
        wrapper.setMinDate("");
        DateTimeRangeValidator validator = new DateTimeRangeValidator();
        try {
            validator.validate(wrapper.getModifiedAttributeDefinition());
        } catch (InvalidDynamicAttributeException e) {
            fail("validation should have succeeded");
        }
    }

    public void testSetMinDateTimeTimeEmpty() throws Exception {
        DynamicAttribute attribute = new DynamicAttribute("test label", DynamicAttribute.DA_TYPE_DATETIMESTAMP);
        wrapper = new DynamicAttributeWrapper(attribute);
        wrapper.setMinHour("");
        wrapper.setMinMinute("");
        wrapper.setMinDate("12-03-2004");
        DateTimeRangeValidator validator = new DateTimeRangeValidator();
        try {
            validator.validate(wrapper.getModifiedAttributeDefinition());
            fail("validation should not have succeeded");
        } catch (InvalidDynamicAttributeException e) {
            e.printStackTrace();
            // ok expected
        }
    }

    public void testSetMaxDateTimeEmpty() throws Exception {
        DynamicAttribute attribute = new DynamicAttribute("test label", DynamicAttribute.DA_TYPE_DATETIMESTAMP);
        wrapper = new DynamicAttributeWrapper(attribute);
        wrapper.setMaxHour("");
        wrapper.setMaxMinute("");
        wrapper.setMaxDate("");
        DateTimeRangeValidator validator = new DateTimeRangeValidator();
        try {
            validator.validate(wrapper.getModifiedAttributeDefinition());
        } catch (InvalidDynamicAttributeException e) {
            fail("validation should have succeeded");
        }
    }

    private DynamicAttributeWrapper wrapper;
}