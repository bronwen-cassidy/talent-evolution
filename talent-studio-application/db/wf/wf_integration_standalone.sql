set echo off
set serveroutput off
set verify off

prompt Enter the name of talentstudio schema
accept l_user char prompt 'User id: '

prompt Enter the name of the WF user.
accept wf_user char prompt 'WF Schema: '

@@wf_integration.sql

quit
