--------------------------------------------------------
-- 17/01/08
--------------------------------------------------------
-- add a new column to the report_columns table then write a stored proc to post apply true's to dynamic_line_items.

alter table report_columns add is_dynamic_lineitem char(1) default 'F';

DECLARE

CURSOR report_cols_cur IS SELECT * from report_columns where que_wf_id is not null;

l_is_dynamic varchar(1);
l_subbed_val number;
l_report_col report_columns%rowtype;
l_string_count number;

BEGIN
   OPEN report_cols_cur;

       LOOP
           FETCH report_cols_cur into l_report_col;
           EXIT WHEN report_cols_cur%notfound;

           --dbms_output.put_line(l_report_col.ref_value);
           l_string_count:= instr(l_report_col.ref_value, '.', -1);

           if instr(l_report_col.ref_value, '.', -1) > 0 then
               l_subbed_val:= to_number(substr(l_report_col.ref_value, l_string_count + 1));
               --dbms_output.put_line(l_subbed_val);
               select is_dynamic into l_is_dynamic from dynamic_attributes where id=l_subbed_val;
           else
               select is_dynamic into l_is_dynamic from dynamic_attributes where id=to_number(l_report_col.ref_value);
           end if;

           update report_columns set is_dynamic_lineitem = l_is_dynamic where id=l_report_col.id;

       END LOOP;

   CLOSE report_cols_cur;
END;
/

-------------------------------------------------
-- list questionnaires menu item
-------------------------------------------------

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description)
VALUES(zynap_app_sp.get_admin_module_id(), 'QUESTIONNAIRES', -18, 'mi.questionnaire.workflows', 20, '/admin/listqueworkflows.htm', 'list.questionnaire.workflow.menu.description');

delete from permits_roles where permit_id in (select id from permits where url like '%editmy.*questionnaire.htm');
delete from permits where url like '%editmy.*questionnaire.htm';

insert into permits (ID,TYPE,ACTION,CONTENT,DESCRIPTION,IS_ACTIVE,URL)
values (PERMIT_SQ.nextval,'AP','edit','QUESTIONNAIRES','Permission to Edit My Questionnaires','T','/talentarena/editmy.*questionnaire.htm');

insert into permits (ID,TYPE,ACTION,CONTENT,DESCRIPTION,IS_ACTIVE,URL)
values (PERMIT_SQ.nextval,'AP','edit','QUESTIONNAIRES','Permission to View/Edit Questionnaire Workflows','T','/admin/listqueworkflows.htm');

exec zynap_loader_sp.menu_permits_link;

-- rebuild admin/home role permits
DECLARE
    l_id number;

    BEGIN
        select id into l_id from permits where url='/talentarena/editmy.*questionnaire.htm';
        insert into permits_roles (permit_id, role_id) values (l_id, 1);
        insert into permits_roles (permit_id, role_id) values (l_id, 8);
        select id into l_id from permits where url='/admin/listqueworkflows.htm';
        insert into permits_roles (permit_id, role_id) values (l_id, 1);
    END;
/

----------------------------------------------------------------------
-- locking of nodes
----------------------------------------------------------------------
alter table organization_units drop column lock_id;
alter table nodes add (lock_id number default -1);

---------------------------------------------------------------------
-- questionnaire locked
---------------------------------------------------------------------

alter table questionnaires add (locked varchar2(1) default 'F');

---------------------------------------------------------------------
-- questionnaire locked by
---------------------------------------------------------------------
alter table questionnaires add (LOCKED_BY INTEGER);

----------------------------------------------------------------------
-- recompile the loaders
-----------------------------------------------------------------------
@upgrades/zynap_loader_spec_4.6.3-4.6.4.sql
@upgrades/zynap_loader_body_4.6.3-4.6.4.sql
@upgrades/zynap_org_unit_spec_4.6.3-4.6.4.sql
@upgrades/zynap_org_unit_body_4.6.3-4.6.4.sql


-- recompile package
ALTER PACKAGE ZYNAP_LOADER_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_LOADER_SP COMPILE BODY;
ALTER PACKAGE ZYNAP_ORG_UNIT_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_ORG_UNIT_SP COMPILE BODY;

commit;
