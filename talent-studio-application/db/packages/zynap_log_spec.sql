CREATE OR REPLACE PACKAGE zynap_log_sp IS

------------------------------------------------------------------------------
--------------- LOGGING ETC --------------------------------------------------
------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------
-- Description - Can be called at any time by PL/SQL or JAVA.
-- 			   	 This methods decides wether to log the information or simply return by looking at a global parameter.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
PROCEDURE log (
   application_ IN VARCHAR2,
   userid_ IN INTEGER,
   text_ IN VARCHAR2 );


END zynap_log_sp;
/