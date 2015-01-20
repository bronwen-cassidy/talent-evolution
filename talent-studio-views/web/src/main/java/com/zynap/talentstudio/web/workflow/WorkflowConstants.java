package com.zynap.talentstudio.web.workflow;

import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;

/**
 * User: amark
 * Date: 21-Aug-2006
 * Time: 11:00:38
 */
public interface WorkflowConstants {

    String MANAGER_PARAM = "SEND_TO_MANAGER_";

    String NOTIFICATION_ID_PARAM = "nid";
    String WORKFLOW_TYPE_PARAM = "workflow_type";
    String NOTIFICATION_STATUS_PARAM = "notification_status";
    String SELF_EVALUATION_PARAM = "SEND_TO_EVALUATEE_";

    String SUBJECT_ID_PARAM_PREFIX = "SUBJECT_ID";
    String WORKFLOW_ID_PARAM_PREFIX = "WORKFLOW_ID";
    String ROLE_PARAM_PREFIX = "ROLE";

    String NOTIFICATION_STATUS_OPEN = QuestionnaireWorkflow.STATUS_OPEN;

    /**
     * Constants for the worklfow constants
     */
    final String START_ASSESMENTS = "START";
    String APPRAISAL_ID = "APP_ID";
}
