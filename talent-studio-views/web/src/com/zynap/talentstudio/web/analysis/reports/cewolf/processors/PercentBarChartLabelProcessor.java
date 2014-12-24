/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;

import org.jfree.data.category.CategoryDataset;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-Feb-2006 08:47:46
 */
public class PercentBarChartLabelProcessor extends AbstractBarChartPercentLabelGenerator {

    public PercentBarChartLabelProcessor(Number total, int decimalPlaces) {
        super(decimalPlaces);
        this.total = total;
    }

    public Object clone() throws CloneNotSupportedException {
        PercentBarChartLabelProcessor o = (PercentBarChartLabelProcessor) super.clone();
        o.total = this.total;
        o.percentageFormatter = this.percentageFormatter;
        return o;
    }

    Number getTotal(CategoryDataset dataset, int row, int column) {
        return total;
    }

    private Number total;
}