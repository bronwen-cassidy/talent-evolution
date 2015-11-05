--------------------------------------------------------
-- 09/11/09
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

ALTER TABLE QUESTIONNAIRES DROP CONSTRAINT QUE_QWW_FK;

ALTER TABLE QUESTIONNAIRES ADD (CONSTRAINT
 QUE_QWW_FK FOREIGN KEY
  (QWF_ID) REFERENCES QUE_WORKFLOWS
  (ID))
/

ALTER TABLE REPORTS ADD LAST_OF VARCHAR(1) DEFAULT 'F';

ALTER TABLE ROLES ADD IS_ARENA_LINKED VARCHAR2(1) DEFAULT 'F';

UPDATE ROLES SET IS_ARENA_LINKED = 'T' WHERE ID IN (1, 3, 5, 6, 7, 8);

-- insert the menu item in admin arena
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'ATTRIBUTES',-70, 'mi.organisation.attributes', 20, '/admin/listorganisationDA.htm', 'list.organisation.da.menu.description', null);
insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD) VALUES (PERMIT_SQ.nextval, 'AP', 'search','ATTRIBUTES' ,'Permission to Browse Organisation Attributes', 'T','/admin/listorganisationDA.htm', null, null, null);

exec zynap_loader_sp.menu_permits_link;

insert into permits_roles select id, '1' from permits where url = '/admin/listorganisationDA.htm' AND type = 'AP';

alter table positions drop constraint POS_POS_FK;

ALTER TABLE POSITIONS ADD (CONSTRAINT
 POS_POS_FK FOREIGN KEY
  (PARENT_ID) REFERENCES POSITIONS
  (NODE_ID) ON DELETE CASCADE)
/

-- compile in the stored procedures
@packages/zynap_node_spec.sql
@packages/zynap_node_body.sql

ALTER PACKAGE zynap_node_sp COMPILE SPECIFICATION;
ALTER PACKAGE zynap_node_sp COMPILE BODY;

-- todo extra added 7/04/2010
ALTER TABLE NODE_DAS ADD LINE_ITEM_ID NUMBER;

DECLARE

CURSOR da_id_cur IS SELECT da.id from dynamic_attributes da where is_dynamic='T';
l_da_rec dynamic_attributes.id%type;

BEGIN
   OPEN da_id_cur;
   LOOP
      FETCH da_id_cur into l_da_rec;
      EXIT WHEN da_id_cur%notfound;
      update node_das set line_item_id=(select QUESTION_LINE_ITEM_ID from questions where da_id=l_da_rec) where da_id=l_da_rec;
   END LOOP;
END;
/
commit;
