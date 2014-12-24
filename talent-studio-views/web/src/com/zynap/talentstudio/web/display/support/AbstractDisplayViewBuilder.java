/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.display.support;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.crosstab.ArtefactAttributeViewFormatter;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.util.resource.PropertiesManager;
import com.zynap.web.utils.HtmlUtils;

import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 16-May-2007 14:53:44
 */
public abstract class AbstractDisplayViewBuilder {

    public AbstractDisplayViewBuilder(String imageUrl, String imageMessage, String noImageMessage, boolean checkNodeAccess, String viewPositionUrl, String viewSubjectUrl) {

        this.imageUrl = imageUrl;
        this.imageMessage = imageMessage;
        this.noImageMessage = noImageMessage;
        this.checkNodeAccess = checkNodeAccess;
        this.viewPositionUrl = viewPositionUrl;
        this.viewSubjectUrl = viewSubjectUrl;
    }

    protected String getCellHtml(Cell cell) {

        boolean isLinkAttribute = cell.getValue() != null && cell.getDynamicAttribute() != null && cell.getDynamicAttribute().isLinkType();
        String cellValue;
        if (cell.isList()) {
            cellValue = getListCellHtml(cell);
        } else if (cell.isImage()) {
            cellValue = getImageCellHtml(cell);
        } else if (isLinkAttribute) {
            cellValue = getLinkAttributeHtml(cell);
        } else {
            cellValue = getLinkHtml(getLink(cell), cell);
        }
        return cellValue;
    }

    protected String getImageCellHtml(Cell cell) {

        String output;
        if (cell.isValueSet()) {
            final String format = propertiesManager.getString("cell.image.inline.format");

            // build URL with node id for security check and item id for image
            final Node node = cell.getSourceNode();
            final Long nodeId = node.getId();
            final Map params = new HashMap();
            params.put(ParameterConstants.NODE_ID_PARAM, nodeId);
            params.put(ParameterConstants.ITEM_ID, cell.getNodeExtendedAttributeId());
            final String url = ZynapWebUtils.buildURL(imageUrl, params);
            output = MessageFormat.format(format, new Object[]{nodeId, url, imageMessage});

        } else {

            final String format = propertiesManager.getString("cell.image.blank.format");
            output = MessageFormat.format(format, new Object[]{noImageMessage});
        }

        return output;
    }

    /**
     * Check if attribute supports link.
     *
     * @param attributeName
     * @param cell
     * @return true or false
     */
    protected boolean supportsLink(final String attributeName, Cell cell) {
        return AnalysisAttributeHelper.supportsLink(attributeName);
    }

    protected final Cell createCell(Node node, Column column, Map answers) {

        Cell cell;
        if (column.getAnalysisParameter().isQuestionnaireAttribute()) {
            cell = getQuestionCell(node, column, answers);
        } else if (column.getAnalysisParameter().isDynamicAttribute()) {
            cell = getDynamicAttributeCell(node, column);
        } else if (column.getAnalysisParameter().isAssociatedArtefactAttribute()) {
            List childCells;
            if (column.getAnalysisParameter().isNestedAssociatedArtefactAttribute()) {
                childCells = getNestedAssociatedArtefactAttributeCells(node, column);
            } else {
                childCells = getAssociatedArtefactAttributeValues(node, column);
            }
            cell = Cell.createListCell(childCells, column.getLabel());
        } else if (column.getAnalysisParameter().isDerivedAttribute()) {
            cell = getDerivedAttributeCell(node, column);
        } else {
            cell = getCoreAttributeCell(node, column);
        }

        return cell;
    }

    private Cell getCell(AttributeValue attributeValue, Node node, Column column) {

        if (attributeValue == null) {
            final DynamicAttribute dynamicAttribute = new DynamicAttribute(column.getLabel(), column.getColumnType());
            return Cell.create(dynamicAttribute, node, node, column.getLabel(), null, null);
        }

        // todo extend to handle multiselects in the same way ??
        final DynamicAttribute dynamicAttribute = attributeValue.getDynamicAttribute();
        if (dynamicAttribute.isDynamic()) {

            final List childCells = new ArrayList();
            final List nodeExtendedAttributes = attributeValue.getNodeExtendedAttributes();
            for (int i = 0; i < nodeExtendedAttributes.size(); i++) {
                final NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) nodeExtendedAttributes.get(i);

                // todo will not work with questions where dynamic is true and type is org unit, subject or position or user
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

    private List getNestedAssociatedArtefactAttributeCells(Node node, Column column) {

        String columnAttributeName = column.getAttributeName();
        String attributeName = columnAttributeName.substring(columnAttributeName.indexOf(AnalysisAttributeHelper.DELIMITER) + 1);
        String nodeAttributeName = columnAttributeName.substring(0, columnAttributeName.indexOf(AnalysisAttributeHelper.DELIMITER));
        String[] values = AnalysisAttributeHelper.splitAttributeNames(attributeName);

        // we need to get the node for which the associated artefact is the target value
        Node targetNode = null;
        try {
            Method nodeMethod = node.getClass().getMethod(AnalysisAttributeHelper.ACCESSOR_PREFIX + org.apache.commons.lang.StringUtils.capitalize(nodeAttributeName), null);
            targetNode = (Node) nodeMethod.invoke(node, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List childCells = new ArrayList();
        if (targetNode != null) {
            childCells = getAssociatedArtefactAttributeCells(targetNode, values, column);
        }

        return childCells;
    }

    private List getAssociatedArtefactAttributeValues(Node node, Column column) {
        String attributeName = column.getAttributeName();
        String[] values = AnalysisAttributeHelper.splitAttributeNames(attributeName);
        return getAssociatedArtefactAttributeCells(node, values, column);
    }

    private List getAssociatedArtefactAttributeCells(Node targetNode, String[] values, Column column) {

        List associatedNodes = AnalysisAttributeHelper.getAssociatedNodes(targetNode, values);

        List nestedCells = new ArrayList();
        for (int i = 0; i < associatedNodes.size(); i++) {
            Node associatedNode = (Node) associatedNodes.get(i);
            final Column subColumn = new Column(column.getLabel(), values[2], column.getColumnType());
            nestedCells.add(createCell(associatedNode, subColumn, null));
        }

        return nestedCells;
    }

    private Cell getDynamicAttributeCell(Node node, Column column) {
        return getCell(AnalysisAttributeHelper.findAttributeValue(column.getAnalysisParameter(), node), node, column);
    }

    private Cell getQuestionCell(Node node, Column column, Map answers) {
        final AttributeValue answer = AnalysisAttributeHelper.findAnswer(answers, node.getId(), column.getAnalysisParameter());
        return getCell(answer, node, column);
    }

    private Cell getCoreAttributeCell(Node node, Column column) {

        String attributeName = column.getAttributeName();
        final String value = AnalysisAttributeHelper.getCoreAttributeValue(attributeName, node);
        DynamicAttribute dynamicAttribute = new DynamicAttribute(attributeName, column.getColumnType());
        return Cell.create(dynamicAttribute, node, node, column.getLabel(), value, null);
    }

    private Cell getDerivedAttributeCell(Node node, Column column) {

        String attributeName = column.getAttributeName();
        String value = AnalysisAttributeHelper.getDerivedAttributeValue(attributeName, node).toString();
        return Cell.create(new DynamicAttribute(attributeName, column.getColumnType()), node, node, column.getLabel(), value, null);
    }

    private String getLinkAttributeHtml(Cell cell) {
        String value = cell.getDisplayValue();
        String labelValue = cell.getAttributeName();
        if (cell.isBlank()) {
            return value;
        }
        return MessageFormat.format(propertiesManager.getString("cell.link.attribute.format"), new Object[]{value, labelValue});
    }

    private String getListCellHtml(Cell cell) {
        StringBuffer linkBuffer = new StringBuffer();

        List values = ((Cell.ListCell) cell).getValues();
        if (values.isEmpty()) {
            String displayValue = HtmlUtils.htmlEscape(cell.getDisplayValue());
            linkBuffer.append(displayValue);
        } else {
            for (int i = 0; i < values.size(); i++) {
                Cell nestedCell = (Cell) values.get(i);
                final String cellValue = getCellHtml(nestedCell);
                linkBuffer.append(cellValue);
                if (i < values.size() - 1) linkBuffer.append("<br/>");
            }
        }

        return linkBuffer.toString();
    }

    private String getLinkHtml(String link, Cell cell) {

        final String displayValue = HtmlUtils.htmlEscape(cell.getDisplayValue());

        if (StringUtils.hasText(link)) {
            String cellFormat = propertiesManager.getString("cell.link.format");
            Object[] tokens = new Object[]{link, displayValue};
            return MessageFormat.format(cellFormat, tokens);
        }

        return displayValue;
    }

    private String getLink(Cell cell) {

        String link = null;

        final String attributeName = cell.getAttributeName();

        if (supportsLink(attributeName, cell)) {

            // link only to be displayed if we are not checking access (as is the case for reports as the nodes returned have already been filtered by access)
            // or if we are checking node access and the node indicates we do have access
            final Node node = cell.getSourceNode();
            if (node != null && (!checkNodeAccess || node.isHasAccess())) {
                if (node instanceof Position) {
                    link = ZynapWebUtils.buildURL(viewPositionUrl, ParameterConstants.NODE_ID_PARAM, node.getId());
                } else if (node instanceof Subject) {
                    link = ZynapWebUtils.buildURL(viewSubjectUrl, ParameterConstants.NODE_ID_PARAM, node.getId());
                }
            }
        }

        return link;
    }

    final PropertiesManager propertiesManager = PropertiesManager.getInstance(AbstractDisplayViewBuilder.class);

    String imageUrl;
    String imageMessage;
    String noImageMessage;
    boolean checkNodeAccess;
    String viewPositionUrl;
    String viewSubjectUrl;
}
