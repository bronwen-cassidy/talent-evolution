/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.functions;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 22-May-2009 12:10:34
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

public class TestOperandWrapperBean extends TestCase {

    public void testGetModifiedExpressionDerivedAttribute() throws Exception {
        
        operandWrapperBean = new OperandWrapperBean(new Expression());
        final String expected = "sourceDerivedAttributes[223]";
        operandWrapperBean.setAttribute(expected);

        AttributeWrapperBean attributeWrapperBean = builldAttributeWrapperBean(expected, "PPA", "Position succeeds");
        operandWrapperBean.setAttributeDefinition(attributeWrapperBean);

        final Expression expression = operandWrapperBean.getModifiedExpression();
        assertEquals(expected, expression.getRefValue());
        assertNull(expression.getAttribute());
        assertNull(expression.getMetric());
    }

    public void testGetModifiedExpressionDynamicAttribute() throws Exception {
        operandWrapperBean = new OperandWrapperBean(new Expression());
        final String expected = "123444";
        operandWrapperBean.setAttribute(expected);
        AttributeWrapperBean attributeWrapperBean = builldAttributeWrapperBean(expected, "S", "Person ID");
        operandWrapperBean.setAttributeDefinition(attributeWrapperBean);

        final Expression expression = operandWrapperBean.getModifiedExpression();
        assertEquals(expected, expression.getAttribute().getId().toString());
        assertNull(expression.getRefValue());
        assertNull(expression.getValue());
        assertNull(expression.getMetric());
    }

    public void testGetModifiedExpressionQuestionAttribute() throws Exception {
        operandWrapperBean = new OperandWrapperBean(new Expression());
        final String expected = "123444_123";
        operandWrapperBean.setAttribute(expected);
        AttributeWrapperBean attributeWrapperBean = builldAttributeWrapperBean(expected, "S", "Person Q Attr");
        operandWrapperBean.setAttributeDefinition(attributeWrapperBean);

        final Expression expression = operandWrapperBean.getModifiedExpression();
        assertEquals("123444", expression.getAttribute().getId().toString());
        assertEquals(new Long(123), expression.getQuestionnaireWorkflowId());
        assertNull(expression.getRefValue());
        assertNull(expression.getValue());
        assertNull(expression.getMetric());
    }

    public void testGetModifiedExpressionConstant() throws Exception {
        operandWrapperBean = new OperandWrapperBean(new Expression());
        final String expected = "23";
        operandWrapperBean.setValue(expected);
        AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean("", "", IDynamicAttributeService.INVALID_ATT);
        operandWrapperBean.setAttributeDefinition(attributeWrapperBean);

        final Expression expression = operandWrapperBean.getModifiedExpression();
        assertEquals("23", expression.getValue());
        assertNull(expression.getAttribute());
        assertNull(expression.getRefValue());
        assertNull(expression.getQuestionnaireWorkflowId());
        assertNull(expression.getMetric());
    }

    private AttributeWrapperBean builldAttributeWrapperBean(String expected, String type, String label) {
        DynamicAttribute dynamicAttribute = new DynamicAttribute(new Long(23), label, type);
        return new AttributeWrapperBean(expected, expected, dynamicAttribute);
    }

    private OperandWrapperBean operandWrapperBean;
}