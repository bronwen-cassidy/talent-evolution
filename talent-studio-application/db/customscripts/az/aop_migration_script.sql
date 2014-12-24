-- the select queries to find the dynamic_attribute ids
-- note as an example aop 2008, dynamic_attribute 'This Year' must be copied into aop 2009 dynamic_attribute 'Last year'
-- the queries will help to find these questions, but you must make sure you have the correct workflow_id
-- the workflow id for 2008 was 1 the workflow_id for 2009 was 61
-- there is an exception regarding leadership this year, you need the attribute with label 'Confirmed Leadership Rating This Year'

--select * from dynamic_attributes where label like '%This Year%'; -- leadership must be confirmed
--select * from dynamic_attributes where label like '%Last Year%';
--select * from dynamic_attributes where label like '%Two Year%';

-- exec params = new wf_id, old wf_id, old da_id, new da_id
-- performance this year, last year -> last year, 2 years ago
exec az_aop_sp.migrate_aop(328, 121, 163296, 389008);
exec az_aop_sp.migrate_aop(328, 121, 163298, 389009);

-- leadership caperbilities
exec az_aop_sp.migrate_aop(328, 121, 163310, 389016);

-- leadership rating confirmed this year, last year -> last year, 2 years ago
exec az_aop_sp.migrate_aop(328, 121, 163322, 389027);
exec az_aop_sp.migrate_aop(328, 121, 163331, 389028);

-- role
exec az_aop_sp.migrate_aop(328, 121, 163294, 389004);
-- orgunit
exec az_aop_sp.migrate_aop(328, 121, 163295, 389005);

-- IDP 2011 -> 2012 @param1 = new @param2 = old
exec az_aop_sp.migrate_workflows(329, 122);

-- objectives 2011 -> 2012
exec az_aop_sp.migrate_workflows(330, 123);

commit;