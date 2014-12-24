CREATE OR REPLACE
PACKAGE zynap_subject_sp AS


---------------------------------------------------------------------------------------------
-- Description -  Get user type for the given user id
-- Exceptions - None.
---------------------------------------------------------------------------------------------
FUNCTION get_user_type (
   user_id_ IN VARCHAR2) RETURN VARCHAR2;      
   
   
procedure assign_targets ( 
		que_wf_id_ in integer, da_id_ in integer, target_ in varchar2);

---------------------------------------------------------------------------------------------
-- Description - Deletes all subjects only used for admin purposes
---------------------------------------------------------------------------------------------
PROCEDURE delete_all_subjects;


END zynap_subject_sp;
/
