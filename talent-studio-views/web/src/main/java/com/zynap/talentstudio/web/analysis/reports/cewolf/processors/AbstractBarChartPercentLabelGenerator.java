package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;

import java.text.MessageFormat;
import java.text.NumberFormat;

/**
 * User: amark
 * Date: 24-Mar-2006
 * Time: 17:38:07
 */
public abstract class AbstractBarChartPercentLabelGenerator extends StandardCategoryItemLabelGenerator {

    public AbstractBarChartPercentLabelGenerator(int decimalPlaces) {
        super(LABEL_FORMAT, FormatUtils.applyPattern(NumberFormat.getNumberInstance(), decimalPlaces));
        this.percentageFormatter = FormatUtils.applyPercentPattern(NumberFormat.getPercentInstance(), decimalPlaces);
    }

    public final String generateLabel(CategoryDataset dataset, int row, int column) {

        Object[] args = new Object[1];
        Number value = dataset.getValue(row, column);
        if (value != null && value.doubleValue() > 0.0) {

            args[0] = getNumberFormat().format(value);

            //Number total = getTotal(dataset, row, column);
            //double percent = value.doubleValue() / total.doubleValue();
            //args[1] = this.percentageFormatter.format(percent);

            return MessageFormat.format(LABEL_FORMAT, args);
        } else {
            return "";
        }
    }

    public Object clone() throws CloneNotSupportedException {
        final AbstractBarChartPercentLabelGenerator abstractBarChartPercentLabelGenerator = (AbstractBarChartPercentLabelGenerator) super.clone();
        abstractBarChartPercentLabelGenerator.percentageFormatter = (NumberFormat) (percentageFormatter != null ? percentageFormatter.clone() : null);
        return abstractBarChartPercentLabelGenerator;
    }

    abstract Number getTotal(CategoryDataset dataset, int row, int column);

    protected NumberFormat percentageFormatter;

    public static final String LABEL_FORMAT = "{0}";
    //public static final String LABEL_FORMAT = "{0} ({1})";
}
