/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;

import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Mar-2006 10:16:45
 */
public class DefaultPieChartProducer extends AbstractPieChartProducer {

    public DefaultPieChartProducer(Row row, String columnLabel, String groupedColumnLabel, Report report) {
        this.row = row;
        this.columnLabel = columnLabel;
        this.groupedColumnLabel = groupedColumnLabel;
        this.verticalColumn = ((CrossTabReport) report).getVerticalColumn();
        this.horizontalColumn = ((CrossTabReport) report).getHorizontalColumn();

        for (Iterator iterator = row.getCells().iterator(); iterator.hasNext();) {
            final Cell cell = (Cell) iterator.next();
            final String headingLabel = cell.getHeading().getLabel();
            final String value = cell.getValue();

            addValueToDataSet(headingLabel, value);
        }
    }

    int getTotalNumberOfCharacters() {
        int size = 0;

        final List cells = row.getCells();
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = (Cell) cells.get(i);
            size += cell.getHeading().getLabel().length();
        }

        return size;
    }

    public String[] getColumnLabelItems() {
        String[] labelItems = new String[3];

        labelItems[0] = columnLabel;
        labelItems[1] = groupedColumnLabel;

        String verticalColumnAxis = verticalColumn.getLabel();
        labelItems[2] = verticalColumnAxis.equals(columnLabel) ? horizontalColumn.getLabel() : verticalColumnAxis;

        return labelItems;
    }

    public boolean isOverlaidChart() {
        return false;
    }

    private final Row row;
    private final String columnLabel;
    private final String groupedColumnLabel;

    private Column verticalColumn;
    private Column horizontalColumn;
}
