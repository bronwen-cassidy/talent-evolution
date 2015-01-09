/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import org.apache.commons.collections.Predicate;

import org.springframework.util.Assert;

/**
* Class or Interface description.
*
* @author bcassidy
* @version 0.1
* @since 10-Apr-2010 11:34:25
*/
public class ReportDirectionPredicate implements Predicate {

    public ReportDirectionPredicate(String direction) {
        Assert.notNull(direction, "cannot evaluate with a null direction");
        this.direction = direction;
    }

    public boolean evaluate(Object object) {
        Column column = (Column) object;
        return direction.equals(column.getColumnSource());
    }

    private String direction;
}
