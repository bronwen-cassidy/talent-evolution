CREATE OR REPLACE
PACKAGE az_aop_sp IS


PROCEDURE migrate_aop (
    new_que_wf_id_ in integer
    ,old_que_wf_id_ in integer
    ,old_da_id_ in integer
    ,new_da_id_ in integer
);   

procedure migrate_workflows (
	new_que_wf_id_ in integer
    ,old_que_wf_id_ in integer  
);

procedure migrate_attrs_sub_to_pos (
	subject_daid_ in integer
	,position_daid_ in integer
); 

procedure migrate_to_pos (
	subject_id_ in integer
	,value_ in varchar2    
	,da_id_ in integer
);


END az_aop_sp;
/
