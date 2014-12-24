/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.dashboard;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.reports.Report;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-May-2010 17:29:42
 */
public class DashboardItem extends ZynapDomainObject {

    public DashboardItem() {
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;        
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Set<DashboardChartValue> getDashboardChartValues() {
        return dashboardChartValues;
    }

    public void setDashboardChartValues(Set<DashboardChartValue> dashboardChartValues) {
        this.dashboardChartValues = dashboardChartValues;
    }

    public void addDashboardChartValue(DashboardChartValue dashboardChartValue) {
        if (dashboardChartValue.getExpectedValue() != null) {
            dashboardChartValue.setDashboardItem(this);
            dashboardChartValue.setPosition(new Integer(dashboardChartValues.size()));
            dashboardChartValues.add(dashboardChartValue);
        }
    }

    public boolean hasChartValues() {
        return dashboardChartValues.size() > 0;
    }

    private Report report;
    private Integer position;
    private String description;
    private Dashboard dashboard;
    private Set<DashboardChartValue> dashboardChartValues = new LinkedHashSet<DashboardChartValue>();
}
