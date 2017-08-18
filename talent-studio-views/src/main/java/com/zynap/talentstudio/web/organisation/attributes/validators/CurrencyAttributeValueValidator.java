/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class CurrencyAttributeValueValidator extends DoubleAttributeValueValidator {

    protected Comparable getRangeValue(String value) throws ParseException {
        return (new Double(value));
    }

    protected boolean checkValueType(AttributeWrapperBean wrapper) {

        boolean decimalMatches = super.checkValueType(wrapper);
        String currency = wrapper.getCurrency();
        if(StringUtils.isBlank(currency)) {
        	errorMsg = "error.currency.required";
        	decimalMatches = false;
        }
        return decimalMatches;
    }
}
