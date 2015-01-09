/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.audit;

import java.io.Serializable;
import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Jan-2009 11:34:42
 */
public class Audit implements Serializable {

    public Audit() {
    }

    public Audit(Long objectId, String tableName, String actionPerformed, String description) {
        this.objectId = objectId;
        this.tableName = tableName;
        this.actionPerformed = actionPerformed;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Long modifiedById) {
        this.modifiedById = modifiedById;
    }

    public String getModifiedByUsername() {
        return modifiedByUsername;
    }

    public void setModifiedByUsername(String modifiedByUsername) {
        this.modifiedByUsername = modifiedByUsername;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuditObject getSerializedObject() {
        return serializedObject;
    }

    public void setSerializedObject(AuditObject serializedObject) {
        this.serializedObject = serializedObject;
    }

    private Long id;
    private Long modifiedById;
    private String modifiedByUsername;
    private Long objectId;
    private Date modifiedDate;
    private String tableName;
    private String actionPerformed;
    private String description;
    private AuditObject serializedObject;
}
