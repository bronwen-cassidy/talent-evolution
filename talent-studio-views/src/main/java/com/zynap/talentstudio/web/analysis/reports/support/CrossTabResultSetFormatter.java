/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.DataFormatter;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTableKey;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTabCellInfo;
import com.zynap.talentstudio.web.analysis.reports.data.CrossTabFilledReport;
import com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class CrossTabResultSetFormatter implements Serializable {

    public CrossTabResultSetFormatter() {
    }

    public CrossTabFilledReport formatResults(Map resultSet, Collection<Heading> columnHeaders, Collection<Row> rowHeaders, int decimalPlaces, 
                                              RunCrossTabReportWrapper wrapper) {

        // default total is zero
        Number totalValue = new Double(0.0);
        // get total
        Object totalResult = findValue(resultSet, IPopulationEngine._CROSS_TAB_TOTAL, IPopulationEngine._CROSS_TAB_TOTAL);
        Number total = evaluateValue(totalResult);
        if (total != null) totalValue = total;

        // add NA and total row headers
        Collection<Row> rows = new ArrayList<Row>();
        if (rowHeaders != null) {
            rows = rowHeaders;
            //rows.add(new Row(new Heading(IPopulationEngine._CROSS_TAB_TOTAL, ReportConstants.TOTAL)));
        }

        // add NA and total column headers
        Collection<Heading> horizontalHeadings = new ArrayList<Heading>();
        if (columnHeaders != null) {
            //columnHeaders.add(new Heading(IPopulationEngine._CROSS_TAB_TOTAL, ReportConstants.TOTAL));
            horizontalHeadings = columnHeaders;
        }

        // clone rows to get headings
        Collection discreetRows = CollectionUtils.collect(rows, TransformerUtils.cloneTransformer());
        Collection percentRows = CollectionUtils.collect(rows, TransformerUtils.cloneTransformer());

        final List<CrossTabCellInfo> infoList = wrapper.getCrossTabCellInfos();
        // iterate rows
        Iterator discreet = discreetRows.iterator();
        Iterator percent = percentRows.iterator();
        while (discreet.hasNext() && percent.hasNext()) {
            Row discreetRow = (Row) discreet.next();
            Row percentRow = (Row) percent.next();

            int index = 0;
            for (Iterator headingsIterator = horizontalHeadings.iterator(); headingsIterator.hasNext(); index++) {
                Heading heading = (Heading) headingsIterator.next();
                final String rowId = discreetRow.getHeading().getId();
                final String headingId = heading.getId();
                Number value = evaluateValue(findValue(resultSet, rowId, headingId));
                String cellLabel = findLabel(rowId, headingId, infoList);
                formatCellValue(value, discreetRow, index, percentRow, heading, totalValue, decimalPlaces, cellLabel);
            }
        }

        return new CrossTabFilledReport(discreetRows, percentRows);
    }

    private String findLabel(String rowId, String headingId, List<CrossTabCellInfo> infoList) {
        for (Iterator<CrossTabCellInfo> crossTabCellInfoIterator = infoList.iterator(); crossTabCellInfoIterator.hasNext();) {
            CrossTabCellInfo crossTabCellInfo = crossTabCellInfoIterator.next();
            if(crossTabCellInfo.getVerticalValueId().toString().equals(rowId) && crossTabCellInfo.getHorizontalValueId().toString().equals(headingId)) {
                return crossTabCellInfo.getLabel();
            }
        }
        return "";
    }

    public static Number evaluateValue(Object object) {
        if (object == null) return null;
        if (object instanceof String) {
            return new Double(object.toString());
        } else {
            return (Number) object;
        }
    }

    private void formatCellValue(Number value, Row discreetRow, int index, Row percentRow, Heading heading, Number totalValue, int decimalPlaces, String cellLabel) {
        if (value == null) {
            Cell.DefaultCell cell = new Cell.DefaultCell(ReportConstants.BLANK_VALUE);
            cell.setHeading(heading);
            cell.setLabel(cellLabel);
            discreetRow.add(index, cell);
            percentRow.add(index, cell);
        } else {
            Cell.DefaultCell numberCell = new Cell.DefaultCell(format(value, decimalPlaces));
            numberCell.setHeading(heading);
            numberCell.setLabel(cellLabel);
            Cell.DefaultCell percentCell = new Cell.DefaultCell(calculatePercentage(value, totalValue, decimalPlaces));
            percentCell.setHeading(heading);
            percentCell.setLabel(cellLabel);
            discreetRow.add(index, numberCell);
            percentRow.add(index, percentCell);
        }
    }

    private String format(Number value, int decimalPlaces) {
        return DataFormatter.formatValue(value, decimalPlaces);
    }

    private String calculatePercentage(Number value, Number totalValue, int decimalPlaces) {
        if (totalValue.floatValue() == 0.0) return format(new Float(0.0), decimalPlaces);
        float numerator = value.floatValue();
        float denominator = totalValue.floatValue();
        float divresult = ((numerator / denominator) * (float) 100);
        return format(new Float(divresult), decimalPlaces);
    }

    public static Object findValue(Map resultSet, String rowValue, String columnValue) {
        return resultSet.get(new CrossTableKey(rowValue, columnValue));
    }
}
