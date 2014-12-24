select u.id from users u, core_details cd where u.cd_id=cd.id and cd.first_name='Gabriel' and cd.second_name='Baertschi';
select * from session_logs where user_id=102;