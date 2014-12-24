--------------------------------------------------------
-- 10/07/08
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

create table QUE_XML_DATA
(
    ID NUMBER NOT NULL
   ,XML_DEFINITION BLOB NOT NULL
)
/

PROMPT Creating Primary Key on 'QUE_XML_DATA'
ALTER TABLE QUE_XML_DATA
 ADD (CONSTRAINT QUE_XD_PK PRIMARY KEY
  (ID))
/

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.0.0-5.1.0.sql', 13, 'QUE_XML_DATA Table Created');

-- copy the blob definitions into the new table from the que_definitions table
insert INTO QUE_XML_DATA (ID, XML_DEFINITION)
select id, XML_DEFINITION from QUE_DEFINITIONS where length(XML_DEFINITION) > 0;

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.0.0-5.1.0.sql', 25, 'Copied xml_definition blob columns into QUE_XML_DATA ');

-- drop the xml_definition column
ALTER TABLE QUE_DEFINITIONS DROP COLUMN XML_DEFINITION;

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.0.0-5.1.0.sql', 31, 'Dropped Column xml_definition from QUE_DEFINITIONS Table');

---------------------------------------------------------------------------------
-- removing the blobs from node_items
create table NODE_ITEM_BLOBS
(
    ID NUMBER NOT NULL
   ,BLOB_VALUE BLOB NOT NULL
)
/

PROMPT Creating Primary Key on 'NODE_ITEM_BLOBS'
ALTER TABLE NODE_ITEM_BLOBS
 ADD (CONSTRAINT NI_BLB_PK PRIMARY KEY
  (ID))
/

insert INTO NODE_ITEM_BLOBS (ID, BLOB_VALUE)
select id, BLOB_VALUE from NODE_ITEMS where length(BLOB_VALUE) > 0;

-- drop the xml_definition column
ALTER TABLE NODE_ITEMS DROP COLUMN BLOB_VALUE;

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.0.0-5.1.0.sql', 25, 'Copied blob_value blob columns from node_items into NODE_ITEM_BLOBS');

-- primary key for the subject_pictures table
ALTER TABLE SUBJECT_PICTURES
 ADD (CONSTRAINT S_PICT_PK PRIMARY KEY
  (SUBJECT_ID))
/

-- cascade delete for questionnaire_definitions

ALTER TABLE MULTI_QUESTIONS DROP CONSTRAINT MQN_QGP_FK;
ALTER TABLE QUESTIONS DROP CONSTRAINT QUN_QGP_FK;
ALTER TABLE QUESTIONS DROP CONSTRAINT QUN_QLM_FK;
ALTER TABLE QUESTIONS DROP CONSTRAINT QUN_ZDT_FK;
ALTER TABLE QUESTION_GROUPS DROP CONSTRAINT QGP_QDL_FK;
ALTER TABLE QUESTION_LINE_ITEMS DROP CONSTRAINT QLM_MQN_FK;
ALTER TABLE QUE_DEFINITION_MODELS DROP CONSTRAINT QUE_DEF_ID;
ALTER TABLE DYNAMIC_ATTR_REFERENCES DROP CONSTRAINT DA_REF_FK;
ALTER TABLE DYNAMIC_ATTR_REFERENCES DROP CONSTRAINT DA_PA_REF_FK;
ALTER TABLE DYNAMIC_ATTR_REFERENCES DROP CONSTRAINT PA_DA_REF_FK;

ALTER TABLE MULTI_QUESTIONS ADD (CONSTRAINT
 MQN_QGP_FK FOREIGN KEY
  (QUE_GROUP_ID) REFERENCES QUESTION_GROUPS
  (ID) ON DELETE CASCADE)
/

ALTER TABLE QUESTIONS ADD (CONSTRAINT
 QUN_QGP_FK FOREIGN KEY
  (QUE_GROUP_ID) REFERENCES QUESTION_GROUPS
  (ID) ON DELETE CASCADE)
/

ALTER TABLE QUESTIONS ADD (CONSTRAINT
 QUN_QLM_FK FOREIGN KEY
  (QUESTION_LINE_ITEM_ID) REFERENCES QUESTION_LINE_ITEMS
  (ID) ON DELETE CASCADE)
/

ALTER TABLE QUESTIONS ADD (CONSTRAINT
 QUN_ZDT_FK FOREIGN KEY
  (DA_ID) REFERENCES DYNAMIC_ATTRIBUTES
  (ID) ON DELETE CASCADE)
/

ALTER TABLE QUESTION_GROUPS ADD (CONSTRAINT
 QGP_QDL_FK FOREIGN KEY
  (QUE_DEF_MODEL_ID) REFERENCES QUE_DEFINITION_MODELS
  (QUE_DEF_ID) ON DELETE CASCADE)
/

ALTER TABLE QUESTION_LINE_ITEMS ADD (CONSTRAINT
 QLM_MQN_FK FOREIGN KEY
  (MULTI_QUESTION_ID) REFERENCES MULTI_QUESTIONS
  (ID) ON DELETE CASCADE)
/

ALTER TABLE QUE_DEFINITION_MODELS ADD (CONSTRAINT
 QUE_DEF_ID FOREIGN KEY
  (QUE_DEF_ID) REFERENCES QUE_DEFINITIONS
  (ID) ON DELETE CASCADE)
/

ALTER TABLE DYNAMIC_ATTR_REFERENCES ADD (CONSTRAINT
 DA_REF_FK FOREIGN KEY
  (REFERENCE_DA_ID) REFERENCES DYNAMIC_ATTRIBUTES
  (ID) ON DELETE CASCADE)
/

ALTER TABLE DYNAMIC_ATTR_REFERENCES ADD (CONSTRAINT
 DA_PA_REF_FK FOREIGN KEY
  (PARENT_DA_ID) REFERENCES DYNAMIC_ATTRIBUTES
  (ID) ON DELETE CASCADE)
/

ALTER TABLE DYNAMIC_ATTR_REFERENCES ADD (CONSTRAINT
 PA_DA_REF_FK FOREIGN KEY
  (PARENT_MAPPING_ID) REFERENCES DYNAMIC_ATTR_REFERENCES
  (ID) ON DELETE CASCADE)
/

drop package zynap_utilities_sp;

-- recompile the triggers and the auth packages
@packages/zynap_node_spec.sql
@packages/zynap_node_body.sql
@packages/zynap_auth_spec.sql
@packages/zynap_auth_body.sql
@packages/zynap_wf_integration_spec.sql
@packages/zynap_wf_integration_body.sql
@packages/zynap_triggers_hierarchy.sql

@views/security_views.sql
@views/autonomy_views.sql


ALTER PACKAGE ZYNAP_AUTH_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_AUTH_SP COMPILE BODY;
ALTER PACKAGE ZYNAP_NODE_SP COMPILE SPECIFICATION;
ALTER PACKAGE ZYNAP_NODE_SP COMPILE BODY;

insert into upgrade_status ( date_executed, script_version, script_line_number, comments)
values (sysdate, 'upgrade5.0.0-5.1.0.sql', 42, 'Compile auth packages and triggers');

commit;