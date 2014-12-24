/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.calculations;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.organisation.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Jul-2007 11:14:37
 */
public abstract class Calculation extends ZynapDomainObject implements Serializable {


    public Calculation() {
    }


    public Calculation(String type) {
        this.type = type;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public void addExpression(Expression expression) {
        expression.setCalculation(this);
        expression.setIndex(expressions.size());
        expressions.add(expression);
    }

    /**
     * This method should be abstract in this class.
     *
     * @param node the object that contains the answers to an attribute question
     * @return the result of the calculation
     */
    public abstract CalculationResult execute(Node node);

    public String getConcatenatedAttributeName() {
        String result = "";
        for (Expression expression : expressions) {
            result += expression.getAttributeName() + expression.getOperator();
        }
        return result;
    }


    protected List<Expression> expressions = new ArrayList<Expression>();
    protected int format;
    private String type;
}
