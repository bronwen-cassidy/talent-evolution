CREATE OR REPLACE
PACKAGE BODY zynap_app_sp IS

-----------------------------------------------
FUNCTION get_admin_module_id RETURN VARCHAR2
IS
BEGIN
   RETURN 'ADMINMODULE';
END get_admin_module_id;

-------------------------------------------------
FUNCTION get_myzynap_module_id RETURN VARCHAR2
IS
BEGIN
   RETURN 'MYZYNAPMODULE';
END get_myzynap_module_id;

---------------------------------------------------
FUNCTION get_organisation_module_id RETURN VARCHAR2
IS
BEGIN
   RETURN 'ORGANISATIONMODULE';
END get_organisation_module_id;

----------------------------------------------------
FUNCTION get_talent_module_id RETURN VARCHAR2
IS
BEGIN
    RETURN 'TALENTIDENTIFIERMODULE';
END get_talent_module_id;

-----------------------------------------------------
FUNCTION get_succession_module_id RETURN VARCHAR2
IS
BEGIN
    RETURN 'SUCCESSIONMODULE';
END get_succession_module_id;

------------------------------------------------------
FUNCTION get_performance_man_module_id RETURN VARCHAR2
IS
BEGIN
    RETURN 'PERFMANMODULE';
END get_performance_man_module_id;

-------------------------------------------------------
FUNCTION get_system_user RETURN INTEGER
IS
BEGIN
   RETURN 0;
END get_system_user;

--------------------------------------------------------
FUNCTION get_admin_user RETURN INTEGER
IS
BEGIN
   RETURN 1;
END get_admin_user;



END zynap_app_sp;
/
