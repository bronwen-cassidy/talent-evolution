package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

/**
* Class or Interface description.
*
* @author syeoh
* @since 14-Dec-2006 11:43:51
* @version 0.1
*/

public class TestJasperPieChartProducer extends AbstractPieChartProducerTestCase {

    IChartProducer pChartProducer;

     public IChartProducer getChartProducer() {
         ChartProducerGenerator generator = new ChartProducerGenerator();
         pChartProducer = generator.getChartProducer();
         return pChartProducer;
    }
}