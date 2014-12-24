set echo off
-- set serveroutput off
set verify off

-- Ignore error if user does not exist

WHENEVER SQLERROR CONTINUE
drop user &l_user cascade;

-- Fail on all errors, returning a system error

WHENEVER SQLERROR EXIT FAILURE

-- Create new user with required permissions

create user &l_user identified by &l_user
default tablespace TS_LARGE
temporary tablespace TEMP;

--alter user &l_user quota unlimited on TS_SMALL;
alter user &l_user quota unlimited on TS_LARGE;
--alter user &l_user quota unlimited on TS_SMALL_IDX;
--alter user &l_user quota unlimited on TS_LARGE_IDX;

grant create table to &l_user;
grant create procedure to &l_user;
grant create session to &l_user;
grant create database link to &l_user;
grant create sequence to &l_user;
grant create view to &l_user;
grant create trigger to &l_user;
grant create synonym to &l_user;
grant create public synonym to &l_user;
grant create any trigger to &l_user;
grant create any view to &l_user;
grant on commit refresh to &l_user;
grant create snapshot to &l_user;


