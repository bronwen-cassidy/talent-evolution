declare
  sequence_val integer;
  sentence varchar(200); 
begin                 
  select count(*) into sequence_val from wf_notifications;
  if (sequence_val >0) then
	  sentence := 'alter sequence wf_notifications_s increment by ' || sequence_val;   
	  EXECUTE IMMEDIATE sentence ;
	  select wf_notifications_s.nextval into sequence_val from dual;                                             
	  EXECUTE IMMEDIATE 'alter sequence wf_notifications_s increment by 1 '; 
 end if;  
 select count(*) into sequence_val from wf_process_activities;
  if (sequence_val >0) then
	  sentence := 'alter sequence wf_process_activities_s increment by ' || sequence_val;   
	  EXECUTE IMMEDIATE sentence ;
	  select wf_process_activities_s.nextval into sequence_val from dual;                                             
	  EXECUTE IMMEDIATE 'alter sequence wf_process_activities_s increment by 1 '; 
 end if;  
  select count(*) into sequence_val from wf_items where item_type='ERROR';
  if (sequence_val >0) then
	  sentence := 'alter sequence WF_ERROR_PROCESSES_S increment by ' || sequence_val;   
	  EXECUTE IMMEDIATE sentence ;
	  select WF_ERROR_PROCESSES_S.nextval into sequence_val from dual;                                             
	  EXECUTE IMMEDIATE 'alter sequence WF_ERROR_PROCESSES_S increment by 1 '; 
 end if;  
   select count(*) into sequence_val from wf_items where item_type='WFSURV';
  if (sequence_val >0) then
	  sentence := 'alter sequence WF_SURVEYDEMO_S increment by ' || sequence_val;   
	  EXECUTE IMMEDIATE sentence ;
	  select WF_SURVEYDEMO_S.nextval into sequence_val from dual;                                             
	  EXECUTE IMMEDIATE 'alter sequence WF_SURVEYDEMO_S increment by 1 '; 
 end if;  
end;
/
quit;
