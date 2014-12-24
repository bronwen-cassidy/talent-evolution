--------------------------------------------------------
-- 26/05/09
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '


ALTER TABLE REPORT_COLUMNS ADD CALCULATION_ID NUMBER;

PROMPT Creating Foreign Key on 'REPORT_COLUMNS'
ALTER TABLE REPORT_COLUMNS ADD (CONSTRAINT
 ZRC_CALC_FK_1 FOREIGN KEY
  (CALCULATION_ID) REFERENCES CALCULATIONS
  (ID))
/

PROMPT Creating index on 'REPORT_COLUMNS' CALCULATION_ID
CREATE INDEX CALC_REPC_FK_IDX ON REPORT_COLUMNS
 (CALCULATION_ID)
/

ALTER TABLE CALCULATION_EXPRESSIONS ADD METRIC NUMBER;
ALTER TABLE CALCULATION_EXPRESSIONS ADD QUE_WF_ID NUMBER;
ALTER TABLE CALCULATION_EXPRESSIONS ADD ROLE VARCHAR2 (1000);
ALTER TABLE CALCULATION_EXPRESSIONS ADD RIGHT_BRACKET VARCHAR2(1);
ALTER TABLE CALCULATION_EXPRESSIONS RENAME COLUMN BRACKET TO LEFT_BRACKET;

PROMPT Creating Foreign Key on 'CALCULATION_EXPRESSIONS'
ALTER TABLE CALCULATION_EXPRESSIONS ADD (CONSTRAINT
 CALC_EXP_MTR_1_FK FOREIGN KEY
  (METRIC) REFERENCES METRICS
  (ID))
/

PROMPT Creating Foreign Key on 'CALCULATION_EXPRESSIONS'
ALTER TABLE CALCULATION_EXPRESSIONS ADD (CONSTRAINT
 CALC_EXP_QWF_1_FK FOREIGN KEY
  (QUE_WF_ID) REFERENCES QUE_WORKFLOWS
  (ID))
/

CREATE INDEX CEXP_MTC_FK_IDX ON CALCULATION_EXPRESSIONS
 (METRIC)
/

CREATE INDEX CEXP_QWF_FK_IDX ON CALCULATION_EXPRESSIONS
 (QUE_WF_ID)
/

ALTER TABLE REPORT_GROUPS DROP CONSTRAINT RPGPS_GP_FK
/

ALTER TABLE REPORT_GROUPS ADD (CONSTRAINT
 RPGPS_GP_FK FOREIGN KEY
  (GROUP_ID) REFERENCES GROUPS
  (ID) ON DELETE CASCADE)
/

ALTER TABLE NODE_ITEMS ADD LAST_MODIFIED_BY NUMBER;

DECLARE

CURSOR helptext_cur IS SELECT * from NODE_ITEMS;
l_helptext node_items%rowtype;

BEGIN

   OPEN helptext_cur;
   LOOP
      FETCH helptext_cur into l_helptext;
      EXIT WHEN helptext_cur%notfound;

      update node_items set last_modified_by=l_helptext.owner_id where id=l_helptext.id;

   END LOOP;

END;
/

ALTER TABLE NODE_ITEMS modify LAST_MODIFIED_BY NUMBER NOT NULL;

EXEC zynap_lookup_sp.install_values( 'DATYPE', 'MULTISELECT', 'Multi Selection List', 'Multiple choice drop-down selection list', 10, TRUE);

INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'TEMPLATES',-11, 'mi.tab.settings',40,'/admin/tabsettings.htm','tab.setting.templates.menu.description', null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','TEMPLATES' ,'Permission to View Tab Settings', 'T','/admin/tabsettings.htm', null, null, null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_AVAILABLE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'edit','TEMPLATES' ,'Permission to Edit Tab Settings', 'T','/admin/edittabsettings.htm', null, null, null);

insert into permits_roles select id, '1' from permits where url like '%tabsettings%' and type = 'AP';

exec zynap_loader_sp.menu_permits_link;

-- compile packages
@packages/zynap_node_body.sql;
ALTER PACKAGE zynap_node_sp COMPILE BODY;


PROMPT ************** NB NB NB NB ******************
PROMPT please run the report upgrader to create the calculations and recompile reports


commit;
