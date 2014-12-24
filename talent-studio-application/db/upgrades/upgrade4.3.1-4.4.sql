--------------------------------------------------------------------------
-- Play this script in TS1@ZYNAP01 to make it look like JOSE@ORADB.ZYNAP.COM
--                                                                      --
-- Please review the script before using it to make sure it won't       --
-- cause any unacceptable data loss.                                    --
--                                                                      --
-- TS1@ZYNAP01 Schema Extracted by User TS1 
-- JOSE@ORADB.ZYNAP.COM Schema Extracted by User JOSE 
--------------------------------------------------------------------------
-- "Set define off" turns off substitution variables
Set define off;

--
-- 13. POPULATIONS_COMPOSED  (Table) 
--
ALTER TABLE POPULATIONS_COMPOSED DROP PRIMARY KEY CASCADE;
DROP TABLE POPULATIONS_COMPOSED CASCADE CONSTRAINTS;

--
-- 20. TYPE  (Column) 
--
ALTER TABLE LOOKUP_TYPES
MODIFY(TYPE  NULL);

ALTER TABLE LOOKUP_TYPES
MODIFY(TYPE  DEFAULT 'USER');

--
-- 21. IS_ACTIVE  (Column) 
--
ALTER TABLE LOOKUP_TYPES
MODIFY(IS_ACTIVE  NULL);

ALTER TABLE LOOKUP_TYPES
MODIFY(IS_ACTIVE  DEFAULT 'T');

--
-- 22. IS_SYSTEM  (Column) 
--
ALTER TABLE LOOKUP_TYPES
MODIFY(IS_SYSTEM  NULL);

ALTER TABLE LOOKUP_TYPES
MODIFY(IS_SYSTEM  DEFAULT 'F');

--
-- 48. WF_INTEGRATION  (Package Body) 
--
ALTER PACKAGE WF_INTEGRATION COMPILE BODY;

--
-- 49. OU_HIERARCHY_IU  (Trigger) 
--
CREATE OR REPLACE TRIGGER "OU_HIERARCHY_IU" AFTER
INSERT
OR UPDATE
OF "PARENT_ID" ON "ORGANIZATION_UNITS" FOR EACH ROW
BEGIN
 if INSERTING then
     zynap_hierarchy_sp.insert_ou_hi(:NEW.node_id,:NEW.parent_id);
 end if;
if UPDATING AND NOT (:NEW.parent_id = :OLD.parent_id )then
    zynap_hierarchy_sp.update_ou_hi(:NEW.node_id,:NEW.parent_id);
end if;

END OU_HIERARCHY_IU;
/
SHOW ERRORS;


--
-- 64. LABEL_INVERSE  (Column) 
--
ALTER TABLE POPULATIONS DROP COLUMN LABEL_INVERSE;

--
-- 65. IS_ASSOCIATION  (Column) 
--
ALTER TABLE POPULATIONS DROP COLUMN IS_ASSOCIATION;

--
-- 78. SUBJECT_SECONDARY_ASSOCIATIONS  (View) 
--
CREATE OR REPLACE FORCE VIEW SUBJECT_SECONDARY_ASSOCIATIONS
(VALUE_ID, ID, POSITION_ID, SUBJECT_ID)
AS 
select sa.* from subject_associations sa, lookup_values lv where
sa.value_id = lv.id and lv.type_id = 'SECONDARYSUBJECTPOSASSOC'
WITH READ ONLY;

--
-- 85. JASPER_DEFINITION  (Column) 
--
ALTER TABLE REPORTS ADD (JASPER_DEFINITION  BLOB);

--
-- 101. RMG_UK  (Constraint) 
--
ALTER TABLE EXTERNAL_REF_MAPPINGS ADD CONSTRAINT RMG_UK UNIQUE (EXTERNAL_REF_ID, EXTERNAL_USER_ID, INTERNAL_REF);


update reports set default_population_id = -2, population_type = 'S' where population_type = 'SA';
update reports set default_population_id = -1, population_type = 'P' where population_type = 'PA';

delete from population_criterias pc where exists
(select null from populations p where p.id = pc.population_id and not ( p.type = 'P' or p.type = 'S') );

 
delete from populations p where  not ( p.type = 'P' or p.type = 'S');

--
-- 43. WF_INTEGRATION  (Package Body) 
--
ALTER PACKAGE WF_INTEGRATION COMPILE BODY;

--
-- 44. ZYNAP_LOOKUP_SP  (Package Body) 
--
ALTER PACKAGE ZYNAP_LOOKUP_SP COMPILE BODY;

--
-- 45. ZYNAP_ORG_UNIT_SP  (Package Body) 
--
ALTER PACKAGE ZYNAP_ORG_UNIT_SP COMPILE BODY;

-- artefact view configuration updates

-- person and position details and secondary reporting for positions cannot be hidden
update display_config_items set hideable = 'F' where id in (-2,-4,-9);

-- set field names tso that links will work
update report_columns set ref_value = 'coreDetail.name' where ref_value = 'label';
update report_columns set ref_value = 'subjectPrimaryAssociations.subject.coreDetail.name' where ref_value = 'subjectPrimaryAssociations.subject.label';

-- increment positions of other columns for my details to make way for pref given name
update report_columns set position = position + 1 where position >= 3 and rep_id = -20;

-- add column for preferred given name in my details
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.prefGivenName', 'Preferred Given Name', 'Person', -20, 3, 'TEXT', 'F');

-- add view for my executive summary

INSERT INTO display_configs (id, node_type, label, position, type) VALUES (-8, 'S', 'Personal Executive Summary', 4, 'MY_EXEC');

-- person details items
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order)
VALUES(-32, -8, 'My Executive Summary', 'ATTRIBUTE', 'T', 'F', 0);

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-32, 'PUBLIC', 'S', 'The details display for my summary', 'My Executive Summary', 0, -2);

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.title', 'Title', 'Person', -32, 0, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.firstName', 'First Name', 'Person', -32, 1, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.secondName', 'Last Name', 'Person', -32, 2, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'dateOfBirth', 'Date of Birth', 'Person', -32, 3, 'DATE', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.contactTelephone', 'Telephone', 'Person', -32, 4, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.contactEmail', 'Email Address', 'Person', -32, 5, 'TEXT', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -32, 'S' ,-32);

-- new default metric
 INSERT INTO METRICS(ID,ACCESS_TYPE,DESCRIPTION,LABEL,USER_ID,OPERATOR, ATTRIBUTE)
 VALUES(-1, 'Public', 'Counts the number of occurances', 'Count', 0, 'count', 'id');

commit;