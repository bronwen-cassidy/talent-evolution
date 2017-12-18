--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table home_pages add tab_view varchar2(255);

--alter table HOME_PAGES add CONSTRAINT HP_TB_IN_CT CHECK (tab_view in ('PORTFOLIO','DASHBOARD','DETAILS'));
alter table que_workflows add parent_id NUMBER;

update que_workflows qw set parent_id = (select pqw.id from que_workflows pqw where pqw.label = qw.parent_label);

insert into CORE_DETAILS (id, first_name, second_name)
values(-10, 'SU', 'ROOT');

insert into users (id, cd_id, user_type, is_active, is_root)
values (-10, -10, 'SYSTEM', 'T', 'T');

insert into populations (id, user_id, LABEL, scope, description, type)
values (-10, -10, 'Dummy Population', 'Private', 'Population for individual dashboards', 'S');

alter table QUE_WORKFLOWS add created_date DATE default sysdate;

-- next session
insert into DYNAMIC_ATTRIBUTES(id, label, MODIFIED_LABEL, TYPE, ARTEFACT_TYPE,UNIQUE_NUMBER) VALUES (-222, 'Published Date', 'PUBDATE12', 'DATE', 'S', -111222);

insert into versions(version) values('5.4.9-5.5.0');

commit;
