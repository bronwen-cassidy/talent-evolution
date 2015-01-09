package com.zynap.talentstudio.util;

/**
 * User: amark
 * Date: 11-Oct-2005
 * Time: 14:00:13
 */

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.security.UserSessionFactory;

import java.util.Locale;

public class TestFormatterFactory extends AbstractHibernateTestCase {

    public void testGetDisplayValue_FrLocale() throws Exception {
        //yyyy-MM-dd
        String expected = "12 mars 1999";
        UserSession us = new UserSession(getAdminUserPrincipal(), getArenaMenuHandler());
        us.setLocale(new Locale("FR"));
        UserSessionFactory.setUserSession(us);
        String actual = FormatterFactory.getDateFormatter().formatDateAsString("1999-03-12");
        assertEquals(expected, actual);
    }

    public void testGetDisplayValue() throws Exception {
        //yyyy-MM-dd
        UserSessionFactory.setUserSession(null);
        String expected = "12 Jul 1999";
        String actual = FormatterFactory.getDateFormatter().formatDateAsString("1999-07-12");
        assertEquals(expected, actual);
    }
}