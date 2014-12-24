--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		Installs the default attributes for subjects and positions
--
-- Pre-condition:	Database must have been set up (ts_setup.sql) + System user must be installaed (ts_install_def_user.sql)
-- Version: 		1.0
-- Since:			10/01/2005
-- Author: 			Bronwen Cassidy
--------------------------------------------------------------------------------------------------

-- Person extended attributes
EXEC zynap_da_sp.add_attribute('Aristocratic Prefix', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Aristocratic Prefix');
EXEC zynap_da_sp.add_attribute('Given Name 2', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Second Given Name');
EXEC zynap_da_sp.add_attribute('Given Name 3', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Third Given Name');
EXEC zynap_da_sp.add_attribute('Middle Name 1', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'First Middle Name');
EXEC zynap_da_sp.add_attribute('Middle Name 2', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Second Middle Name');
EXEC zynap_da_sp.add_attribute('Middle Name 3', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Third Middle Name');
EXEC zynap_da_sp.add_attribute('Family Name 2', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Second Family Name');
EXEC zynap_da_sp.add_attribute('Family Name 3', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Third Family Name');
EXEC zynap_da_sp.add_attribute('Gender', 'STRUCT', 'S', NULL, NULL, 'F', 'T', 'T', 'GENDER', 'Gender');
EXEC zynap_da_sp.add_attribute('Generation Affix', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Generation Affix');
EXEC zynap_da_sp.add_attribute('Qualification Affix', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Qualification Affix');
EXEC zynap_da_sp.add_attribute('Work Phone Number', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Work Phone Number');
EXEC zynap_da_sp.add_attribute('Home Phone Number', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Home Phone Number');
EXEC zynap_da_sp.add_attribute('Mobile Phone Number', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Mobile Phone Number');
EXEC zynap_da_sp.add_attribute('Home Email Address', 'TEXT', 'S', NULL, NULL, 'F', 'T', 'T', NULL, 'Home Email Address');
EXEC zynap_da_sp.add_attribute('Work Email Address', 'TEXT', 'S', NULL, NULL, 'F', 'T', 'T', NULL, 'Work Email Address');
EXEC zynap_da_sp.add_attribute('Post Office Box', 'TEXT', 'S', '20', NULL, 'F', 'T', 'F', NULL, 'Post Office Box');
EXEC zynap_da_sp.add_attribute('Address Line One', 'TEXT', 'S', '255', NULL, 'F', 'T', 'F', NULL, 'Address Line One');
EXEC zynap_da_sp.add_attribute('Address Line Two', 'TEXT', 'S', '255', NULL, 'F', 'T', 'F', NULL, 'Address Line Two');
EXEC zynap_da_sp.add_attribute('Address Line Three', 'TEXT', 'S', '255', NULL, 'F', 'T', 'F', NULL, 'Address Line Three');
EXEC zynap_da_sp.add_attribute('Address Line Four', 'TEXT', 'S', '255', NULL, 'F', 'T', 'F', NULL, 'Address Line Four');
EXEC zynap_da_sp.add_attribute('Postal Code', 'TEXT', 'S', '10', NULL, 'F', 'T', 'F', NULL, 'Postal Code');
EXEC zynap_da_sp.add_attribute('First Region', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'First Region');
EXEC zynap_da_sp.add_attribute('Second Region', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Second Region');
EXEC zynap_da_sp.add_attribute('Third Region', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Third Region');
EXEC zynap_da_sp.add_attribute('Municipality', 'TEXT', 'S', '100', NULL, 'F', 'T', 'F', NULL, 'Municipality');
EXEC zynap_da_sp.add_attribute('Country', 'STRUCT', 'S', NULL, NULL, 'F', 'T', 'T', 'COUNTRY', 'Country');
EXEC zynap_da_sp.add_attribute('Key Successor', 'STRUCT', 'S', NULL, NULL, 'F', 'T', 'T', 'CHOICE', 'Defines whether or not this person is a key successor');
EXEC zynap_da_sp.add_attribute('High Potential', 'STRUCT', 'S', NULL, NULL, 'F', 'T', 'T', 'CHOICE', 'Defines whether or not this person has a high potential');
EXEC zynap_da_sp.add_attribute('Key Contributor', 'STRUCT', 'S', NULL, NULL, 'F', 'T', 'T', 'CHOICE', 'Defines whether or not this person is a key contributor');
EXEC zynap_da_sp.add_attribute('Critical Skill Holder', 'STRUCT', 'S', NULL, NULL, 'F', 'T', 'T', 'CHOICE', 'Defines whether or not this person has a critical skill');

-- Position extended attributes
EXEC zynap_da_sp.add_attribute('Key Position', 'STRUCT', 'P', NULL, NULL, 'F', 'T', 'T', 'CHOICE', 'Defines whether this position is a key position in the company');
EXEC zynap_da_sp.add_attribute('Development Role', 'STRUCT', 'P', NULL, NULL, 'F', 'T', 'T', 'CHOICE', 'Defines whether this position is a development role');
EXEC zynap_da_sp.add_attribute('Classification', 'STRUCT', 'P', NULL, NULL, 'F', 'T', 'T', 'CLASSIFICATION', 'Position Classification Codes');
EXEC zynap_da_sp.add_attribute('Remuneration', 'STRUCT', 'P', NULL, NULL, 'F', 'T', 'T', 'REMUNERATION','Position Remuneration Codes');
EXEC zynap_da_sp.add_attribute('Category', 'STRUCT', 'P', NULL, NULL, 'F', 'T', 'T', 'JOBCATEGORY', 'Position Category Codes');
EXEC zynap_da_sp.add_attribute('Location', 'STRUCT', 'P', NULL, NULL, 'F', 'T', 'T', 'LOCATION', 'Position Location');
EXEC zynap_da_sp.add_attribute('Grade', 'STRUCT', 'P', NULL, NULL, 'F', 'T', 'T', 'JOBGRADE', 'Position Grade Codes');
EXEC zynap_da_sp.add_attribute('Preferred Language', 'STRUCT', 'P', NULL, NULL, 'F', 'T', 'T', 'LANGUAGE', 'Preferred Language');
EXEC zynap_da_sp.add_attribute('Competency', 'TEXTAREA', 'P', NULL, NULL, 'F', 'T', 'T', NULL, 'Position Competency Information');

-- system dynamic attributes (values can be changed but cannot be disabled)
EXEC zynap_da_sp.add_attribute('Primary Association Type', 'STRUCT', 'PA', NULL, NULL, 'T', 'T', 'T', 'PRIMARY', 'Primary Association Type');
EXEC zynap_da_sp.add_attribute('Secondary Association Type', 'STRUCT', 'PA', NULL, NULL, 'T', 'T', 'T', 'SECONDARY', 'Secondary Association Type');

EXEC zynap_da_sp.add_attribute('Primary Association Type', 'STRUCT', 'SA', NULL, NULL, 'T', 'T', 'T', 'POSITIONSUBJECTASSOC', 'Primary Association Type');
EXEC zynap_da_sp.add_attribute('Secondary Association Type', 'STRUCT', 'SA', NULL, NULL, 'T', 'T', 'T', 'SECONDARYSUBJECTPOSASSOC', 'Secondary Association Type');

-- objective dynamic attributes
EXEC zynap_da_sp.add_attribute('Goal', 'STRUCT', 'OBJ', NULL, NULL, 'F', 'T', 'F', 'OBJ_TYPE', 'Objective Goal');
EXEC zynap_da_sp.add_attribute('Priority', 'STRUCT', 'OBJ', NULL, NULL, 'F', 'T', 'F', 'OBJ_IMPORTANCE', 'Objective Priority');
EXEC zynap_da_sp.add_attribute('Leadership Style', 'STRUCT', 'OBJ', NULL, NULL, 'F', 'T', 'F', 'OBJ_LEADERSHIP', 'Leadership Style');
EXEC zynap_da_sp.add_attribute('% Time', 'NUMBER', 'OBJ', NULL, NULL, 'F', 'T', 'F', null, 'Objective Target Times');

-- FUNCTION TYPE DYNAMIC ATTRIBUTES
EXEC zynap_da_sp.add_attribute('Current Date', 'DATE', 'FUNC', NULL, NULL, 'F', 'T', 'F', NULL, 'Date determined at time of execution');

-- set modified label
update DYNAMIC_ATTRIBUTES set modified_label = replace(trim(lower(label)),' ');

-- insert default objective definition
INSERT INTO OBJECTIVE_DEFINITIONS values (-1, 'Default Objective Template', 'PUBLISHED', 'The default installed objective definition', SYSDATE);

INSERT INTO OBJECTIVE_ATTRIBUTES (DA_ID, OBJ_DEF_ID) VALUES ((SELECT ID FROM DYNAMIC_ATTRIBUTES WHERE LABEL = 'Goal' AND ARTEFACT_TYPE='OBJ'), -1);
INSERT INTO OBJECTIVE_ATTRIBUTES (DA_ID, OBJ_DEF_ID) VALUES ((SELECT ID FROM DYNAMIC_ATTRIBUTES WHERE LABEL = 'Priority' AND ARTEFACT_TYPE='OBJ'), -1);
INSERT INTO OBJECTIVE_ATTRIBUTES  (DA_ID, OBJ_DEF_ID) VALUES ((SELECT ID FROM DYNAMIC_ATTRIBUTES WHERE LABEL = 'Leadership Style' AND ARTEFACT_TYPE='OBJ'), -1);
INSERT INTO OBJECTIVE_ATTRIBUTES  (DA_ID, OBJ_DEF_ID) VALUES ((SELECT ID FROM DYNAMIC_ATTRIBUTES WHERE LABEL LIKE '%Time' AND ARTEFACT_TYPE='OBJ'), -1);

insert into dynamic_attributes (ID, LABEL, TYPE, ARTEFACT_TYPE, MAX_SIZE, MIN_SIZE, IS_MANDATORY, IS_ACTIVE, IS_SEARCHABLE, DESCRIPTION, LOCK_ID, MODIFIED_LABEL, UNIQUE_NUMBER, VALIDATION_MASK, IS_UNIQUE)
values (da_sq.nextval, 'Unique Id', 'TEXT', 'S', 7, 7, 'T', 'T', 'F', 'Provides the person with a unique identifier', -1, 'uniqueid', '1.01', '^[k][a-z]{3}[0-9]{3}', 'T');
