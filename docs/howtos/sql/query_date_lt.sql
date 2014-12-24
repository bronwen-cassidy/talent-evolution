select count(*) from session_logs where logged_out_dts > add_months(sysdate,-12);
delete from session_logs where logged_in_dts < add_months(sysdate,-6);

-- selects a days worth of logins 4 days ago
select * from session_logs where logged_in_dts > (sysdate -5) and logged_in_dts < (sysdate - 4);
