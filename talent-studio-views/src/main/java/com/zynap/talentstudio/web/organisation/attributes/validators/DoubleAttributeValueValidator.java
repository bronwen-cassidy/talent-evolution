/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes.validators;

import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import java.text.ParseException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DoubleAttributeValueValidator extends IntegerAttributeValueValidator {

    protected Comparable getRangeValue(String value) throws ParseException {
        return (new Double(value));
    }

    protected boolean checkValueType(AttributeWrapperBean wrapper) {

        DynamicAttribute definition = wrapper.getAttributeDefinition();
        Integer val = definition.getDecimalPlaces();
        //if(val == null) val = new Integer(0);
        String value = wrapper.getValue();
        boolean b = TypeValidator.isDouble(value);
        if(!b) {
            errorMsg = super.getTypeErrorKey();
            return false;
        }

        boolean decimalMatches = true;
        if(val != null && value.contains(".")) {
            // get the substring after any . 
            String sub = value.substring(value.indexOf(".") + 1, value.length());
            if(sub.length() > val.intValue()) {
                decimalMatches = false;
                errorMsg = "incorrect.decimal.places";
            }
        }
        
        return decimalMatches;
    }

    protected String getTypeErrorKey() {
        return errorMsg;
    }

    String errorMsg = "not.a.number";
}
