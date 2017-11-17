package com.zynap.talentstudio.questionnaires;

import net.sf.hibernate.expression.Criterion;

/**
 *  Builds a query that add criteria to find all questionnaire workflows that the given user is involved in.
 *  We only load workflows where parent is null as this is just to build a chart specification.
 *  <p>
 *      We are looking to thus join table que_wf_participants with the given user_id and the root of any questionnaire
 *      that are involved in republishing.
 *  </p>
 */
public class RepublishableWorkflowSpecification implements QuestionnaireWorkflowSpecification {


	public RepublishableWorkflowSpecification(Long subjectId) {
		this.subjectId = subjectId;
	}

	@Override
	public Criterion toCriteria() {
		return null;
	}

	private Long subjectId;

}
