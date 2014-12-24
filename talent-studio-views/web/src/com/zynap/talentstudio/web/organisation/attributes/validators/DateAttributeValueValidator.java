/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.util.IFormatter;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DateAttributeValueValidator extends AbstractAttributeValueValidator {

    protected String getLessThanErrorKey() {
        return "value.less.than.earliest";
    }

    protected String getGreaterThanErrorKey() {
        return "value.after.latest";
    }

    protected String getTypeErrorKey() {
        return "not.a.date";
    }

    protected boolean checkValueType(AttributeWrapperBean wrapper) {
        return TypeValidator.isDate(wrapper.getValue());
    }

	protected String getErrorValue(String value) {
		return FormatterFactory.getDateFormatter().formatDateAsString(value);
	}

    protected Comparable getRangeValue(String value) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_DATE_PATTERN);
        formatter.setLenient(false);
        return formatter.parse(value);
    }    
}
