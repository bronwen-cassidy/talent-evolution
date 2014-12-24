/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;

import de.laures.cewolf.ChartPostProcessor;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;

import java.text.NumberFormat;
import java.util.Map;

/**
 * Class or Interface description.
 * <br/> Post process for pie charts - builds labels for legend and for sections.
 *
 * @author bcassidy
 * @version 0.1
 * @since 15-Mar-2006 17:00:47
 */
public class PieChartPostProcessor implements ChartPostProcessor {

    public PieChartPostProcessor(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public void processChart(Object chart, Map params) {
        JFreeChart fChart = (JFreeChart) chart;
        PiePlot plot = (PiePlot) fChart.getPlot();

        // Don't put an outline around chart - CSS style will do that if required 

        plot.setOutlinePaint(null);
// Needs up-to-date JFreeChart...
//        fChart.setPadding(RectangleInsets.ZERO_INSETS);

        // set the pie charts legend label format
        StandardPieSectionLabelGenerator legendGenerator = new StandardPieSectionLabelGenerator(LEGEND_LABEL_FORMAT);
        plot.setLegendLabelGenerator(legendGenerator);

        // set the pie charts display label format
        final NumberFormat numberFormat = FormatUtils.applyPattern(NumberFormat.getNumberInstance(), decimalPlaces);
        final NumberFormat percentFormat = FormatUtils.applyPercentPattern(NumberFormat.getPercentInstance(), decimalPlaces);
        StandardPieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(ITEM_LABEL_FORMAT, numberFormat, percentFormat);

        plot.setLabelGenerator(labelGenerator);
    }

    private int decimalPlaces;

    private final String LEGEND_LABEL_FORMAT = "{0}";

    private static final String ITEM_LABEL_FORMAT = "{0}: ({1}, {2})";
}
