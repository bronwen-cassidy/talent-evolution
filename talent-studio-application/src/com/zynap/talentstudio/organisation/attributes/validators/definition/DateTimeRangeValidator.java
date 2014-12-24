package com.zynap.talentstudio.organisation.attributes.validators.definition;

import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;
import com.zynap.talentstudio.util.IFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
* Class or Interface description.
*
* @author jsuiras
* @since 29-Jun-2005 16:08:49
* @version 0.1
*/
public class DateTimeRangeValidator extends AbstractRangeValidator {

    protected boolean checkType(String value) {
        return TypeValidator.isDateTime(value);
    }

    protected String getTypeErrorKey() {
        return NO_DATE_TIME_VALUE_ERROR;
    }

    protected Comparable getValue(String stringValue) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_DATE_TIME_PATTERN);
        formatter.setLenient(false);
        return formatter.parse(stringValue);
    }

    private static final String NO_DATE_TIME_VALUE_ERROR = "error.no.values.for.time";    
}
