package com.zynap.talentstudio.organisation.attributes.validators.definition;

import com.zynap.talentstudio.organisation.attributes.validators.TypeValidator;
import com.zynap.talentstudio.util.IFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**               
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 30-Jun-2005
 * Time: 08:19:01
 * To change this template use File | Settings | File Templates.
 */
public class TimeRangeValidator extends AbstractRangeValidator {

    protected boolean checkType(String value) {
        return TypeValidator.isTime(value);
    }

    protected String getTypeErrorKey() {
        return INVALID_TIME_TYPE;
    }

    protected Comparable getValue(String stringValue) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_TIME_PATTERN);
        formatter.setLenient(false);
        return formatter.parse(stringValue);
    }
}
