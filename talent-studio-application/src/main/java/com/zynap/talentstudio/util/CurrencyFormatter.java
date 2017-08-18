package com.zynap.talentstudio.util;

import com.zynap.talentstudio.security.UserSessionFactory;
import org.springframework.util.StringUtils;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 *
 */
public class CurrencyFormatter {

	public static String formatCurrency(String value, String currency) {
		Locale locale = UserSessionFactory.getUserSession().getLocale();
		if (StringUtils.hasText(value)) {
			NumberFormat format = NumberFormat.getCurrencyInstance(locale);
			try {
				Currency c = Currency.getInstance(currency);
				format.setCurrency(c);
				return format.format(Double.valueOf(value));
			} catch (Exception e) {
				return value;
			}
		}
		return value;
	}
}
