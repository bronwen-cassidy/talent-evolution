--------------------------------------------------------
-- 08/01/09
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

create table AUDITS (
    ID number not null
   ,MODIFIED_BY_ID number
   ,MODIFIED_BY_USERNAME varchar2(2000)
   ,OBJECT_ID number not null
   ,MODIFIED_DATE DATE
   ,TABLE_NAME varchar2(255)
   ,ACTION_PERFORMED varchar2(500)
   ,DESCRIPTION varchar2(2000)
   ,SERIALIZED_OBJECT BLOB
)
/

CREATE SEQUENCE AUDITS_SQ
    NOMAXVALUE NOMINVALUE NOCYCLE
/

ALTER TABLE AUDITS
 ADD (CONSTRAINT AUDITS_PK PRIMARY KEY
  (ID))
/

@packages/zynap_position_spec.sql
@packages/zynap_position_body.sql
@packages/zynap_org_unit_spec.sql
@packages/zynap_org_unit_body.sql

ALTER PACKAGE zynap_position_sp COMPILE SPECIFICATION;
ALTER PACKAGE zynap_position_sp COMPILE BODY;
ALTER PACKAGE zynap_org_unit_sp COMPILE SPECIFICATION;
ALTER PACKAGE zynap_org_unit_sp COMPILE BODY;

commit;