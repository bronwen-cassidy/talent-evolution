--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table QUESTIONS add CELL_STYLE VARCHAR2(2000);
alter table QUESTIONS add QUESTION_STYLE VARCHAR2(2000);
alter table QUESTION_LINE_ITEMS add ROW_STYLE VARCHAR2(2000);
alter table QUESTION_LINE_ITEMS add HEADER_STYLE VARCHAR2(2000);
alter table QUESTION_LINE_ITEMS add FOOTER_STYLE VARCHAR2(2000);

commit;
