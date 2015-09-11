--------------------------------------------------------
-- 20/07/2014
--------------------------------------------------------
prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

ALTER TABLE LOOKUP_VALUES ADD REQUIRES VARCHAR2(1000);
ALTER TABLE LOOKUP_VALUES ADD LINK_ID VARCHAR2(1000);

@views/zynap_views.sql

ALTER TABLE OBJECTIVES MODIFY DATE_CREATED DATE DEFAULT SYSDATE;
ALTER TABLE SECURITY_DOMAINS MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';
ALTER TABLE RULES MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';
ALTER TABLE DISPLAY_CONFIG_ITEMS MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';
ALTER TABLE DISPLAY_CONFIG_ITEMS MODIFY HIDEABLE VARCHAR2(1) DEFAULT 'F';
ALTER TABLE USERS MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';
ALTER TABLE USERS MODIFY IS_ROOT VARCHAR2(1) DEFAULT 'F';
ALTER TABLE ROLES MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';
ALTER TABLE ROLES MODIFY IS_SYSTEM VARCHAR2(1) DEFAULT 'F';
ALTER TABLE LOOKUP_VALUES MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';
ALTER TABLE LOGINS MODIFY LOCKED VARCHAR2(1) DEFAULT 'F';
ALTER TABLE LOGINS MODIFY FORCE_PWD_CHANGE VARCHAR2(1) DEFAULT 'F';
ALTER TABLE CONFIGS MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';

ALTER TABLE PERMITS DROP COLUMN IS_AVAILABLE;
ALTER TABLE PERMITS ADD IS_ACTIVE VARCHAR(1) DEFAULT 'T';

ALTER TABLE NODES MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';
ALTER TABLE MODULES MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';
ALTER TABLE MODULES MODIFY IS_HIDEABLE VARCHAR2(1) DEFAULT 'F';
ALTER TABLE DYNAMIC_ATTRIBUTES MODIFY IS_MANDATORY VARCHAR2(1) DEFAULT 'F';
ALTER TABLE DYNAMIC_ATTRIBUTES MODIFY IS_ACTIVE VARCHAR2(1) DEFAULT 'T';
ALTER TABLE DYNAMIC_ATTRIBUTES MODIFY IS_SEARCHABLE VARCHAR2(1) DEFAULT 'T';

insert into permits(ID, TYPE, ACTION, CONTENT, DESCRIPTION, IS_ACTIVE, URL, ID_PARAM, CLASS, METHOD)
VALUES (PERMIT_SQ.nextval, 'AP', 'search','QUESTIONNAIRES' ,'Permission to Export Questionnaires', 'T','/orgbuilder/exportallsubjectquestionnaires.htm', null, null, null) ;

insert into permits_roles select id, '3' from permits where url = '/orgbuilder/exportallsubjectquestionnaires.htm' AND type = 'AP';

CREATE OR REPLACE VIEW SURVEY_LIST AS
  select sub_spa.subject_id, cd.first_name, cd.second_name, l.username as manager_name, l.user_id as manager_id, qwp.que_wf_id
  from subject_primary_associations sub_spa,
    que_wf_participants qwp, positions p,
    subject_primary_associations boss_spa,
    subjects bosses,
    subjects subordinates,
    core_details cd,
    logins l,
    lookup_values lv
  where qwp.subject_id = sub_spa.subject_id
        and sub_spa.position_id = p.node_id
        and p.parent_id = boss_spa.position_id
        and sub_spa.value_id=lv.id
        and lv.value_id='PERMANENT'
        and lv.type_id='POSITIONSUBJECTASSOC'
        and sub_spa.subject_id = subordinates.node_id
        and subordinates.cd_id = cd.id
        and boss_spa.subject_id = bosses.node_id
        and bosses.user_id = l.user_id
  WITH READ ONLY;

commit;
