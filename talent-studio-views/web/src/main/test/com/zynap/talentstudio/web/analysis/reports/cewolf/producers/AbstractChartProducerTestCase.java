package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;
/**
 * Created by IntelliJ IDEA.
 * User: syeoh
 * Date: 14-Dec-2006
 * Time: 11:13:20
 * To change this template use File | Settings | File Templates.
 */

import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

public abstract class AbstractChartProducerTestCase extends ZynapMockControllerTest {

    public abstract IChartProducer getChartProducer();

    static final String TEST_STRING = "someteststring";

    public void testIsNonZeroValue() throws Exception {
        //test for zero value
        assertFalse(getChartProducer().isNonZeroValue(new Double(0.0)));
    }

    public void testIsNotNA() throws Exception {
        assertFalse(getChartProducer().isNotNA(ReportConstants.NA));
        assertTrue(getChartProducer().isNotNA(TEST_STRING));
    }

    public void testIsNotTotal()
    {
        assertFalse(getChartProducer().isNotTotal(ReportConstants.TOTAL));
        assertTrue(getChartProducer().isNotTotal(TEST_STRING));
    }

    public void testIsNotTotalOrNA()
    {
        assertFalse(getChartProducer().isNotTotalOrNA(ReportConstants.TOTAL));
        assertTrue(getChartProducer().isNotTotalOrNA(TEST_STRING));
        assertFalse(getChartProducer().isNotTotalOrNA(ReportConstants.NA));
        assertTrue(getChartProducer().isNotTotalOrNA(TEST_STRING));
    }

       public void testConvertValue()
        {
            Double expected1 = new Double(2.0);
            Double actual1 = AbstractChartProducer.convertValue("2.0");

            Double expected2 = null;
            Double actual2 = AbstractChartProducer.convertValue(TEST_STRING);

            Double expected3 = new Double(-5.7);
            Double actual3 = AbstractChartProducer.convertValue("-5.7");

            Double expected4 = new Double(0.0);
            Double actual4 = AbstractChartProducer.convertValue("0.0");

            assertEquals(expected1, actual1);
            assertEquals(expected2, actual2);
            assertEquals(expected3, actual3);
            assertEquals(expected4, actual4);
        }
    
}