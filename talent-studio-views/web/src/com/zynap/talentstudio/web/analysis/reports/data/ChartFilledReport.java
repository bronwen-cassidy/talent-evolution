/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.data;

import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.chart.ChartDataStructure;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractChartProducer;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 25-May-2010 08:50:56
 */
public class ChartFilledReport extends FilledReport {

    public ChartFilledReport(ChartDataStructure cds, AbstractChartProducer producer, String chartType) {
        super(null);
        this.chartDataStructure = cds;
        this.producer = producer;
        this.chartType = chartType;
    }

    public ChartDataStructure getChartDataStructure() {
        return chartDataStructure;
    }

    public AbstractChartProducer getProducer() {
        return producer;
    }

    public boolean isPieChart() {
        return ChartReport.PIE_CHART.equals(chartType);
    }

    public boolean isSpiderChart() {
        return false;
    }

    private ChartDataStructure chartDataStructure;
    private AbstractChartProducer producer;
    private String chartType;
}
