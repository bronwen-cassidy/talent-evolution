/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.data;

import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderCategoryURLGenerator;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderChartPostProcessor;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.SpiderChartProducer;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Apr-2011 09:43:15
 */
public class FilledSpiderChartReport extends FilledReport {

    public FilledSpiderChartReport() {
        super(null);
    }


    public void setProducer(SpiderChartProducer producer) {
        this.producer = producer;
    }

    public void setURLGenerator(SpiderCategoryURLGenerator URLGenerator) {
        this.URLGenerator = URLGenerator;
    }

    public void setPostProcessor(SpiderChartPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public SpiderChartProducer getProducer() {
        return producer;
    }

    public SpiderCategoryURLGenerator getURLGenerator() {
        return URLGenerator;
    }

    public SpiderChartPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public boolean isPieChart() {
        return false;
    }

    public boolean isSpiderChart() {
        return true;
    }

    private SpiderChartProducer producer;
    private SpiderCategoryURLGenerator URLGenerator;
    private SpiderChartPostProcessor postProcessor;
}
