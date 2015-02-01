/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.calculations.Expression;

import org.apache.commons.lang.math.NumberUtils;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 11-Jul-2007 16:25:21
 */
public class ExpressionWrapper implements Serializable {

    public ExpressionWrapper() {
        this.expression = new Expression();
    }

    public ExpressionWrapper(Expression expression) {
        this.expression = expression;
        this.value = expression.getValue();

        String refValue = expression.getRefValue();
        if (StringUtils.hasText(refValue) && refValue.equals(AnalysisAttributeHelper.DOB_ATTR)) {
            this.attribute = DynamicAttribute.DATE_OF_BITH_ATTR.getId().toString();
            this.label = DynamicAttribute.DATE_OF_BITH_ATTR.getLabel();

        } else if (expression.getAttribute() != null) {
            DynamicAttribute attribute1 = expression.getAttribute();
            this.attribute = attribute1.getId().toString();
            this.label = attribute1.getLabel();

        } else {
            this.label = expression.getValue() + " " + expression.getFormatDisplayValue();
        }
    }

    /**
     * @return new Created or updated Expression business object
     */
    public Expression getModifiedExpression() {

        if (attribute != null && StringUtils.hasText(attribute)) {
            // check if it is a ref value
            Long attributeId = null;
            try {
                attributeId = new Long(attribute);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (DynamicAttribute.DATE_OF_BITH_ATTR.getId().equals(attributeId)) {
                expression.setRefValue(AnalysisAttributeHelper.DOB_ATTR);
                expression.setAttribute(null);
            } else if (attributeId != null) {
                expression.setAttribute(new DynamicAttribute(attributeId));
                expression.setRefValue(null);
            }
            expression.setValue(null);

        } else {            
            expression.setValue(this.value);
            expression.setRefValue(null);
            expression.setAttribute(null);
        }
        return expression;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (StringUtils.hasText(value)) {
            this.value = value;
            this.attribute = null;
        }
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        // attribute must be a number
        if(!NumberUtils.isNumber(attribute)) attribute = null;
        this.attribute = attribute;
        if(StringUtils.hasText(attribute)) {
            this.value = null;
            setFormat("0");
        }
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
        return attribute != null || StringUtils.hasText(expression.getValue());
    }

    public String getLabel() {
        return label;
    }

    private Expression expression;
    private String attribute;
    private String label;
    private String value;
}
