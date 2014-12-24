CREATE OR REPLACE PACKAGE BODY zynap_admin_sp IS

------------------------------------------------------------------------------
--------------- BATCH JOBS ---------------------------------------------------
------------------------------------------------------------------------------
PROCEDURE start_batch_jobs(
   p_schema_owner IN VARCHAR2
)   
IS
   job_ BINARY_INTEGER;
BEGIN
     -- Run analyze job once every Sunday 1.00 AM 
	 dbms_job.submit(
	   job_, 
	   'zynap_admin_sp.analyze_schema_job(' || p_schema_owner || ');',
	   trunc(next_day(sysdate,'SUN'))+1/24,
	   'trunc(next_day(sysdate,''SUN''))+1/24' 
	 );
   
END start_batch_jobs;


-- Because Oracle9i schema statistics work best with external system load.
-- Here we refresh statistics using the "auto" option which works with the table monitoring facility 
-- to only re-analyze those Oracle tables that have experienced more than a 10% change in row content
--
PROCEDURE analyze_schema_job(
   p_schema_owner IN VARCHAR2
)
IS
BEGIN
   
   dbms_stats.gather_schema_stats(
      ownname          => p_schema_owner, 
      estimate_percent => dbms_stats.auto_sample_size, 
      method_opt       => 'for all columns size auto', 
      degree           => 7
   );
   
END analyze_schema_job;

--==============================================
-- ASSIGN JOB AND CURRENT HOLDER COLUMNS
--==============================================
procedure assign_job_info

IS
	CURSOR subject_cur IS SELECT node_id from subjects where current_job_info is null;
	subject_rec subjects.node_id%type;
	
BEGIN
   	OPEN subject_cur;
   		LOOP
   			FETCH subject_cur INTO subject_rec;
   			EXIT WHEN subject_cur%notfound;
	
        	zynap_node_sp.update_current_job_info(subject_rec); 
        END LOOP;
    CLOSE subject_cur;       

END assign_job_info; 

--====================================================================
procedure assign_holder_info

IS
	CURSOR position_cur IS select node_id from positions where current_holder_info is null;
	position_rec positions.node_id%type;
	
BEGIN
       
    OPEN position_cur;
   		LOOP
   			FETCH position_cur INTO position_rec;
   			EXIT WHEN position_cur%notfound;
        	zynap_node_sp.update_current_holder_info(position_rec);
        END LOOP;
    CLOSE position_cur; 
    

END assign_holder_info; 	

END zynap_admin_sp;
/
