@echo off

echo first parameter provided is: %1
rem IF (%1)==() goto end
rem IF (%2) == () goto end

sqlplus SYSTEM/Ta13ntman@xe @promptAndCreateUser.sql

rem imp %1/%1 LOG=%2/ts_restore.log FULL=y COMMIT=y ROWS=y INDEXES=y CONSTRAINTS=y GRANTS=y FILE=%2/ts.dmp

impdp system/Ta13ntman@xe directory=DATA_PUMP_DIR dumpfile=ts.dmp logfile=tsRestore.log schemas=TME remap_schema=TME:tsdev



:end
echo required parameters of [db username], [restore_dir] not specified