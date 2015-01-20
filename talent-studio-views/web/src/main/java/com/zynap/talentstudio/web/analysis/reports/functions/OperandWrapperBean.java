package com.zynap.talentstudio.web.analysis.reports.functions;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import org.springframework.util.StringUtils;


/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 01-Mar-2005
 * Time: 09:59:55
 */
public class OperandWrapperBean extends AnalysisAttributeWrapperBean {


    public OperandWrapperBean() {
        this.expression = new Expression();
    }

    public OperandWrapperBean(Expression expression) {

        this.expression = expression;
        this.value = expression.getValue();
        this.metric = expression.getMetric();
        this.metricId = metric != null ? metric.getId() : null;
        this.refValue = expression.getRefValue();
        this.questionnaireWorkflowId = expression.getQuestionnaireWorkflowId();
        this.role = expression.getRole();

        if (expression.getAttribute() != null) {
            this.dynamicAttribute = expression.getAttribute();
            this.attributeId = dynamicAttribute.getId().toString();
            this.label = dynamicAttribute.getLabel();
            this.attribute = expression.getAttributeExpression();

        } else if (refValue != null) {
            this.attribute = refValue;

        } else if (metricId != null) {
            this.attribute = metricId.toString();
        }

        if (metric != null) this.label = metric.getLabel();
    }

    /**
     * @return new Created or updated Expression business object
     */
    public Expression getModifiedExpression() {
        reset();

        if (this.metricId != null) {
            expression.setMetric(new Metric(metricId));

        } else if (StringUtils.hasText(value)) {
            expression.setValue(this.value);

        } else {
            AnalysisParameter analysisParameter = AnalysisAttributeHelper.getAttributeFromName(attribute);
            if (analysisParameter.isDynamicAttribute()) {
                expression.setAttribute(new DynamicAttribute(Long.valueOf(analysisParameter.getName())));
                expression.setRole(analysisParameter.getRole());
                expression.setQuestionnaireWorkflowId(analysisParameter.getQuestionnaireWorkflowId());
            } else {
                expression.setRefValue(refValue);
            }
        }
        return expression;
    }

    private void reset() {
        expression.setAttribute(null);
        expression.setQuestionnaireWorkflowId(null);
        expression.setRole(null);
        expression.setMetric(null);
        expression.setValue(null);
        expression.setRefValue(null);
    }

    public String getLeftBracket() {
        return expression.getLeftBracket();
    }

    public void setLeftBracket(String value) {
        expression.setLeftBracket(value);
    }

    public String getRightBracket() {
        return expression.getRightBracket();
    }

    public void setRightBracket(String value) {
        expression.setRightBracket(value);
    }

    public void setMetricId(Long metricId) {
        this.metricId = metricId;
        if (metricId != null) {
            this.attribute = metricId.toString();
            this.value = null;
            this.attributeId = null;
            this.refValue = null;
        }
    }

    public Long getMetricId() {
        return metricId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {

        if (StringUtils.hasText(value)) {
            this.value = value;
            this.label = value;
            this.attributeId = null;
            this.metricId = null;
            this.refValue = null;
        }
    }

    /**
     * The only AttributeWrapperBean that will ever be passed into this class are those that wrap {@link com.zynap.talentstudio.organisation.attributes.DynamicAttribute}.
     *
     * @param attributeDefinition - the object wrapping the dynamicAttribute.
     */
    public void setAttributeDefinition(AttributeWrapperBean attributeDefinition) {
        super.setAttributeDefinition(attributeDefinition);
        this.label = attributeDefinition.getLabel();
        final DynamicAttribute attr = attributeDefinition.getAttributeDefinition();

        if (!IDynamicAttributeService.INVALID_ATT.equals(attr) && attr.getId() != null) {
            this.dynamicAttribute = attr;
            this.label = attr.getLabel();
        }
    }

    public String getAttribute() {
        return attribute;
    }

    /**
     * Spring binding method
     *
     * @param attribute
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
        if (StringUtils.hasText(attribute)) {
            AnalysisParameter analysisParameter = AnalysisAttributeHelper.getAttributeFromName(attribute);

            this.value = null;
            this.metricId = null;

            if (analysisParameter.isDerivedAttribute()) {
                this.refValue = analysisParameter.getName();
            } else {
                this.attributeId = analysisParameter.getName();
                this.questionnaireWorkflowId = analysisParameter.getQuestionnaireWorkflowId();
                this.role = analysisParameter.getRole();
            }
        }
    }

    public BasicAnalysisAttribute getAnalysisAttribute() {
        return getModifiedExpression();
    }

    public String getFormat() {
        return String.valueOf(expression.getFormat());
    }

    public void setFormat(String format) {
        expression.setFormat(Integer.parseInt(format));
    }

    public String getOperator() {
        return expression.getOperator();
    }

    public void setOperator(String operator) {
        expression.setOperator(operator);
    }

    public boolean isExpressionValid() {
        return attributeId != null || StringUtils.hasText(value) || metricId != null || StringUtils.hasText(refValue);
    }

    public String getDisplayValue() {
        StringBuffer result = new StringBuffer();
        if (getLeftBracket() != null) result.append(getLeftBracket()).append(" ");
        result.append(getAttributeLabel());
        if (getRightBracket() != null) result.append(" ").append(getRightBracket());
        if (getOperator() != null) result.append(" ").append(getOperator());
        return result.toString();
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
        this.label = metric.getLabel();
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public String getRefValue() {
        return refValue;
    }

    public void setRefValue(String refValue) {
        this.refValue = refValue;
    }

    public DynamicAttribute getDynamicAttribute() {
        return dynamicAttribute;
    }

    public void setDynamicAttribute(DynamicAttribute dynamicAttribute) {
        this.dynamicAttribute = dynamicAttribute;
    }

    public Long getQuestionnaireWorkflowId() {
        return questionnaireWorkflowId;
    }

    public String getRole() {
        return role;
    }

    private Expression expression;
    private String attributeId;
    private String value;
    private String refValue;
    private Long metricId;
    private DynamicAttribute dynamicAttribute;
    private Metric metric;
    private Long questionnaireWorkflowId;
    private String role;
    private String attribute;
}
