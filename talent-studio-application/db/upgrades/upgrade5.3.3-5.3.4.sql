--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

CREATE TABLE REPORT_WORKFLOWS (
   ID NUMBER NOT NULL
  ,REPORT_ID NUMBER NOT NULL
  ,WORKFLOW_ID NUMBER NOT NULL
  ,ROLE VARCHAR2(512)
  ,LABEL VARCHAR2(1024) NOT NULL
  ,POSITION NUMBER NOT NULL
)
/

ALTER TABLE REPORT_WORKFLOWS
 ADD (CONSTRAINT REP_WKF_PK PRIMARY KEY
  (ID))
/

ALTER TABLE REPORT_WORKFLOWS
 ADD (CONSTRAINT REPWF_RP_FK FOREIGN KEY
  (REPORT_ID) REFERENCES REPORTS (ID))
/

ALTER TABLE REPORT_WORKFLOWS
 ADD (CONSTRAINT REPWF_WF_FK FOREIGN KEY
  (WORKFLOW_ID) REFERENCES QUE_WORKFLOWS (ID))
/

ALTER TABLE REPORTS ADD QD_ID NUMBER;

ALTER TABLE REPORTS
 ADD (CONSTRAINT REP_QD_FK FOREIGN KEY
  (QD_ID) REFERENCES QUE_DEFINITIONS (ID))
/

ALTER TABLE REPORTS ADD CHART_TYPE VARCHAR2 (252);


-- progress report menu_items and permits
-- menu items and permits
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type)
VALUES('ANALYSISMODULE', 'REPORTS', -58, 'mi.progress.reports', 32, '/analysis/listprogressreports.htm', 'progress.reports.menu.description', null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','PROGRESS REPORTS' ,'Permission to Browse Progress Reports', 'T','/analysis/listprogressreports.htm', null, null, null);
exec zynap_loader_sp.menu_permits_link;

insert into permits_roles select id, '6' from permits where url = '/analysis/listprogressreports.htm' AND type = 'AP';
insert into permits_roles select id, '1' from permits where url = '/analysis/listprogressreports.htm' AND type = 'AP';

update reports set chart_type='PIE' where rep_type='CHART' and chart_type is null;
update permits set url = '/admin/.*questionnaireworkflow.*.htm' where url = '/admin/confirmclosequestionnaireworkflow.*.htm';

commit;
