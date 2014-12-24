CREATE OR REPLACE PACKAGE BODY zynap_node_sp IS

--==================================================================================
-- deletes a questionnaire definition and all of its dependancies.
-- if the questionnaire definition's attributes are referenced in reports though...

-- step 1 delete all answers associated with the questionnaires for each workflow
-- step 2 delete questionnaires that reference the workflows
-- step 3 delete from the nodes table the referenced questionnaires
-- step 1 delete all notification processes
-- step 1 delete all performance_managers
-- step 1 delete all performance_evaluators
-- step 2 delete all performance_reviews referenced by the workflows
-- step 3 delete all the associated workflows
-- step 3 gather the lookups associated with the dynamic_attributes
-- step 3 delete the attributes and references
-- step 4 delete the lookup_values
-- step 4 delete the lookup_types

--==================================================================================
PROCEDURE delete_que_definition (
    que_def_id_ in integer
)

IS

    cursor node_cur is select n.* from nodes n, questionnaires q
        where q.node_id = n.id and q.qwf_id in (select id from que_workflows where qd_id=que_def_id_);

    node_rec nodes%rowtype;

BEGIN
    -- delete answers, questionnaires and nodes
	FOR node_rec IN node_cur LOOP                
		delete from node_das where node_id=node_rec.id;
		delete from questionnaires where node_id=node_rec.id;	
   		delete from nodes where id=node_rec.id;     		
 		--dbms_output.put_line(node_rec.id);
    END LOOP;

    -- delete the notifications, managers and participants
    delete from notifications where workflow_id in (select id from que_workflows where qd_id=que_def_id_);
    delete from performance_managers where performance_id in (select performance_id from que_workflows where qd_id=que_def_id_);
    delete from performance_evaluators where performance_id in (select performance_id from que_workflows where qd_id=que_def_id_);

    -- delete the workflows
    delete from que_workflows where qd_id=que_def_id_;
    -- performance_reviews
    delete from performance_reviews where id in (select performance_id from que_workflows where qd_id=que_def_id_);

    -- delete the lookup_values
    delete from lookup_values where id in
        ( select lv.id from lookup_values lv, lookup_types lt, dynamic_attributes da
            where da.qd_id=que_def_id_
            and da.refers_to=lt.id
            and lv.type_id=lt.id );

     -- lookup_types
     delete from lookup_types where id in
        ( select lt.id from lookup_types lt, dynamic_attributes da
            where da.qd_id=que_def_id_
            and da.refers_to=lt.id);

     -- dynamic_attributes       
     delete from node_das where da_id in (select id from dynamic_attributes where qd_id=que_def_id_);
     delete from dynamic_attributes where qd_id=que_def_id_;

     -- note questions should be handled by the cascade delete definition on the table
     delete from que_definitions where id=que_def_id_;
     delete from que_xml_data where id=que_def_id_;

END delete_que_definition;
------------------------------------------------------

PROCEDURE delete_lineitem_row (
    dynamic_position_ in integer
   ,line_item_id_ in integer
   ,q_id_ in integer
)

IS
    count_ integer := 0;
    cursor node_cur is select nd.* from node_das nd, questions q
                            where nd.node_id=q_id_
                            and q.question_line_item_id=line_item_id_
                            and q.da_id=nd.da_id
                            and nd.dynamic_position > dynamic_position_
                            order by nd.dynamic_position;

    attr_rec node_das%rowtype;
BEGIN

    delete from node_das nd
        where nd.node_id=q_id_
        and nd.dynamic_position=dynamic_position_
        and da_id in (
            select da_id from questions q where q.question_line_item_id=line_item_id_ );
    BEGIN
        OPEN node_cur;
            LOOP
                FETCH node_cur INTO attr_rec;
                EXIT WHEN node_cur%notfound;

                UPDATE node_das set dynamic_position=attr_rec.dynamic_position - 1 where node_das.id=attr_rec.id;

            END LOOP;
        CLOSE node_cur;
    END;

EXCEPTION
  WHEN OTHERS THEN
	zynap_error_sp.ERROR( zynap_error_sp.ERR_INTERNAL, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

END delete_lineitem_row;   

------------------------------------------------------------------------------------------------------------
-- private end of line function that updates the current job of each subject found from the procedure
-- update_current_holder_info(position_id) for each subject found we need to update their current jobs.
------------------------------------------------------------------------------------------------------------
PROCEDURE update_current_job (
    subject_id_ in integer
)
IS
    cursor position_cur is
    SELECT p.title, p.node_id
		from positions p, subject_primary_associations sa, nodes n
		where sa.position_id = p.node_id
		and p.node_id=n.id
		and n.is_active='T'
		and sa.subject_id = subject_id_
        order by p.title;

	pos_rec positions%rowtype;
	l_string varchar2(1025);

BEGIN

    l_string := '';
	FOR pos_rec IN position_cur LOOP
            l_string := (l_string || pos_rec.title || ', ');
    END LOOP;
    IF length(l_string) > 3 THEN
        l_string := substr(l_string, 0, (length(l_string)-2));
    END IF;
    update subjects set current_job_info = l_string where node_id = subject_id_;

EXCEPTION WHEN NO_DATA_FOUND THEN
	zynap_error_sp.store_error('update_current_job', null, zynap_error_sp.ERR_INTERNAL, sqlcode, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

END update_current_job;

------------------------------------------------------------------------------------------------------------
-- updates a column on the positions table providing information on the current holders if any
------------------------------------------------------------------------------------------------------------
PROCEDURE update_current_holder_info (
    position_id_ in integer
)
IS
    cursor coredetails_cur is 
    SELECT s.node_id, cd.first_name, cd.second_name
		from core_details cd, subject_primary_associations sa, subjects s, nodes n
		where s.cd_id = cd.id
		and s.node_id = sa.subject_id 
		and s.node_id=n.id
		and n.is_active='T'
		and sa.position_id=position_id_
		order by cd.first_name;				

	core_details_rec core_details%rowtype;
	l_string varchar2(1025);
    active_ varchar(1);

BEGIN

    l_string := '';
    select is_active into active_ from nodes where id = position_id_;
	FOR core_details_rec IN coredetails_cur LOOP
        l_string := (l_string || core_details_rec.first_name || ' ' || core_details_rec.second_name || ', ');
       IF active_ = 'F'   THEN
           -- update the info for each of these positions
           update_delete_job_info(core_details_rec.node_id, position_id_);
       ELSE
          update_current_job(core_details_rec.node_id);
       END IF;
    END LOOP;
    IF length(l_string) > 3 THEN
        l_string := substr(l_string, 0, (length(l_string)-2));
    END IF;
    update positions set current_holder_info = l_string where node_id = position_id_;
    
EXCEPTION WHEN NO_DATA_FOUND THEN  
--module_id_ IN VARCHAR2, user_id_ IN INTEGER, error_type_ IN VARCHAR2, error_code_ IN VARCHAR2, error_message_ IN VARCHAR2 )
	zynap_error_sp.store_error('update_current_holder_info', null, zynap_error_sp.ERR_INTERNAL, sqlcode, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

END update_current_holder_info;        

------------------------------------------------------------------------------------------------------------
-- private end of the line function get called from update_current_job(subject_id) and is meant to take each
-- position that the subject has been assigned to and updates the positions current holder info.
------------------------------------------------------------------------------------------------------------
PROCEDURE update_current_holder (
    position_id_ in integer
)
IS
    cursor coredetails_cur is
    SELECT cd.id, cd.first_name, cd.second_name
		from core_details cd, subject_primary_associations sa, subjects s, nodes n
		where s.cd_id = cd.id
		and s.node_id = sa.subject_id
		and s.node_id=n.id
		and n.is_active='T'
		and sa.position_id=position_id_
		order by cd.first_name;

	core_details_rec core_details%rowtype;
	l_string varchar2(1025);
	active_ varchar2(1);

BEGIN

    select is_active into active_ from nodes where id = position_id_;
    l_string := '';
	FOR core_details_rec IN coredetails_cur LOOP
        l_string := (l_string || core_details_rec.first_name || ' ' || core_details_rec.second_name || ', ');
    END LOOP;
    IF length(l_string) > 3 THEN
        l_string := substr(l_string, 0, (length(l_string)-2));
    END IF;
    update positions set current_holder_info = l_string where node_id = position_id_;

EXCEPTION WHEN NO_DATA_FOUND THEN
	zynap_error_sp.store_error('zynap_node_sp.update_current_holder', null, zynap_error_sp.ERR_INTERNAL, sqlcode, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

END update_current_holder;

------------------------------------------------------------------------------------------------------------
-- updates a column on the subjects table providing the comma separated list of current jobs
-- if subject.is_active = 'F' update positions current holder info!
-- ROOT entry into updating a person's job info, it also calls update_current_holder
------------------------------------------------------------------------------------------------------------
PROCEDURE update_current_job_info (
    subject_id_ in integer
)
IS
    cursor position_cur is 
    SELECT p.title, p.node_id
		from positions p, subject_primary_associations sa, nodes n
		where sa.position_id = p.node_id
		and p.node_id=n.id
		and n.is_active='T'
		and sa.subject_id = subject_id_		
        order by p.title;
        
	pos_rec positions%rowtype;
	l_string varchar2(1025);
	active_ varchar(1);

BEGIN

    select is_active into active_ from nodes where id = subject_id_;
    l_string := '';
	FOR pos_rec IN position_cur LOOP
        l_string := (l_string || pos_rec.title || ', ');
        IF active_ = 'F'   THEN
            -- active is false therefore we need to remove this information
            update_delete_holder_info(pos_rec.node_id, subject_id_);
        ELSE         
           -- node is active now update the job
           update_current_holder(pos_rec.node_id);     
        END IF;
    END LOOP;
    IF length(l_string) > 3 THEN
        l_string := substr(l_string, 0, (length(l_string)-2)); 
    END IF;
    update subjects set current_job_info = l_string where node_id = subject_id_;
    
EXCEPTION WHEN NO_DATA_FOUND THEN  
--module_id_ IN VARCHAR2, user_id_ IN INTEGER, error_type_ IN VARCHAR2, error_code_ IN VARCHAR2, error_message_ IN VARCHAR2 )
	zynap_error_sp.store_error('zynap_node_sp.update_current_job_info', null, zynap_error_sp.ERR_INTERNAL, sqlcode, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

END update_current_job_info;


------------------------------------------------------------------------------------------------------------
-- updates a column on the subjects table providing the comma separated list of current jobs
------------------------------------------------------------------------------------------------------------
PROCEDURE update_delete_holder_info (
    position_id_ in integer,
    subject_id_ in integer
)
IS
    cursor coredetails_cur is SELECT cd.*
		from core_details cd, subject_primary_associations sa, subjects s, nodes n
		where s.cd_id = cd.id
		and s.node_id = sa.subject_id
		and s.node_id <> subject_id_
		and s.node_id=n.id
		and n.is_active='T'
		and sa.position_id=position_id_ order by cd.first_name;

	core_details_rec core_details%rowtype;
	l_string varchar2(1025);

BEGIN

    l_string := '';
	FOR core_details_rec IN coredetails_cur LOOP
            l_string := (l_string || core_details_rec.first_name || ' ' || core_details_rec.second_name || ', ');
    END LOOP;
    IF length(l_string) > 3 THEN
        l_string := substr(l_string, 0, (length(l_string)-2));
    END IF;
    update positions set current_holder_info = l_string where node_id = position_id_;

EXCEPTION WHEN NO_DATA_FOUND THEN
	zynap_error_sp.store_error('update_delete_holder_info', null, zynap_error_sp.ERR_INTERNAL, sqlcode, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

END update_delete_holder_info;

------------------------------------------------------------------------------------------------------------
-- selects the positions for which a subject holds a job, excluding the about to be deleted position
------------------------------------------------------------------------------------------------------------
PROCEDURE update_delete_job_info (
    subject_id_ in integer,
    position_id_ in integer
)
IS
    cursor position_cur is SELECT p.*
		from positions p, subject_primary_associations sa, nodes n
		where sa.position_id = p.node_id                          
		and p.node_id <> position_id_  
		and p.node_id=n.id
		and n.is_active='T'
		and sa.subject_id=subject_id_ order by p.title;

	pos_rec positions%rowtype;
	l_string varchar2(1025);

BEGIN

    l_string := '';
	FOR pos_rec IN position_cur LOOP
         l_string := (l_string || pos_rec.title || ', ');
    END LOOP;
    IF length(l_string) > 3 THEN
        l_string := substr(l_string, 0, (length(l_string)-2));
    END IF;
    update subjects set current_job_info = l_string where node_id = subject_id_;

EXCEPTION WHEN NO_DATA_FOUND THEN  
	zynap_error_sp.store_error('zynap_node_sp.update_delete_job_info', null, zynap_error_sp.ERR_INTERNAL, sqlcode, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');

END update_delete_job_info;

------------------------------------------------------------------------------------------------------------
END zynap_node_sp;
/
