DECLARE
	cursor type_cur is select p.node_id
		from positions p , organization_units ou
where p.org_unit_id = ou.node_id
and ou.label in
('Basingstoke & North Hampshire FT', 'Berkshire East PCT', 'Berkshire Healthcare Trust', 'Berkshire West PCT',
'Buckinghamshire Hospitals', 'Hampshire PCT', 'Buckinghamshire PCT', 'Hampshire Partnership',
'Heatherwood & Wrexham Park Hospitals FT', 'Isle of Wight PCT', 'Milton Keynes FT', 'Milton Keynes PCT', 'Nuffield Orthopaedic Centre',
'Ox. & Bucks. Mental Health', 'Oxford Radcliffe Hospitals', 'Oxfordshire Learning Disability Trust',
'Oxfordshire PCT', 'Portsmouth City Teaching PCT', 'Portsmouth Hospitals', 'Royal Berkshire FT',
'SC Ambulance Service', 'Southampton City PCT', 'Southampton University Hospitals NHS Trust', 'Winchester and Eastleigh Healthcare NHS Trust');

	lookup_type_rec positions.node_id%type;
	num_occurances_ number;

BEGIN
	OPEN type_cur;
        LOOP
            FETCH type_cur INTO lookup_type_rec;
            EXIT WHEN type_cur%notfound;
            -- area da_id= 271685
            -- north value_id=5872
            select count(nd.value) into num_occurances_ from node_das nd where da_id=271685 and node_id=lookup_type_rec;
            if num_occurances_ <= 0 then
            	-- insert node_das for area
            	insert into node_das (id, da_id, node_id, value) values	(da_sq.nextval, 271685, lookup_type_rec, '5874');
            end if;

            -- locality da_id=271687
            -- north_west value=5877
            select count(nd.value) into num_occurances_ from node_das nd where da_id=271687 and node_id=lookup_type_rec;
            if num_occurances_ <= 0 then
            	-- insert node_das for locality
            	insert into node_das (id, da_id, node_id, value) values	(da_sq.nextval, 271687, lookup_type_rec, '5883');
            end if;

        END LOOP;
    CLOSE type_cur;
END;
