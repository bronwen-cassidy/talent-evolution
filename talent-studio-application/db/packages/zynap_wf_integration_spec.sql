CREATE OR REPLACE PACKAGE WF_INTEGRATION AS

  ----------------------------------------------------------------------------------------------------
  -- starts the performance review process
  ----------------------------------------------------------------------------------------------------
  PROCEDURE start_people_review(
    performance_id_      IN NUMBER,
    manager_qwf_instance IN NUMBER,
    evaluator_wf_id_     IN NUMBER,
    launcher_id          IN NUMBER,
    survey_name_         IN VARCHAR2,
    expire_date          IN DATE
  );

  ----------------------------------------------------------------------------------------------------
  -- starts the performance review process where the launcher manages the process
  ----------------------------------------------------------------------------------------------------
  PROCEDURE start_user_managed_review(
    performance_id_      IN NUMBER,
    manager_qwf_instance IN NUMBER,
    evaluator_wf_id_     IN NUMBER,
    launcher_id          IN NUMBER,
    survey_name_         IN VARCHAR2,
    expire_date          IN DATE
  );

  ----------------------------------------------------------------------------------------------------
  -- starts the questionnaire process
  ----------------------------------------------------------------------------------------------------
  PROCEDURE start_questionnaire(
    qwf_instance IN NUMBER,
    launcher_id  IN NUMBER,
    expire_date  IN DATE
  );

  ----------------------------------------------------------------------------------------------------
  -- starts the performance review for evaluators
  ----------------------------------------------------------------------------------------------------
  PROCEDURE Start_Subject_Review(
    notification_id_ IN NUMBER,
    subject_id_      IN NUMBER,
    role_id_         IN NUMBER,
    next_action_     IN VARCHAR2
  );

  ----------------------------------------------------------------------------------------------------
  -- sets the next action to be performed and the current state
  -- enables the notification to be acted upon
  ----------------------------------------------------------------------------------------------------
  PROCEDURE set_actionable(
    notification_id IN NUMBER,
    actionable_     IN VARCHAR2,
    next_action_    IN VARCHAR2
  );

  ----------------------------------------------------------------------------------------------------
  -- Sets the next state, for appraisals, questionnaires
  ----------------------------------------------------------------------------------------------------
  PROCEDURE respond_notification(
    notification_id_ IN NUMBER,
    next_action_     IN VARCHAR2
  );

  ------------------------------------------------------------------------------------------------------
  -- Removes all notifications for a subject when they are being deleted from the system
  ------------------------------------------------------------------------------------------------------
  PROCEDURE remove_notification(
    subject_id_ IN NUMBER
  );
  ------------------------------------------------------------------------------------------------------
  -- approves the given subjects appraisal and removes the hr notification
  ------------------------------------------------------------------------------------------------------
  PROCEDURE approve_notification(
      subject_id_     IN NUMBER
    , hr_id_          IN NUMBER
    , performance_id_ IN NUMBER
  );
  ------------------------------------------------------------------------------------------------------
  -- verifies the given subjects appraisal and removes the manager's manager's notification
  ------------------------------------------------------------------------------------------------------
  PROCEDURE verify_notification(
      subject_id_          IN NUMBER
    , managers_manager_id_ IN NUMBER
    , performance_id_      IN NUMBER
  );
  ------------------------------------------------------------------------------------------------------
  -- Create a notification with the given root notification, for recipient, with the target
  -- as the subjetc_id with a mnager of manager_id.
  ------------------------------------------------------------------------------------------------------
  PROCEDURE create_notification(
      root_notification_id_ IN NUMBER
    , next_action_          IN VARCHAR2
    , subject_id_           IN NUMBER
    , manager_id_           IN NUMBER
    , recipient_id_         IN NUMBER
  );


  PROCEDURE reopen_notification(
    notif_id_ IN NUMBER
  );

  PROCEDURE complete_notification(
    notif_id_ IN NUMBER
  );

  PROCEDURE reopen_subject_notification(
      subject_id_     IN NUMBER
    , perf_review_id_ IN NUMBER
  );

  -------------------------------------------------------------------------------------------------------
  -- removes a workflow process and all of it's dependent processes
  -------------------------------------------------------------------------------------------------------
  PROCEDURE closeProcess(
    workflow_id_ IN NUMBER
  );

  PROCEDURE completeProcess(
    performance_id_ IN NUMBER
  );

END WF_INTEGRATION;

/
