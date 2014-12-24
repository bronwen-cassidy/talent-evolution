DECLARE
	cursor positions_cur is SELECT p.title, p.org_unit_id, q.node_id FROM positions p, subject_primary_associations spa, questionnaires q
							where spa.subject_id=q.subject_id
							and spa.position_id=p.node_id
							and q.qwf_id=61;
	positions_title_rec positions.title%type;	
	org_unit_rec positions.org_unit_id%type; 
	que_id_rec questionnaires.node_id%type;

BEGIN
	OPEN positions_cur;
        LOOP
            FETCH positions_cur INTO positions_title_rec, org_unit_rec, que_id_rec;
            EXIT WHEN positions_cur%notfound;
                -- the position
            	insert into node_das (id, node_id, da_id, value) values (da_sq.nextval, que_id_rec, -1, positions_title_rec);
            	-- the org unit
            	insert into node_das (id, node_id, da_id, value) values (da_sq.nextval, que_id_rec, -2, org_unit_rec);
            	
        END LOOP;
    CLOSE positions_cur;
END;
