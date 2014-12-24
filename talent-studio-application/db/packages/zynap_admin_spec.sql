CREATE OR REPLACE PACKAGE zynap_admin_sp AS

------------------------------------------------------------------------------
--------------- BATCH JOBS ---------------------------------------------------
------------------------------------------------------------------------------

-- This method starts the batch job queue, and it should be executed manually once
-- after the system has been installed. It sets up the different Oracle batch jobs 
PROCEDURE start_batch_jobs(
   p_schema_owner IN VARCHAR2
);   


 -- Procedure that gathers statistics for the given schema
PROCEDURE analyze_schema_job(
   p_schema_owner IN VARCHAR2
);

procedure assign_job_info;   
procedure assign_holder_info;

END zynap_admin_sp;
/
