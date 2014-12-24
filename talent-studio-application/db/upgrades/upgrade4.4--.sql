--------------------------------------------------------------------------
-- Play this script in TS1@ZYNAP01 to make it look like JOSE@TS
--                                                                      --
-- Please review the script before using it to make sure it won't       --
-- cause any unacceptable data loss.                                    --
--                                                                      --
-- TS1@ZYNAP01 Schema Extracted by User TS1
-- JOSE@TS Schema Extracted by User JOSE
--------------------------------------------------------------------------
-- "Set define off" turns off substitution variables
Set define off;

--
-- 190. REPORTS  (Table)
--
-- No action taken.
--
-- 191. IS_FORMULA  (Column)
--
ALTER TABLE REPORT_COLUMNS ADD (IS_FORMULA  VARCHAR2(1 BYTE)  DEFAULT 'F');

@packages/zynap_loader_spec.sql
@packages/zynap_loader_body.sql

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
SHOW ERRORS;

-- add comments to add and details view for
INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'comments', 'Comments', 'Position', -2, 2, 'TEXTAREA', 'F');

INSERT INTO REPORT_COLUMNS(ID,REF_VALUE,LABEL,SOURCE,REP_ID,POSITION,COLUMN_TYPE,MANDATORY)
VALUES(rep_column_sq.nextval, 'comments', 'Comments', 'Position', -31, 2, 'TEXTAREA', 'F');

-- increment report column positions to make way for comments in position views
update report_columns set position = position + 1 where position >= 2 and ref_value <> 'comments' and rep_id = -2;
update report_columns set position = position + 1 where position >= 2 and ref_value <> 'comments' and rep_id = -31;

commit;

