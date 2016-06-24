package com.zynap.talentstudio.workflow;

import com.zynap.exception.TalentStudioException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.StoredProcedure;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of IWorkflowBasicCommander that uses stored procedures to talk to Oracle workflow engine.
 * <p/>
 * User: jsueiras
 * Date: 05-Jul-2005
 * Time: 17:27:00
 */
public class WorkflowBasicJdbcCommander extends JdbcDaoSupport implements IWorkflowBasicCommander {


    public void startAppraisalProcess(String username, String performanceReviewId, String performanceReviewLabel, Date expiryDate,
                                      Long managerWorkflowId, Long evaluateeWorkflowId, Long userId) {
        new WorkflowStarterProcedure(getJdbcTemplate()).execute(performanceReviewId, managerWorkflowId, evaluateeWorkflowId, expiryDate, userId, performanceReviewLabel);
    }

    public void startUserManagedAppraisalProcess(String username, String performanceReviewId, String performanceReviewLabel, Date expiryDate,
                                                 Long managerWorkflowId, Long evaluateeWorkflowId, Long userId) {
        new UserManagedAppraisal(getJdbcTemplate()).execute(performanceReviewId, managerWorkflowId, evaluateeWorkflowId, expiryDate, userId, performanceReviewLabel);
    }

    public void startQuestionnaireProcess(Long workflowId, Date expiryDate, Long userId) {
        new StartWorkflowProc(getJdbcTemplate()).execute(workflowId, expiryDate, userId);
    }

    public void setNotificationActionable(Long notificationId, Long nextUserId, String actionable, String nextAction) throws TalentStudioException {
        try {
            new WorkflowSetNotificationActionable(getJdbcTemplate()).execute(notificationId, nextUserId, actionable, nextAction);
        } catch (DataAccessException e) {
            throw new WorkflowException(e.getMessage(), e);
        }
    }



    public void respondNotification(Long notificationId, String action, String responder, Long userId) throws TalentStudioException {
        try {
            new WorkflowRespondNotification(getJdbcTemplate()).execute(notificationId, action, responder, userId);
        } catch (DataAccessException e) {
            throw new WorkflowException(e.getMessage(), e);
        }
    }

    public void closeProcess(Long workflowId) throws TalentStudioException {
        new WorkflowCloseProcedure(getJdbcTemplate()).execute(workflowId);
    }

    public void completeAppraisalProcess(Long appraisalId) {
        new WorkflowCompleteProcedure(getJdbcTemplate()).execute(appraisalId);
    }

    public void reopenNotification(Long notificationId) {
        new ReopenNotificationProcedure(getJdbcTemplate()).execute(notificationId);
    }

    public void completeNotification(Long notificationId) {
        new CompleteNotificationProcedure(getJdbcTemplate()).execute(notificationId);
    }

    public void removeNotifications(Long subjectId) throws TalentStudioException {
        try {
            new WorkflowRemoveNotifications(getJdbcTemplate()).execute(subjectId);
        } catch (DataAccessException e) {
            throw new WorkflowException("Unable to remove the notifications for the given subject: " + subjectId, e);
        }
    }

    public void startSubjectReview(Long notificationId, String nextAction, Long subjectId, Long roleId) {
        new StartSubjectReviewProcedure(getJdbcTemplate()).execute(notificationId, nextAction, subjectId, roleId);
    }

    private static class StartSubjectReviewProcedure extends StoredProcedure {

        private StartSubjectReviewProcedure(JdbcTemplate template) {
            setSql("wf_integration.Start_Subject_Review");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("notification_id_", Types.INTEGER));
            declareParameter(new SqlParameter("subject_id_", Types.INTEGER));
            declareParameter(new SqlParameter("role_id_", Types.INTEGER));
            declareParameter(new SqlParameter("next_action_", Types.VARCHAR));
        }

        public void execute(Long notificationId, String nextAction, Long subjectId, Long roleId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("notification_id_", notificationId);
            in.put("subject_id_", subjectId);
            in.put("role_id_", roleId);
            in.put("next_action_", nextAction);
            execute(in);
        }
    }

    private static class WorkflowStarterProcedure extends StoredProcedure {

        public WorkflowStarterProcedure(JdbcTemplate template) {
            setSql("wf_integration.start_people_review");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("performance_id_", Types.INTEGER));
            declareParameter(new SqlParameter("manager_wf_id_", Types.INTEGER));
            declareParameter(new SqlParameter("evaluator_wf_id_", Types.INTEGER));
            declareParameter(new SqlParameter("launcher_id_", Types.INTEGER));
            declareParameter(new SqlParameter("survey_name_", Types.VARCHAR));
            declareParameter(new SqlParameter("expire_date_", Types.DATE));
        }

        public void execute(String performanceReviewId, Long managerWorkflowId, Long evaluateeWorkflowId, Date expiryDate, Long userId, String workflowName) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("performance_id_", performanceReviewId);
            in.put("manager_wf_id_", managerWorkflowId);
            in.put("evaluator_wf_id_", evaluateeWorkflowId);
            in.put("launcher_id_", userId);
            in.put("survey_name_", workflowName);
            in.put("expire_date_", expiryDate);
            execute(in);
        }
    }

    private static class UserManagedAppraisal extends StoredProcedure {

        public UserManagedAppraisal(JdbcTemplate template) {
            setSql("wf_integration.start_user_managed_review");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("performance_id_", Types.INTEGER));
            declareParameter(new SqlParameter("manager_wf_id_", Types.INTEGER));
            declareParameter(new SqlParameter("evaluator_wf_id_", Types.INTEGER));
            declareParameter(new SqlParameter("launcher_id_", Types.INTEGER));
            declareParameter(new SqlParameter("survey_name_", Types.VARCHAR));
            declareParameter(new SqlParameter("expire_date_", Types.DATE));
        }

        public void execute(String performanceReviewId, Long managerWorkflowId, Long evaluateeWorkflowId, Date expiryDate, Long userId, String workflowName) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("performance_id_", performanceReviewId);
            in.put("manager_wf_id_", managerWorkflowId);
            in.put("evaluator_wf_id_", evaluateeWorkflowId);
            in.put("launcher_id_", userId);
            in.put("survey_name_", workflowName);
            in.put("expire_date_", expiryDate);
            execute(in);
        }
    }

    private static class StartWorkflowProc extends StoredProcedure {

        public StartWorkflowProc(JdbcTemplate template) {
            setSql("wf_integration.start_questionnaire");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("wf_id_", Types.INTEGER));
            declareParameter(new SqlParameter("launcher_id_", Types.INTEGER));
            declareParameter(new SqlParameter("expire_date_", Types.DATE));
        }

        public void execute(Long workflowId, Date expiryDate, Long userId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("wf_id_", workflowId);
            in.put("launcher_id_", userId);
            in.put("expire_date_", expiryDate);
            execute(in);
        }
    }

    private static class WorkflowSetNotificationActionable extends StoredProcedure {

        public WorkflowSetNotificationActionable(JdbcTemplate template) {
            setSql("wf_integration.set_actionable");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("nid_", Types.INTEGER));
            declareParameter(new SqlParameter("next_user_id_", Types.INTEGER));
            declareParameter(new SqlParameter("actionable_", Types.VARCHAR));
            declareParameter(new SqlParameter("next_action_", Types.VARCHAR));
        }

        public void execute(Long notificationId, String actionable, String nextAction) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("nid_", notificationId);
            in.put("actionable_", actionable);
            in.put("next_action_", nextAction);
            execute(in);
        }
    }

    private static class WorkflowRespondNotification extends StoredProcedure {

        public WorkflowRespondNotification(JdbcTemplate template) {
            setSql("wf_integration.respond_notification");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("nid", Types.INTEGER));
            declareParameter(new SqlParameter("next_action_", Types.VARCHAR));
            declareParameter(new SqlParameter("responder", Types.VARCHAR));
            declareParameter(new SqlParameter("user_id_", Types.INTEGER));
        }

        public void execute(Long notificationId, String action, String responder, Long userId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("nid", notificationId);
            in.put("next_action_", action);
            in.put("responder", responder);
            in.put("user_id_", userId);
            execute(in);
        }

    }

    private static class WorkflowRemoveNotifications extends StoredProcedure {

        public WorkflowRemoveNotifications(JdbcTemplate template) {
            setSql("wf_integration.remove_notification");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("subject_id_", Types.INTEGER));
        }

        public void execute(Long subjectId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("subject_id_", subjectId);
            execute(in);
        }
    }

    private static class ReopenNotificationProcedure extends StoredProcedure {

        public ReopenNotificationProcedure(JdbcTemplate template) {
            setSql("wf_integration.reopen_notification");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("notif_id_", Types.INTEGER));
        }

        public void execute(Long notificaitonId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("notif_id_", notificaitonId);
            execute(in);
        }
    }

    private static class CompleteNotificationProcedure extends StoredProcedure {

        public CompleteNotificationProcedure(JdbcTemplate template) {
            setSql("wf_integration.complete_notification");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("notif_id_", Types.INTEGER));
        }

        public void execute(Long notificaitonId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("notif_id_", notificaitonId);
            execute(in);
        }
    }

    private static class WorkflowCloseProcedure extends StoredProcedure {

        public WorkflowCloseProcedure(JdbcTemplate template) {
            setSql("wf_integration.closeProcess");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("workflow_id_", Types.INTEGER));
        }

        public void execute(Long workflowId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("workflow_id_", workflowId);
            execute(in);
        }
    }

    private static class WorkflowCompleteProcedure extends StoredProcedure {

        public WorkflowCompleteProcedure(JdbcTemplate template) {
            setSql("wf_integration.completeProcess");
            setJdbcTemplate(template);
            declareParameter(new SqlParameter("performance_id_", Types.INTEGER));
        }

        public void execute(Long appraisalId) {
            Map<String, Object> in = new HashMap<String, Object>();
            in.put("performance_id_", appraisalId);
            execute(in);
        }
    }
}
