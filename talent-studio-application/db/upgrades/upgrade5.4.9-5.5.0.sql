--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table home_pages add tab_view varchar2(255);

alter table HOME_PAGES add CONSTRAINT HP_TB_IN_CT CHECK (tab_view in ('PORTFOLIO','DASHBOARD','DETAILS'));

insert into CORE_DETAILS (id, first_name, second_name)
values(-10, 'SU', 'ROOT');

insert into users (id, cd_id, user_type, is_active, is_root)
values (-10, -10, 'SYSTEM', 'T', 'T');

insert into populations (id, user_id, LABEL, scope, description, type)
values (-10, -10, 'Dummy Population', 'Private', 'Population for individual dashboards', 'S');

insert into versions(version) values('5.4.9-5.5.0');

commit;
