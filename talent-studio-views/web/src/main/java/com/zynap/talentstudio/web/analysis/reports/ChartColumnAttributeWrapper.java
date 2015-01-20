/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.reports.ChartColumnAttribute;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-Mar-2011 15:12:23
 */
public class ChartColumnAttributeWrapper extends AnalysisAttributeWrapperBean {

    public ChartColumnAttributeWrapper(ChartColumnAttribute attribute) {
        this.chartColumnAttribute = attribute;
        this.columnLabel = attribute.getGroupLabel();
        this.column = attribute.getColumn() == null ? new Column() : attribute.getColumn();
    }

    public ChartColumnAttribute getModifiedAttribute() {
        return (ChartColumnAttribute) chartColumnAttribute.clone();
    }

    public void setLabel(String value) {
        chartColumnAttribute.setLabel(value);
    }

    public String getLabel() {
        return chartColumnAttribute.getLabel();
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String value) {
        this.columnLabel = value;
    }

    @Override
    public BasicAnalysisAttribute getAnalysisAttribute() {
        return chartColumnAttribute;
    }

    public void setExpectedValue(Integer val) {
        chartColumnAttribute.setExpectedValue(val);
    }

    public Integer getExpectedValue() {
        return chartColumnAttribute.getExpectedValue();
    }

    public String getDisplayColour() {
        return column.getDisplayColour();
    }

    public void setDisplayColour(String value) {
        column.setDisplayColour(value);
    }

    private ChartColumnAttribute chartColumnAttribute;
    /* the group label determined from the column needed only when editing and going back and forth */
    private String columnLabel;
    private Column column;
}
