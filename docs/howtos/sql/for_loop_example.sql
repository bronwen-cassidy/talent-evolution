DECLARE
	BEGIN
		FOR i IN 1..175 LOOP
			insert into nodes (ID, NODE_TYPE, IS_ACTIVE) VALUES (NODE_SQ.NEXTVAL, 'Q', 'T');
		END LOOP;
	END;
/

DECLARE
    cursor q_cur is SELECT * FROM NODES WHERE NODE_TYPE='Q' AND ID NOT IN (3090, 3091, 3092, 3093, 3094);

    l_id number;
    l_row nodes%rowtype;

    BEGIN
        OPEN q_cur;
            LOOP
                FETCH q_cur into l_row;
                    EXIT WHEN q_cur%notfound;
                    INSERT INTO QUESTIONNAIRES (node_id, QWF_ID, user_id, is_completed, subject_id)
                    VALUES (l_row.id, 424, 1, 'F', 2901);
            END LOOP;
        CLOSE q_cur;
    END;
/

select * from nodes where node_type='Q' order by id;
select * from QUESTIONNAIRES where subject_id=2901 order by node_id;


