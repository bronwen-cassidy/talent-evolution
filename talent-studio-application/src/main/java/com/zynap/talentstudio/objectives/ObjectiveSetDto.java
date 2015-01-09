/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.objectives;

import java.util.Date;
import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Aug-2008 10:13:46
 */
public class ObjectiveSetDto implements Serializable {

    public ObjectiveSetDto(String status, Date lastModifiedDate, String actionRequired, String actionGroup, String type, boolean approved, Long subjectId, Long id) {
        this.status = status;
        this.lastModifiedDate = lastModifiedDate;
        this.actionRequired = actionRequired;
        this.actionGroup = actionGroup;
        this.type = type;
        this.approved = approved;
        this.subjectId = subjectId;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getActionRequired() {
        return actionRequired;
    }

    public String getActionGroup() {
        return actionGroup;
    }

    public String getType() {
        return type;
    }

    public boolean isApproved() {
        return approved;
    }

    public boolean isComplete() {
        return ObjectiveConstants.STATUS_COMPLETE.equals(status);
    }

    public boolean isOpen() {
        return ObjectiveConstants.STATUS_OPEN.equals(status);
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public boolean isIndividualPending() {
        return isPending() && ObjectiveConstants.ACTION_GROUP_INDIVIDUAL.equals(actionGroup);
    }

    public boolean isPending() {
        return ObjectiveConstants.STATUS_PENDING.equals(status);
    }

    public boolean isManagerPending() {
        return isPending() && ObjectiveConstants.ACTION_GROUP_MANAGER.equals(actionGroup);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    private String status;
    private Date lastModifiedDate;
    private String actionRequired;
    private String actionGroup;
    private String type;
    private boolean approved;
    private Long subjectId;
    private Long id;
}
