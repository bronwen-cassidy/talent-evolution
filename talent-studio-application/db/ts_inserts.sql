--------------------------------------------------------------------------------------------------
-- Product:			Talent Studio
-- Description:		Main script for setting up Talent Studio with default data, such as a default company, user etc.
--
-- Pre-condition:	Database must have been set up (ts_setup.sql)
-- Note:            This script should be run under SQL *Plus or Golden/Toad
-- 					A user should already have been created, and the scripts should be run logged in as that user.
-- 					If you run it from SQL Plus then make sure that file references are remembered, this is easiest 
--					done by opening this file in SQL *Plus before executing it.
-- Version: 		1.0
-- Since:			27/05/2004
-- Author: 			Andreas Andersson
--------------------------------------------------------------------------------------------------

  -- Definitions used during installation
DEFINE SYSUSER = 0
DEFINE SYSCOMPANY = 0
DEFINE SYSORGUNIT = 0
DEFINE SYSPOSITION = 0
DEFINE ADMINUSER = 1

@data/ts_install_modules.sql
@data/ts_install_permits.sql
@data/ts_install_def_users.sql
@data/ts_install_def_roles.sql
@data/ts_install_content_types.sql
@data/ts_install_def_config.sql
@data/ts_install_lookups.sql
@data/ts_install_attributes.sql
@data/ts_install_default_security.sql
@data/ts_install_def_analysis.sql
@data/ts_install_def_display_config.sql

COMMIT;


