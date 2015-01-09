package com.zynap.talentstudio.organisation.attributes.validators.definition;

import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;

import java.text.ParseException;

/**
* Class or Interface description.
*
* @author jsuiras
* @since 29-Jun-2005 16:08:49
* @version 0.1
*/
public class IntegerRangeValidator extends AbstractRangeValidator {

    protected boolean checkType(String value) {
        return TypeValidator.isInteger(value);
    }

    protected String getTypeErrorKey() {
        return ERROR_CODE;
    }

    public Comparable getValue(String stringValue) throws ParseException {
        return (new Integer(stringValue));
    }

    private static final String ERROR_CODE = "not.a.number";
}
