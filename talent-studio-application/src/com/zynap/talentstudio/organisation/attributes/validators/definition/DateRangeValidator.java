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
public class DateRangeValidator extends AbstractRangeValidator {

    protected boolean checkType(String value) {
        return TypeValidator.isDate(value);
    }

    protected String getTypeErrorKey() {
        return "not.a.date";
    }

    protected Comparable getValue(String stringValue) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_DATE_PATTERN);
        formatter.setLenient(false);
        return formatter.parse(stringValue);
    }
}
