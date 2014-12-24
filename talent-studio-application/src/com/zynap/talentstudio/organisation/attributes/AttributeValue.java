/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.reports.crosstab.ArtefactAttributeViewFormatter;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.Node;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Class that wraps a number of NodeExtendedAttributes (at least 1).
 *
 * @author bcassidy
 */
public class AttributeValue extends ZynapDomainObject implements Comparable<AttributeValue> {

    AttributeValue(DynamicAttribute dynamicAttribute) {
        super();
        this.dynamicAttribute = dynamicAttribute;
    }

    public final String getValue() {

        if (value == null) {
            evaluate();
        }

        return value;
    }

    public final void setValue(String value) {
        this.value = value;
    }

    public final boolean hasValue() {
        return StringUtils.hasText(getValue());
    }

    public final void clearValue() {
        value = "";
    }

    public void initialiseNodeExtendedAttributes(String newValue) {

        value = newValue;

        if (dynamicAttribute != null) {
            if (dynamicAttribute.supportsMultipleAnswers()) {
                processMultipleValues(value);
            } else {
                processSingleValue(value);
            }
        }
    }

    public final void addValue(NodeExtendedAttribute nodeExtendedAttribute, boolean hasToEvaluate) {
        nodeExtendedAttributes.add(nodeExtendedAttribute);
        Integer position = nodeExtendedAttribute.getMaxDynamicPosition();
        if(position != null && position > maxDynamicPosition) {
            maxDynamicPosition =  nodeExtendedAttribute.getMaxDynamicPosition();
        }
        if (isBlogComment(nodeExtendedAttribute)) sort(nodeExtendedAttributes);
        if (hasToEvaluate) evaluate();
    }

    private boolean isBlogComment(NodeExtendedAttribute nodeExtendedAttribute) {
        return nodeExtendedAttribute != null && nodeExtendedAttribute.getDynamicAttribute().isBlogComment();
    }

    public final NodeExtendedAttribute getLastNodeExtendedAttribute() {
        final int index = nodeExtendedAttributes.size() - 1;
        return getNodeExtendedAttribute(index);
    }

    public NodeExtendedAttribute getNodeExtendedAttribute(final int index) {
        return nodeExtendedAttributes.size() > index ? nodeExtendedAttributes.get(index) : null;
    }

    public NodeExtendedAttribute getAttributeAtDynamicPosition(int dynamicPosition) {
        if (nodeExtendedAttributes == null) return null;

        for (NodeExtendedAttribute attribute : nodeExtendedAttributes) {
            final Integer position = attribute.getDynamicPosition();
            if (position != null && position.equals(dynamicPosition)) {
                return attribute;
            }
        }
        return null;
    }

    public NodeExtendedAttribute getAttributeAtLastDynamicPosition() {
        if(maxDynamicPosition == null) maxDynamicPosition = new Integer(0);
        return getAttributeAtDynamicPosition(maxDynamicPosition.intValue());
    }

    public final DynamicAttribute getDynamicAttribute() {
        return dynamicAttribute;
    }

    public final Node getNode() {
        return node;
    }

    /**
     * Get display value (will return value formatted according to dynamic attribute type or label is dynamic attribute is of type node or last updated by).
     *
     * @return display value or empty string (not null)
     */
    public String getDisplayValue() {
        //if (nodeExtendedAttributes.size() > 1 && !(dynamicAttribute.isMultiSelectionType() || dynamicAttribute.isBlogComment())) {
            // split get displayValue and re-add
            String[] answers = new String[nodeExtendedAttributes.size()];
            int index = 0;
            for (NodeExtendedAttribute nodeExtendedAttribute : nodeExtendedAttributes) {
                answers[index] = ArtefactAttributeViewFormatter.formatValue(nodeExtendedAttribute, label);
                index++;
            }
            return StringUtils.arrayToCommaDelimitedString(answers);
        //}
        //return ArtefactAttributeViewFormatter.formatValue(dynamicAttribute, value, label);
    }

    public final List<NodeExtendedAttribute> getNodeExtendedAttributes() {
        return nodeExtendedAttributes;
    }

    public final LookupValue getSelectionLookupValue() {
        final LookupType lookupType = dynamicAttribute.getRefersToType();
        return getSelectionLookupValue(lookupType, value);
    }

    public final String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("value", getValue())
                .toString();
    }

    /**
     * Process single value.
     *
     * @param value
     */
    private void processSingleValue(String value) {

        if (nodeExtendedAttributes.size() == 1) {
            nodeExtendedAttributes.get(0).setValue(value);
        } else {
            NodeExtendedAttribute nodeExtendedAttribute = new NodeExtendedAttribute(value, node, dynamicAttribute);
            addValue(nodeExtendedAttribute, false);
        }
    }

    /**
     * Processes multiple values.
     *
     * @param value Comma-delimited list.
     */
    private void processMultipleValues(String value) {

        nodeExtendedAttributes.clear();

        String[] values = StringUtils.commaDelimitedListToStringArray(value);
        for (int i = 0; i < values.length; i++) {
            String s = values[i];
            NodeExtendedAttribute nodeExtendedAttribute = new NodeExtendedAttribute(s, node, dynamicAttribute);
            addValue(nodeExtendedAttribute, false);
        }
    }

    /**
     * Build up value based on node extended attributes.
     */
    private void evaluate() {

        if (!nodeExtendedAttributes.isEmpty()) {

            final int numNodeExtAttrs = nodeExtendedAttributes.size();

            if (numNodeExtAttrs > 1) {
                sort(nodeExtendedAttributes);
            }

            String[] values = new String[numNodeExtAttrs];
            int i = 0;
            for (Iterator iterator = nodeExtendedAttributes.iterator(); iterator.hasNext();) {
                NodeExtendedAttribute nodeExtendedAttribute = (NodeExtendedAttribute) iterator.next();
                values[i++] = nodeExtendedAttribute.getValue();
            }

            if (values[0] != null) {
                value = StringUtils.arrayToCommaDelimitedString(values);
            }
        }
    }

    /**
     * Sort node extended attributes.
     * <br/> Sorts multi-selects by lookup value sort order, and sorts dynamic line item attributes by dynamic position.
     *
     * @param nodeExtendedAttributes
     */
    private void sort(List<NodeExtendedAttribute> nodeExtendedAttributes) {
        Collections.sort(nodeExtendedAttributes, new Comparator<NodeExtendedAttribute>() {
            public int compare(NodeExtendedAttribute n1, NodeExtendedAttribute n2) {

                // default is to leave order unchanged
                int order = 0;

                if (isMultiSelection(n1) && isMultiSelection(n2)) {

                    // sort by lookup value sort order
                    LookupValue n1Value = n1.getDynamicAttribute().getRefersToType().getLookupValue(Long.valueOf(n1.getValue()));
                    LookupValue n2Value = n2.getDynamicAttribute().getRefersToType().getLookupValue(Long.valueOf(n2.getValue()));
                    order = n1Value.compareBySortOrder(n2Value);

                } else if (isDynamic(n1) && isDynamic(n2)) {

                    // sort by dynamic position
                    final Integer dynamicPosition = n1.getDynamicPosition();
                    if (dynamicPosition != null) {
                        order = dynamicPosition.compareTo(n2.getDynamicPosition());
                    }

                } else if (isBlogComment(n1) && isBlogComment(n2)) {

                    final Date dateN1 = n1.getDateAdded();
                    final Date dateN2 = n2.getDateAdded();

                    Date dateOne = dateN1 != null ? new Date(dateN1.getTime()) : null;
                    Date dateTwo = dateN2 != null ? new Date(dateN2.getTime()) : null;
                    if (dateTwo == null) order = 1;
                    else if (dateOne == null) order = -1;
                    else order = dateTwo.compareTo(dateOne);
                }

                return order;
            }
        });
    }

    /**
     * Is the NodeExtendedAttribute linked to a dynamic line item extended attribute.
     *
     * @param nodeExtendedAttribute
     * @return true or false
     */
    private boolean isDynamic(NodeExtendedAttribute nodeExtendedAttribute) {
        return nodeExtendedAttribute != null && nodeExtendedAttribute.getDynamicAttribute().isDynamic();
    }

    /**
     * Is the NodeExtendedAttribute linked to a multiselect extended attribute.
     *
     * @param nodeExtendedAttribute
     * @return true or false
     */
    private boolean isMultiSelection(NodeExtendedAttribute nodeExtendedAttribute) {
        return nodeExtendedAttribute != null && nodeExtendedAttribute.getDynamicAttribute().isMultiSelectionType();
    }

    /**
     * Get lookup value from lookup type.
     *
     * @param lookupType
     * @param lookupValueId
     * @return LookupValue or null
     */
    private LookupValue getSelectionLookupValue(LookupType lookupType, String lookupValueId) {
        return lookupType != null ? lookupType.getLookupValue(new Long(lookupValueId)) : null;
    }

    public static AttributeValue create(DynamicAttribute dynamicAttribute) {
        if (dynamicAttribute != null) {
            if (dynamicAttribute.isImageType()) {
                return new AttributeValueFile(dynamicAttribute);
            } else if (dynamicAttribute.isBlogComment()) {
                return new BlogCommentAttributeValue(dynamicAttribute);
            }
        }
        return new AttributeValue(dynamicAttribute);
    }

    public static AttributeValue create(String value, DynamicAttribute dynamicAttribute) {
        if(!StringUtils.hasText(value)) return null;
        AttributeValue newAttributeValue = create(dynamicAttribute);
        newAttributeValue.initialiseNodeExtendedAttributes(value);
        return newAttributeValue;
    }

    public static AttributeValue create(String value, Node node, DynamicAttribute dynamicAttribute) {
        AttributeValue newAttributeValue = create(value, dynamicAttribute);
        newAttributeValue.node = node;
        return newAttributeValue;
    }

    public static AttributeValue create(Node node, DynamicAttribute dynamicAttribute) {
        AttributeValue newAttributeValue = create(dynamicAttribute);
        newAttributeValue.node = node;
        return newAttributeValue;
    }

    public static AttributeValue create(NodeExtendedAttribute nodeExtendedAttribute) {
        final DynamicAttribute dynamicAttribute = nodeExtendedAttribute.getDynamicAttribute();
        final AttributeValue attributeValue = create(dynamicAttribute);
        attributeValue.setId(nodeExtendedAttribute.getId());
        attributeValue.node = nodeExtendedAttribute.getNode();
        attributeValue.addValue(nodeExtendedAttribute, true);
        attributeValue.disabled = nodeExtendedAttribute.isDisabled();
        return attributeValue;
    }

    public String getCalculatableField() {
        String numberValue;
        LookupType type = dynamicAttribute.getRefersToType();
        if (dynamicAttribute.isSelectionType()) {
            LookupValue lookupValue = type.getLookupValue(new Long(getValue()));
            numberValue = lookupValue.getValueId();
        } else if (dynamicAttribute.isMultiSelectionType()) {
            double runningTotal = 0;
            final String[] lookupValueIds = StringUtils.commaDelimitedListToStringArray(getValue());
            for (int i = 0; i < lookupValueIds.length; i++) {
                String lookupValueId = lookupValueIds[i];
                LookupValue lookupValue = type.getLookupValue(new Long(lookupValueId));
                runningTotal += Double.parseDouble(lookupValue.getValueId());
            }
            numberValue = String.valueOf(runningTotal);
        } else {
            numberValue = getDisplayValue();
        }
        return numberValue;
    }

    public int compareTo(AttributeValue o) {
        return getValue().compareTo(o.getValue());
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean value) {
        this.disabled = value;
    }

    /**
     * @param attributeList - the new Attribute values created by dwr, please note this method is only valid for questionnaires which
     *                      are handled by DWR rather than cantrollers
     */
    public void setAttributes(List<NodeExtendedAttribute> attributeList) {
        this.nodeExtendedAttributes = attributeList;
    }

    private List<NodeExtendedAttribute> nodeExtendedAttributes = new ArrayList<NodeExtendedAttribute>();
    private Integer maxDynamicPosition = new Integer(0);
    private String value;
    private Node node;
    private final DynamicAttribute dynamicAttribute;
    private boolean disabled;
}
