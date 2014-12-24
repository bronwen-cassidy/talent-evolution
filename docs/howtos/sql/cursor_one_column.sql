DECLARE
    cursor q_cur is select distinct nd.node_id from node_das nd, dynamic_attributes da, subjects s
		where da.qd_id=142
		and nd.da_id=da.id
		and nd.node_id not in (2993, 3001, 3048)
		and s.node_id=nd.node_id;

    l_id number;
    --l_row node_das%rowtype;

    BEGIN
        OPEN q_cur;
            LOOP
                FETCH q_cur into l_id;
                    EXIT WHEN q_cur%notfound;
                    insert into nodes (ID, NODE_TYPE, IS_ACTIVE) VALUES (NODE_SQ.NEXTVAL, 'Q', 'T');

                    INSERT INTO QUESTIONNAIRES (node_id, QWF_ID, user_id, is_completed, subject_id)
                    VALUES (node_sq.currval, 424, 1, 'F', l_id);

                    update node_das set node_id=node_sq.currval where node_id=l_id;

            END LOOP;
        CLOSE q_cur;
    END;
/



select nd.* from node_das nd, dynamic_attributes da, subjects s
		where da.qd_id=142
		and nd.da_id=da.id
		and nd.node_id not in (2993, 3001, 3048)
		and s.node_id=nd.node_id;


select * from questionnaires order by subject_id;

--select * from nodes where node_type='Q';
select nd.* from node_das nd, dynamic_attributes da, QUESTIONNAIRES s
		where da.qd_id=142
		and nd.da_id=da.id
		and nd.node_id not in (2993, 3001, 3048)
		and s.node_id=nd.node_id;
--select * from QUE_WORKFLOWS	where qd_id=142;
