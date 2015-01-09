/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class QueryParameter implements Serializable {

    /**
     * Create a new QueryParameter containing a string value and type.
     *
     * @param value - the string value
     * @param type
     */
    public QueryParameter(Object value, int type) {
        this(value, type, false, false);
    }

    public QueryParameter(AttributeValue av) {
        this(av.getValue(), STRING, false, false);
    }

    public QueryParameter(AttributeValue attributeValue, boolean wildCard, boolean upper) {
        this(attributeValue.getValue(), STRING, wildCard, upper);
    }

    /**
     * Create a new QueryParameter containing a string value and type.
     *
     * @param value - the string value
     * @param type
     * @param wildcard If this is a wild card
     * @param upper if the string should be converted to upper case
     */
    public QueryParameter(Object value, int type, boolean wildcard, boolean upper) {
        this.value = value;
        this.type = type;
        this.wildcard = wildcard;
        this.upper = upper;
    }

    public Object getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public void buildClause(String entityName, String key, StringBuffer stringBuffer) {
        stringBuffer.append(IPopulationEngine.AND);
        if ((type == STRING) && upper) stringBuffer.append(UPPER_FUNCTION_START);
        stringBuffer.append(entityName).append(DOT).append(key);
        if ((type == STRING) && upper) stringBuffer.append(UPPER_FUNCTION_END);
        buildClause(stringBuffer);
    }

    public PopulationCriteria buildCriteria(String key) {
        PopulationCriteria criteria = new PopulationCriteria();
        criteria.setType(IPopulationEngine.OP_TYPE_);
        criteria.setOperator(IPopulationEngine.AND);
        criteria.setAttributeName(key);
        boolean string = (type == STRING);
        if (string) {
            if (wildcard) {
                criteria.setRefValue(value.toString());
                criteria.setComparator(IPopulationEngine.LIKE);
            } else {
                criteria.setRefValue(value.toString());
                criteria.setComparator(IPopulationEngine.EQ);
            }
        } else {
            criteria.setRefValue(value.toString());
            criteria.setComparator(IPopulationEngine.EQ);
        }
        return criteria;
    }


    public void buildClause(StringBuffer stringBuffer) {

        if (wildcard) {
            stringBuffer.append(WILDCARD_OPERATOR);
        } else {
            stringBuffer.append(EQUALITY_OPERATOR);
        }

        boolean string = (type == STRING);
        if (string) {

            final String escapedValue = StringUtils.replace((String) value, "'", "''");

            if (upper) stringBuffer.append(UPPER_FUNCTION_START);
            if (wildcard) {
                stringBuffer.append(SINGLE_QUOTE).append(WILDCARD_CHAR).append(escapedValue).append(WILDCARD_CHAR).append(SINGLE_QUOTE);
            } else {
                stringBuffer.append(SINGLE_QUOTE).append(escapedValue).append(SINGLE_QUOTE);
            }
            if (upper) stringBuffer.append(UPPER_FUNCTION_END);
        } else {
            stringBuffer.append(value);
        }


    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("QueryParameter[");
        stringBuffer.append("\r\n value=").append(value);
        stringBuffer.append("\r\n type=").append(type);
        stringBuffer.append("\r\n wildcard=").append(wildcard);
        stringBuffer.append("\r\n upper=").append(upper);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryParameter)) return false;

        final QueryParameter queryParameter = (QueryParameter) o;

        return type == queryParameter.type && value.equals(queryParameter.value) && wildcard == queryParameter.wildcard;
    }

    public int hashCode() {
        int result;
        result = value.hashCode();
        result = 29 * result + type + (wildcard ? 1 : 0);
        return result;
    }

    public String buildValue() {

        if(type == STRING) {
            return " = '" + value + "'";
        } else if (type == STRINGARRAY) {
            return buildArrayValue();
        }
        return " = " + value.toString();
    }

    private String buildArrayValue() {
        
        StringBuffer x = new StringBuffer(" in (");
        String[] temp = (String[]) value;
        for (int i = 0; i < temp.length; i++) {
            String s = temp[i];
            x.append(" '").append(s).append("' ");
            if(i < (temp.length - 1)) {
                x.append(",");
            }
        }
        x.append(") ");
        return x.toString();
    }

    private static final String WILDCARD_CHAR = "%";
    private static final String SINGLE_QUOTE = "'";
    private static final String WILDCARD_OPERATOR = " LIKE ";
    private static final String EQUALITY_OPERATOR = " = ";
    private static final String UPPER_FUNCTION_START = " UPPER (";
    //private static final String AND_OPERATOR = " AND ";
    private static final String UPPER_FUNCTION_END = ")";
    private static final String DOT = ".";

    private Object value;
    private int type;
    private boolean wildcard;
    private boolean upper;
    
    public static final int NUMBER = 4;
    public static final int BOOLEAN = 2;
    public static final int STRING = 12;
    public static final int STRINGARRAY = 22;
}
