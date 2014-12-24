--------------------------------------------------------
-- 06/08/09
--------------------------------------------------------

prompt
prompt **************************** HAVE YOU BACKED UP YOUR DATABASE (Y/N)? ********************************************
prompt
accept l_yes char prompt 'db backed up (y/n): '

-- drop the constraint that prevents multiple roles for a subject for a performance review, we should possibly prevent the same user evaluating as multiple roles, but then why?
alter table PERFORMANCE_EVALUATORS drop constraint PEREV_UK;
alter table PERFORMANCE_REVIEWS add APPRAISAL_TYPE VARCHAR2(100) DEFAULT 'PERFORMANCE';

update permits set url='/perfman/add.*review.htm' where url='/perfman/addperformancereview.htm';
update permits set url='/perfman/delete.*review.htm' where url='/perfman/deleteperformancereview.htm';
update permits set url='/perfman/view.*review.htm' where url='/perfman/.*performancereview.htm';

CREATE OR REPLACE VIEW SURVEY_LIST AS
select sub_spa.subject_id, cd.first_name, cd.second_name, l.username as manager_name, l.user_id as manager_id, qwp.que_wf_id
	from subject_primary_associations sub_spa,
	que_wf_participants qwp, positions p,
	subject_primary_associations boss_spa,
	subjects bosses,
	subjects subordinates,
	core_details cd,
	logins l
	where qwp.subject_id = sub_spa.subject_id
	and sub_spa.position_id = p.node_id
	and p.parent_id = boss_spa.position_id
    and sub_spa.value_id=402
	and sub_spa.subject_id = subordinates.node_id
	and subordinates.cd_id = cd.id
	and boss_spa.subject_id = bosses.node_id
	and bosses.user_id = l.user_id
WITH READ ONLY;

-- compile in the stored procedures
@packages/zynap_wf_integration_spec.sql
@packages/zynap_wf_integration_body.sql

ALTER PACKAGE wf_integration COMPILE SPECIFICATION;
ALTER PACKAGE wf_integration COMPILE BODY;

commit;
