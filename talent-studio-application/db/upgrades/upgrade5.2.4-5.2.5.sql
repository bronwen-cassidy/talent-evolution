--------------------------------------------------------
-- 29/01/09
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table metrics add value varchar2(2000);
alter table roles add arena_id varchar2(2000);

update roles set arena_id='ADMINMODULE' where id=1;
update roles set arena_id='ANALYSISMODULE' where id=6;
update roles set arena_id='MYZYNAPMODULE' where id=8;
update roles set arena_id='ORGANISATIONMODULE' where id=3;
update roles set arena_id='PERFMANMODULE' where id=7;
update roles set arena_id='TALENTIDENTIFIERMODULE' where id=4;
update roles set arena_id='SUCCESSIONMODULE' where id=5;

drop trigger PRIMARY_ASSOC_INFO;

--load the trigger file in order to re-compile them
@packages/zynap_triggers_hierarchy.sql;
@packages/zynap_node_body.sql;

ALTER PACKAGE zynap_node_sp COMPILE BODY;

commit;