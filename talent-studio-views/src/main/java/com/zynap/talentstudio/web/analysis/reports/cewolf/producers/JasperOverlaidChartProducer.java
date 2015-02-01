package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import net.sf.jasperreports.engine.JasperPrint;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataItem;

import com.zynap.talentstudio.analysis.reports.Column;

import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.ChartHelper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author taulant
 * @since 11/02/2009
 */
public class JasperOverlaidChartProducer extends AbstractOverlaidChartProducer {

    public JasperOverlaidChartProducer(String chartType, int numberOfColumns, Collection selectedColumns, ColumnWrapperBean groupingColumn, JasperPrint jasperPrint) {
        super(chartType);

        // save number of columns
        numColumns = selectedColumns.size();

        // labels
        this.xAxisLabel = groupingColumn.getLabel();
        this.yAxisLabel = "";

        // build data set
        final NodeList nodeList = ChartHelper.getValues(jasperPrint);
        for (int start = numberOfColumns; start < nodeList.getLength(); start += (numberOfColumns + 1)) {
            final Node node = nodeList.item(start);

            // this is the value of the group (eg: "Default Org Unit")
            String groupingValue = node.getFirstChild().getNodeValue();


            if (isNotTotalOrNA(groupingValue)) {
                
                // iterate selected columns and get results
                for (Iterator iterator = selectedColumns.iterator(); iterator.hasNext();) {

                    numRows++;
                    final Column selectedColumn = (Column) iterator.next();
                    final String columnLabel = selectedColumn.getLabel();

                    // this is the value for the group for the selected column (metric or function)
                    final int selectedColumnPosition = selectedColumn.getPosition().intValue();
                    final String value = nodeList.item(start + selectedColumnPosition + 1).getFirstChild().getNodeValue();
                    addValueToDataSet(groupingValue, columnLabel, numRows, value, selectedColumns.size());
                }
            }
        }
    }

    public boolean isOverlaidChart() {
        return true;
    }

    int getTotalNumberOfCharacters() {
        int size = 0;
        for (OverlaidXYSeries dataSetXYSery : dataSetXYSeries) {
            final XYSeries xySeries = dataSetXYSery.getXySeries();
            final String key = (String) xySeries.getKey();
            size += key.length();
        }

        return size;
    }

    int getNumberOfColumns() {
        return numColumns;
    }

    int calculateSize() {

        int width = 30;
        if (numRows == 1) width = 30;
        if (numRows > 100) width = 20;

        return (numRows * numColumns * width);
    }

    public String generateToolTip(XYDataset data, int series, int item) {
        return null;
    }

    public String generateLink(Object data, int series, int item) {
      return null;
    }


    private int numRows = 0;
    private int numColumns;
}