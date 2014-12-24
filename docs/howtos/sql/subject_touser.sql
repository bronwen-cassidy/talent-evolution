select * from logins where username='feyza.aysan';
--user_id=561
select * from core_details where second_name = 'Aysan';
-- cd_id=2344
select * from subjects where cd_id=2463;
select * from users where id=561;
-- node_id=9097
update subjects set user_id=561 where node_id=9097;
update subjects set cd_id=2344 where node_id=9097;
delete from core_details where id=2463;
