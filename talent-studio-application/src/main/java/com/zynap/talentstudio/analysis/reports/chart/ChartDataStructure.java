/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.chart;

import com.zynap.talentstudio.analysis.reports.Report;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Apr-2010 17:34:19
 */
public class ChartDataStructure implements Serializable, Chartable {

    public ChartDataStructure(Map<String, ChartSlice> results, Report report) {
        this.results = results;
        this.report = report;
    }

    public Report getReport() {
        return report;
    }

    public Map<? extends Comparable, Integer> getMappedDataSet() {
        Map<ChartKey, Integer> values = new LinkedHashMap<ChartKey, Integer>();

        for (Map.Entry<String, ChartSlice> entry : results.entrySet()) {
            values.put(new ChartKey(entry.getKey()), new Integer(entry.getValue().getCount()));
        }
        return values;
    }

    public double calculatePercentage(String key) {
        int total = 0;
        for (ChartSlice chartSlice : results.values()) {
            total += chartSlice.getCount();
        }
        ChartSlice bar = results.get(key);

        double count = bar.getCount();
        return ((count / total) * 100);
    }

    public ChartSlice get(Comparable key) {
        return results.get(key.toString());
    }

    public Collection<ChartSlice> getValues() {
        return results.values();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Map<String, ChartSlice> getResults() {
        return results;
    }

    private Map<String, ChartSlice> results;
    private Report report;
    private String id;
}
