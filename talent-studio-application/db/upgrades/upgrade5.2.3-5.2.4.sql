--------------------------------------------------------
-- 29/01/09
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

update permits set url='/perfman/.*performancereviews.htm' where url='/perfman/listperformancereviews.htm';
update permits set url='/perfman/.*performancereview.htm' where url='/perfman/viewperformancereview.htm';

alter table preferences add is_secure varchar2(1) default 'F'; 

@packages/zynap_auth_spec.sql
@packages/zynap_auth_body.sql

ALTER PACKAGE zynap_auth_sp COMPILE SPECIFICATION;
ALTER PACKAGE zynap_auth_sp COMPILE BODY;

commit;