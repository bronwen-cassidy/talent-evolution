package com.zynap.talentstudio.web.utils.displaytag;

import java.text.SimpleDateFormat;

/**
 * Decorator used to convert dates to correct display format.
 *
 * User: amark
 * Date: 29-Mar-2005
 * Time: 14:17:48
 */
public class DateDecorator extends DateColumnDecorator {

	public SimpleDateFormat getFormat() {
		return DATE_FORMAT;
	}

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
}