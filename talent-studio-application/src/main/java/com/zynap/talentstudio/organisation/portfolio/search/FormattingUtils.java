package com.zynap.talentstudio.organisation.portfolio.search;

import com.zynap.util.ArrayUtils;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for formatting strings for Autonomy commands.
 * User: amark
 * Date: 12-Jul-2006
 * Time: 12:05:04
 */
public final class FormattingUtils {

    public static String formatDataSources(String[] datasources) {
        return ArrayUtils.arrayToString(datasources, DATASOURCE_DELIMITER);
    }

    public static String[] splitDataSources(String datasources) {
        return StringUtils.split(datasources, DATASOURCE_DELIMITER);
    }

    private static final String DATASOURCE_DELIMITER = "+";
}
