package com.zynap.talentstudio.analysis.metrics;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.common.AccessType;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author Hibernate CodeGenerator
 */
public class Metric extends BasicAnalysisAttribute {

    /**
     * default constructor
     */
    public Metric() {
    }

    public Metric(Long id) {
        super(id);
    }

    public Metric(String label, String operator, String attribute) {
        this(label, operator, attribute, null);
    }

    public Metric(String label, String operator, String attribute, Long questionnaireWorkflowId) {
        this.label = label;
        this.operator = operator;
        this.attributeName = attribute;
        this.questionnaireWorkflowId = questionnaireWorkflowId;
    }

    /**
     * full constructor
     */
    public Metric(Long id, String label, String description, String accessType, String operator, String attribute) {
        super(id);
        this.label = label;
        this.description = description;
        this.accessType = accessType;
        this.operator = operator;
        this.attributeName = attribute;
    }

    public String getAccessType() {
        return this.accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtefactType() {
        return this.artefactType;
    }

    public void setArtefactType(String artefactType) {
        this.artefactType = artefactType;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("accessType", getAccessType())
                .append("description", getDescription())
                .append("label", getLabel())
                .append("artefactType", getArtefactType())
                .append("operator", getOperator())
                .append("attributeName", getAttributeName())
                .toString();
    }

    public boolean isPublic() {
        return AccessType.PUBLIC_ACCESS.toString().equals(accessType);
    }

    public boolean isPrivate() {
        return AccessType.PRIVATE_ACCESS.toString().equals(accessType);
    }

    public boolean isCount() {
        return IPopulationEngine.COUNT.equals(operator);
    }

    public boolean isAverage() {
        return IPopulationEngine.AVG.equals(operator);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * nullable persistent field
     */
    private String accessType;

    /**
     * nullable persistent field
     */
    private String description;

    /**
     * nullable persistent field
     */
    private String artefactType;

    /**
     * nullable persistent field
     */
    private String operator;

    private Long userId;
    private String value;
}
