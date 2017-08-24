package com.zynap.talentstudio.util;

import com.zynap.talentstudio.security.UserSessionFactory;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 */
public class DecimalFormatter {

	public static String formatDecimal(String value, int decimalPlaces) {
		Locale locale = UserSessionFactory.getUserSession().getLocale();
		if (StringUtils.hasText(value)) {
			
			final NumberFormat formatter = NumberFormat.getInstance(locale);
			formatter.setMaximumFractionDigits(decimalPlaces);
			formatter.setMinimumFractionDigits(decimalPlaces);
			
			return formatter.format(new BigDecimal(value));
		}
		return value;
	}
}
