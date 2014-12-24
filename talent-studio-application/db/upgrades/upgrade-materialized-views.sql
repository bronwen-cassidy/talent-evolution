--------------------------------------------------------
-- 17/01/08
--------------------------------------------------------
--DECLARE
--    l_id number;
    
--    BEGIN
--        select id into l_id from permits where url='/orgbuilder/listquestionnaire.htm';
--        delete from permits_roles where permit_id = l_id and role_id=3;
--        insert into permits_roles (permit_id, role_id) values (l_id, 3);
--    END;
/

--alter table ORGANIZATION_UNITS add hlevel number;
--alter table ORGANIZATION_UNITS add path varchar2(1000);
--alter table positions add hlevel number;
--alter table positions add path varchar2(1000);

-- primary key for user_node_domain_permits, need for materialized view
ALTER TABLE USER_NODE_DOMAIN_PERMITS
 ADD CONSTRAINT UNDP_PK
 PRIMARY KEY (USER_ID, NODE_ID, PERMIT_ID)
 ENABLE
 VALIDATE
/

-- give positions a path and level value
--DECLARE
--    CURSOR l_hierarchy_cur IS

--        SELECT id, root_id, level as hlevel, sys_connect_by_path( root_id, '.' ) path
--        FROM POSITIONS_HIERARCHY
--        CONNECT BY PRIOR id = root_id order by root_id, level;

--    rec_ l_hierarchy_cur%ROWTYPE;

--    BEGIN
--        OPEN l_hierarchy_cur;

--        LOOP
--           FETCH l_hierarchy_cur into rec_;
--           EXIT WHEN l_hierarchy_cur%notfound;
          --dbms_output.put_line( 'path: ' || rec_.path);
--           update positions set path = rec_.path where node_id = rec_.id;
--           update positions set hlevel = rec_.hlevel where node_id = rec_.id;

--        END LOOP;

--        CLOSE l_hierarchy_cur;

--    END;
--/

-- give organisation units a path and level value
--DECLARE
--    CURSOR l_hierarchy_cur IS

--        SELECT id, root_id, level as hlevel, sys_connect_by_path( root_id, '.' ) path
--        FROM OU_HIERARCHY
--        CONNECT BY PRIOR id = root_id order by root_id, level;

--    rec_ l_hierarchy_cur%ROWTYPE;

--    BEGIN
--        OPEN l_hierarchy_cur;

--        LOOP
--           FETCH l_hierarchy_cur into rec_;
--           EXIT WHEN l_hierarchy_cur%notfound;
          --dbms_output.put_line( 'path: ' || rec_.path);
--           update ORGANIZATION_UNITS set path = rec_.path where node_id = rec_.id;
--           update ORGANIZATION_UNITS set hlevel = rec_.hlevel where node_id = rec_.id;

--        END LOOP;

--        CLOSE l_hierarchy_cur;

--    END;
--/

prompt
prompt PLEASE ENTER SYSTEM USER PASSWORD.
prompt
accept l_sysuser char prompt 'SYSTEM USER PASSWORD: '

-- login as sysuser
connect SYSTEM/&l_sysuser

prompt
prompt Enter the name of the user to apply grants to.
prompt
accept l_user char prompt 'User id: '

grant query rewrite to &l_user;
grant create materialized view to &l_user;

-- log back in as user
connect &l_user/&l_user

-- create and update the views
@views/materialized_view_logs.sql
@views/security_views.sql
@packages/zynap_triggers_hierarchy.sql

@packages/zynap_app_spec.sql
@packages/zynap_app_body.sql

@packages/zynap_loader_spec.sql
@packages/zynap_loader_body.sql
@packages/zynap_org_unit_spec.sql
@packages/zynap_org_unit_body.sql

@packages/zynap_permit_spec.sql
@packages/zynap_permit_body.sql

-- recompile package
ALTER PACKAGE zynap_permit_sp COMPILE BODY;
ALTER PACKAGE zynap_permit_sp COMPILE BODY;


-- populate all the domain permits
EXEC zynap_permit_sp.populate_domain_permits;

-- recompile package
ALTER PACKAGE WF_INTEGRATION COMPILE BODY;
ALTER PACKAGE ZYNAP_AUTH_SP COMPILE BODY;
ALTER PACKAGE ZYNAP_POSITION_SP COMPILE BODY;
ALTER PACKAGE ZYNAP_ORG_UNIT_SP COMPILE BODY;

commit;