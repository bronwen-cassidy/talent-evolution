CREATE OR REPLACE package WF_INTEGRATION as

----------------------------------------------------------------------------------------------------
-- starts the performance review process
----------------------------------------------------------------------------------------------------
procedure start_people_review (
              performance_id_ in number,
              manager_qwf_instance in number,
              evaluator_wf_id_ in number,
              launcher_id in number,
              survey_name_ in varchar2,
              expire_date in date
);

----------------------------------------------------------------------------------------------------
-- starts the performance review process where the launcher manages the process
----------------------------------------------------------------------------------------------------
procedure start_user_managed_review (
              performance_id_ in number,
              manager_qwf_instance in number,
              evaluator_wf_id_ in number,
              launcher_id in number,
              survey_name_ in varchar2,
              expire_date in date
);

----------------------------------------------------------------------------------------------------
-- starts the questionnaire process
----------------------------------------------------------------------------------------------------
procedure start_questionnaire (
              qwf_instance in number,
              launcher_id in number,
              expire_date in date
);

----------------------------------------------------------------------------------------------------
-- starts the performance review for evaluators
----------------------------------------------------------------------------------------------------
procedure Start_Subject_Review (
            notification_id_ in number,
            subject_id_ in number,
            role_id_ in number,
            next_action_ in varchar2
);

----------------------------------------------------------------------------------------------------
-- sets the next action to be performed and the current state
-- enables the notification to be acted upon
----------------------------------------------------------------------------------------------------
procedure set_actionable (
              notification_id in number,
              next_user_id_ in number,
              actionable_ in varchar2,
              next_action_ in varchar2
);

----------------------------------------------------------------------------------------------------
-- Sets the next state, for appraisals, questionnaires
----------------------------------------------------------------------------------------------------
procedure respond_notification (
              notification_id_ in number,
              next_action_ in varchar2,
              responder_user_name_ in varchar2,
              responder_id_ in number
);

------------------------------------------------------------------------------------------------------
-- Removes all notifications for a subject when they are being deleted from the system
------------------------------------------------------------------------------------------------------
procedure remove_notification (
    subject_id_ in number
);


procedure reopen_notification (
    notif_id_ in number
);

procedure complete_notification (
    notif_id_ in number
);

procedure reopen_subject_notification (
    subject_id_ in number
    ,perf_review_id_ in number
);

-------------------------------------------------------------------------------------------------------
-- removes a workflow process and all of it's dependent processes
-------------------------------------------------------------------------------------------------------
procedure closeProcess (
    workflow_id_ in number
);

procedure completeProcess (
    performance_id_ in number
);

end WF_INTEGRATION;

/
