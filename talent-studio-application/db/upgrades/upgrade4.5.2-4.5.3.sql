--------------------------------------------------------
--30/11/06 11:25
--------------------------------------------------------
-- new column under logins to indicate the number of failed attempts
alter table logins add FAILED_LOGIN_ATTEMPTS NUMBER(10) DEFAULT 0;
update LOGINS set failed_login_attempts=0;

-- new configuration rules to indicate number of login failed attempts
INSERT INTO configs (id, label, comments, is_active) VALUES (-3, 'Login Rules', 'Groups all the rules the system that apply to logging into the system', 'T');
ALTER TABLE rules ADD (MIN_VALUE INTEGER, MAX_VALUE INTEGER);
INSERT INTO rules (id, config_id, label, description, class, type, is_active, value, min_value, max_value) VALUES (-30, -3, 'Login Attempts', 'The number of attempts to login before the system locks you out', 'com.zynap.domain.rules.IsAlpha', 'NUMBER', 'T', '3', '3', '9');

-- recompile package
ALTER PACKAGE ZYNAP_AUTH_SP COMPILE BODY;


--------------------------------------------------------
-- 05/12/06 09:38
--------------------------------------------------------
ALTER table performance_reviews add (IS_NOTIFIABLE VARCHAR2(1) DEFAULT 'F');

ALTER TABLE LOOKUP_VALUES MODIFY SHORT_DESC VARCHAR2 (500);


-------------------------------------------------------------
-- 08/01/07
-------------------------------------------------------------
ALTER TABLE QUE_WORKFLOWS ADD MANAGER_READ VARCHAR2(1) DEFAULT 'F';
ALTER TABLE QUE_WORKFLOWS ADD MANAGER_WRITE VARCHAR2(1) DEFAULT 'F';
ALTER TABLE QUE_WORKFLOWS ADD INDIVIDUAL_READ VARCHAR2(1) DEFAULT 'F';
ALTER TABLE QUE_WORKFLOWS ADD INDIVIDUAL_WRITE VARCHAR2(1) DEFAULT 'F';

UPDATE QUE_WORKFLOWS SET INDIVIDUAL_WRITE = 'T' WHERE WORKFLOW_TYPE = 'INFO_FORM';
UPDATE QUE_WORKFLOWS SET INDIVIDUAL_READ='T' WHERE WORKFLOW_TYPE = 'INFO_FORM';
UPDATE QUE_WORKFLOWS SET MANAGER_READ='T' WHERE WORKFLOW_TYPE = 'INFO_FORM';
UPDATE QUE_WORKFLOWS SET MANAGER_WRITE='T' WHERE WORKFLOW_TYPE = 'INFO_FORM';

alter table node_das add ADDED_BY NUMBER(38);
alter table node_das add DATE_ADDED DATE;

ALTER TABLE NODE_DAS ADD (CONSTRAINT
 USER_BLOG_ADDED_FK FOREIGN KEY
  (ADDED_BY) REFERENCES USERS
  (ID))
/

@packages/zynap_loader_spec.sql
@packages/zynap_loader_body.sql

-- recompile package
ALTER PACKAGE ZYNAP_LOADER_SP COMPILE BODY;
