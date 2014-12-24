package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.managers.MetricReportFiller;
import com.zynap.talentstudio.web.analysis.reports.views.RunMetricReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.calculations.Expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 01-Mar-2006
 * Time: 10:21:00
 */
public final class MetricReportRunner extends ReportRunner {

    public void run(RunReportWrapperBean wrapperBean, Long userId) throws TalentStudioException {

        RunMetricReportWrapper wrapper = (RunMetricReportWrapper) wrapperBean;
        final Report report = wrapper.getReport();
        final Population population = wrapper.getPopulation();

        final List<Metric> metrics = new ArrayList<Metric>();
        AnalysisParameter groupingAttribute = null;

        final List columns = report.getColumns();
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            Column column = (Column) iterator.next();
            if (column.isGrouped()) {
                groupingAttribute = column.getAnalysisParameter();

            } else if (column.isFormula()) {

                List<Expression> expressions = column.getCalculation().getExpressions();
                for (Expression expression : expressions) {
                    Long metricId = expression.getMetricId();
                    if (metricId != null) {
                        metrics.add((Metric) metricService.findById(metricId));
                    }
                }
                
            } else {

                Metric metric = column.getMetric();
                if (metric == null) metric = IPopulationEngine.COUNT_METRIC;
                metrics.add(metric);
            }
        }

        Map results = populationEngine.findMetrics(population, metrics, groupingAttribute, userId);
        FilledReport filledReport = reportFiller.fillReport(results, report, wrapper.getDecimalPlaces());
        wrapper.setFilledReport(filledReport);
    }

    public void setReportFiller(MetricReportFiller reportFiller) {
        this.reportFiller = reportFiller;
    }


    public void setMetricService(IMetricService metricService) {
        this.metricService = metricService;
    }

    private MetricReportFiller reportFiller;
    private IMetricService metricService;
}
