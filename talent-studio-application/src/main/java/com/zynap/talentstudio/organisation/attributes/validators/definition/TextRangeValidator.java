package com.zynap.talentstudio.organisation.attributes.validators.definition;

import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;

/**
* Class or Interface description.
*
* @author jsuiras
* @since 29-Jun-2005 16:08:49
* @version 0.1
*/
public class TextRangeValidator extends IntegerRangeValidator {
    /**
     * checks the type of the dynamicAttributes min/max sizes
     * @param value
     * @return true if we have an integer and the size is greater than zero, false otherwise.
     */
    protected boolean checkType(String value) {
        return TypeValidator.isInteger(value) && new Integer(value).intValue() > 0;
    }    
}
