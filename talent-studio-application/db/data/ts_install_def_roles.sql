--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		installs the default access roles and resource roles in the system
--
-- Pre-condition:	This script should never be run alone. It should ALWAYS be started from ts_inserts.sql
-- Version: 		1.0
-- Since:			26/05/2004
-- Author: 			Andreas Andersson
--------------------------------------------------------------------------------------------------

  -- The system user automically gets added to these roles
   

--EXEC zynap_auth_sp.install_role_('Home Arena Access Role', 'T', 'AR', 'Home');
  -- ADMIN APPLICATION ROLES
INSERT INTO roles (id, type, label, description, is_active,  is_system, is_admin, arena_id, is_arena_linked)
    VALUES (1, 'AR', 'Administration Arena', 'Enable access to the Administration Arena', 'T', 'T', 'T', 'ADMINMODULE', 'T');
insert into permits_roles select id, '1' from permits where type = 'AP';
select role_sq.nextval from dual;

-- role for data upload only.
INSERT INTO roles (id, type, label, description, is_active,  is_system, is_admin, arena_id, is_arena_linked)
VALUES (-10, 'AR', 'Admin Arena - Uploads', 'Allow data uploads', 'T', 'T', 'T', 'ADMINMODULE', 'T');
insert into permits_roles select id, '-10' from permits where url like '%datauploads%' AND type = 'AP';
insert into permits_roles select id, '-10' from permits where url = '/admin/home.htm' and type = 'AP';

 --  ADMIN DOMAIN ROLES
--EXEC zynap_auth_sp.install_role_( 'Administration Domain Role', 'T', 'DR', 'Administration Domain');
--insert into permits_roles select id, '2' from permits where type = 'DP';

INSERT INTO roles (id, type, label, description, is_active, is_system)
    VALUES (2, 'DR', 'Administration Domain', 'Administration Domain Role', 'T', 'T');
insert into permits_roles select id, '2' from permits where type = 'DP';
select role_sq.nextval from dual;

  -- APPLICATION ROLES

INSERT INTO roles (id, type, label, description, is_active,  is_system, arena_id, is_arena_linked)
    VALUES (3, 'AR', 'Organisation Arena', 'Enable access to the Organisation Arena', 'T', 'T', 'ORGANISATIONMODULE', 'T');
insert into permits_roles select id, '3' from permits where url like '%orgbuilder%' AND type = 'AP';
select role_sq.nextval from dual;

INSERT INTO roles (id, type, label, description, is_active,  is_system, arena_id, is_arena_linked)
    VALUES (4, 'AR', 'Talent Identifier Arena', 'Enable access to the Talent Identifier Arena', 'T', 'T', 'TALENTIDENTIFIERMODULE', 'T');
insert into permits_roles select id, '4' from permits where url like '%talentidentifier%' AND type = 'AP';
select role_sq.nextval from dual;

INSERT INTO roles (id, type, label, description, is_active,  is_system, arena_id, is_arena_linked)
    VALUES (5, 'AR', 'Succession Builder Arena', 'Enable access to the Succession Builder Arena', 'T', 'T', 'SUCCESSIONMODULE', 'T');
insert into permits_roles select id, '5' from permits where url like '%succession%' AND type = 'AP';
select role_sq.nextval from dual;

INSERT INTO roles (id, type, label, description, is_active,  is_system, arena_id, is_arena_linked)
    VALUES (6, 'AR', 'Analyser Arena', 'Enable access to the Analyser Arena', 'T', 'T', 'ANALYSISMODULE', 'T');
insert into permits_roles select id, '6' from permits where url like '%analysis%' AND type = 'AP';
select role_sq.nextval from dual;

INSERT INTO roles (id, type, label, description, is_active,  is_system, arena_id, is_arena_linked)
    VALUES (7, 'AR', 'Performance Management Arena', 'Enable access to the Performance Management Arena', 'T', 'T', 'PERFMANMODULE', 'T');
insert into permits_roles select id, '7' from permits where url like '%perfman%' AND type = 'AP';
select role_sq.nextval from dual;

INSERT INTO roles (id, type, label, description, is_active,  is_system, arena_id, is_arena_linked)
    VALUES (8, 'AR', 'Home Arena', 'Enable access to the Home Arena', 'T', 'T', 'MYZYNAPMODULE', 'T');
insert into permits_roles select id, '8' from permits where url like '%talentarena%' AND type = 'AP';
insert into permits_roles select id, '8' from permits where url like '%addmy%' AND type = 'AP';
insert into permits_roles select id, '8' from permits where url like '%editmy%' AND type = 'AP';
insert into permits_roles select id, '8' from permits where upper(url) like upper('%MYPORTFOLIOPOS%') AND type = 'AP';
select role_sq.nextval from dual;

-- RESOURCE ROLES
-- EXEC zynap_auth_sp.install_role_('Full Organisation Control', 'T', 'DR', 'Full Organization Control');
INSERT INTO roles (id, type, label, description, is_active, is_system) VALUES (9, 'DR', 'Full Organisation Control', 'Full Organisation Control', 'T', 'T');
select role_sq.nextval from dual;
insert into permits_roles select id, '9' from permits where content = 'ORGANISATIONS' AND not action ='secure' AND type = 'DP';
INSERT INTO roles (id, type, label, description, is_active, is_system) VALUES (10, 'DR', 'View Organisations', 'View Organisations', 'T', 'T');
select role_sq.nextval from dual;
insert into permits_roles select id, '10' from permits where content = 'ORGANISATIONS' and action ='view' AND type = 'DP';
INSERT INTO roles (id, type, label, description, is_active, is_system) VALUES (11, 'DR', 'Full Positions Control', 'Full Positions Control', 'T', 'T');
select role_sq.nextval from dual;
insert into permits_roles select id, '11' from permits where content = 'POSITIONS' AND not action ='secure' AND type = 'DP';
INSERT INTO roles (id, type, label, description, is_active, is_system) VALUES (12, 'DR', 'View Positions', 'View Positions', 'T', 'T');
select role_sq.nextval from dual;
insert into permits_roles select id, '12' from permits where content = 'POSITIONS' and action ='view' AND type = 'DP';
INSERT INTO roles (id, type, label, description, is_active, is_system) VALUES (13, 'DR', 'Full People Control', 'Full People Control', 'T', 'T');
select role_sq.nextval from dual;
insert into permits_roles select id, '13' from permits where content = 'SUBJECTS' AND not action ='secure' AND type = 'DP';
INSERT INTO roles (id, type, label, description, is_active, is_system) VALUES (14, 'DR', 'View People', 'View People', 'T', 'T');
select role_sq.nextval from dual;
insert into permits_roles select id, '14' from permits where content = 'SUBJECTS' and action ='view' AND type = 'DP';
INSERT INTO roles (id, type, label, description, is_active, is_system) VALUES (15, 'DR', 'Security Administrator', 'Security Administrator', 'T', 'T');
select role_sq.nextval from dual;
insert into permits_roles select id, '15' from permits where  action ='secure' AND type = 'DP';



-- insert the default users in the admin role
EXEC zynap_auth_sp.load_user_roles_app_('Administration Arena', zynap_app_sp.get_system_user());
EXEC zynap_auth_sp.load_user_roles_app_('Administration Arena', zynap_app_sp.get_admin_user());
EXEC zynap_auth_sp.load_user_roles_app_('Organisation Arena', zynap_app_sp.get_system_user());
EXEC zynap_auth_sp.load_user_roles_app_('Organisation Arena', zynap_app_sp.get_admin_user());
EXEC zynap_auth_sp.load_user_roles_app_('Analyser Arena', zynap_app_sp.get_system_user());
EXEC zynap_auth_sp.load_user_roles_app_('Analyser Arena', zynap_app_sp.get_admin_user());

-- set the position and ou hierarchy as default
update roles set is_ounit_default='T', is_position_default='T' where type='DR';
