/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.messages;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;

import java.util.Date;

/**
 * Class representing a users inbox where any items sent by subordinates to superiors or visa versa are stored
 *
 * @author bcassidy
 * @version 0.1
 * @since 13-Sep-2007 12:22:07
 */
public class MessageItem extends ZynapDomainObject {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public Long getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(Long objectiveId) {
        this.objectiveId = objectiveId;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public boolean isIndividualView() {
        return INDIVIDUAL_VIEW.equals(viewType);
    }

    /* whether the item is read or unread constants used to define the status. */      
    private String status = STATUS_UNREAD;
    private Long toUserId;
    private User fromUser;
    private Long questionnaireId;
    private Long objectiveId;
    private Date dateReceived;
    private String type;
    /* the manager or individuals view */   
    private String viewType;
    
    public static final String STATUS_UNREAD = "UNREAD";
    public static final String STATUS_READ = "READ";
    public static final String TYPE_QUESTIONNAIRE = "questionnaire";
    public static final String INDIVIDUAL_VIEW = "individual";
    public static final String MANAGER_VIEW = "manager";
}
