package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import de.laures.cewolf.links.PieSectionLinkGenerator;
import de.laures.cewolf.tooltips.PieToolTipGenerator;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.zynap.talentstudio.util.BadArithmeticException;
import com.zynap.talentstudio.util.MathsUtils;

import java.text.MessageFormat;
import java.util.Map;


/**
 * User: amark
 * Date: 22-Mar-2006
 * Time: 13:33:38
 */
public abstract class AbstractPieChartProducer extends AbstractChartProducer implements PieToolTipGenerator, PieSectionLinkGenerator {

    public final boolean isBarChart() {
        return false;
    }

    public String generateLink(Object dataset, Object category) {
        return null;
    }

    public Object produceDataset(Map params) {
        return dataSet;
    }

    public int getPreferredWidth() {
        return width;
    }

    public final int getPreferredHeight() {
        return height;
    }

    public String generateToolTip(PieDataset data, Comparable key, int pieIndex) {
        Number value = data.getValue(key);
        return MessageFormat.format(TOOLTIP_FORMAT, key.toString(), value);
    }

    final void addValueToDataSet(final String groupingLabel, final String value) {

        // pie charts exclude totals but include NA values
        if (isNotTotal(groupingLabel)) {

            Double convertedValue = convertValue(value);

            // only use non-null values
            if (convertedValue != null) {
                dataSet.setValue(groupingLabel, convertedValue);

                // determine if we have at least one non-zero value
                if (isNonZeroValue(convertedValue)) {
                    setHasValues(true);
                }
            }
        }
    }


    public boolean isNonZeroValue(Double value) {

        try {
            return super.isNonZeroValue(value) && !MathsUtils.lessThan(value, DEFAULT_VALUE);    //To change body of overridden methods use File | Settings | File Templates.
        } catch (BadArithmeticException e) {
            return false;
        }
    }

    final int getNumberOfColumns() {
        return dataSet.getItemCount();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected final DefaultPieDataset dataSet = new DefaultPieDataset();

    private int width = 800;
    private int height = 800;

    protected static final String TOOLTIP_FORMAT = "{0} = {1}";
}
