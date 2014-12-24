/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.views;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.cewolf.ChartConstants;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Feb-2006 10:43:00
 */
public class RunMetricReportWrapper extends RunReportWrapperBean {

    public RunMetricReportWrapper(Report reportDefinition) {
        super(reportDefinition);
        this.decimalPlaces=reportDefinition.getDecimalPlaces();
        
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ColumnWrapperBean columnWrapperBean = (ColumnWrapperBean) iterator.next();
            if (columnWrapperBean.isGrouped()) {
                this.groupingColumn = columnWrapperBean;
                iterator.remove();
            }
        }
    }

    public String getOperator() {
        return report.getOperator();
    }

    public List<AnalysisAttributeWrapperBean> getSettableColumns() {
        List<AnalysisAttributeWrapperBean> result = new ArrayList<AnalysisAttributeWrapperBean>(columns);
        if (groupingColumn != null) {
            result.add(groupingColumn);
        }
        return result;
    }

    public ColumnWrapperBean getGroupingColumn() {
        return groupingColumn;
    }

    public FilledReport getFilledReport() {
        return filledReport;
    }

    public void setFilledReport(FilledReport filledReport) {
        this.filledReport = filledReport;
    }

    /**
     * Find column.
     * @param columnId
     * @return ColumnWrapperBean (or null although this should never happen)
     */
    public ColumnWrapperBean findColumn(Long columnId) {

        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ColumnWrapperBean columnWrapperBean = (ColumnWrapperBean) iterator.next();
            if (columnId.equals(columnWrapperBean.getId())) {
                return columnWrapperBean;
            }
        }

        return null;
    }

    /**
     * Spring binding method used when selecting columns to display on bar chart.
     * @param columnIds
     */
    public void setSelectedMetrics(Long[] columnIds) {
        clearSelectedMetrics();
        for (int i = 0; i < columnIds.length; i++) {
            Long metricId = columnIds[i];
            setColumnWithSelectedMetric(metricId);
        }
    }

    /**
     * Spring binding method.
     * @return Long array
     */
    public Long[] getSelectedMetrics() {
        return new Long[0];
    }

    /**
     * Get list of columns selected for use in bar chart.
     * @return List
     */
    public List<Column> getSelectedColumnMetrics() {

        List<Column> selectedColumnMetrics = new ArrayList<Column>();

        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ColumnWrapperBean columnWrapperBean = (ColumnWrapperBean) iterator.next();
            if (columnWrapperBean.isMetricSelected()) {
                selectedColumnMetrics.add(columnWrapperBean.getModifiedColumn());
            }
        }

        return selectedColumnMetrics;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getChartType() {
        return chartType;
    }

    private void clearSelectedMetrics() {
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ColumnWrapperBean wrapper = (ColumnWrapperBean) iterator.next();
            wrapper.setMetricSelected(false);
        }
    }

    private void setColumnWithSelectedMetric(Long columnId) {
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ColumnWrapperBean wrapper = (ColumnWrapperBean) iterator.next();
            if (columnId.equals(wrapper.getId())) {
                wrapper.setMetricSelected(true);
            }
        }
    }

    private ColumnWrapperBean groupingColumn;
    private String chartType = ChartConstants.BAR_HORIZONTAL_TYPE_3D;
}
