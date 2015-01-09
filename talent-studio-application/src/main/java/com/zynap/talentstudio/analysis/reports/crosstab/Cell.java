/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.crosstab;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.util.IFormatter;

import org.apache.commons.lang.math.NumberUtils;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class Cell implements Serializable, Comparable<Cell> {

    private Cell(DynamicAttribute dynamicAttribute, Node sourceNode, Node targetNode, String label, String value, Long nodeExtendedAttributeId) {
        if (!StringUtils.hasText(value)) {
            value = ReportConstants.BLANK_VALUE;
        }
        this.value = value;
        this.dynamicAttribute = dynamicAttribute;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.label = label;
        this.nodeExtendedAttributeId = nodeExtendedAttributeId;
    }

    public String getAttributeName() {
        return dynamicAttribute.getLabel();
    }

    public String getDisplayValue() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return getDisplayValue();
    }

    public String getOperator() {
        return null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Heading getHeading() {
        return this.heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public boolean isValueSet() {
        return StringUtils.hasText(getValue()) && !isBlank(getValue());
    }

    public boolean isBlank(String value) {
        return !(Report.hasValue(value));
    }

    /**
     * Calls #isBlank(value) using the display value
     *
     * @return true if the value equals {@link ReportConstants#BLANK_VALUE} false otherwise
     */
    public boolean isBlank() {
        return isBlank(getDisplayValue());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;

        final Cell other = (Cell) o;

        return value != null && other.value != null && dynamicAttribute.equals(other.dynamicAttribute)
                && value.equals(other.value);

    }

    public int hashCode() {
        int result = 0;

        if (value != null) {
            result = dynamicAttribute.hashCode();
            result = 29 * result + value.hashCode();
        }

        return result;
    }

    public static Cell create(DynamicAttribute dynamicAttribute, Node sourceNode, Node targetNode, String label, String value, Long nodeExtendedAttributeId) {
        Cell defaultCell = new DefaultCell(dynamicAttribute, sourceNode, targetNode, label, value, nodeExtendedAttributeId);
        if (dynamicAttribute.typeMatches(DynamicAttribute.DA_TYPE_DATE)) {
            return new DateCell(dynamicAttribute, sourceNode, targetNode, label, value, nodeExtendedAttributeId);
        } else if (dynamicAttribute.isNumericType()) {
            return new NumberCell(dynamicAttribute, sourceNode, targetNode, label, value, nodeExtendedAttributeId);
        } else if (dynamicAttribute.isImageType()) {
            return new ImageCell(dynamicAttribute, sourceNode, targetNode, label, value, nodeExtendedAttributeId);
        }
        return defaultCell;
    }

    public static Cell createListCell(List<Cell> values, String label) {
        return new ListCell(label, values);
    }

    public boolean isList() {
        return false;
    }

    public boolean isCalculation() {
        return false;
    }

    public boolean isImage() {
        return false;
    }

    /**
     * @return the node for which we asking information about
     */
    public Node getSourceNode() {
        return sourceNode;
    }

    /**
     * The node that the attribute value has been taken from
     *
     * @return The node the attribute belongs to
     */
    public Node getTargetNode() {
        return this.targetNode;
    }

    public Long getNodeExtendedAttributeId() {
        return nodeExtendedAttributeId;
    }

    public DynamicAttribute getDynamicAttribute() {
        return dynamicAttribute;
    }

    public int getWeighting() {
        return 0;
    }

    public static class DefaultCell extends Cell {

        private DefaultCell(DynamicAttribute dynamicAttribute, Node sourceNode, Node targetNode, String label, String value, Long nodeExtendedAttributeId) {
            super(dynamicAttribute, sourceNode, targetNode, label, value, nodeExtendedAttributeId);
        }

        public DefaultCell(String value) {
            super(null, null, null, null, value, null);
        }

        public int compareTo(Cell o) {
            return getValue().compareTo(o.getValue());
        }
    }

    public static class DateCell extends Cell {

        private DateCell(DynamicAttribute dynamicAttribute, Node sourceNode, Node targetNode, String label, String value, Long nodeExtendedAttributeId) {
            super(dynamicAttribute, sourceNode, targetNode, label, value, nodeExtendedAttributeId);
        }

        public int compareTo(Cell o) {
            SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_DATE_PATTERN);
            try {
                Date me = formatter.parse(getValue());
                Date you = formatter.parse(o.getValue());
                return me.compareTo(you);
            } catch (Exception e) {
            }
            return 0;
        }
    }

    public static class NumberCell extends Cell {

        private NumberCell(DynamicAttribute dynamicAttribute, Node sourceNode, Node targetNode, String label, String value, Long nodeExtendedAttributeId) {
            super(dynamicAttribute, sourceNode, targetNode, label, value, nodeExtendedAttributeId);
        }

        public int compareTo(Cell o) {
            try {
                Long me = new Long(Long.parseLong(getValue()));
                Long you = new Long(Long.parseLong(o.getValue()));
                return me.compareTo(you);
            } catch (Exception e) {
            }
            return 0;
        }
    }

    public static class ListCell extends Cell {

        private ListCell(String label, List<Cell> values) {
            // all values are null as each of the cells in the list of values contain their own information
            super(null, null, null, label, null, null);
            this.values = values;
        }

        public boolean equals(Object o) {

            if (this == o) return true;
            if (!(o instanceof ListCell)) return false;

            final Cell cell = (ListCell) o;
            final String otherValue = cell.getDisplayValue();
            final String thisValue = this.getDisplayValue();

            return thisValue != null && otherValue != null
                    && (dynamicAttribute != null ? this.dynamicAttribute.equals(cell.dynamicAttribute) : cell.dynamicAttribute == null)
                    && thisValue.equals(otherValue);

        }

        public int hashCode() {
            int result = super.hashCode();
            result = 29 * result + getDisplayValue().hashCode();

            return result;
        }

        public int compareTo(Cell o) {
            ListCell you = (ListCell) o;
            if (values.isEmpty() && !you.values.isEmpty())
                return -1;
            else if (values.isEmpty() && you.values.isEmpty())
                return 0;
            else if (you.values.isEmpty() && !values.isEmpty())
                return 1;
            else
                return (values.get(0)).compareTo(you.values.get(0));
        }

        public List<Cell> getValues() {
            return values;
        }

        public boolean isList() {
            return true;
        }

        public String getDisplayValue() {
            if (values.isEmpty()) return ReportConstants.BLANK_VALUE;
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < values.size(); i++) {
                Cell cell = values.get(i);
                result.append(cell.getDisplayValue());
                if (i < values.size() - 1) result.append(" ");
            }
            return result.toString();
        }

        private List<Cell> values = new ArrayList<Cell>();
    }

    public static class ImageCell extends DefaultCell {

        private ImageCell(DynamicAttribute dynamicAttribute, Node sourceNode, Node targetNode, String label, String value, Long nodeExtendedAttributeId) {
            super(dynamicAttribute, sourceNode, targetNode, label, value, nodeExtendedAttributeId);
        }

        public boolean isImage() {
            return true;
        }
    }

    public static class CalculationCell extends DefaultCell {

        public CalculationCell(String value, String operator, int weighting) {
            super(value);
            this.operator = operator;
            this.weighting = weighting;
        }

        public int compareTo(Cell o) {
            return 0;
        }

        public String getDisplayValue() {
            // this is the average need to add in the weighting
            Double answer = null;
            if (count > 0 && calculation != null) {
                if (IPopulationEngine.AVG.equals(operator)) {
                    answer = calculation / count;
                }
            } else {
                answer = calculation;
            }
            // make sure it is rounded always to 2 decimal places
            if (answer != null) {
                BigDecimal bd = new BigDecimal(answer);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                return "" + bd.doubleValue();
            }
            return "-";
        }

        public void add(String value) {

            if (NumberUtils.isNumber(value)) {
                if(calculation == null) calculation = new Double(0);
                calculation += Double.valueOf(value);
                count++;
            }
        }

        public void add(String value, int weight) {
            if (weight < 1) add(value);
            else {
                if (NumberUtils.isNumber(value)) {
                    if(calculation == null) calculation = new Double(0);
                    calculation += ((Double.valueOf(value).doubleValue() * weight) / 100);
                }
            }
        }

        public boolean isCalculation() {
            return true;
        }

        public int getWeighting() {
            return weighting;
        }

        public int getCount() {
            return count;
        }

        public String getOperator() {
            return operator;
        }

        public boolean equals(Object o) {

            if (this == o) return true;
            if (!(o instanceof CalculationCell)) return false;

            final CalculationCell cell = (CalculationCell) o;
            final String otherValue = cell.getDisplayValue();
            final String thisValue = this.getDisplayValue();

            return (thisValue != null && otherValue != null) &&
                    (thisValue.equals(otherValue)) &&
                    (this.weighting == cell.weighting) &&
                    (this.count == cell.count);

        }

        public int hashCode() {
            int result = count + 12;
            result = 29 * result + getDisplayValue().hashCode();
            return result;
        }

        private Double calculation;
        private int weighting = 0;
        private int count = 0;
        private String operator;
    }

    private String value;
    private String label;
    private Heading heading;
    private Node targetNode;
    private Node sourceNode;
    private Long nodeExtendedAttributeId;
    protected DynamicAttribute dynamicAttribute;
}

