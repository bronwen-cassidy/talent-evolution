--------------------------------------------------------
-- 13/11/08
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table dynamic_attributes add has_helptext varchar2 (1) DEFAULT 'F';

-- make sure we update the table to reflect the questions with help text
DECLARE

CURSOR helptext_cur IS SELECT ID from help_text_items;
l_helptext help_text_items.id%type;

BEGIN

   OPEN helptext_cur;
   LOOP
      FETCH helptext_cur into l_helptext;
      EXIT WHEN helptext_cur%notfound;

      update dynamic_attributes set has_helptext='T' where id=l_helptext;

   END LOOP;

END;
/

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.2.1-5.2.1.sql', 29, 'dynamic attributes given a marker to check for help text');

-- compile fixed stored procs
@packages/zynap_node_spec.sql
@packages/zynap_node_body.sql
@packages/zynap_wf_integration_spec.sql
@packages/zynap_wf_integration_body.sql

ALTER PACKAGE ZYNAP_NODE_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_NODE_SP COMPILE BODY;
ALTER PACKAGE WF_INTEGRATION COMPILE SPECIFICATION;
ALTER PACKAGE WF_INTEGRATION COMPILE BODY;

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_myzynap_module_id(), 'ACCOUNT',-30, 'mi.my.portfolio',40,'/talentarena/viewmyportfolio.htm', 'myportfolio.menu.description', 'SYSTEMSUBJECT');
exec zynap_loader_sp.menu_permits_link;
commit;