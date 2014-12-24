/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.AppraisalSummaryReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeBranch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Sep-2009 15:17:21
 */
public class AppraisalReportWrapperBean implements Serializable {

    public AppraisalReportWrapperBean(AppraisalSummaryReport report) {
        this.report = report;
        this.report.setReportType(Report.APPRAISAL_REPORT);
        this.report.setOperator(IPopulationEngine.AVG);
        this.appraisalId = report.getAppraisalId();
        assignColumns(report);
    }

    private void assignColumns(Report report) {
        for (Column column : report.getColumns()) {
            columns.add(column);
        }
    }

    public Long getAppraisalId() {
        return appraisalId;
    }

    public void setAppraisalId(Long appraisalId) {
        this.appraisalId = appraisalId;
        assignSelectedReview();
    }

    private void assignSelectedReview() {
        for (PerformanceReview appraisal : appraisals) {
            if (appraisal.getId().equals(appraisalId)) {
                selectedReview = appraisal;
                break;
            }
        }
    }

    public Report getModifiedReport() {
        report.setAppraisalId(appraisalId);
        report.setAccessType(AccessType.PUBLIC_ACCESS.toString());
        report.setPopulationType(Node.SUBJECT_UNIT_TYPE_);
        report.getColumns().clear();
        report.setMenuItems(null);
        int index = 0;
        for (Column column : columns) {
            Long dynamicAttributeId = null;
            try {
                dynamicAttributeId = column.getDynamicAttributeId();
            } catch (Exception e) {
            }
            if (dynamicAttributeId != null) {
                column.setColumnType(DynamicAttribute.DA_TYPE_NUMBER);
                column.setPosition(new Integer(index));
                report.addColumn(column);
                index++;
            }
        }
        return report;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void removeColumn(int index) {
        columns.remove(index);
    }

    public void setLabel(String value) {
        report.setLabel(value);
    }

    public String getLabel() {
        return report.getLabel();
    }

    public void setDescription(String value) {
        report.setDescription(value);
    }

    public String getDescription() {
        return report.getDescription();
    }

    public void setOperator(String value) {
        report.setOperator(value);
    }

    public String getOperator() {
        return report.getOperator();
    }

    public Long getReportId() {
        return report.getId();
    }

    public void setAppraisals(List<PerformanceReview> appraisals) {
        this.appraisals = appraisals;
    }

    public List<PerformanceReview> getAppraisals() {
        return appraisals;
    }

    public PerformanceReview getSelectedReview() {
        return selectedReview;
    }

    public void setSelectedReview(PerformanceReview selectedReview) {
        this.selectedReview = selectedReview;
    }

    public void setTree(List<AnalysisAttributeBranch> tree) {
        this.tree = tree;
    }

    public List<AnalysisAttributeBranch> getTree() {
        return tree;
    }

    private Long appraisalId;
    private AppraisalSummaryReport report;
    private List<Column> columns = new ArrayList<Column>();
    private List<DynamicAttribute> attributes = new ArrayList<DynamicAttribute>();
    private List<PerformanceReview> appraisals = new ArrayList<PerformanceReview>();
    private PerformanceReview selectedReview;
    private List<AnalysisAttributeBranch> tree;
}
