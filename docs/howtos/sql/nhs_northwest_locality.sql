DECLARE
	cursor type_cur is select p.node_id
		from positions p, organization_units ou
		where p.org_unit_id=ou.node_id
		and ou.label in (
		'5 Boroughs Partnership Trust', 'Aintree University Hospitals NHS FT', 'Alder Hey Childrens NHS FT',
'Blackpool, Fylde & Wyre Hospitals NHS FT', 'Bolton, Salford & Trafford Mental Health Trust',
'Calderstones NHS Trust', 'Central & Eastern Cheshire PCT', 'Central Manchester University Hospitals NHS FT',
'Cheshire & Wirral Partnership NHS Trust', 'Countess of Chester Hospital NHS FT', 'Cumbria PCT',
'Cumbria Partnership NHS FT', 'East Cheshire NHS Trust', 'East Lancashire Hospitals NHS FT',
'Greater Manchester West Mental Health NHS Foundation Trust', 'Lancashire Care NHS Foundation Trust',
'Lancashire Teaching Hospitals NHS FT', 'Liverpool Heart and Chest Hospital NHS Trust', 'Liverpool PCT',
'Liverpool Womens NHS Foundation Trust', 'Manchester Mental Health & Social Care NHS Trust', 'Mersey Care NHS Trust',
'Mid Cheshire Hospital NHS FT', 'NHS Ashton, Leigh and Wigan', 'NHS Blackburn with Darwen', 'NHS Blackpool',
'NHS Bolton', 'NHS Bury', 'NHS Central Lancashire', 'NHS Cumbria', 'NHS East Lancashire', 'NHS Halton & St Helens',
'NHS Heywood, Middleton & Rochdale', 'NHS Knowsley', 'NHS Manchester', 'NHS North Lancashire', 'NHS North West', 'NHS North West Leadership Academy',
'NHS Oldham', 'NHS Salford', 'NHS Sefton', 'NHS Tameside & Glossop', 'NHS Trafford', 'NHS Warrington', 'NHS Western Cheshire',
'NHS Wirral', 'NW Ambulance Service NHS Trust', 'North Cumbria Acute Hospitals NHS Trust', 'Pennine Care NHS FT',
'Royal Bolton Hospitals NHS FT', 'Royal Liverpool & Broadgreen University Hospitals NHS Trust', 'Salford Royal NHS Foundation Trust',
'Southport & Ormskirk Hospital NHS Trust', 'St Helens & Knowsley Hospitals NHS Trust', 'Stockport NHS Foundation Trust', 'Stockport PCT',
'Tameside Hospital NHS FT', 'The Christie', 'The Pennine Acute Hospitals NHS Trust', 'The Walton Centre for Neurology & Neurosurgery NHS Trust',
'Trafford Healthcare NHS Trust', 'University Hospital of South Manchester NHS FT', 'University Hospitals of Morecambe Bay NHS Trust',
'Warrington & Halton Hospitals NHS FT', 'Wirral University Teaching Hospital NHS FT', 'Wrightington, Wigan & Leigh NHS FT'
		);

	lookup_type_rec positions.node_id%type;
	num_occurances_ number;

BEGIN
	OPEN type_cur;
        LOOP
            FETCH type_cur INTO lookup_type_rec;
            EXIT WHEN type_cur%notfound;
            -- area da_id= 271685
            -- north value_id=5872
            -- locality da_id=271687
            -- north_west value=5877
            select count(nd.value) into num_occurances_ from node_das nd where da_id=271685 and node_id=lookup_type_rec;
            if num_occurances_ <= 0 then
            	-- insert node_das
            	insert into node_das (id, da_id, node_id, value) values	(da_sq.nextval, 271685, lookup_type_rec, '5872');
            end if;

            select count(nd.value) into num_occurances_ from node_das nd where da_id=271687 and node_id=lookup_type_rec;
            if num_occurances_ <= 0 then
            	-- insert node_das
            	insert into node_das (id, da_id, node_id, value) values	(da_sq.nextval, 271687, lookup_type_rec, '5877');
            end if;

        END LOOP;
    CLOSE type_cur;
END;
