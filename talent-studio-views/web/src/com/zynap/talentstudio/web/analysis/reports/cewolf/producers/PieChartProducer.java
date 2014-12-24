/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import de.laures.cewolf.ChartPostProcessor;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Apr-2010 17:28:39
 */
public class PieChartProducer extends AbstractPieChartProducer implements ChartPostProcessor {

    public PieChartProducer(Chartable chartable) {
        this.chartable = chartable;
        pieDataset = new DefaultPieDataset();
        Map<? extends Comparable, Integer> results = chartable.getMappedDataSet();
        for (Map.Entry<? extends Comparable, Integer> entry : results.entrySet()) {
            pieDataset.setValue(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String[] getColumnLabelItems() {
        return new String[0];
    }

    @Override
    public boolean isOverlaidChart() {
        return false;
    }

    @Override
    int getTotalNumberOfCharacters() {
        return 0;
    }

    public Object produceDataset(Map params) {
        return pieDataset;
    }

    public String generateLink(Object dataset, Object category) {
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

    public String generateToolTip(PieDataset data, Comparable key, int pieIndex) {
        Number value = data.getValue(key);
        return MessageFormat.format(TOOLTIP_PATTERN, key.toString(), value, new Double(chartable.calculatePercentage(key.toString())));
    }

    public void processChart(Object chart, Map params) {

        JFreeChart localChart = (JFreeChart) chart;
        PiePlot piePlot = (PiePlot) localChart.getPlot();
        List<Comparable> keys = piePlot.getDataset().getKeys();

//        localChart.setBackgroundPaint(null);
//        piePlot.setBackgroundPaint(null);
//        piePlot.setBackgroundAlpha(0.0f);
//        localChart.setBackgroundImageAlpha(0.0f);

        piePlot.setLabelFont(DEFAULT_FONT);

        int index = 0;
        for (Comparable key : keys) {
            ChartSlice slice = chartable.get(key);
            Column column = slice.getColumn();
            String colour = column.getDisplayColour();
            if (StringUtils.hasText(colour)) {
                //piePlot.setSectionPaint(key, Color.decode("#" + colour));
                piePlot.setSectionPaint(index++, Color.decode("#" + colour));

            }
        }

        // default format patterns
        NumberFormat percentFormat = FormatUtils.applyPercentPattern(NumberFormat.getPercentInstance(), 2);
        NumberFormat numberFormat = FormatUtils.applyPattern(NumberFormat.getNumberInstance(), 0);
        String pattern = TOOLTIP_PATTERN;
        StandardPieSectionLabelGenerator labelGenerator;
        // pattern used for expected as apposed to actual
        if (!showNumbers) {
            pattern = TOOLTIP_PERCENT_PATTERN;
            percentFormat = FormatUtils.applyPercentPattern(NumberFormat.getPercentInstance(), 0);
            labelGenerator = new StandardPieSectionLabelGenerator(pattern, numberFormat, percentFormat);
        } else {
            labelGenerator = new StandardPieSectionLabelGenerator(pattern, numberFormat, percentFormat);
        }

        piePlot.setLabelGenerator(labelGenerator);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setShowNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
    }

    private Chartable chartable;
    private static final String TOOLTIP_PATTERN = "{0}  {2} ({1})";
    private static final String TOOLTIP_PERCENT_PATTERN = "{0} {2}";

    private DefaultPieDataset pieDataset;
    private String baseUrl;
    private boolean showNumbers = true;
    public static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 10);
}
