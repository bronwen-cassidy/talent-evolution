CREATE OR REPLACE
PACKAGE BODY zynap_org_unit_sp AS


---------------------------------------------------------------------------------------------
-- Description - Does not perform a convenntional delete, instead this procedure - checks the
-- 			   	 validility of the delete, checks to see if the user has full security privilidges
--				 within the OU stucture and full security privilidges over the positions within
--				 the OU structure
--
-- Exceptions - None
---------------------------------------------------------------------------------------------

PROCEDURE delete_org_unit (
   org_unit_id_ IN NUMBER
   ,user_id_ in integer
   ,username_ in varchar2
)
IS

   cursor node_cur is SELECT * FROM ou_hierarchy_inc WHERE root_id = org_unit_id_ order by hlevel desc;
   ou_rec ou_hierarchy_inc%rowtype;     
   
   title_ varchar2(2000);

BEGIN

    OPEN node_cur;
		LOOP
			FETCH node_cur INTO ou_rec;
			EXIT WHEN node_cur%notfound;

            zynap_position_sp.delete_ou_positions_cascade(ou_rec.id, user_id_, username_);
            
            select label into title_ from organization_units where node_id=ou_rec.id;                                                                                
            
            delete from external_ref_mappings where internal_ref_id=ou_rec.id;
            delete from node_audits where node_id=ou_rec.id;

            -- delete the external references to these organisation units
            delete from external_ref_mappings where internal_ref_id=ou_rec.id;

            -- get all objectives and sets and cascade objectives delete
            delete_ou_objectives(ou_rec.id);

            -- todo determine if we need to remove report_columns or population_criterias 
            delete from node_das where node_id=ou_rec.id;
            delete from area_elements where node_id=ou_rec.id; 
            
            -- delete any objective_sets belonging to this organisation unit
            delete from objective_sets where org_unit_id=ou_rec.id;
            
            delete from ou_hierarchy where id=ou_rec.id;
            delete from ou_hierarchy_inc where id=ou_rec.id;
            delete from organization_units where node_id=ou_rec.id;
            delete from nodes where id=ou_rec.id;

            insert into audits (id, modified_by_id, MODIFIED_BY_USERNAME, OBJECT_ID, MODIFIED_DATE, TABLE_NAME, ACTION_PERFORMED, DESCRIPTION)
            values (audits_sq.nextval, user_id_, username_, ou_rec.id, sysdate, 'ORGANIZATION_UNITS', 'DELETED', 'com.zynap.talentstudio.organisation.OrganisationUnit@[id=' || ou_rec.id || ', label=' || title_ || ']');


		END LOOP;
	CLOSE node_cur;

EXCEPTION
  WHEN OTHERS THEN
	zynap_error_sp.ERROR( zynap_error_sp.ERR_INTERNAL, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');
END delete_org_unit;


---------------------------------------------------------------------------------------------
-- Description remove the objectives belonging to the deleted organisation unit
---------------------------------------------------------------------------------------------
procedure delete_ou_objectives (
    org_unit_id_ IN INTEGER
)
IS

    cursor obj_set_cur is select o.* from objectives o, objective_sets s
    where s.org_unit_id=org_unit_id_
    and o.objective_set_id = s.id;
    obj_rec objectives%rowtype;

BEGIN
    OPEN obj_set_cur;
        LOOP
            FETCH obj_set_cur INTO obj_rec;
            EXIT WHEN obj_set_cur%notfound;

            delete_ou_child_objectives(obj_rec.node_id);
            delete from objectives where node_id=obj_rec.node_id;
            delete from nodes where id=obj_rec.node_id;
            

        END LOOP;
    CLOSE obj_set_cur;

EXCEPTION
  WHEN OTHERS THEN
    zynap_error_sp.ERROR( zynap_error_sp.ERR_INTERNAL, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');
END delete_ou_objectives;


---------------------------------------------------------------------------------------------
-- Description remove the objectives belonging to the deleted organisation unit
-- These are the user objectivse that are parented by the organisation unit objectives
--
---------------------------------------------------------------------------------------------
procedure delete_ou_child_objectives (
    objective_id_ IN INTEGER
)
IS
    cursor obj_set_cur is select node_id from objectives where parent_id=objective_id_;    
    obj_rec objectives.node_id%type;

BEGIN
    OPEN obj_set_cur;
        LOOP
            FETCH obj_set_cur INTO obj_rec;
            EXIT WHEN obj_set_cur%notfound;
             
            delete from node_das where node_id=obj_rec;                                                             
            delete from objective_assessments where objective_id=obj_rec;
            delete from objective_assessors where objective_id=obj_rec;
            delete from objectives where node_id=obj_rec;
            delete from nodes where id=obj_rec;

        END LOOP;
    CLOSE obj_set_cur;

EXCEPTION
  WHEN OTHERS THEN
    zynap_error_sp.ERROR( zynap_error_sp.ERR_INTERNAL, 'Oracle Error : [' || sqlcode || ':' || sqlerrm || ']');
END delete_ou_child_objectives;



--------------------------------------------------------------------------------------------------------------
-- objective archive functions
-- approve_assessments functions goes through all the assessments for the given objective id
-- and sets them to approved
--------------------------------------------------------------------------------------------------------------

PROCEDURE approve_assessments (
    objective_id_ IN INTEGER
)

IS

	CURSOR objAssCur IS SELECT id FROM OBJECTIVE_ASSESSMENTS ass WHERE ass.objective_id = objective_id_;
	assessments_rec OBJECTIVE_ASSESSMENTS.id%TYPE;

BEGIN

	OPEN objAssCur;
		LOOP
			FETCH objAssCur INTO assessments_rec;
			EXIT WHEN objAssCur%notfound;

			UPDATE OBJECTIVE_ASSESSMENTS SET IS_APPROVED = 'T' WHERE OBJECTIVE_ASSESSMENTS.ID = assessments_rec;

		END LOOP;
	CLOSE objAssCur;

END approve_assessments;

--------------------------------------------------------------------------------------------------------------
-- archives the objectives that belong to the given objective_set_id
--------------------------------------------------------------------------------------------------------------
PROCEDURE archive_objective_children (
    objective_set_id_ IN INTEGER
)

IS

	CURSOR objCur IS SELECT NODE_ID FROM OBJECTIVES objs WHERE objs.objective_set_id = objective_set_id_;
	objectives_rec OBJECTIVES.NODE_ID%TYPE;

BEGIN

	OPEN objCur;
		LOOP
  			FETCH objCur INTO objectives_rec;
  			EXIT WHEN objCur%notfound;

  			UPDATE OBJECTIVES SET STATUS = 'ARCHIVED' WHERE OBJECTIVES.NODE_ID = objectives_rec;
  			-- set objective_assessments to approved
  			approve_assessments(objectives_rec);

  		END LOOP;
	CLOSE objCur;

END archive_objective_children;

--------------------------------------------------------------------------------------------------------------
-- archives the objective sets that are children to the given objective_set_id
-- this procedure will only be called when the objective set id passed in
-- belongs to the corporate objective set
--------------------------------------------------------------------------------------------------------------

PROCEDURE archive_set_children (
    objective_set_id_ IN INTEGER
)

IS

	CURSOR objSetCur IS SELECT ID FROM OBJECTIVE_SETS objs WHERE objs.corporate_objective_id = objective_set_id_;
	objectives_rec OBJECTIVE_SETS.ID%TYPE;

BEGIN

	OPEN objSetCur;
		LOOP
			FETCH objSetCur INTO objectives_rec;
			EXIT WHEN objSetCur%notfound;

			UPDATE OBJECTIVE_SETS SET STATUS = 'ARCHIVED' WHERE OBJECTIVE_SETS.ID = objectives_rec;
			archive_objective_children(objectives_rec);

		END LOOP;
	CLOSE objSetCur;

END archive_set_children;

--------------------------------------------------------------------------------------------------------------
-- PROCEDURE archive_objectives
-- this takes a currently open corporate objective set id and cascades down through
-- all it's objectives, it's children objectives and archives them, it then
-- makes sure that all assessments have been approved.
--------------------------------------------------------------------------------------------------------------
PROCEDURE archive_objectives (
    objective_set_id_ IN INTEGER
)

IS
    -- Declare any local variables
    isCorporate VARCHAR2(1) := 'T';
    objectiveType VARCHAR2(50);

BEGIN
    -- load
    SELECT objective_sets.type INTO objectiveType FROM objective_sets WHERE objective_sets.id=objective_set_id_;
    UPDATE objective_sets SET status = 'ARCHIVED' WHERE objective_sets.id=objective_set_id_;

    -- archive all direct objectives
    archive_objective_children(objective_set_id_);

    -- archive all objective_sets that hang off the corporate set
    IF objectiveType = 'CORPORATE' THEN
    	-- -- archive all child objective sets
    	archive_set_children(objective_set_id_);

    END IF;

END archive_objectives;

END zynap_org_unit_sp;
/
