select * from que_workflows;

-- old then new
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('%his yea%');  --163296, managers view of leadership rating=163322
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('%ast yea%');   -- 388748, managers view of leadership rating=388767

select * from dynamic_attributes where qd_id=185 and upper(label) like upper('%ast yea%');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('%years a%');

-- leadership
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Target Leadership Capability Level');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Target Leadership Capability Level');

select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Rating');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Rating');
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Supporting Examples');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Supporting Examples');

-- derailers
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Abrasive, arrogant or insensitive to personal impact on others');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Abrasive, arrogant or insensitive to personal impact on others');
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Difficulty communicating clearly and persuasively');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Difficulty communicating clearly and persuasively');
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Perfectionist or micromanager');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Perfectionist or micromanager');
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Takes credit for the work of others');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Takes credit for the work of others');
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Difficulty about making sound judgements about people');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Difficulty about making sound judgements about people');
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Hesitancy to take necessary risks');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Hesitancy to take necessary risks');

-- mobility
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Interest');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Interest');
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Mobility Region');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Mobility Region');
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Comment');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Comment');

-- aspirations
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Goal');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Goal');
select * from dynamic_attributes where qd_id=185 and upper(label) like upper('Timescale');
select * from dynamic_attributes where qd_id=201 and upper(label) like upper('Timescale');


-- performance da then leadership da this year
-- param1 = new workflow id, param2 = old workflow id, param3 = old da_id, param4=new da_id
exec az_aop_sp.migrate_aop(101, 61, 81782, 163298);
exec az_aop_sp.migrate_aop(101, 61, 81808, 163331);

-- preformance da then leadership da last year
exec az_aop_sp.migrate_aop(101, 61, 81784, 163299);
exec az_aop_sp.migrate_aop(101, 61, 81817, 163332);

-- aop leadership
exec az_aop_sp.migrate_aop(101, 61, 81796, 163310);
exec az_aop_sp.migrate_aop(101, 61, 81797, 163311);
exec az_aop_sp.migrate_aop(101, 61, 81799, 163313);
exec az_aop_sp.migrate_aop(101, 61, 81800, 163314);
exec az_aop_sp.migrate_aop(101, 61, 81801, 163315);
exec az_aop_sp.migrate_aop(101, 61, 81802, 163316);
exec az_aop_sp.migrate_aop(101, 61, 81803, 163317);
exec az_aop_sp.migrate_aop(101, 61, 81804, 163318);
exec az_aop_sp.migrate_aop(101, 61, 81805, 163319);
exec az_aop_sp.migrate_aop(101, 61, 81806, 163320);
exec az_aop_sp.migrate_aop(101, 61, 81807, 163321);

exec az_aop_sp.migrate_aop(101, 61, 81809, 163323);
exec az_aop_sp.migrate_aop(101, 61, 81810, 163324);
exec az_aop_sp.migrate_aop(101, 61, 81811, 163325);
exec az_aop_sp.migrate_aop(101, 61, 81812, 163326);
exec az_aop_sp.migrate_aop(101, 61, 81813, 163327);
exec az_aop_sp.migrate_aop(101, 61, 81814, 163328);

-- aop derailers!!
exec az_aop_sp.migrate_aop(101, 61, 81835, 163347);
exec az_aop_sp.migrate_aop(101, 61, 81836, 163348);
exec az_aop_sp.migrate_aop(101, 61, 81837, 163349);
exec az_aop_sp.migrate_aop(101, 61, 81838, 163350);
exec az_aop_sp.migrate_aop(101, 61, 81839, 163351);
exec az_aop_sp.migrate_aop(101, 61, 81840, 163352);

-- mobility
exec az_aop_sp.migrate_aop(101, 61, 81841, 163354);
exec az_aop_sp.migrate_aop(101, 61, 81842, 163355);
exec az_aop_sp.migrate_aop(101, 61, 81843, 163356);

-- aspirations
exec az_aop_sp.migrate_aop(101, 61, 81845, 163358);
exec az_aop_sp.migrate_aop(101, 61, 81846, 163359);

-- IDP old_wf_id=62
exec az_aop_sp.migrate_workflows(121, 62);

-- objectives 2009 -> 2010
exec az_aop_sp.migrate_workflows(122, 64);
