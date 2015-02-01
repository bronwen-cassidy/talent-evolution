/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.talentstudio.web.analysis.reports.views.RunMetricReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractBarChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.BarChartLabelPostProcessor;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.AbstractBarChartPercentLabelGenerator;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Feb-2009 13:01:17
 */
public class HomePageDashboardWrapper implements Serializable {

    public HomePageDashboardWrapper(RunMetricReportWrapper reportWrapper, AbstractBarChartProducer producer, BarChartLabelPostProcessor labelProcessor,
                            AbstractBarChartPercentLabelGenerator percentLabelProcessor) {

        this.reportWrapper = reportWrapper;
        this.producer = producer;
        this.labelProcessor = labelProcessor;
        this.percentLabelProcessor = percentLabelProcessor;
    }

    public RunMetricReportWrapper getReportWrapper() {
        return reportWrapper;
    }

    public AbstractBarChartProducer getProducer() {
        return producer;
    }

    public BarChartLabelPostProcessor getLabelProcessor() {
        return labelProcessor;
    }

    public AbstractBarChartPercentLabelGenerator getPercentLabelProcessor() {
        return percentLabelProcessor;
    }

    private RunMetricReportWrapper reportWrapper;
    private AbstractBarChartProducer producer;
    private BarChartLabelPostProcessor labelProcessor;
    private AbstractBarChartPercentLabelGenerator percentLabelProcessor;
}
