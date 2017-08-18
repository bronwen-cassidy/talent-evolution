package com.zynap.talentstudio.util;

import org.springframework.util.StringUtils;

import java.text.NumberFormat;
import java.util.Currency;

/**
 *
 */
public class CurrencyFormatter {

	public static String formatCurrency(String value, String currency) {
		if (StringUtils.hasText(value)) {
			NumberFormat format = NumberFormat.getCurrencyInstance();
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
