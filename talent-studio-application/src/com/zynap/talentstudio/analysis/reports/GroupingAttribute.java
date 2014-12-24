/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.analysis.reports.jasper.JRCollectionDataSource;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 07-Jul-2008 12:45:42
 */
public class GroupingAttribute implements Serializable {

    public GroupingAttribute(String attributeName, Integer sortOrder, Column column) {
        this.attributeName = attributeName;
        this.sortOrder = sortOrder;
        this.column = column;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getDirection() {
        return sortOrder == JRCollectionDataSource.DEFAULT_ORDER ? "ASC" : "DESC";
    }

    public AnalysisParameter getAnalysisParameter() {
        return column.getAnalysisParameter();
    }

    public boolean isStruct() {
        final String type = column.getColumnType();
        return (DynamicAttribute.DA_TYPE_SELECT.equals(type) || DynamicAttribute.DA_TYPE_STRUCT.equals(type));
    }

    public boolean isNumber() {
        return DynamicAttribute.DA_TYPE_NUMBER.equals(column.getColumnType());
    }

    public boolean isDate() {
         return DynamicAttribute.DA_TYPE_DATE.equals(column.getColumnType());
    }

    private String attributeName;
    private Integer sortOrder = new Integer(JRCollectionDataSource.DEFAULT_ORDER);
    private Column column;
}
