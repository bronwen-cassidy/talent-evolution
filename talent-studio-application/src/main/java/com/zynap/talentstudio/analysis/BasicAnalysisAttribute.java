package com.zynap.talentstudio.analysis;

import com.zynap.domain.ZynapDomainObject;

import org.springframework.util.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 01-Mar-2006
 * Time: 17:35:21
 *
 * Common base class for metrics, columns and population criteria.
 */
public class BasicAnalysisAttribute extends ZynapDomainObject {

    public BasicAnalysisAttribute() {
        super();
    }

    public BasicAnalysisAttribute(Long id) {
        super(id);
    }

    /**
     * Hibernate getter.
     * @return
     */
    public final String getAttributeName() {
        return attributeName;
    }

    /**
     * Hibernate setter
     * @param attributeName
     */
    public final void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public AnalysisParameter getAnalysisParameter() {
        return (attributeName != null) ? new AnalysisParameter(attributeName, questionnaireWorkflowId, role) : null;        
    }

    public final void setAnalysisParameter(AnalysisParameter attribute) {
        setAttributeName(attribute.getName());
        setQuestionnaireWorkflowId(attribute.getQuestionnaireWorkflowId());
        setRole(attribute.getRole());
    }

    public final Long getQuestionnaireWorkflowId() {
        return questionnaireWorkflowId;
    }

    public final void setQuestionnaireWorkflowId(Long questionnaireWorkflowId) {
        this.questionnaireWorkflowId = questionnaireWorkflowId;
    }

    public Long getDynamicAttributeId() {
        return Long.valueOf(getUnqualifiedAttribute());
    }

    public final String getRole() {
        return role;
    }

    public final void setRole(String role) {
        this.role = role;
    }

    public String getUnqualifiedAttribute() {
        return attributeName != null ? StringUtils.unqualify(attributeName) : attributeName;
    }

    protected String attributeName;

    protected Long questionnaireWorkflowId;

    protected String role;
}