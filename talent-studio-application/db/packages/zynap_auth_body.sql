CREATE OR REPLACE
PACKAGE BODY zynap_auth_sp IS

------------------------------------------------------------------------------
-- * Add password history
-- * Expire date functionality for password  functions getDaysbeforeExpires and hasExpired
-- * Sort out Modify_system-user to use change password method instead so we can have password history and expires thing in on place an re-use it
-- * Loads of the stored procs are not used in the file can we clean up what is used or not?
------------------------------------------------------------------------------

------------------------------------------------------------------------------
--------------- PRIVATE PROC/FUNCTIONS/VARIABLES DECLARATIONS ----------------
------------------------------------------------------------------------------

  -- Private variables required for encryption methods - need to see if these can be inclucded below.
g_key     RAW(32767)  := UTL_RAW.cast_to_raw('12345678');
g_pad_chr VARCHAR2(1) := '~';

  -- Declarations
PROCEDURE padstring (p_text  IN OUT  VARCHAR2);

------------------------------------------------------------------------------
--------------- ENCRYPTION FUNCTIONS/PROCEDURES ------------------------------
------------------------------------------------------------------------------

-- --------------------------------------------------
FUNCTION encrypt (p_text  IN  VARCHAR2) RETURN RAW IS
-- --------------------------------------------------
    l_text       VARCHAR2(32767) := p_text;
    l_encrypted  RAW(32767);
  BEGIN
    padstring(l_text);
    DBMS_OBFUSCATION_TOOLKIT.desencrypt(input          => UTL_RAW.cast_to_raw(l_text),
                                        key            => g_key,
                                        encrypted_data => l_encrypted);
    RETURN l_encrypted;
  END;
-- --------------------------------------------------


-- --------------------------------------------------
FUNCTION decrypt (p_raw  IN  RAW) RETURN VARCHAR2 IS
-- --------------------------------------------------
    l_decrypted  VARCHAR2(32767);
  BEGIN
    DBMS_OBFUSCATION_TOOLKIT.desdecrypt(input => p_raw,
                                        key   => g_key,
                                        decrypted_data => l_decrypted);

    RETURN RTrim(UTL_RAW.cast_to_varchar2(l_decrypted), g_pad_chr);
  END;
-- --------------------------------------------------

------------------------------------------------------------------------------
--------------- PRIVATE PROC/FUNCTIONS IMPLEMENTATIONS -----------------------
------------------------------------------------------------------------------

-- --------------------------------------------------
PROCEDURE padstring (p_text  IN OUT  VARCHAR2) IS
-- --------------------------------------------------
    l_units  NUMBER;
  BEGIN
    IF LENGTH(p_text) MOD 8 > 0 THEN
      l_units := TRUNC(LENGTH(p_text)/8) + 1;
      p_text  := RPAD(p_text, l_units * 8, g_pad_chr);
    END IF;
  END padstring;
-- --------------------------------------------------

PROCEDURE load_user_roles_app_(
role_label_ in varchar,
user_id_ in integer
)
IS
id_ integer;
 BEGIN

   select id into id_ from roles where label = role_label_;
 -- insert the default users in the admin role
	INSERT INTO app_roles_users ( user_id, role_id)
	VALUES ( user_id_, id_ );

 END  load_user_roles_app_;

-- --------------------------------------------------------
-- loads the users permits after login
-- --------------------------------------------------------
procedure assignPermits (
  user_id_ in integer
)

is

BEGIN
      delete from USER_NODE_DOMAIN_PERMITS WHERE USER_ID = user_id_;
      insertPermits(user_id_);

END assignPermits;

procedure insertPermits (
  user_id_ in integer
)

is

BEGIN
    insert /*+ APPEND */ into USER_NODE_DOMAIN_PERMITS (USER_ID,NODE_ID,PERMIT_ID)
    select USER_ID,NODE_ID,PERMIT_ID from ZYNAP_USER_DOMAIN_PERMITS WHERE USER_ID = user_id_
    and node_id not in (
        select s.node_id from subjects s
        where s.user_id=user_id_
        union
        select p.node_id from positions p, subjects s, subject_primary_associations spa
        where s.user_id = user_id_
        and spa.subject_id = s.node_id
        and spa.position_id=p.node_id
    );

END insertPermits;

END zynap_auth_sp;
/