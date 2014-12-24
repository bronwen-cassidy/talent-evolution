--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		Installs the default modules and menus supported by Talent Studio
--
-- Pre-condition:	This script should never be run alone. It should ALWAYS be started from ts_inserts.sql
-- Version: 		1.0
-- Since:			26/05/2004
-- Author: 			Andreas Andersson
--------------------------------------------------------------------------------------------------

  -- INSTALL MODULES note session timeout for each module is defined here)
INSERT INTO modules (id, label, url, description, session_timeout, is_active, is_hideable, sort_order)
VALUES (zynap_app_sp.get_admin_module_id(), 'Administration', '/admin/home.htm', 'Administration', 25, 'T', 'F', 10);

INSERT INTO modules( id, label, url, description, session_timeout, is_active, is_hideable, sort_order)
VALUES ('ANALYSISMODULE', 'Analyser', '/analysis/home.htm', 'Analyser', 25, 'T', 'F', 20);

INSERT INTO modules( id, label, url, description, session_timeout, is_active, is_hideable, sort_order)
VALUES ( zynap_app_sp.get_myzynap_module_id(), 'Home', '/talentarena/home.htm', 'Home', 25, 'T', 'F', 30);

INSERT INTO modules( id, label, url, description, session_timeout, is_active, is_hideable, sort_order)
VALUES ( zynap_app_sp.get_organisation_module_id(), 'Organisation', '/orgbuilder/home.htm', 'Organisation', 25, 'T', 'F', 40);

INSERT INTO modules( id, label, url, description, session_timeout, is_active, is_hideable, sort_order)
VALUES ('PERFMANMODULE', 'Performance Management', '/perfman/home.htm', 'Performance Management', 25, 'T', 'T', 50);

INSERT INTO modules( id, label, url, description, session_timeout, is_active, is_hideable, sort_order)
VALUES ( zynap_app_sp.get_talent_module_id(), 'Talent Identifier', '/talentidentifier/home.htm', 'Talent Identifier', 25, 'T', 'T', 60);

INSERT INTO modules( id, label, url, description, session_timeout, is_active, is_hideable, sort_order)
VALUES ( zynap_app_sp.get_succession_module_id(), 'Succession Builder', '/succession/home.htm', 'Succession Builder', 25, 'T', 'T', 70);

-------------------------------------------------------------------
-- INSERT MENUS
-------------------------------------------------------------------

-- menu sections for Administration arena
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_admin_module_id(),'TEMPLATES','configuration',10, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_admin_module_id(),'ATTRIBUTES','manage.attributes',20, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_admin_module_id(),'ROLES','manage.roles',30, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_admin_module_id(),'SECURITY DOMAINS','manage.security',40, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_admin_module_id(),'USERS','manage.users',50, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_admin_module_id(),'QUESTIONNAIRES', 'questionnaires', 60, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_admin_module_id(),'LOOKUPS', 'selection.data', 70, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_admin_module_id(),'OBJECTIVES', 'objectives', 80, 'menu.htm');

-- menu sections for Organisation arena
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_organisation_module_id(), 'CREATE', 'add', 10, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_organisation_module_id(),'BROWSE_ORG_UNIT_TREE_','browse',20, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_organisation_module_id(),'SEARCH', 'search', 30, 'menu.htm');


-- menu sections for Analyser arena
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES('ANALYSISMODULE', 'MAN_REPORTS', 'data', 10, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES('ANALYSISMODULE', 'REPORTS', 'reports', 20, 'menu.htm');

-- menu sections for Home arena

INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_myzynap_module_id(),'ACCOUNT','account.info',10, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_myzynap_module_id(),'TODOS','to-do.lists',20, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_myzynap_module_id(),'MESSAGES','my.messages',30, 'menu.htm');
-- allow home page only reports
INSERT INTO menu_sections (id, section_label, sort_order, module_id, url) VALUES ('REPORTS', 'reports', 45, 'MYZYNAPMODULE', 'menu.htm');


-- menu sections for favourite reports
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_myzynap_module_id(),'PERFMANMODULE','Performance Management',40, 'processdescription.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_myzynap_module_id(),'TALENTIDENTIFIERMODULE','Talent Identifier',50, 'processdescription.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_myzynap_module_id(),'SUCCESSIONMODULE','Succession Builder',60, 'processdescription.htm');


-- menu sections for Talent Identifier arena
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_talent_module_id(),'TALENTPROFILE_ORG_UNIT_TREE_','browse',10, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_talent_module_id(),'TALENTPROFILE','search',20, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_talent_module_id(),'REPORTS','reports',30, 'menu.htm');

-- menu sections for Succession Builder arena
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_succession_module_id(),'SUCCESSION_ORG_UNIT_TREE_','browse',10, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_succession_module_id(),'SUCCESSION','search',20, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_succession_module_id(),'REPORTS','reports',30, 'menu.htm');

-- menu sections for the Performance Management arena
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_performance_man_module_id(),'APPRAISALS','appraisals',10, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_performance_man_module_id(),'PERFMAN_ORG_UNIT_TREE_','browse',20, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_performance_man_module_id(),'PERFMAN','search',30, 'menu.htm');
INSERT INTO menu_sections(module_id, id, section_label, sort_order, url) VALUES(zynap_app_sp.get_performance_man_module_id(),'REPORTS','reports',40, 'menu.htm');


-- menu items for Administration arena
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'SECURITY DOMAINS',-4, 'mi.browse.areas',10,'/admin/listarea.htm','list.areas.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'SECURITY DOMAINS',-5, 'mi.browse.security.domains',20,'/admin/listsecuritydomain.htm','list.securitydomains.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'SECURITY DOMAINS',-3, 'mi.browse.home.pages',30,'/admin/listhomepages.htm','list.homepages.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'ROLES',-6, 'mi.browse.roles',10,'/admin/listrole.htm','list.roles.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'USERS',-7, 'mi.search.users',10,'/admin/listuser.htm','list.users.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'ATTRIBUTES',-8, 'mi.person.attributes', 10, '/admin/listsubjectDA.htm', 'list.subject.da.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'ATTRIBUTES',-9, 'mi.position.attributes', 20, '/admin/listpositionDA.htm', 'list.position.da.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'ATTRIBUTES',-70, 'mi.organisation.attributes', 30, '/admin/listorganisationDA.htm', 'list.organisation.da.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES',-12, 'mi.arenas',10,'/admin/listarenas.htm','list.arenas.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES', -13, 'mi.artefact.views', 20, '/admin/displayconfigsettings.htm', 'display.config.settings.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES', -71, 'mi.dashboards', 25, '/admin/listdashboards.htm', 'dashboards.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES',-10, 'mi.system.settings',30,'/admin/listsettings.htm','list.templates.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES',-11, 'mi.tab.settings',40,'/admin/tabsettings.htm','tab.setting.templates.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES',-372, 'mi.sync.settings',50,'/admin/listadaptors.htm','sync.setting.templates.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES',-373, 'mi.data.upload',60,'/admin/datauploads.htm','upload.data.templates.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'LOOKUPS',-14, 'mi.browse.selection.types', 10, '/admin/listlookuptypes.htm', 'list.selection.types.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'QUESTIONNAIRES',-15, 'mi.questionnaire.definitions', 10, '/admin/listquestionnairedefinition.htm', 'list.questionnaire.definitions.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'QUESTIONNAIRES',-18, 'mi.questionnaire.workflows', 20, '/admin/listqueworkflows.htm', 'list.questionnaire.workflow.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'OBJECTIVES',-16, 'mi.corporate.objectives', 10, '/admin/listcorporateobjectives.htm', 'list.corporate.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'OBJECTIVES',-17, 'mi.organisation.objectives', 20, '/admin/listorgunitobjectives.htm', 'list.oganisation.objectives.menu.description', null);

-- menu items for Organisation arena
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES (zynap_app_sp.get_organisation_module_id() , 'CREATE', -20, 'mi.organisation.unit', 10, '/orgbuilder/addorganisation.htm', 'add.organisation.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_organisation_module_id(),'CREATE',-21,'mi.person', 20, '/orgbuilder/addsubject.htm','add.subjects.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_organisation_module_id(), 'CREATE',-22, 'mi.position', 30, '/orgbuilder/addposition.htm', 'add.positions.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_organisation_module_id(),'SEARCH',-23,'mi.people', 10, '/orgbuilder/listsubject.htm','list.subjects.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_organisation_module_id(), 'SEARCH',-24, 'mi.positions', 20, '/orgbuilder/listposition.htm', 'list.positions.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_organisation_module_id(), 'SEARCH',-29, 'mi.questionnaires', 30, '/orgbuilder/listquestionnaire.htm', 'list.questionnaires.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES (zynap_app_sp.get_organisation_module_id() , 'BROWSE_ORG_UNIT_TREE_', -25, 'mi.organisation', 10, '/orgbuilder/listorganisations.htm', 'list.organisation.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_organisation_module_id(),'BROWSE_ORG_UNIT_TREE_',-26,'mi.people', 20,'/orgbuilder/browsesubject.htm','browse.subjects.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_organisation_module_id(), 'BROWSE_ORG_UNIT_TREE_',-27, 'mi.positions', 30, '/orgbuilder/browseposition.htm', 'browse.positions.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_organisation_module_id(), 'BROWSE_ORG_UNIT_TREE_',-28, 'mi.reporting.structure', 40,'/orgbuilder/reportingchart.htm','report.browser.menu.description', null);

-- menu items for Talent Identifier arena
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_talent_module_id(), 'TALENTPROFILE',-40, 'mi.documents',10,'/talentidentifier/documentsearch.htm', 'search.documents.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_talent_module_id(), 'TALENTPROFILE',-41, 'mi.people',20,'/talentidentifier/listsubject.htm', 'list.talentidentifier.subjects.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_talent_module_id(), 'TALENTPROFILE',-42, 'mi.positions',30,'/talentidentifier/listposition.htm', 'list.talentidentifier.positions.menu.description', null);

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_talent_module_id(), 'TALENTPROFILE_ORG_UNIT_TREE_',-43, 'mi.people',10,'/talentidentifier/browsesubject.htm', 'browse.subjects.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_talent_module_id(), 'TALENTPROFILE_ORG_UNIT_TREE_',-44, 'mi.positions',20,'/talentidentifier/browseposition.htm', 'browse.positions.menu.description', null);

--menu items for Succession Builder arena
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_succession_module_id(), 'SUCCESSION',-60, 'mi.documents',10,'/succession/documentsearch.htm', 'search.documents.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_succession_module_id(), 'SUCCESSION',-61, 'mi.people',20,'/succession/listsubject.htm', 'list.talentidentifier.subjects.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_succession_module_id(), 'SUCCESSION',-62, 'mi.positions',30,'/succession/listposition.htm', 'list.talentidentifier.positions.menu.description', null);

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_succession_module_id(), 'SUCCESSION_ORG_UNIT_TREE_',-63, 'mi.people',10,'/succession/browsesubject.htm', 'browse.subjects.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_succession_module_id(), 'SUCCESSION_ORG_UNIT_TREE_',-64, 'mi.positions',20,'/succession/browseposition.htm', 'browse.positions.menu.description', null);

-- menu items for Home arena
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_myzynap_module_id(), 'ACCOUNT',-31, 'mi.my.account',10,'/talentarena/viewmyaccount.htm', 'myaccount.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_myzynap_module_id(), 'ACCOUNT',-32, 'mi.my.details',20,'/talentarena/viewmydetails.htm', 'mydetails.menu.description', 'SYSTEMSUBJECT');
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_myzynap_module_id(), 'ACCOUNT',-33, 'mi.my.team',30,'/talentarena/viewmyteam.htm', 'myteam.menu.description', 'SYSTEMSUBJECT');
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_myzynap_module_id(), 'ACCOUNT',-30, 'mi.my.portfolio',40,'/talentarena/viewmyportfolio.htm', 'myportfolio.menu.description', 'SYSTEMSUBJECT');
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_myzynap_module_id(), 'ACCOUNT',-38, 'mi.my.dashboard',50,'/talentarena/viewmydashboard.htm', 'mydashboard.menu.description', 'SYSTEMSUBJECT');

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES (zynap_app_sp.get_myzynap_module_id(), 'TODOS', -35, 'mi.appraisals', 10, '/talentarena/worklistappraisals.htm', 'worklist.performance.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES (zynap_app_sp.get_myzynap_module_id(), 'TODOS', -36, 'mi.assessments', 20, '/talentarena/worklistassessments.htm', 'worklist.assessment.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES (zynap_app_sp.get_myzynap_module_id(), 'TODOS', -34, 'mi.questionnaires', 30, '/talentarena/worklistquestionnaires.htm', 'worklist.menu.description', null);

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES (zynap_app_sp.get_myzynap_module_id(), 'MESSAGES', -37, 'mi.inbox', 30, '/talentarena/worklistmessages.htm', 'worklist.messages.description', 'SYSTEMSUBJECT');

-- menu items for Analyser arena
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'MAN_REPORTS', -51, 'mi.metrics',10,'/analysis/listmetrics.htm','metrics.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'MAN_REPORTS',-50, 'mi.populations',20,'/analysis/listpopulations.htm','populations.menu.description', null);

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'REPORTS', -52, 'mi.cross.tab',10,'/analysis/listcrosstabreports.htm','crosstabs.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'REPORTS', -53, 'mi.metric',20,'/analysis/listmetricreports.htm','metrics.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'REPORTS', -54, 'mi.tabular',30,'/analysis/listreports.htm','reports.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'REPORTS', -56, 'mi.appraisal.summary.reports',35,'/analysis/listappraisalreports.htm', 'appraisal.reports.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'REPORTS', -55, 'mi.reporting.charts', 40, '/analysis/listchartsettings.htm', 'chart.settings.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'REPORTS', -57, 'mi.chart.reports', 31, '/analysis/listchartreports.htm', 'chart.reports.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'REPORTS', -58, 'mi.progress.reports', 32, '/analysis/listprogressreports.htm', 'progress.reports.menu.description', null);


-- menu items for the Performance Management arena
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_performance_man_module_id(), 'PERFMAN',-80, 'mi.documents',10,'/perfman/documentsearch.htm', 'search.documents.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_performance_man_module_id(), 'PERFMAN',-81, 'mi.people',20,'/perfman/listsubject.htm', 'list.talentidentifier.subjects.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_performance_man_module_id(), 'PERFMAN',-82, 'mi.positions',30,'/perfman/listposition.htm', 'list.talentidentifier.positions.menu.description', null);

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_performance_man_module_id(), 'PERFMAN_ORG_UNIT_TREE_',-83, 'mi.people',10,'/perfman/browsesubject.htm', 'browse.subjects.menu.description', null);
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_performance_man_module_id(), 'PERFMAN_ORG_UNIT_TREE_',-84, 'mi.positions',20,'/perfman/browseposition.htm', 'browse.positions.menu.description', null);

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_performance_man_module_id(), 'APPRAISALS',-85, 'mi.appraisals',10,'/perfman/listperformancereviews.htm', 'appraisals.menu.description', null);



