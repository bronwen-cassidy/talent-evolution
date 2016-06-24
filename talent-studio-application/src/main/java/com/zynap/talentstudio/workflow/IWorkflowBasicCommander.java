package com.zynap.talentstudio.workflow;

import com.zynap.exception.TalentStudioException;

import java.util.Date;

/**
 * Interface for components that talk to workflow engines. 
 *
 * User: jsueiras
 * Date: 05-Jul-2005
 * Time: 17:26:33
 */
public interface IWorkflowBasicCommander {

    void startAppraisalProcess(String username, String performanceReviewId, String performanceReviewLabel, Date expiryDate, Long managerWorkflowId, Long evaluateeWorkflowId, Long userId);

    void startQuestionnaireProcess(Long workflowId, Date expiryDate, Long userId);

    void setNotificationActionable(Long notificationId, Long nextUserId, String actionable, String nextAction) throws TalentStudioException;

    void respondNotification(Long notificationId, String action, String responder, Long userId) throws TalentStudioException;

    void closeProcess(Long workflowId) throws TalentStudioException;

    /**
     * Removes notifications for a subject.
     *
     * @param subjectId the id of the subject.
     */
    void removeNotifications(Long subjectId) throws TalentStudioException;

    void startSubjectReview(Long notificationId, String nextAction, Long subjectId, Long roleId);

    void startUserManagedAppraisalProcess(String username, String performanceReviewId, String performanceReviewLabel, Date expiryDate, Long managerWorkflowId, Long evaluateeWorkflowId, Long userId);

    void completeAppraisalProcess(Long appraisalId);

    void reopenNotification(Long notificationId);

    void completeNotification(Long notificationId);
}
