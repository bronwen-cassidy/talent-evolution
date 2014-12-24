CREATE OR REPLACE
PACKAGE BODY zynap_lookup_sp IS

-- INSTALLATION OF A TYPE  VALUES
-- INSERT INTO zynap_lookup_type_tb (x,x,x,...) VALUES( 'SUBJECTSUBTYPES', x,x,x....);
--INSERT INTO zynap_lookup_value_tb (type_id,id,description,is_active,last_updated,last_updated_by)
-- VALUES( 'SUBJECTSUBTYPES', 'EMPLOYEE', 'Employess', 'T', SYSDATE, '"systemuser or whoever creates the value"))
--INSERT INTO zynap_lookup_value_tb (type_id,id,description,is_active,last_updated,last_updated_by)
-- VALUES( 'SUBJECTSUBTYPES', 'ANOTHERIDIOT', 'Idiot', 'T', SYSDATE, '"systemuser or whoever creates the value"))


-- USAGE FROM JAVA LAYER - includiong queries - Excuse me for the macro type of syntax but I didn't have time to do it properly.
-- It is more an example of how we could use it
-- protected static Lookup readType( String typeId) {
--    try {
--      db.queryForOneRow( "SELECT description,is_active FROM zynap_lookup_type_tb WHERE id=?", typeId);

--      Lookup l = new Lookup(); //Object to return
--      l.typeId = typeId;
--      l.description = db.get( "description");
--      l.active = db.get( "IS_ACTIVE").equals( "T");
--      cache.set( typeId, l); // Maybe we should cache lookups per company???

--      db.queryForManyRows( "SELECT id,short_desc,description,parameter,is_active FROM zynap_lookup_value_tb WHERE type_id=? ORDER BY sort_order,description", typeId);
--      while( db.next()) {
--        LookupValue val = new LookupValue( db.get( "id"), db.get( "description"), db.get( "is_active").equals("T"));
--        l.valueList.add( val);
--        l.valueMap.put( db.get( "id"), val);
--      }
--      return l;
--    } catch( java.sql.SQLException sqle) {
--      System.out.println( "Cannot find lookup type " + typeId + " in database, " + sqle.getMessage());
--   } finally {
--      db.disconnect();
--    }
--    return null;
--  }

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
--   user_id_ IN INTEGER )
--IS
--    check_existance_ zynap_lookup_type_tb.type_id%TYPE;
--	new_type_id_ zynap_lookup_type_tb.type_id%TYPE;
--	seq_ INTEGER;
--    cnt INTEGER;
--
--BEGIN

      ----- VALIDATE ----------------------------------------------------------

   --zynap_field_sp.check_field_size ( type_id_, 1, 50, 'Type Name');
   --zynap_field_sp.check_field_size ( description_, 1, 100, 'Description');
   --zynap_field_sp.validate_key_format( type_id_, 'Type Name');

   --IF NVL( type_, 'X') <> 'SYSTEM' THEN
   --   zynap_field_sp.validate_lookup ( type_, 'LOOKUPTYPETYPE', 'Type');
   --END IF;

--   IF NVL( active_, 'F') NOT IN( 'T', 'F') THEN
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.invalid.field,setOffender=active,setDescription=Active field is not set correctly ');
--   END IF;
--
--
--   SELECT count(*) INTO cnt FROM  zynap_lookup_type_tb WHERE label = label_;
--   IF (cnt > 0) THEN
--   	  --the proposed label already exists for this type id - cannot add this value
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.duplicate.name,setOffender=roleid,setDescription=!A lookup type with given name already exists. Please select a new name');
--   END IF;



   --use a sequence to generate a primary key based on the label
--   SELECT zynap_seq.nextval INTO seq_ FROM DUAL;
--   new_type_id_ := label_ || seq_;



  ----- PERFORM ACTION ----------------------------------------------------

--   INSERT INTO  zynap_lookup_type_tb (
--                type_id,
--				label,
--                company_id,
--                description,
--                type,
--                is_active,
--                is_system,
--                last_updated,
--                last_updated_by )
--  VALUES      ( UPPER(new_type_id_),
--  			  	label_,
--                company_id_,
--                description_,
--                type_,
--                NVL( active_, 'F'),
--                'F',
--                SYSDATE,
--                user_id_);
--
--	  type_id_ := new_type_id_;
--
--      zynap_log_sp.log( zynap_app_sp.GET_ADMIN_MODULE_ID, user_id_, 'Created lookup type ' || type_id_ || ' - ' || description_);
--
--  EXCEPTION
--	WHEN DUP_VAL_ON_INDEX THEN
--	  zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.duplicate.name,setOffender=roleid,setDescription=A lookup type with given name already exists. Please select a new name');
--
--END create_type;

------------------------------------------------------------------------------

--PROCEDURE modify_type (
--   type_id_ IN VARCHAR2,
--   label_ IN VARCHAR2,
--   company_id_ IN INTEGER,
--   description_ IN VARCHAR2,
--   type_ IN VARCHAR2,
--   active_ IN VARCHAR2,
--   user_id_ IN VARCHAR2 )
--IS
--   cnt INTEGER;
--BEGIN
      ----- VALIDATE ----------------------------------------------------------

   --zynap_field_sp.check_field_size ( description_, 1, 100, 'Description');

--   IF NVL( active_, 'F') NOT IN( 'T', 'F') THEN
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.invalid.field,setOffender=active,setDescription=Active field is not set correctly ');
--   END IF;
--
--   SELECT count(*) INTO cnt FROM  zynap_lookup_type_tb WHERE type_id <> UPPER(type_id_) AND label = label_;
--   IF (cnt > 0) THEN
   	  --the proposed label already exists for this type id - cannot add this value
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.duplicate.name,setOffender=roleid,setDescription=!A lookup type with given name already exists. Please select a new name');
--   END IF;


      ----- PERFORM ACTION ----------------------------------------------------

   --If the user is inactivating the type - inactivate all of its associated lookup values
--   IF active_ = 'F' THEN
--   	  UPDATE zynap_lookup_value_tb SET
--	  	is_active = 'F'
--	  WHERE type_id = type_id_
--	  AND company_id = company_id_
--	  AND is_system = 'F';
--   END IF;
--
--   UPDATE zynap_lookup_type_tb SET
--          company_id = company_id_,
--		  label = label_,
--          description = description_,
--          is_active = NVL( active_, 'F'),
--          last_updated = SYSDATE,
--          type = type_ ,
--          last_updated_by = user_id_
--   WHERE  UPPER(type_id) = UPPER(type_id_);
--
--   IF SQL%ROWCOUNT = 0 THEN
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.data.access.problem,setOffender=typeId,setDescription=Unable to update record');
--   END IF;

--   zynap_log_sp.log( zynap_app_sp.GET_ADMIN_MODULE_ID, user_id_, 'Modified lookup type ' || type_id_ || ' - ' || description_);
--END modify_type;

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
--   sort_order_ IN INTEGER
--    )
--IS
--  cnt INTEGER;
--  new_id_ zynap_lookup_type_tb.type_id%TYPE;
--  seq_ INTEGER;
--
--BEGIN
      ----- VALIDATE ----------------------------------------------------------

   --zynap_field_sp.check_field_size ( node_id_, 1, 50, 'Name');
   --zynap_field_sp.check_field_size ( description_, 1, 100, 'Description');
   --zynap_field_sp.check_field_size ( parent_id_, 0, 50, 'Parent Id');
   --zynap_field_sp.check_field_size ( parameter_, 0, 2000, 'Parameter');
   --zynap_field_sp.validate_key_format( node_id_, 'Name');


--   IF NVL( active_, 'F') NOT IN( 'T', 'F') THEN
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.invalid.field,setOffender=active,setDescription=Active field is not set correctly ');
--   END IF;

   --Check that the label does not already exist for the type
   --SELECT count(*) INTO cnt FROM  zynap_lookup_value_tb WHERE type_id = type_id_ AND short_desc = short_desc_;

--   SELECT count(*) INTO cnt FROM  zynap_lookup_value_tb WHERE type_id = UPPER(type_id_) AND company_id = company_id_ AND short_desc = short_desc_;
--   IF (cnt > 0) THEN
   	  --the proposed label already exists for this type id - cannot add this value
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.duplicate.name,setOffender=roleid,setDescription=!A lookup type with given name already exists. Please select a new name');
--   END IF;


   --use a sequence to generate a primary key based on the label
--   SELECT zynap_seq.nextval INTO seq_ FROM DUAL;
--   new_id_ := short_desc_ || seq_;

      ----- PERFORM ACTION ----------------------------------------------------

--   INSERT INTO  zynap_lookup_value_tb (
--                type_id,
--                id,
--                company_id,
--                description,
--                short_desc,
--                is_active,
--				is_system,
--                sort_order,
--                last_updated,
--                last_updated_by )
--   VALUES (     UPPER(type_id_),
--                UPPER( new_id_),
--                company_id_ ,
--                description_,
--                short_desc_,
--                NVL( active_, 'F'),
--				'F',
--                sort_order_,
--                SYSDATE,
--                user_id_);
--
--      node_id_ := new_id_;
--
--	  zynap_log_sp.log( zynap_app_sp.GET_ADMIN_MODULE_ID, user_id_, 'Created lookup node ' || type_id_ || ':' || node_id_ || ' - ' || description_);
--
--
--  EXCEPTION
--	WHEN DUP_VAL_ON_INDEX THEN
--	  zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.duplicate.name,setOffender=roleid,setDescription=A lookup type with given name already exists. Please select a new name');
--	WHEN OTHERS THEN
--	  zynap_error_sp.ERROR( zynap_error_sp.ERR_INTERNAL, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

--END create_node;

------------------------------------------------------------------------------
--PROCEDURE modify_node (
--   id_ IN VARCHAR2,
--   type_id_ IN VARCHAR2,
--   company_id_ IN INTEGER,
--   short_desc_  IN VARCHAR2,
--   description_ IN VARCHAR2,
--   active_ IN VARCHAR2,
--   sort_order_ IN INTEGER,
--   user_id_ IN INTEGER
-- )
--IS
--  cnt INTEGER;
--  seq_ INTEGER;
--
--BEGIN
      ----- VALIDATE ----------------------------------------------------------


   --zynap_field_sp.check_field_size ( description_, 1, 100, 'Description');
   --zynap_field_sp.check_field_size ( parent_id_, 0, 50, 'Parent Id');
   --zynap_field_sp.check_field_size ( parameter_, 0, 2000, 'Parameter');

      ----- PERFORM ACTION ----------------------------------------------------
--   IF NVL( active_, 'F') NOT IN( 'T', 'F') THEN
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.invalid.field,setOffender=active,setDescription=Active field is not set correctly ');
--   END IF;

   --Check that the label does not already exist for the type that is not the value being edited
--   SELECT count(*) INTO cnt FROM  zynap_lookup_value_tb WHERE type_id = UPPER(type_id_) AND company_id = company_id_ AND id <> UPPER(id_) AND short_desc = short_desc_;
--   IF (cnt > 0) THEN
   	  --the proposed label already exists for this type id - cannot add this value
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.duplicate.name,setOffender=roleid,setDescription=A lookup type with given name already exists. Please select a new name');
--   END IF;


--   UPDATE zynap_lookup_value_tb SET
--          short_desc = short_desc_,
--          description = description_,
--          is_active = NVL( active_, 'F'),
--          sort_order = sort_order_,
--          last_updated = SYSDATE,
--          last_updated_by = user_id_
--   WHERE UPPER(type_id) = UPPER(type_id_)
--   AND company_id = company_id_
--   AND id = UPPER(id_);
--
--   IF SQL%ROWCOUNT = 0 THEN
--      zynap_error_sp.ERROR( zynap_error_sp.ERR_FORMVAL, 'setReason=generic.data.access.problem,setOffender=typeId,setDescription=Unable to update record');
--   END IF;
--
--   zynap_log_sp.log( zynap_app_sp.GET_ADMIN_MODULE_ID, user_id_, 'Modified lookup node ' || type_id_ || ':' || id_ || ' - ' || description_);
--
--END modify_node;

------------------------------------------------------------------------------
--------------- RETREIVAL -----------------------------------------------------
------------------------------------------------------------------------------

FUNCTION get_description (
   type_id_ IN VARCHAR2,
   id_ IN VARCHAR2 ) RETURN VARCHAR2
IS
   CURSOR cur IS
      SELECT description FROM lookup_values
      WHERE UPPER(type_id) = UPPER(type_id_) AND value_id = UPPER(id_);
   description_ VARCHAR2(200);
BEGIN
   OPEN cur;
   FETCH cur INTO description_;
   CLOSE cur;
   RETURN description_;
END get_description;

------------------------------------------------------------------------------

FUNCTION get_short_desc (
   type_id_ IN VARCHAR2,
   id_ IN VARCHAR2 ) RETURN VARCHAR2
IS
   CURSOR cur IS
      SELECT NVL( short_desc, description)
      FROM lookup_values
      WHERE UPPER(type_id) = UPPER(type_id_) AND value_id = UPPER(id_);
   description_ VARCHAR2(200);
BEGIN
   OPEN cur;
   FETCH cur INTO description_;
   CLOSE cur;
   RETURN description_;
END get_short_desc;

------------------------------------------------------------------------------

/*
FUNCTION get_struct_desc (
   type_id_ IN VARCHAR2,
   id_ IN VARCHAR2 ) RETURN VARCHAR2
IS
   CURSOR cur IS
      SELECT short_desc
      FROM zynap_lookup_value_tb
      WHERE type_id = type_id
      START WITH type_id = type_id_ AND id = id_
      CONNECT BY type_id = PRIOR type_id AND id = PRIOR parent_id
      ORDER BY LEVEL;
   description_ VARCHAR2(1000);
BEGIN
   FOR rec_ IN cur LOOP
      description_ := description_ || '/' || rec_.short_desc;
   END LOOP;
   RETURN SUBSTR( description_, 2);
END get_struct_desc;
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
)
IS
	x_ VARCHAR2(1) := 'F';
BEGIN
   IF is_system_ THEN
   	  x_ := 'T';
   END IF;

   INSERT INTO lookup_types (
   	 id,
   	 description,
   	 type,
   	 is_active,
	 is_system,
	 label)
   VALUES (
     id_,
     description_,
     type_,
	 'T',
	 x_,
	 label_ );
END install_type;



PROCEDURE install_values (
	type_id_ IN VARCHAR,
	id_ IN VARCHAR2,
	short_desc_ IN VARCHAR2 DEFAULT NULL,
	description_ IN VARCHAR2,
	sort_order_ INTEGER,
	is_system_ BOOLEAN DEFAULT FALSE
	)
IS
	x_ VARCHAR2(1) := 'F';
BEGIN
   IF is_system_ THEN x_ := 'T'; END IF;
   INSERT INTO lookup_values (
          id,
   		  value_id,
		  short_desc,
		  description,
          is_active,
		  is_system,
		  sort_order,
		  type_id
)
   VALUES(
           lookupvalue_sq.nextval,
   		   id_,
		   short_desc_,
		   description_,
           'T',
		   x_,
		   sort_order_,
		   type_id_
);
END install_values;

PROCEDURE install_vals (
	type_id_ IN VARCHAR,
	id_ IN VARCHAR2,
	short_desc_ IN VARCHAR2 DEFAULT NULL,
	sort_order_ INTEGER,
	is_system_ BOOLEAN DEFAULT FALSE
	)
IS

BEGIN
    install_values(type_id_, id_, short_desc_, short_desc_, sort_order_, is_system_);
END install_vals;

END zynap_lookup_sp;
/
