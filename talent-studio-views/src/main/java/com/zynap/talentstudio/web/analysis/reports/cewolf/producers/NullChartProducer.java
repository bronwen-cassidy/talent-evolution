/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 19-May-2008 12:57:36
 */
public class NullChartProducer extends AbstractPieChartProducer {

    public String[] getColumnLabelItems() {
        return new String[0];
    }

    public boolean isOverlaidChart() {
        return false;
    }

    int getTotalNumberOfCharacters() {
        return 0;
    }

    public boolean isHasValues() {
        return false;
    }
}
