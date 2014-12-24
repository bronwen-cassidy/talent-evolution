connect &wf_user/&wf_user
grant execute on wf_engine to &l_user;
grant execute on wf_notification to &l_user;
grant select on WF_WORKLIST_V to &l_user;
grant select on WF_NOTIFICATION_ATTRIBUTES to &l_user;
grant select on WF_MESSAGE_ATTRIBUTES to &l_user;
grant select on WF_MESSAGE_ATTRIBUTES_TL to &l_user;
grant select on WF_MESSAGES_TL to &l_user;
grant select on wf_lookups to &l_user;
grant select on WF_LOOKUPS_TL to &l_user;
grant select on WF_ITEM_TYPES_TL to &l_user;
grant select,update on WF_NOTIFICATIONS to &l_user;
grant select,update on wf_item_activity_statuses to &l_user;
grant select,update on wf_item_activity_statuses_h to &l_user;
grant select,update on wf_items to &l_user;
grant execute on wf_purge to &l_user;



connect &l_user/&l_user
grant select on users to &wf_user;
grant select on logins to &wf_user;
grant select on core_details to &wf_user;
grant select on QUE_WF_PARTICIPANTS to &wf_user;

CREATE or REPLACE SYNONYM WF_ENGINE FOR &wf_user..WF_ENGINE;
CREATE or REPLACE SYNONYM WF_NOTIFICATION FOR &wf_user..WF_NOTIFICATION;
CREATE or REPLACE SYNONYM WF_NOTIFICATION_ATTRIBUTES FOR &wf_user..WF_NOTIFICATION_ATTRIBUTES;
CREATE or REPLACE SYNONYM WF_MESSAGE_ATTRIBUTES FOR &wf_user..WF_MESSAGE_ATTRIBUTES;
CREATE or REPLACE SYNONYM WF_MESSAGE_ATTRIBUTES_TL FOR &wf_user..WF_MESSAGE_ATTRIBUTES_TL;
CREATE or REPLACE SYNONYM WF_LOOKUPS FOR &wf_user..WF_LOOKUPS;
CREATE or REPLACE SYNONYM WF_LOOKUPS_TL FOR &wf_user..WF_LOOKUPS_TL;
CREATE or REPLACE SYNONYM WF_ITEM_TYPES_TL FOR &wf_user..WF_ITEM_TYPES_TL;
CREATE or REPLACE SYNONYM WF_NOTIFICATIONS FOR &wf_user..WF_NOTIFICATIONS;
CREATE or REPLACE SYNONYM wf_item_activity_statuses FOR &wf_user..wf_item_activity_statuses;
CREATE or REPLACE SYNONYM wf_item_activity_statuses_h FOR &wf_user..wf_item_activity_statuses_h;
CREATE or REPLACE SYNONYM wf_items FOR &wf_user..wf_items;
CREATE or REPLACE SYNONYM wf_purge FOR &wf_user..wf_purge;
CREATE or REPLACE SYNONYM WF_MESSAGES_TL FOR &wf_user..WF_MESSAGES_TL;
CREATE or REPLACE SYNONYM WF_MONITOR FOR &wf_user..WF_MONITOR;
CREATE or REPLACE SYNONYM FND_DOCUMENT_MANAGEMENT FOR &wf_user..FND_DOCUMENT_MANAGEMENT;

CREATE OR REPLACE VIEW WF_MESSAGE_ATTRIBUTES_VL
  ("ROW_ID","MESSAGE_TYPE","MESSAGE_NAME","NAME","SEQUENCE",
  "TYPE","SUBTYPE","VALUE_TYPE","PROTECT_LEVEL","CUSTOM_LEVEL",
  "FORMAT","TEXT_DEFAULT","NUMBER_DEFAULT","DATE_DEFAULT",
  "DISPLAY_NAME","DESCRIPTION","ATTACH") AS
  select /* $Header: wfntfv.sql 26.4 2001/05/02 05:44:08 dlam
  ship $ */
  B.ROWID ROW_ID,
  B.MESSAGE_TYPE,
  B.MESSAGE_NAME,
  B.NAME,
  B.SEQUENCE,
  B.TYPE,
  B.SUBTYPE,
  B.VALUE_TYPE,
  B.PROTECT_LEVEL,
  B.CUSTOM_LEVEL,
  B.FORMAT,
  B.TEXT_DEFAULT,
  B.NUMBER_DEFAULT,
  B.DATE_DEFAULT,
  T.DISPLAY_NAME,
  T.DESCRIPTION,
  B.ATTACH
from WF_MESSAGE_ATTRIBUTES B, WF_MESSAGE_ATTRIBUTES_TL T
where B.MESSAGE_NAME = T.MESSAGE_NAME
and B.MESSAGE_TYPE = T.MESSAGE_TYPE
and B.NAME = T.NAME ;


@packages/zynap_wf_integration_spec.sql;

CREATE OR REPLACE VIEW WF_WORKLIST_V ("ROW_ID","NID",
    "PRIORITY","MESSAGE_TYPE","RECIPIENT_ROLE","SUBJECT",
    "BEGIN_DATE","DUE_DATE","END_DATE","DISPLAY_STATUS","STATUS",
    "ORIGINAL_RECIPIENT","ITEM_TYPE","MESSAGE_NAME","FROM_USER",
    "TO_USER","LANGUAGE","MORE_INFO_ROLE","URL","ACTIONABLE",
    "WORKFLOW_ID","WORKFLOW_NAME", "SUBJECT_ID", "SUBJECT_NAME", "TARGET","ROLE_ID", "ROLE_NAME") AS
    select /* $Header: wfntfv.sql 26.4 2001/05/02 05:44:08 dlam
    ship $ */
  WN.ROWID,
  WN.NOTIFICATION_ID,
  WN.PRIORITY,
  WIT.DISPLAY_NAME,
  WN.RECIPIENT_ROLE,
  wf_integration.GetSubject(WN.notification_id),
  WN.BEGIN_DATE,
  WN.DUE_DATE,
  WN.END_DATE,
  WL.MEANING,
  WN.STATUS,
  WN.ORIGINAL_RECIPIENT,
  WN.MESSAGE_TYPE,
  WN.MESSAGE_NAME,
  WN.FROM_USER,
  WN.TO_USER,
  WN.LANGUAGE,
  WN.MORE_INFO_ROLE,
  wf_integration.GetUrl(WN.notification_id),
  wf_integration.getActionable(WN.notification_id),
  wf_integration.getWorkflowId(WN.notification_id),
  wf_integration.getWorkflowName(WN.notification_id),
  wf_integration.getSubjectId(WN.notification_id),
  wf_integration.getSubjectName(WN.notification_id),
  wf_integration.getTarget(WN.notification_id),
  wf_integration.getRoleId(WN.notification_id),
  wf_integration.getRoleName(WN.notification_id)
 from WF_NOTIFICATIONS WN, WF_ITEM_TYPES_TL WIT, WF_LOOKUPS_TL WL
 where WN.MESSAGE_TYPE = WIT.NAME
  and WL.LOOKUP_TYPE = 'WF_NOTIFICATION_STATUS'
  and WN.STATUS = WL.LOOKUP_CODE;

CREATE OR REPLACE VIEW WF_NOTIFICATION_ACTIONS (
    "LOOKUP_CODE","LOOKUP_TYPE","MEANING","NID") AS
    Select
	   WfLookups.LOOKUP_CODE,
       WfLookups.LOOKUP_TYPE,
       WfLookups.MEANING,
       worklist.nid
   From WF_LOOKUPS_TL WfLookups, WF_MESSAGE_ATTRIBUTES WfMessageAttributesVl, wf_worklist_v worklist
   Where WfMessageAttributesVl.type = 'LOOKUP' and WfLookups.lookup_type = WfMessageAttributesVl.format
   and WfMessageAttributesVl.message_type = worklist.item_type
   and WfMessageAttributesVl.MESSAGE_NAME = worklist.message_name;


@packages/zynap_wf_integration_body.sql;
grant execute on wf_integration to &wf_user;

connect &wf_user/&wf_user

CREATE or REPLACE SYNONYM wf_integration FOR &l_user..wf_integration;

CREATE OR REPLACE VIEW WF_USERS ("NAME",
    "DISPLAY_NAME","DESCRIPTION","NOTIFICATION_PREFERENCE",
    "LANGUAGE","TERRITORY","EMAIL_ADDRESS","FAX","ORIG_SYSTEM",
    "ORIG_SYSTEM_ID","STATUS","EXPIRATION_DATE") AS select	/*
    $Header: wfdirouv.sql 26.0 2000/07/05 22:54:09 kma ship $ */
        username,
	username,
	username,
	NVL(wf_pref.get_pref(d.username,'MAILTYPE'),'MAILHTML'),
	NVL(wf_pref.get_pref(d.username,'LANGUAGE'),l.nls_language),
	NVL(wf_pref.get_pref(d.username,'TERRITORY'),l.nls_territory),
	username,
	'',
	'ORACLE',
	d.user_id,
        'ACTIVE',
	to_date(NULL)
from 	dba_users d, wf_languages l
where l.code=userenv('LANG')
union  all
select l.username ,
       c.first_name || ' ' || c.second_name,
       c.first_name || ' ' || c.second_name,
       'MAILHTML',
       'AMERICAN',
       'AMERICA',
       c.contact_email,
       '',
       'ZYNAP',
       u.id,
       'ACTIVE',
       null
from  &l_user..users u, &l_user..logins l, &l_user..core_details c
where u.id = l.user_id and u.cd_id = c.id;
alter view wf_roles compile;
alter view wf_user_roles compile;





