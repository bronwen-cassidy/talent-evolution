/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.crosstab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Row implements Serializable, Comparable {

    public Row() {
    }

    public Row(Heading heading) {
        this.heading = heading;
    }

    public void add(Cell value) {
        cells.add(value);
    }

    public List<Cell> getCells() {
        return cells;
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public void add(int index, Cell cell) {
        if(index > cells.size()) index = cells.size();
        cells.add(index, cell);
    }

    public Cell getCell(int columnIndex) {
        if (columnIndex > (cells.size() - 1)) return null;
        return cells.get(columnIndex);
    }

    public int getCellCount() {
        return cells.size();
    }

    public int compareTo(Object o) {
        Row you = (Row) o;

        final Heading otherHeading = you.getHeading();
        if (heading != null && otherHeading != null) {
            return heading.getLabel().compareTo(otherHeading.getLabel());
        }

        return 0;
    }

    private List<Cell> cells = new ArrayList<Cell>();
    private Heading heading;
}
