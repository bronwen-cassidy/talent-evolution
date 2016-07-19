--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

drop table UPGRADE_STATUS;

create table versions
( version varchar2(100) not null, upgrade_date date default sysdate )
/

alter table QUE_WORKFLOWS add HR_USER_ID INTEGER;
alter table HOME_PAGES add FILE_EXTENSION VARCHAR2 (10);

alter table notifications add VERIFIED VARCHAR2(1) DEFAULT 'F';
alter table notifications add APPROVED VARCHAR2(1) DEFAULT 'F';
alter table notifications add hr_id number;
alter table notifications add managers_manager_id number;

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

insert into versions(version) values('5.4.7-5.4.8');

commit;
