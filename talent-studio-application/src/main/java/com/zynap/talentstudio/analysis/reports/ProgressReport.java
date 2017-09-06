/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Apr-2010 21:12:38
 */
public class ProgressReport extends Report {

    public ProgressReport() {
        setReportType(Report.PROGRESS_REPORT);
    }

    public ProgressReport(Long id) {
        super(id);
    }

    public ProgressReport(String label, String description, String access) {
        super(label, description, access);
    }

    public Set<ReportWorkflow> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(Set<ReportWorkflow> workflows) {
        this.workflows = workflows;
    }

    public List<AnalysisParameter> getQuestionnaireAttributes() {

        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();
        List<Column> columns = getColumns();
        if (columns != null && !columns.isEmpty()) {

            List<Column> subColumns = columns.subList(1, columns.size());
            for (Column column : subColumns) {
                for (ReportWorkflow workflow : workflows) {
                    Long workflowId = workflow.getWorkflow().getId();
                    String role = workflow.getRole();
                    AnalysisParameter analysisParameter = new AnalysisParameter(column.getAttributeName(), workflowId, role);
                    attributes.add(analysisParameter);
                }
            }
        }

        return attributes;
    }

    public Long getQuestionnaireDefinitionId() {
        return questionnaireDefinitionId;
    }

    public void setQuestionnaireDefinitionId(Long questionnaireDefinitionId) {
        this.questionnaireDefinitionId = questionnaireDefinitionId;
    }

    public QuestionnaireDefinition getQuestionnaireDefinition() {
        return questionnaireDefinition;
    }

    public void setQuestionnaireDefinition(QuestionnaireDefinition questionnaireDefinition) {
        this.questionnaireDefinition = questionnaireDefinition;
    }

    public boolean isProgressReport() {
        return true;
    }

    public void addReportWorkflow(ReportWorkflow reportWorkflow) {
        reportWorkflow.setReport(this);
        reportWorkflow.setPosition(workflows.size());
        workflows.add(reportWorkflow);
    }

	void resetWorkflowPositions() {
    	int index = 0;
		for (ReportWorkflow workflow : workflows) {
			workflow.setPosition(index++);	
		}
	}

	private Set<ReportWorkflow> workflows = new LinkedHashSet<ReportWorkflow>();
    private QuestionnaireDefinition questionnaireDefinition;
    private Long questionnaireDefinitionId;
}