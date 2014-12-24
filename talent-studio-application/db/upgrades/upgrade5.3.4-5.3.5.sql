--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

-- compile in the stored procedures
@packages/zynap_auth_spec.sql
@packages/zynap_auth_body.sql

ALTER PACKAGE zynap_auth_sp COMPILE SPECIFICATION;
ALTER PACKAGE zynap_auth_sp COMPILE BODY;

--ALTER TABLE LOOKUP_VALUES
-- ADD (CONSTRAINT LVSD_LT_UK UNIQUE
--  (SHORT_DESC, TYPE_ID))
--/

drop table SCHEMA_DESCRIPTOR_TB;
drop table TRACE_TABLE;

commit;
