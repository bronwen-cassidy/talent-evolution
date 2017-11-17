--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table home_pages add tab_view varchar2(255);

alter table HOME_PAGES add CONSTRAINT HP_TB_IN_CT CHECK (tab_view in ('PORTFOLIO','DASHBOARD','DETAILS'));

insert into versions(version) values('5.4.9-5.5.0');

commit;
