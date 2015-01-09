/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Apr-2010 11:15:00
 */
public class ChartColumnAttribute extends BasicAnalysisAttribute implements Serializable, Cloneable {

    public ChartColumnAttribute() {
    }

    public Integer getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(Integer expectedValue) {
        this.expectedValue = expectedValue;
    }

    public Long getColumnId() {
        return column != null ? column.getId() : null;
    }    

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public String getGroupLabel() {
        return column != null ? column.getLabel() : null;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new ChartColumnAttribute();
        }
    }

    private Column column;
    private Integer expectedValue;
}