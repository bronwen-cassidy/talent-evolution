/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import org.jfree.data.category.DefaultCategoryDataset;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.ChartColumnAttribute;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.DataFormatter;
import com.zynap.talentstudio.web.analysis.reports.views.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 30-Mar-2011 10:25:47
 */
public class SpiderChartProducer implements DatasetProducer {

    public SpiderChartProducer(JRDataSource dataSource, ChartReport report, Collection artefacts) {
        
        dataset = new DefaultCategoryDataset();
        try {
            List<Column> columns = report.getColumns();
            Map<CellElement, Number> answers = new LinkedHashMap<CellElement, Number>();

            for (int i = 0; i < artefacts.size(); i++) {
                // for each node i need to call next
                dataSource.next();
                for (Column column : columns) {
                    Set<ChartColumnAttribute> attrs = column.getChartColumnAttributes();

                    for (ChartColumnAttribute attr : attrs) {
                        FieldValue value = new FieldValue(attr);
                        Object answer = dataSource.getFieldValue(value);
                        String formattedAnswer = DataFormatter.formatValue(answer);
                        try {
                            Double number = new Double(formattedAnswer);
                            CellElement element = new CellElement(column.getLabel(), attr.getLabel());
                            Number calc = answers.get(element);
                            if (calc == null) answers.put(element, number);
                            else {
                                double newVal = calc.doubleValue() + number.doubleValue();
                                final ArrayList<CellElement> keys = new ArrayList<CellElement>(answers.keySet());
                                element = keys.get(keys.indexOf(element));
                                element.incrementCount();
                                answers.put(element, newVal);
                            }
                            //defaultcategorydataset.addValue(number, column.getLabel(), attr.getLabel());
                            //System.out.println(number + "  " + column.getLabel() + "  " + attr.getLabel());
                        } catch (NumberFormatException e) {
                            // not a number
                        }
                    }
                }
            }
            for (Map.Entry<CellElement, Number> entry : answers.entrySet()) {
                CellElement cell = entry.getKey();
                Number value = entry.getValue();
                if (IPopulationEngine.AVG.equals(report.getOperator()) && cell.getCount() > 1) {
                    value = new Double(value.doubleValue() / cell.getCount());
                }
                dataset.addValue(value, cell.getGroup(), cell.getAttribute());
            }
        } catch (JRException e) {
            e.printStackTrace();
        }        
    }

    public DefaultCategoryDataset getDataSet() {
        return dataset;
    }

    static class CellElement {

        private int count = 0;
        private final String group;
        private final String attribute;

        public CellElement(String group, String attribute) {
            this.group = group;
            this.attribute = attribute;
        }

        public String getGroup() {
            return group;
        }

        public String getAttribute() {
            return attribute;
        }

        public void incrementCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CellElement that = (CellElement) o;

            if (!attribute.equals(that.attribute)) return false;
            if (!group.equals(that.group)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = group.hashCode();
            result = 31 * result + attribute.hashCode();
            return result;
        }
    }

    public Object produceDataset(Map params) throws DatasetProduceException {
        return dataset;
    }

    public boolean hasExpired(Map params, Date since) {
        return true;
    }

    public String getProducerId() {        
        return getClass().getName();
    }

    private DefaultCategoryDataset dataset;
}
