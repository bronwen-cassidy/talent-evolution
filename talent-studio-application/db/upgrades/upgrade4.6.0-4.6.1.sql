--------------------------------------------------------
-- 04/09/07
--------------------------------------------------------
ALTER TABLE QUESTIONS ADD MANAGER_WRITE VARCHAR2(1) DEFAULT 'F';
ALTER TABLE QUESTION_LINE_ITEMS ADD CAN_DISABLE VARCHAR2(1) DEFAULT 'F';
ALTER TABLE NODE_DAS ADD DISABLED VARCHAR2(1) DEFAULT 'F';

--------------------------------------------------------
-- 13/09/07
--------------------------------------------------------
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_myzynap_module_id(),'MESSAGES','my.messages',25, 'menu.htm');
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES (zynap_app_sp.get_myzynap_module_id(), 'MESSAGES', -37, 'mi.inbox', 30, '/talentarena/worklistmessages.htm', 'worklist.messages.description', 'SYSTEMSUBJECT');

-- rebuild the home arena permits
DELETE FROM PERMITS_ROLES WHERE ROLE_ID = 8;
INSERT INTO PERMITS_ROLES SELECT ID, '8' FROM PERMITS WHERE URL LIKE '%talentarena%' AND TYPE = 'AP';
exec zynap_loader_sp.menu_permits_link;

PROMPT Creating Table 'INBOX'
CREATE TABLE INBOX
(
  ID NUMBER NOT NULL,
  LABEL VARCHAR2(500) NOT NULL,
  STATUS VARCHAR2(20) DEFAULT 'UNREAD',
  FROM_USER_ID NUMBER NOT NULL,
  TO_USER_ID NUMBER NOT NULL,
  QUESTIONNIRE_ID NUMBER,
  OBJECTIVE_ID NUMBER,
  DATE_RECEIVED DATE,
  TYPE VARCHAR2(50),
  VIEW_TYPE VARCHAR2(100)
)
/

PROMPT Creating Primary Key on 'INBOX'
ALTER TABLE INBOX
 ADD (CONSTRAINT INBOX_PK PRIMARY KEY
  (ID))
/

PROMPT Creating Check Constraint on 'INBOX'
ALTER TABLE INBOX
 ADD (CONSTRAINT AVCON_3346657_STATUS_012 CHECK (STATUS IN ('READ', 'UNREAD')))
/

PROMPT Creating Foreign Key on 'INBOX FROM USER'
ALTER TABLE INBOX ADD (CONSTRAINT
 INBOX_USER_1_FK FOREIGN KEY
  (FROM_USER_ID) REFERENCES USERS
  (ID))
/

PROMPT Creating Foreign Key on 'INBOX TO USER'
ALTER TABLE INBOX ADD (CONSTRAINT
 INBOX_USER_2_FK FOREIGN KEY
  (TO_USER_ID) REFERENCES USERS
  (ID))
/

PROMPT Creating Foreign Key on 'INBOX QUESTIONNAIRES'
ALTER TABLE INBOX ADD (CONSTRAINT
 INBOX_QUE_1_FK FOREIGN KEY
  (QUESTIONNIRE_ID) REFERENCES QUESTIONNAIRES
  (NODE_ID))
/

PROMPT Creating Foreign Key on 'INBOX OBJECTIVES'
ALTER TABLE INBOX ADD (CONSTRAINT
 INBOX_OBJ_1_FK FOREIGN KEY
  (OBJECTIVE_ID) REFERENCES OBJECTIVES
  (NODE_ID))
/

PROMPT Creating Index ON 'INBOX FROM USERS'
CREATE INDEX INBX_FM_USR_FK_I ON INBOX
 (FROM_USER_ID)
/

PROMPT Creating Index ON 'INBOX TO USERS'
CREATE INDEX INBX_TO_USR_FK_2 ON INBOX
 (TO_USER_ID)
/

PROMPT Creating Index ON 'INBOX QUESTIONNAIRE'
CREATE INDEX INBX_QUE_FK_1 ON INBOX
 (QUESTIONNIRE_ID)
/

PROMPT Creating Index ON 'INBOX OBJECTIVE'
CREATE INDEX INBX_OBJ_FK_2 ON INBOX
 (OBJECTIVE_ID)
/

PROMPT Creating Sequence 'MESSAGES_SQ'
CREATE SEQUENCE MESSAGES_SQ
    NOMAXVALUE NOMINVALUE NOCYCLE
/

--------------------------------------------------------
-- 17/09/07
--------------------------------------------------------
update permits set url = '/talentarena/editmy.*questionnaire.htm' where url = '/talentarena/editmyquestionnaire.htm';

commit;