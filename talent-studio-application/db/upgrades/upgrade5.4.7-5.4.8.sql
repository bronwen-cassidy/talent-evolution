--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table QUE_WORKFLOWS add HR_USER_ID INTEGER;
alter table HOME_PAGES add FILE_EXTENSION VARCHAR2 (10);
alter table notifications add next_user_id number;

ALTER TABLE NOTIFICATIONS ADD (CONSTRAINT
  NTF_NEXT_USER_1_FK FOREIGN KEY
  (NEXT_USER_ID) REFERENCES USERS
  (ID))
/


ALTER TABLE QUE_WORKFLOWS ADD (CONSTRAINT
  QWW_HR_USER_1_FK FOREIGN KEY
  (HR_USER_ID) REFERENCES USERS
  (ID))
/

@views/zynap_views.sql;

@packages/zynap_wf_integration_spec.sql
@packages/zynap_wf_integration_body.sql

ALTER PACKAGE WF_INTEGRATION COMPILE SPECIFICATION;
ALTER PACKAGE WF_INTEGRATION COMPILE BODY;


commit;
