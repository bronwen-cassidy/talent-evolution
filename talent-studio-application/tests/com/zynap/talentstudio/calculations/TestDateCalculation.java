/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */

package com.zynap.talentstudio.calculations;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 25-Jul-2007 14:24:15
 * @version 0.1
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.DateCalculation;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.calculations.CalculationResult;

public class TestDateCalculation extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        dateCalculation = new DateCalculation();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testExecute() throws Exception {

        Subject node = new Subject();
        dateCalculation.setFormat(DateCalculation.YEARS_MONTHS);

        DynamicAttribute currentDateAttr = new DynamicAttribute();
        currentDateAttr.setArtefactType(DynamicAttribute.NODE_TYPE_FUNCTION);
        currentDateAttr.setExternalRefLabel(DynamicAttribute.CURRENT_DATE);

        Expression exp0 = new Expression();
        exp0.setAttribute(currentDateAttr);
        exp0.setOperator(Expression.PLUS);
        exp0.setIndex(0);
        dateCalculation.addExpression(exp0);

        Expression exp1 = new Expression();
        exp1.setValue("2");
        exp1.setFormat(DateCalculation.YEARS);
        exp1.setOperator(Expression.MINUS);
        exp1.setIndex(1);
        dateCalculation.addExpression(exp1);

        Expression exp2 = new Expression();
        exp2.setValue("3");
        exp2.setFormat(DateCalculation.MONTHS);
        exp2.setIndex(2);
        exp2.setOperator(Expression.MINUS);
        dateCalculation.addExpression(exp2);

        CalculationResult result = dateCalculation.execute(node);
        assertEquals(result.getType(), DynamicAttribute.DA_TYPE_DATE);

        Expression exp3 = new Expression();
        exp3.setAttribute(currentDateAttr);
        exp3.setIndex(3);
        dateCalculation.addExpression(exp3);

        CalculationResult result1 = dateCalculation.execute(node);
        assertEquals(result1.getType(), DynamicAttribute.DA_TYPE_TEXTFIELD);
        assertEquals("1.9", result1.getValue());
    }

    public void testExecute_CDMinusCD() throws Exception {
        
        Subject node = new Subject();
        DynamicAttribute currentDateAttr = new DynamicAttribute();
        currentDateAttr.setArtefactType(DynamicAttribute.NODE_TYPE_FUNCTION);
        currentDateAttr.setExternalRefLabel(DynamicAttribute.CURRENT_DATE);

        Expression exp0 = new Expression();
        exp0.setAttribute(currentDateAttr);
        exp0.setOperator(Expression.MINUS);
        exp0.setIndex(0);
        dateCalculation.addExpression(exp0);

        Expression exp1 = new Expression();
        exp1.setAttribute(currentDateAttr);
        exp1.setIndex(1);
        dateCalculation.addExpression(exp1);

        CalculationResult result1 = dateCalculation.execute(node);
        assertEquals(result1.getType(), DynamicAttribute.DA_TYPE_TEXTFIELD);
        assertEquals("0", result1.getValue());
        
    }

    public void testExecute_CDMinusCD_PlusYear() throws Exception {

        Subject node = new Subject();
        DynamicAttribute currentDateAttr = new DynamicAttribute();
        currentDateAttr.setArtefactType(DynamicAttribute.NODE_TYPE_FUNCTION);
        currentDateAttr.setExternalRefLabel(DynamicAttribute.CURRENT_DATE);

        Expression exp0 = new Expression();
        exp0.setAttribute(currentDateAttr);
        exp0.setOperator(Expression.MINUS);
        exp0.setIndex(0);
        dateCalculation.addExpression(exp0);

        Expression exp1 = new Expression();
        exp1.setAttribute(currentDateAttr);
        exp1.setIndex(1);
        exp1.setOperator(Expression.PLUS);
        dateCalculation.addExpression(exp1);

        Expression exp2 = new Expression();
        exp2.setValue("2");
        exp2.setIndex(2);
        exp2.setFormat(DateCalculation.YEARS);
        dateCalculation.addExpression(exp2);

        CalculationResult result1 = dateCalculation.execute(node);
        assertEquals(DynamicAttribute.DA_TYPE_TEXTFIELD, result1.getType());
        assertEquals("2", result1.getValue());
    }

    private DateCalculation dateCalculation;
}