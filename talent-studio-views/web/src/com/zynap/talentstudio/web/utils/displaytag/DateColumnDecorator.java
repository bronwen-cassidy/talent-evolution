package com.zynap.talentstudio.web.utils.displaytag;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import com.zynap.talentstudio.util.FormatterFactory;

import javax.servlet.jsp.PageContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Decorator used to convert dates to correct display format.
 *
 * User: amark
 * Date: 29-Mar-2005
 * Time: 14:17:48
 */
public class DateColumnDecorator implements DisplaytagColumnDecorator {

    /**
     * Convert date to display format.
     * @param o
     * @return The formatted date, or an empty string if there is a problem.
     * @throws DecoratorException
     */
    public String decorate(Object o) throws DecoratorException {
        try {
            return FormatterFactory.getDateFormatter().formatDateTimeAsString((Date) o);
        } catch (Exception e) {            
            return "";
        }
    }


    public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
        return decorate(columnValue);
    }

    public SimpleDateFormat getFormat() {
		return DATE_FORMAT;
	}

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
}