package com.zynap.talentstudio.workflow;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.domain.admin.User;

import java.util.List;

/**
 * Adapter / bridge that wraps access to workflow engine.
 *
 * User: jsueiras
 * Date: 05-Jul-2005
 * Time: 15:20:04
 */
public interface IWorkflowAdapter {

    /**
     * Removes a workflow process of the given type with the given id.
     *
     * @param workflowId
     */
    void closeProcess(Long workflowId) throws TalentStudioException;

    /**
     * Removes the notifications for the given subject.
     *
     * @param subjectId the subject's id
     */
    void removeNotifications(Long subjectId) throws TalentStudioException;

    /**
     * Respond to notification.
     * @param notification
     * @param action
     * @param userId @throws TalentStudioException
     * @param subjectId
     * @param roleId
     */
    void processNotification(Notification notification, String action, Long userId, Long subjectId, Long roleId) throws TalentStudioException;

    /**
     * Case where an HR user has been selected or the manager has requested verification from his manager.
     *
     * @param notification - the root notification
     * @param nextAction - APPROVAL or VERIFY
     * @param managerId - the id of the manager requesting approval or verification
     * @param subjectId - the person being reviewed
     * @param recipientId - the manager's manager or the hr the notification is being created for
     * @throws TalentStudioException
     */
    void processApprovalNotification(Notification notification, String nextAction, Long managerId, Long subjectId, Long recipientId) throws TalentStudioException;

    void completeAppraisalProcess(Long appraisalId);

    /**
     * Get notifications.
     *
     * @param performanceReview
     * @return List of {@link Notification} objects.
     * @throws TalentStudioException
     */
    List<Notification> getNotifications(Long userId, boolean performanceReview) throws TalentStudioException;

    /**
     * Set status of notification to actionable or not.
     *
     * @param notificationId
     * @param actionable
     * @param nextAction
     * @throws TalentStudioException
     */
    void setNotificationActionable(Long notificationId, boolean actionable, String nextAction) throws TalentStudioException;

    void start(PerformanceReview performanceReview, User user);

    void start(QuestionnaireWorkflow questionnaireWorkflow, Long userId);

    /**
     * Starts a review process that is completely managed by the given user
     * @param performanceReview the performance review process
     * @param user the user who will manage the process
     */
    void startUserManagedReview(PerformanceReview performanceReview, User user);

    void reopenNotification(Long notificationId);

    void completeNotification(Long notificationId);

    void approveNotification(Long subjectId, Long hrId, Long performanceId);

    void verifyNotification(Long subjectId, Long managersManagerId, Long performanceId);

    /**
     * Constants for workflow parameters.
     */
    String START_DATE_ATTRIBUTE_NAME = "START_DATE";
    String EXPIRE_DATE_ATTRIBUTE_NAME = "EXPIRE_DATE";
    String LAUNCHER_ATTRIBUTE_NAME = "LAUNCHER";
    String DETAIL_PROCESS_TYPE_ATTRIBUTE_NAME = "DETAIL_PROCESS_TYPE";
    String WORK_FLOW_ID_ATTRIBUTE_NAME = "WORKFLOW_ID";
    String WORK_FLOW_NAME_ATTRIBUTE_NAME = "WORKFLOW_NAME";
    String MANAGER_QUE_ID_ATTRIBUTE_NAME = "MANAGER_INSTANCE_ID";
    String EVALUATORS_QUE_ID_ATTRIBUTE_NAME = "EVALUATORS_INSTANCE_ID";

    /**
     * Constant for internal name of questionnaire workflow engine item.
     */
    String QUESTIONNAIRE_WORKFLOW_ITEM_TYPE = "QUESTION";

    /**
     * Constant for internal name of master questionnaire process in questionnaire workflow engine definition.
     */
    String MASTER_QUESTIONNAIRE_PROCESS = "QUESTIONNARE_MASTER";

    /**
     * Constant for internal name of questionnaire process in questionnaire workflow engine definition.
     */
    String QUESTIONNAIRE_PROCESS = "QUESTIONNARE_GENERAL";

    /**
     * Constant for internal name of master appraisal process in questionnaire workflow engine definition.
     */
    String MASTER_APPRAISAL_PROCESS = "PERFORMANCE_MASTER";

    /**
     * Constant for internal name of appraisal process in questionnaire workflow engine definition.
     */
    String APPRAISAL_PROCESS = "PERSON_PERFORM_REVIEW";
}
