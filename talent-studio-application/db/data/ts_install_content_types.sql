--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		Installs the different content types and the possible actions for these.
--
-- Pre-condition:	Database must have been set up (ts_setup.sql) + System user must be installaed (ts_install_def_user.sql)
-- Version: 		1.0
-- Since:			26/05/2004
-- Author: 			Andreas Andersson
--------------------------------------------------------------------------------------------------
  
  -- Postion: Description
INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
VALUES ('DESC', 'Position Description', 'P', 'UPLOAD,TEXT,URL', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('DESC', 'read', 'Can read position descriptions', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('DESC', 'write', 'Can write position descriptions', 'T');

  -- Position: Competencey Framework
INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
VALUES ('CF', 'Competency Framework', 'P', 'UPLOAD,TEXT,URL', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('CF', 'read', 'Can read position competency frameworks', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('CF', 'write', 'Can write position competency frameworks', 'T');

  -- Position: Supporting Material
INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
VALUES ('SM', 'Supporting Materials', 'P', 'UPLOAD,TEXT,URL', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('SM', 'read', 'Can read position supporting materials', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('SM', 'write', 'Can write position supporting materials', 'T');

  -- Subject: CV
INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
VALUES ('CV', 'Curriculum Vitae', 'S', 'UPLOAD,TEXT', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('CV', 'read', 'Can read person curriculum vitae', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('CV', 'write', 'Can write person curriculum vitae', 'T');

  -- Subject: Person Sourced
INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
VALUES ('SS', 'Person Sourced', 'S', 'UPLOAD,TEXT', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('SS', 'read', 'Can read person sourced information', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('SS', 'write', 'Can write person sourced information', 'T');

  -- Subject: Email Reference
--INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
--VALUES ('EMAIL', 'Email', 'S', 'REFERENCE', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('EMAIL', 'read', 'Can read email of a person', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('EMAIL', 'write', 'Can write email of a person', 'T');

  -- Subject: Written Correspondence
INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
VALUES ('WC', 'Written Correspondence', 'S', 'UPLOAD', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('WC', 'read', 'Can read written correspondence of a person', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('WC', 'write', 'Can write to written correspondence of a person', 'T');

  -- Subject: Work Products
INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
VALUES ('WP', 'Work Products', 'S', 'UPLOAD', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('WP', 'read', 'Can read work products created by a person', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('WP', 'write', 'Can write to work products created for a person', 'T');

  -- Subject: Training Records
INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
VALUES ('TR', 'Training Records', 'S', 'UPLOAD', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('TR', 'read', 'Can read training records of a person', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('TR', 'write', 'Can write to training records created for a person', 'T');

  -- Subject: Performance and Reviews
INSERT INTO content_types (id, label, type, sub_types, is_active, is_available)
VALUES ('AR', 'Performance and Reviews', 'S', 'UPLOAD,TEXT', 'T', 'T');

--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('AR', 'read', 'Can read performance and reviews', 'T');
--INSERT INTO zynap_resource_action_tb (, action, description, is_available)
--VALUES ('AR', 'write', 'Can write performance and reviews', 'T');



  ---------------------------
  -- RESOURCE ROLE ACTIONS --
  ---------------------------

--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('DESC', 'read', 0, 'POSITIONPORTFOLIO');
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('DESC', 'write', 0, 'POSITIONPORTFOLIO');
--
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('CF', 'read', 0, 'POSITIONPORTFOLIO');
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('CF', 'write', 0, 'POSITIONPORTFOLIO');
--
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('SM', 'read', 0, 'POSITIONPORTFOLIO');
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('SM', 'write', 0, 'POSITIONPORTFOLIO');
--
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('CV', 'read', 0, 'SUBJECTPORTFOLIO');
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('CV', 'write', 0, 'SUBJECTPORTFOLIO');
--
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('SS', 'read', 0, 'SUBJECTPORTFOLIO');
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('SS', 'write', 0, 'SUBJECTPORTFOLIO');

--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('EMAIL', 'read', 0, 'SUBJECTCONTENT');
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('EMAIL', 'write', 0, 'SUBJECTCONTENT');

--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('WC', 'read', 0, 'SUBJECTPORTFOLIO');
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('WC', 'write', 0, 'SUBJECTPORTFOLIO');
--
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('WP', 'read', 0, 'SUBJECTPORTFOLIO');
--INSERT INTO zynap_resource_role_action_tb (, action, company_id, role_id)
--VALUES ('WP', 'write', 0, 'SUBJECTPORTFOLIO');
--
--INSERT INTO zynap_resource_role_action_tb (content_type_id, action, company_id, role_id)
--VALUES ('TR', 'read', 0, 'SUBJECTPORTFOLIO');
--INSERT INTO zynap_resource_role_action_tb (content_type_id, action, company_id, role_id)
--VALUES ('TR', 'write', 0, 'SUBJECTPORTFOLIO');
--
--INSERT INTO zynap_resource_role_action_tb (content_type_id, action, company_id, role_id)
--VALUES ('AR', 'read', 0, 'SUBJECTPORTFOLIO');
--INSERT INTO zynap_resource_role_action_tb (content_type_id, action, company_id, role_id)
--VALUES ('AR', 'write', 0, 'SUBJECTPORTFOLIO');


















