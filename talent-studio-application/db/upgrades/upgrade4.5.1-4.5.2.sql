-- remove old menu_items
DELETE FROM MENU_ITEMS WHERE url = '/talentarena/worklistobjectives.htm';

-- new menu item
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_myzynap_module_id(), 'ACCOUNT',-33, 'My Team',30,'/talentarena/viewmyteam.htm', 'myteam.menu.description', 'SYSTEMSUBJECT');

-- new permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'view', 'DETAILS', 'Access to My Details','T', '/talentarena/viewmy*.*', null, null, null);

-- set permit on existing mydetails and myaccount menu items
update menu_items set permit_id = permit_sq.currval where url = '/talentarena/viewmydetails.htm';
update menu_items set permit_id = permit_sq.currval where url = '/talentarena/viewmyaccount.htm';
update menu_items set permit_id = permit_sq.currval where url = '/talentarena/viewmyteam.htm';

-- remove old permits for mydetails and myaccount menu items from roles
delete from PERMITS_ROLES where permit_id in (select id from permits where URL = '/talentarena/viewmydetails.htm');
delete from PERMITS_ROLES where permit_id in (select id from permits where URL = '/talentarena/viewmyaccount.htm');

-- remove old permits for mydetails and myaccount menu items
DELETE FROM PERMITS WHERE URL = '/talentarena/viewmydetails.htm';
DELETE FROM PERMITS WHERE URL = '/talentarena/viewmyaccount.htm';

-- questionnaire permits changes
update permits set url = '/talentarena/viewmyquestionnaire.htm', description = 'Permission to View My Questionnaires' where url   ='/talentarena/viewmyquestionnaireitem.htm';
insert into permits(ID, TYPE, CONTENT_PARAM, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', null, 'edit','QUESTIONNAIRES' ,'Permission to Edit My Questionnaires', 'T','/talentarena/editmyquestionnaire.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'close','QUESTIONNAIRES' ,'Permission to close questionnaires', 'T','/admin/confirmclosequestionnaireworkflow.*.htm', null, null, null);

-- new permit to run reporting charts in analysis arena
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'browse','REPORTING STRUCTURE' ,'Permission to Browse Reporting Structure', 'T','/analysis/reportingchart.htm', null, null, null);

-- new permit to view performance reviews
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'view','APPRAISALS' ,'Permission to View Appraisals', 'T','/perfman/viewperformancereview.htm', null, null, null) ;

-- rebuild home role
delete from permits_roles where role_id = 8;
insert into permits_roles select id, '8' from permits where url like '%talentarena%' AND type = 'AP';

-- rebuild administrator role
delete from permits_roles where role_id = 1;
insert into permits_roles select id, '1' from permits where type = 'AP';

-- rebuild analysis role
delete from permits_roles where role_id = 6;
insert into permits_roles select id, '6' from permits where url like '%analysis%' AND type = 'AP';

-- rebuild performance management role
delete from permits_roles where role_id = 7;
insert into permits_roles select id, '7' from permits where url like '%perfman%' AND type = 'AP';

exec zynap_loader_sp.menu_permits_link;

-- new columns and constraints

-- add a column
ALTER TABLE QUE_WORKFLOWS ADD CLOSED_DATE DATE;

ALTER TABLE QUE_WORKFLOWS ADD POPULATION_ID INTEGER;

ALTER TABLE QUE_WORKFLOWS ADD (CONSTRAINT
 QWF_POP_FK FOREIGN KEY
  (POPULATION_ID) REFERENCES POPULATIONS
  (ID))
/

-- new tables

CREATE TABLE QUE_WF_PARTICIPANTS
(
  QUE_WF_ID INTEGER NOT NULL,
  SUBJECT_ID INTEGER NOT NULL
)
/

ALTER TABLE QUE_WF_PARTICIPANTS
 ADD CONSTRAINT QWFP_FK_ID
 FOREIGN KEY (QUE_WF_ID)
 REFERENCES QUE_WORKFLOWS (ID) ON DELETE CASCADE
/

ALTER TABLE QUE_WF_PARTICIPANTS
 ADD CONSTRAINT QWFP_NODE_FK_ID
 FOREIGN KEY (SUBJECT_ID)
 REFERENCES NODES (ID) ON DELETE CASCADE
/

ALTER TABLE QUE_WF_PARTICIPANTS
 ADD CONSTRAINT QWP_PK
 PRIMARY KEY (SUBJECT_ID, QUE_WF_ID)
 ENABLE
 VALIDATE
/

ALTER TABLE QUE_WF_PARTICIPANTS
 ADD CONSTRAINT QUE_WF_PK
 UNIQUE (SUBJECT_ID, QUE_WF_ID)
/

CREATE TABLE TRACE_TABLE
(
  TEXT VARCHAR2(255),
  T_STAMP DATE
)
/

-- grants

grant select on QUE_WF_PARTICIPANTS to &wf_user;

@packages/zynap_loader_spec.sql
@packages/zynap_loader_body.sql
@packages/zynap_wf_integration_spec.sql
@packages/zynap_wf_integration_body.sql

-- recompile views and procedures
ALTER PACKAGE WF_INTEGRATION COMPILE BODY;

-- constraint alteration
ALTER TABLE QUE_WORKFLOWS DROP CONSTRAINT QUWFLL_UK;

ALTER TABLE QUE_WORKFLOWS
 ADD (CONSTRAINT QUWFLL_UK UNIQUE
  (LABEL, WORKFLOW_TYPE));

-- appraisal roles lookup type
EXEC zynap_lookup_sp.install_type( 'APPRAISAL_ROLES', 'SYSTEM', 'Appraisal Roles', 'Appraisal Roles');
EXEC zynap_lookup_sp.install_values( 'APPRAISAL_ROLES', 'SELFEVALUATOR', 'Self Evaluator', 'Self Evaluator', 10, TRUE);
EXEC zynap_lookup_sp.install_values( 'APPRAISAL_ROLES', 'INTERNALCUSTOMER', 'Internal Customer', 'Internal Customer', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'APPRAISAL_ROLES', 'PEER', 'Peer', 'Peer', 30, FALSE);

-- notification view
CREATE OR REPLACE VIEW WF_WORKLIST_V ("ROW_ID","NID",
    "PRIORITY","MESSAGE_TYPE","RECIPIENT_ROLE","SUBJECT",
    "BEGIN_DATE","DUE_DATE","END_DATE","DISPLAY_STATUS","STATUS",
    "ORIGINAL_RECIPIENT","ITEM_TYPE","MESSAGE_NAME","FROM_USER",
    "TO_USER","LANGUAGE","MORE_INFO_ROLE","URL","ACTIONABLE",
    "WORKFLOW_ID","WORKFLOW_NAME", "SUBJECT_ID", "SUBJECT_NAME", "TARGET","ROLE_ID", "ROLE_NAME") AS
    select /* $Header: wfntfv.sql 26.4 2001/05/02 05:44:08 dlam
    ship $ */
  WN.ROWID,
  WN.NOTIFICATION_ID,
  WN.PRIORITY,
  WIT.DISPLAY_NAME,
  WN.RECIPIENT_ROLE,
  wf_integration.GetSubject(WN.notification_id),
  WN.BEGIN_DATE,
  WN.DUE_DATE,
  WN.END_DATE,
  WL.MEANING,
  WN.STATUS,
  WN.ORIGINAL_RECIPIENT,
  WN.MESSAGE_TYPE,
  WN.MESSAGE_NAME,
  WN.FROM_USER,
  WN.TO_USER,
  WN.LANGUAGE,
  WN.MORE_INFO_ROLE,
  wf_integration.GetUrl(WN.notification_id),
  wf_integration.getActionable(WN.notification_id),
  wf_integration.getWorkflowId(WN.notification_id),
  wf_integration.getWorkflowName(WN.notification_id),
  wf_integration.getSubjectId(WN.notification_id),
  wf_integration.getSubjectName(WN.notification_id),
  wf_integration.getTarget(WN.notification_id),
  wf_integration.getRoleId(WN.notification_id),
  wf_integration.getRoleName(WN.notification_id)
 from WF_NOTIFICATIONS WN, WF_ITEM_TYPES_TL WIT, WF_LOOKUPS_TL WL
 where WN.MESSAGE_TYPE = WIT.NAME
  and WL.LOOKUP_TYPE = 'WF_NOTIFICATION_STATUS'
  and WN.STATUS = WL.LOOKUP_CODE;

-- questionnaire role modification

-- rename old column
ALTER TABLE QUESTIONNAIRES RENAME COLUMN ROLE to OLD_ROLE;

-- add new column
ALTER TABLE QUESTIONNAIRES ADD ROLE NUMBER;

-- add constraint joining role to lookup value
ALTER TABLE QUESTIONNAIRES ADD CONSTRAINT Q_ROLE_FK FOREIGN KEY (ROLE) REFERENCES LOOKUP_VALUES (ID);

-- set values for new column
UPDATE QUESTIONNAIRES Q SET ROLE = (SELECT LV.ID FROM LOOKUP_VALUES LV WHERE Q.OLD_ROLE = LV.SHORT_DESC);
UPDATE REPORT_COLUMNS RC SET ROLE = (SELECT LV.ID FROM LOOKUP_VALUES LV WHERE RC.ROLE = LV.SHORT_DESC);
UPDATE POPULATION_CRITERIAS P SET ROLE = (SELECT LV.ID FROM LOOKUP_VALUES LV WHERE P.ROLE = LV.SHORT_DESC);
UPDATE METRICS M SET ROLE = (SELECT LV.ID FROM LOOKUP_VALUES LV WHERE M.ROLE = LV.SHORT_DESC);

ALTER TABLE PERFORMANCE_EVALUATORS RENAME COLUMN ROLE to OLD_ROLE;
ALTER TABLE PERFORMANCE_EVALUATORS ADD ROLE NUMBER;
ALTER TABLE PERFORMANCE_EVALUATORS ADD CONSTRAINT PE_ROLE_FK FOREIGN KEY (ROLE) REFERENCES LOOKUP_VALUES (ID);
UPDATE PERFORMANCE_EVALUATORS PE SET ROLE = (SELECT LV.ID FROM LOOKUP_VALUES LV WHERE PE.OLD_ROLE = LV.SHORT_DESC);

ALTER TABLE PERFORMANCE_EVALUATORS DROP CONSTRAINT PEREV_UK;

ALTER TABLE PERFORMANCE_EVALUATORS
 ADD (CONSTRAINT PEREV_UK UNIQUE
  (PERFORMANCE_ID
  ,SUBJECT_ID
  ,ROLE));

-- drop old role column
ALTER TABLE QUESTIONNAIRES DROP COLUMN OLD_ROLE;
ALTER TABLE PERFORMANCE_EVALUATORS DROP COLUMN OLD_ROLE;

ALTER TABLE PERFORMANCE_EVALUATORS MODIFY(ROLE  NOT NULL);

-- change perf evaluator constraints to be cascade delete
ALTER TABLE PERFORMANCE_EVALUATORS DROP CONSTRAINT PEREV_USER_1_FK;

ALTER TABLE PERFORMANCE_EVALUATORS ADD (CONSTRAINT
 PEREV_USER_1_FK FOREIGN KEY
  (USER_ID) REFERENCES USERS
  (ID) ON DELETE CASCADE);

ALTER TABLE PERFORMANCE_EVALUATORS DROP CONSTRAINT PEREV_SUB_FK;

ALTER TABLE PERFORMANCE_EVALUATORS ADD (CONSTRAINT
 PEREV_SUB_FK FOREIGN KEY
  (SUBJECT_ID) REFERENCES SUBJECTS
  (NODE_ID) ON DELETE CASCADE);

-- dynamic attribute reference changes to support cross tab reporting on enum mappings
ALTER TABLE DYNAMIC_ATTR_REFERENCES ADD LOOKUP_VALUE_ID NUMBER;

ALTER TABLE DYNAMIC_ATTR_REFERENCES DROP COLUMN LABEL;

ALTER TABLE DYNAMIC_ATTR_REFERENCES ADD CONSTRAINT
 DAR_LV_FK FOREIGN KEY
  (LOOKUP_VALUE_ID) REFERENCES LOOKUP_VALUES
  (ID)  ON DELETE CASCADE
/

COMMIT;
