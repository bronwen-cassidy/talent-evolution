/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.dataset;

import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.DomainInfo;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.PublicCloneable;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 *          replicating the XYSeriesCollection for barchart behaviour of normal distribution
 * @since 23-Feb-2009 17:02:25
 */
public class NdXYDelegate implements DatasetChangeListener,
        DomainInfo, Serializable,
        Cloneable, PublicCloneable {

    private static final long serialVersionUID = -685166711639592857L;

    private XYDataset dataset;


    public NdXYDelegate(XYDataset dataset) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        this.dataset = dataset;

    }


    public Number getStartX(int series, int item) {


        return new Long((dataset.getX(series, item)).intValue()-1);


    }

    public double getStartXValue(int series, int item) {
        return dataset.getXValue(series, item);

    }


    public Number getEndX(int series, int item) {

        return this.dataset.getX(series, item);

    }

    public double getEndXValue(int series, int item) {
        return dataset.getXValue(series, item);

    }

    public double getDomainLowerBound(boolean includeInterval) {
        double result = Double.NaN;
        Range r = getDomainBounds(includeInterval);
        if (r != null) {
            result = r.getLowerBound();
        }
        return result;
    }

    public double getDomainUpperBound(boolean includeInterval) {
        double result = Double.NaN;
        Range r = getDomainBounds(includeInterval);
        if (r != null) {
            result = r.getUpperBound();
        }
        return result;
    }

    public Range getDomainBounds(boolean includeInterval) {
        Range range = DatasetUtilities.findDomainBounds(this.dataset, false);
        return range;
    }

    public void datasetChanged(DatasetChangeEvent e) {

    }


    /**
     * @return A clone of this delegate.
     * @throws CloneNotSupportedException if the object cannot be cloned.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
