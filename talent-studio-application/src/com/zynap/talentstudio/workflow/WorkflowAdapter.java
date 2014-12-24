package com.zynap.talentstudio.workflow;

import com.zynap.common.util.StringUtil;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.domain.admin.User;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import java.util.Date;
import java.util.List;

/**
 * Implementation of IWorkflowAdapter for Oracle workflow engine.
 * <p/>
 * User: jsueiras
 * Date: 05-Jul-2005
 * Time: 15:32:41
 */
public class WorkflowAdapter extends HibernateDaoSupport implements IWorkflowAdapter {

    public void start(PerformanceReview performanceReview, User user) {

        final String username = user.getLoginInfo().getUsername();
        final String performanceReviewId = performanceReview.getId().toString();
        final String performanceReviewLabel = performanceReview.getLabel();
        final Long userId = user.getId();

        QuestionnaireWorkflow managerQuestionnaire = performanceReview.getManagerWorkflow();
        final Date expiryDate = managerQuestionnaire.getExpiryDate();
        QuestionnaireWorkflow evaluatorsQuestionnaire = performanceReview.getEvaluatorWorkflow();
        Long managerWorkflowId = managerQuestionnaire.getId();
        Long evaluateeWorkflowId = evaluatorsQuestionnaire.getId();
        workflowBasicCommander.startAppraisalProcess(username, performanceReviewId, performanceReviewLabel, expiryDate,
                managerWorkflowId, evaluateeWorkflowId, userId);
    }

    public void startUserManagedReview(PerformanceReview performanceReview, User user) {
        final String username = user.getLoginInfo().getUsername();
        final String performanceReviewId = performanceReview.getId().toString();
        final String performanceReviewLabel = performanceReview.getLabel();
        final Long userId = user.getId();

        QuestionnaireWorkflow managerQuestionnaire = performanceReview.getManagerWorkflow();
        final Date expiryDate = managerQuestionnaire.getExpiryDate();
        QuestionnaireWorkflow evaluatorsQuestionnaire = performanceReview.getEvaluatorWorkflow();
        Long managerWorkflowId = managerQuestionnaire.getId();
        Long evaluateeWorkflowId = evaluatorsQuestionnaire.getId();
        workflowBasicCommander.startUserManagedAppraisalProcess(username, performanceReviewId, performanceReviewLabel, expiryDate,
                managerWorkflowId, evaluateeWorkflowId, userId);
    }

    public void reopenNotification(Long notificationId) {
        workflowBasicCommander.reopenNotification(notificationId);
    }

    public void completeNotification(Long notificationId) {
        workflowBasicCommander.completeNotification(notificationId);
    }

    public void start(QuestionnaireWorkflow questionnaireWorkflow, Long userId) {
        final Date expiryDate = questionnaireWorkflow.getExpiryDate();
        final Long workflowId = questionnaireWorkflow.getId();
        workflowBasicCommander.startQuestionnaireProcess(workflowId, expiryDate, userId);
    }

    public void closeProcess(Long workflowId) throws TalentStudioException {
        workflowBasicCommander.closeProcess(workflowId);
    }

    public void removeNotifications(Long subjectId) throws TalentStudioException {
        workflowBasicCommander.removeNotifications(subjectId);
    }

    public void processNotification(Notification notification, String action, String userName, Long userId, Long subjectId, Long roleId) throws TalentStudioException {
        // nothing to do if the notification is null
        if(notification == null) return;

        String nextAction = Notification.getNextAction(action, notification.getSubType());
        workflowBasicCommander.respondNotification(notification.getId(), nextAction, userName, userId);
        if (notification.isPerformanceReviewType() && Notification.ANSWER.equals(nextAction)) {
            workflowBasicCommander.startSubjectReview(notification.getId(), nextAction, subjectId, roleId);
        }
    }

    public void completeAppraisalProcess(Long appraisalId) {
        workflowBasicCommander.completeAppraisalProcess(appraisalId);
    }

    public List getNotifications(Long userId, boolean performanceReview) throws TalentStudioException {
        Object[] params = new Object[1];
        String[] paramNames = new String[1];
        params[0] = userId;
        paramNames[0] = "userId";

        String query = "select n from Notification n where n.recipientId = :userId";
        query += " and n.status <> 'COMPLETED'";
        query += " and " + (performanceReview ? "" : "not")
                + " exists (select pr.id from PerformanceReview pr," +
                "QuestionnaireWorkflow qw where " +
                "n.workflowId = pr.id or n.workflowId = qw.id and qw.performanceReview.id = pr.id  )";
        
        return getHibernateTemplate().findByNamedParam(query, paramNames, params);
    }

    public void setNotificationActionable(Long notificationId, boolean actionable, String nextAction) throws TalentStudioException {
        workflowBasicCommander.setNotificationActionable(notificationId, StringUtil.convertToString(actionable), nextAction);
    }

    public void setWorkflowBasicCommander(IWorkflowBasicCommander workflowBasicCommander) {
        this.workflowBasicCommander = workflowBasicCommander;
    }

    private IWorkflowBasicCommander workflowBasicCommander;
}
