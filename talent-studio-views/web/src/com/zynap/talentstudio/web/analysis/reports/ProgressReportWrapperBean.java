/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.ProgressReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.ReportWorkflow;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeBranch;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Jun-2010 10:42:21
 */
public class ProgressReportWrapperBean implements Serializable {

    public ProgressReportWrapperBean(Report report) {
        this.report = report;
        this.populationId = report.getDefaultPopulation() != null ? report.getDefaultPopulation().getId() : null;
        initColumns();
    }

    private void initColumns() {
        ProgressReport progressReport = (ProgressReport) report;
        if (progressReport.getColumns().isEmpty()) {
            // add the default column that holds the column where the worflows get listed, it is the workflow column
            Column column = new Column("", ReportConstants.WORKFLOW_NAME, new Integer(0), DynamicAttribute.DA_TYPE_TEXTFIELD);
            workflowColumn = new ProgressColumnWrapperBean(column);
        } else {   // edit
            List<Column> columnList = progressReport.getColumns();
            workflowColumn = new ProgressColumnWrapperBean(columnList.get(0));
            for (int i = 1; i < columnList.size(); i++) {
                addColumn(columnList.get(i));
            }
        }
        for (ReportWorkflow reportWorkflow : progressReport.getWorkflows()) {
            workflows.add(new ReportWorkflowWrapper(reportWorkflow));
        }
    }

    public void addColumn(Column column) {
        column.setPosition(columns.size());
        columns.add(new ProgressColumnWrapperBean(column));
    }

    public Report getModifiedReport() {
        ProgressReport progressReport = (ProgressReport) report;
        progressReport.setDefaultPopulation(new Population(populationId));
        assignReportWorkflows(progressReport);
        assignColumns(progressReport);

        return progressReport;
    }

    private void assignColumns(ProgressReport progressReport) {
        progressReport.getColumns().clear();
        Collections.sort(columns, new Comparator<ProgressColumnWrapperBean>() {
            public int compare(ProgressColumnWrapperBean o1, ProgressColumnWrapperBean o2) {
                return o1.getPosition().compareTo(o2.getPosition());
            }
        });
        
        workflowColumn.setPosition(new Integer(0));
        columns.add(0, workflowColumn);

        for (ProgressColumnWrapperBean column : columns) {
            Column modifiedColumn = column.getModifiedColumn();
            // find the dynamic attribute
            String attribute = column.getAttributeName();
            if(StringUtils.isNumeric(attribute)) {
                DynamicAttribute target = findAttribute(new Long(attribute));
                if(target != null) {
                    modifiedColumn.setColumnType(target.getType());
                    modifiedColumn.setDynamicLineItem(target.isDynamic());
                }
            }
            progressReport.addColumn(modifiedColumn);
        }
    }

    private void assignReportWorkflows(ProgressReport progressReport) {
        progressReport.getWorkflows().clear();
        Collections.sort(workflows, new Comparator<ReportWorkflowWrapper>() {
            public int compare(ReportWorkflowWrapper o1, ReportWorkflowWrapper o2) {
                return o1.getPosition().compareTo(o2.getPosition());
            }
        });
        
        for (ReportWorkflowWrapper reportWorkflowWrapper : workflows) {
            progressReport.addReportWorkflow(reportWorkflowWrapper.getModifiedReportWorkflow());
        }
    }

    private DynamicAttribute findAttribute(Long dynamicAttributeId) {
        for (DynamicAttribute dynamicAttribute : dynamicAttributes) {
            if(dynamicAttribute.getId().equals(dynamicAttributeId)) {
                return dynamicAttribute;
            }
        }
        return null;
    }

    public Long getQuestionnaireDefinitionId() {
        return ((ProgressReport) report).getQuestionnaireDefinitionId();
    }

    public void setQuestionnaireDefinitionId(Long questionnaireDefinitionId) {
        ((ProgressReport) report).setQuestionnaireDefinitionId(questionnaireDefinitionId);
    }

    public ProgressColumnWrapperBean getWorkflowColumn() {
        return workflowColumn;
    }

    public void setWorkflowColumn(ProgressColumnWrapperBean workflowColumn) {
        this.workflowColumn = workflowColumn;
    }

    public void setTree(List<AnalysisAttributeBranch> branches) {
        this.tree = branches;
    }

    public List<AnalysisAttributeBranch> getTree() {
        return tree;
    }

    public void setQuestionnaireWorkflows(Set<QuestionnaireWorkflow> questionnaireWorkflows) {
        this.questionnaireWorkflows = questionnaireWorkflows;
        questionnaireWorkflows.size();
    }

    public Set<QuestionnaireWorkflow> getQuestionnaireWorkflows() {
        return questionnaireWorkflows;
    }

    public List<ReportWorkflowWrapper> getWorkflows() {
        return workflows;
    }

    public void addWorkflow() {
        ReportWorkflow workflow = new ReportWorkflow();
        workflow.setPosition(workflows.size());
        workflows.add(new ReportWorkflowWrapper(workflow));
    }

    public void removeWorkflow(int index) {
        workflows.remove(index);    
    }

    public boolean isAdding() {
        return report.getId() == null;
    }

    public void setQuestionnaireDefinition(QuestionnaireDefinition questionnaireDefinition) {
        this.questionnaireDefinition = questionnaireDefinition;
        questionnaireDefinition.getDynamicAttributes().size();
        this.dynamicAttributes = questionnaireDefinition.getDynamicAttributes();
    }

    public QuestionnaireDefinition getQuestionnaireDefinition() {
        return questionnaireDefinition;
    }

    public void clearCollections() {
        workflows = new ArrayList<ReportWorkflowWrapper>();
        questionnaireWorkflows = new HashSet<QuestionnaireWorkflow>();
        tree = null;
    }

    public List<ProgressColumnWrapperBean> getColumns() {
        return columns;
    }

    public void removeColumn(int index) {
        columns.remove(index);
    }

    public Report getReport() {
        return report;
    }

    public void setLabel(String label) {
        report.setLabel(label);
    }

    public String getLabel() {
        return report.getLabel();
    }

    public void setDescription(String description) {
        report.setDescription(description); 
    }

    public String getDescription() {
        return report.getDescription();
    }

    public Long getPopulationId() {
        return populationId;
    }

    public void setPopulationId(Long populationId) {
        this.populationId = populationId;
    }

    public void setLastLineItem(boolean lastLineItem) {
        report.setLastLineItem(lastLineItem);
    }

    public boolean isLastLineItem() {
        return report.isLastLineItem();
    }

    private ProgressColumnWrapperBean workflowColumn;
    private List<AnalysisAttributeBranch> tree;
    private Set<QuestionnaireWorkflow> questionnaireWorkflows = new HashSet<QuestionnaireWorkflow>();
    private List<ReportWorkflowWrapper> workflows = new ArrayList<ReportWorkflowWrapper>();
    private List<ProgressColumnWrapperBean> columns = new ArrayList<ProgressColumnWrapperBean>();
    private QuestionnaireDefinition questionnaireDefinition;
    private Report report;
    private List<DynamicAttribute> dynamicAttributes;
    private Long populationId;
}
