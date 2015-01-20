package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;
/**
 * Created by IntelliJ IDEA.
 * User: syeoh
 * Date: 14-Dec-2006
 * Time: 11:21:46
 * To change this template use File | Settings | File Templates.
 */

public abstract class AbstractPieChartProducerTestCase extends AbstractChartProducerTestCase {

    public abstract IChartProducer getChartProducer();

    public void testIsNonZeroValue() throws Exception {
        //test for zero value
         assertFalse(getChartProducer().isNonZeroValue(new Double(0.0)));
        //also test for negative values. See TS-2270.
        assertFalse(getChartProducer().isNonZeroValue(new Double(-2.8)));
    }
}