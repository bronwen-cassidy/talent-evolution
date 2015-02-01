package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import net.sf.jasperreports.engine.JasperPrint;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zynap.talentstudio.analysis.reports.Column;

import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.ChartHelper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * User: amark
 * Date: 22-Mar-2006
 * Time: 13:05:15
 */
public class JasperBarChartProducer extends AbstractBarChartProducer {

    public JasperBarChartProducer(String chartType, int numberOfColumns, Collection selectedColumns, ColumnWrapperBean groupingColumn, JasperPrint jasperPrint) {
        super(chartType);

        // save number of columns
        numColumns = selectedColumns.size();

        // labels
        boolean isGrouped = groupingColumn != null;
        this.xAxisLabel = isGrouped ? groupingColumn.getLabel() : "";
        this.yAxisLabel = "";

        // build data set
        final NodeList nodeList = ChartHelper.getValues(jasperPrint);
        // if the metric report has a grouping we handle things this way, otherwise check the else
        if (isGrouped) {
            for (int start = numberOfColumns; start < nodeList.getLength(); start += (numberOfColumns + 1)) {
                final Node node = nodeList.item(start);
                // this is the value of the group (eg: "Default Org Unit")
                final String groupingValue = node.getFirstChild().getNodeValue();
                // bar charts exclude totals and NA values
                if (isNotTotalOrNA(groupingValue)) {
                    numRows++;
                    // iterate selected columns and get results
                    for (Iterator iterator = selectedColumns.iterator(); iterator.hasNext();) {
                        
                        final Column selectedColumn = (Column) iterator.next();
                        // this is the value for the group for the selected column (metric or function)
                        final int selectedColumnPosition = selectedColumn.getPosition().intValue();
                        Node node1 = nodeList.item(start + selectedColumnPosition + 1);
                        addValue(groupingValue, selectedColumn, node1);
                    }
                }
            }

        } else {

            List<Column> columns = new ArrayList<Column>(selectedColumns);
            for (int start = 0; start < numberOfColumns; start++) {
                // the label
                final Node node = nodeList.item(start);
                // this is the value of the group (eg: "Default Org Unit")
                final String groupingValue = node.getFirstChild().getNodeValue();
                numRows++;

                final Column selectedColumn = columns.get(start);
                // the value/answer for column
                Node node1 = nodeList.item(start + numberOfColumns);
                addValue(groupingValue, selectedColumn, node1);
            }
        }
    }

    private void addValue(String groupingValue, Column selectedColumn, Node node1) {
        final String columnLabel = selectedColumn.getLabel();
        if (node1 != null) {
            final String value = node1.getFirstChild().getNodeValue();
            addValueToDataSet(true, groupingValue, columnLabel, value);
        }
    }

    int getTotalNumberOfCharacters() {
        int size = 0;

        final List columnKeys = dataSet.getColumnKeys();
        for (int i = 0; i < columnKeys.size(); i++) {
            String s = (String) columnKeys.get(i);
            size += s.length();
        }

        return size;
    }

    int getNumberOfColumns() {
        return numColumns;
    }

    int calculateSize() {

        if (numRows == 1) width = 20;
        if (numRows > 100) width = 10;

        return (int) (numRows * numColumns * width);
    }

    public void setWidth(double width) {
        this.width = width;
    }

    private int numRows = 0;
    private int numColumns;
    private double width = 30.0;
}
