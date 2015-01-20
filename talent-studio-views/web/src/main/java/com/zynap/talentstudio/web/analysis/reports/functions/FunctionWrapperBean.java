package com.zynap.talentstudio.web.analysis.reports.functions;

import com.zynap.talentstudio.calculations.MixedCalculation;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.Expression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Function wrapper for functions.
 */
public class FunctionWrapperBean implements Serializable {

    public FunctionWrapperBean() {
        this(null);
    }

    public FunctionWrapperBean(Calculation calculation) {
        if (calculation != null) {
            this.calculation = calculation;
            final List<Expression> exps = calculation.getExpressions();
            if (exps.isEmpty()) {
                initNewExpressions();
            }
            for (Expression exp : exps) {
                addWrappedExpression(exp);
            }
        } else {
            this.calculation = new MixedCalculation();
            initNewExpressions();
        }
    }

    public void addWrappedExpression(Expression exp) {
        operands.add(new OperandWrapperBean(exp));
    }

    public void addEmptyExpression() {
        addWrappedExpression(new Expression());
    }

    private void initNewExpressions() {
        addWrappedExpression(new Expression());
        addWrappedExpression(new Expression());
    }

    public void reset() {
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    /**
     * Get formula as textual representation eg: "salary + bonus".
     * or salary * 23 / 100
     * or (salary * 23) / 100
     * @return String
     */
    public String getFormulaDisplay() {
        StringBuffer formulaDisplay = new StringBuffer();
        for (OperandWrapperBean expression : operands) {
            formulaDisplay.append(expression.getDisplayValue());
            formulaDisplay.append(" ");
        }

        return formulaDisplay.toString();
    }

    /**
     * Index is the index of the attribute being modified in the list of population criteria in the command object.
     *
     * @return index
     */
    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Calculation getModifiedCalculation() {
        calculation.getExpressions().clear();
        for (OperandWrapperBean expression : operands) {
            calculation.addExpression(expression.getModifiedExpression());
        }
        return calculation;
    }

    public List<OperandWrapperBean> getOperands() {
        return operands;
    }

    private int target;

    private Long index;

    private Calculation calculation;
    private List<OperandWrapperBean> operands = new ArrayList<OperandWrapperBean>();
}
