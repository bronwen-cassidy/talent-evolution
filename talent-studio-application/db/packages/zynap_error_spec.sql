CREATE OR REPLACE PACKAGE zynap_error_sp IS

---------------------------------------------------------------------------------------------
-- Description - This method takes a user friendly error message and returns it back up to the JAVA tier.
-- 			   	 Only used by the PL/SQL tier, since the JAVA tier can call throw the exceptions straight away.
-- 				 Before raising the error it calls store_error to insert the error into zynap_error_tb.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
PROCEDURE ERROR (
   type_ IN VARCHAR2,
   text_ IN VARCHAR2 );

ERR_UNAUTH CONSTANT VARCHAR2(20) := 'UNAUTH'; -- The user is unauthorized to perform the action
ERR_FORMVAL CONSTANT VARCHAR2(20) := 'FORMVAL'; -- The user has inputted an invalid value
ERR_INTERNAL CONSTANT VARCHAR2(20) := 'INTERNAL'; -- Something is inconsistent or unexpected in the application
ERR_GENERAL CONSTANT VARCHAR2(20) := 'GENERAL'; -- A general error, not related to anything the user has done wrong
ERR_PROGRAM CONSTANT VARCHAR2(20) := 'PROGRAM'; -- Could be invalid package or constraints problem

---------------------------------------------------------------------------------------------
-- Description - Stores the error into zynap_error_tb.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
PROCEDURE store_error (
   module_id_ IN VARCHAR2,
   user_id_ IN INTEGER,
   error_type_ IN VARCHAR2,
   error_code_ IN VARCHAR2,
   error_message_ IN VARCHAR2 );

END zynap_error_sp;
/
