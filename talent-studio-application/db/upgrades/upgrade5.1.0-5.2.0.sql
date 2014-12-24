--------------------------------------------------------
-- 10/09/08
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

alter table dynamic_attributes add validation_mask varchar2(512);
alter table dynamic_attributes add is_unique varchar2(1) default 'F';

insert into dynamic_attributes (ID, LABEL, TYPE, ARTEFACT_TYPE, MAX_SIZE, MIN_SIZE, IS_MANDATORY, IS_ACTIVE, IS_SEARCHABLE, DESCRIPTION, LOCK_ID, MODIFIED_LABEL, UNIQUE_NUMBER, VALIDATION_MASK, IS_UNIQUE)
values (da_sq.nextval, 'Unique Id', 'TEXT', 'S', 7, 7, 'T', 'T', 'F', 'Provides the person with a unique identifier', -1, 'uniqueid', '1.01', '^[k][a-z]{3}[0-9]{3}', 'T');

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.1.0-5.2.0.sql', 18, 'Unique Dynamic Attribute Created');

create table MY_TEAM_VIEWS (
    id number not null
   ,user_id number not null
   ,da_id number not null
)
/

ALTER TABLE MY_TEAM_VIEWS
 ADD (CONSTRAINT M_TEAM_PK PRIMARY KEY
  (ID))
/

ALTER TABLE MY_TEAM_VIEWS ADD (CONSTRAINT
 MT_USR_FK FOREIGN KEY
  (USER_ID) REFERENCES USERS
  (ID) ON DELETE CASCADE)
/

ALTER TABLE MY_TEAM_VIEWS ADD (CONSTRAINT
 MT_DA_FK FOREIGN KEY
  (DA_ID) REFERENCES DYNAMIC_ATTRIBUTES
  (ID) ON DELETE CASCADE)
/

ALTER TABLE MY_TEAM_VIEWS
 ADD (CONSTRAINT MT_USRID_UK UNIQUE
  (USER_ID))
/

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.1.0-5.2.0.sql', 49, 'my team view info created');

ALTER TABLE SUBJECT_ASSOCIATIONS ADD
(COMMENTS VARCHAR2(3000));


-- create the lookup_type and values for this associations
-- succession planning reasons
EXEC zynap_lookup_sp.install_type('SUCCESSION_REASON', 'SYSTEM', 'Reasons for Succession', 'Job Vacancy Reasons');
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'CAREERMOVE', 'Career Move', 'Career Move', 10, FALSE);
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'PROMOTION', 'Promotion', 'Promotion', 20, FALSE);
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'RETIREMENT', 'Retirement', 'Retirement', 30, FALSE);
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'ASSIGNMENTEND', 'End of Assignment', 'End of Assignment', 40, FALSE);
EXEC zynap_lookup_sp.install_values('SUCCESSION_REASON', 'OTHERSR', 'Other', 'Other', 50, FALSE);

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.1.0-5.2.0.sql', 49, 'subject_associations enhanced');

alter table QUESTIONS ADD IS_HIDDEN VARCHAR2(1) DEFAULT 'F';

-- compile the views
@views/zynap_views.sql
@packages/zynap_triggers_hierarchy.sql;
@packages/zynap_wf_integration_spec.sql
@packages/zynap_wf_integration_body.sql



commit;