prompt Enter the name of the dump directory
prompt
accept dumpdir char prompt 'Dump directory: '

SET HEADING OFF
SET FEEDBACK OFF
SET TRIMSPOOL ON
SET LINESIZE 500
SET PAGES 0
SET RECSEP OFF
SET VERIFY OFF
SET SHOWMODE OFF
SET TERMOUT OFF

spool ./&dumpdir/wf_insert_data.sql

prompt set define off
prompt set showmode off
prompt set termout off
prompt spool ./wf_insert.log
select 'insert into wf_item_attribute_values(item_type,item_key,name,text_value,date_value) values("' || item_type || '","' || item_key || '","' || name  || '","' || text_value || '","' || date_value || '");'
from wf_item_attribute_values;
prompt commit;;
prompt quit;;

spool off
quit
/
