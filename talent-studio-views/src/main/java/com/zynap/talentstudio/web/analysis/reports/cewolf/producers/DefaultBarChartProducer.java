/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;

import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Mar-2006 10:18:14
 */
public class DefaultBarChartProducer extends AbstractBarChartProducer {

    public DefaultBarChartProducer(String chartType, List horizontalHeadings, List rows, String horizontalColumnLabel, String verticalColumnLabel, String axisOrientation) {
        super(chartType);
        this.horizontalHeadings = horizontalHeadings;
        this.rows = rows;

        // are we going to use the horizontal headers as the horizontal axis or the vertical headers
        final boolean useHorizontal = "horizontal".equals(axisOrientation);
        this.xAxisLabel = useHorizontal ? horizontalColumnLabel : verticalColumnLabel;
        this.yAxisLabel = useHorizontal ? verticalColumnLabel : horizontalColumnLabel;

        for (Iterator rowIterator = rows.iterator(); rowIterator.hasNext();) {
            final Row row = (Row) rowIterator.next();
            for (Iterator cellIterator = row.getCells().iterator(); cellIterator.hasNext();) {
                final Cell cell = (Cell) cellIterator.next();

                final String rowLabel = row.getHeading().getLabel();
                final String columnLabel = cell.getHeading().getLabel();
                final String value = cell.getValue();

                addValueToDataSet(useHorizontal, rowLabel, columnLabel, value);
            }
        }
    }

    int getTotalNumberOfCharacters() {

        int size = 0;

        for (int i = 0; i < horizontalHeadings.size(); i++) {
            Heading heading = (Heading) horizontalHeadings.get(i);
            size += heading.getLabel().length();
        }

        return size;
    }

    int getNumberOfColumns() {
        return horizontalHeadings.size();
    }

    int calculateSize() {

        int hSize = horizontalHeadings.size();
        int vSize = rows.size();

        int width = 30;
        if (hSize == 1) width = 20;
        if (hSize > 100) width = 10;

        return (hSize * vSize * width);
    }

    private boolean useHorizontal(List horizontalHeadings, List rows) {
        return horizontalHeadings.size() > rows.size();
    }

    protected List rows;
    protected List horizontalHeadings;
}
