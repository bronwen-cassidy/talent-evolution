/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import java.text.ParseException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TextAttributeValueValidator extends AbstractAttributeValueValidator {

    protected String getLessThanErrorKey() {
        return "text.is.less.than.minlength";
    }

    protected String getGreaterThanErrorKey() {
        return "text.is.greater.than.maxlength";
    }

    protected Comparable getRangeValue(String value) throws ParseException {
        return (new Integer(value));
    }

    protected boolean checkValueType(AttributeWrapperBean wrapper) {
        return true;
    }

    protected Comparable getAttributeValue(String value) throws ParseException {
        return new Integer(value.length());
    }
}
