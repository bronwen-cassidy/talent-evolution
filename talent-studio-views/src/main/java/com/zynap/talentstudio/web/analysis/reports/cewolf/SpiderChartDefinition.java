/*
 * Created on 13.04.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf;

import de.laures.cewolf.ChartValidationException;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.taglib.AbstractChartDefinition;
import de.laures.cewolf.taglib.DataAware;
import de.laures.cewolf.taglib.DataContainer;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

import java.io.Serializable;
import java.util.Map;

public class SpiderChartDefinition extends AbstractChartDefinition implements DataAware, Serializable {

    private DataContainer dataAware = new DataContainer();

    public SpiderChartDefinition() {
        setType("spider");
    }

    protected JFreeChart produceChart() throws DatasetProduceException, ChartValidationException {
        ZynapSpiderWebPlot plot = new ZynapSpiderWebPlot((CategoryDataset) getDataset());
        plot.setStartAngle(54D);
        plot.setInteriorGap(0.40000000000000002D);
        
        plot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setURLGenerator(new StandardCategoryURLGenerator());
        return new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    }

    public Object getDataset() throws DatasetProduceException {
        return dataAware.getDataset();
    }

    public void setDataProductionConfig(DatasetProducer dsp, Map params, boolean useCache) {
        dataAware.setDataProductionConfig(dsp, params, useCache);
    }
}