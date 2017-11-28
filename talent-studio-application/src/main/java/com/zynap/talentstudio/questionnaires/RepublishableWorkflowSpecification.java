package com.zynap.talentstudio.questionnaires;

import com.zynap.talentstudio.common.Specification;

/**
 *  Builds a query that add criteria to find all questionnaire workflows that the given user is involved in.
 *  We only load workflows where parent is null as this is just to build a chart specification.
 *  <p>
 *      We are looking to thus join table que_wf_participants with the given user_id and the root of any questionnaire
 *      that are involved in republishing.
 *  </p>
 */
public class RepublishableWorkflowSpecification implements Specification {


	public RepublishableWorkflowSpecification(Long subjectId) {
		this.subjectId = subjectId;
	}

	@Override
	public String toCriteria() {
		return "participant.subject.id = " + subjectId + " and workflow.parentLabel = null";
	}

	@Override
	public String select() {
		return "new " + QuestionnaireWorkflowDTO.class.getName() + "(workflow.id, workflow.label)";
	}

	public String from () {
		return "QuestionnaireWorkflow workflow, WorkflowParticipant participant";
	}
	
	public String[] prefix () {
		return new String[] {"workflow", "participant"};
	}

	private Long subjectId;
}
