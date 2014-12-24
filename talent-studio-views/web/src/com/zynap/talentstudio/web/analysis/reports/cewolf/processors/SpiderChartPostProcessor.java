/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;

import de.laures.cewolf.ChartPostProcessor;
import de.laures.cewolf.links.CategoryItemLinkGenerator;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;

import org.springframework.util.StringUtils;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 * <br/> Post process for pie charts - builds labels for legend and for sections.
 *
 * @author bcassidy
 * @version 0.1
 * @since 15-Mar-2006 17:00:47
 */
public class SpiderChartPostProcessor implements ChartPostProcessor, CategoryToolTipGenerator, CategoryItemLinkGenerator {

    public SpiderChartPostProcessor(ChartReport report, DefaultCategoryDataset dataSet) {
        this.chartReport = report;
        this.dataSet = dataSet;
        this.generator = new StandardCategoryToolTipGenerator();
    }

    public void processChart(Object chart, Map params) {
        JFreeChart fChart = (JFreeChart) chart;
        SpiderWebPlot plot = (SpiderWebPlot) fChart.getPlot();
        //fChart.setBackgroundPaint(null);
        //plot.setBackgroundPaint(null);
        
        //fChart.setBackgroundImageAlpha(0.0f);
        //plot.setBackgroundImageAlpha(0.0f);
        //plot.setBackgroundAlpha(0.0f);

        // Don't put an outline around chart - CSS style will do that if required
        plot.setOutlinePaint(null);
        List<Comparable> keys = dataSet.getRowKeys();
        int index = 0;
        for (Comparable key : keys) {
            Column column = findColumn(key, chartReport.getColumns());
            String colour = column.getDisplayColour();
            if (StringUtils.hasText(colour)) {
                plot.setSeriesPaint(index++, Color.decode("#" + colour));
            }
        }

        plot.setLabelGenerator(new StandardCategoryItemLabelGenerator());
    }

    private Column findColumn(Comparable key, List<Column> columns) {
        for (Column column : columns) {
            if (column.getLabel().equals(key)) {
                return column;
            }
        }
        return null;
    }

    public String generateLink(Object dataset, int series, Object category) {
        return "testing.htm?series = " +series + "&category=" + category;
    }

    public String generateToolTip(CategoryDataset data, int series, int item) {
        return generator.generateToolTip(data, series, item);
    }

    private ChartReport chartReport;
    private DefaultCategoryDataset dataSet;
    private StandardCategoryToolTipGenerator generator;
}