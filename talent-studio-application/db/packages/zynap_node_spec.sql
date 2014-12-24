CREATE OR REPLACE PACKAGE zynap_node_sp IS

PROCEDURE delete_que_definition (
    que_def_id_ in integer
);

PROCEDURE delete_lineitem_row (
    dynamic_position_ in integer
   ,line_item_id_ in integer
   ,q_id_ in integer
);

PROCEDURE update_current_holder_info (
    position_id_ in integer    
);

PROCEDURE update_current_job_info (
    subject_id_ in integer  
); 

PROCEDURE update_delete_holder_info (
    position_id_ in integer,
    subject_id_ in integer
);

PROCEDURE update_delete_job_info (
    subject_id_ in integer,
    position_id_ in integer
);

END zynap_node_sp;
/
