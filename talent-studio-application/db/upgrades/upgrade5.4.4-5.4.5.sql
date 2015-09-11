--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table QUESTIONS add (WIDTH VARCHAR2(240),CELL_CLASS VARCHAR2(240));

commit;
