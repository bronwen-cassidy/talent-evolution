/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.dashboard;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.reports.Column;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-May-2010 17:29:42
 */
public class DashboardChartValue extends ZynapDomainObject {

    public DashboardChartValue() {
    }

    public DashboardChartValue(Column column, Integer position) {
        this.column = column;
        this.position = position;
    }

    public Integer getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(Integer expectedValue) {
        this.expectedValue = expectedValue;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public DashboardItem getDashboardItem() {
        return dashboardItem;
    }

    public void setDashboardItem(DashboardItem dashboardItem) {
        this.dashboardItem = dashboardItem;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    private Integer expectedValue;
    private Column column = new Column();
    private DashboardItem dashboardItem;
    private Integer position = new Integer(0);
}