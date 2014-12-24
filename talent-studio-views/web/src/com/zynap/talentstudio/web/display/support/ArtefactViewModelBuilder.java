/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.display.support;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.ArtefactAttributeViewFormatter;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.calculations.CalculationResult;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Oct-2008 11:21:04
 */
public class ArtefactViewModelBuilder {

    public static Row buildRow(Report report, Node node, ArtefactViewQuestionnaireHelper displayHelper, Map answers) {

        final Row row = new Row();

        final List columns = report.getColumns();
        if (columns != null && !columns.isEmpty()) {


            for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                final Column column = (Column) iterator.next();
                row.add(createCell(node, column, answers, displayHelper));
            }
        }

        return row;
    }

    public static Cell createCell(Node node, Column column, Map answers, ArtefactViewQuestionnaireHelper displayHelper) {
        final AnalysisParameter parameter = column.getAnalysisParameter();

        Cell cell;
        if (parameter.isQuestionnaireAttribute()) {
            cell = getQuestionCell(node, column, answers);
        } else if (parameter.isAssociatedArtefactAttribute()) {
            List<Cell> childCells;
            if (parameter.isNestedAssociatedArtefactAttribute()) {
                childCells = getNestedAssociatedArtefactAttributeCells(node, column, displayHelper);
            } else {
                childCells = getAssociatedArtefactAttributeValues(node, column, displayHelper);
            }
            cell = Cell.createListCell(childCells, column.getLabel());
        } else if (parameter.isDynamicAttribute()) {
            cell = getDynamicAttributeCell(node, column, displayHelper);
        } else if (parameter.isDerivedAttribute()) {
            cell = getDerivedAttributeCell(node, column);
        } else {
            cell = getCoreAttributeCell(node, column);
        }
        return cell;
    }

    private static Cell getCell(AttributeValue attributeValue, Node node, Column column) {

        if (attributeValue == null) {
            final DynamicAttribute dynamicAttribute = new DynamicAttribute(column.getLabel(), column.getColumnType(), "");
            return Cell.create(dynamicAttribute, node, node, column.getLabel(), null, null);
        }

        final DynamicAttribute dynamicAttribute = attributeValue.getDynamicAttribute();
        if (dynamicAttribute.isDynamic() || dynamicAttribute.isMultiSelectionType()) {

            final List<Cell> childCells = new ArrayList<Cell>();
            final List nodeExtendedAttributes = attributeValue.getNodeExtendedAttributes();
            for (int i = 0; i < nodeExtendedAttributes.size(); i++) {
                final NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) nodeExtendedAttributes.get(i);
                final String value = ArtefactAttributeViewFormatter.formatValue(nodeExtendedAttribute, null);
                final Cell childCell = Cell.create(dynamicAttribute, node, node, column.getLabel(), value, nodeExtendedAttribute.getId());
                childCells.add(childCell);
            }

            return Cell.createListCell(childCells, column.getLabel());

        } else {
            final Node targetNode = attributeValue.getNode();
            final String value = attributeValue.getDisplayValue();
            final Long attrId = attributeValue.getId();
            return Cell.create(dynamicAttribute, node, targetNode, column.getLabel(), value, attrId);
        }
    }

    private static List<Cell> getNestedAssociatedArtefactAttributeCells(Node node, Column column, ArtefactViewQuestionnaireHelper displayHelper) {

        String columnAttributeName = column.getAttributeName();
        String attributeName = columnAttributeName.substring(columnAttributeName.indexOf(AnalysisAttributeHelper.DELIMITER) + 1);
        String nodeAttributeName = columnAttributeName.substring(0, columnAttributeName.indexOf(AnalysisAttributeHelper.DELIMITER));
        String[] values = AnalysisAttributeHelper.splitAttributeNames(attributeName);

        // we need to get the node for which the associated artefact is the target value
        Node targetNode = null;
        try {
            Method nodeMethod = node.getClass().getMethod(AnalysisAttributeHelper.ACCESSOR_PREFIX + org.apache.commons.lang.StringUtils.capitalize(nodeAttributeName));
            targetNode = (Node) nodeMethod.invoke(node);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Cell> childCells = new ArrayList<Cell>();
        if (targetNode != null) {
            childCells = getAssociatedArtefactAttributeCells(targetNode, values, column, displayHelper);
        }

        return childCells;
    }

    private static List<Cell> getAssociatedArtefactAttributeValues(Node node, Column column, ArtefactViewQuestionnaireHelper displayHelper) {
        String attributeName = column.getAttributeName();
        String[] values = AnalysisAttributeHelper.splitAttributeNames(attributeName);
        return getAssociatedArtefactAttributeCells(node, values, column, displayHelper);
    }

    private static Cell getDynamicAttributeCell(Node node, Column column, ArtefactViewQuestionnaireHelper displayHelper) {
        DynamicAttribute dynamicAttribute = displayHelper.getDynamicAttribute(node.getNodeType(), column.getAnalysisParameter());
        if (dynamicAttribute != null && dynamicAttribute.isCalculated()) {
            CalculationResult answer = dynamicAttribute.getCalculation().execute(node);
            DynamicAttribute da = new DynamicAttribute(column.getLabel(), answer.getType(), dynamicAttribute.getDescription());
            return Cell.create(da, node, node, column.getLabel(), answer.getValue(), dynamicAttribute.getId());
        }
        AttributeValue attributeValue = AnalysisAttributeHelper.findAttributeValue(column.getAnalysisParameter(), node);
        return getCell(attributeValue, node, column);
    }

    private static List<Cell> getAssociatedArtefactAttributeCells(Node targetNode, String[] values, Column column, ArtefactViewQuestionnaireHelper displayHelper) {

        List associatedNodes = AnalysisAttributeHelper.getAssociatedNodes(targetNode, values);
        List<Cell> nestedCells = new ArrayList<Cell>();
        for (int i = 0; i < associatedNodes.size(); i++) {
            Node associatedNode = (Node) associatedNodes.get(i);
            String attributeName = values[2];
            final int arrayLength = values.length - 2;
            if (arrayLength > 1) {
                String[] newValues = new String[arrayLength];
                System.arraycopy(values, 2, newValues, 0, arrayLength);
                attributeName = StringUtils.arrayToDelimitedString(newValues, ".");
            }
            final Column subColumn = new Column(column.getLabel(), attributeName, column.getColumnType());
            final Cell nextCell = createCell(associatedNode, subColumn, null, displayHelper);
            if (nextCell instanceof Cell.ListCell) {
                final List<Cell> childNestedCells = ((Cell.ListCell) nextCell).getValues();
                nestedCells.addAll(childNestedCells);
            } else {
                nestedCells.add(nextCell);
            }
        }

        return nestedCells;
    }

    private static Cell getQuestionCell(Node node, Column column, Map answers) {
        final AttributeValue answer = AnalysisAttributeHelper.findAnswer(answers, node.getId(), column.getAnalysisParameter());
        return getCell(answer, node, column);
    }

    private static Cell getCoreAttributeCell(Node node, Column column) {

        String attributeName = column.getAttributeName();
        final String value = AnalysisAttributeHelper.getCoreAttributeValue(attributeName, node);
        DynamicAttribute dynamicAttribute = new DynamicAttribute(attributeName, column.getColumnType(), attributeName);
        return Cell.create(dynamicAttribute, node, node, column.getLabel(), value, null);
    }

    private static Cell getDerivedAttributeCell(Node node, Column column) {

        String attributeName = column.getAttributeName();
        String value = AnalysisAttributeHelper.getDerivedAttributeValue(attributeName, node).toString();
        return Cell.create(new DynamicAttribute(attributeName, column.getColumnType(), attributeName), node, node, column.getLabel(), value, null);
    }
}
