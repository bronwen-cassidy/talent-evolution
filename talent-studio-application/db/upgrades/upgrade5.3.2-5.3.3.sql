--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

ALTER TABLE REPORTS ADD IS_PERSONAL VARCHAR(1) DEFAULT 'F';

CREATE TABLE DISPLAY_CONFIG_GROUPS
 (DISPLAY_CONFIG_ITEM_ID NUMBER NOT NULL
 ,GROUP_ID NUMBER NOT NULL
 )
/

ALTER TABLE DISPLAY_CONFIG_GROUPS
 ADD (CONSTRAINT DC_GRP_PK PRIMARY KEY
  (DISPLAY_CONFIG_ITEM_ID
  ,GROUP_ID))
/

ALTER TABLE DISPLAY_CONFIG_GROUPS ADD (CONSTRAINT
 DC_DC_GRP_1_FK FOREIGN KEY
  (DISPLAY_CONFIG_ITEM_ID) REFERENCES DISPLAY_CONFIG_ITEMS
  (ID))
/

ALTER TABLE DISPLAY_CONFIG_GROUPS ADD (CONSTRAINT
 DC_GRP_GRPS_FK FOREIGN KEY
  (GROUP_ID) REFERENCES GROUPS
  (ID))
/

ALTER TABLE DISPLAY_CONFIG_ITEMS ADD POPULATION_ID NUMBER;
ALTER TABLE DISPLAY_CONFIG_ITEMS ADD (CONSTRAINT
 DCFI_POP_FK FOREIGN KEY
  (POPULATION_ID) REFERENCES POPULATIONS
  (ID))
/

-- menu items and permits
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'REPORTS', -57, 'mi.chart.reports', 25, '/analysis/listchartreports.htm', 'chart.reports.menu.description', null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','CHART REPORTS' ,'Permission to Browse Chart Reports', 'T','/analysis/listchartreports.htm', null, null, null);
exec zynap_loader_sp.menu_permits_link;

insert into permits_roles select id, '6' from permits where url = '/analysis/listchartreports.htm' AND type = 'AP';
insert into permits_roles select id, '1' from permits where url = '/analysis/listchartreports.htm' AND type = 'AP';


CREATE TABLE CHART_REPORT_ATTRIBUTES
 (ID NUMBER NOT NULL
 ,REPORT_ID NUMBER NOT NULL
 ,QWF_ID NUMBER NOT NULL
 ,DA_ID NUMBER NOT NULL
 )
/
ALTER TABLE CHART_REPORT_ATTRIBUTES
 ADD (CONSTRAINT CT_RPT_ATTR_PK PRIMARY KEY
  (ID))
/
ALTER TABLE CHART_REPORT_ATTRIBUTES ADD (CONSTRAINT
 CTRP_ATT_RPT_FK FOREIGN KEY
  (REPORT_ID) REFERENCES REPORTS
  (ID))
/
ALTER TABLE CHART_REPORT_ATTRIBUTES ADD (CONSTRAINT
 CTRP_ATTR_FK FOREIGN KEY
  (DA_ID) REFERENCES DYNAMIC_ATTRIBUTES
  (ID))
/
ALTER TABLE CHART_REPORT_ATTRIBUTES ADD (CONSTRAINT
 CTRP_QWF_FK FOREIGN KEY
  (QWF_ID) REFERENCES QUE_WORKFLOWS
  (ID))
/
CREATE SEQUENCE REP_ATTRS_SQ
    NOMAXVALUE NOMINVALUE NOCYCLE
/

ALTER TABLE REPORT_COLUMNS ADD DISPLAY_COLOUR VARCHAR2 (255);
ALTER TABLE REPORT_COLUMNS ADD VALUE VARCHAR2 (512);
-- ========================================================================== HERE DONE 1

-- allow home page only reports
insert into menu_sections (id, section_label, sort_order, module_id, url) values ('REPORTS', 'reports', 50, 'MYZYNAPMODULE', 'menu.htm');

-- 05-05-2010
ALTER TABLE CHART_REPORT_ATTRIBUTES ADD POSITION INTEGER;
ALTER TABLE CHART_REPORT_ATTRIBUTES ADD LABEL VARCHAR2 (2000);

-- todo 13-05-2010

-- no reports for dashboard as the content is not configurable
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES((select min(id) - 1 from display_config_items), -2, 'Dashboard', 'DASHBOARD', 'T', 'T', -1, 'T');

-- no reports for portfolio as the content is not configurable
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES((select min(id) - 1 from display_config_items), -5, 'My Dashboard', 'DASHBOARD', 'T', 'T', -1, 'T');

-- roles for the above initially only superuser must be edited to change
INSERT INTO DISPLAY_CONFIG_ROLES (DISPLAY_CONFIG_ITEM_ID, ROLE_ID) VALUES ((select id from DISPLAY_CONFIG_ITEMS where content_type='DASHBOARD' and display_config_id=-2), 1);

-- admin and home for my content
INSERT INTO DISPLAY_CONFIG_ROLES (DISPLAY_CONFIG_ITEM_ID, ROLE_ID) VALUES ((select id from DISPLAY_CONFIG_ITEMS where content_type='DASHBOARD' and display_config_id=-5), 1);
INSERT INTO DISPLAY_CONFIG_ROLES (DISPLAY_CONFIG_ITEM_ID, ROLE_ID) VALUES ((select id from DISPLAY_CONFIG_ITEMS where content_type='DASHBOARD' and display_config_id=-5), 8);

-- TODO dashboard build tables
CREATE TABLE DASHBOARDS
 (ID NUMBER NOT NULL
 ,TYPE VARCHAR2 (512) NOT NULL
 ,LABEL VARCHAR2 (2800) NOT NULL
 ,POPULATION_ID NUMBER NOT NULL
 )
/
CREATE TABLE DASHBOARD_GROUPS
 (DASHBOARD_ID NUMBER NOT NULL
 ,GROUP_ID NUMBER NOT NULL
 )
/
CREATE TABLE DASHBOARD_ROLES
 (DASHBOARD_ID NUMBER NOT NULL
 ,ROLE_ID NUMBER NOT NULL
 )
/
CREATE TABLE DASHBOARD_PARTICIPANTS
 (DASHBOARD_ID NUMBER NOT NULL
 ,SUBJECT_ID NUMBER NOT NULL
 )
/
CREATE TABLE DASHBOARD_ITEMS
 (ID NUMBER NOT NULL
 ,DASHBOARD_ID NUMBER NOT NULL
 ,REPORT_ID NUMBER NOT NULL
 ,POSITION INTEGER NOT NULL
 ,LABEL VARCHAR2 (2800) NOT NULL
 ,DESCRIPTION VARCHAR2 (2800)
 )
/
CREATE TABLE DASHBOARD_CHART_VALUES
  (ID NUMBER NOT NULL
  ,DASHBOARD_ITEM_ID NUMBER NOT NULL
  ,REPORT_COLUMN_ID NUMBER NOT NULL
  ,EXPECTED_VALUE INTEGER NOT NULL
  ,POSITION INTEGER NOT NULL
  )
/
-- SEQUENCE
CREATE SEQUENCE DASHBOARDS_SQ
    NOMAXVALUE NOMINVALUE NOCYCLE
/
-- CONSTRAINTS
ALTER TABLE DASHBOARDS ADD (CONSTRAINT
 DSH_BD_PK PRIMARY KEY
  (ID))
/
ALTER TABLE DASHBOARD_ITEMS ADD (CONSTRAINT
 DSH_BDIT_PK PRIMARY KEY
  (ID))
/
ALTER TABLE DASHBOARD_CHART_VALUES ADD (CONSTRAINT
 RP_CNVS_PK PRIMARY KEY
  (ID))
/
ALTER TABLE DASHBOARD_GROUPS ADD (CONSTRAINT
 DSHB_GP_PK PRIMARY KEY
  (GROUP_ID, DASHBOARD_ID))
/
ALTER TABLE DASHBOARD_ROLES ADD (CONSTRAINT
 DSHB_RL_PK PRIMARY KEY
  (ROLE_ID, DASHBOARD_ID))
/
ALTER TABLE DASHBOARD_PARTICIPANTS ADD (CONSTRAINT
 DSHB_PT_PK PRIMARY KEY
  (SUBJECT_ID, DASHBOARD_ID))
/
ALTER TABLE DASHBOARDS ADD (CONSTRAINT
 DSHBD_POP_FK FOREIGN KEY
  (POPULATION_ID) REFERENCES POPULATIONS
  (ID))
/
ALTER TABLE DASHBOARD_ITEMS ADD (CONSTRAINT
 DSHBD_IT_FK FOREIGN KEY
  (DASHBOARD_ID) REFERENCES DASHBOARDS
  (ID) ON DELETE CASCADE)
/
ALTER TABLE DASHBOARD_ITEMS ADD (CONSTRAINT
 DSHIT_RP_FK FOREIGN KEY
  (REPORT_ID) REFERENCES REPORTS
  (ID))
/
ALTER TABLE DASHBOARD_CHART_VALUES ADD (CONSTRAINT
 RP_DI_FK FOREIGN KEY
  (DASHBOARD_ITEM_ID) REFERENCES DASHBOARD_ITEMS
  (ID) ON DELETE CASCADE)
/
ALTER TABLE DASHBOARD_CHART_VALUES ADD (CONSTRAINT
 RP_CLN_FK FOREIGN KEY
  (REPORT_COLUMN_ID) REFERENCES REPORT_COLUMNS
  (ID) ON DELETE CASCADE)
/

ALTER TABLE DASHBOARD_PARTICIPANTS ADD (CONSTRAINT
 DBPT_SB_FK FOREIGN KEY
  (SUBJECT_ID) REFERENCES SUBJECTS
  (NODE_ID) ON DELETE CASCADE)
/
ALTER TABLE DASHBOARD_PARTICIPANTS ADD (CONSTRAINT
 DBPT_DB_FK FOREIGN KEY
  (DASHBOARD_ID) REFERENCES DASHBOARDS
  (ID) ON DELETE CASCADE)
/
ALTER TABLE DASHBOARD_GROUPS ADD (CONSTRAINT
 DBGP_GP_FK FOREIGN KEY
  (GROUP_ID) REFERENCES GROUPS
  (ID) ON DELETE CASCADE)
/
ALTER TABLE DASHBOARD_GROUPS ADD (CONSTRAINT
 DBGP_DB_FK FOREIGN KEY
  (DASHBOARD_ID) REFERENCES DASHBOARDS
  (ID) ON DELETE CASCADE)
/
ALTER TABLE DASHBOARD_ROLES ADD (CONSTRAINT
 DBRL_DB_FK FOREIGN KEY
  (DASHBOARD_ID) REFERENCES DASHBOARDS
  (ID) ON DELETE CASCADE)
/
ALTER TABLE DASHBOARD_ROLES ADD (CONSTRAINT
 DBRL_RL_FK FOREIGN KEY
  (ROLE_ID) REFERENCES ROLES
  (ID) ON DELETE CASCADE)
/

-- DON'T THINK WE NEED AN INDEX EXCEPT PERHAPS THE PARTICIPANTS

-- permits and menu_items
-- menu items and permits
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ADMINMODULE', 'TEMPLATES', -71, 'mi.dashboards', 25, '/admin/listdashboards.htm', 'dashboards.menu.description', null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','DASHBOARDS' ,'Permission to Browse Dashboards', 'T','/admin/listdashboards.htm', null, null, null);
exec zynap_loader_sp.menu_permits_link;

-- wildcard the dashboard permits
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'modify','DASHBOARDS' ,'Permission to Modify Dashboards', 'T','/admin/.*dashboard.htm', null, null, null);

-- only admin
insert into permits_roles select id, '1' from permits where url = '/admin/listdashboards.htm' AND type = 'AP';
insert into permits_roles select id, '1' from permits where url = '/admin/.*dashboard.htm' AND type = 'AP';



-- here 09 june
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_myzynap_module_id(), 'ACCOUNT',-38, 'mi.my.dashboard',50,'/talentarena/viewmydashboard.htm', 'mydashboard.menu.description', 'SYSTEMSUBJECT');
exec zynap_loader_sp.menu_permits_link;


commit;
