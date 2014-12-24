--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		installs:
--					1) default password and username rules
--					2) turning on and off database logging per module
--
-- Version: 		1.0
-- Since:			24/08/2005
-- Author: 			Bronwen Cassidy
--------------------------------------------------------------------------------------------------

	----------------------------------------------
	--		POSITION VIEW CONTENT AREAS 		--
	----------------------------------------------

--##################################################### DETAILS AREA ###############################################--
INSERT INTO display_configs (id, node_type, label, position, type) VALUES (-1, 'P', 'Position Views', 0, 'VIEW');

-- items for the position details content area
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-2, -1, 'Details', 'ATTRIBUTE', 'T', 'F', 1, 'F');

-- report for the position details content
INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-2, 'PUBLIC', 'P', 'The detail content position report', 'Details', 0, -1);

-- position details content area columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Title', 'Position', -2, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'organisationUnit.label', 'Organisation Unit', 'Position', -2, 1, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'comments', 'Comments', 'Position', -2, 2, 'TEXTAREA', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'subjectPrimaryAssociations.subject.coreDetail.name', 'Current Holder', 'Position', -2, 3, 'TEXT', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -2, 'P' ,-2);


--##################################################### REPORTING AREA ###############################################--
-- ## direct reporting content area ## --

-- item
-- PPA position primary associations
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-3, -1, 'Direct Reporting', 'ASSOCIATION', 'T', 'F', 4, 'T');

-- Reports to
INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-3, 'PUBLIC', 'P', 'Position reports to content', 'Reports To', 0, -1);

-- reporting to columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'qualifier.label', 'Reports To Type', 'Position', -3, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Reports To', 'Superior Position', -3, 1, 'TEXT', 'T');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -3, 'PRIMARY_SOURCE' ,-3);

-- Team report
INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-15, 'PUBLIC', 'P', 'Position reporting to content', 'Subordinates', 0, -1);

-- reporting to columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'qualifier.label', 'Subordinate Type', 'Position', -15, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Subordinate', 'Subordinate Position', -15, 1, 'TEXT', 'T');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -3, 'PRIMARY_TARGET' ,-15);

--##################################################### OTHER REPORTING AREA ###############################################--
-- ## indirect reporting content area (secondary associations) ## --

-- item
-- PSA position secondary associations
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-4, -1, 'Provisional Reporting', 'ASSOCIATION', 'T', 'F', 5, 'T');

-- reports to provisionally
INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-4, 'PUBLIC', 'P', 'Provisional, secondary position to position association content', 'Provisional Supervisor', 0, -1);

-- reporting to columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'qualifier.label', 'Reports To Type', 'Position', -4, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Reports To', 'Superior Position', -4, 1, 'TEXT', 'T');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -4, 'SECONDARY_SOURCE' ,-4);

-- Team report
INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-16, 'PUBLIC', 'P', 'Indirect, secondary position to position reporting to content', 'Provisional Subordinate', 0, -1);

-- reporting to columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'qualifier.label', 'Subordinate Type', 'Position', -16, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Subordinate', 'Subordinate Position', -16, 1, 'TEXT', 'T');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -4, 'SECONDARY_TARGET' ,-16);

--##################################################### INCUMBENTS AREA ###############################################--
-- ## incumbent(s) ## --

-- PSPA position subject primary associations
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-5, -1, 'Incumbents', 'ASSOCIATION', 'T', 'F', 2, 'T');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-5, 'PUBLIC', 'S', 'Primary person to position associations (Incumbents)', 'Incumbents', 0, -2);

-- reporting to columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'qualifier.label', 'Holder Type', 'Person', -5, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.name', 'Current Holder', 'Person', -5, 1, 'TEXT', 'T');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -5, 'SUBJECT_POS_PRIMARY' ,-5);

--##################################################### SUCCESSORS AREA ###############################################--
-- ## person successor(s) ## --
-- PSSA = Position Subject Secondary Associations
-- item
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-6, -1, 'Successors', 'ASSOCIATION', 'T', 'F', 3, 'T');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-6, 'PUBLIC', 'S', 'Secondary person to position associations (successors)', 'Successors', 0, -2);

-- successors columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'qualifier.label', 'Succession Type', 'Person', -6, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.name', 'Successor', 'Person', -6, 1, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'subjectPrimaryAssociations.position.title', 'Current Job Title', 'Person', -6, 2, 'TEXT', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -6, 'SUBJECT_POS_SECONDARY' ,-6);


--##################################################### PORTFOLIO AREA ###############################################--

-- portfolio content area non modifiable content just the label can be changed ## --
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-7, -1, 'Portfolio', 'PORTFOLIO', 'T', 'F', 6, 'T');

--##################################################### POSITION EXEC SUMMARY ###############################################--
-- 1. Position Executive Summary
INSERT INTO display_configs (id, node_type, label, position, type) VALUES (-3, 'P', 'Position Executive Summary', 1, 'EXEC');

-- item for the position executive summary
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-1, -3, 'Executive Summary', 'EXEC', 'T', 'F', 0, 'F');

-- report for the position executive summary
INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-1, 'PUBLIC', 'P', 'The executive summary for positions', 'Summary of', 0, -1);

-- position executive summary columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Title', 'Position', -1, 0, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'subjectPrimaryAssociations.subject.coreDetail.name', 'Current Holder', 'Position', -1, 1, 'TEXT', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -1, 'P' ,-1);

	----------------------------------------------
	--		PEOPLE VIEW CONTENT AREAS 		--
	----------------------------------------------

-- ################################################# PEOPLE CONTENT AREAS ########################################### --

--##################################################### DETAILS AREA ###############################################--

INSERT INTO display_configs (id, node_type, label, position, type) VALUES (-2, 'S', 'Person Views', 2, 'VIEW');

-- person display item
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-9, -2, 'Details', 'ATTRIBUTE', 'T', 'F', 1, 'F');

-- person details
INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-9, 'PUBLIC', 'S', 'Person Details', 'Details', 0, -2);

-- person details report columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.title', 'Title', 'Person', -9, 0, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.firstName', 'First Name', 'Person', -9, 1, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.secondName', 'Last Name', 'Person', -9, 2, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.prefGivenName', 'Preferred Given Name', 'Person', -9, 3, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'dateOfBirth', 'Date of Birth', 'Person', -9, 4, 'DATE', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'subjectPrimaryAssociations.position.title', 'Current Position', 'Person', -9, 5, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'picture', 'Photograph', 'Person', -9, 6, 'IMG', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -9, 'S' ,-9);

--##################################################### CURRENT JOBS ###############################################--

-- SPPA = Subject position primary associations
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-10, -2, 'Current Jobs', 'ASSOCIATION', 'T', 'F', 2, 'T');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-10, 'PUBLIC', 'P', 'Current Jobs', 'Current Jobs', 0, -2);

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'qualifier.label', 'Current Job Type', 'Person', -10, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Job Title', 'Person', -10, 1, 'TEXT', 'T');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -10, 'SUBJECT_PRIMARY' ,-10);

--##################################################### SUCCESSION AREA ##############################################--

-- SPSA = Subject position secondary associations
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-11, -2, 'Succession Profile', 'ASSOCIATION','T', 'F', 4, 'T');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-11, 'PUBLIC', 'P', 'Succession Plan', 'Succession', 0, -2);

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'qualifier.label', 'Succession Type', 'Person', -11, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Succession Position', 'Person', -11, 1, 'TEXT', 'T');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -11, 'SUBJECT_SECONDARY' ,-11);

--##################################################### TALENT AREA ###############################################--

-- no columns defined at the moment
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-12, -2, 'Talent Profile', 'ATTRIBUTE', 'T', 'T', 3, 'T');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-12, 'PUBLIC', 'S', 'Persons Talent Profile', 'Talent Profile', 0, -2);

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -12, 'S' ,-12);

--##################################################### PERFORMANCE AREA ###############################################--

-- no columns defined at the moment
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-13, -2, 'Performance Profile', 'ATTRIBUTE', 'T', 'T', 5, 'T');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-13, 'PUBLIC', 'S', 'Persons Performance Profile', 'Performance Profile', 0, -2);

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -13, 'S' ,-13);

--##################################################### DASHBOARD AREA ###############################################--

-- no reports for dashboard as the content is not configurable
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-17, -2, 'Dashboard', 'DASHBOARD', 'T', 'T', 1, 'T');


--##################################################### PORTFOLIO AREA ###############################################--

-- no reports for portfolio as the content is not configurable
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-14, -2, 'Portfolio', 'PORTFOLIO', 'T', 'F', 6, 'T');

--##################################################### PROGRESS REPORTS ###############################################--

insert into display_config_items (ID, DISPLAY_CONFIG_ID, LABEL, is_active, content_type, hideable, sort_order, roles_modifiable)
VALUES (-65, -2, 'Progress Reports', 'F', 'PROGRESS_REPORT', 'T', 10, 'T');

--##################################################### OBJECTIVES AREA ###############################################--

-- objectives content area non modifiable content just the label can be changed but it can be hidden--
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-16, -2, 'Objectives', 'OBJECTIVES', 'T', 'T', 7, 'T');

--##################################################### USER DETAILS AREA ###############################################--

-- no reports for user details as the content is not configurable
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-15, -2, 'User Details', 'USER', 'T', 'F', 8, 'T');

-- person reports content area is non modifiable content just the label can be changed ## --
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-60, -2, 'Person Reports', 'REPORTS', 'T', 'T', 9, 'T');

--##################################################### PERSON EXECUTIVE SUMMARY ###############################################--
-- 2. Person Executive Summary

INSERT INTO display_configs (id, node_type, label, position, type) VALUES (-4, 'S', 'Person Executive Summary', 3, 'EXEC');

-- person display config exec summary items
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-8, -4, 'Executive Summary', 'EXEC', 'T', 'F', 0, 'F');

-- person executive summary report
INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-8, 'PUBLIC', 'S', 'The executive summary for people', 'Summary of', 0, -2);

-- person executive summary report columns
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.title', 'Title', 'Person', -8, 0, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'subjectPrimaryAssociations.position.title', 'Current Position', 'Person', -8, 1, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.firstName', 'First Name', 'Person', -8, 2, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'dateOfBirth', 'Date of Birth', 'Person', -8, 3, 'DATE', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.secondName', 'Last Name', 'Person', -8, 4, 'TEXT', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -8, 'S' ,-8);

	----------------------------------------------
	--		MY DETAILS CONTENT AREAS 		--
	----------------------------------------------

--##################################################### MY EXECUTIVE SUMMARY ###########################################--
INSERT INTO display_configs (id, node_type, label, position, type) VALUES (-8, 'S', 'Personal Executive Summary', 4, 'MY_EXEC');

-- person details items
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-32, -8, 'My Executive Summary', 'ATTRIBUTE', 'T', 'F', 0, 'F');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-32, 'PUBLIC', 'S', 'The details display for my summary', 'My Executive Summary', 0, -2);

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.title', 'Title', 'Person', -32, 0, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'dateOfBirth', 'Date of Birth', 'Person', -32, 1, 'DATE', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.firstName', 'First Name', 'Person', -32, 2, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.contactTelephone', 'Telephone', 'Person', -32, 3, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.secondName', 'Last Name', 'Person', -32, 4, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.contactEmail', 'Email Address', 'Person', -32, 5, 'TEXT', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -32, 'S' ,-32);

--##################################################### MY DETAILS ###############################################--
INSERT INTO display_configs (id, node_type, label, position, type) VALUES (-5, 'S', 'Personal Details View', 4, 'MY_DETAILS');

-- person details items
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-20, -5, 'My Details', 'ATTRIBUTE', 'T', 'F', 0, 'F');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-20, 'PUBLIC', 'S', 'The details display for my information', 'My Details', 0, -2);

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.title', 'Title', 'Person', -20, 0, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.firstName', 'First Name', 'Person', -20, 1, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.secondName', 'Last Name', 'Person', -20, 2, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.prefGivenName', 'Preferred Given Name', 'Person', -20, 3, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'dateOfBirth', 'Date of Birth', 'Person', -20, 4, 'DATE', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.contactTelephone', 'Telephone', 'Person', -20, 5, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.contactEmail', 'Email Address', 'Person', -20, 6, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'picture', 'Photograph', 'Person', -20, 7, 'IMG', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -20, 'S' ,-20);

--##################################################### MY CURRENT JOB AREA #############################################--

INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-21, -5, 'My Current Jobs', 'ASSOCIATION', 'T', 'F', 1, 'T');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-21, 'PUBLIC', 'P', 'My Current Jobs', 'My Current Jobs', 0, -2);

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'qualifier.label', 'Current Job Type', 'Person', -21, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Job Title', 'Person', -21, 1, 'TEXT', 'T');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -21, 'SUBJECT_PRIMARY' ,-21);

--##################################################### MY DASHBOARD AREA ###############################################--

-- no reports for dashboard as the content is configured elsewhere
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-25, -5, 'My Dashboard', 'DASHBOARD', 'T', 'T', 1, 'T');


--##################################################### MY PORTFOLIO AREA ###############################################--

-- no reports for portfolio as the content is not configurable
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-22, -5, 'My Portfolio', 'PORTFOLIO', 'T', 'F', 2, 'T');

--##################################################### MY PROGRESS REPORTS ###############################################--

insert into display_config_items (ID, DISPLAY_CONFIG_ID, LABEL, is_active, content_type, hideable, sort_order, roles_modifiable)
VALUES (-66, -5, 'My Progress Reports', 'F', 'PROGRESS_REPORT', 'T', 10, 'T');

--##################################################### MY OBJECTIVES AREA ###############################################--

-- objectives content area non modifiable content just the label can be changed but it can be hidden --
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-23, -5, 'My Objectives', 'OBJECTIVES', 'T', 'T', 3, 'T');

--##################################################### MY PERSON REPORTS ###############################################--
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-24, -5, 'My Person Reports', 'REPORTS', 'T', 'T', 4, 'T');

--##################################################### PERSONS ADD VIEWS ####################################################--

INSERT INTO display_configs (id, node_type, label, position, type) VALUES (-6, 'S', 'Person Add Details', 5, 'ADD');

-- person add details items
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-30, -6, 'Details', 'ATTRIBUTE', 'T', 'F', 0, 'F');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-30, 'PUBLIC', 'S', 'The details required when adding a person', 'Person Add Details', 0, -2);

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.title', 'Title', 'Person', -30, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.firstName', 'First Name', 'Person', -30, 1, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.secondName', 'Last Name', 'Person', -30, 2, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.prefGivenName', 'Preferred Given Name', 'Person', -30, 3, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'dateOfBirth', 'Date of Birth', 'Person', -30, 4, 'DATE', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.contactTelephone', 'Telephone', 'Person', -30, 5, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'coreDetail.contactEmail', 'Email Address', 'Person', -30, 6, 'TEXT', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'picture', 'Photograph', 'Person', -30, 7, 'IMG', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -30, 'SUB_ADD' ,-30);

--##################################################### POSITIONS ADD VIEWS ####################################################--

INSERT INTO display_configs (id, node_type, label, position, type) VALUES (-7, 'P', 'Position Add Details', 6, 'ADD');

-- person add details items
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-31, -7, 'Details', 'ATTRIBUTE', 'T', 'F', 0, 'F');

INSERT INTO REPORTS (ID, ACCESS_TYPE, REP_TYPE, DESCRIPTION, LABEL, USER_ID, DEFAULT_POPULATION_ID)
VALUES(-31, 'PUBLIC', 'P', 'The details required when adding a position', 'Position Add Details', 0, -1);

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'title', 'Title', 'Position', -31, 0, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'organisationUnit.label', 'Organisation Unit', 'Position', -31, 1, 'TEXT', 'T');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'comments', 'Comments', 'Position', -31, 2, 'TEXTAREA', 'F');

INSERT INTO DISPLAY_ITEM_REPORTS(ID, DISPLAY_CONFIG_ITEM_ID, CONTENT_TYPE, REPORT_ID)
VALUES(config_sq.nextval, -31, 'POS_ADD' ,-31);

--#################################################################################################################
--########################### SET UP DEFAULT ARTEFACT VIEWS FOR ARENAS ############################################
--#################################################################################################################

-- Performance Management arena

-- Position (uses Details view)
INSERT INTO MODULE_DISPLAY_CONFIG_ITEMS (ID, MODULE_ID, DISPLAY_CONFIG_ITEM_ID)
VALUES (-1, 'PERFMANMODULE', -2);

-- Person  (uses Performance Profile view)
INSERT INTO MODULE_DISPLAY_CONFIG_ITEMS (ID, MODULE_ID, DISPLAY_CONFIG_ITEM_ID)
VALUES (-2, 'PERFMANMODULE', -13);

-- Succession arena

-- Position (uses Successors view)
INSERT INTO MODULE_DISPLAY_CONFIG_ITEMS (ID, MODULE_ID, DISPLAY_CONFIG_ITEM_ID)
VALUES (-3, 'SUCCESSIONMODULE', -6);

-- Person  (uses Succession Profile view)
INSERT INTO MODULE_DISPLAY_CONFIG_ITEMS (ID, MODULE_ID, DISPLAY_CONFIG_ITEM_ID)
VALUES (-4, 'SUCCESSIONMODULE', -11);


-- Talent Identifier arena

-- Position  (uses Details view)
INSERT INTO MODULE_DISPLAY_CONFIG_ITEMS (ID, MODULE_ID, DISPLAY_CONFIG_ITEM_ID)
VALUES (-5, 'TALENTIDENTIFIERMODULE', -2);

-- Person (uses Talent Profile view)
INSERT INTO MODULE_DISPLAY_CONFIG_ITEMS (ID, MODULE_ID, DISPLAY_CONFIG_ITEM_ID)
VALUES (-6, 'TALENTIDENTIFIERMODULE', -12);

--########################################################################
-- DISPLAY_CONFIG_ROLES
--########################################################################

PROMPT Updating Table 'DISPLAY_CONFIG_ROLES'
DECLARE
cursor roles_cur IS SELECT ID FROM ROLES WHERE TYPE = 'AR';
cursor display_items_cur IS SELECT ID FROM DISPLAY_CONFIG_ITEMS;

l_roles roles.id%type;
l_configs display_config_items.id%type;

BEGIN
   OPEN display_items_cur;
    <<outer_loop>>
       LOOP
           FETCH display_items_cur into l_configs;
           EXIT WHEN display_items_cur%notfound;

           OPEN roles_cur;
            <<inner_loop>>
               LOOP
                   FETCH roles_cur INTO l_roles;
                   EXIT inner_loop WHEN roles_cur%notfound;
                   --dbms_output.put_line( 'config_id: ' || l_configs || ' role_id: ' || l_roles);
                   INSERT INTO DISPLAY_CONFIG_ROLES (DISPLAY_CONFIG_ITEM_ID, ROLE_ID) VALUES (l_configs, l_roles);

               END LOOP inner_loop;
           CLOSE roles_cur;
       END LOOP outer_loop;
   CLOSE display_items_cur;

END;
/
