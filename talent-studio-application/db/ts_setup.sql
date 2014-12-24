-- ***************************************************************************
-- Created by Andreas Andersson for Zynap Ltd 2004
-- Version: 1.0
-- Description: Installs Talent Studio database schema and PL/SQL packages
-- ***************************************************************************

-- This script should be run under SQL/Plus or Golden/Toad
-- A user should already have been created,
-- and the scripts should be run logged in as that user
-- Note if you run it from SQL Plus then make sure that file references are remembered,
-- this is easiest done by opening this file in SQL Plus before executing it.
-- ***************************************************************************


-- Create all tables
-- @tables/admin_tables.sql
@tables/tsmodel.sql
@tables/alter_tables.sql


-- Create views
@views/zynap_views.sql
@views/autonomy_views.sql
@views/security_views.sql

-- Create package specifications
@packages/zynap_hierarchy_spec.sql
@packages/zynap_app_spec.sql
@packages/zynap_log_spec.sql
@packages/zynap_error_spec.sql
@packages/zynap_node_spec.sql
@packages/zynap_auth_spec.sql
@packages/zynap_position_spec.sql
@packages/zynap_nav_spec.sql
@packages/zynap_lookup_spec.sql
@packages/zynap_subject_spec.sql
@packages/zynap_da_spec.sql
@packages/zynap_org_unit_spec.sql
@packages/zynap_admin_spec.sql
@packages/zynap_loader_spec.sql
@packages/zynap_wf_integration_spec.sql

-- Create package bodies
@packages/zynap_hierarchy_body.sql
@packages/zynap_app_body.sql
@packages/zynap_log_body.sql
@packages/zynap_error_body.sql
@packages/zynap_node_body.sql
@packages/zynap_auth_body.sql
@packages/zynap_position_body.sql
@packages/zynap_nav_body.sql
@packages/zynap_lookup_body.sql
@packages/zynap_subject_body.sql
@packages/zynap_da_body.sql
@packages/zynap_org_unit_body.sql
@packages/zynap_admin_body.sql
@packages/zynap_loader_body.sql
@packages/zynap_wf_integration_body.sql

-- triggers
@packages/zynap_triggers_hierarchy.sql

COMMIT;




