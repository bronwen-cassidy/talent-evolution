package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.links.XYItemLinkGenerator;
import de.laures.cewolf.tooltips.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.zynap.talentstudio.web.analysis.reports.cewolf.dataset.NdXYSeriesCollection;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

/**
 * @author taulant
 * @since 11/02/09
 *        <p/>
 *        the anonomous classes (or inner classes) can be set to be independent but for this problem
 *        they are fine to be here
 */
public abstract class AbstractOverlaidChartProducer extends AbstractChartProducer implements XYToolTipGenerator, XYItemLinkGenerator {

    protected AbstractOverlaidChartProducer(String chartOrientation) {
        this.chartOrientation = chartOrientation;
    }

    public final boolean isBarChart() {
        return false;
    }


    public OverlaidBarChartProducer getOverlaidBarChartProducer() {
        return new OverlaidBarChartProducer(dataSetXYSeries);
    }


    public Object produceDataset(Map params) throws DatasetProduceException {


        final XYSeries series = new XYSeries("Normal Distribution");

        final double sum = getSum();
        final int ny = getN();

        /**
         * this is going to be the curve in percentage representation i.e. drawing curve in percents
         * to smoothe the curve try and add small points in between
         *
         * x points goes up to 50% and then back to 0 providing curve level 
         *
         * y points only go up i.e. straight line 
         */
//        double[] samplePointsX = new double[]{0.0, 0.08, 0.15, 0.4, 0.45, 0.48, 0.5, 0.48, 0.45, 0.4, 0.15, 0.08, 0.0};
//        double[] samplePointsY = new double[]{0.0, 0.15, 0.25, 0.35, 0.45, 0.48, 0.5, 0.52, 0.55, 0.65, 0.75, 0.85, 1.0};
        /**
         * approved by tom and jenny(az) 
         */
        double[] samplePointsX = new double[]{0.0, 0.05, 0.20, 0.5, 0.20, 0.05, 0.0};
        double[] samplePointsY = new double[]{0.0, 0.16, 0.35, 0.5, 0.65, 0.84, 1.0};


        if (samplePointsX.length != samplePointsY.length) throw new DatasetProduceException("Sample points length must match");
        /**
         * the generation is going to be the percents taken into account
         * with the n being the (x or y) as a point generated to be ploted
         */
//        double[] generatedSamplesX = new double[samplePointsX.length];
//        for (int i = 0; i < samplePointsX.length; i++) {
//            generatedSamplesX[i] = nx * samplePointsX[i];
//
//        }
        double[] generatedSamplesX = new double[samplePointsX.length];


        for (int i = 0; i < generatedSamplesX.length; i++) {
            double p = samplePointsX[i];

            generatedSamplesX[i] = p * sum;

        }

        //perfect -gen
        double[] generatedSamplesY = new double[samplePointsY.length];
        for (int i = 0; i < samplePointsY.length; i++) {
            generatedSamplesY[i] = ny * samplePointsY[i];

        }

        /**
         * add the plots to series
         */
        for (int i = 0; i <= generatedSamplesX.length - 1; i++) {

            series.add(generatedSamplesY[i], generatedSamplesX[i]);

        }

        return new XYSeriesCollection(series);
    }



    private int getN() {
        return dataSetXYSeries.size();
    }

    private double getSum() {

        double sum = 0;

        for (OverlaidXYSeries dataSetValue : dataSetXYSeries) {
            sum += dataSetValue.getValue();
        }
        return sum;
    }

    public final int getPreferredWidth() {

        // if bars are vertical width must expand
        int size = calculateSize();
        return (size < defaultWidth) ? defaultWidth : size;
    }

    public final int getPreferredHeight() {
        return defaultHeight;
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

    void addValueToDataSet(final String rowLabel, final String columnLabel, int x, final String value, int numColumns) {

        // bar charts exclude totals and NA values
        if (isNotTotalOrNA(rowLabel) && isNotTotalOrNA(columnLabel)) {

            String legendLabel = rowLabel;
            if(numColumns > 1) {
                legendLabel = legendLabel  + "-" + columnLabel;
            }

            // use default value
            Double convertedValue = convertValue(value);
            if (convertedValue == null) {
                convertedValue = DEFAULT_VALUE;
            }

            XYSeries xySeries = new XYSeries(legendLabel, false, true);
            xySeries.setDescription(legendLabel);


            xySeries.add(x, convertedValue);

            dataSetXYSeries.add(new OverlaidXYSeries(x, xySeries, convertedValue, rowLabel, columnLabel));

            // determine if we have at least one non-zero value
            if (isNonZeroValue(convertedValue)) {
                setHasValues(true);
            }
        }
    }


    protected final String chartOrientation;
    protected String xAxisLabel;
    protected String yAxisLabel;

    protected final String chartLabel = "";

    protected int defaultWidth = 600;
    protected int defaultHeight = 600;
    protected Vector<OverlaidXYSeries> dataSetXYSeries = new Vector<OverlaidXYSeries>();

    protected static final String HORIZONTAL_ALIGNMENT = "horizontal";
    protected static final String TOOLTIP_FORMAT = "({0}) = {1}";


    public class OverlaidBarChartProducer implements XYToolTipGenerator, XYItemLinkGenerator, DatasetProducer {

        public OverlaidBarChartProducer(Vector<OverlaidXYSeries> overlaidXYSeries) {

            this.barOverlaidXYSeries = overlaidXYSeries;
        }

        public Object produceDataset(Map params) throws DatasetProduceException {
            for (OverlaidXYSeries overlaidXYSeries : barOverlaidXYSeries) {
                barDataSet.addSeries(overlaidXYSeries.getXySeries());
            }
            return barDataSet;
        }

        public boolean hasExpired(Map params, Date since) {
            return false;
        }

        public String getProducerId() {
            return getClass().getName();
        }

        public String generateToolTip(XYDataset data, int series, int item) {
            String label = (String) data.getSeriesKey(series);
            double yval = data.getYValue(series, item);

            Object[] args = new Object[]{label, yval};
            return MessageFormat.format(TOOLTIP_FORMAT, args);

        }

        public String generateLink(Object data, int series, int item) {
            return null;
        }


        private Vector<OverlaidXYSeries> barOverlaidXYSeries = new Vector<OverlaidXYSeries>();
        private NdXYSeriesCollection barDataSet = new NdXYSeriesCollection();

    }

    class OverlaidXYSeries{

        public OverlaidXYSeries(int x, double normalDist) {
            this.x = x;
            this.value = normalDist;
        }

      


        OverlaidXYSeries(int x, XYSeries xySeries, Double value, String rowLabel, String columnLabel) {
            this.x = x;
            this.xySeries = xySeries;
            this.value = value;
            this.rowLabel = rowLabel;
            this.columnLabel = columnLabel;
        }


        public XYSeries getXySeries() {
            return xySeries;
        }

        public void setXySeries(XYSeries xySeries) {
            this.xySeries = xySeries;
        }

        public int getIntValue() {
            return (int) (value < 1 ? 1 : Math.round(value));
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public String getRowLabel() {
            return rowLabel;
        }

        public void setRowLabel(String rowLabel) {
            this.rowLabel = rowLabel;
        }

        public String getColumnLabel() {
            return columnLabel;
        }

        public void setColumnLabel(String columnLabel) {
            this.columnLabel = columnLabel;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public String toString() {
            return " y "+getValue() + ", x "+getX();
        }


        int x;
        XYSeries xySeries;
        Double value;
        String rowLabel;
        String columnLabel;
    }


}