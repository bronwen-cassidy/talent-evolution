package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import net.sf.jasperreports.engine.JasperPrint;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.ChartHelper;

import java.util.List;

/**
 * User: amark
 * Date: 22-Mar-2006
 * Time: 13:06:58
 */
public class JasperPieChartProducer extends AbstractPieChartProducer {

    public JasperPieChartProducer(ColumnWrapperBean selectedColumn, ColumnWrapperBean groupingColumn, JasperPrint jasperPrint, int numberOfColumns) {
        this.selectedColumn = selectedColumn;
        this.groupingColumn = groupingColumn;

        final int selectedColumnPosition = this.selectedColumn.getColumnPosition();

        final NodeList nodeList = ChartHelper.getValues(jasperPrint);

        // check if grouping column provided
        final boolean hasGrouping = hasGrouping();
        if (!hasGrouping) {

            final Node selectedNode = nodeList.item(selectedColumnPosition + numberOfColumns);
            String value = selectedNode.getFirstChild().getNodeValue();

            addValueToDataSet(selectedColumn.getLabel(), value);

        } else {

            // start iteration form number of columns and jump by number of columns + 1
            for (int start = numberOfColumns; start < nodeList.getLength(); start += (numberOfColumns + 1)) {
                final Node node = nodeList.item(start);

                // this is the value of the group (eg: "Default Org Unit")
                final String groupingValue = node.getFirstChild().getNodeValue();
                // this is the value for the group for the selected column (metric or function)
                final String nodeValue = nodeList.item(start + selectedColumnPosition + 1).getFirstChild().getNodeValue();

                addValueToDataSet(groupingValue, nodeValue);
            }
        }
    }

    public String[] getColumnLabelItems() {
        String[] labelItems = new String[3];

        labelItems[0] = "Metric";
        labelItems[1] = selectedColumn.getLabel();
        labelItems[2] = getGroupingColumnLabel();

        return labelItems;
    }

    public boolean isOverlaidChart() {
        return false;
    }

    int getTotalNumberOfCharacters() {
        int size = 0;

        final List keys = dataSet.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            String s = (String) keys.get(i);
            size += s.length();
        }

        return size;
    }

    private boolean hasGrouping() {
        return (this.groupingColumn != null);
    }

    private String getGroupingColumnLabel() {
        return hasGrouping() ? groupingColumn.getLabel() : "";
    }

    private ColumnWrapperBean selectedColumn;
    private ColumnWrapperBean groupingColumn;
}