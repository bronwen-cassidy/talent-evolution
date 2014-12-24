CREATE OR REPLACE PACKAGE BODY az_aop_sp IS

------------------------------------------------------------------------------------------------------------
-- updates a column on the positions table providing information on the current holders if any
------------------------------------------------------------------------------------------------------------
PROCEDURE migrate_aop (
    new_que_wf_id_ in integer
    ,old_que_wf_id_ in integer
    ,old_da_id_ in integer
    ,new_da_id_ in integer
)
IS

    cursor das_cur IS select nd.* from node_das nd, dynamic_attributes da, questionnaires q
                    where nd.node_id =q.node_id
                    and q.qwf_id=old_que_wf_id_
                    and nd.da_id=da.id
                    and da.id=old_da_id_;

    das_rec node_das%rowtype;

    subject_id_ number;
    q_id_ number;
    q_count number;
    n_id_ number;
    nd_value_ number;       
    da_type_ varchar2(255);

BEGIN

    FOR das_rec IN das_cur LOOP
       
      -- clear any previous value
      nd_value_ := null;
      
      -- load in the subject_id from questionnaires with the given node_id
      select subject_id into subject_id_ from questionnaires where node_id=das_rec.node_id;

      -- check to see if we have a questionnaire
      select count(*) into q_count from questionnaires where qwf_id=new_que_wf_id_ and subject_id=subject_id_;

      IF q_count > 0 THEN
          -- we have a questionnaire no need to create one
        select node_id into n_id_ from questionnaires where subject_id=subject_id_ and qwf_id=new_que_wf_id_;
        --dbms_output.put_line('in the if');
        
      ELSE
          -- we need to create a questionnaire
          -- store the node_id
          select node_sq.nextval into n_id_ from dual; 
          insert into nodes (id, node_type, is_active, lock_id) values (n_id_, 'Q', 'T', -1);
          insert into questionnaires(node_id, qwf_id, user_id, subject_id, is_completed) values(n_id_, new_que_wf_id_, 1, subject_id_, 'F');
          
          --dbms_output.put_line('in the else');

      END IF;
	  
	  -- check to see if we need to look at the lookup_values table
	  select refers_to into da_type_ from dynamic_attributes where id=old_da_id_;
      -- insert into node_das the answer we found 		
      
	  IF da_type_ is not null THEN 	        
          -- find the answer with the matching label
          select lv.id into nd_value_ from lookup_values lv, dynamic_attributes da
      	      where da.refers_to = lv.type_id
      	      and da.id = new_da_id_
      	      and lv.short_desc=(select short_desc from lookup_values where id=das_rec.value);  
      	      
		  IF nd_value_ is not null THEN      
		      
      	      insert into node_das (id, node_id, da_id, value) values (da_sq.nextval, n_id_, new_da_id_, nd_value_);      
          END IF;       
          
      ELSE       	  
        --dbms_output.put_line('BLAST');	
      	insert into node_das (id, node_id, da_id, value) values (da_sq.nextval, n_id_, new_da_id_, das_rec.value);      	
      END IF;
      
      
      --dbms_output.put_line('inserting da_id: ' || new_da_id_ || ' with a value of ' || das_rec.value);

 END LOOP;

END migrate_aop;

-----------------------------------------------------------------------         

procedure migrate_workflows (
	new_que_wf_id_ in integer
    ,old_que_wf_id_ in integer  
)

IS
cursor das_cur IS select nd.* from node_das nd, questionnaires q
                    where nd.node_id =q.node_id
                    and q.qwf_id=old_que_wf_id_;

    das_rec node_das%rowtype;

    subject_id_ number;
    q_count number;
    n_id_ number;

BEGIN

    FOR das_rec IN das_cur LOOP

      -- load in the subject_id from questionnaires with the given node_id
      select subject_id into subject_id_ from questionnaires where node_id=das_rec.node_id;

      -- check to see if we have a questionnaire
      select count(*) into q_count from questionnaires where qwf_id=new_que_wf_id_ and subject_id=subject_id_;

      IF q_count > 0 THEN
          -- we have a questionnaire no need to create one
        select node_id into n_id_ from questionnaires where subject_id=subject_id_ and qwf_id=new_que_wf_id_;
        --dbms_output.put_line('in the if');
        
      ELSE
          -- we need to create a questionnaire
          -- store the node_id
          select node_sq.nextval into n_id_ from dual; 
          insert into nodes (id, node_type, is_active, lock_id) values (n_id_, 'Q', 'T', -1);
          insert into questionnaires(node_id, qwf_id, user_id, subject_id, is_completed) values(n_id_, new_que_wf_id_, 1, subject_id_, 'F');
          
          --dbms_output.put_line('in the else');

      END IF;
      
       -- insert into node_das the answer we found 	
      insert into node_das (id, node_id, da_id, value, dynamic_position, added_by, date_added, disabled) 
             values (da_sq.nextval, n_id_, das_rec.da_id, das_rec.value, das_rec.dynamic_position, das_rec.added_by, das_rec.date_added, das_rec.disabled);
      
      --dbms_output.put_line('inserting da_id: ' || new_da_id_ || ' with a value of ' || das_rec.value);

 END LOOP;    
     
END migrate_workflows;
-----------------------------------------------------------------------   

procedure migrate_to_pos (
	subject_id_ in integer 
	,value_ in varchar2 
	,da_id_ in integer
)

is

	cursor x_cur is select sa.position_id from subject_primary_associations sa, nodes n
 		where sa.subject_id=subject_id_
 		and n.id=position_id
 		and n.is_active='T';    
 		
 	x_rec subject_primary_associations.position_id%type;
 		                     
 		
begin   
    
	OPEN x_cur;
         LOOP
             FETCH x_cur INTO x_rec;
             EXIT WHEN x_cur%notfound;
			insert into node_das (id, node_id, da_id, value) values (da_sq.nextval, x_rec, da_id_, value_);
	END LOOP;

end migrate_to_pos;  

--------------------------------------------------------------------------------------------------------------------------
  
procedure migrate_attrs_sub_to_pos (
	subject_daid_ in integer
	,position_daid_ in integer
)

is
	
	CURSOR das_cur IS select nd.* from node_das nd, nodes n 
	where nd.da_id=subject_daid_ 
	and nd.node_id=n.id
	and n.is_active='T';
	
das_rec node_das%rowtype;
pos_id_ integer;
p_count integer;

BEGIN

	 FOR das_rec IN das_cur LOOP
	
	    select count(*) into p_count from subject_primary_associations where subject_id=das_rec.node_id;
	
	    if p_count > 0 then
	
	 		migrate_to_pos(das_rec.node_id, das_rec.value, position_daid_);
	
	 	end if;
	
	 END LOOP;                             

end migrate_attrs_sub_to_pos;

END az_aop_sp;
/
