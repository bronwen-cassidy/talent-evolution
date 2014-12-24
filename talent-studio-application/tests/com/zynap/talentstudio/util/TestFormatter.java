package com.zynap.talentstudio.util;

/**
 * User: amark
 * Date: 03-Aug-2006
 * Time: 17:13:34
 */

import junit.framework.TestCase;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.security.UserSessionFactory;

import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Locale;

public class TestFormatter extends TestCase {

    public void testFormatDateAsString() throws Exception {

        final String dateAsStr = "2007-12-31";
        final Date date = formatter.getDateValue(dateAsStr);

        final String output = formatter.formatDateAsString(date);
        assertEquals("31 Dec 2007", output);

        final String formattedDate = formatter.formatDateAsString(dateAsStr);
        assertEquals(output, formattedDate);
    }

    public void testFormatDateTimeAsString() throws Exception {
        UserSession userSession = new UserSession(null);
        userSession.setLocale(new Locale("EN"));
        UserSessionFactory.setUserSession(userSession);

        final String dateAsStr = "2007-12-31";
        final Date date = formatter.getDateValue(dateAsStr);
        String result = formatter.getDisplayDateTime(date);
        assertEquals("Dec 31, 2007 12:00 AM", result);
    }

    public void testGetStoredDateTimeFormat() throws Exception {
        UserSession userSession = new UserSession(null);
        userSession.setLocale(new Locale("EN"));
        UserSessionFactory.setUserSession(userSession);

        String result = formatter.getStoredDateTimeFormat(new Date());
        String[] splitDate = StringUtils.tokenizeToStringArray(result, " ");
        assertEquals(2, splitDate.length);
        assertTrue(result.indexOf(":") != -1);
    }

    Formatter formatter = new Formatter(new Locale(IFormatter.DEFAULT_LOCALE), IFormatter.DISPLAY_DATE_PATTERN, IFormatter.STORED_DATE_PATTERN);
}