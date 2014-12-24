/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.views;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTabCellInfo;
import com.zynap.talentstudio.util.collections.DomainObjectCollectionHelper;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.cewolf.ChartConstants;
import com.zynap.talentstudio.web.analysis.reports.data.CrossTabFilledReport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Feb-2006 09:40:15
 */
public class RunCrossTabReportWrapper extends RunReportWrapperBean {

    public RunCrossTabReportWrapper(Report reportDefinition) {
        super(reportDefinition);
        this.metric = report.getDefaultMetric();
        this.resultFormat=reportDefinition.getDisplayOption();
        this.decimalPlaces=reportDefinition.getDecimalPlaces();
        for (AnalysisAttributeWrapperBean column : columns) {
            final Long id = column.getAnalysisAttribute().getId();
            if(id.equals(((CrossTabReport) report).getHorizontalColumn().getId())) {
                this.horizontalColumn = column;
            } else if (id.equals(((CrossTabReport) report).getVerticalColumn().getId())) {
                this.verticalColumn = column;
            }
        }
    }

    public Collection getRows() {
        return getCrossTabFilledReport().getRows(isPercent());
    }

    public Report getDisplayReport() {
        return report.getDisplayReport();
    }

    public Integer getDisplayLimit() {
        return ((CrossTabReport) report).getDisplayLimit();
    }

    public Metric getMetric() {
        return metric;
    }

    public Long getMetricId() {
        return metric != null ? metric.getId() : null;
    }

    public void setMetricId(Long metricId) {
        this.metric = (Metric) DomainObjectCollectionHelper.findById(metrics, metricId);
        if (this.metric == null) {
            this.metric = IPopulationEngine.COUNT_METRIC;
        }
    }

    public Collection getMetrics() {
        return metrics;
    }

    public void setMetrics(Collection metrics) {
        this.metrics = metrics;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public void setOverflow(Integer overflow) {
        this.overflow = overflow;
    }

    public Integer getOverflow() {
        return overflow;
    }

    public int getResultFormat() {
        return this.resultFormat;
    }

    public void setResultFormat(int resultFormat) {
        this.resultFormat = resultFormat;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public boolean isPercent() {
        return resultFormat == PERCENTAGE_VALUE;
    }

    public boolean isArtefactView() {
        return resultFormat == ARTEFACTS_VALUE;
    }

    public boolean isResultsDisplayable() {
        return super.isResultsDisplayable() || getOverflow() != null;
    }

    public boolean isHasResults() {
        boolean resultsDisplayable;

        if (isArtefactView()) {
            // this shows peoples and positions each cell consists of a tabular report therefore we need to get that report not the crosstab one 
            resultsDisplayable = isResultsDisplayable() && getFilledReport().isHasResults();
        } else {
            resultsDisplayable = isResultsDisplayable() && !getRows().isEmpty();
        }

        return resultsDisplayable;
    }

    public String getHorizontalHeader() {
        return ((CrossTabReport) report).getHorizontalColumn().getLabel();
    }

    public String getVerticalHeader() {
        return ((CrossTabReport) report).getVerticalColumn().getLabel();
    }

    public String getVerticalColumnAttribute() {
        return AnalysisAttributeHelper.getName(((CrossTabReport) report).getVerticalColumn().getAnalysisParameter());
    }

    public String getHorizontalColumnAttribute() {
        return AnalysisAttributeHelper.getName(((CrossTabReport) report).getHorizontalColumn().getAnalysisParameter());
    }

    public AnalysisAttributeWrapperBean getHorizontalColumn() {
        return horizontalColumn;
    }

    public Collection getDiscreetRows() {
        return getCrossTabFilledReport().getDiscreetRows();
    }

    public boolean isHasDisplayReport() {
        return report.getDisplayReport() != null;
    }

    private CrossTabFilledReport getCrossTabFilledReport() {
        return (CrossTabFilledReport) getFilledReport();
    }

    public void setHorizontalHeadings(Collection columnHeaders) {
        this.horizontalHeadings = columnHeaders;
    }

    public Collection getHorizontalHeadings() {
        return horizontalHeadings;
    }

    public void setRowHeadings(Collection rowHeaders) {
        this.rowHeadings = rowHeaders;
    }

    public Collection getRowHeadings() {
        return rowHeadings;
    }

    public AnalysisAttributeWrapperBean getVerticalColumn() {
        return verticalColumn;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public Number getTotal() {
        return total;
    }

    public void setRowNaTotal(Object rowNaTotal) {
        this.rowNaTotal = rowNaTotal;
    }

    public Object getRowNaTotal() {
        return rowNaTotal != null ? rowNaTotal : "0";
    }

    public void setColumnNaTotal(Object columnNaTotal) {
        this.columnNaTotal = columnNaTotal;
    }

    public Object getColumnNaTotal() {
        return columnNaTotal != null ? columnNaTotal : "0";
    }

    public void setCrosstabCellInfos(List<CrossTabCellInfo> crossTabCellInfos) {
        this.crossTabCellInfos = crossTabCellInfos;
    }

    public List<CrossTabCellInfo> getCrossTabCellInfos() {
        return crossTabCellInfos;
    }

    public void setAxisOrientation(String axisOrientation) {
        this.axisOrientation = axisOrientation;
    }

    public String getAxisOrientation() {
        return axisOrientation;
    }

    /**
     * The metric to run (can be changed at run-time.)
     */
    private Metric metric;

    /**
     * The collection of metrics that can be run.
     */
    private Collection metrics;

    /**
     * The chart type.
     */
    private String chartType = ChartConstants.BAR_VERTICAL_TYPE_3D;

    /**
     * The overflow - actually the number of cells produced by a report - used to indicate that a cross tab is considered too large to display.
     */
    private Integer overflow;

    /**
     * Determines whether we return the discrete cells or the percentage values.
     */
    private int resultFormat;

    private Collection horizontalHeadings;

    private Collection rowHeadings;

    /**
     * Number of decimal places.
     */
    //private int decimalPlaces = -1;

    public static final int DISCREET_VALUE = 0;
    public static final int PERCENTAGE_VALUE = 1;
    public static final int ARTEFACTS_VALUE = 2;
    private AnalysisAttributeWrapperBean horizontalColumn;
    private AnalysisAttributeWrapperBean verticalColumn;
    private Number total;
    private Object rowNaTotal;
    private Object columnNaTotal;
    private List<CrossTabCellInfo> crossTabCellInfos = new ArrayList<CrossTabCellInfo>();
    private String axisOrientation = "horizontal";
}
