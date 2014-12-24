set echo off
set serveroutput off
set verify off

--This procedure was built to apply patches.
--It allows to put references to other scripts (like the ones to build stored procedures)
--in the patch and keep the directory structure.
prompt
prompt Enter the name of the Patch.
prompt
accept upgrade char prompt 'Name of the Patch: '



@@upgrades/&upgrade
commit;


quit
