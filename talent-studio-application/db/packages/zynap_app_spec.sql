CREATE OR REPLACE
PACKAGE zynap_app_sp IS

---------------------------------------------------------------------------------------------
-- Description - Sets a return value of ADMINMODULE.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
FUNCTION get_admin_module_id RETURN VARCHAR2;

---------------------------------------------------------------------------------------------
-- Description - Sets a return value of MYZYNAPMODULE.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
FUNCTION get_myzynap_module_id RETURN VARCHAR2;

---------------------------------------------------------------------------------------------
-- Description - Sets a return value of ORGANISATIONMODULE.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
FUNCTION get_organisation_module_id RETURN VARCHAR2;

---------------------------------------------------------------------------------------------
-- Description - Sets a return value of TALENTIDENTIFIERMODULE
-- Exception none
---------------------------------------------------------------------------------------------
FUNCTION get_talent_module_id RETURN VARCHAR2;

---------------------------------------------------------------------------------------------
-- Description - Sets a return value of SUCCESSIONMODULE
-- Exception none
---------------------------------------------------------------------------------------------
FUNCTION get_succession_module_id RETURN VARCHAR2;

---------------------------------------------------------------------------------------------
-- Description - Sets a return value of PERFMANMODULE
-- Exception none
---------------------------------------------------------------------------------------------
FUNCTION get_performance_man_module_id RETURN VARCHAR2;

---------------------------------------------------------------------------------------------
-- Description - Sets a return value of 0.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
FUNCTION get_system_user RETURN INTEGER;

---------------------------------------------------------------------------------------------
-- Description - Sets a return value of 1.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
FUNCTION get_admin_user RETURN INTEGER;

END zynap_app_sp;
/
