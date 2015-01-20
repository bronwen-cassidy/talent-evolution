/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;

import de.laures.cewolf.ChartPostProcessor;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.ui.TextAnchor;

import com.zynap.talentstudio.web.analysis.reports.cewolf.ChartConstants;

import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Dec-2005 18:37:31
 */
public class BarChartLabelPostProcessor implements ChartPostProcessor {

    public BarChartLabelPostProcessor() {
    }

    public void processChart(Object chart, Map params) {

        CategoryItemLabelGenerator generator = (CategoryItemLabelGenerator) params.get(ChartConstants.PERCENT_ITEM_PROCESSOR);

        JFreeChart fChart = (JFreeChart)chart;
        CategoryPlot plot = fChart.getCategoryPlot();

        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setUpperBound(rangeAxis.getUpperBound() + 0.5);

        CategoryAxis domainAxis = plot.getDomainAxis();
        // make the labels at a 45 degree angle
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // start the first element closer to the edge i.e 1 percent of the total
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setCategoryMargin(0.3);

        // make the labels visible on the bars
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemLabelGenerator(generator);
        renderer.setItemLabelsVisible(true);


        ItemLabelAnchor itemLabelAnchor;
        double angle = 0.0;
        if (plot.getOrientation().equals(PlotOrientation.VERTICAL)) {
            itemLabelAnchor = ItemLabelAnchor.OUTSIDE12;
            angle = 4.75;
        } else {
            // orientation is horizontal
            itemLabelAnchor = ItemLabelAnchor.OUTSIDE3;
        }
        ItemLabelPosition position = new ItemLabelPosition(itemLabelAnchor, TextAnchor.BASELINE_CENTER, TextAnchor.CENTER, angle);
        renderer.setPositiveItemLabelPosition(position);
        renderer.setNegativeItemLabelPosition(position);
        renderer.setItemLabelAnchorOffset(30.0);
    }
}
