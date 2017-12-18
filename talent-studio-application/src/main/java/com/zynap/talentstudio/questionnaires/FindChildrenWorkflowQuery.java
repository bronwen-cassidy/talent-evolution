package com.zynap.talentstudio.questionnaires;

import com.zynap.talentstudio.common.QuerySpecification;

/**
 *
 */
public class FindChildrenWorkflowQuery implements QuerySpecification {

	public FindChildrenWorkflowQuery(Long parentWorkflowId) {
		this.parentWorkflowId = parentWorkflowId;	
	}

	@Override
	public String where() {
		return "workflow.parentId = " + parentWorkflowId;
	}

	@Override
	public String from() {
		return "QuestionnaireWorkflow workflow";
	}

	@Override
	public String select() {
		return "workflow";
	}

	@Override
	public String orderBy() {
		return "workflow.createdDate";
	}

	private Long parentWorkflowId;
}
