CREATE OR REPLACE
PACKAGE BODY zynap_da_sp AS

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
    	description_ IN VARCHAR2)
IS
    new_da_id_ INTEGER;
    mod_label_ varchar2(3800);

BEGIN
     --Get the next sequence for the attribute Id
     SELECT da_sq.nextval INTO new_da_id_ FROM DUAL;
      mod_label_ := lower(label_) || new_da_id_;

     INSERT INTO dynamic_attributes (id, label, type, artefact_type, max_size, min_size, is_mandatory, is_active,
                                     is_searchable, description, refers_to, modified_label, lock_id )
            VALUES (new_da_id_, label_, UPPER(type_), UPPER(artefact_type_), max_size_, min_size_, is_mandatory_ , is_active_,
                    is_searchable_, description_, refers_to_, mod_label_, 0);
  	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
	  		 zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.duplicate.name,setOffender=roleid,setDescription=An extended attribute with given label already exists. Please select a new name');
		WHEN OTHERS THEN
		  	 zynap_error_sp.ERROR( zynap_error_sp.ERR_INTERNAL, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

END add_attribute;  

PROCEDURE add_attribute (
    	label_ IN VARCHAR2,
		type_ IN VARCHAR2,
    	artefact_type_ IN VARCHAR2,    	    	
    	refers_to_ IN VARCHAR2,
    	description_ IN VARCHAR2,
    	modified_label_ IN VARCHAR2)
IS
    new_da_id_ INTEGER;

BEGIN
     --Get the next sequence for the attribute Id
     SELECT da_sq.nextval INTO new_da_id_ FROM DUAL;
      
     INSERT INTO dynamic_attributes (id, label, type, artefact_type, max_size, min_size, is_mandatory, is_active,
                                     is_searchable, description, refers_to, modified_label, lock_id )
            VALUES (new_da_id_, label_, UPPER(type_), UPPER(artefact_type_), null, null, 'F' , 'T',
                    'T', description_, refers_to_, modified_label_, 0);
  	EXCEPTION
		WHEN DUP_VAL_ON_INDEX THEN
	  		 zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.duplicate.name,setOffender=roleid,setDescription=An extended attribute with given label already exists. Please select a new name');
		WHEN OTHERS THEN
		  	 zynap_error_sp.ERROR( zynap_error_sp.ERR_INTERNAL, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

END add_attribute;

END zynap_da_sp;
/
