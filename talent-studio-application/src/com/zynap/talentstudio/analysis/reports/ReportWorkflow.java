/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Jun-2010 09:14:50
 */
public class ReportWorkflow extends ZynapDomainObject {

    public ReportWorkflow() {
    }

    public QuestionnaireWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(QuestionnaireWorkflow workflow) {
        this.workflow = workflow;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Report getReport() {
        return report;
    }

    private QuestionnaireWorkflow workflow;
    private Integer position;
    private String role;
    private Report report;
}
