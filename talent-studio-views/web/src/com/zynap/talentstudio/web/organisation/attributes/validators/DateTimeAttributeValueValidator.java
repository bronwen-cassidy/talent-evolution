/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.util.IFormatter;
import com.zynap.talentstudio.util.FormatterFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DateTimeAttributeValueValidator extends DateAttributeValueValidator {

    protected boolean checkValueType(AttributeWrapperBean wrapper) {
        return TypeValidator.isDateTime(wrapper.getValue());
    }

	protected String getErrorValue(String value) {
		return FormatterFactory.getDateFormatter().formatDateTimeAsString(value);
	}

    protected Comparable getRangeValue(String value) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_DATE_TIME_PATTERN);
        formatter.setLenient(false);
        return formatter.parse(value);
    }    
}
