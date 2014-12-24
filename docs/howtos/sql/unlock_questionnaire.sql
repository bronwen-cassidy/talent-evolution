select s.node_id, cd.* from core_details cd, subjects s
where cd.first_name='Michaela' and cd.second_name='Sharp'
and s.cd_id = cd.id;    -- 50855

select * from que_workflows where label like 'ZFC 3%';

select * from questionnaires where qwf_id=1996 and subject_id=50855;

update questionnaires set locked ='F' where node_id= 65506;
