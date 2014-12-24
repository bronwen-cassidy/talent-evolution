CREATE OR REPLACE
PACKAGE zynap_lookup_sp IS

------------------------------------------------------------------------------
--------------- LOOKUP TYPES -------------------------------------------------
------------------------------------------------------------------------------

--PROCEDURE create_type (
--   label_ IN VARCHAR2,
--   type_id_ OUT VARCHAR2,
--   company_id_ IN INTEGER,
--   description_ IN VARCHAR2,
--   type_ IN VARCHAR2,
--   active_ IN VARCHAR2,
--   user_id_ IN INTEGER );

-----------------------------------------------------------------------------------------
----------- Added company id to the signature - please ensure that the java reflects this
-----------------------------------------------------------------------------------------
--PROCEDURE modify_type (
--   type_id_ IN VARCHAR2,
--   label_ IN VARCHAR2,
--   company_id_ IN INTEGER,
--   description_ IN VARCHAR2,
--   type_ IN VARCHAR2,
--   active_ IN VARCHAR2,
--   user_id_ IN VARCHAR2 );

------------------------------------------------------------------------------
--------------- LOOKUP VALUES/NODES ------------------------------------------
------------------------------------------------------------------------------

--PROCEDURE create_node (
--   user_id_ IN INTEGER,
--   company_id_ IN INTEGER,
--   node_id_ OUT VARCHAR2,
--   type_id_ IN VARCHAR2,
--   short_desc_ IN VARCHAR2,
--   description_ IN VARCHAR2,
--   active_ IN VARCHAR2,
--   sort_order_ IN INTEGER );
--
--PROCEDURE modify_node (
--   id_ IN VARCHAR2,
--   type_id_ IN VARCHAR2,
--   company_id_ IN INTEGER,
--   short_desc_  IN VARCHAR2,
--   description_ IN VARCHAR2,
--   active_ IN VARCHAR2,
--   sort_order_ IN INTEGER,
--   user_id_ IN INTEGER );

------------------------------------------------------------------------------
--------------- RETRIEVAL -----------------------------------------------------
------------------------------------------------------------------------------

FUNCTION get_description (
  type_id_ IN VARCHAR2,
  id_ IN VARCHAR2 ) RETURN VARCHAR2;

FUNCTION get_short_desc (
   type_id_ IN VARCHAR2,
   id_ IN VARCHAR2 ) RETURN VARCHAR2;

/*
FUNCTION get_struct_desc (
   type_id_ IN VARCHAR2,
   id_ IN VARCHAR2 ) RETURN VARCHAR2;
*/
------------------------------------------------------------------------------
--------------- INSTALLATION/TEST --------------------------------------------
------------------------------------------------------------------------------

PROCEDURE install_type (
	id_ IN VARCHAR2,
	type_ IN VARCHAR2,
	description_ IN VARCHAR2,
	label_ IN VARCHAR2,
	is_system_ BOOLEAN DEFAULT FALSE
	);

PROCEDURE install_values (
	type_id_ IN VARCHAR,
	id_ IN VARCHAR2,
	short_desc_ IN VARCHAR2 DEFAULT NULL,
	description_ IN VARCHAR2,
	sort_order_ INTEGER,
	is_system_ BOOLEAN DEFAULT FALSE
	);

PROCEDURE install_vals (
	type_id_ IN VARCHAR,
	id_ IN VARCHAR2,
	short_desc_ IN VARCHAR2 DEFAULT NULL,	
	sort_order_ INTEGER,
	is_system_ BOOLEAN DEFAULT FALSE
	);

END zynap_lookup_sp;
/
