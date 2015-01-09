/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Apr-2010 11:15:00
 */
public class ChartReportAttribute extends BasicAnalysisAttribute implements Serializable {

    public ChartReportAttribute() {
    }

    public ChartReportAttribute(Long dynamicAttributeId, Long questionnaireWorkflowId) {
        this.dynamicAttributeId = dynamicAttributeId;
        this.questionnaireWorkflowId = questionnaireWorkflowId;
    }

    public ChartReportAttribute(String label, Long dynamicAttributeId, Long questionnaireWorkflowId) {
        this(dynamicAttributeId, questionnaireWorkflowId);
        setLabel(label);
    }

    public Long getDynamicAttributeId() {
        return dynamicAttributeId;
    }

    public void setDynamicAttributeId(Long dynamicAttributeId) {
        this.dynamicAttributeId = dynamicAttributeId;         
        setAttributeName(String.valueOf(dynamicAttributeId));
    }

    public DynamicAttribute getDynamicAttribute() {
        return dynamicAttribute;
    }

    public void setDynamicAttribute(DynamicAttribute dynamicAttribute) {
        this.dynamicAttribute = dynamicAttribute;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    private DynamicAttribute dynamicAttribute;
    private Report report;
    private Long dynamicAttributeId;
    private Integer position;
}
