package com.zynap.talentstudio.util;

import java.util.Date;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 12-Apr-2005
 * Time: 15:56:41
 */
public interface IFormatter {

    String formatDateAsString(String date);

    String formatDateTimeAsString(String date);

    String formatDateTimeAsString(Date date);

    String getDisplayDateTime(Date date);
    String getDisplayDateTime(Locale locale,Date date);

    String getStoredDateTimeFormat(Date date);

    String formatDateAsString(Date date);

    Date getDateValue(String date);

    String getStoredDateFormat(Date date);


    final String DEFAULT_LOCALE = "EN";
    final String STORED_DATE_PATTERN = "yyyy-MM-dd";
    final String DISPLAY_DATE_PATTERN = "dd MMM yyyy";

    final String TIME_DELIMITER = ":";
    final String DATE_TIME_DELIMITER = " ";

    final String STORED_TIME_PATTERN = "HH:mm";
    final String STORED_DATE_TIME_PATTERN = STORED_DATE_PATTERN + " " + STORED_TIME_PATTERN;
    final String DISPLAY_DATE_TIME_PATTERN = DISPLAY_DATE_PATTERN + DATE_TIME_DELIMITER + STORED_TIME_PATTERN;
}
