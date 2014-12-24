select * from performance_reviews; -- 4381;

select * from notifications where performance_review_id=4381 and subject_name = 'Ian Howard';  -- notification_id=4473
-- select * from notifications where performance_review_id=4381 and status <> 'COMPLETED';  -- notification_id=4473
update notifications set action='COMPLETE' where id=4474;
update notifications set status='OPEN' where id=4474;
