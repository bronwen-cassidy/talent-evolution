CREATE OR REPLACE PACKAGE zynap_nav_sp IS

------------------------------------------------------------------------------
--------------- MENU SECTIONS -----------------------------------------------
------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------
-- Description - Currently set to null.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
PROCEDURE create_menu_section (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   label_ IN VARCHAR2,
   sort_order_ IN VARCHAR2 );

---------------------------------------------------------------------------------------------
-- Description - Currently set to null.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
PROCEDURE modify_menu_section (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   label_ IN VARCHAR2,
   sort_order_ IN VARCHAR2 );

---------------------------------------------------------------------------------------------
-- Description - Currently set to null.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
PROCEDURE drop_menu_section (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2 );


------------------------------------------------------------------------------
--------------- MENU ITEMS --------------------------------------------------
------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------
-- Description - Currently set to null.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
PROCEDURE create_menu_item (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   label_ IN VARCHAR2,
   url_ IN VARCHAR2,
   role_id_ IN VARCHAR2,
   sort_order_ IN VARCHAR2 );

---------------------------------------------------------------------------------------------
-- Description - Currently set to null.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
PROCEDURE modify_menu_item (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   menu_item_id_ IN INTEGER,
   label_ IN VARCHAR2,
   url_ IN VARCHAR2,
   role_id_ IN VARCHAR2,
   sort_order_ IN VARCHAR2 );

---------------------------------------------------------------------------------------------
-- Description - Currently set to null.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
PROCEDURE drop_menu_item (
   user_id_ IN INTEGER,
   company_id_ IN INTEGER,
   module_id_ IN VARCHAR2,
   section_id_ IN VARCHAR2,
   menu_item_id_ IN INTEGER );

END zynap_nav_sp;
/
