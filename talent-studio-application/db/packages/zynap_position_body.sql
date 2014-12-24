CREATE OR REPLACE
PACKAGE BODY zynap_position_sp AS



-------------------------------------------------------------------------------------
-- procedure that deletes the positions, position_associations, subject_associations, positions_hierarchy,
-- positions_hierarchy_inc, relevant rows in area alements, node_audits, node_das for all descendants inclusive
-- of current position
-------------------------------------------------------------------------------------

PROCEDURE delete_position_cascade (
   position_id_ IN INTEGER
   ,user_id_ in integer
   ,username_ in varchar2
)
IS
    cursor node_cur is SELECT * FROM positions_hierarchy_inc WHERE root_id = position_id_ order by hlevel desc;
    positions_rec positions_hierarchy_inc%rowtype;

    title_ varchar2(2000);

BEGIN

    OPEN node_cur;
		LOOP
			FETCH node_cur INTO positions_rec;
			EXIT WHEN node_cur%notfound;

            select title into title_ from positions where node_id=positions_rec.id;

            delete from external_ref_mappings where internal_ref_id=positions_rec.id;
            delete from area_elements where node_id=positions_rec.id;
            delete from node_da_files where node_da_id in (select id from node_das where node_id=positions_rec.id);
            delete from node_das where node_id=positions_rec.id;
            delete from node_items where node_id=positions_rec.id;
            delete from node_audits where node_id=positions_rec.id;
            delete from position_associations where target_id=positions_rec.id;
            delete from position_associations where source_id=positions_rec.id;
            delete from subject_associations where position_id=positions_rec.id;
            delete from positions_hierarchy where id=positions_rec.id;
            delete from positions_hierarchy_inc where id=positions_rec.id;
            delete from positions where node_id=positions_rec.id;
            delete from nodes where id=positions_rec.id;

            insert into audits (id, modified_by_id, MODIFIED_BY_USERNAME, OBJECT_ID, MODIFIED_DATE, TABLE_NAME, ACTION_PERFORMED, DESCRIPTION)
            values (audits_sq.nextval, user_id_, username_, positions_rec.id, sysdate, 'POSITIONS', 'DELETED', 'com.zynap.talentstudio.organisation.positions.Position@[id=' || positions_rec.id || ', title=' || title_ || ']');

		END LOOP;
	CLOSE node_cur;


END delete_position_cascade;

-------------------------------------------------------------------------------------
-- procedure that deletes the positions, position_associations, subject_associations, positions_hierarchy,
-- positions_hierarchy_inc, relevant rows in area alements, node_audits, node_das for all descendants inclusive
-- of current position
-------------------------------------------------------------------------------------

PROCEDURE delete_ou_positions_cascade (
   organisation_id_ IN INTEGER
   ,user_id_ in integer
   ,username_ in varchar2
)
IS
    cursor node_cur is SELECT * FROM positions WHERE org_unit_id = organisation_id_;
    positions_rec positions%rowtype;

BEGIN

    OPEN node_cur;
		LOOP
			FETCH node_cur INTO positions_rec;
			EXIT WHEN node_cur%notfound;

            if positions_rec.node_id <> 1 then
                delete_position_cascade(positions_rec.node_id, user_id_, username_);
            end if;

		END LOOP;
	CLOSE node_cur;

END delete_ou_positions_cascade;

END zynap_position_sp;
/
