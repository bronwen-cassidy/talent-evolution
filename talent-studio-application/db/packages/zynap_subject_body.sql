CREATE OR REPLACE
PACKAGE BODY zynap_subject_sp AS

------------------------------------------------------------
-- update an exisitng subject to position association
------------------------------------------------------------
PROCEDURE assign_targets ( 
		que_wf_id_ in integer, da_id_ in integer, target_ in varchar2
)
IS
	cursor type_cur is select * from que_wf_participants where que_wf_id=que_wf_id_;
	lookup_type_rec que_wf_participants%rowtype;      
	
	node_id_ number;
	
BEGIN

	OPEN type_cur;
        LOOP
            FETCH type_cur INTO lookup_type_rec;
            EXIT WHEN type_cur%notfound;
            select node_sq.nextval into node_id_ from dual;
            
            insert into nodes (id, node_type, is_active) values (node_id_, 'Q', 'T');
            insert into questionnaires (node_id, qwf_id, user_id, subject_id, is_completed, locked) values (node_id_, que_wf_id_, 0, lookup_type_rec.subject_id, 'F', 'F');             	
			
			insert into node_das(id, node_id, da_id, value, added_by, date_added) values (da_sq.nextval, node_id_, da_id_, target_, 0, sysdate);
			

        END LOOP;
    CLOSE type_cur;

END assign_targets;

PROCEDURE delete_all_subjects
IS
    cursor node_cur is SELECT * FROM subjects;
    subjects_rec subjects%rowtype;

BEGIN

    OPEN node_cur;
		LOOP
			FETCH node_cur INTO subjects_rec;
			EXIT WHEN node_cur%notfound;

            delete from subject_associations where subject_id = subjects_rec.node_id;
            delete from subject_primary_associations where subject_id = subjects_rec.node_id;
            delete from area_elements where node_id = subjects_rec.node_id;
            delete from dashboard_participants where subject_id = subjects_rec.node_id;
            delete from node_audits where node_id = subjects_rec.node_id;
            delete from node_das where node_id = subjects_rec.node_id;
            delete from node_items where node_id = subjects_rec.node_id;
            delete from notifications where subject_id = subjects_rec.node_id;
            delete from performance_evaluators where subject_id = subjects_rec.node_id;
            delete from performance_managers where subject_id = subjects_rec.node_id;
            delete from questionnaires where subject_id = subjects_rec.node_id;
            delete from que_wf_participants where subject_id = subjects_rec.node_id;
            delete from report_participants where subject_id = subjects_rec.node_id;
            delete from subject_pictures where subject_id = subjects_rec.node_id;
            delete from user_node_domain_permits where node_id = subjects_rec.node_id;
            delete from subjects where node_id = subjects_rec.node_id;
            
            delete from nodes where id = subjects_rec.node_id;

            if subjects_rec.user_id is null then
                delete from core_details where id = subjects_rec.cd_id;
            end if;

		END LOOP;
	CLOSE node_cur;

END delete_all_subjects;



FUNCTION get_user_type (
   user_id_ IN VARCHAR2) RETURN VARCHAR2
IS
   user_type_ VARCHAR2(50);
BEGIN
   SELECT user_type INTO user_type_ FROM users WHERE id = user_id_;
   RETURN user_type_;
END get_user_type;


END zynap_subject_sp;
/
