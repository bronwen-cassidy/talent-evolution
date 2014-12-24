/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.calculations;

import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.io.Serializable;

/**
 * Class represents a mathematical expression used in calculations to be applied to calculated fields
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Jul-2007 10:34:11
 */
public class Expression extends BasicAnalysisAttribute implements Serializable {

    public Expression() {
    }

    public Expression(Metric metric, String operator) {
        this.metric = metric;
        this.operator = operator;
        this.attributeName = metric.getId().toString();
    }

    public Expression(DynamicAttribute attribute, String operator) {
        this.attribute = attribute;
        this.operator = operator;
        this.attributeName = attribute.getId().toString();
    }

    public Expression(String refValue, String operator) {
        this.refValue = refValue;
        this.operator = operator;
        this.attributeName = refValue;
    }

    public DynamicAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(DynamicAttribute attribute) {
        this.attribute = attribute;
        if(attribute != null && attribute.getId() != null) {
            this.attributeName = attribute.getId().toString();
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        if(value != null) this.attributeName = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getLeftBracket() {
        return leftBracket;
    }

    public void setLeftBracket(String leftBracket) {
        this.leftBracket = leftBracket;
    }

    public String getRefValue() {
        return refValue;
    }

    public void setRefValue(String refValue) {
        this.refValue = refValue;
        if(refValue != null) this.attributeName = refValue;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    /**
     * The attribute operand has been set when one needs to find the answer in the nodeAttributes of a node (the dynamicAttribute answer)
     *
     * @return true if the attribute is not null false otherwise
     */
    public boolean isAttributeOperand() {
        return attribute != null;
    }

    public boolean isValueOperand() {
        return value != null;
    }

    /**
     * RefValue is used when one needs to use reflection on the node to determine the answer
     *
     * @return true if the refValue has been set, false otherwise
     */
    public boolean isRefValueOperand() {
        return refValue != null;
    }

    public boolean isCurrentDate() {
        return isAttributeOperand() && attribute.isCurrentDate();
    }

    public boolean isMetricExpression() {
        return metric != null;
    }

    public boolean isQuestionnaireAttribute() {
        return questionnaireWorkflowId != null;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        if(leftBracket != null) result.append(leftBracket).append(" ");
        if(isAttributeOperand()) result.append("Attribute: ").append(attribute.getId());
        if(isQuestionnaireAttribute()) result.append(QUE_SEPARATOR).append(questionnaireWorkflowId);
        if(isRefValueOperand()) result.append("Ref Value: ").append(refValue);
        if(isValueOperand()) result.append("Value: ").append(value);
        if(isMetricExpression()) result.append("Metric: ").append(metric.getLabel());
        if(rightBracket != null) result.append(" ").append(rightBracket);
        if(operator != null) result.append(" ").append(operator);
        return result.toString();
    }

    public String getFormatDisplayValue() {
        String result = "";

        switch (format) {
            case DateCalculation.YEARS:
                result = "years";
                break;
            case DateCalculation.MONTHS:
                result = "months";
                break;
            case DateCalculation.DAYS:
                result = "days";
                break;
        }
        return result;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
        if(metric != null) this.attributeName = String.valueOf(metric.getId());
    }

    public Metric getMetric() {
        return metric;
    }

    public void setRightBracket(String rightBracket) {
        this.rightBracket = rightBracket;
    }

    public String getRightBracket() {
        return rightBracket;
    }

    public String getAttributeExpression() {

        StringBuffer result = new StringBuffer();
        if(isAttributeOperand()) {
            result.append(attribute.getId());
        }
        if(questionnaireWorkflowId != null) {
            result.append(QUE_SEPARATOR).append(questionnaireWorkflowId);
        }
        if(role != null) {
            result.append(QUE_SEPARATOR).append(role);
        }
        return result.toString();
    }

    public Long getMetricId() {
        return metric != null ? metric.getId() : null;
    }

    private DynamicAttribute attribute;
    private String value;
    /* the maths symbol to apply '+', '-', '/', or '*' */
    private String operator;
    /* either open or close bracket */
    private String leftBracket;
    private String rightBracket;

    /**
     * This represents either the id of a lookup_value or a coreAttribute reflection string (dateOfBirth an example of the latter)
     */
    private String refValue;
    /* will be different for different attributes dates will have years, months or days */
    private int format;
    private int index;

    public static final String MINUS = "-";
    public static final String PLUS = "+";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";
    private Calculation calculation;

    private Metric metric;
    private static final String QUE_SEPARATOR = AnalysisAttributeHelper.QUESTION_CRITERIA_DELIMITER;
}
