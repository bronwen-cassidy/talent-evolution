--------------------------------------------------------
--23/01/07
--------------------------------------------------------

PROMPT Updating Table 'APP_ROLES_USERS'
-- TS-551 Sequence to add home role to all users  (default role)
DECLARE
    cursor login_cur is SELECT * FROM LOGINS l WHERE NOT EXISTS (select * from APP_ROLES_USERS a where l.user_id = a.user_id and role_id = 8);
    l_id number;
    l_row logins%rowtype;
    BEGIN
        OPEN login_cur;
            LOOP
                FETCH login_cur into l_row;
                    EXIT WHEN login_cur%notfound;
                    INSERT INTO APP_ROLES_USERS values (l_row.user_id, 8);
            END LOOP;
        CLOSE login_cur;
    END;
/

-- objectives tables
-- todo check cascade drops constraints

alter table objectives drop constraint OBJECTIVES_PK;
alter table objectives drop constraint OBJ_TYPE_FK;
alter table objectives drop constraint OBJ_UNIT_FK;
alter table objectives drop constraint OBJ_IMPORTANCE_FK;
alter table objectives drop constraint OBJ_CREATED_BY_FK;

alter table objectives drop constraint OBJ_UPDATED_BY_FK;

alter table objective_sets drop constraint OBJ_SETS_SUB_FK;

drop table OBJECTIVES;
drop table OBJECTIVE_SETS;

PROMPT DROPPING Check Constraint on 'NODES'
ALTER TABLE NODES DROP CONSTRAINT AVCON_1147443622_NODE__000;
alter table nodes modify(node_type varchar2(3));

PROMPT Creating Check Constraint on 'NODES'
ALTER TABLE NODES ADD CONSTRAINT AVCON_1147443622_NODE__000 CHECK (NODE_TYPE IN ('P', 'Q', 'A', 'O', 'S', 'OBJ'));

PROMPT Creating Table 'OBJECTIVE_DEFINITIONS'
CREATE TABLE OBJECTIVE_DEFINITIONS
(
  ID NUMBER NOT NULL,
  LABEL VARCHAR2(512),
  STATUS VARCHAR2(20) DEFAULT 'OPEN',
  DESCRIPTION VARCHAR2(3000),
  DATE_CREATED DATE DEFAULT SYSDATE,
  DATE_PUBLISHED DATE,
  EXPIRY_DATE DATE
)
/

PROMPT Creating Table 'OBJECTIVE_SETS'
CREATE TABLE OBJECTIVE_SETS
(
  ID NUMBER NOT NULL,
  TYPE VARCHAR2(30) NOT NULL,
  OBJ_DEF_ID NUMBER NOT NULL,
  SUBJECT_ID NUMBER,
  ORG_UNIT_ID NUMBER,
  IS_APPROVED VARCHAR2(1) DEFAULT 'F'
)
/

PROMPT Creating Table 'OBJECTIVE_ATTRIBUTES'
CREATE TABLE OBJECTIVE_ATTRIBUTES
(
    DA_ID NUMBER NOT NULL,
    OBJ_DEF_ID NUMBER NOT NULL
)
/

PROMPT Creating Table 'OBJECTIVES'
CREATE TABLE OBJECTIVES
(
  NODE_ID NUMBER NOT NULL,
  LABEL VARCHAR2(512) NOT NULL,
  STATUS VARCHAR2(50),
  DESCRIPTION VARCHAR2(2500),
  OBJECTIVE_SET_ID NUMBER NOT NULL,
  DATE_CREATED DATE,
  DATE_UPDATED DATE,
  DATE_APPROVED DATE,
  CREATED_BY NUMBER NOT NULL,
  UPDATED_BY NUMBER,
  PARENT_ID NUMBER
)
/

PROMPT Creating Table 'OBJECTIVE_ASSESSMENTS'
CREATE TABLE OBJECTIVE_ASSESSMENTS
(
  ID NUMBER NOT NULL,
  SELF_COMMENTS VARCHAR2(4000),
  MANAGER_COMMENTS VARCHAR2(4000),
  IS_APPROVED VARCHAR2(1),
  OBJECTIVE_ID NUMBER NOT NULL,
  SELF_RATING_ID NUMBER,
  MANAGER_RATING_ID NUMBER,
  DATE_APPROVED DATE
)
/

-- objective constaints
PROMPT Creating Primary Key on 'OBJECTIVE_DEFINITIONS'
ALTER TABLE OBJECTIVE_DEFINITIONS
 ADD (CONSTRAINT OBJ_DEFS_PK PRIMARY KEY
  (ID))
/

PROMPT Creating Primary Key on 'OBJECTIVE_SETS'
ALTER TABLE OBJECTIVE_SETS
 ADD (CONSTRAINT OBJ_SETS_PK PRIMARY KEY
  (ID))
/

PROMPT Creating Primary Key on 'OBJECTIVES'
ALTER TABLE OBJECTIVES
 ADD (CONSTRAINT OBJECTIVES_PK PRIMARY KEY
  (NODE_ID))
/

PROMPT Creating Primary Key on 'OBJECTIVE_ASSESSMENTS'
ALTER TABLE OBJECTIVE_ASSESSMENTS
 ADD (CONSTRAINT OBJECTIVE_ASS_PK PRIMARY KEY
  (ID))
/

-- foreign keys

PROMPT Creating Foreign Key on 'OBJECTIVE_SETS'
ALTER TABLE OBJECTIVE_SETS ADD (CONSTRAINT
 OBJ_SET_DEF_FK FOREIGN KEY
  (OBJ_DEF_ID) REFERENCES OBJECTIVE_DEFINITIONS
  (ID) ON DELETE CASCADE)
/

PROMPT Creating Foreign Keys on 'OBJECTIVE_SETS'
ALTER TABLE OBJECTIVE_SETS ADD (CONSTRAINT
 OBJ_SETS_SUB_FK FOREIGN KEY
  (SUBJECT_ID) REFERENCES SUBJECTS
  (NODE_ID))
/

PROMPT Creating Foreign Keys on 'OBJECTIVE_SETS'
ALTER TABLE OBJECTIVE_SETS ADD (CONSTRAINT
 OBJ_SETS_OU_FK FOREIGN KEY
  (ORG_UNIT_ID) REFERENCES ORGANIZATION_UNITS
  (NODE_ID))
/

PROMPT Creating Foreign Key on 'OBJECTIVES'
ALTER TABLE OBJECTIVES ADD (CONSTRAINT
 OBJ_NODE_FK FOREIGN KEY
  (NODE_ID) REFERENCES NODES
  (ID))
/

PROMPT Creating Foreign Key on 'OBJECTIVES'
ALTER TABLE OBJECTIVES ADD (CONSTRAINT
 OBJ_P_OBJ_FK FOREIGN KEY
  (PARENT_ID) REFERENCES OBJECTIVES
  (NODE_ID) ON DELETE CASCADE)
/

PROMPT Creating Foreign Keys on 'OBJECTIVES'
ALTER TABLE OBJECTIVES ADD (CONSTRAINT
 OBJECTIVES_SET_FK FOREIGN KEY
  (OBJECTIVE_SET_ID) REFERENCES OBJECTIVE_SETS
  (ID))
/

ALTER TABLE OBJECTIVES ADD (CONSTRAINT
 OBJ_CREATED_BY_FK FOREIGN KEY
  (CREATED_BY) REFERENCES USERS
  (ID))
/

ALTER TABLE OBJECTIVES ADD (CONSTRAINT
 OBJ_UPDATED_BY_FK FOREIGN KEY
  (UPDATED_BY) REFERENCES USERS
  (ID))
/

PROMPT Creating Foreign Keys on 'OBJECTIVE_ASSESSEMENTS'
ALTER TABLE OBJECTIVE_ASSESSMENTS ADD (CONSTRAINT
 OBJ_ASS_RAT_FK FOREIGN KEY
  (SELF_RATING_ID) REFERENCES LOOKUP_VALUES
  (ID))
/

ALTER TABLE OBJECTIVE_ASSESSMENTS ADD (CONSTRAINT
 OBJ_ASS_MAN_RAT_FK FOREIGN KEY
  (MANAGER_RATING_ID) REFERENCES LOOKUP_VALUES
  (ID))
/

ALTER TABLE OBJECTIVE_ASSESSMENTS ADD (CONSTRAINT
 OBJ_ASS_OBJ_FK FOREIGN KEY
  (OBJECTIVE_ID) REFERENCES OBJECTIVES
  (NODE_ID))
/

PROMPT Creating Foreign Key on 'OBJECTIVE_ATTRIBUTES'
ALTER TABLE OBJECTIVE_ATTRIBUTES ADD (CONSTRAINT
 OBJ_ATTR_DA_FK FOREIGN KEY
  (DA_ID) REFERENCES DYNAMIC_ATTRIBUTES
  (ID))
/

PROMPT Creating Foreign Key on 'OBJECTIVE_ATTRIBUTES'
ALTER TABLE OBJECTIVE_ATTRIBUTES ADD (CONSTRAINT
 OBJ_ATTR_OBJ_FK FOREIGN KEY
  (OBJ_DEF_ID) REFERENCES OBJECTIVE_DEFINITIONS
  (ID) ON DELETE CASCADE)
/

-- drop lookup_types
delete from lookup_values where type_id='OBJ_TYPE';
delete from lookup_types where id='OBJ_TYPE';

delete from lookup_values where type_id='OBJ_IMPORTANCE';
delete from lookup_types where id='OBJ_IMPORTANCE';

delete from lookup_values where type_id='OBJ_TARGET_UNITS';
delete from lookup_types where id='OBJ_TARGET_UNITS';

-- lookup_types and values and attributes fro objectives
-- objective goal
EXEC zynap_lookup_sp.install_type( 'OBJ_TYPE', 'SYSTEM', 'Objective goal types', 'Goal Type');
EXEC zynap_lookup_sp.install_values( 'OBJ_TYPE', 'PERFORMANCE', 'Performance', 'Performance', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_TYPE', 'LEARNING', 'Learning', 'Learning Goal Type', 10, FALSE);

-- priority
EXEC zynap_lookup_sp.install_type( 'OBJ_IMPORTANCE', 'SYSTEM', 'Objective priority ratings', 'Priority');
EXEC zynap_lookup_sp.install_values( 'OBJ_IMPORTANCE', 'A', 'A', 'A', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_IMPORTANCE', 'B', 'B', 'B', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_IMPORTANCE', 'C', 'C', 'C', 30, FALSE);

-- Leadership Style
EXEC zynap_lookup_sp.install_type( 'OBJ_LEADERSHIP', 'SYSTEM', 'Objective Leadership Style', 'Leadership Style');
EXEC zynap_lookup_sp.install_values( 'OBJ_LEADERSHIP', 'DIRECTING', 'Directing', 'The Leadership provides specific direction and closely monitors task accomplishment', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_LEADERSHIP', 'COACHING', 'Coaching', 'The leader continues to direct and closely monitor task accomplishment, but also explains decisions, solicits suggestions, and supports progress.', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_LEADERSHIP', 'SUPPORTING', 'Supporting', 'The leader facilitates and supports people''s efforts toward task accomplishment and shares responsibility for decision-making with them.', 30, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_LEADERSHIP', 'DELEGATING', 'Delegating', 'The leader turns over responsibility for decision-making and problem-solving to people.', 40, FALSE);

-- goal rating
EXEC zynap_lookup_sp.install_type( 'OBJ_RATING', 'SYSTEM', 'Goal Rating', 'Rating');
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'NONE', 'None', 'ONone', 1, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'OUTSTANDING', 'Outstanding', 'Outstanding Performance', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'EXCEEDED', 'Exceeded', 'Exceeded Goal Requirements', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'MET_EXPECTATIONS', 'Met Expectations', 'Met Expectations', 30, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'FELL_BELOW', 'Fell Below', 'Fell Below Expectations', 40, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_RATING', 'UNACCEPTABLE', 'Unacceptable', 'Unacceptable', 50, FALSE);


-- objective dynamic attributes
EXEC zynap_da_sp.add_attribute('Goal', 'STRUCT', 'OBJ', NULL, NULL, 'F', 'T', 'F', 'OBJ_TYPE', 'Objective Goal');
EXEC zynap_da_sp.add_attribute('Priority', 'STRUCT', 'OBJ', NULL, NULL, 'F', 'T', 'F', 'OBJ_IMPORTANCE', 'Objective Priority');
EXEC zynap_da_sp.add_attribute('Leadership Style', 'STRUCT', 'OBJ', NULL, NULL, 'F', 'T', 'F', 'OBJ_LEADERSHIP', 'Leadership Style');
EXEC zynap_da_sp.add_attribute('% Time', 'NUMBER', 'OBJ', NULL, NULL, 'F', 'T', 'F', null, 'Objective Target Times');

-- set modified label
update DYNAMIC_ATTRIBUTES set modified_label = replace(trim(lower(label)),' ');

INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_admin_module_id(),'OBJECTIVES', 'Corporate Objectives', 80, 'menu.htm');
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'OBJECTIVES',-16, 'Corporate Objectives', 10, '/admin/listcorporateobjectives.htm', 'list.corporate.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'OBJECTIVES',-17, 'Organisation Objectives', 10, '/admin/listorgunitobjectives.htm', 'list.oganisation.objectives.menu.description', null);

-- Permits for corporate objectives
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','OBJECTIVES' ,'Permission to add/edit corporate objectives', 'T','/admin/.*objectives.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','OBJECTIVES' ,'Permission to list corporate objectives', 'T','/admin/listcorporateobjectives.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','OBJECTIVES' ,'Permission to list corporate objectives', 'T','/admin/listorgunitobjectives.htm', null, null, null);


DECLARE
    l_id number;
    ld_id number;
    ldd_id number;
    BEGIN
        select id into l_id from permits where url='/admin/listcorporateobjectives.htm';
        insert into permits_roles (permit_id, role_id) values (l_id, 1);

        select id into ld_id from permits where url='/admin/listorgunitobjectives.htm';
        insert into permits_roles (permit_id, role_id) values (ld_id, 1);

        select id into ldd_id from permits where url='/admin/.*objectives.htm';
        insert into permits_roles (permit_id, role_id) values (ldd_id, 1);

    END;
/

exec zynap_loader_sp.menu_permits_link

commit;

prompt !!!!! NB NB NB [DO NOT RUN THE NEXT UPGRADE SCRIPT WITHOUT FIRST RUNNING THE QUESTIONNAIRE UPGRADER JAVA TOOL] NB NB NB !!!! 
