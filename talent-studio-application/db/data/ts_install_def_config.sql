--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		installs:
--					1) default password and username rules
--					2) turning on and off database logging per module
--
-- Pre-condition:	Database must have been set up (ts_setup.sql) + System user must be installed (ts_install_def_company_user.sql)
-- Version: 		1.0
-- Since:			26/05/2004
-- Author: 			Andreas Andersson
--------------------------------------------------------------------------------------------------


  ----------------------------
  -- 		RULES			--
  ----------------------------

 -- Grouping of password and username rules
INSERT INTO configs (id, label, comments, is_active) VALUES (-1, 'Password Rules', 'Groups all the rules the system should apply for password', 'T');
INSERT INTO configs (id, label, comments, is_active) VALUES (-2, 'Username Rules', 'Groups all the rules the system should apply for usernames', 'T');
INSERT INTO configs (id, label, comments, is_active) VALUES (-3, 'Login Rules', 'Groups all the rules the system that apply to logging into the system', 'T');

INSERT INTO rules (id, config_id, label, description, class, type, is_active, value) VALUES (-10, -1, 'Password Expiry', 'Days before passwords expire', 'com.zynap.domain.rules.Expiry', 'NUMBER', 'T', '20');
INSERT INTO rules (id, config_id, label, description, class, type, is_active, value) VALUES (-11, -1, 'Minimum Password Length', 'Minimum password length (characters)', 'com.zynap.domain.rules.MinLength', 'NUMBER', 'T', '5');
INSERT INTO rules (id, config_id, label, description, class, type, is_active, value) VALUES (-12, -1, 'Maximum Password Length', 'Maximum password length (characters)', 'com.zynap.domain.rules.MaxLength', 'NUMBER', 'T', '10');
INSERT INTO rules (id, config_id, label, description, class, type, is_active, value) VALUES (-13, -1, 'Password Characters', 'Allow only letters (including @, _ and .) and numbers', 'com.zynap.domain.rules.IsAlpha', 'BOOLEAN', 'T', 'F');
INSERT INTO rules (id, config_id, label, description, class, type, is_active, value) VALUES (-14, -1, 'Force Password Change', 'Force password change on first login', 'com.zynap.domain.rules.ShouldChange', 'BOOLEAN', 'T', 'T');
INSERT INTO rules (id, config_id, label, description, class, type, is_active, value) VALUES (-15, -1, 'Password Reuse', 'Number of unique passwords required before user can recycle password', 'com.zynap.domain.rules.Unique', 'NUMBER', 'T', '3');

INSERT INTO rules (id, config_id, label, description, class, type, is_active, value) VALUES (-20, -2, 'Minimum Username Length', 'Minimum user name length (characters)', 'com.zynap.domain.rules.MinLength', 'NUMBER', 'T', '5');
INSERT INTO rules (id, config_id, label, description, class, type, is_active, value) VALUES (-21, -2, 'Maximum Username Length', 'Maximum user name length (characters)', 'com.zynap.domain.rules.MaxLength', 'NUMBER', 'T', '12');
INSERT INTO rules (id, config_id, label, description, class, type, is_active, value) VALUES (-22, -2, 'Username Characters', 'Allow only letters (including @, _ and .) and numbers', 'com.zynap.domain.rules.IsAlpha', 'BOOLEAN', 'T', 'F');


INSERT INTO rules (id, config_id, label, description, class, type, is_active, value, min_value, max_value) VALUES (-30, -3, 'Login Attempts', 'The number of attempts to login before the system locks you out', 'com.zynap.domain.rules.IsAlpha', 'NUMBER', 'T', '3', '3', '9');
