package com.zynap.talentstudio.util;

import org.junit.Test;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.security.UserSessionFactory;

import java.util.Locale;

import static org.junit.Assert.*;

/**
 *
 */
public class DecimalFormatterTest {

	@Test
	public void formatDecimalZeroPlaces() throws Exception {
		setUp(Locale.US);
		final String actual = DecimalFormatter.formatDecimal("220000000", 0);
		assertEquals("220,000,000", actual);
	}

	@Test
	public void formatDecimalTwoPlaces() throws Exception {
		setUp(Locale.US);
		final String actual = DecimalFormatter.formatDecimal("220000000.986", 2);
		assertEquals("220,000,000.99", actual);
	}

	private void setUp(Locale locale) {
		UserSession userSession = new UserSession(null);
		userSession.setLocale(locale);
		UserSessionFactory.setUserSession(userSession);
	}

}