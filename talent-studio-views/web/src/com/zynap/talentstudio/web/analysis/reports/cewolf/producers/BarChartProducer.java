/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import de.laures.cewolf.ChartPostProcessor;
import de.laures.cewolf.links.CategoryItemLinkGenerator;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.chart.ChartKey;
import com.zynap.talentstudio.analysis.reports.chart.ChartSlice;
import com.zynap.talentstudio.analysis.reports.chart.Chartable;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.FormatUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;

import java.awt.*;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Apr-2010 17:28:39
 */
public class BarChartProducer extends AbstractChartProducer implements ChartPostProcessor, CategoryToolTipGenerator, CategoryItemLinkGenerator {

    public BarChartProducer(Chartable chartable) {
        super();
        this.chartable = chartable;
        dataset = new DefaultCategoryDataset();
        Map<? extends Comparable, Integer> results = chartable.getMappedDataSet();
        for (Map.Entry<? extends Comparable, Integer> entry : results.entrySet()) {
            dataset.addValue(entry.getValue(), entry.getKey(), entry.getKey());
        }
    }

    @Override
    public boolean isBarChart() {
        return true;
    }

    @Override
    public int getPreferredWidth() {
        return chartable.getMappedDataSet().keySet().size() * 100;
    }

    @Override
    public int getPreferredHeight() {
        return 400;
    }

    @Override
    int getNumberOfColumns() {
        return chartable.getMappedDataSet().keySet().size();
    }

    @Override
    public String[] getColumnLabelItems() {
        Set<? extends Comparable> comparables = chartable.getMappedDataSet().keySet();
        String[] columnItemLabels = new String[comparables.size()];
        int i = 0;
        for (Comparable comparable : comparables) {
            columnItemLabels[i++] = comparable.toString();
        }
        return columnItemLabels;
    }

    @Override
    public boolean isOverlaidChart() {
        return false;
    }

    @Override
    int getTotalNumberOfCharacters() {
        int total = 0;
        String[] labelItems = getColumnLabelItems();
        for (int i = 0; i < labelItems.length; i++) {
            String labelItem = labelItems[i];
            total += labelItem.length();
        }
        return total;
    }

    public Object produceDataset(Map params) {
        return dataset;
    }


    public String generateLink(Object dataset, int series, Object category) {
        if (!StringUtils.hasText(baseUrl)) return null;

        if (chartable.getReport().getDrillDownReport() != null) {

            ChartKey key = (ChartKey) category;
            ChartSlice chartSlice = chartable.get(key);
            // we have here all the nodeExtendedAttributes and dynamicAttributes
            if (chartSlice.getCount() > 0) {
                Map<String, Object> params = new HashMap<String, Object>();
                final Column column = chartSlice.getColumn();
                params.put(ReportConstants.HORIZONTAL_ATTR_LABEL, column.getLabel());

                params.put(ReportConstants.CHART_ATTR_VALUE, key.toString());
                params.put(ReportConstants.CHART_REPORT_PARAM, "Yes");
                if (chartable.getId() != null) {
                    params.put(ReportConstants.CHART_DATA_ID, chartable.getId());
                }
                return ZynapWebUtils.buildURL(baseUrl, params);
            }
        }

        return null;
    }

    public String generateToolTip(CategoryDataset data, int series, int item) {
        Comparable columnKey = data.getColumnKey(item);
        Number value = data.getValue(data.getRowKey(series), columnKey);
        NumberFormat numberFormat = FormatUtils.applyPattern(NumberFormat.getNumberInstance(), 0);
        Double percentage = new Double(chartable.calculatePercentage(columnKey.toString()));
        String result = numberFormat.format(percentage.doubleValue());
        return MessageFormat.format(TOOLTIP_PATTERN, columnKey.toString(), value, result);
    }

    public void processChart(Object chart, Map params) {

        JFreeChart localChart = (JFreeChart) chart;
        CategoryPlot plot = localChart.getCategoryPlot();

        Color trans = new Color(0xFF, 0xFF, 0xFF, 0);
        
        localChart.setBackgroundPaint(trans);  // was null
        plot.setBackgroundPaint(trans);        // was null
        plot.setBackgroundAlpha(0.0f);
        plot.setBackgroundImageAlpha(0.0f);
        localChart.setBackgroundImageAlpha(0.0f);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // start the first element closer to the edge i.e 1 percent of the total
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setCategoryMargin(0.0);

        NumberFormat numberFormat = FormatUtils.applyPattern(NumberFormat.getNumberInstance(), 0);
        String pattern = TOOLTIP_PATTERN;
        StandardCategoryItemLabelGenerator labelGenerator;
        // pattern used for expected as apposed to actual
        if (!showNumbers) {
            pattern = TOOLTIP_PERCENT_PATTERN;
            labelGenerator = new StandardCategoryItemLabelGenerator(pattern, numberFormat);
        } else {
            labelGenerator = new StandardCategoryItemLabelGenerator(pattern, numberFormat);
        }

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(0.0);
        renderer.setMaximumBarWidth(0.5);
        //renderer.setMaxBarWidth(0.5);

        renderer.setBaseItemLabelGenerator(labelGenerator);
        plot.setDrawingSupplier(new CustomDrawingSupplier());
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setShowNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
    }

    class CustomDrawingSupplier extends DefaultDrawingSupplier {

        public CustomDrawingSupplier() {
            super();
            Collection<ChartSlice> chartSliceCollection = chartable.getValues();
            java.util.List<Paint> paints = new ArrayList<Paint>();
            for (ChartSlice chartSlice : chartSliceCollection) {
                String colour = chartSlice.getColumn().getDisplayColour();
                if (colour != null) {
                    paints.add(Color.decode("#" + colour));
                } else {
                    paints.add(super.getNextPaint());
                }
            }
            this.iterator = paints.iterator();

        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @return The item color.
         */
        /*public Paint getItemPaint(final int row, final int column) {
            Comparable key = dataset.getColumnKey(column);
            ChartSlice chartSlice = chartable.get(key);
            String colour = chartSlice.getColumn().getDisplayColour();
            if (colour != null) {
                return Color.decode("#" + colour);
            }
            return super.getItemPaint(row, column);
        }*/
        @Override
        public Paint getNextPaint() {
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return super.getNextPaint();
        }

        private Iterator<Paint> iterator;
    }

    private Chartable chartable;
    private static final String TOOLTIP_PATTERN = "{0} = {1} ({2}%)";
    private static final String TOOLTIP_PERCENT_PATTERN = "{0} {2}";

    private DefaultCategoryDataset dataset;
    private String baseUrl;
    private boolean showNumbers = true;
}