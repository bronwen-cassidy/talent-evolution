--------------------------------------------------------
-- 26/09/09
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

-- insert the menu item in analyser arena
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES('ANALYSISMODULE', 'REPORTS', -56, 'mi.appraisal.summary.reports',20,'/analysis/listappraisalreports.htm', 'appraisal.reports.menu.description', null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','APPRAISAL REPORTS' ,'Permission to Browse Appraisal Report Templates', 'T','/analysis/listappraisalreports.htm', null, null, null);

exec zynap_loader_sp.menu_permits_link;

insert into permits_roles select id, '6' from permits where url = '/analysis/listappraisalreports.htm' AND type = 'AP';
insert into permits_roles select id, '1' from permits where url = '/analysis/listappraisalreports.htm' AND type = 'AP';

-- column for weighting only this for AZ
ALTER TABLE REPORT_COLUMNS ADD WEIGHTING INTEGER;
ALTER TABLE REPORTS ADD APPRAISAL_ID NUMBER;
ALTER TABLE REPORTS ADD STATUS VARCHAR2(250);

ALTER TABLE REPORTS ADD (CONSTRAINT
 REP_PERF_FK FOREIGN KEY
  (APPRAISAL_ID) REFERENCES PERFORMANCE_REVIEWS
  (ID))
/

-- person reports content area is non modifiable content just the label can be changed ## --
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-60, -2, 'Person Reports', 'REPORTS', 'T', 'T', 15, 'T');

--##################################################### MY PERSON REPORTS ###############################################--
INSERT INTO display_config_items (id, display_config_id, label, content_type, is_active, hideable, sort_order, roles_modifiable)
VALUES(-24, -5, 'My Person Reports', 'REPORTS', 'T', 'T', 4, 'T');


INSERT INTO display_config_roles (display_config_item_id, role_id) values (-60, 1);
INSERT INTO display_config_roles (display_config_item_id, role_id) values (-24, 1);
INSERT INTO display_config_roles (display_config_item_id, role_id) values (-24, 8);

commit;
