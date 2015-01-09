/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.common.Direction;

import org.apache.commons.collections.CollectionUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Apr-2010 21:13:59
 */
public class TabularReport extends Report {

    public TabularReport() {
        super();
    }

    public TabularReport(Long id) {
        super(id);
    }

    public TabularReport(String label, String description, String access) {
        super(label, description, access);
    }

    public TabularReport(Long id, String label, String description, String access) {
        super(id, label, description, access);
    }

    /**
     * is the equivalent to the left column on a cross tab report.
     * <br/>
     * The vertical column is identifiable by it's columnSource equal to {@link com.zynap.talentstudio.common.Direction#VERTICAL } toString value
     *
     * @return Column
     */
    public Column getVerticalColumn() {
        return (Column) CollectionUtils.find(getColumns(), new ReportDirectionPredicate(Direction.VERTICAL.toString()));
    }

    /**
     * Gets the horizontal column for the cross-tab if this is not cross-tab report returns null.
     *
     * @return Column identified by the toString() value of {@link Direction#HORIZONTAL }
     */
    public Column getHorizontalColumn() {
        return (Column) CollectionUtils.find(getColumns(), new ReportDirectionPredicate(Direction.HORIZONTAL.toString()));
    }

    public void setVerticalColumn(Column column) {
        column.setColumnSource(Direction.VERTICAL.toString());
        addColumn(column);
    }

    public void setHorizontalColumn(Column column) {
        column.setColumnSource(Direction.HORIZONTAL.toString());
        addColumn(column);
    }

    public boolean isTabularReport() {
        return true;
    }
}
