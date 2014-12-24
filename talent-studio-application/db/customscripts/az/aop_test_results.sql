select * from dynamic_attributes where id=163331;
select * from lookup_values where type_id='LEADERSHIPASSESSMENTLASTYEAR0.9612431109879778163259';

select * from dynamic_attributes where id=81808;
select * from lookup_values where type_id='LEADERSHIPRATINGTHISYEAR0.252069941330543581743';

select * from que_workflows;
select * from dynamic_attributes where type='ENUMMAPPING';

select * from node_das where da_id in (select id from dynamic_attributes where qd_id=185);

select * from lookup_values where type_id in (select refers_to from dynamic_attributes where qd_id=185);
select * from lookup_values where type_id in (select refers_to from dynamic_attributes where qd_id=101);

select refers_to from dynamic_attributes where type='TEXT';

select nd.* from node_das nd, dynamic_attributes da, questionnaires q
                    where nd.node_id =q.node_id
                    and q.qwf_id=61
                    and nd.da_id=da.id
                    and da.id=81784;

select lv.id from lookup_values lv, dynamic_attributes da
      	      where da.refers_to = lv.type_id
      	      and da.id = 163299
      	      and lv.short_desc=(select short_desc from lookup_values where id=2371);

delete from node_das where da_id in (select id from dynamic_attributes where qd_id = 185);

delete from node_das where node_id in (select node_id from questionnaires where qwf_id in (101, 121, 122));


