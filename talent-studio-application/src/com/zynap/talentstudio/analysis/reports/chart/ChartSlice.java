/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.chart;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Apr-2010 14:13:46
 */
public class ChartSlice {

    public ChartSlice(Column column) {
        this.column = column;
    }

    public void add(NodeExtendedAttribute answer) {
        attributes.add(answer);
    }

    /**
     * This method can only be used when setting the null count, or the count of people without and anwser
     *
     * @param nullCount - the number of subjects without an answer
     */
    public void setCount(int nullCount) {
        this.count = nullCount;
    }

    public void postProcess() {
        for (AttributeValue attributeValue : attributeValuesMap.values()) {
            NodeExtendedAttribute extendedAttribute = attributeValue.getLastNodeExtendedAttribute();
            attributes.add(extendedAttribute);
        }
        attributeValuesMap.clear();
    }

    public Column getColumn() {
        return column;
    }

    public int getCount() {
        return attributes.isEmpty() ? count : attributes.size();
    }

    public List<NodeExtendedAttribute> getAttributes() {
        return attributes;
    }

    private Map<String, AttributeValue> attributeValuesMap = new HashMap<String, AttributeValue>();
    private List<NodeExtendedAttribute> attributes = new ArrayList<NodeExtendedAttribute>();
    private Column column;
    private int count;
}
