--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

-- duble dynamic attributes

EXEC zynap_lookup_sp.install_values( 'DATYPE', 'DOUBLE', 'Double', 'Double Number', 3, TRUE);

alter table dynamic_attributes add DECIMAL_PLACES INTEGER;

-- recompile zynap_auth packages
@packages/zynap_auth_body.sql
@packages/zynap_permit_spec.sql
@packages/zynap_permit_body.sql

-- recompile procedures
ALTER PACKAGE ZYNAP_AUTH_SP COMPILE BODY;
ALTER PACKAGE ZYNAP_PERMIT_SP COMPILE BOTH;

commit;
