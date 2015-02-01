/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import org.jfree.data.xy.XYDataset;

import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;

import java.util.Iterator;
import java.util.List;

/**
 * @author taulant
 * @since 11/02/2009
 */

public class DefaultOverlaidChartProducer extends AbstractOverlaidChartProducer {

    public DefaultOverlaidChartProducer(String chartType, List horizontalHeadings, List rows, String horizontalColumnLabel, String verticalColumnLabel, String axisOrientation) {
        super(chartType);
        this.horizontalHeadings = horizontalHeadings;
        this.rows = rows;

        // are we going to use the horizontal headers as the horizontal axis or the vertical headers
        final boolean useHorizontal = "horizontal".equals(axisOrientation);
        this.xAxisLabel = useHorizontal ? horizontalColumnLabel : verticalColumnLabel;
        this.yAxisLabel = useHorizontal ? verticalColumnLabel : horizontalColumnLabel;


        if (useHorizontal) {
            //do vertical plottings

            final int length = computeLength(rows);
            int xpos = 0;
            int ypos = 0;
            /**
             * iterate total number of grids assumuning grid provided has even rows and columns
             *
             * iterate each of the rows and pick cells in index order providing vertical plotting
             *
             * 
             */
            for (int j = 0; j < length; j++) {

                final Row row = (Row) rows.get(xpos);

                final List<Cell> cellList = row.getCells();
                numRows++;


                final Cell cell = cellList.get(ypos);
                final String rowLabel = row.getHeading().getLabel();
                final String columnLabel = cell.getHeading().getLabel();
                final String value = cell.getValue();


                addValueToDataSet(rowLabel, columnLabel, numRows, value, horizontalHeadings.size());

                //update index settings
                /**
                 * if xpos=has reached the size the increase y position to be +1 and do again iteration
                 */
                if (xpos == rows.size()-1) {
                    xpos = 0;
                    ypos++;
                } else {
                    xpos++;
                }

            }
        } else {

            for (Iterator rowIterator = rows.iterator(); rowIterator.hasNext();) {
                final Row row = (Row) rowIterator.next();
                for (Iterator cellIterator = row.getCells().iterator(); cellIterator.hasNext();) {
                    final Cell cell = (Cell) cellIterator.next();
                    numRows++;

                    final String rowLabel = row.getHeading().getLabel();
                    final String columnLabel = cell.getHeading().getLabel();
                    final String value = cell.getValue();


                    addValueToDataSet(rowLabel, columnLabel, numRows, value, horizontalHeadings.size());
                }
            }
        }
    }

    private int computeLength(List<Row> rows) {
        int n=0;
        for (Row row : rows) {
            n+=(row.getCells().size());
        }
        return n;
    }

    public boolean isOverlaidChart() {
        return true;
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
        if (hSize == 1) width = 30;
        if (hSize > 100) width = 20;

        return (hSize * vSize * width);
    }

    public String generateToolTip(XYDataset data, int series, int item) {
        return null;
    }

    public String generateLink(Object data, int series, int item) {
        return null;
    }

    protected List rows;
    protected List horizontalHeadings;
    private int numRows = 0;
}