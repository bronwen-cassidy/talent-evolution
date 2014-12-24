package com.zynap.talentstudio.util;

import com.zynap.talentstudio.security.UserSessionFactory;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 12-Apr-2005
 * Time: 16:04:25
 */
public final class Formatter implements IFormatter {

    /**
     *
     * @param locale
     * @param displayPattern
     * @param originalPattern: currently dd MMM yyyy
     */
    public Formatter(Locale locale, String displayPattern, String originalPattern) {
        this.displayPattern = displayPattern;
        this.locale = locale;
        this.originalPattern = originalPattern;
    }

    public String formatDateAsString(String date) {
        Date value = getDateValue(date);
        return value != null ? new SimpleDateFormat(this.displayPattern, this.locale).format(value) : "";
    }

    public String formatDateTimeAsString(String date) {
        if (!StringUtils.hasText(date)) return date;
        String[] splitDate = StringUtils.tokenizeToStringArray(date, DATE_TIME_DELIMITER);
        String result = formatDateAsString(splitDate[0]);
        if (splitDate.length > 1) result += " " + splitDate[1];
        return result;
    }

    public String formatDateTimeAsString(Date date) {
        if (date == null) return null;
        return new SimpleDateFormat(this.displayPattern + " " + TIME_PATTERN).format(date);
    }

    public String getDisplayDateTime(Date date) {
        final Locale currentLocale = UserSessionFactory.getUserSession().getLocale();
        return DateFormat.getDateTimeInstance(DEFAULT_DATE_STYLE, DEFAULT_TIME_STYLE, currentLocale).format(date);
    }
    public String getDisplayDateTime(Locale locale, Date date) {
        return DateFormat.getDateTimeInstance(DEFAULT_DATE_STYLE, DEFAULT_TIME_STYLE, locale).format(date);
    }

    public String getStoredDateTimeFormat(Date date) {
        return new SimpleDateFormat(this.originalPattern + DATE_TIME_DELIMITER + TIME_PATTERN, this.locale).format(date);
    }

    public String formatDateAsString(Date value) {
        return value != null ? new SimpleDateFormat(this.displayPattern, this.locale).format(value) : "";
    }

    public String getStoredDateFormat(Date date) {
        return new SimpleDateFormat(this.originalPattern, this.locale).format(date);
    }

    public Date getDateValue(String date) {
        Date value = null;
        boolean hasText = StringUtils.hasText(date);
        if (hasText) {
            try {
                value = new SimpleDateFormat(this.originalPattern, this.locale).parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    /* returns a date format of dd MMM yyyy */
    private static final int DEFAULT_DATE_STYLE = 2;

    /* time format of HH:SS (AM)/(PM)*/
    private static final int DEFAULT_TIME_STYLE = 3;

    private static final String TIME_PATTERN = "HH:mm";
    //private static final String HH_MM = " hh:mm";
    protected String displayPattern;
    protected String originalPattern;
    protected Locale locale;
}
