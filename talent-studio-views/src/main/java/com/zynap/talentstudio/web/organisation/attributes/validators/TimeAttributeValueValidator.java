/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.util.IFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TimeAttributeValueValidator extends AbstractAttributeValueValidator {

    protected String getLessThanErrorKey() {
        return "value.less.than.earliest";
    }

    protected String getGreaterThanErrorKey() {
        return "value.after.latest";
    }

    protected String getTypeErrorKey() {
        return INVALID_TIME_TYPE;
    }

    protected boolean checkValueType(AttributeWrapperBean wrapper) {
        return TypeValidator.isTime(wrapper.getValue());
    }

    protected Comparable getRangeValue(String value) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_TIME_PATTERN);
        formatter.setLenient(false);
        return formatter.parse(value);
    }    
}
