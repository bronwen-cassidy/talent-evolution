/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.ReportWorkflow;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 30-Jun-2010 11:26:04
 */
public class ReportWorkflowWrapper implements Serializable {

    public ReportWorkflowWrapper(ReportWorkflow reportWorkflow) {
        this.reportWorkflow = reportWorkflow;
        this.questionnaireWorkflowId = reportWorkflow.getWorkflow() != null ? reportWorkflow.getWorkflow().getId() : null;
    }

    public ReportWorkflow getModifiedReportWorkflow() {
        reportWorkflow.setWorkflow(new QuestionnaireWorkflow(questionnaireWorkflowId));
        return reportWorkflow;
    }

    public ReportWorkflow getReportWorkflow() {
        return reportWorkflow;
    }

    public void setReportWorkflow(ReportWorkflow reportWorkflow) {
        this.reportWorkflow = reportWorkflow;
    }

    public Long getQuestionnaireWorkflowId() {
        return questionnaireWorkflowId;
    }

    public void setQuestionnaireWorkflowId(Long questionnaireWorkflowId) {
        this.questionnaireWorkflowId = questionnaireWorkflowId;
    }

    public void setLabel(String label) {
        reportWorkflow.setLabel(label);
    }

    public String getLabel() {
        return reportWorkflow.getLabel();
    }

    public void setPosition(Integer position) {
        reportWorkflow.setPosition(position);
    }

    public Integer getPosition() {
        return reportWorkflow.getPosition();
    }

    private ReportWorkflow reportWorkflow;
    private Long questionnaireWorkflowId;
}
