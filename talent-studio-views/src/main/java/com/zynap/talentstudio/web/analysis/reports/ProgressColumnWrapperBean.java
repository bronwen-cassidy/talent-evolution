/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.Column;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 30-Jun-2010 18:26:52
 */
public class ProgressColumnWrapperBean implements Serializable {

    public ProgressColumnWrapperBean(Column column) {
        this.column = column;
    }

    public void setAttributeName(String value) {
        column.setAttributeName(value);
    }

    public String getAttributeName() {
        return column.getAttributeName();
    }

    public Column getModifiedColumn() {
        return column;
    }

    public Integer getPosition() {
        return column.getPosition();
    }

    public void setPosition(Integer position) {
        column.setPosition(position);
    }

    public void setLabel(String label) {
        column.setLabel(label);
    }

    public String getLabel() {
        return column.getLabel();
    }

    private Column column;
}
