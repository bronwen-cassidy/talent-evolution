package com.zynap.talentstudio.web.analysis.populations;

import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 01-Mar-2005
 * Time: 09:59:55
 */
public class CriteriaWrapperBean extends AnalysisAttributeWrapperBean {

    private PopulationCriteria criteria;

    private String nodeLabel;

    private String leftBracket;

    private boolean inverse;

    private String rightBracket;

    private String operator;

    public Collection getComparators() {

        final Collection comparators = new ArrayList();

        if (attributeDefinition != null) {

            if (!isImage() && !isBlogComment()) {
                comparators.add(IPopulationEngine.EQ);
            }

            comparators.add(IPopulationEngine.ISNULL);

            if (isText() || isBlogComment()) {
                comparators.add(IPopulationEngine.LIKE);
            } else if (isDerivedAttribute() || isDate() || isNumber() || isSum() || isLastUpdated()) {
                comparators.add(IPopulationEngine.LT);
                comparators.add(IPopulationEngine.GT);
            }
        }

        return comparators;
    }

    public CriteriaWrapperBean(PopulationCriteria criteria) {
        this.criteria = criteria;
    }

    public void updateRefValue() {
        criteria.setRefValue(attributeDefinition.getValue());
    }

    public PopulationCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(PopulationCriteria criteria) {
        this.criteria = criteria;
    }

    public BasicAnalysisAttribute getAnalysisAttribute() {
        return criteria;
    }

    public String getType() {
        return criteria.getType();
    }

    public void setType(String type) {
        criteria.setType(type);
    }

    public String getAttrType() {
        return attributeDefinition.getType();
    }

    public Collection getActiveLookupValues() {
        return attributeDefinition.getActiveLookupValues();
    }

    public String getDisplayValue() {
        //TS-2303 Check to see if this bean can return the required value (blogcomments/text attributes) or whether it needs to get it from the attribute definition
        DynamicAttribute definition = attributeDefinition.getAttributeDefinition();
        if(definition.isBlogComment() || definition.isTextAttribute() || definition.isNumericType()) {
            return getRefValue();
        }                                                      
        return attributeDefinition.getDisplayValue();
    }

    public String getRefValue() {
        return criteria.getRefValue();
    }

    public void setRefValue(String refValue) {
        criteria.setRefValue(refValue);
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getComparator() {
        return criteria.getComparator();
    }

    public void setComparator(String comparator) {
        criteria.setComparator(comparator);
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public String getLeftBracket() {
        return leftBracket;
    }

    public void setLeftBracket(String leftBracket) {
        this.leftBracket = leftBracket;
    }

    public boolean getInverse() {
        return inverse;
    }

    public String getNot() {
        return getInverse() ? IPopulationEngine.NOT : "";
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }

    public String getRightBracket() {
        return rightBracket;
    }

    public void setRightBracket(String rightBracket) {
        this.rightBracket = rightBracket;
    }
}