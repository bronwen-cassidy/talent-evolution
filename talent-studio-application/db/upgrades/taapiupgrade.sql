--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES',-372, 'mi.sync.settings',50,'/admin/listadaptors.htm','sync.setting.templates.menu.description', null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','TEMPLATES' ,'Permission to View Syncs', 'T','/admin/listadaptors.htm', null, null, null);

update menu_items set permit_id=(select id from permits where url = '/admin/listadaptors.htm');

insert into permits_roles select id, '1' from permits where url = '/admin/listadaptors.htm' AND type = 'AP';

-- data imports to support taapi data
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','actC', 'actC', 'actC');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','actE', 'actE', 'actE');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','actO', 'actO', 'actO');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','actR', 'actR', 'actR');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','Ambitions Benchmark', 'Ambitions Benchmark match', 'matchAMB');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','Core Benchmark Match', 'The match to the core benchmark', 'matchCORE');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','Total Benchmark Match', 'Total Match to default Benchmark', 'matchTOTAL');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','modC', 'modC', 'modC');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','modE', 'modE', 'modE');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','modO', 'modO', 'modO');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','modR', 'modR', 'modR');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','rankALT', 'rankALT', 'rankALT');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','rankAUT', 'rankAUT', 'rankAUT');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','rankCRE', 'rankCRE', 'rankCRE');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','rankECO', 'rankECO', 'rankECO');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','rankIND', 'rankIND', 'rankIND');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','rankPOL', 'rankPOL', 'rankPOL');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','rankTHE', 'rankTHE', 'rankTHE');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','scoreALT', 'scoreALT', 'scoreALT');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','scoreAUT', 'scoreAUT', 'scoreAUT');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','scoreCRE', 'scoreCRE', 'scoreCRE');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','scoreECO', 'scoreECO', 'scoreECO');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','scoreIND', 'scoreIND', 'scoreIND');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','scorePOL', 'scorePOL', 'scorePOL');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'DECIMAL', 'S', 'T', 'T', 'F','scoreTHE', 'scoreTHE', 'scoreTHE');
insert into dynamic_attributes (id, type, artefact_type, is_active, is_searchable, is_mandatory, label, description, modified_label)
values(da_sq.nextval, 'TEXTAREA', 'S', 'T', 'T', 'F','Tips for Communicating', 'Tips for Communicating with this person', '"tips_for_communicating"');

-- recompile zynap_auth packages
@packages/zynap_auth_body.sql

-- recompile procedures
ALTER PACKAGE ZYNAP_AUTH_SP COMPILE BODY;

commit;
