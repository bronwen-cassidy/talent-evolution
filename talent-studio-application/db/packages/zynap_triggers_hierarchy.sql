CREATE OR REPLACE TRIGGER "POSITIONS_HIERARCHY_IU" AFTER
INSERT
OR UPDATE
OF "PARENT_ID" ON "POSITIONS" FOR EACH ROW BEGIN
 if INSERTING then
    zynap_hierarchy_sp.insert_position_hi(:NEW.node_id,:NEW.parent_id);
 end if;
if UPDATING AND NOT (:NEW.parent_id = :OLD.parent_id )then
    zynap_hierarchy_sp.update_position_hi(:NEW.node_id,:NEW.parent_id);
end if;

END POSITIONS_HIERARCHY;
/

CREATE OR REPLACE TRIGGER "OU_HIERARCHY_IU" AFTER
INSERT
OR UPDATE
OF "PARENT_ID" ON "ORGANIZATION_UNITS" FOR EACH ROW BEGIN
 if INSERTING then
     zynap_hierarchy_sp.insert_ou_hi(:NEW.node_id,:NEW.parent_id);
 end if;
if UPDATING AND NOT (:NEW.parent_id = :OLD.parent_id )then
    zynap_hierarchy_sp.update_ou_hi(:NEW.node_id,:NEW.parent_id);
end if;

END OU_HIERARCHY_IU;
/

CREATE OR REPLACE TRIGGER "PRIMARY_SUB_ASSOC" AFTER
INSERT
OR UPDATE
OF "VALUE_ID","POSITION_ID","SUBJECT_ID" ON "SUBJECT_ASSOCIATIONS" FOR EACH ROW

declare flag_ integer;

BEGIN
 
 if INSERTING AND (zynap_hierarchy_sp.isSubPrimaryAssociation(:NEW.value_id)) then

    INSERT INTO SUBJECT_PRIMARY_ASSOCIATIONS (ID,VALUE_ID,POSITION_ID,SUBJECT_ID)
    VALUES(:NEW.ID,:NEW.VALUE_ID,:NEW.POSITION_ID,:NEW.SUBJECT_ID);                 
    
 end if;
 
 if UPDATING then

    delete SUBJECT_PRIMARY_ASSOCIATIONS where ID = :NEW.ID;    
    if (zynap_hierarchy_sp.isSubPrimaryAssociation(:NEW.value_id)) then
    
    	INSERT INTO SUBJECT_PRIMARY_ASSOCIATIONS (ID,VALUE_ID,POSITION_ID,SUBJECT_ID)
        VALUES(:NEW.ID,:NEW.VALUE_ID,:NEW.POSITION_ID,:NEW.SUBJECT_ID);

        select count(*) into flag_ from subject_primary_associations sa  where sa.subject_id = :OLD.subject_id;
        IF flag_ < 1 THEN
        	insert into area_elements (ID,AREA_ID,NODE_ID,CASCADING) VALUES (ASSOC_SQ.NEXTVAL,-2, :OLD.SUBJECT_ID, 'F');
        	zynap_node_sp.update_delete_holder_info(:OLD.POSITION_ID, :OLD.SUBJECT_ID);
        	zynap_node_sp.update_delete_job_info(:OLD.SUBJECT_ID, :OLD.POSITION_ID);
        END IF;

        zynap_node_sp.update_current_holder_info(:NEW.POSITION_ID);
        zynap_node_sp.update_current_holder_info(:OLD.POSITION_ID);
        zynap_node_sp.update_current_job_info(:NEW.SUBJECT_ID);
        zynap_node_sp.update_current_job_info(:OLD.SUBJECT_ID);

    end if;

end if;

END PRIMARY_SUB_ASSOC;
/

CREATE OR REPLACE TRIGGER "PRIMARY_SUB_ASSOC2" BEFORE
DELETE ON SUBJECT_ASSOCIATIONS
 FOR EACH ROW
declare flag_ integer;
 BEGIN
     IF (zynap_hierarchy_sp.isSubPrimaryAssociation(:OLD.value_id)) THEN

        delete from subject_primary_associations where id = :OLD.id;
        select count(*) into flag_ from subject_primary_associations sa  where sa.subject_id = :OLD.subject_id;
        IF flag_ < 1 THEN
        	insert into area_elements (ID,AREA_ID,NODE_ID,CASCADING) VALUES (ASSOC_SQ.NEXTVAL,-2, :OLD.SUBJECT_ID, 'F');
        END IF;

        zynap_node_sp.update_delete_holder_info(:OLD.POSITION_ID, :OLD.SUBJECT_ID);
        zynap_node_sp.update_delete_job_info(:OLD.SUBJECT_ID, :OLD.POSITION_ID);

     END IF;

END PRIMARY_SUB_ASSOC2;
/

CREATE OR REPLACE TRIGGER "SUB_PRIMARY_ASSOC_AE" AFTER
INSERT
OR DELETE ON "SUBJECT_PRIMARY_ASSOCIATIONS" FOR EACH ROW 
 declare 
 flag_  integer;
 comp_ boolean;
 BEGIN
 if INSERTING then
    delete from area_elements where area_id = -2 and node_id = :NEW.SUBJECT_ID; 
 end if;   
 comp_ := true;
END SUB_PRIMARY_ASSOC_AE;
/

CREATE OR REPLACE TRIGGER "SUBJECTS_AE" AFTER
INSERT OR DELETE
ON "SUBJECTS" FOR EACH ROW
BEGIN
 if INSERTING then
    insert into area_elements (ID,AREA_ID,NODE_ID,CASCADING) VALUES (ASSOC_SQ.NEXTVAL,-2, :NEW.NODE_ID, 'F');
 end if;
 if DELETING then
    delete from area_elements where area_id = -2 and node_id = :OLD.NODE_ID;
 end if;
END SUBJECTS_AE;
/

CREATE OR REPLACE TRIGGER "PERMITS_USER_SESSION" AFTER
INSERT
OR UPDATE
OF "LOGGED_OUT_DTS" ON "SESSION_LOGS" FOR EACH ROW BEGIN
 if INSERTING then
     --delete from USER_NODE_DOMAIN_PERMITS WHERE USER_ID = :NEW.USER_ID;
     --insert /*+ APPEND */ into USER_NODE_DOMAIN_PERMITS (USER_ID,NODE_ID,PERMIT_ID)
     --select USER_ID,NODE_ID,PERMIT_ID from ZYNAP_USER_DOMAIN_PERMITS WHERE USER_ID = :NEW.USER_ID and action ='view';
     insert into SESSIONS_OPENED (SESSION_ID,USER_ID) VALUES (:NEW.ID,:NEW.USER_ID);
 end if;
 if UPDATING AND (:NEW.LOGGED_OUT_DTS is not null )then
    delete from SESSIONS_OPENED WHERE SESSION_ID = :NEW.ID;   
    --delete from USER_NODE_DOMAIN_PERMITS WHERE USER_ID = :NEW.USER_ID AND
    --NOT EXISTS (SELECT 1 FROM SESSIONS_OPENED WHERE USER_ID =:NEW.USER_ID) ;
end if;
END PERMITS_USER_SESSION;
/


CREATE OR REPLACE TRIGGER "ADD_NODE_PT_USER_SESSION" AFTER
INSERT
ON "NODE_AUDITS" FOR EACH ROW BEGIN
     insert /*+ APPEND */ into USER_NODE_DOMAIN_PERMITS (USER_ID,NODE_ID,PERMIT_ID)
     select USER_ID,NODE_ID,PERMIT_ID from ZYNAP_USER_DOMAIN_PERMITS dp WHERE NODE_ID = :NEW.NODE_ID;
     --and EXISTS (SELECT 1 from USER_NODE_DOMAIN_PERMITS WHERE USER_ID = dp.USER_ID);
END ADD_OU_PT_USER_SESSION;
/

CREATE OR REPLACE TRIGGER "VERIFY_NON_DULICATE_NODE_DAS"
  BEFORE INSERT ON NODE_DAS FOR EACH ROW

  declare flag_ integer;
  datype_ varchar2 (100);
  dynamic_ varchar2 (2);

  BEGIN

    SELECT TYPE INTO datype_ FROM DYNAMIC_ATTRIBUTES WHERE ID = :NEW.DA_ID;
    SELECT IS_DYNAMIC INTO dynamic_ FROM DYNAMIC_ATTRIBUTES WHERE ID = :NEW.DA_ID;

    IF(dynamic_ = 'F') THEN
        IF (datype_ NOT IN ('MULTISELECT', 'COMMENTS')) THEN
            -- this is a single valued type so there should be no existing values for the da_id and node_id in the database if there is we need to delete it
            SELECT COUNT(*) INTO flag_ FROM NODE_DAS WHERE DA_ID = :NEW.DA_ID AND NODE_ID = :NEW.NODE_ID;
            IF(flag_ > 0) THEN
                DELETE FROM NODE_DAS WHERE DA_ID = :NEW.DA_ID AND NODE_ID = :NEW.NODE_ID;
            END IF;
        END IF;
    END IF;

END VERIFY_NON_DULICATE_NODE_DAS;
/







