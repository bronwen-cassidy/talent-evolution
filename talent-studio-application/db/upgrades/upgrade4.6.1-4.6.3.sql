--------------------------------------------------------
-- 19/11/07
--------------------------------------------------------

-- table to support groups of STUFF!!!!
PROMPT Creating Table 'GROUPS'
CREATE TABLE GROUPS
(
  ID NUMBER NOT NULL,
  LABEL VARCHAR2(512) NOT NULL,
  TYPE VARCHAR2(250) NOT NULL
)
/

PROMPT Creating Primary Key on 'GROUPS'
ALTER TABLE GROUPS
 ADD (CONSTRAINT GRP_1_PK PRIMARY KEY
  (ID))
/

PROMPT Creating Unique Key on 'GROUPS'
ALTER TABLE GROUPS
 ADD (CONSTRAINT GP_LB_TYP_UK UNIQUE
  (LABEL, TYPE))
/

PROMPT Creating Sequence 'GROUPS_SQ'
CREATE SEQUENCE GROUPS_SQ
    NOMAXVALUE NOMINVALUE NOCYCLE
/

ALTER TABLE QUE_WORKFLOWS ADD GROUP_ID NUMBER;

PROMPT Creating Foreign Key on 'QUE_WORKFLOWS'
ALTER TABLE QUE_WORKFLOWS ADD (CONSTRAINT
 GRP_QW_FK FOREIGN KEY
  (GROUP_ID) REFERENCES GROUPS
  (ID))
/

-------------------------------------------------------------------------------
-- changes to the que_workflow table requires an update of the loader packages
-------------------------------------------------------------------------------
@upgrades/zynap_loader_spec_4.6.1-4.6.3.sql
@upgrades/zynap_loader_body_4.6.1-4.6.3.sql

-- recompile package
ALTER PACKAGE ZYNAP_LOADER_SP COMPILE BODY;

-----------------------------------------------------------------------------
-- Section 2 Home Pages and User Groups
-----------------------------------------------------------------------------
-- 1. add a column called group_id
ALTER TABLE HOME_PAGES ADD GROUP_ID NUMBER;
ALTER TABLE USERS ADD GROUP_ID NUMBER;

-- 2. create groups to match the current set of security domains for each of the home pages referenced security domains

 
DECLARE
CURSOR home_pages_cur IS SELECT DISTINCT DOMAIN_ID FROM HOME_PAGES;
l_domain_id home_pages.domain_id%type;

p_domain_label VARCHAR2(500);
p_group_label VARCHAR2(500);
p_group_id NUMBER;

BEGIN
   OPEN home_pages_cur;

       LOOP
           FETCH home_pages_cur into l_domain_id;
           EXIT WHEN home_pages_cur%notfound;
           SELECT LABEL into p_domain_label from security_domains where id=l_domain_id;
           -- create a group and do an update for all matching rows
           SELECT groups_sq.nextval INTO p_group_id FROM DUAL;
           INSERT INTO GROUPS(ID, LABEL, TYPE) VALUES(p_group_id, p_domain_label, 'HOMEPAGE');
           UPDATE HOME_PAGES SET GROUP_ID=p_group_id WHERE DOMAIN_ID=l_domain_id;

           UPDATE USERS SET GROUP_ID=p_group_id WHERE ID IN
           (
                SELECT user_id FROM SECURITY_DOMAINS_USERS WHERE sd_id=l_domain_id
           );           
       END LOOP;

   CLOSE home_pages_cur;
END;
/

-- drop the security domain id column and constraint

ALTER TABLE HOME_PAGES DROP COLUMN DOMAIN_ID;
ALTER TABLE HOME_PAGES DROP CONSTRAINT HP_DOM_FK_ID;
ALTER TABLE HOME_PAGES MODIFY GROUP_ID NUMBER NOT NULL;

-- add the foreign keys
PROMPT Creating Foreign Key on 'HOME_PAGES'
ALTER TABLE HOME_PAGES ADD (CONSTRAINT
 GRP_HPS_FK FOREIGN KEY
  (GROUP_ID) REFERENCES GROUPS
  (ID))
/

PROMPT Creating Foreign Key on 'USERS'
ALTER TABLE USERS ADD (CONSTRAINT
 GRP_USS_FK FOREIGN KEY
  (GROUP_ID) REFERENCES GROUPS
  (ID))
/


----------------------------------------------------
-- Home Page menu-items permits
----------------------------------------------------
INSERT INTO menu_items(module_id, section_id, id, label, sort_order, url, description, user_type) VALUES(zynap_app_sp.get_admin_module_id(), 'SECURITY DOMAINS',-3, 'mi.browse.home.pages',30,'/admin/listhomepages.htm','list.homepages.menu.description', null);

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD)
VALUES (PERMIT_SQ.nextval, 'AP', 'search','SECURITY DOMAINS' ,'Permission to Browse Home Pages', 'T','/admin/listhomepages.htm', null, null, null) ;

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD)
VALUES (PERMIT_SQ.nextval, 'AP', 'modify','SECURITY DOMAINS' ,'Permission to Modify Home Pages', 'T','/admin/.*homepages.htm', null, null, null) ;

exec zynap_loader_sp.menu_permits_link;

-- rebuild admin role permits
DECLARE
    l_id number;
        
    BEGIN
        select id into l_id from permits where url='/admin/listhomepages.htm';
        insert into permits_roles (permit_id, role_id) values (l_id, 1);
        select id into l_id from permits where url='/admin/.*homepages.htm';
        insert into permits_roles (permit_id, role_id) values (l_id, 1);
    END;
/

----------------------------------------------------------
-- Menu Item Ordering
----------------------------------------------------------
update menu_items set sort_order=10 where id=-51;
update menu_items set sort_order=20 where id=-50;

update menu_items set sort_order=10 where id=-12;
update menu_items set sort_order=20 where id=-13;
update menu_items set sort_order=30 where id=-10;

update menu_items set sort_order=10 where id=-35;
update menu_items set sort_order=20 where id=-36;
update menu_items set sort_order=30 where id=-34;

update menu_items set sort_order=40 where id=-54;
update menu_items set sort_order=30 where id=-55;


------------------------------------------------------
-- remove redundant foreign key constraint
------------------------------------------------------
alter table inbox drop constraint INBOX_QUE_1_FK;

-----------------------------------------------------
-- UNIQUE CONSTRAINT ON QUE_WORKFLOWS
-----------------------------------------------------
ALTER TABLE QUE_WORKFLOWS DROP CONSTRAINT QUWFLL_UK;
ALTER TABLE QUE_WORKFLOWS ADD (CONSTRAINT QUWFLL_UK UNIQUE (LABEL, WORKFLOW_TYPE));

commit;
