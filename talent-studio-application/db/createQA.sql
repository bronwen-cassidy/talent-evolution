set echo off
set serveroutput off
set verify off

prompt CREATE NEW TALENT STUDIO USER AND SCHEMA

prompt
prompt Enter the name of the user to create. An existing user will be deleted
prompt
accept l_user char prompt 'User id: '

prompt Creating schema for user &l_user
prompt

@@createUser.sql

-- Connect as the new user

connect &l_user/&l_user

-- Create TalentStudio schema

define table_space_for_large_tables = 'TS_LARGE'
--define table_space_for_small_tables = 'TS_SMALL'
--define table_space_for_large_indexes = 'TS_LARGE_IDX'
--define table_space_for_small_indexes = 'TS_SMALL_IDX'

@@ts_setup.sql
@@ts_inserts.sql

quit
