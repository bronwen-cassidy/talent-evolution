CREATE OR REPLACE PACKAGE BODY zynap_nav_sp IS

-----------------------------------------------------------------------------
-- NOTE -- NOTE -- NOTE -- NOTE -- NOTE -- NOTE -- NOTE -- NOTE
-- Needs some thinking here if we like to build admin screen to manage modules, sections and menues
-- Also companies needs to be involved
-- All code comment out to enable package to compile
-- Need re-work!!! - Maybe a install module procedure
-- Maybe this should not be a admin role but instead a SUPERUSER ROLE - def only for ZYNAP STAFF
-- Andreas
------------------------------------------------------------------------------

------------------------------------------------------------------------------
--------------- MODULE/MENU SECTIONS -----------------------------------------------
------------------------------------------------------------------------------
PROCEDURE create_menu_section (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   label_ IN VARCHAR2,
   sort_order_ IN VARCHAR2 )
IS
BEGIN
null;
/*
      ----- VALIDATE ----------------------------------------------------------

   zynap_auth_sp.check_user_in_role( user_id_, 'ADMIN');

   zynap_field_sp.check_field_size( module_id_, 1, 50, 'Menu Id');
   zynap_field_sp.check_field_size( section_id_, 1, 50, 'Section Id');
   zynap_field_sp.check_field_size( label_, 1, 100, 'Label');
   zynap_field_sp.validate_number( sort_order_, 0, 999999, 'Sort Order');

   zynap_field_sp.validate_key_format( module_id_, 'Menu Id');
   zynap_field_sp.validate_key_format( section_id_, 'Section Id');

      ----- PERFORM ACTION ----------------------------------------------------

   INSERT INTO zynap_module_menu_section_tb (
      module_id, section_id, module_label, sort_order )
   VALUES( module_id_, section_id_, label_, sort_order_);

      ----- LOG ---------------------------------------------------------------

   zynap_log_sp.log( 'ADMIN', user_id_, 'Created module menu section ' || module_id_ || ' - ' || section_id_);
   -- ??? zynap_log_sp.log( 'SYS', user_id_, 'Created menu section ' || menu_id_ || ' - ' || section_id_);
*/
END create_menu_section;

------------------------------------------------------------------------------


PROCEDURE modify_menu_section (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   label_ IN VARCHAR2,
   sort_order_ IN VARCHAR2 )
IS
BEGIN
null;
/*
      ----- VALIDATE ----------------------------------------------------------

   zynap_auth_sp.check_user_in_role( user_id_, 'ADMIN');

   zynap_field_sp.check_field_size( label_, 1, 100, 'Label');
   zynap_field_sp.validate_number( sort_order_, 0, 999999, 'Sort Order');

      ----- PERFORM ACTION ----------------------------------------------------

   UPDATE zynap_module_menu_section_tb SET module_label = label_, sort_order = sort_order_
   WHERE meodule_id = module_id_ AND section_id = section_id_;

      ----- LOG ---------------------------------------------------------------
   zynap_log_sp.log( 'ADMIN', user_id_, 'Modified menu section ' || module_id_ || ' - ' || section_id_);
   -- ??? zynap_log_sp.log( 'SYS', user_id_, 'Modified menu section ' || module_id_ || ' - ' || section_id_);
   
*/
END modify_menu_section;

------------------------------------------------------------------------------


PROCEDURE drop_menu_section (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2 )
IS
BEGIN
null;
/*
      ----- VALIDATE ----------------------------------------------------------

   zynap_auth_sp.check_user_in_role( user_id_, 'ADMIN');

      ----- PERFORM ACTION ----------------------------------------------------
    
   DELETE FROM zynap_module_menu_item_tb WHERE module_id = module_id_ AND section_id = section_id_;
   DELETE FROM zynap_module_menu_section_tb WHERE module_id = module_id_ AND section_id = section_id_;

      ----- LOG ---------------------------------------------------------------
	
	zynap_log_sp.log( 'ADMIN', user_id_, 'Deleted module/menu section ' || module_id_ || ' - ' || section_id_);
   -- ??? zynap_log_sp.log( 'SYS', user_id_, 'Deleted module/menu section ' || module_id_ || ' - ' || section_id_);
*/
END drop_menu_section;


------------------------------------------------------------------------------
--------------- MODULE/MENU ITEMS --------------------------------------------------
------------------------------------------------------------------------------

PROCEDURE create_menu_item (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   label_ IN VARCHAR2,
   url_ IN VARCHAR2,
   role_id_ IN VARCHAR2,
   sort_order_ IN VARCHAR2 )
IS
   dummy_ INTEGER;
BEGIN
null;
/*
      ----- VALIDATE ----------------------------------------------------------

   zynap_auth_sp.check_user_in_role( user_id_, 'ADMIN');

   zynap_field_sp.check_field_size( module_id_, 1, 50, 'Menu Id');
   zynap_field_sp.check_field_size( section_id_, 1, 50, 'Section Id');
   zynap_field_sp.check_field_size( label_, 1, 100, 'Label');
   zynap_field_sp.check_field_size( url_, 0, 100, 'Url');
   zynap_field_sp.validate_number( sort_order_, 0, 999999, 'Sort Order');

   zynap_field_sp.validate_key_format( module_id_, 'Menu Id');
   zynap_field_sp.validate_key_format( section_id_, 'Section Id');

	-- If we connect menu items to role id's the perform the following 		
--   IF role_id_ IS NOT NULL THEN
--      SELECT 1 INTO dummy_ FROM zynap_role_tb WHERE role_id = role_id_;
--      IF dummy_ IS NULL THEN
--         zynap_error_sp.ERROR( ERR_FORMVAL, 'The specified security role is invalid');
--      END IF;
--   END IF;

      ----- PERFORM ACTION ----------------------------------------------------

   SELECT NVL( MAX( menu_item_id) + 1, 1) INTO dummy_
      FROM zynap_module_menu_item_tb WHERE module_id = module_id_ AND section_id = section_id_;

   --INSERT INTO zynap_module_menu_item_tb  (
   --   module_id, section_id, menu_item_id, module_label, image, url, role_id, sort_order )
   --VALUES( module_id_, section_id_, dummy_, label_, url_, role_id_, sort_order_);
   
   INSERT INTO zynap_module_menu_item_tb  (
   --   module_id, section_id, menu_item_id, module_label, image, url, sort_order )
   --VALUES( module_id_, section_id_, dummy_, label_, url_, sort_order_);

      ----- LOG ---------------------------------------------------------------
	zynap_log_sp.log( 'ADMIN', user_id_, 'Created menu item ' || module_id_ || ' - ' || section_id_ || ' - ' || label_);
   -- ??? zynap_log_sp.log( 'SYS', user_id_, 'Created menu item ' || module_id_ || ' - ' || section_id_ || ' - ' || label_);

*/
END create_menu_item;

------------------------------------------------------------------------------


PROCEDURE modify_menu_item (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   menu_item_id_ IN INTEGER,
   label_ IN VARCHAR2,
   url_ IN VARCHAR2,
   role_id_ IN VARCHAR2,
   sort_order_ IN VARCHAR2 )
IS
   dummy_ INTEGER;
BEGIN
null;
-- TBD, Andreas
END modify_menu_item;

------------------------------------------------------------------------------


PROCEDURE drop_menu_item (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   menu_item_id_ IN INTEGER )
IS
BEGIN
null;
/*
      ----- VALIDATE ----------------------------------------------------------

   zynap_auth_sp.check_user_in_role( user_id_, 'ADMIN');

      ----- PERFORM ACTION ----------------------------------------------------

   DELETE FROM zynap_module_menu_item_tb WHERE module_id = module_id_ AND section_id = section_id_ AND menu_item_id = menu_item_id_;

      ----- LOG ---------------------------------------------------------------
   zynap_log_sp.log( 'ADMIN', user_id_, 'Deleted menu item ' || module_id_ || ' - ' || section_id_ || ' - ' || menu_item_id_);
   -- ??? zynap_log_sp.log( 'SYS', user_id_, 'Deleted menu item ' || module_id_ || ' - ' || section_id_ || ' - ' || menu_item_id_);
   
*/
END drop_menu_item;

END zynap_nav_sp;
/
