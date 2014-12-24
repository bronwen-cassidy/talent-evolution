--------------------------------------------------------
-- 26/03/08
--------------------------------------------------------

create table subject_pictures
(
 SUBJECT_ID INTEGER NOT NULL
 ,PICTURE BLOB
)
/

insert INTO subject_pictures (SUBJECT_ID, PICTURE)
select node_id, picture from subjects where length(picture) > 0;

ALTER TABLE SUBJECTS ADD HAS_PICTURE VARCHAR2(1) DEFAULT 'F';

UPDATE SUBJECTS SET HAS_PICTURE='T' where length(picture) > 0;

ALTER TABLE SUBJECTS DROP COLUMN PICTURE;

ALTER TABLE LOOKUP_VALUES DROP CONSTRAINT ZLVT_UK;

CREATE TABLE NOTIFICATIONS (
 ID NUMBER NOT NULL
,MANAGER_ID NUMBER
,SUBJECT_ID NUMBER NOT NULL
,RECIPIENT_ID NUMBER NOT NULL
,MANAGER_INSTANCE_ID NUMBER
,EVALUATORS_INSTANCE_ID NUMBER
,PERFORMANCE_REVIEW_ID NUMBER
,WORKFLOW_ID NUMBER
,ROLE_ID NUMBER
,ROOT_ID NUMBER
,SUBJECT_NAME VARCHAR2 (1000)
,ROLE_NAME VARCHAR2 (1000)
,WORKFLOW_NAME VARCHAR2 (1000)
,TYPE VARCHAR2 (1000)
,SUB_TYPE VARCHAR2 (1000)
,STATUS VARCHAR2 (1000)
,BEGIN_DATE DATE
,END_DATE DATE
,DUE_DATE DATE
,ACTIONABLE CHAR(1) DEFAULT 'F'
,ACTION VARCHAR2(500)
,LAUNCHER_ID NUMBER
,USER_MANAGED VARCHAR(1) DEFAULT 'F'
)
/

ALTER TABLE NOTIFICATIONS
 ADD CONSTRAINT NOT_PK
 PRIMARY KEY (ID)
 ENABLE
 VALIDATE
/

alter table performance_reviews add is_user_managed varchar2(1) default 'F';
alter table que_workflows add LAST_REPUPLISHED_DATE Date;


PROMPT Creating Table 'PERFORMANCE_MANAGERS'
CREATE TABLE PERFORMANCE_MANAGERS
 (ID NUMBER NOT NULL
 ,SUBJECT_ID INTEGER NOT NULL
 ,PERFORMANCE_ID NUMBER NOT NULL
 ,MANAGER_ID INTEGER NOT NULL
 )
/

PROMPT Creating Primary Key on 'PERFORMANCE_MANAGERS'
ALTER TABLE PERFORMANCE_MANAGERS
 ADD (CONSTRAINT PMAN_PK PRIMARY KEY
  (ID))
/

PROMPT Creating Foreign Key on 'PERFORMANCE_MANAGERS'
ALTER TABLE PERFORMANCE_MANAGERS ADD (CONSTRAINT
 PMAN_SUB_FK FOREIGN KEY
  (SUBJECT_ID) REFERENCES SUBJECTS
  (NODE_ID))
/
PROMPT Creating Foreign Key on 'PERFORMANCE_MANAGERS'
ALTER TABLE PERFORMANCE_MANAGERS ADD (CONSTRAINT
 PMAN_US_FK FOREIGN KEY
  (MANAGER_ID) REFERENCES USERS
  (ID))
/
PROMPT Creating Foreign Key on 'PERFORMANCE_MANAGERS'
ALTER TABLE PERFORMANCE_MANAGERS ADD (CONSTRAINT
 PMAN_PM_FK FOREIGN KEY
  (PERFORMANCE_ID) REFERENCES PERFORMANCE_REVIEWS
  (ID))
/



DELETE FROM SESSION_LOGS;
DELETE FROM SESSIONS_OPENED;
ALTER TABLE SESSION_LOGS ADD ID NUMBER NOT NULL;
ALTER TABLE SESSIONS_OPENED DROP CONSTRAINT SOD_ZSLT_FK;
ALTER TABLE SESSION_LOGS DROP CONSTRAINT ZSLT_PK;
ALTER TABLE SESSIONS_OPENED DROP COLUMN SESSION_ID;
ALTER TABLE SESSIONS_OPENED ADD SESSION_ID NUMBER;


ALTER TABLE SESSION_LOGS
ADD (CONSTRAINT ZSLT_PK PRIMARY KEY
(ID))
/

ALTER TABLE SESSIONS_OPENED ADD (CONSTRAINT
SOD_ZSLT_FK FOREIGN KEY
(SESSION_ID) REFERENCES SESSION_LOGS
(ID) ON DELETE CASCADE)
/

update display_config_items set content_type='EXEC' where id=-32;

prompt
prompt Enter the name of the schema you are currently upgrading
prompt
accept l_user char prompt 'User id: '

prompt
prompt Enter the name of their WF user.
prompt
accept wf_user char prompt 'WF Schema: '

connect &wf_user/&wf_user
REVOKE execute on wf_engine FROM &l_user;
REVOKE execute on wf_notification from &l_user;
REVOKE select on WF_WORKLIST_V from &l_user;
REVOKE select on WF_NOTIFICATION_ATTRIBUTES from &l_user;
REVOKE select on WF_MESSAGE_ATTRIBUTES from &l_user;
REVOKE select on WF_MESSAGE_ATTRIBUTES_TL from &l_user;
REVOKE select on WF_MESSAGES_TL from &l_user;
REVOKE select on wf_lookups from &l_user;
REVOKE select on WF_LOOKUPS_TL from &l_user;
REVOKE select on WF_ITEM_TYPES_TL from &l_user;
REVOKE select,update on WF_NOTIFICATIONS from &l_user;
REVOKE select,update on wf_item_activity_statuses from &l_user;
REVOKE select,update on wf_item_activity_statuses_h from &l_user;
REVOKE select,update on wf_items from &l_user;
REVOKE execute on wf_purge from &l_user;

connect &l_user/&l_user
revoke select on users from &wf_user;
revoke select on logins from &wf_user;
revoke select on core_details from &wf_user;
revoke select on QUE_WF_PARTICIPANTS from &wf_user;

DROP SYNONYM WF_ENGINE;
DROP SYNONYM WF_NOTIFICATION;
DROP SYNONYM WF_NOTIFICATION_ATTRIBUTES;
DROP SYNONYM WF_MESSAGE_ATTRIBUTES;
DROP SYNONYM WF_MESSAGE_ATTRIBUTES_TL;
DROP SYNONYM WF_LOOKUPS;
DROP SYNONYM WF_LOOKUPS_TL;
DROP SYNONYM WF_ITEM_TYPES_TL;
DROP SYNONYM WF_NOTIFICATIONS;
DROP SYNONYM wf_item_activity_statuses;
DROP SYNONYM wf_item_activity_statuses_h;
DROP SYNONYM wf_items;
DROP SYNONYM wf_purge;
DROP SYNONYM WF_MESSAGES_TL;
DROP SYNONYM WF_MONITOR;
DROP SYNONYM FND_DOCUMENT_MANAGEMENT;

DROP VIEW WF_MESSAGE_ATTRIBUTES_VL;
DROP VIEW WF_WORKLIST_V;
DROP VIEW WF_NOTIFICATION_ACTIONS;

REVOKE execute on wf_integration FROM &wf_user;

connect &wf_user/&wf_user

DROP SYNONYM wf_integration;
DROP VIEW WF_USERS;

connect &l_user/&l_user
@views/zynap_views.sql
@views/security_views.sql
@packages/zynap_app_spec.sql
@packages/zynap_app_body.sql
@packages/zynap_wf_integration_spec.sql
@packages/zynap_wf_integration_body.sql
@packages/zynap_triggers_hierarchy.sql

@packages/zynap_position_spec.sql
@packages/zynap_position_body.sql
@packages/zynap_org_unit_spec.sql
@packages/zynap_org_unit_body.sql

ALTER PACKAGE WF_INTEGRATION COMPILE SPECIFICATION;
ALTER PACKAGE WF_INTEGRATION COMPILE BODY;
ALTER PACKAGE ZYNAP_APP_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_APP_SP COMPILE BODY;
ALTER PACKAGE ZYNAP_AUTH_SP COMPILE BODY;
ALTER PACKAGE ZYNAP_LOADER_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_LOADER_SP COMPILE BODY;
ALTER PACKAGE ZYNAP_ORG_UNIT_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_ORG_UNIT_SP COMPILE BODY;
ALTER PACKAGE ZYNAP_POSITION_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_POSITION_SP COMPILE BODY;

commit;