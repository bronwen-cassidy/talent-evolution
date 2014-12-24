/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;

import java.text.ParseException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class IntegerAttributeValueValidator extends AbstractAttributeValueValidator {

    protected String getLessThanErrorKey() {
        return "number.is.less.than.minsize";
    }

    protected String getGreaterThanErrorKey() {
        return "number.is.greater.than.maxsize";
    }

    protected Comparable getRangeValue(String value) throws ParseException {
        return (new Integer(value));
    }

    protected boolean checkValueType(AttributeWrapperBean wrapper) {
        return TypeValidator.isInteger(wrapper.getValue());
    }

    protected String getTypeErrorKey() {
        return "not.a.number";
    }
}
