/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import de.laures.cewolf.DatasetProducer;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;

import java.util.Date;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Feb-2006 15:53:40
 */
public abstract class AbstractChartProducer implements DatasetProducer, IChartProducer {

    public final boolean hasExpired(Map params, Date since) {
        return true;
    }

    public final String getProducerId() {
        return getClass().getName();
    }

    /**
     * Has values if has at least 1 non-zero value.
     *
     * @return hasValues
     */
    public boolean isHasValues() {
        return hasValues;
    }

    public void setHasValues(boolean hasValues) {
        this.hasValues = hasValues;
    }

    public abstract String[] getColumnLabelItems();

    public abstract boolean isBarChart();
    
    public abstract boolean isOverlaidChart();

    public abstract int getPreferredWidth();

    public abstract int getPreferredHeight();

    public int getLegendHeight() {
        final int numberOfRows = ((getNumberOfColumns() * CELL_PADDING + getTotalNumberOfCharacters()) / MAX_CHARACTERS_PER_LINE);
        return (numberOfRows < 1 ? ROW_HEIGHT : numberOfRows * ROW_HEIGHT);
    }

    abstract int getTotalNumberOfCharacters();

    abstract int getNumberOfColumns();

    public static final Double convertValue(String value) {

        Double result = null;

        try {
            if (value != null && Report.hasValue(value)) {
                result = new Double(value);
            }
        } catch (NumberFormatException ignored) {
        }

        return result;
    }

    /**
     * Check not equals to zero.
     *
     * @param value
     * @return true or false
     */
    public boolean isNonZeroValue(Double value) {
        return !DEFAULT_VALUE.equals(value);
    }

    /**
     * Check not equals to {@link ReportConstants#TOTAL} or {@link ReportConstants#NA} .
     *
     * @param value
     * @return true or false
     */
    public final boolean isNotTotalOrNA(final String value) {
        return isNotTotal(value) && isNotNA(value);
    }

    /**
     * Check not equals to {@link ReportConstants#NA}.
     *
     * @param value
     * @return true or false
     */
    public final boolean isNotNA(final String value) {
        return !ReportConstants.NA.equals(value);
    }

    /**
     * Check not equals to {@link ReportConstants#TOTAL}.
     *
     * @param value
     * @return true or false
     */
    public final boolean isNotTotal(final String value) {
        return !ReportConstants.TOTAL.equals(value);
    }

    public static final Double DEFAULT_VALUE = new Double(0.0);

    private static final int MAX_CHARACTERS_PER_LINE = 130;
    private static final int ROW_HEIGHT = 20;
    private static final int CELL_PADDING = 5;

    /**
     * Indicates if has at least one value that is not zero.
     */
    private boolean hasValues = false;
}
