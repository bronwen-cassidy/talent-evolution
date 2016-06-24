CREATE OR REPLACE package body WF_INTEGRATION as

-------------------------------------------------------------------------------------------------------
--Name: Start_People_Review
--
--Description:
-- Starts 360 PERSON_PERFORM_REVIEW process for each subject being evaluated
-- Finds subjects to evaluate by finding active subjects who have managers who are also active
-- Managers then recieve a notification for each for subject involved in the appraisal process
-------------------------------------------------------------------------------------------------------
procedure start_people_review (
              performance_id_ in number,
              manager_qwf_instance in number,
              evaluator_wf_id_ in number,
              launcher_id in number,
              survey_name_ in varchar2,
              expire_date in date ) is
begin
declare

child_key number;
full_name varchar2(500);
num_records_ number;

cursor surveylist is
    select * from survey_list where que_wf_id = manager_qwf_instance;
begin

    for user_rec in surveylist loop

        select wf_sq.nextval into child_key from dual;
        full_name := user_rec.first_name || ' ' || user_rec.second_name;

        -- check to see if the row we are inserting has a manager for the subject already assigned
        select count(*) into num_records_
                        from notifications
                        where subject_id = user_rec.subject_id
                        and performance_review_id=performance_id_
                        and role_name='Manager'
                        and action='ASSIGN_ROLES'
                        and WORKFLOW_ID=manager_qwf_instance;

        if num_records_ < 1 then
            insert into NOTIFICATIONS
                (ID, MANAGER_ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME,
                WORKFLOW_ID, MANAGER_INSTANCE_ID, PERFORMANCE_REVIEW_ID,
                EVALUATORS_INSTANCE_ID, TYPE, SUB_TYPE, STATUS, BEGIN_DATE, DUE_DATE,
                ACTION, LAUNCHER_ID, WORKFLOW_NAME, ROLE_NAME)
            values
                (child_key, user_rec.manager_id, user_rec.manager_id, user_rec.subject_id, full_name,
                manager_qwf_instance, manager_qwf_instance, performance_id_,
                evaluator_wf_id_, 'APPRAISAL', 'MANAGERASSESMENT', 'OPEN', sysdate, expire_date,
                'ASSIGN_ROLES', launcher_id, survey_name_, 'Manager');
        end if;        

    end loop;

    update performance_reviews set status='PENDING' where id = performance_id_;
    update que_workflows set status='PENDING' where performance_id = performance_id_;
    update que_workflows set start_date=sysdate where performance_id = performance_id_;

end;

end start_people_review;


-------------------------------------------------------------------------------------------------------
--Name: start_user_managed_review
--
--Description:
-- Starts performance review process for each subject being evaluated by creating notifications
-- for each evaluatee, each of these notifications though are sent to the person who has launched the process
-- as this person is to assign roles and start the review and not the managers of these people.
-------------------------------------------------------------------------------------------------------
procedure start_user_managed_review (
              performance_id_ in number,
              manager_qwf_instance in number,
              evaluator_wf_id_ in number,
              launcher_id in number,
              survey_name_ in varchar2,
              expire_date in date ) is
begin
declare

child_key number;
full_name varchar2(1000);

cursor surveylist is
    select s.node_id as subject_id, cd.first_name, cd.second_name, qwp.que_wf_id
        from subjects s, que_wf_participants qwp, core_details cd
        where s.node_id = qwp.subject_id
        and s.cd_id = cd.id
        and qwp.que_wf_id=manager_qwf_instance;
begin

    for user_rec in surveylist loop

        select wf_sq.nextval into child_key from dual;

        full_name := user_rec.first_name || ' ' || user_rec.second_name;

        insert into NOTIFICATIONS
            (ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME,
            WORKFLOW_ID, MANAGER_INSTANCE_ID, PERFORMANCE_REVIEW_ID,
            EVALUATORS_INSTANCE_ID, TYPE, SUB_TYPE, STATUS, BEGIN_DATE, DUE_DATE,
            ACTION, LAUNCHER_ID, WORKFLOW_NAME, ROLE_NAME, USER_MANAGED)
        values
            (child_key, launcher_id, user_rec.subject_id, full_name,
            manager_qwf_instance, manager_qwf_instance, performance_id_,
            evaluator_wf_id_, 'APPRAISAL', 'MANAGERASSESMENT', 'OPEN', sysdate, expire_date,
            'ASSIGN_ROLES', launcher_id, survey_name_, 'Administrator', 'T');

    end loop;

    update performance_reviews set status='PENDING' where id = performance_id_;
    update que_workflows set status='PENDING' where performance_id = performance_id_;
    update que_workflows set start_date=sysdate where performance_id = performance_id_;

end;

end start_user_managed_review;


-------------------------------------------------------------------------------------------------------
--Name: start_questionnaire
--
-- Description:
-- Starts the questionnaire process
-------------------------------------------------------------------------------------------------------
procedure start_questionnaire (
              qwf_instance in number,
              launcher_id in number,
              expire_date in date )
is
begin
declare

survey_name varchar2(80);
child_key number;
full_name varchar2(500);

cursor surveylist is
    select p.subject_id, s.user_id, cd.first_name, cd.second_name
    from que_wf_participants p, subjects s, core_details cd
    where p.subject_id=s.node_id
    and s.cd_id = cd.id
    and p.que_wf_id=qwf_instance;
begin

	BEGIN
    	select label into survey_name from que_workflows where id = qwf_instance;
    	EXCEPTION
		   WHEN NO_DATA_FOUND THEN
	       NULL;

	end;

    for user_rec in surveylist loop
        BEGIN
	        select wf_sq.nextval into child_key from dual;
	        full_name := user_rec.first_name || ' ' || user_rec.second_name;

	        insert into NOTIFICATIONS
	            (ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME, WORKFLOW_ID, TYPE, SUB_TYPE,
	            STATUS, BEGIN_DATE, DUE_DATE, ACTION, LAUNCHER_ID, WORKFLOW_NAME, ACTIONABLE)
	        values
	            (child_key, user_rec.user_id, user_rec.subject_id, full_name, qwf_instance, 'QUESTIONNAIRE', 'QUESTIONNAIRE_GENERAL',
	            'OPEN', sysdate, expire_date, 'ANSWER', launcher_id, survey_name, 'T');

	     EXCEPTION
		   WHEN NO_DATA_FOUND THEN
		   exit;
		END;
    end loop;

    update que_workflows set status='PENDING' where id = qwf_instance;
    update que_workflows set start_date=sysdate where id = qwf_instance;

end;
end start_questionnaire;

-------------------------------------------------------------------------------------------------------
-- Name: set_actionable
--
-- Description:
-- updates the notifications with a flag saying whether the notification can be acted upon, for
-- appraisals this means they can be started, for questionnaires this means they can be answered
-------------------------------------------------------------------------------------------------------
procedure set_actionable (
              notification_id in number,
              next_user_id_ in number,
              actionable_ in varchar2,
              next_action_ in varchar2 ) is

begin
     update notifications set actionable = actionable_ where id = notification_id;
     update notifications set action = next_action_ where id = notification_id;
     update notifications set next_user_id = next_user_id_ where id = notification_id;

end set_actionable;

-------------------------------------------------------------------------------------------------------
-- Name: respond_notification
--
-- Description: Sets the next state, for appraisals, questionnaires
-------------------------------------------------------------------------------------------------------
procedure respond_notification (
              notification_id_ in number,
              next_action_ in varchar2,
              responder_user_name_ in varchar2,
              responder_id_ in number)

is
begin
declare
performance_id_ number;
workflow_id_ number;
notification_count_ integer;

begin
    select performance_review_id into performance_id_ from notifications where id = notification_id_;
    select workflow_id into workflow_id_ from notifications where id = notification_id_;
    update notifications set action = next_action_ where id = notification_id_;

	if next_action_ = 'CLOSE' then

	    -- part of the process has been completed we need to complete dependant notifications
	    update notifications set status = 'COMPLETED' where id = notification_id_;

        update questionnaires set is_completed = 'T' where notification_id = notification_id_;
        update questionnaires set completed_date = sysdate where notification_id = notification_id_;

	    -- if this is a performance review we need to check if there are any waiting notifications, if not we need to mark the process as complete
	    if performance_id_ is not null then
	        select count(*) into notification_count_ from notifications where performance_review_id = performance_id_ and status <> 'COMPLETED';

	        -- should only be 1 the one who is to close the process
	        if notification_count_ < 1 then
	            update performance_reviews set status = 'COMPLETED' where id = performance_id_;
	            update que_workflows set status = 'COMPLETED' where performance_id = performance_id_;
	        end if;

	    -- this is a questionnaire
	    else
	        select count(*) into notification_count_ from notifications where workflow_id = workflow_id_ and status <> 'COMPLETED';
	        if notification_count_ < 1 then
                update que_workflows set status = 'COMPLETED' where id = workflow_id_;
            end if;
	    end if;

	end if;

end;
end respond_notification;

-------------------------------------------------------------------------------------------------------
-- Name: Start_Subject_Review
--
-- Description: Find evaluators selected by manager for each subject (evaluatee)
-- and sends each evaluator a notification
-------------------------------------------------------------------------------------------------------
procedure Start_Subject_Review (
            notification_id_ in number,
            subject_id_ in number,
            role_id_ in number,
            next_action_ in varchar2
            )
is
begin
declare

child_key number;
workflow_name_ varchar2(250);
subject_name_ varchar2(1000);
role_name_ varchar2(1000);
expire_date date;
evaluators_instance number;
performance_id_par number;
selected_manager_ number;
user_managed_ varchar(1);

cursor surveylist(sub_id_ in NUMBER, perf_id in NUMBER) is
    select l.username, p.role, p.user_id
    from performance_evaluators p, logins l
    where p.user_id = l.user_id
    and p.subject_id = sub_id_
    and p.performance_id = perf_id;

begin

    select workflow_name into workflow_name_ from notifications where id = notification_id_;
    select evaluators_instance_id into evaluators_instance from notifications where id = notification_id_;
    select performance_review_id into performance_id_par from notifications where id = notification_id_;
    select due_date into expire_date from notifications where id = notification_id_;
    select subject_name into subject_name_ from notifications where id = notification_id_;

    begin
        select user_managed into user_managed_ from notifications where id = notification_id_;

        if user_managed_ = 'T' then

            select manager_id into selected_manager_
                from performance_managers
                where subject_id=subject_id_
                and performance_id=performance_id_par;

            if selected_manager_ is not null then
                update notifications set recipient_id = selected_manager_, role_name = 'Manager' where id = notification_id_;
            end if;
        end if;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN  
            delete from notifications where id = notification_id_;
    END;

    for user_rec in surveylist(subject_id_,performance_id_par) loop

        select wf_sq.nextval into child_key from dual;
        select short_desc into role_name_ from lookup_values where id = user_rec.role;
        
        -- insert the notifications
        insert into NOTIFICATIONS
            (ID, RECIPIENT_ID, SUBJECT_ID, SUBJECT_NAME,
            WORKFLOW_ID, WORKFLOW_NAME, PERFORMANCE_REVIEW_ID,
            EVALUATORS_INSTANCE_ID, TYPE, SUB_TYPE, STATUS, DUE_DATE,
            ACTION, ROLE_ID, ROLE_NAME, ACTIONABLE, ROOT_ID, BEGIN_DATE)
        values
            (child_key, user_rec.user_id, subject_id_, subject_name_,
            evaluators_instance, workflow_name_, performance_id_par,
            evaluators_instance, 'APPRAISAL', 'EVALUATOR_ASSESMENT', 'OPEN', expire_date,
            next_action_, user_rec.role, role_name_, 'T', notification_id_, sysdate);

    end loop;

end;

end Start_Subject_Review;

-----------------------------------------------------------------------------------------------------
-- removes the notifications and completes the workflows and questionnaires
-----------------------------------------------------------------------------------------------------
procedure closeProcess (
        workflow_id_ in number)
IS
begin
    -- the notifications
    delete from notifications where workflow_id=workflow_id_;
    -- the workflows
    update que_workflows set status = 'COMPLETED' where id = workflow_id_;
    update que_workflows set closed_date = sysdate where id = workflow_id_;
    -- the questionnaires
    update questionnaires set is_completed = 'T' where qwf_id = workflow_id_;
    update questionnaires set completed_date = sysdate where qwf_id = workflow_id_;

end closeProcess;


------------------------------------------------------------------------
-- Name: remove_notification
-- Description:
-- Removes all notifications for the subject given by the subject_id_
-- given by the parameter subject_id_
------------------------------------------------------------------------
procedure remove_notification(subject_id_ in number)

IS
BEGIN

    delete from notifications where subject_id = subject_id_;

end remove_notification;

------------------------------------------------------------------------
-- Name: reopen_notification
-- Description:
-- reopens the given notification
------------------------------------------------------------------------
procedure reopen_notification(notif_id_ in number)

IS
BEGIN

    update notifications set status = 'OPEN' where id=notif_id_;
    update notifications set action = 'COMPLETE' where id = notif_id_;
    -- the questionnaires
    update questionnaires set is_completed = 'F'
                  where qwf_id = (select workflow_id from notifications where id = notif_id_)
                  and subject_id = (select subject_id from notifications where id = notif_id_);

end reopen_notification;

------------------------------------------------------------------------
-- Name: complete_notification
-- Description:
-- completes the given notification
------------------------------------------------------------------------
procedure complete_notification(notif_id_ in number)

IS
BEGIN

    update notifications set status = 'COMPLETED' where id=notif_id_;
    update notifications set action = 'CLOSE' where id = notif_id_;
    -- the questionnaires
    update questionnaires set is_completed = 'T'
                  where qwf_id = (select workflow_id from notifications where id = notif_id_)
                  and subject_id = (select subject_id from notifications where id = notif_id_);

end complete_notification;


------------------------------------------------------------------------
-- Name: completeProcess-process
-- Description:
-- given a performance id it completes the questionnaires associated with the workflows, the
-- workflows associated with the performance reviews!
------------------------------------------------------------------------
procedure completeProcess (
    performance_id_ in number
)

IS
BEGIN

  -- update the performanceReview
  update performance_reviews set status = 'COMPLETED' where id = performance_id_;
  -- the workflows
  update que_workflows set status = 'COMPLETED' where performance_id = performance_id_;
  update que_workflows set closed_date = sysdate where performance_id = performance_id_;

  -- the questionnaires
  update questionnaires set is_completed = 'T' where qwf_id in (select id from que_workflows where performance_id = performance_id_);
  update questionnaires set completed_date = sysdate where qwf_id in (select id from que_workflows where performance_id = performance_id_);

  -- the notifications must all be set to completed
  update notifications set status = 'COMPLETED' where performance_review_id = performance_id_;

  -- next action must also be set
  update notifications set action = 'CLOSE' where performance_review_id = performance_id_;

END completeProcess;

------------------------------------------------------------------------
-- Name: reopen_subject_notification
-- Description:
-- reopens all the notifications for a given subject and performance review
------------------------------------------------------------------------
procedure reopen_subject_notification (
    subject_id_ in number
    ,perf_review_id_ in number)

IS
	CURSOR notification_cur is select id from notifications where performance_review_id=perf_review_id_ and subject_id=subject_id_;
	notif_id_rec notifications.id%type;
	q_count_ integer;
	role_name_ varchar2(255);

BEGIN
	OPEN notification_cur;
   		LOOP
   			FETCH notification_cur INTO notif_id_rec;
   			EXIT WHEN notification_cur%notfound;

   			update notifications set status = 'OPEN' where id=notif_id_rec;

   			select role_name into role_name_ from notifications where id=notif_id_rec;
   			if 'Admin' = role_name_ then
    			update notifications set action = 'CLOSE' where id = notif_id_rec;
   			else
				-- action becames answer if there is no questionnaire
				select count(*) into q_count_ from questionnaires where notification_id=notif_id_rec;
				if q_count_ > 0 then
	    			update notifications set action = 'COMPLETE' where id = notif_id_rec;
	   		    else
	    			update notifications set action = 'ANSWER' where id = notif_id_rec;
	   		    end if;

    		end if;

    		-- the questionnaires
    		update questionnaires set is_completed = 'F' where notification_id=notif_id_rec;
			update questionnaires set completed_date = null where notification_id=notif_id_rec;

        END LOOP;
    CLOSE notification_cur;

end reopen_subject_notification;



END WF_INTEGRATION;
/
