--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

-- minimum info required to add a new tab
insert into display_config_items (ID, DISPLAY_CONFIG_ID, LABEL, is_active, content_type, hideable, sort_order, roles_modifiable)
VALUES (-65, -2, 'Progress Reports', 'F', 'PROGRESS_REPORT', 'T', 10, 'T');

INSERT INTO display_config_roles (DISPLAY_CONFIG_ITEM_ID, role_id) VALUES (-65, 1);

-- personal tab of progress reports
insert into display_config_items (ID, DISPLAY_CONFIG_ID, LABEL, is_active, content_type, hideable, sort_order, roles_modifiable)
VALUES (-66, -5, 'My Progress Reports', 'F', 'PROGRESS_REPORT', 'T', 10, 'T');

INSERT INTO display_config_roles (DISPLAY_CONFIG_ITEM_ID, role_id) VALUES (-66, 8);

CREATE TABLE REPORT_PARTICIPANTS
(
  REPORT_ID INTEGER NOT NULL,
  SUBJECT_ID INTEGER NOT NULL
)
/
ALTER TABLE REPORT_PARTICIPANTS
 ADD CONSTRAINT RPP_PK
 PRIMARY KEY (SUBJECT_ID, REPORT_ID)
 ENABLE
 VALIDATE
/
ALTER TABLE REPORT_PARTICIPANTS
 ADD CONSTRAINT RPRPP_FK_ID
 FOREIGN KEY (REPORT_ID)
 REFERENCES REPORTS (ID) ON DELETE CASCADE
/
ALTER TABLE REPORT_PARTICIPANTS
 ADD CONSTRAINT RPPT_NODE_FK_ID
 FOREIGN KEY (SUBJECT_ID)
 REFERENCES NODES (ID) ON DELETE CASCADE
/

commit;
