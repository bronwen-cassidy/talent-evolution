select s.node_id from subjects s, core_details cd where s.cd_id=cd.id and cd.first_name='Sam' and cd.second_name='Burrows';
--13407
select id from performance_reviews where label like 'Flexi Placement Manager Evaluation (Hr%'; 
--4821           

select id from notifications where subject_id=13407 and performance_review_id=4821;
-- 5026
update notifications set action='COMPLETE' where id =  5026;