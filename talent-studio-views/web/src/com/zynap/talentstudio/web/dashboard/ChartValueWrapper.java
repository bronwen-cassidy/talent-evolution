/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.dashboard.DashboardChartValue;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 04-Jun-2010 09:37:47
 */
public class ChartValueWrapper implements Serializable {


    public ChartValueWrapper() {
        value = new DashboardChartValue();
    }

    public ChartValueWrapper(DashboardChartValue value) {
        this.value = value;
        this.column = value.getColumn();
        if(column != null) {
            this.columnLabel = column.getLabel();            
        }
    }

    public DashboardChartValue getModifiedChartValue() {
        value.setColumn(new Column(columnId, columnLabel));
        return value;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getColumnLabel() {
        return columnLabel != null ? columnLabel : column != null ? column.getLabel() : null;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public Integer getExpectedValue() {
        return value.getExpectedValue();
    }

    public void setExpectedValue(Integer info) {
        value.setExpectedValue(info);
    }

    public boolean isValid() {
        return columnId != null;
    }

    public Integer getPosition() {
        return value.getPosition();
    }

    public void setPosition(Integer position) {
        value.setPosition(position);
    }

    public Column getColumn() {
        return column;
    }

    private DashboardChartValue value;
    private Long columnId;
    private String columnLabel;
    private Column column = new Column();
}
