/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.calculations;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.util.FormatterFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Jul-2007 10:42:09
 */
public class DateCalculation extends Calculation {

    public DateCalculation() {
    }

    public DateCalculation(String type) {
        super(type);
    }

    /**
     * The methods loops an ordered list of expressions applying the operator to each.
     * precondition: the expressions have been ordered so calculation can proceed from left to right or first to last.
     * So any brackets are moved to the beginning of the list.
     *
     * @param node the Node that contains the answers to an attribute questions
     * @return the result of the calculation
     */
    public CalculationResult execute(Node node) {

        CalculationFactory factory = CalculationFactory.getInstance();
        Expression first = null;
        Object value1 = null;
        for (Expression expression : expressions) {
            if (first == null) {
                first = expression;
                value1 = getValue(first, node);
            } else {
                Object value2 = getValue(expression, node);
                String operator = first.getOperator();
                Expression formatExpression = expression.getFormat() == NO_FORMAT ? first : expression;
                value1 = factory.evaluateDate(value1, formatExpression, value2, operator, format);
                first = expression;
            }
        }
        if (value1 == null) return new CalculationResult("-", DynamicAttribute.DA_TYPE_DATE);
        if (value1 instanceof Date) {
            return new CalculationResult(FormatterFactory.getDateFormatter().formatDateAsString((Date) value1), DynamicAttribute.DA_TYPE_DATE);
        }
        return new CalculationResult(value1.toString(), DynamicAttribute.DA_TYPE_TEXTFIELD);
    }

    private Object getValue(Expression expression, Node node) {

        Object value1 = null;
        if (expression.isAttributeOperand()) {
            if (expression.isCurrentDate()) {
                value1 = new Date();
            } else {
                // find the answer to the dateAttribute we have
                DynamicAttribute dynamicAttribute = expression.getAttribute();
                if (dynamicAttribute.isCalculated()) {
                    CalculationResult calculationResult = dynamicAttribute.getCalculation().execute(node);
                    String value = calculationResult.getValue();
                    if (!Report.hasValue(value)) {
                        return null;
                    }
                    Object result;
                    if (DynamicAttribute.DA_TYPE_DATE.equals(calculationResult.getType())) {
                        result = FormatterFactory.getDateFormatter().getDateValue(value);
                    } else {
                        try {
                            result = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            result = value;
                        }
                    }
                    return result;
                }
                AttributeValue attributeValue = node.getDynamicAttributeValues().get(dynamicAttribute);
                if (attributeValue != null) {
                    String value = attributeValue.getValue();
                    value1 = FormatterFactory.getDateFormatter().getDateValue(value);
                }
            }

        } else if (expression.isRefValueOperand()) {
            try {
                value1 = PropertyUtils.getNestedProperty(node, expression.getRefValue());
            } catch (Exception e) {
                logger.error(e);
            }

        } else {
            try {
                value1 = new Double(expression.getValue());
            } catch (NumberFormatException e) {
                logger.error(e);
            }
        }
        return value1;
    }

    public static final int NO_FORMAT = 0;
    public static final int YEARS = 1;
    public static final int MONTHS = 2;
    public static final int DAYS = 3;
    public static final int WEEKYEARS = 4;
    public static final int WEEKS = 5;
    public static final int YEARS_MONTHS = 6;
    public static final int YEARS_MONTHS_DAYS = 7;

    public static final int HOURS = 101;
    public static final int MINUTES = 102;
    public static final int SECONDS = 103;
    public static final int DATE = 104;

    protected final Log logger = LogFactory.getLog(getClass());
}
