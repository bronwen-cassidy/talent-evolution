--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		Installs the default company (internal - Zynap), a default security zone
--					and a default super user in the system.
--					This script should be run as the first "insert" script
--
-- Pre-condition:	This script should never be run alone. It should ALWAYS be started from ts_inserts.sql
-- Version: 		1.0
-- Since:			26/05/2004
-- Author: 			Andreas Andersson
--------------------------------------------------------------------------------------------------


----------------------------------------------------------------------------------------------------
-- Set up the SYSTEM user. This user is needed during installation but can be disabled afterwards --
----------------------------------------------------------------------------------------------------

INSERT INTO core_details(id, first_name, second_name, pref_given_name, contact_email, contact_telephone)
VALUES(0, 'Zynap', 'Administrator', 'Zynap Administrator', 'admin@zynap.com', '');

INSERT INTO core_details(id, first_name, second_name, pref_given_name, contact_email, contact_telephone)
VALUES(1, 'System', 'Administrator', 'Sysadmin', '', '');

select details_sq.nextval from dual;

INSERT INTO core_details(id, first_name, second_name, pref_given_name, contact_email, contact_telephone)
VALUES(2, 'System', 'webserviceuser', 'Sysadmin', '', '');

select details_sq.nextval from dual;


-- create default system user. We need someone that can log in when system is installed
INSERT INTO users (id, user_type,  is_active, cd_id, is_root)
VALUES(0, 'SYSTEM', 'T', 0, 'T');

-- create default company administrator
INSERT INTO users (id, user_type, is_active, cd_id, is_root)
VALUES(1, 'SYSTEM', 'T', 1, 'F');

SELECT USER_SQ.NEXTVAL FROM DUAL;

-- create default webservice user
INSERT INTO users (id, user_type, is_active, cd_id, is_root)
VALUES(2, 'SYSTEM', 'T', 1, 'T');

SELECT USER_SQ.NEXTVAL FROM DUAL;

INSERT INTO logins (user_id,  username, password, expires, locked, force_pwd_change)
VALUES(0,  'talentsys', zynap_auth_sp.encrypt('TMEvolution123'), null, 'F', 'F');

INSERT INTO logins (user_id,  username, password, expires, locked, force_pwd_change)
VALUES(1, 'administrator', zynap_auth_sp.encrypt('administrator'), null, 'F', 'F');

INSERT INTO logins (user_id,  username, password, expires, locked, force_pwd_change)
VALUES(2, 'webserviceuser', zynap_auth_sp.encrypt('webserviceuser'), null, 'F', 'F');

commit;
--Create a default org unit - required because org units cannot be orphans so system requires at least one
EXEC zynap_loader_sp.create_org_unit(0, 'Default Org Unit', null,  'Please edit this default org unit', 'T');
commit;
--Create a default position - required because positions cannot be orphans so system requires at least one

EXEC zynap_loader_sp.create_position (1, 'Default Position', 0, NULL, 'This is the default position - please edit this', 'T');
SELECT node_SQ.NEXTVAL FROM DUAL;
commit;


