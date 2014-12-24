--------------------------------------------------------
-- 31/10/08
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table subjects add current_job_info varchar2(2048);
alter table positions add current_holder_info varchar2(2048);

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.2.0-5.2.1.sql', 14, 'subject and position tables altered');

@packages/zynap_node_spec.sql
@packages/zynap_node_body.sql

ALTER PACKAGE ZYNAP_NODE_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_NODE_SP COMPILE BODY;

-------------------------------------------------------
-- populate current job and current holder info
-------------------------------------------------------
DECLARE

CURSOR assoc_cur IS SELECT * from subject_primary_associations;
assoc_rec subject_primary_associations%rowtype;

BEGIN

   FOR assoc_rec IN assoc_cur LOOP
        zynap_node_sp.update_current_job_info(assoc_rec.subject_id);
        zynap_node_sp.update_current_holder_info(assoc_rec.position_id);
   END LOOP;

END;
/

@packages/zynap_triggers_hierarchy.sql;


commit;