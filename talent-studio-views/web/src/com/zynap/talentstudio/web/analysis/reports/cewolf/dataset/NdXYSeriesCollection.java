/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.dataset;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.DomainInfo;
import org.jfree.data.Range;
import org.jfree.util.ObjectUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.Serializable;

/**
 * Class or Interface description.
 * see xy series collection - replicated to deal with normal distribution
 * @author taulant bajraktari
 * @version 0.1
 * @since 23-Feb-2009 15:56:59
 */
public class NdXYSeriesCollection extends AbstractIntervalXYDataset implements IntervalXYDataset, DomainInfo,
        Serializable {

    private static final long serialVersionUID = -7590013825931496766L;


    ArrayList data;
    private NdXYDelegate intervalDelegate;

    public NdXYSeriesCollection() {
        this(null);
    }


    public NdXYSeriesCollection(XYSeries series) {
        this.data = new java.util.ArrayList();
        this.intervalDelegate = new NdXYDelegate(this);
        addChangeListener(this.intervalDelegate);
        if (series != null) {
            this.data.add(series);
            series.addChangeListener(this);

        }

    }


    /**
     * Adds a series to the collection and sends a {@link DatasetChangeEvent}
     * to all registered listeners.
     *
     * @param series the series (<code>null</code> not permitted).
     */
    public void addSeries(XYSeries series) {

        if (series == null) {
            throw new IllegalArgumentException("Null 'series' argument.");
        }
        this.data.add(series);
        series.addChangeListener(this);
        fireDatasetChanged();

    }



    /**
     * Returns the number of series in the collection.
     *
     * @return The series count.
     */
    public int getSeriesCount() {
        return this.data.size();
    }


    /**
     * Returns a series from the collection.
     *
     * @param series the series index (zero-based).
     * @return The series.
     */
    public XYSeries getSeries(int series) {
        if ((series < 0) || (series >= getSeriesCount())) {
            throw new IllegalArgumentException("Series index out of bounds");
        }
        return (XYSeries) this.data.get(series);
    }

    /**
     * Returns the key for a series.
     *
     * @param series the series index (zero-based).
     * @return The key for a series.
     */
    public Comparable getSeriesKey(int series) {
        // defer argument checking
        return getSeries(series).getKey();
    }

    /**
     * Returns the number of items in the specified series.
     *
     * @param series the series (zero-based index).
     * @return The item count.
     */
    public int getItemCount(int series) {
        // defer argument checking
        return getSeries(series).getItemCount();
    }

    /**
     * Returns the x-value for the specified series and item.
     *
     * @param series the series (zero-based index).
     * @param item   the item (zero-based index).
     * @return The value.
     */
    public Number getX(int series, int item) {
        XYSeries ts = (XYSeries) this.data.get(series);
        XYDataItem xyItem = ts.getDataItem(item);
        return xyItem.getX();
    }

    /**
     * Returns the starting X value for the specified series and item.
     *
     * @param series the series (zero-based index).
     * @param item   the item (zero-based index).
     * @return The starting X value.
     */
    public Number getStartX(int series, int item) {
        return this.intervalDelegate.getStartX(series, item);
    }

    /**
     * Returns the ending X value for the specified series and item.
     *
     * @param series the series (zero-based index).
     * @param item   the item (zero-based index).
     * @return The ending X value.
     */
    public Number getEndX(int series, int item) {
        return this.intervalDelegate.getEndX(series, item);
    }

    /**
     * Returns the y-value for the specified series and item.
     *
     * @param series the series (zero-based index).
     * @param index  the index of the item of interest (zero-based).
     * @return The value (possibly <code>null</code>).
     */
    public Number getY(int series, int index) {

        XYSeries ts = (XYSeries) this.data.get(series);
        XYDataItem xyItem = ts.getDataItem(index);
        return xyItem.getY();

    }

    /**
     * Returns the starting Y value for the specified series and item.
     *
     * @param series the series (zero-based index).
     * @param item   the item (zero-based index).
     * @return The starting Y value.
     */
    public Number getStartY(int series, int item) {
        return getY(series, item);
    }

    /**
     * Returns the ending Y value for the specified series and item.
     *
     * @param series the series (zero-based index).
     * @param item   the item (zero-based index).
     * @return The ending Y value.
     */
    public Number getEndY(int series, int item) {
        return getY(series, item);
    }

    /**
     * Tests this collection for equality with an arbitrary object.
     *
     * @param obj the object (<code>null</code> permitted).
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        /*
        * XXX
        *
        * what about  the interval delegate...?
        * The interval width etc wasn't considered
        * before, hence i did not add it here (AS)
        *
        */

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NdXYSeriesCollection)) {
            return false;
        }
        NdXYSeriesCollection that = (NdXYSeriesCollection) obj;
        return ObjectUtilities.equal(this.data, that.data);
    }

    /**
     * Returns a hash code.
     *
     * @return A hash code.
     */
    public int hashCode() {
        // Same question as for equals (AS)
        return (this.data != null ? this.data.hashCode() : 0);
    }

    /**
     * Returns the minimum x-value in the dataset.
     *
     * @param includeInterval a flag that determines whether or not the
     *                        x-interval is taken into account.
     * @return The minimum value.
     */
    public double getDomainLowerBound(boolean includeInterval) {
        return this.intervalDelegate.getDomainLowerBound(includeInterval);
    }

    /**
     * Returns the maximum x-value in the dataset.
     *
     * @param includeInterval a flag that determines whether or not the
     *                        x-interval is taken into account.
     * @return The maximum value.
     */
    public double getDomainUpperBound(boolean includeInterval) {
        return this.intervalDelegate.getDomainUpperBound(includeInterval);
    }

    /**
     * Returns the range of the values in this dataset's domain.
     *
     * @param includeInterval a flag that determines whether or not the
     *                        x-interval is taken into account.
     * @return The range.
     */
    public Range getDomainBounds(boolean includeInterval) {
        if (includeInterval) {
            return this.intervalDelegate.getDomainBounds(includeInterval);
        } else {
            return DatasetUtilities.iterateDomainBounds(this, includeInterval);
        }

    }

 
}
