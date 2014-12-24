/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.messages;

import com.zynap.talentstudio.messages.IMessageService;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 17-Sep-2007 10:31:15
 */
public class MessageItemDwrBean implements Serializable {

    public void setMessageItemRead(Long messageItemId) {
        try {
            messageService.markAsRead(messageItemId);
        } catch (TalentStudioException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public String getUnreadMessageCount(Long userId) {
        try {
            Integer numItems = messageService.countUnreadMessages(userId);
            if(numItems == null) numItems = new Integer(0);
            return " ( " + numItems.toString() + " ) ";
        } catch (TalentStudioException e) {
            return " ( 0 ) ";            
        }
    }

    public String getAppraisalCount(Long userId) {
        try {
            Integer numItems = questionnaireService.countNumAppraisals(userId);
            if(numItems == null) numItems = new Integer(0);
            return " ( " + numItems.toString() + " ) ";
        } catch (TalentStudioException e) {
            return " ( 0 ) ";
        }
    }

    public String getQuestionnaireCount(Long userId) {
        try {
            Integer numItems = questionnaireService.countNumQuestionnaires(userId);
            if(numItems == null) numItems = new Integer(0);
            return " ( " + numItems.toString() + " ) ";
        } catch (TalentStudioException e) {
            return " ( 0 ) ";
        }
    }

    public String getAssessmentCount(Long userId) {
        try {
            Integer numItems = objectiveService.countNumAssessments(userId);
            if(numItems == null) numItems = new Integer(0);
            return " ( " + numItems.toString() + " ) ";
        } catch (TalentStudioException e) {
            return " ( 0 ) ";
        }
    }

    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    private IMessageService messageService;
    private IQuestionnaireService questionnaireService;
    private IObjectiveService objectiveService;
    private static final Log logger = LogFactory.getLog(MessageItemDwrBean.class);
}
