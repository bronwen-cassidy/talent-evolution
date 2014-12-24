-- proc to insert an answer for all aops that already have one for 2010
DECLARE
	cursor questoinnaire_cur is SELECT q.node_id FROM questionnaires q
							where q.qwf_id=101;

	que_id_rec questionnaires.node_id%type;

BEGIN
	OPEN questoinnaire_cur;
        LOOP
            FETCH questoinnaire_cur INTO que_id_rec;
            EXIT WHEN questoinnaire_cur%notfound;
                -- the position
            	-- the org unit
            	insert into node_das (id, node_id, da_id, value) values (da_sq.nextval, que_id_rec, 163293, '4557');

        END LOOP;
    CLOSE questoinnaire_cur;
END;
