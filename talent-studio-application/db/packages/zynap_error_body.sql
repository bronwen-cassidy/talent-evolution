CREATE OR REPLACE PACKAGE BODY zynap_error_sp IS

PROCEDURE ERROR (
   type_ IN VARCHAR2,
   text_ IN VARCHAR2 )
IS
BEGIN
   BEGIN
      store_error( NULL, NULL, type_, -20100, text_);
   EXCEPTION WHEN OTHERS THEN NULL;
   END;

   RAISE_APPLICATION_ERROR( -20100, '[[' || type_ || ': ' || text_ || ']]');
END ERROR;

-- This procedure runs its own transaction
PROCEDURE store_error (
   module_id_ IN VARCHAR2,
   user_id_ IN INTEGER,
   error_type_ IN VARCHAR2,
   error_code_ IN VARCHAR2,
   error_message_ IN VARCHAR2 )
IS
PRAGMA AUTONOMOUS_TRANSACTION; -- Use separate transaction
BEGIN
   INSERT INTO errors (
      timestamp,
      module_id,
      user_id,
      error_type,
      error_code,
      error_message )
   VALUES (
      SYSDATE,
      module_id_,
      user_id_,
      error_type_,
      error_code_,
      error_message_ );
   COMMIT;
END store_error;

END zynap_error_sp;
/
