/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.dashboard.DashboardChartValue;
import com.zynap.talentstudio.dashboard.DashboardItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-May-2010 14:46:39
 */
public class DashboardItemWrapper {

    public DashboardItemWrapper() {
        dashboardItem = new DashboardItem();
    }

    public DashboardItemWrapper(DashboardItem dashboardItem) {
        this.dashboardItem = dashboardItem;
        this.report = dashboardItem.getReport();
        this.position = dashboardItem.getPosition();

        if (report != null) {
            final List<Column> columns = report.getColumns();
            initColumnString(columns);
            initChartValues(dashboardItem, columns);
        }
    }

    public String getReference() {
        String ref = super.toString();
        return ref.substring(ref.indexOf("@") + 1);
    }

    private void initColumnString(List<Column> columns) {
        String[] vals = new String[columns.size()];
        int i = 0;
        for (Column column : columns) {
            vals[i++] = column.getLabel();
        }
        this.columnString = StringUtils.arrayToDelimitedString(vals, ", ");
    }

    private void initChartValues(DashboardItem dashboardItem, List<Column> columns) {
        if (report.isChartReport()) {
            List<ChartValueWrapper> valueWrapperList = wrapValues(dashboardItem.getDashboardChartValues(), columns);
            this.dashboardChartValues = LazyList.decorate(new ArrayList<ChartValueWrapper>(valueWrapperList), FactoryUtils.instantiateFactory(ChartValueWrapper.class));
            if (dashboardChartValues.isEmpty()) {
                int j = 0;
                for (Column column : columns) {
                    dashboardChartValues.add(new ChartValueWrapper(new DashboardChartValue(column, new Integer(j++))));
                }
            }
        }
    }

    private List<ChartValueWrapper> wrapValues(Set<DashboardChartValue> dashboardChartValues, List<Column> columns) {
        List<ChartValueWrapper> wrappers = new ArrayList<ChartValueWrapper>();
        List<Column> foundColumns = new ArrayList<Column>();
        for (DashboardChartValue value : dashboardChartValues) {
            if(columns.contains(value.getColumn())) {
                foundColumns.add(value.getColumn());
            }
            wrappers.add(new ChartValueWrapper(value));    
        }
        if(foundColumns.size() != columns.size()) {
            Collection<Column> extraColumns = CollectionUtils.subtract(columns, foundColumns);
            for (Column extraColumn : extraColumns) {
                wrappers.add(new ChartValueWrapper(new DashboardChartValue(extraColumn, wrappers.size())));
            }
        }
        return wrappers;
    }

    public DashboardItem getModifiedItem() {
        Report newReport = Report.create(reportId, reportType);
        dashboardItem.setReport(newReport);
        dashboardItem.getDashboardChartValues().clear();
        if (newReport.isChartReport()) {
            for (Iterator<ChartValueWrapper> iterator = dashboardChartValues.iterator(); iterator.hasNext();) {
                ChartValueWrapper value = iterator.next();
                if (value.isValid()) {
                    dashboardItem.addDashboardChartValue(value.getModifiedChartValue());    
                } else {
                    iterator.remove();
                }
            }
        }
        return dashboardItem;
    }

    public String getLabel() {
        return dashboardItem.getLabel();
    }

    public void setLabel(String label) {
        dashboardItem.setLabel(label);
    }

    public String getDescription() {
        return dashboardItem.getDescription();
    }

    public void setDescription(String value) {
        dashboardItem.setDescription(value);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Report getReport() {
        if (this.report == null) {
            if (reportType != null && reportId != null) this.report = Report.create(reportId, reportType);
        }
        return report;
    }

    public String getColumnString() {
        return columnString;
    }

    public void setColumnString(String value) {
        this.columnString = value;
    }

    public List<ChartValueWrapper> getDashboardChartValues() {
        return dashboardChartValues;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Integer getPosition() {
        return position != null ? position : new Integer(0);
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public boolean isValid() {
        return reportId != null;
    }

    public boolean isChart() {
        return reportType != null && reportType.indexOf(Report.CHART_REPORT) != -1;
    }

    private DashboardItem dashboardItem;
    private Report report;
    private int index;
    private Long reportId;
    private String reportType;
    private List<ChartValueWrapper> dashboardChartValues = LazyList.decorate(new ArrayList<ChartValueWrapper>(), FactoryUtils.instantiateFactory(ChartValueWrapper.class));
    private String columnString;
    private Integer position = new Integer(0);
}
