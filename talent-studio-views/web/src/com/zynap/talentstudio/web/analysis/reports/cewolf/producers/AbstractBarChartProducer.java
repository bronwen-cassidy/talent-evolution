package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import de.laures.cewolf.links.CategoryItemLinkGenerator;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.text.MessageFormat;
import java.util.Map;

/**
 * User: amark
 * Date: 22-Mar-2006
 * Time: 13:32:08
 */
public abstract class AbstractBarChartProducer extends AbstractChartProducer implements CategoryToolTipGenerator, CategoryItemLinkGenerator {

    protected AbstractBarChartProducer(String chartOrientation) {
        this.chartOrientation = chartOrientation;
    }

    public final boolean isBarChart() {
        return true;
    }

    public boolean isOverlaidChart() {
        return false;
    }

    public final String generateLink(Object dataset, int series, Object category) {
        return null;
    }

    public final Object produceDataset(Map params) {
        return dataSet;
    }

    public final int getPreferredWidth() {

        // if bars are vertical width must expand
        boolean isHorizontal = chartOrientation.startsWith(HORIZONTAL_ALIGNMENT);
        if (!isHorizontal) {
            int size = calculateSize();
            return (size < defaultWidth) ? defaultWidth : size;
        } else {
            return defaultWidth;
        }
    }

    public final int getPreferredHeight() {

        // if bars are horizontal height must expand
        boolean isHorizontal = chartOrientation.startsWith(HORIZONTAL_ALIGNMENT);
        if (isHorizontal) {
            int size = calculateSize();
            return (size < defaultHeight) ? defaultHeight : size;
        } else {
            return defaultHeight;
        }
    }

    public final String generateToolTip(CategoryDataset data, int series, int item) {
        String rowKey = (String) data.getRowKey(series);
        String columnKey = (String) data.getColumnKey(item);
        Number value = data.getValue(rowKey, columnKey);

        Object[] args = new Object[]{rowKey, columnKey, value};
        return MessageFormat.format(TOOLTIP_FORMAT, args);
    }

    public final String[] getColumnLabelItems() {
        return new String[]{chartLabel};
    }

    public String getYAxisLabel() {
        return yAxisLabel;
    }

    public String getXAxisLabel() {
        return xAxisLabel;
    }

    public String getChartOrientation() {
        return chartOrientation;
    }

    abstract int calculateSize();

    void addValueToDataSet(final boolean useHorizontal, final String rowLabel, final String columnLabel, final String value) {

        // bar charts exclude totals and NA values
        if (isNotTotalOrNA(rowLabel) && isNotTotalOrNA(columnLabel)) {

            // use default value
            Double convertedValue = convertValue(value);
            if (convertedValue == null) {
                convertedValue = DEFAULT_VALUE;
            }

            if (useHorizontal) {
                dataSet.addValue(convertedValue, rowLabel, columnLabel);
            } else {
                dataSet.addValue(convertedValue, columnLabel, rowLabel);
            }

            // determine if we have at least one non-zero value
            if (isNonZeroValue(convertedValue)) {
                setHasValues(true);
            }
        }
    }

    public void setDefaultWidth(int defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    public void setDefaultHeight(int defaultHeight) {
        this.defaultHeight = defaultHeight;
    }

    protected final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
    protected final String chartOrientation;
    protected String xAxisLabel;
    protected String yAxisLabel;

    protected final String chartLabel = "";

    // todo pass in as parameters to constructor???
    protected int defaultWidth = 600;
    protected int defaultHeight = 600;

    protected static final String HORIZONTAL_ALIGNMENT = "horizontal";
    protected static final String TOOLTIP_FORMAT = "{0} : {1} = {2}";
}
