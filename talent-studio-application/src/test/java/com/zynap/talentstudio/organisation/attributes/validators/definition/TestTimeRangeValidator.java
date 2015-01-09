/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes.validators.definition;

import junit.framework.TestCase;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.InvalidDynamicAttributeException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestTimeRangeValidator extends TestCase {

	protected void setUp() throws Exception {
		timeRangeValidator = new TimeRangeValidator();
	}

	public void testValidateNullTimes() throws Exception {
		DynamicAttribute timeAttribute = createTimeAttribute(null, null);
		try {
			timeRangeValidator.validate(timeAttribute);
		} catch (InvalidDynamicAttributeException e) {
			fail("No error expected but got: " + e.getMessage());
		}
	}

	public void testValidateValueEmptyTimes() throws Exception {
		DynamicAttribute timeAttribute = createTimeAttribute("", "");
		try {
			timeRangeValidator.validate(timeAttribute);
		} catch (InvalidDynamicAttributeException e) {
			fail("No error expected but got: " + e.getMessage());
		}
	}

	public void testValidateValueSameValues() throws Exception {
		DynamicAttribute timeAttribute = createTimeAttribute("10:00", "10:00");
		try {
			timeRangeValidator.validate(timeAttribute);
		} catch (InvalidDynamicAttributeException e) {
			fail("No error expected but got: " + e.getMessage());
		}
	}

	public void testValidateValueMinOnly() throws Exception {
		DynamicAttribute timeAttribute = createTimeAttribute("12:00", null);
		try {
			timeRangeValidator.validate(timeAttribute);
		} catch (InvalidDynamicAttributeException e) {
			fail("No error expected but got: " + e.getMessage());
		}
	}

	public void testValidateValueMinOnlyMaxEmpty() throws Exception {
		DynamicAttribute timeAttribute = createTimeAttribute("12:00", "");
		try {
			timeRangeValidator.validate(timeAttribute);
		} catch (InvalidDynamicAttributeException e) {
			fail("No error expected but got: " + e.getMessage());
		}
	}

	public void testValidateValueMaxOnly() throws Exception {
		DynamicAttribute timeAttribute = createTimeAttribute(null, "12:00");
		try {
			timeRangeValidator.validate(timeAttribute);
		} catch (InvalidDynamicAttributeException e) {
			fail("No error expected but got: " + e.getMessage());
		}
	}

	public void testValidateValueMaxOnlyMinEmpty() throws Exception {
		DynamicAttribute timeAttribute = createTimeAttribute("", "12:00");
		try {
			timeRangeValidator.validate(timeAttribute);
		} catch (InvalidDynamicAttributeException e) {
			fail("No error expected but got: " + e.getMessage());
		}
	}

	private DynamicAttribute createTimeAttribute(String minValue, String maxValue) {
		DynamicAttribute attribute = new DynamicAttribute();
		attribute.setType(DynamicAttribute.DA_TYPE_TIMESTAMP);
		attribute.setLabel("Hello World");
		attribute.setDescription("Test Description");
		attribute.setMinSize(minValue);
		attribute.setMaxSize(maxValue);
		return attribute;
	}

	private TimeRangeValidator timeRangeValidator;
}