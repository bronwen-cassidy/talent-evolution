--------------------------------------------------------
-- 19/04/07
--------------------------------------------------------

-- drop the digester model blob as it is no longer needed

PROMPT Alter QUE_DEFINITION_MODELS Dropping Column DEFINITION_BLOB
ALTER TABLE QUE_DEFINITION_MODELS DROP COLUMN DEFINITION_BLOB;


--question indexes
PROMPT Creating Index 'Question Groups'
CREATE INDEX Q_MOD_FK_I ON QUESTION_GROUPS
 (QUE_DEF_MODEL_ID)
/

PROMPT Creating Index 'Multi Questions'
CREATE INDEX Q_GRP_FK_I ON MULTI_QUESTIONS
 (QUE_GROUP_ID)
/

PROMPT Creating Index 'QUESTION_LINE_ITEMS'
CREATE INDEX Q_MQ_FK_I ON QUESTION_LINE_ITEMS
 (MULTI_QUESTION_ID)
/

PROMPT Creating Index 'QUESTIONS'
CREATE INDEX Q_GPQ_FK_I ON QUESTIONS
 (QUE_GROUP_ID)
/

PROMPT Creating Index 'QUESTIONS'
CREATE INDEX Q_LIQ_FK_I ON QUESTIONS
 (QUESTION_LINE_ITEM_ID)
/

PROMPT Creating Index 'QUESTIONS'
CREATE INDEX Q_DAQ_FK_I ON QUESTIONS
 (DA_ID)
/

-- DYNAMIC ATTRIBUTE REFERENCES
PROMPT Creating Index ON 'DYNAMIC_ATTR_REFERENCES'
CREATE INDEX DAR_PMID_FK_I ON DYNAMIC_ATTR_REFERENCES
 (PARENT_MAPPING_ID)
/

-- objectives
PROMPT Creating index on 'OBJECTIVE_SETS' subject_id
CREATE INDEX OBJ_SUB_REF_FK_I ON OBJECTIVE_SETS
 (SUBJECT_ID)
/

PROMPT Creating index on 'OBJECTIVE_SETS' org_unit_id
CREATE INDEX OBJ_OU_REF_FK_I ON OBJECTIVE_SETS
 (ORG_UNIT_ID)
/

PROMPT Creating index on 'OBJECTIVES' node_id
CREATE INDEX OBJ_NODE_REF_FK_I ON OBJECTIVES
 (NODE_ID)
/

-- TS-2437
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_organisation_module_id(), 'SEARCH',-29, 'Questionnaires', 30, '/orgbuilder/listquestionnaire.htm', 'list.questionnaires.menu.description', null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','QUESTIONNAIRES' ,'Permission to Browse Questionnaires', 'T','/orgbuilder/listquestionnaire.htm', null, null, null) ;

exec zynap_loader_sp.menu_permits_link

DECLARE
    l_id number;
    ld_id number;
    ldd_id number;
    BEGIN
        select id into l_id from permits where url='/orgbuilder/listquestionnaire.htm';
        insert into permits_roles (permit_id, role_id) values (l_id, 1);
        insert into permits_roles (permit_id, role_id) values (l_id, 3);
    END;
/

-- update the menusection labels
UPDATE menu_sections SET section_label = 'configuration' where id = 'TEMPLATES';
UPDATE menu_sections SET section_label = 'manage.attributes' where id='ATTRIBUTES';
UPDATE menu_sections SET section_label = 'manage.roles' where id='ROLES';
UPDATE menu_sections SET section_label = 'manage.security' where id='SECURITY DOMAINS';
UPDATE menu_sections SET section_label = 'manage.users' where id='USERS';
UPDATE menu_sections SET section_label = 'questionnaires' where id='QUESTIONNAIRES';
UPDATE menu_sections SET section_label = 'selection.data' WHERE ID = 'LOOKUPS';
UPDATE menu_sections SET section_label = 'objectives' WHERE ID = 'OBJECTIVES';

-- menu sections for Organisation arena
UPDATE menu_sections SET section_label = 'add' WHERE ID =  'CREATE';
UPDATE menu_sections SET section_label = 'browse' WHERE ID = 'BROWSE_ORG_UNIT_TREE_';
UPDATE menu_sections SET section_label = 'search' WHERE ID = 'SEARCH';


-- menu sections for Analyser arena
UPDATE menu_sections SET section_label = 'data' WHERE ID = 'MAN_REPORTS';
UPDATE menu_sections SET section_label = 'reports' WHERE ID = 'REPORTS';

-- menu sections for Home arena

-- menu sections for favourite reports
UPDATE menu_sections SET section_label = 'account.info' WHERE ID = 'ACCOUNT';
UPDATE menu_sections SET section_label = 'to-do.lists' WHERE ID = 'TODOS';

UPDATE menu_sections SET section_label = 'performance.management' WHERE ID = 'PERFMANMODULE';
UPDATE menu_sections SET section_label = 'talent.identifier' WHERE ID = 'TALENTIDENTIFIERMODULE';
UPDATE menu_sections SET section_label = 'succession.builder' WHERE ID = 'SUCCESSIONMODULE';


-- menu sections for Talent Identifier arena
UPDATE menu_sections SET section_label = 'browse' WHERE ID = 'TALENTPROFILE_ORG_UNIT_TREE_';
UPDATE menu_sections SET section_label = 'search' WHERE ID = 'TALENTPROFILE';
UPDATE menu_sections SET section_label = 'reports' WHERE ID = 'REPORTS';

-- menu sections for Succession Builder arena
UPDATE menu_sections SET section_label = 'browse' WHERE ID = 'SUCCESSION_ORG_UNIT_TREE_';
UPDATE menu_sections SET section_label = 'search' WHERE ID = 'SUCCESSION';

-- menu sections for the Performance Management arena
UPDATE menu_sections SET section_label = 'appraisals' WHERE ID = 'APPRAISALS';
UPDATE menu_sections SET section_label = 'browse' WHERE ID = 'PERFMAN_ORG_UNIT_TREE_';
UPDATE menu_sections SET section_label = 'search' WHERE ID = 'PERFMAN';

-- menu-items
UPDATE menu_items SET label = 'mi.browse.areas' WHERE LABEL = 'Browse Areas' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.browse.security.domains' WHERE LABEL = 'Browse Security Domains' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.browse.roles' WHERE LABEL = 'Browse Roles' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.search.users' WHERE LABEL = 'Search Users' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.person.attributes' WHERE LABEL = 'Person Attributes' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.position.attributes' WHERE LABEL = 'Position Attributes' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.system.settings' WHERE LABEL = 'System Settings' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.arenas' WHERE LABEL = 'Arenas' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.artefact.views' WHERE LABEL = 'Artefact Views' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.browse.selection.types' WHERE LABEL = 'Browse Selection Types' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.questionnaire.definitions' WHERE LABEL = 'Questionnaire Definitions' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.corporate.objectives' WHERE LABEL = 'Corporate Objectives' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.organisation.objectives' WHERE LABEL = 'Organisation Objectives' AND REPORT_ID IS NULL;


UPDATE menu_items SET label = 'mi.organisation.unit' WHERE LABEL = 'Organisation Unit' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.person' WHERE LABEL = 'Person' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.position' WHERE LABEL = 'Position' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.people' WHERE LABEL = 'People' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.positions' WHERE LABEL = 'Positions' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.questionnaires' WHERE LABEL = 'Questionnaires' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.organisation' WHERE LABEL = 'Organisation' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.reporting.structure' WHERE LABEL = 'Reporting Structure' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.documents' WHERE LABEL = 'Documents' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.my.account' WHERE LABEL = 'My Account' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.my.details' WHERE LABEL = 'My Details' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.my.team' WHERE LABEL = 'My Team' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.appraisals' WHERE LABEL = 'Appraisals' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.populations' WHERE LABEL = 'Populations' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.metrics' WHERE LABEL = 'Metrics' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.cross.tab' WHERE LABEL = 'Cross-tab' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.metric' WHERE LABEL = 'Metric' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.tabular' WHERE LABEL = 'Tabular' AND REPORT_ID IS NULL;
UPDATE menu_items SET label = 'mi.reporting.charts' WHERE LABEL = 'Reporting Charts' AND REPORT_ID IS NULL;

-- Changes for TS-2478
ALTER TABLE OBJECTIVE_SETS ADD EXPIRY_DATE DATE;
ALTER TABLE OBJECTIVE_SETS ADD PUBLISHED_DATE DATE;
ALTER TABLE OBJECTIVE_SETS ADD STATUS VARCHAR2(20) DEFAULT 'OPEN';
ALTER TABLE OBJECTIVE_SETS ADD LABEL VARCHAR2(500);

-- Changes for TS-2488
ALTER TABLE OBJECTIVE_SETS ADD LAST_MODIFIED_DATE DATE;
ALTER TABLE OBJECTIVE_SETS ADD ACTION_REQUIRED VARCHAR2(20) DEFAULT 'NO_ACTION';
ALTER TABLE OBJECTIVE_SETS ADD ACTION_GROUP VARCHAR2(20) DEFAULT 'UNASSIGNED';

PROMPT updating objective_set labels
DECLARE
    CURSOR corp_obj_cur IS SELECT * FROM OBJECTIVE_DEFINITIONS;
    l_row OBJECTIVE_DEFINITIONS%ROWTYPE;

    BEGIN
        OPEN corp_obj_cur;
            LOOP
                FETCH corp_obj_cur INTO l_row;
                EXIT WHEN corp_obj_cur%notfound;
                IF l_row.label IS NOT NULL THEN
                    UPDATE OBJECTIVE_SETS SET LABEL = l_row.label
                    WHERE OBJECTIVE_SETS.OBJ_DEF_ID=l_row.id and objective_sets.label is null and objective_sets.obj_def_id <> -1;
                END IF;
            END LOOP;
        CLOSE corp_obj_cur;
    END;
/

ALTER TABLE OBJECTIVE_DEFINITIONS DROP COLUMN EXPIRY_DATE;
ALTER TABLE OBJECTIVE_DEFINITIONS DROP COLUMN DATE_PUBLISHED;

DELETE FROM OBJECTIVE_ATTRIBUTES;
INSERT INTO OBJECTIVE_DEFINITIONS values (-1, 'Objective Definition Template', 'PUBLISHED', 'Default Objective Definition Template', SYSDATE);

UPDATE OBJECTIVE_SETS SET OBJ_DEF_ID = -1;

DELETE FROM OBJECTIVE_DEFINITIONS WHERE ID <> -1;

INSERT INTO OBJECTIVE_ATTRIBUTES (DA_ID, OBJ_DEF_ID) VALUES ((SELECT ID FROM DYNAMIC_ATTRIBUTES WHERE LABEL = 'Goal' AND ARTEFACT_TYPE='OBJ'), -1);
INSERT INTO OBJECTIVE_ATTRIBUTES (DA_ID, OBJ_DEF_ID) VALUES ((SELECT ID FROM DYNAMIC_ATTRIBUTES WHERE LABEL = 'Priority' AND ARTEFACT_TYPE='OBJ'), -1);
INSERT INTO OBJECTIVE_ATTRIBUTES  (DA_ID, OBJ_DEF_ID) VALUES ((SELECT ID FROM DYNAMIC_ATTRIBUTES WHERE LABEL = 'Leadership Style' AND ARTEFACT_TYPE='OBJ'), -1);
INSERT INTO OBJECTIVE_ATTRIBUTES  (DA_ID, OBJ_DEF_ID) VALUES ((SELECT ID FROM DYNAMIC_ATTRIBUTES WHERE LABEL LIKE '%Time' AND ARTEFACT_TYPE='OBJ'), -1);


-- It seems some databases have missed getting column size changes

-- ALTER
ALTER TABLE LOOKUP_VALUES MODIFY TYPE_ID VARCHAR2(4000);
ALTER TABLE LOOKUP_VALUES MODIFY VALUE_ID VARCHAR2(2000);
ALTER TABLE LOOKUP_VALUES MODIFY SHORT_DESC VARCHAR2(2000);

ALTER TABLE LOOKUP_VALUES DROP CONSTRAINT ZLVT_UK2;
