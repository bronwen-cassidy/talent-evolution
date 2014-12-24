CREATE OR REPLACE
PACKAGE zynap_org_unit_sp IS

---------------------------------------------------------------------------------------------
-- deletes the orgunit cascading down through it's children and all associated positions
-- and their children whew, beware, massive delete use with caution!!!!
---------------------------------------------------------------------------------------------
PROCEDURE delete_org_unit (
   org_unit_id_ IN NUMBER
   ,user_id_ in integer
   ,username_ in varchar2   
);
       
---------------------------------------------------------------------------------------------
-- deletes the objectives for a given organisation unit.
-- NOTE: this procedure will alos delete any USER objectives which hang of any of the 
-- specified organisation unit's objectives
---------------------------------------------------------------------------------------------
procedure delete_ou_objectives (
    org_unit_id_ IN INTEGER
);
                                      
--------------------------------------------------------------------------------------------
-- deletes the user objectives which are parented by the specified objective_id_ param
--------------------------------------------------------------------------------------------
procedure delete_ou_child_objectives (
    objective_id_ IN INTEGER
);
    
--------------------------------------------------------------------------------------------
-- archives all objectives within the given objective set specified by the 
-- objective_set_id_ param
--------------------------------------------------------------------------------------------
PROCEDURE archive_objectives (
    objective_set_id_ IN INTEGER
);

END zynap_org_unit_sp;
/
