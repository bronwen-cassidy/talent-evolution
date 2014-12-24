--------------------------------------------------------
-- 30/07/07
--------------------------------------------------------
CREATE TABLE OBJECTIVE_ASSESSORS
(
   USER_ID NUMBER NOT NULL
   ,OBJECTIVE_ID NUMBER NOT NULL
)
/

PROMPT Creating Primary Key on 'OBJECTIVE_ASSESSORS'
ALTER TABLE OBJECTIVE_ASSESSORS
 ADD (CONSTRAINT OBJ_ASS_RS_PK PRIMARY KEY
  (USER_ID, OBJECTIVE_ID))
/

PROMPT Creating Foreign Keys on 'OBJECTIVE_ASSESSORS'
ALTER TABLE OBJECTIVE_ASSESSORS ADD (CONSTRAINT
 OBJ_USRS_1_FK FOREIGN KEY
  (USER_ID) REFERENCES USERS
  (ID))
/

ALTER TABLE OBJECTIVE_ASSESSORS ADD (CONSTRAINT
 OBJ_ASSRS_OBJS_1_FK FOREIGN KEY
  (OBJECTIVE_ID) REFERENCES OBJECTIVES
  (NODE_ID))
/

PROMPT ALTERING 'OBJECTIVE_ASSESSMENTS' ADD USER
ALTER TABLE OBJECTIVE_ASSESSMENTS ADD ASSESSED_BY_ID NUMBER;

ALTER TABLE OBJECTIVE_ASSESSMENTS ADD (CONSTRAINT
  OBJ_ASS_USR_FK_2 FOREIGN KEY
  (ASSESSED_BY_ID) REFERENCES USERS
  (ID))
/

commit;
                                                                                                                               

ALTER TABLE OBJECTIVES ADD APPEAR_TODO VARCHAR2(5) DEFAULT 'T';
ALTER TABLE OBJECTIVE_SETS ADD APPEAR_TODO VARCHAR2(5) DEFAULT 'T';

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES (zynap_app_sp.get_myzynap_module_id(), 'TODOS', -36, 'mi.assessments', 30, '/talentarena/worklistassessments.htm', 'worklist.assessment.description', null);

-- rebuild home role
delete from permits_roles where role_id = 8;
insert into permits_roles select id, '8' from permits where url like '%talentarena%' AND type = 'AP';

exec zynap_loader_sp.menu_permits_link;

@packages/zynap_org_unit_spec.sql
@packages/zynap_org_unit_body.sql

-- recompile package
ALTER PACKAGE zynap_org_unit_SP COMPILE SPECIFICATION;
ALTER PACKAGE zynap_org_unit_SP COMPILE BODY;

delete from menu_items where preference_id is not null;
delete from preferences;

commit;
