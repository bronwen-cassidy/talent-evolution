@echo off

echo dbuser= %1 dump dir = %2
IF (%1)==() goto end
IF (%2)==() goto end

rem : TODO CREATE THE DIR IF NOT EXISTS

exp %1/%1 LOG=%2/ts.log CONSISTENT=y CONSTRAINTS=y GRANTS=y INDEXES=y ROWS=y STATISTICS=NONE FILE=%2/ts.dmp

goto success

:end echo restore failed required parameters DBUser and Dump Dir must be provided
:success echo dump suceeded