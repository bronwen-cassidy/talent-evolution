--------------------------------------------------------
-- 06/08/09
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '


CREATE TABLE POPULATION_GROUPS
 (
  POPULATION_ID NUMBER NOT NULL
 ,GROUP_ID NUMBER NOT NULL
 )
/

PROMPT Creating Primary Key on 'POPULATION_GROUPS'
ALTER TABLE POPULATION_GROUPS
 ADD (CONSTRAINT POPGPS_PK PRIMARY KEY
  (POPULATION_ID
  ,GROUP_ID))
/

PROMPT Creating Foreign Key on 'POPULATION_GROUPS'
ALTER TABLE POPULATION_GROUPS ADD (CONSTRAINT
 PPGPS_GP_FK FOREIGN KEY
  (GROUP_ID) REFERENCES GROUPS
  (ID) ON DELETE CASCADE)
/

PROMPT Creating Foreign Key on 'POPULATION_GROUPS'
ALTER TABLE POPULATION_GROUPS ADD (CONSTRAINT
 PPGPS_RP_FK FOREIGN KEY
  (POPULATION_ID) REFERENCES POPULATIONS
  (ID) ON DELETE CASCADE)
/

PROMPT Creating Index ON 'POPULATION_GROUPS'
CREATE INDEX PPGPS_GP_FK_2 ON POPULATION_GROUPS
 (GROUP_ID)
/

PROMPT Creating Index ON 'POPULATION_GROUPS'
CREATE INDEX PPGPS_PP_FK_2 ON POPULATION_GROUPS
 (POPULATION_ID)
/

CREATE OR REPLACE VIEW population_groups_view AS
select p.ID,p.TYPE,p.USER_ID,p.LABEL,p.SCOPE,p.DESCRIPTION,p.ACTIVE_CRITERIA, pg.group_id from populations p left outer join population_groups pg on pg.population_id=p.id
WITH READ ONLY;

ALTER TABLE ROLES ADD IS_ADMIN VARCHAR2(1) DEFAULT 'F';
update roles set is_admin='T' where arena_id='ADMINMODULE' and type='AR';

PROMPT ***************** NB NB NB NB NB NB Please check there are no other roles to be made admin only assignable, most importantly compensation role for bnym ************* 

-- TODO DO THE FOLLOWING MAKE SENSE FOR CLIENTS OTHER THAN BNYM
--INSERT INTO roles (id, type, label, description, is_active,  is_system, arena_id) VALUES (role_sq.nextval , 'AR', 'Admin Only Arena', 'Enable access to the Admin Arena Only', 'T', 'T', 'ADMINMODULE');
--INSERT INTO roles (id, type, label, description, is_active,  is_system, arena_id) VALUES (role_sq.nextval , 'AR', 'Compensation', 'Enable access to Compensation', 'T', 'T', 'ADMINMODULE');
-- TODO '16' NEEDS TO BE VERIFIED
--insert into permits_roles select id, '21' from permits where url like '%admin/%' AND type = 'AP';
--update roles set label = 'Superuser' where id=1;
--update roles set description = 'Enable access to the Entire System' where id=1;
--update roles set is_admin = 'T' where arena_id='ADMINMODULE'; 

commit;
