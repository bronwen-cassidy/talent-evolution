CREATE OR REPLACE PACKAGE zynap_hierarchy_sp AS

---------------------------------------------------------------------------------------------
 -- Description - Adds a position to the system.
 ---------------------------------------------------------------------------------------------
 PROCEDURE insert_position_hi (
    id_ IN INTEGER,
    parent_id_ IN INTEGER
 );

 PROCEDURE update_position_hi (
    id_ IN INTEGER,
    parent_id_ IN INTEGER
 );

-------------------------------------------------------------------------------------
-- This uses the procedures in the subject package
-------------------------------------------------------------------------------------
 PROCEDURE delete_position_hi (
    id_ IN INTEGER
 );
 

	
 
 ---------------------------------------------------------------------------------------------
 -- Description - Adds a position to the system.
 ---------------------------------------------------------------------------------------------
 PROCEDURE insert_OU_hi (
    id_ IN INTEGER,
    parent_id_ IN INTEGER
 );

 PROCEDURE update_OU_hi (
    id_ IN INTEGER,
    parent_id_ IN INTEGER
 );
	
-------------------------------------------------------------------------------------
-- This uses the procedures in the subject package
-------------------------------------------------------------------------------------
 PROCEDURE delete_OU_hi (
    id_ IN INTEGER
 );
                             
 
 FUNCTION isSubPrimaryAssociation
 (                               
 	qualifierId_ IN INTEGER
 ) RETURN boolean;
                  
 
 
               
 
 
 

END zynap_hierarchy_sp;
/
