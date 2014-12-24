CREATE OR REPLACE
PACKAGE BODY zynap_log_sp IS

------------------------------------------------------------------------------
--------------- LOGGING ETC --------------------------------------------------
------------------------------------------------------------------------------
  -- This loggin proce only logs to the database if the global flag
  -- for logging for current application is ON.
PROCEDURE log(
   application_ IN VARCHAR2,
   userid_ IN INTEGER,
   text_ IN VARCHAR2 )
IS
--   CURSOR cur IS
--      SELECT 1 FROM sys WHERE id = 'LOG_' || application_ AND value = 'ON';
--   dummy_ INTEGER;
BEGIN
--   OPEN cur; FETCH cur INTO dummy_; CLOSE cur;
--   IF dummy_ = 1 THEN
      INSERT INTO errors (
         timestamp,
         module_id,
         user_id,
         error_message )
      VALUES (
         SYSDATE,
         application_,
         userid_,
         text_ );
--   END IF;
   EXCEPTION WHEN OTHERS THEN NULL;
END log;

END zynap_log_sp;
/
