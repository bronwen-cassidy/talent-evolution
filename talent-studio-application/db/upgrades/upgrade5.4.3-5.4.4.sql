--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

-- remember to check is_ounit_default on DR roles

-- orgunit root tracking
alter TABLE ORGANIZATION_UNITS ADD ROOT_ID NUMBER;
alter TABLE ORGANIZATION_UNITS ADD IS_COMPANY_ROOT_ID VARCHAR2(1) DEFAULT 'F';

@packages/zynap_auth_body.sql
ALTER PACKAGE ZYNAP_AUTH_SP COMPILE BODY;

CREATE OR REPLACE TRIGGER "ADD_NODE_PT_USER_SESSION" AFTER
INSERT
ON "NODE_AUDITS" FOR EACH ROW BEGIN
     insert /*+ APPEND */ into USER_NODE_DOMAIN_PERMITS (USER_ID,NODE_ID,PERMIT_ID)
     select USER_ID,NODE_ID,PERMIT_ID from ZYNAP_USER_DOMAIN_PERMITS dp WHERE NODE_ID = :NEW.NODE_ID;
     --and EXISTS (SELECT 1 from USER_NODE_DOMAIN_PERMITS WHERE USER_ID = dp.USER_ID);
END ADD_OU_PT_USER_SESSION;
/


commit;
