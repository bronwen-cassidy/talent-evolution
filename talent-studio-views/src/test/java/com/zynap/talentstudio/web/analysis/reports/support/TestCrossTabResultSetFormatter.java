/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.support;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTableKey;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.web.analysis.reports.data.CrossTabFilledReport;
import com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestCrossTabResultSetFormatter extends TestCase {

    public void setUp() {
        resultSet = new HashMap<CrossTableKey, Number>();

        final int row1 = 122;
        final int row2 = 444;

        final String col1 = "0";
        final String col2 = "2";
        final String col3 = "3";

        Collection<Heading> columnHeaders = new LinkedList<Heading>();
        columnHeaders.add(new Heading(col1, "UK"));
        columnHeaders.add(new Heading(col2, "US"));
        columnHeaders.add(new Heading(col3, "Europe"));

        rowHeaders = new LinkedList<Row>();
        rowHeaders.add(new Row(new Heading(Integer.toString(row1), "Yes")));
        rowHeaders.add(new Row(new Heading(Integer.toString(row2), "No")));

        resultSet.put(new CrossTableKey(Integer.toString(row1), col1), new Integer(32));
        resultSet.put(new CrossTableKey(Integer.toString(row2), col2), new Integer(19));
        resultSet.put(new CrossTableKey(Integer.toString(row1), col3), new Integer(16));
        resultSet.put(new CrossTableKey(Integer.toString(row2), col3), new Integer(16));
        resultSet.put(new CrossTableKey(IPopulationEngine._CROSS_TAB_TOTAL, IPopulationEngine._CROSS_TAB_TOTAL), new Integer(134));

        Report crossTab = new CrossTabReport("Test Cross Tabs", "", "Public");
        crossTab.setDefaultPopulation(new Population(new Long(-10), IPopulationEngine.P_POS_TYPE_, "name", AccessType.PUBLIC_ACCESS.toString(), "description", "", null));
        CrossTabResultSetFormatter crossTabResultSetFormatter = new CrossTabResultSetFormatter();

        int decimalPlaces = 0;
        crossTabFilledReport = crossTabResultSetFormatter.formatResults(resultSet, columnHeaders, rowHeaders, decimalPlaces, new RunCrossTabReportWrapper(crossTab));
    }

    public void testGetRowsSimple() throws Exception {
        Collection actual = crossTabFilledReport.getRows(false);
        assertEquals(rowHeaders.size(), actual.size());

        int i = 0;
        for (Iterator iterator = actual.iterator(); iterator.hasNext();i++) {
            Row row = (Row) iterator.next();

            // check header
            final Heading heading = row.getHeading();
            assertEquals(rowHeaders.get(i).getHeading(), heading);

            // check number of cells - should be 2 less as we have removed the n/a column and the total column
            final List cells = row.getCells();
            assertEquals(3, cells.size());

            for (int j = 0; j < cells.size(); j++) {
                Cell cell = (Cell) cells.get(j);
                assertNotNull(cell.getDisplayValue());

                final String colLabel = cell.getHeading().getId();
                final String rowLabel = heading.getId();

                final Object expected = resultSet.get(new CrossTableKey(rowLabel, colLabel));
                if (expected != null) assertEquals(expected, new Integer(cell.getValue()));
            }
        }
    }

    public void testGetRowsPercentage() throws Exception {
        Collection actual = crossTabFilledReport.getRows(true);
        assertEquals(rowHeaders.size(), actual.size());
        Row row = (Row) actual.iterator().next();
        Cell cell = (Cell) row.getCells().iterator().next();
        assertEquals("24", cell.getValue());
    }

    private List<Row> rowHeaders;
    private CrossTabFilledReport crossTabFilledReport;
    private Map<CrossTableKey, Number> resultSet;
}
