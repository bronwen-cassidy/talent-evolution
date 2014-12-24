/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf;

import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.taglib.AbstractChartDefinition;
import de.laures.cewolf.taglib.DataAware;
import de.laures.cewolf.taglib.tags.AbstractChartTag;

import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 28-Mar-2011 16:33:42
 */
public class SpiderChartTag extends AbstractChartTag implements DataAware {

    public void setDataProductionConfig(DatasetProducer dsp, Map params, boolean useCache) {
        ((SpiderChartDefinition)chartDefinition).setDataProductionConfig(dsp, params, useCache);
    }

    @Override
    protected AbstractChartDefinition createChartDefinition() {
        return new SpiderChartDefinition();
    }
}
