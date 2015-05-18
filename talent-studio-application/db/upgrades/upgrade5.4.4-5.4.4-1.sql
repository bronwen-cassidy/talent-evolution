--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD) VALUES(-20, 'AP', 'view', 'UPLOADS', 'Access to Data Uploads','T', '/admin/datauploads.htm', null, null, null);

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type, permit_id) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES',-373, 'mi.upload.data',60,'/admin/datauploads.htm','upload.data.templates.menu.description', null, -20);

-- role for data upload only.
INSERT INTO roles (id, type, label, description, is_active,  is_system, is_admin, arena_id,is_arena_linked)
VALUES (-10, 'AR', 'Admin Arena - Uploads', 'Allow data uploads', 'T', 'T', 'T', 'ADMINMODULE', 'F');
insert into permits_roles select id, '-10' from permits where url like '%datauploads%' AND type = 'AP';
insert into permits_roles select id, '-10' from permits where url = '/admin/home.htm' and type = 'AP';

insert into app_roles_users (user_id, role_id) values (0, -10);
insert into app_roles_users (user_id, role_id) values (1, -10);
insert into app_roles_users (user_id, role_id) values (2, -10);

commit;
