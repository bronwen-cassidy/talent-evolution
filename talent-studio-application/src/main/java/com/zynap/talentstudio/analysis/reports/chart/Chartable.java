/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.chart;

import com.zynap.talentstudio.analysis.reports.Report;

import java.util.Collection;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Apr-2010 17:31:52
 */
public interface Chartable {

    Map<? extends Comparable, Integer> getMappedDataSet();

    double calculatePercentage(String key);

    ChartSlice get(Comparable key);

    Collection<ChartSlice> getValues();

    Report getReport();

    String getId();
}
