CREATE OR REPLACE PACKAGE BODY WF_INTEGRATION AS

  -------------------------------------------------------------------------------------------------------
  --Name: Start_People_Review
  --
  --Description:
  -- Starts 360 PERSON_PERFORM_REVIEW process for each subject being evaluated
  -- Finds subjects to evaluate by finding active subjects who have managers who are also active
  -- Managers then recieve a notification for each for subject involved in the appraisal process
  -------------------------------------------------------------------------------------------------------
  PROCEDURE start_people_review(
    performance_id_      IN NUMBER,
    manager_qwf_instance IN NUMBER,
    evaluator_wf_id_     IN NUMBER,
    launcher_id          IN NUMBER,
    survey_name_         IN VARCHAR2,
    expire_date          IN DATE) IS
    BEGIN
      DECLARE

        child_key    NUMBER;
        full_name    VARCHAR2(500);
        num_records_ NUMBER;

        CURSOR surveylist IS
          SELECT *
          FROM survey_list
          WHERE que_wf_id = manager_qwf_instance;
      BEGIN

        FOR user_rec IN surveylist LOOP

          SELECT wf_sq.nextval
          INTO child_key
          FROM dual;
          full_name := user_rec.first_name || ' ' || user_rec.second_name;

          -- check to see if the row we are inserting has a manager for the subject already assigned
          SELECT count(*)
          INTO num_records_
          FROM notifications
          WHERE subject_id = user_rec.subject_id
                AND performance_review_id = performance_id_
                AND role_name = 'Manager'
                AND action = 'ASSIGN_ROLES'
                AND WORKFLOW_ID = manager_qwf_instance;

          IF num_records_ < 1
          THEN
            INSERT INTO NOTIFICATIONS
            (ID, MANAGER_ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME,
             WORKFLOW_ID, MANAGER_INSTANCE_ID, PERFORMANCE_REVIEW_ID,
             EVALUATORS_INSTANCE_ID, TYPE, SUB_TYPE, STATUS, BEGIN_DATE, DUE_DATE,
             ACTION, LAUNCHER_ID, WORKFLOW_NAME, ROLE_NAME)
            VALUES
              (child_key, user_rec.manager_id, user_rec.manager_id, user_rec.subject_id, full_name,
                          manager_qwf_instance, manager_qwf_instance, performance_id_,
                          evaluator_wf_id_, 'APPRAISAL', 'MANAGERASSESMENT', 'OPEN', sysdate, expire_date,
               'ASSIGN_ROLES', launcher_id, survey_name_, 'Manager');
          END IF;

        END LOOP;

        UPDATE performance_reviews
        SET status = 'PENDING'
        WHERE id = performance_id_;
        UPDATE que_workflows
        SET status = 'PENDING'
        WHERE performance_id = performance_id_;
        UPDATE que_workflows
        SET start_date = sysdate
        WHERE performance_id = performance_id_;

      END;

    END start_people_review;

  -------------------------------------------------------------------------------------------------------
  --Name: start_user_managed_review
  --
  --Description:
  -- Starts performance review process for each subject being evaluated by creating notifications
  -- for each evaluatee, each of these notifications though are sent to the person who has launched the process
  -- as this person is to assign roles and start the review and not the managers of these people.
  -------------------------------------------------------------------------------------------------------
  PROCEDURE start_user_managed_review(
    performance_id_      IN NUMBER,
    manager_qwf_instance IN NUMBER,
    evaluator_wf_id_     IN NUMBER,
    launcher_id          IN NUMBER,
    survey_name_         IN VARCHAR2,
    expire_date          IN DATE) IS
    BEGIN
      DECLARE

        child_key NUMBER;
        full_name VARCHAR2(1000);

        CURSOR surveylist IS
          SELECT
            s.node_id AS subject_id,
            cd.first_name,
            cd.second_name,
            qwp.que_wf_id
          FROM subjects s, que_wf_participants qwp, core_details cd
          WHERE s.node_id = qwp.subject_id
                AND s.cd_id = cd.id
                AND qwp.que_wf_id = manager_qwf_instance;
      BEGIN

        FOR user_rec IN surveylist LOOP

          SELECT wf_sq.nextval
          INTO child_key
          FROM dual;

          full_name := user_rec.first_name || ' ' || user_rec.second_name;

          INSERT INTO NOTIFICATIONS
          (ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME,
           WORKFLOW_ID, MANAGER_INSTANCE_ID, PERFORMANCE_REVIEW_ID,
           EVALUATORS_INSTANCE_ID, TYPE, SUB_TYPE, STATUS, BEGIN_DATE, DUE_DATE,
           ACTION, LAUNCHER_ID, WORKFLOW_NAME, ROLE_NAME, USER_MANAGED)
          VALUES
            (child_key, launcher_id, user_rec.subject_id, full_name,
                        manager_qwf_instance, manager_qwf_instance, performance_id_,
                        evaluator_wf_id_, 'APPRAISAL', 'MANAGERASSESMENT', 'OPEN', sysdate, expire_date,
             'ASSIGN_ROLES', launcher_id, survey_name_, 'Administrator', 'T');

        END LOOP;

        UPDATE performance_reviews
        SET status = 'PENDING'
        WHERE id = performance_id_;
        UPDATE que_workflows
        SET status = 'PENDING'
        WHERE performance_id = performance_id_;
        UPDATE que_workflows
        SET start_date = sysdate
        WHERE performance_id = performance_id_;

      END;

    END start_user_managed_review;

  -------------------------------------------------------------------------------------------------------
  --Name: start_questionnaire
  --
  -- Description:
  -- Starts the questionnaire process
  -------------------------------------------------------------------------------------------------------
  PROCEDURE start_questionnaire(
    qwf_instance IN NUMBER,
    launcher_id  IN NUMBER,
    expire_date  IN DATE)
  IS
    BEGIN
      DECLARE

        survey_name VARCHAR2(80);
        child_key   NUMBER;
        full_name   VARCHAR2(500);

        CURSOR surveylist IS
          SELECT
            p.subject_id,
            s.user_id,
            cd.first_name,
            cd.second_name
          FROM que_wf_participants p, subjects s, core_details cd
          WHERE p.subject_id = s.node_id
                AND s.cd_id = cd.id
                AND p.que_wf_id = qwf_instance;
      BEGIN

        BEGIN
          SELECT label
          INTO survey_name
          FROM que_workflows
          WHERE id = qwf_instance;
          EXCEPTION
          WHEN NO_DATA_FOUND THEN
          NULL;

        END;

        FOR user_rec IN surveylist LOOP
          BEGIN
            SELECT wf_sq.nextval
            INTO child_key
            FROM dual;
            full_name := user_rec.first_name || ' ' || user_rec.second_name;

            INSERT INTO NOTIFICATIONS
            (ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME, WORKFLOW_ID, TYPE, SUB_TYPE,
             STATUS, BEGIN_DATE, DUE_DATE, ACTION, LAUNCHER_ID, WORKFLOW_NAME, ACTIONABLE)
            VALUES
              (child_key, user_rec.user_id, user_rec.subject_id, full_name, qwf_instance, 'QUESTIONNAIRE',
                          'QUESTIONNAIRE_GENERAL',
                          'OPEN', sysdate, expire_date, 'ANSWER', launcher_id, survey_name, 'T');

            EXCEPTION
            WHEN NO_DATA_FOUND THEN
            EXIT;
          END;
        END LOOP;

        UPDATE que_workflows
        SET status = 'PENDING'
        WHERE id = qwf_instance;
        UPDATE que_workflows
        SET start_date = sysdate
        WHERE id = qwf_instance;

      END;
    END start_questionnaire;

  -------------------------------------------------------------------------------------------------------
  -- Name: set_actionable
  --
  -- Description:
  -- updates the notifications with a flag saying whether the notification can be acted upon, for
  -- appraisals this means they can be started, for questionnaires this means they can be answered
  -------------------------------------------------------------------------------------------------------
  PROCEDURE set_actionable(
    notification_id IN NUMBER,
    actionable_     IN VARCHAR2,
    next_action_    IN VARCHAR2) IS

    BEGIN
      UPDATE notifications
      SET actionable = actionable_
      WHERE id = notification_id;
      UPDATE notifications
      SET action = next_action_
      WHERE id = notification_id;

    END set_actionable;

  -------------------------------------------------------------------------------------------------------
  -- Name: create_notification
  --
  -- Description:
  -- creates a notification outside of the workflow for an hr or a managers manager to verify or approve
  -------------------------------------------------------------------------------------------------------
  PROCEDURE create_notification(
      root_notification_id_ IN NUMBER
    , next_action_          IN VARCHAR2
    , subject_id_           IN NUMBER
    , manager_id_           IN NUMBER
    , recipient_id_         IN NUMBER
  )
  IS
    v_subject_name          VARCHAR2(1000);
    v_manager_instance_id   NUMBER;
    v_evaluator_instance_id NUMBER;
    v_workflow_id           NUMBER;
    v_workflow_name         VARCHAR2(1000);
    v_performance_id        NUMBER;
    v_due_date              DATE;
    v_hr_id                 NUMBER;

    BEGIN

      SELECT cd.first_name || ' ' || cd.second_name
      INTO v_subject_name
      FROM core_details cd, subjects s
      WHERE s.node_id = subject_id_ AND s.cd_id = cd.id;

      SELECT
        WORKFLOW_ID,
        manager_instance_id,
        workflow_name,
        PERFORMANCE_REVIEW_ID,
        evaluators_instance_id,
        DUE_DATE
      INTO v_workflow_id, v_manager_instance_id, v_workflow_name, v_performance_id, v_evaluator_instance_id, v_due_date
      FROM notifications
      WHERE id = root_notification_id_;

      SELECT (SELECT HR_USER_ID
              FROM QUE_WORKFLOWS
              WHERE id = v_workflow_id)
      INTO v_hr_id
      FROM dual;

      -- update the all instances for the subject to identify that a notification has been created for the hr_user and to his manager
      IF recipient_id_ = v_hr_id
      THEN
        INSERT INTO NOTIFICATIONS
        (ID, MANAGER_ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME, WORKFLOW_ID, WORKFLOW_NAME, PERFORMANCE_REVIEW_ID,
         MANAGER_INSTANCE_ID, EVALUATORS_INSTANCE_ID, TYPE, SUB_TYPE, STATUS, DUE_DATE, ACTION, ACTIONABLE,
         ROOT_ID, BEGIN_DATE, HR_ID)

        VALUES (wf_sq.nextval, manager_id_, recipient_id_, subject_id_, v_subject_name, v_workflow_id,
                               v_workflow_name, v_performance_id, v_manager_instance_id, v_evaluator_instance_id,
                               'APPRAISAL', 'MANAGERASSESMENT', 'OPEN',
                v_due_date, next_action_, 'F', root_notification_id_, sysdate, v_hr_id);

        UPDATE notifications
        SET hr_id = recipient_id_
        WHERE SUBJECT_ID = subject_id_ AND PERFORMANCE_REVIEW_ID = v_performance_id;
      ELSE
        INSERT INTO NOTIFICATIONS
        (ID, MANAGER_ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME, WORKFLOW_ID, WORKFLOW_NAME, PERFORMANCE_REVIEW_ID,
         MANAGER_INSTANCE_ID, EVALUATORS_INSTANCE_ID, TYPE, SUB_TYPE, STATUS, DUE_DATE, ACTION, ACTIONABLE,
         ROOT_ID, BEGIN_DATE, HR_ID, managers_manager_id)

        VALUES (wf_sq.nextval, manager_id_, recipient_id_, subject_id_, v_subject_name, v_workflow_id,
                               v_workflow_name, v_performance_id, v_manager_instance_id, v_evaluator_instance_id,
                               'APPRAISAL', 'MANAGERASSESMENT', 'OPEN',
                v_due_date, next_action_, 'F', root_notification_id_, sysdate, v_hr_id, recipient_id_);

        UPDATE notifications
        SET managers_manager_id = recipient_id_
        WHERE SUBJECT_ID = subject_id_ AND PERFORMANCE_REVIEW_ID = v_performance_id;
      END IF;

    END create_notification;

  -------------------------------------------------------------------------------------------------------
  -- Name: respond_notification
  --
  -- Description: Sets the next state, for appraisals, questionnaires
  -------------------------------------------------------------------------------------------------------
  PROCEDURE respond_notification(
    notification_id_ IN NUMBER,
    next_action_     IN VARCHAR2)

  IS
    BEGIN
      DECLARE
        performance_id_     NUMBER;
        workflow_id_        NUMBER;
        notification_count_ INTEGER;

      BEGIN
        SELECT performance_review_id
        INTO performance_id_
        FROM notifications
        WHERE id = notification_id_;
        SELECT workflow_id
        INTO workflow_id_
        FROM notifications
        WHERE id = notification_id_;
        UPDATE notifications
        SET action = next_action_
        WHERE id = notification_id_;

        IF next_action_ = 'CLOSE'
        THEN

          -- part of the process has been completed we need to complete dependant notifications
          UPDATE notifications
          SET status = 'COMPLETED'
          WHERE id = notification_id_;

          UPDATE questionnaires
          SET is_completed = 'T'
          WHERE notification_id = notification_id_;
          UPDATE questionnaires
          SET completed_date = sysdate
          WHERE notification_id = notification_id_;

          -- if this is a performance review we need to check if there are any waiting notifications, if not we need to mark the process as complete
          IF performance_id_ IS NOT NULL
          THEN
            SELECT count(*)
            INTO notification_count_
            FROM notifications
            WHERE performance_review_id = performance_id_ AND status <> 'COMPLETED';

            -- should only be 1 the one who is to close the process
            IF notification_count_ < 1
            THEN
              UPDATE performance_reviews
              SET status = 'COMPLETED'
              WHERE id = performance_id_;
              UPDATE que_workflows
              SET status = 'COMPLETED'
              WHERE performance_id = performance_id_;
            END IF;

            -- this is a questionnaire
          ELSE
            SELECT count(*)
            INTO notification_count_
            FROM notifications
            WHERE workflow_id = workflow_id_ AND status <> 'COMPLETED';
            IF notification_count_ < 1
            THEN
              UPDATE que_workflows
              SET status = 'COMPLETED'
              WHERE id = workflow_id_;
            END IF;
          END IF;

        END IF;

      END;
    END respond_notification;

  -------------------------------------------------------------------------------------------------------
  -- Name: Start_Subject_Review
  --
  -- Description: Find evaluators selected by manager for each subject (evaluatee)
  -- and sends each evaluator a notification
  -------------------------------------------------------------------------------------------------------
  PROCEDURE Start_Subject_Review(
    notification_id_ IN NUMBER,
    subject_id_      IN NUMBER,
    role_id_         IN NUMBER,
    next_action_     IN VARCHAR2
  )
  IS
    BEGIN
      DECLARE

        child_key           NUMBER;
        workflow_name_      VARCHAR2(250);
        subject_name_       VARCHAR2(1000);
        role_name_          VARCHAR2(1000);
        expire_date         DATE;
        evaluators_instance NUMBER;
        performance_id_par  NUMBER;
        selected_manager_   NUMBER;
        user_managed_       VARCHAR(1);

        CURSOR surveylist(sub_id_ IN NUMBER, perf_id IN NUMBER) IS
          SELECT
            l.username,
            p.role,
            p.user_id
          FROM performance_evaluators p, logins l
          WHERE p.user_id = l.user_id
                AND p.subject_id = sub_id_
                AND p.performance_id = perf_id;

      BEGIN

        SELECT workflow_name
        INTO workflow_name_
        FROM notifications
        WHERE id = notification_id_;
        SELECT evaluators_instance_id
        INTO evaluators_instance
        FROM notifications
        WHERE id = notification_id_;
        SELECT performance_review_id
        INTO performance_id_par
        FROM notifications
        WHERE id = notification_id_;
        SELECT due_date
        INTO expire_date
        FROM notifications
        WHERE id = notification_id_;
        SELECT subject_name
        INTO subject_name_
        FROM notifications
        WHERE id = notification_id_;

        BEGIN
          SELECT user_managed
          INTO user_managed_
          FROM notifications
          WHERE id = notification_id_;

          IF user_managed_ = 'T'
          THEN

            SELECT manager_id
            INTO selected_manager_
            FROM performance_managers
            WHERE subject_id = subject_id_
                  AND performance_id = performance_id_par;

            IF selected_manager_ IS NOT NULL
            THEN
              UPDATE notifications
              SET recipient_id = selected_manager_, role_name = 'Manager'
              WHERE id = notification_id_;
            END IF;
          END IF;
          EXCEPTION
          WHEN NO_DATA_FOUND THEN
          DELETE FROM notifications
          WHERE id = notification_id_;
        END;

        FOR user_rec IN surveylist(subject_id_, performance_id_par) LOOP

          SELECT wf_sq.nextval
          INTO child_key
          FROM dual;
          SELECT short_desc
          INTO role_name_
          FROM lookup_values
          WHERE id = user_rec.role;

          -- insert the notifications
          INSERT INTO NOTIFICATIONS
          (ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME,
           WORKFLOW_ID, WORKFLOW_NAME, PERFORMANCE_REVIEW_ID,
           EVALUATORS_INSTANCE_ID, TYPE, SUB_TYPE, STATUS, DUE_DATE,
           ACTION, ROLE_ID, ROLE_NAME, ACTIONABLE, ROOT_ID, BEGIN_DATE)
          VALUES
            (child_key, user_rec.user_id, subject_id_, subject_name_,
                        evaluators_instance, workflow_name_, performance_id_par,
                        evaluators_instance, 'APPRAISAL', 'EVALUATOR_ASSESMENT', 'OPEN', expire_date,
             next_action_, user_rec.role, role_name_, 'T', notification_id_, sysdate);

        END LOOP;

      END;

    END Start_Subject_Review;

  -----------------------------------------------------------------------------------------------------
  -- removes the notifications and completes the workflows and questionnaires
  -----------------------------------------------------------------------------------------------------
  PROCEDURE closeProcess(
    workflow_id_ IN NUMBER)
  IS
    BEGIN
      -- the notifications
      DELETE FROM notifications
      WHERE workflow_id = workflow_id_;
      -- the workflows
      UPDATE que_workflows
      SET status = 'COMPLETED'
      WHERE id = workflow_id_;
      UPDATE que_workflows
      SET closed_date = sysdate
      WHERE id = workflow_id_;
      -- the questionnaires
      UPDATE questionnaires
      SET is_completed = 'T'
      WHERE qwf_id = workflow_id_;
      UPDATE questionnaires
      SET completed_date = sysdate
      WHERE qwf_id = workflow_id_;

    END closeProcess;

  ------------------------------------------------------------------------
  -- Name: remove_notification
  -- Description:
  -- Removes all notifications for the subject given by the subject_id_
  -- given by the parameter subject_id_
  ------------------------------------------------------------------------
  PROCEDURE remove_notification(subject_id_ IN NUMBER)

  IS
    BEGIN

      DELETE FROM notifications
      WHERE subject_id = subject_id_;

    END remove_notification;
  ------------------------------------------------------------------------
  -- Name: approve_notification
  -- Description:
  -- approves the appraisal workflow for the given subject
  -- given by the parameter subject_id_
  ------------------------------------------------------------------------

  PROCEDURE approve_notification(
      subject_id_     IN NUMBER
    , hr_id_          IN NUMBER
    , performance_id_ IN NUMBER
  )
  IS
    BEGIN
      UPDATE notifications
      SET APPROVED = 'T'
      WHERE subject_id = subject_id_ AND HR_ID = hr_id_ AND PERFORMANCE_REVIEW_ID = performance_id_;
      -- update the manager's notificaiton to indicate it can now be completed i.e. assign the next action
      DELETE FROM notifications
      WHERE RECIPIENT_ID = hr_id_ AND PERFORMANCE_REVIEW_ID = performance_id_ AND subject_id = subject_id_;

    END approve_notification;
  ------------------------------------------------------------------------
  -- Name: verify_notification
  -- Description:
  -- verifies the appraisal workflow for the given subject
  -- given by the parameter subject_id_
  ------------------------------------------------------------------------

  PROCEDURE verify_notification(
      subject_id_          IN NUMBER
    , managers_manager_id_ IN NUMBER
    , performance_id_      IN NUMBER
  )
  IS
    BEGIN
      UPDATE notifications
      SET VERIFIED = 'T'
      WHERE subject_id = subject_id_ AND MANAGERS_MANAGER_ID = managers_manager_id_ AND
            PERFORMANCE_REVIEW_ID = performance_id_;
      -- update the manager's notificaiton to indicate it can now be completed i.e. assign the next action
      DELETE FROM notifications
      WHERE
        RECIPIENT_ID = managers_manager_id_ AND PERFORMANCE_REVIEW_ID = performance_id_ AND subject_id = subject_id_;

    END verify_notification;
  ------------------------------------------------------------------------
  -- Name: reopen_notification
  -- Description:
  -- reopens the given notification
  ------------------------------------------------------------------------
  PROCEDURE reopen_notification(notif_id_ IN NUMBER)

  IS
    BEGIN

      UPDATE notifications
      SET status = 'OPEN'
      WHERE id = notif_id_;
      UPDATE notifications
      SET action = 'COMPLETE'
      WHERE id = notif_id_;
      -- the questionnaires
      UPDATE questionnaires
      SET is_completed = 'F'
      WHERE qwf_id = (SELECT workflow_id
                      FROM notifications
                      WHERE id = notif_id_)
            AND subject_id = (SELECT subject_id
                              FROM notifications
                              WHERE id = notif_id_);

    END reopen_notification;

  ------------------------------------------------------------------------
  -- Name: complete_notification
  -- Description:
  -- completes the given notification
  ------------------------------------------------------------------------
  PROCEDURE complete_notification(notif_id_ IN NUMBER)

  IS
    BEGIN

      UPDATE notifications
      SET status = 'COMPLETED'
      WHERE id = notif_id_;
      UPDATE notifications
      SET action = 'CLOSE'
      WHERE id = notif_id_;
      -- the questionnaires
      UPDATE questionnaires
      SET is_completed = 'T'
      WHERE qwf_id = (SELECT workflow_id
                      FROM notifications
                      WHERE id = notif_id_)
            AND subject_id = (SELECT subject_id
                              FROM notifications
                              WHERE id = notif_id_);

    END complete_notification;

  ------------------------------------------------------------------------
  -- Name: completeProcess-process
  -- Description:
  -- given a performance id it completes the questionnaires associated with the workflows, the
  -- workflows associated with the performance reviews!
  ------------------------------------------------------------------------
  PROCEDURE completeProcess(
    performance_id_ IN NUMBER
  )

  IS
    BEGIN

      -- update the performanceReview
      UPDATE performance_reviews
      SET status = 'COMPLETED'
      WHERE id = performance_id_;
      -- the workflows
      UPDATE que_workflows
      SET status = 'COMPLETED'
      WHERE performance_id = performance_id_;
      UPDATE que_workflows
      SET closed_date = sysdate
      WHERE performance_id = performance_id_;

      -- the questionnaires
      UPDATE questionnaires
      SET is_completed = 'T'
      WHERE qwf_id IN (SELECT id
                       FROM que_workflows
                       WHERE performance_id = performance_id_);
      UPDATE questionnaires
      SET completed_date = sysdate
      WHERE qwf_id IN (SELECT id
                       FROM que_workflows
                       WHERE performance_id = performance_id_);

      -- the notifications must all be set to completed
      UPDATE notifications
      SET status = 'COMPLETED'
      WHERE performance_review_id = performance_id_;

      -- next action must also be set
      UPDATE notifications
      SET action = 'CLOSE'
      WHERE performance_review_id = performance_id_;

    END completeProcess;

  ------------------------------------------------------------------------
  -- Name: reopen_subject_notification
  -- Description:
  -- reopens all the notifications for a given subject and performance review
  ------------------------------------------------------------------------
  PROCEDURE reopen_subject_notification(
      subject_id_     IN NUMBER
    , perf_review_id_ IN NUMBER)

  IS
    CURSOR notification_cur IS SELECT id
                               FROM notifications
                               WHERE performance_review_id = perf_review_id_ AND subject_id = subject_id_;
    notif_id_rec notifications.id%TYPE;
    q_count_     INTEGER;
    role_name_   VARCHAR2(255);

    BEGIN
      OPEN notification_cur;
      LOOP
        FETCH notification_cur INTO notif_id_rec;
        EXIT WHEN notification_cur%NOTFOUND;

        UPDATE notifications
        SET status = 'OPEN'
        WHERE id = notif_id_rec;

        SELECT role_name
        INTO role_name_
        FROM notifications
        WHERE id = notif_id_rec;
        IF 'Admin' = role_name_
        THEN
          UPDATE notifications
          SET action = 'CLOSE'
          WHERE id = notif_id_rec;
        ELSE
          -- action becames answer if there is no questionnaire
          SELECT count(*)
          INTO q_count_
          FROM questionnaires
          WHERE notification_id = notif_id_rec;
          IF q_count_ > 0
          THEN
            UPDATE notifications
            SET action = 'COMPLETE'
            WHERE id = notif_id_rec;
          ELSE
            UPDATE notifications
            SET action = 'ANSWER'
            WHERE id = notif_id_rec;
          END IF;

        END IF;

        -- the questionnaires
        UPDATE questionnaires
        SET is_completed = 'F'
        WHERE notification_id = notif_id_rec;
        UPDATE questionnaires
        SET completed_date = NULL
        WHERE notification_id = notif_id_rec;

      END LOOP;
      CLOSE notification_cur;

    END reopen_subject_notification;


END WF_INTEGRATION;
/
