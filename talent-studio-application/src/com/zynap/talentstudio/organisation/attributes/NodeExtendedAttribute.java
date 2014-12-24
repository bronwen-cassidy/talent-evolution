/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.attributes;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.Auditable;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.Node;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class NodeExtendedAttribute extends ZynapDomainObject {

    public NodeExtendedAttribute() {
    }

    public NodeExtendedAttribute(String value, Node node, DynamicAttribute dynamicAttribute) {
        this.value = value;
        this.node = node;
        this.dynamicAttribute = dynamicAttribute;
    }

    public NodeExtendedAttribute(Long id, String value, Node node, DynamicAttribute dynamicAttribute) {
        this(value, node, dynamicAttribute);
        this.id = id;
    }

    public NodeExtendedAttribute(String value, DynamicAttribute dynamicAttribute) {
        this(value, null, dynamicAttribute);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DynamicAttribute getDynamicAttribute() {
        return dynamicAttribute;
    }

    public void setDynamicAttribute(DynamicAttribute dynamicAttribute) {
        this.dynamicAttribute = dynamicAttribute;
    }

    public Node getNode() {
        return this.node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Integer getDynamicPosition() {
        return dynamicPosition;
    }

    public void setDynamicPosition(Integer dynamicPosition) {
        this.dynamicPosition = dynamicPosition;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("value", getValue())
                .toString();
    }

    public void copyValues(NodeExtendedAttribute newNodeExtendedAttribute) {
        this.value = newNodeExtendedAttribute.getValue();
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public NodeExtendedAttribute createAuditable() {
        final DynamicAttribute da = dynamicAttribute.createAuditable();
        return new NodeExtendedAttribute(id, value, null, da);
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public Integer getMaxDynamicPosition() {
        return maxDynamicPosition;
    }

    public void setMaxDynamicPosition(Integer maxDynamicPosition) {
        this.maxDynamicPosition = maxDynamicPosition;
    }

    private String value;
    private Node node;
    private DynamicAttribute dynamicAttribute;
    private Integer dynamicPosition;
    private User addedBy;
    private Date dateAdded;
    private boolean disabled;
    private Long lineItemId;
    private Integer maxDynamicPosition;
}
