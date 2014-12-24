select count(lv.short_desc)
from lookup_values lv, lookup_types lt, dynamic_attributes da, node_das nd, questionnaires q
where da.id in (81792, 81794, 6925, 6927, -4)
and nd.da_id = da.id
and nd.node_id=q.node_id
and q.qwf_id in (select id from que_workflows where qd_id in (101, 1, 185))
and nd.value=lv.id
and lv.short_desc='Improving'
and lv.type_id=lt.id
and da.refers_to=lt.id and q.subject_id in (2549);

