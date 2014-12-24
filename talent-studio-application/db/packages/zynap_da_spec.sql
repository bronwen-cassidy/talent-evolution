CREATE OR REPLACE
PACKAGE zynap_da_sp IS

---------------------------------------------------------------------
--- Add a New Dynamic Attribute
---------------------------------------------------------------------

 PROCEDURE add_attribute (
    	label_ IN VARCHAR2,
		type_ IN VARCHAR2,
    	artefact_type_ IN VARCHAR2,
    	max_size_ IN VARCHAR2,
    	min_size_ IN VARCHAR2,
    	is_mandatory_ IN VARCHAR2,
		is_active_ IN VARCHAR2,
    	is_searchable_ IN VARCHAR2,
    	refers_to_ IN VARCHAR2,
    	description_ IN VARCHAR2
 );

 PROCEDURE add_attribute (
    	label_ IN VARCHAR2,
		type_ IN VARCHAR2,
    	artefact_type_ IN VARCHAR2,    	
    	refers_to_ IN VARCHAR2,
    	description_ IN VARCHAR2,
    	modified_label_ IN VARCHAR2
 );

END zynap_da_sp;
/
