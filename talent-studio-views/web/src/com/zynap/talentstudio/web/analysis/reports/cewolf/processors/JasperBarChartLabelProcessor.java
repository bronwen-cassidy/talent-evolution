package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;

import net.sf.jasperreports.engine.JasperPrint;
import org.jfree.data.category.CategoryDataset;
import org.w3c.dom.NodeList;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractChartProducer;

import java.util.List;


/**
 * User: amark
 * Date: 24-Mar-2006
 * Time: 16:34:39
 */
public class JasperBarChartLabelProcessor extends AbstractBarChartPercentLabelGenerator {

    public JasperBarChartLabelProcessor(int decimalPlaces, List selectedColumns, int numberOfColumns, JasperPrint jasperPrint) {
        super(decimalPlaces);

        NodeList nodeList = ChartHelper.getValues(jasperPrint);
        totals = new Number[numberOfColumns];

        // start
        final int length = nodeList.getLength();
        final int start = (length - numberOfColumns);

        for (int i = 0; i < selectedColumns.size(); i++) {
            Column selectedColumn = (Column) selectedColumns.get(i);
            final int selectedColumnPosition = selectedColumn.getPosition().intValue();
            final int pos = start + selectedColumnPosition;
            final String nodeValue = nodeList.item(pos).getFirstChild().getNodeValue();

            Double convertedValue = AbstractChartProducer.convertValue(nodeValue);
            if (convertedValue == null) {
                convertedValue = AbstractChartProducer.DEFAULT_VALUE;
            }

            totals[i] = convertedValue;
        }
    }

    Number getTotal(CategoryDataset dataset, int row, int column) {
        return totals[column];
    }

    private Number[] totals;
}
