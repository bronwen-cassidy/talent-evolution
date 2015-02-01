/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.views;

import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.ChartReportAttribute;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.chart.ChartDataStructure;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.PieChartProducer;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Feb-2006 09:40:15
 */
public class RunChartReportWrapper extends RunReportWrapperBean {

    public RunChartReportWrapper(Report reportDefinition) {
        super(reportDefinition);       
    }

    protected void initColumns(Report report) {
        ChartReport chartReport = (ChartReport) report;
        List<ChartReportAttribute> chartAttributes = chartReport.getChartReportAttributes();
        this.reportColumns = chartReport.getColumns();
        int index = 0;
        for (ChartReportAttribute chartAttribute : chartAttributes) {
            DynamicAttribute da = chartAttribute.getDynamicAttribute();
            Column col = new Column(chartAttribute.getLabel(), da.getId().toString(), index++, da.getType(), "NA");
            col.setQuestionnaireWorkflowId(chartAttribute.getQuestionnaireWorkflowId());
            this.columns.add(new ColumnWrapperBean(col));
        }
    }

    public List<Column> getReportColumns() {
        return reportColumns;
    }

    public void setProducer(AbstractChartProducer producer) {
        this.producer = producer;
    }

    public AbstractChartProducer getProducer() {
        return producer;
    }

    public boolean isResultsDisplayable() {
        return producer != null;
    }

    public void setData(ChartDataStructure data) {
        this.data = data;
    }

    public ChartDataStructure getData() {
        return data;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getChartType() {
        return chartType;
    }

    public boolean isPieChart() {
        return ((ChartReport) report).isPieChartType();
    }

    private List<Column> reportColumns;
    private AbstractChartProducer producer;
    private ChartDataStructure data;
    private String chartType;
}