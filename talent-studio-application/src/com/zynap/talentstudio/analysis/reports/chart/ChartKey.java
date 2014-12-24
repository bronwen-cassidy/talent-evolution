/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.chart;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Apr-2010 19:02:01
 */
public class ChartKey implements Comparable<ChartKey>, Serializable {

    public ChartKey(String label) {
        this.label = label;
    }

    public int compareTo(ChartKey o) {
        return label.compareTo(o.label);
    }

    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChartKey)) return false;
        ChartKey chartKey = (ChartKey) o;
        return label.equals(chartKey.label);        
    }

    @Override
    public int hashCode() {
        return 14 * label.hashCode();
    }

    private String label;
}
