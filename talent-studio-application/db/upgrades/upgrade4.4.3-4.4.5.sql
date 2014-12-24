CREATE TABLE OBJECTIVE_SETS
(
  ID NUMBER NOT NULL,
  LABEL VARCHAR2(50),
  STATUS VARCHAR2(20) NOT NULL,
  SUBJECT_ID NUMBER NOT NULL,
  DATE_CREATED DATE NOT NULL,
  DATE_APPROVED DATE
)
/

ALTER TABLE OBJECTIVE_SETS
 ADD (CONSTRAINT OBJ_SETS_PK PRIMARY KEY
  (ID))
/

ALTER TABLE OBJECTIVE_SETS ADD (CONSTRAINT
 OBJ_SETS_SUB_FK FOREIGN KEY
  (SUBJECT_ID) REFERENCES SUBJECTS
  (NODE_ID))
/

ALTER TABLE OBJECTIVE_SETS
 ADD (CONSTRAINT OBJ_SET_LABEL UNIQUE
  (LABEL))
/

CREATE TABLE OBJECTIVES
(
  ID NUMBER NOT NULL,
  LABEL VARCHAR2(50) NOT NULL,
  STATUS VARCHAR2(20),
  DESCRIPTION VARCHAR2(500),
  TYPE NUMBER NOT NULL,
  TARGET NUMBER,
  UNIT NUMBER,
  IMPORTANCE NUMBER, 
  OBJECTIVE_SET_ID NUMBER NOT NULL,
  DATE_CREATED DATE NOT NULL,
  DATE_UPDATED DATE,
  CREATED_BY INTEGER NOT NULL,
  UPDATED_BY INTEGER
)
/

ALTER TABLE OBJECTIVES
 ADD (CONSTRAINT OBJECTIVES_PK PRIMARY KEY
  (ID))
/

--ALTER TABLE OBJECTIVES
-- ADD (CONSTRAINT OBJECTIVES_LABEL UNIQUE
--  (LABEL))
--/

ALTER TABLE OBJECTIVES ADD (CONSTRAINT
 OBJECTIVES_SET_FK FOREIGN KEY
  (OBJECTIVE_SET_ID) REFERENCES OBJECTIVE_SETS
  (ID))
/

ALTER TABLE OBJECTIVES ADD (CONSTRAINT
 OBJ_TYPE_FK FOREIGN KEY
  (TYPE) REFERENCES LOOKUP_VALUES
  (ID))
/

ALTER TABLE OBJECTIVES ADD (CONSTRAINT
 OBJ_UNIT_FK FOREIGN KEY
  (UNIT) REFERENCES LOOKUP_VALUES
  (ID))
/

ALTER TABLE OBJECTIVES ADD (CONSTRAINT
 OBJ_IMPORTANCE_FK FOREIGN KEY
  (IMPORTANCE) REFERENCES LOOKUP_VALUES
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

CREATE SEQUENCE OBJECTIVE_SET_SQ
 NOMAXVALUE
 NOMINVALUE
 NOCYCLE
/

CREATE SEQUENCE OBJECTIVE_SQ
 NOMAXVALUE
 NOMINVALUE
 NOCYCLE
/


-- lookup values

-- objective type
EXEC zynap_lookup_sp.install_type( 'OBJ_TYPE', 'SYSTEM', 'Types of objectives', 'Objective Types');
EXEC zynap_lookup_sp.install_values( 'OBJ_TYPE', 'PERSONAL', 'Personal', 'Personal', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_TYPE', 'STRATEGIC', 'Strategic', 'Strategic', 20, FALSE);

-- objective importance
EXEC zynap_lookup_sp.install_type( 'OBJ_IMPORTANCE', 'SYSTEM', 'Objective importance ratings', 'Objective Importance');
EXEC zynap_lookup_sp.install_values( 'OBJ_IMPORTANCE', 'HIGH', 'High', 'High', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_IMPORTANCE', 'LOW', 'Low', 'Low', 20, FALSE);

-- objective target units
EXEC zynap_lookup_sp.install_type( 'OBJ_TARGET_UNITS', 'SYSTEM', 'Objective target units', 'Objective Target Units');
EXEC zynap_lookup_sp.install_values( 'OBJ_TARGET_UNITS', 'POUNDS', 'Pounds', 'Pounds', 10, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_TARGET_UNITS', 'DOLLARS', 'Dollars', 'Dollars', 20, FALSE);
EXEC zynap_lookup_sp.install_values( 'OBJ_TARGET_UNITS', 'EUROS', 'Euros', 'Euros', 30, FALSE);

-- set default metric on cross tab reports
update reports set metric_id = -1 where rep_type = 'CROSSTAB' and metric_id is null;

-- navigation
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES (zynap_app_sp.get_myzynap_module_id(), 'TODOS', -36, 'Objectives', 30, '/talentarena/worklistobjectives.htm', 'objectives.description', 'SYSTEMSUBJECT');

-- objectives tab
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order)
VALUES(-16, -2, 'Objectives', 'OBJECTIVES', 'T', 'T', 7);

-- increment sort order for user details tab
update display_config_items set sort_order = sort_order + 1 where id = -15 and content_type = 'USER';

-- my objectives tab
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order)
VALUES(-23, -5, 'My Objectives', 'OBJECTIVES', 'T', 'T', 3);

-- my objectives permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES(PERMIT_SQ.nextval, 'AP', 'admin', 'OBJECTIVES', 'objectives','T', '/talentarena/*.*myobjective*.*', null, null, null);

-- rebuild home role
delete from permits_roles where role_id = 8;

insert into permits_roles select id, '8' from permits where url like '%talentarena%' AND type = 'AP';

exec zynap_loader_sp.menu_permits_link;

ALTER TABLE SESSION_LOGS DROP CONSTRAINT ZSLT_USER_1_FK;

ALTER TABLE SESSION_LOGS ADD (CONSTRAINT
 ZSLT_USER_1_FK FOREIGN KEY
  (USER_ID) REFERENCES USERS
  (ID) ON DELETE CASCADE);

ALTER TABLE PASSWORDS_HISTORY DROP CONSTRAINT ZPHT_ZLT1_FK;

ALTER TABLE PASSWORDS_HISTORY ADD (CONSTRAINT
 ZPHT_ZLT1_FK FOREIGN KEY
  (USER_ID) REFERENCES LOGINS
  (USER_ID) ON DELETE CASCADE);

update roles set label = 'Security Administrator', description = 'Security Administrator' where label = 'Security administrator';

-- merge portfolio item and file item tables

ALTER TABLE NODE_FILES ADD ID NUMBER;
ALTER TABLE NODE_FILES ADD NODE_ID INTEGER;
ALTER TABLE NODE_FILES ADD LABEL VARCHAR2(100);
ALTER TABLE NODE_FILES ADD COMMENTS VARCHAR2(4000);
ALTER TABLE NODE_FILES ADD STATUS VARCHAR2(10) DEFAULT 'LIVE';
ALTER TABLE NODE_FILES ADD SCOPE VARCHAR2(10) DEFAULT 'RESTRICTED';
ALTER TABLE NODE_FILES ADD OWNER_ID INTEGER;
ALTER TABLE NODE_FILES ADD LAST_MODIFIED DATE;

-- copy data from NODE_ITEMS to NODE_FILES
DELETE FROM NODE_FILES WHERE CONTENT_SUB_TYPE = 'URL';

ALTER TABLE NODE_FILES drop column URL;
ALTER TABLE NODE_FILES drop column UPLOAD_VALUE;
ALTER TABLE NODE_FILES drop column REF_VALUE;

UPDATE NODE_FILES SET ID = NODE_ITEM_ID;
UPDATE NODE_FILES NF SET NODE_ID = (SELECT NODE_ID FROM NODE_ITEMS NI WHERE NF.ID = NI.ID);
UPDATE NODE_FILES NF SET LABEL = (SELECT LABEL FROM NODE_ITEMS NI WHERE NF.ID = NI.ID);
UPDATE NODE_FILES NF SET COMMENTS = (SELECT COMMENTS FROM NODE_ITEMS NI WHERE NF.ID = NI.ID);
UPDATE NODE_FILES NF SET STATUS = (SELECT STATUS FROM NODE_ITEMS NI WHERE NF.ID = NI.ID);
UPDATE NODE_FILES NF SET SCOPE = (SELECT SCOPE FROM NODE_ITEMS NI WHERE NF.ID = NI.ID);
UPDATE NODE_FILES NF SET OWNER_ID = (SELECT OWNER_ID FROM NODE_ITEMS NI WHERE NF.ID = NI.ID);
UPDATE NODE_FILES NF SET LAST_MODIFIED = (SELECT LAST_MODIFIED FROM NODE_ITEMS NI WHERE NF.ID = NI.ID);

-- drop NODE_ITEM_ID from NODE_FILES
ALTER TABLE NODE_FILES DROP COLUMN NODE_ITEM_ID;

-- drop old table
DROP TABLE NODE_ITEMS;

-- rename table
ALTER TABLE NODE_FILES RENAME TO NODE_ITEMS;

-- make new columns not null
ALTER TABLE NODE_ITEMS MODIFY(ID NOT NULL);
ALTER TABLE NODE_ITEMS MODIFY(NODE_ID NOT NULL);
ALTER TABLE NODE_ITEMS MODIFY(LABEL NOT NULL);
ALTER TABLE NODE_ITEMS MODIFY(OWNER_ID NOT NULL);
ALTER TABLE NODE_ITEMS MODIFY(BLOB_VALUE NOT NULL);

-- add constraints
ALTER TABLE NODE_ITEMS
 ADD (CONSTRAINT ZPIT_PK PRIMARY KEY
  (ID));

ALTER TABLE NODE_ITEMS
 ADD (CONSTRAINT AVCON_1147443622_STATU_000 CHECK (STATUS IN ('LIVE', 'ARCHIVED')));

ALTER TABLE NODE_ITEMS
 ADD (CONSTRAINT AVCON_1147443622_SCOPE_000 CHECK (SCOPE IN ('PUBLIC', 'RESTRICTED', 'PRIVATE')));

ALTER TABLE NODE_ITEMS ADD (CONSTRAINT
 ZPIT_NODE_FK FOREIGN KEY
  (NODE_ID) REFERENCES NODES
  (ID));

ALTER TABLE NODE_ITEMS ADD (CONSTRAINT
 ZPIT_USER_1_FK FOREIGN KEY
  (OWNER_ID) REFERENCES USERS
  (ID));

ALTER TABLE NODE_ITEMS
 ADD (CONSTRAINT NODE_ITEMS_LABEL_UK UNIQUE
  (LABEL, NODE_ID));

-- add indexes

CREATE INDEX ZPIT_NODE_FK_I ON NODE_ITEMS
 (NODE_ID);

CREATE INDEX ZPIT_USER_1_FK_I ON NODE_ITEMS
 (OWNER_ID);

-- redo views for autonomy

CREATE OR REPLACE VIEW subject_fileitem_autonomy_view AS
  SELECT
    si.id, c.first_name || ' ' || c.second_name AS title, si.node_id AS artefact_id,  si.owner_id,
      si.label AS content_title,
    NVL(si.scope,'RESTRICTED') as scope, 'S' as type, si.content_type_id,
    si.blob_value as blob_value, si.file_extension
  FROM node_items si, subjects u, core_details c
  WHERE si.node_id = u.node_id
  AND u.cd_id=c.id
  AND si.status='LIVE'
WITH READ ONLY;

CREATE OR REPLACE VIEW pos_fileitem_autonomy_view AS
  SELECT
    pi.id, p.title, pi.node_id AS artefact_id, pi.owner_id,
     pi.label as content_title, NVL(pi.scope,'RESTRICTED') as scope,
    'P' as type, pi.content_type_id,
    pi.blob_value as blob_value, pi.file_extension
  FROM node_items pi, positions p
  WHERE  pi.node_id = p.node_id
  AND pi.status='LIVE'
WITH READ ONLY;

@packages/zynap_wf_integration_body.sql

COMMIT;
