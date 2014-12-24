package com.zynap.talentstudio.analysis.populations;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTableKey;
import com.zynap.talentstudio.organisation.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 08-Jun-2006
 * Time: 09:48:12
 */
public class TestHibernatePopulationEngineCrossTab extends AbstractPopulationEngineTestCase {

    protected String getDataSetFileName() {
        return "test-crosstab-data.xml";
    }

    public void testOrgUnitVsExtendedAttributeCountMetric() throws Exception {

        final Long userId = ROOT_USER_ID;

        final Population population = populationEngine.getAllPositionsPopulation();

        final AnalysisParameter verticalColumnAttribute = new AnalysisParameter(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, null, null);
        final AnalysisParameter horizontalColumnAttribute = new AnalysisParameter(IMPORTANCE_EXT_ATTR, null, null);

        Metric metric = IPopulationEngine.COUNT_METRIC;
        runCrossTabReport(population, metric, verticalColumnAttribute, horizontalColumnAttribute, userId);
    }

    public void testOrgUnitVsExtendedAttributeAverageMetric() throws Exception {

        final Population population = populationEngine.getAllPositionsPopulation();

        final AnalysisParameter verticalColumnAttribute = new AnalysisParameter(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, null, null);
        final AnalysisParameter horizontalColumnAttribute = new AnalysisParameter(IMPORTANCE_EXT_ATTR, null, null);

        Metric metric = (Metric) metricService.findById(CONTRACT_DURATION_AVG_METRIC_ID);
        runCrossTabReport(population, metric, verticalColumnAttribute, horizontalColumnAttribute, ROOT_USER_ID);
    }

    public void testOrgUnitVsExtendedAttributeSumMetric() throws Exception {

        final Population population = populationEngine.getAllPositionsPopulation();

        final AnalysisParameter verticalColumnAttribute = new AnalysisParameter(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, null, null);
        final AnalysisParameter horizontalColumnAttribute = new AnalysisParameter(IMPORTANCE_EXT_ATTR, null, null);

        Metric metric = (Metric) metricService.findById(CONTRACT_DURATION_SUM_METRIC_ID);
        runCrossTabReport(population, metric, verticalColumnAttribute, horizontalColumnAttribute, ROOT_USER_ID);
    }

    public void testOrgUnitVsExtendedAttributeDerivedAttributeAverageMetric() throws Exception {

        final Population population = populationEngine.getAllPositionsPopulation();

        final AnalysisParameter verticalColumnAttribute = new AnalysisParameter(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, null, null);
        final AnalysisParameter horizontalColumnAttribute = new AnalysisParameter(IMPORTANCE_EXT_ATTR, null, null);

        Metric metric = (Metric) metricService.findById(DIRECT_SUB_AVG_METRIC_ID);
        runCrossTabReport(population, metric, verticalColumnAttribute, horizontalColumnAttribute, ROOT_USER_ID);
    }

    public void testOrgUnitVsExtendedAttributeDerivedAttributeSumMetric() throws Exception {

        final Population population = populationEngine.getAllPositionsPopulation();

        final AnalysisParameter verticalColumnAttribute = new AnalysisParameter(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, null, null);
        final AnalysisParameter horizontalColumnAttribute = new AnalysisParameter(IMPORTANCE_EXT_ATTR, null, null);

        Metric metric = (Metric) metricService.findById(DIRECT_SUB_SUM_METRIC_ID);
        runCrossTabReport(population, metric, verticalColumnAttribute, horizontalColumnAttribute, ROOT_USER_ID);
    }

    public void testExtendedVsExtendedAttributeDerivedAttributeSumMetric() throws Exception {

        final Population population = populationEngine.getAllPositionsPopulation();

        final AnalysisParameter verticalColumnAttribute = new AnalysisParameter("-12", null, null);
        final AnalysisParameter horizontalColumnAttribute = new AnalysisParameter(IMPORTANCE_EXT_ATTR, null, null);

        Metric metric = (Metric) metricService.findById(DIRECT_SUB_SUM_METRIC_ID);
        runCrossTabReport(population, metric, verticalColumnAttribute, horizontalColumnAttribute, ROOT_USER_ID);
    }

    private void runCrossTabReport(Population population, final Metric metric, final AnalysisParameter verticalColumnAttribute, final AnalysisParameter horizontalColumnAttribute, Long userId) throws Exception {

        final Map results = populationEngine.findCrossTab(population, metric, verticalColumnAttribute, horizontalColumnAttribute, userId);

        assertNotNull(results);
        assertFalse(results.isEmpty());

        final Map expectedValues = getExpectedValues(population, metric, verticalColumnAttribute, horizontalColumnAttribute, userId);
        compareExpectedValues(expectedValues, results);
    }

    private Map getExpectedValues(Population population, Metric metric, AnalysisParameter verticalColumnAttribute, AnalysisParameter horizontalColumnAttribute, Long userId) throws Exception {

        final boolean average = metric.isAverage();

        final Map<CrossTableKey, Number> expectedValues = new HashMap<CrossTableKey, Number>();
        final Map count = new HashMap();

        final List nodes = populationEngine.find(population, userId);
        for (int i = 0; i < nodes.size(); i++) {
            Node node = (Node) nodes.get(i);

            final String verticalAttributeValue = getValue(verticalColumnAttribute, node);
            final String horizontalAttributeValue = getValue(horizontalColumnAttribute, node);
            final Number metricValue = getMetricValue(metric, node);

            CrossTableKey crossTableKey = new CrossTableKey(verticalAttributeValue, horizontalAttributeValue);
            addValue(expectedValues, count, crossTableKey, metricValue);

            // add totals for rows, cols and totals
            addToRowTotal(expectedValues, count, verticalAttributeValue, metricValue);
            addToColTotal(expectedValues, count, horizontalAttributeValue, metricValue);
            addToTotal(expectedValues, count, metricValue);
        }

        // ensure that total / total, na / total, total / na and na / na are all present
        checkForTotalTotal(expectedValues);
        checkForNATotal(expectedValues);
        checkForTotalNA(expectedValues);
        checkForNANA(expectedValues);

        // calculate averages
        if (average) {
            calculateAverages(expectedValues, count);
        }

        return expectedValues;
    }

    private void calculateAverages(Map<CrossTableKey, Number> output, Map count) {

        for (Iterator iterator = count.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            final CrossTableKey key = (CrossTableKey) entry.getKey();
            final Number value = output.get(key);
            if (value != null) {
                final Number countValue = (Number) count.get(key);
                final Number avg = new Double(value.doubleValue() / countValue.doubleValue());
                output.put(key, avg);
            }
        }
    }

    private void checkForTotalTotal(Map<CrossTableKey, Number> output) {
        CrossTableKey key = new CrossTableKey(IPopulationEngine._CROSS_TAB_TOTAL, IPopulationEngine._CROSS_TAB_TOTAL);
        if (!output.containsKey(key)) {
            output.put(key, new Integer(0));
        }
    }

    private void checkForNATotal(Map<CrossTableKey, Number> output) {
        CrossTableKey key = new CrossTableKey(null, IPopulationEngine._CROSS_TAB_TOTAL);
        if (!output.containsKey(key)) {
            output.put(key, new Integer(0));
        }
    }

    private void checkForTotalNA(Map<CrossTableKey, Number> output) {
        CrossTableKey key = new CrossTableKey(IPopulationEngine._CROSS_TAB_TOTAL, null);
        if (!output.containsKey(key)) {
            output.put(key, new Integer(0));
        }
    }

    private void checkForNANA(Map<CrossTableKey, Number> output) {
        CrossTableKey key = new CrossTableKey(null, null);
        if (!output.containsKey(key)) {
            output.put(key, new Integer(0));
        }
    }

    private void addToTotal(Map output, Map count, Number newValue) {
        // add to total total
        final CrossTableKey key = new CrossTableKey(IPopulationEngine._CROSS_TAB_TOTAL, IPopulationEngine._CROSS_TAB_TOTAL);
        addValue(output, count, key, newValue);
    }

    private void addToRowTotal(Map output, Map count, String attributeValue, Number newValue) {
        final CrossTableKey key = new CrossTableKey(attributeValue, IPopulationEngine._CROSS_TAB_TOTAL);
        addValue(output, count, key, newValue);
    }

    private void addToColTotal(Map output, Map count, String attributeValue, Number newValue) {
        final CrossTableKey key = new CrossTableKey(IPopulationEngine._CROSS_TAB_TOTAL, attributeValue);
        addValue(output, count, key, newValue);
    }

    private void addValue(Map output, Map count, CrossTableKey crossTableKey, Number metricValue) {

        addToMap(output, crossTableKey, metricValue);
        addToMap(count, crossTableKey, new Integer(1));
    }

    private void compareExpectedValues(Map expectedValues, Map results) {

        for (Iterator iterator = results.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            final CrossTableKey key = (CrossTableKey) entry.getKey();

            final Number value = (Number) entry.getValue();
            final Number expectedValue = (Number) expectedValues.get(key);

            if (value == null) {
                assertEquals(0, expectedValue.intValue());
            } else {
                // format numbers to stop comparison failing due to less significant digits - eg: 1.81817 vs 1.8181
                assertEquals(key.toString(), formatValue(expectedValue), formatValue(value));
            }
        }
    }
}
