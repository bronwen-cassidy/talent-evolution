--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '


ALTER TABLE LOOKUP_VALUES ADD IS_BLANK VARCHAR2(1) DEFAULT 'F';
ALTER TABLE QUESTIONS ADD SORT_ORDER INTEGER;

update questions set sort_order = id where sort_order is null;

commit;
