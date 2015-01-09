package com.zynap.talentstudio.organisation;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Hibernate CodeGenerator
 */
public class NodeAudit implements Serializable {

    /**
     * full constructor
     */
    public NodeAudit(Date date, Node node, Long userId) {
        this.nodeId = node.getId();
        this.node = node;
        this.createdById = userId;
        this.created = date;
    }

    /**
     * default constructor
     */
    public NodeAudit() {
    }


    public Long getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Node getNode() {
        return this.node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long updatedById) {
        this.updatedById = updatedById;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("nodeId", getNodeId())
                .append("updated", getUpdated())
                .append("created", getCreated())
                .toString();
    }

    /**
     * identifier field
     */
    private Long nodeId;

    /**
     * nullable persistent field
     */
    private Date updated;

    /**
     * nullable persistent field
     */
    private Date created;

    /**
     * nullable persistent field
     */
    private Node node;

    private Long createdById;
    private Long updatedById;
}
