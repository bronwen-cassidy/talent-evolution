set echo off
set serveroutput off
set verify off

prompt CREATE NEW TALENT STUDIO USER
prompt
prompt Enter the name of the user to create. An existing user will be deleted
prompt
accept l_user char prompt 'User id: '
prompt
prompt Creating schema for user &l_user
prompt

@@createUser.sql

quit
