CREATE OR REPLACE
PACKAGE zynap_auth_sp IS

------------------------------------------------------------------------------
--------------- ENCRYPTION FUNCTIONS -----------------------------------------
------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------
-- Description - Encrypts the given string using DES Encryption standard.
-- Exceptions - None.   Not too many
---------------------------------------------------------------------------------------------
FUNCTION encrypt (
	p_text  IN  VARCHAR2) RETURN RAW;

---------------------------------------------------------------------------------------------
-- Description - Decrypts the given encrypted string using DES Encryption standard.
-- Exceptions - None.
---------------------------------------------------------------------------------------------
FUNCTION decrypt (
	p_raw  IN  RAW) RETURN VARCHAR2;

---------------------------------------------------------------------------------------------
PROCEDURE load_user_roles_app_(
role_label_ in varchar,
user_id_ in integer
);

----------------------------------------
-- assigns a users permits
---------------------------------------
procedure assignPermits (
  user_id_ in integer  
);

-----------------------------------------
--  inserts the users permits
-----------------------------------------
procedure insertPermits (
  user_id_ in integer
);

END zynap_auth_sp;
/
