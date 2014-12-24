--===================================================
--UPDATE CURRENT JOB/HOLDER INFO
--===================================================
DECLARE

CURSOR assoc_cur IS SELECT * from subject_primary_associations;
assoc_rec subject_primary_associations%rowtype;

BEGIN

   FOR assoc_rec IN assoc_cur LOOP
        zynap_node_sp.update_current_job_info(assoc_rec.subject_id);
        zynap_node_sp.update_current_holder_info(assoc_rec.position_id);
   END LOOP;

END;
/
