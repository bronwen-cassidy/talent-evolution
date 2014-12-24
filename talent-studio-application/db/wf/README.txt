Scripts in this directory set up synonyms etc required for workflow.

If you want to clear expired notifications you need to open sqlplus (use the workflow schema user name) and do
"
exec wf_engine.background;
commit;
"

if you want to see if there are processes which would be removed by the background engine,
you can run "/home/oracle/product/<version>/wf/admin/sql/wfbkgchk.sql" on the database server using sqlplus (use the workflow schema user name)