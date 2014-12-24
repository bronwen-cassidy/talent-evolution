DECLARE
	cursor type_cur is select * from node_das where value in ('8140', '8141');
	lookup_type_rec node_das%rowtype;
BEGIN
	OPEN type_cur;
        LOOP
            FETCH type_cur INTO lookup_type_rec;
            EXIT WHEN type_cur%notfound;

            	if lookup_type_rec.value = '8140' then
            		--dbms_output.put_line( 'in the if: ' || lookup_type_rec.value);
            		insert into node_das (id, node_id, da_id, value)
            		values (da_sq.nextval, lookup_type_rec.node_id, 964211, '18923');

            	else
            	   --dbms_output.put_line( ' in the else: ' || lookup_type_rec.value );
					insert into node_das (id, node_id, da_id, value)
					values (da_sq.nextval, lookup_type_rec.node_id, 964211, '18924');
            	end if;


        END LOOP;
    CLOSE type_cur;
END;
