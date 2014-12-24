/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.functions;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 21-Apr-2006 09:03:52
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.MixedCalculation;
import com.zynap.talentstudio.calculations.Expression;

import java.util.List;

public class TestFunctionWrapperBean extends TestCase {

    public void testFunctionWrapperBeanNoFormula() throws Exception {

        final FunctionWrapperBean functionWrapperBean = new FunctionWrapperBean(null);

        final List<OperandWrapperBean> operands = functionWrapperBean.getOperands();
        assertEquals(2, operands.size());

        for (int i = 0; i < operands.size(); i++) {
            OperandWrapperBean operandWrapperBean = operands.get(i);
            assertNull(operandWrapperBean.getLeftBracket());
            assertNull(operandWrapperBean.getOperator());
            assertNull(operandWrapperBean.getRightBracket());
            assertNull(operandWrapperBean.getAttribute());
            assertNull(operandWrapperBean.getMetricId());
            assertEquals("", operandWrapperBean.getAttributeLabel());
        }
    }

    public void testGetModifiedFormula() throws Exception {

        final FunctionWrapperBean functionWrapperBean = new FunctionWrapperBean(null);
        assertNotNull(functionWrapperBean.getModifiedCalculation());
    }

    public void testGetFormulaDisplayEmpty() throws Exception {

        FunctionWrapperBean functionWrapperBean = new FunctionWrapperBean(null);
        assertEquals("  ", functionWrapperBean.getFormulaDisplay());
    }

    public void testGetFormulaDisplay() throws Exception {

        Calculation calc = new MixedCalculation();
        calc.addExpression(new Expression("sourceDerivedAttributes[6]", "+"));
        calc.addExpression(new Expression("targetDerivedAttributes[7]", null));
        FunctionWrapperBean functionWrapperBean = new FunctionWrapperBean(calc);

        final OperandWrapperBean sourceOperand = functionWrapperBean.getOperands().get(0);
        final OperandWrapperBean targetOperand = functionWrapperBean.getOperands().get(1);

        final String sourceOperandLabel = "sourceOperandLabel";
        sourceOperand.setAttributeLabel(sourceOperandLabel);

        final String targetOperandLabel = "targetOperandLabel";
        targetOperand.setAttributeLabel(targetOperandLabel);

        final String displayFormula = functionWrapperBean.getFormulaDisplay();
        assertEquals(sourceOperandLabel + " " + sourceOperand.getOperator() + " " + targetOperandLabel + " ", displayFormula);
    }

    public void testGetOperands() throws Exception {

        FunctionWrapperBean functionWrapperBean = new FunctionWrapperBean(null);
        assertFalse(functionWrapperBean.getOperands().isEmpty());
    }

    public void testReset() throws Exception {

        Calculation calc = new MixedCalculation();
        calc.addExpression(new Expression("sourceDerivedAttributes[6]", "+"));
        calc.addExpression(new Expression("targetDerivedAttributes[7]", null));
        FunctionWrapperBean functionWrapperBean = new FunctionWrapperBean(calc);
        functionWrapperBean.reset();
        assertEquals(calc, functionWrapperBean.getModifiedCalculation());
    }
}