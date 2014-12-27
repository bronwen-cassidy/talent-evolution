--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

ALTER TABLE LOOKUP_VALUES ADD REQUIRES VARCHAR2(1000);
ALTER TABLE LOOKUP_VALUES ADD LINK_ID VARCHAR2(1000);

@views/zynap_views.sql

commit;
