package com.zynap.talentstudio.analysis.populations;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.organisation.Node;

import java.util.*;

/**
 * User: amark
 * Date: 13-Jun-2006
 * Time: 15:44:21
 */
public class TestMetricPopulationEngine extends AbstractPopulationEngineTestCase {

    protected String getDataSetFileName() {
        return "test-metric-data.xml";
    }

    public void testRunMetrics() throws Exception {

        final Long userId = ROOT_USER_ID;
        final Population population = populationEngine.getAllPositionsPopulation();

        runMetricReport(population, null, userId);
    }

    public void testRunSubjectMetricsEnumValueNullGrouping() throws Exception {

        int expectedValueCount = 2;
        int expectedPersonCount= 4;

        final Long userId = ROOT_USER_ID;
        final Population population = populationEngine.getAllSubjectsPopulation();


        final Collection metrics = new ArrayList();

        final Metric countMetric = IPopulationEngine.COUNT_METRIC;
        metrics.add(countMetric);

        final Metric enumValueCount = (Metric) metricService.findById(COUNT_OF_LEVEL_ONES);
        metrics.add(enumValueCount);

        final Map<String, Map> results = populationEngine.findMetrics(population, metrics, null, userId);
        final Map<String, Integer> innerResults = results.get(null);

        final Integer countNum = innerResults.get("met_-1");
        assertEquals(expectedPersonCount, countNum.intValue());

        final Integer valueCountNum = innerResults.get("met_7");
        assertEquals(expectedValueCount, valueCountNum.intValue());
    }

    public void testRunSubjectMetricsTextValueNullGrouping() throws Exception {

        int expectedValueCount = 1;
        int expectedPersonCount= 4;

        final Long userId = ROOT_USER_ID;
        final Population population = populationEngine.getAllSubjectsPopulation();


        final Collection metrics = new ArrayList();

        final Metric countMetric = IPopulationEngine.COUNT_METRIC;
        metrics.add(countMetric);

        final Metric enumValueCount = (Metric) metricService.findById(new Long(8));
        metrics.add(enumValueCount);

        final Map<String, Map> results = populationEngine.findMetrics(population, metrics, null, userId);
        final Map<String, Integer> innerResults = results.get(null);

        final Integer countNum = innerResults.get("met_-1");
        assertEquals(expectedPersonCount, countNum.intValue());

        final Integer valueCountNum = innerResults.get("met_8");
        assertEquals(expectedValueCount, valueCountNum.intValue());
    }

    public void testRunSubjectMetricsQStructValueNullGrouping() throws Exception {

        int expectedValueCount = 1;
        int expectedPersonCount= 4;

        final Long userId = ROOT_USER_ID;
        final Population population = populationEngine.getAllSubjectsPopulation();


        final Collection metrics = new ArrayList();

        final Metric countMetric = IPopulationEngine.COUNT_METRIC;
        metrics.add(countMetric);

        final Metric enumValueCount = (Metric) metricService.findById(new Long(9));
        metrics.add(enumValueCount);

        final Map<String, Map> results = populationEngine.findMetrics(population, metrics, null, userId);
        final Map<String, Integer> innerResults = results.get(null);

        final Integer countNum = innerResults.get("met_-1");
        assertEquals(expectedPersonCount, countNum.intValue());

        final Integer valueCountNum = innerResults.get("met_9");
        assertEquals(expectedValueCount, valueCountNum.intValue());
    }

    public void testRunSubjectMetricsQNumValueNullGrouping() throws Exception {

        int expectedValueCount = 3;
        int expectedPersonCount= 4;

        final Long userId = ROOT_USER_ID;
        final Population population = populationEngine.getAllSubjectsPopulation();


        final Collection metrics = new ArrayList();

        final Metric countMetric = IPopulationEngine.COUNT_METRIC;
        metrics.add(countMetric);

        final Metric enumValueCount = (Metric) metricService.findById(new Long(10));
        metrics.add(enumValueCount);

        final Map<String, Map> results = populationEngine.findMetrics(population, metrics, null, userId);
        final Map<String, Integer> innerResults = results.get(null);

        final Integer countNum = innerResults.get("met_-1");
        assertEquals(expectedPersonCount, countNum.intValue());

        final Integer valueCountNum = innerResults.get("met_10");
        assertEquals(expectedValueCount, valueCountNum.intValue());
    }

    public void testRunSubjectMetricsQNumValueOUGrouping() throws Exception {

        int expectedValueCount = 2;

        final Long userId = ROOT_USER_ID;
        final Population population = populationEngine.getAllSubjectsPopulation();
        final AnalysisParameter groupingAttribute = new AnalysisParameter("subjectPrimaryAssociations.position.organisationUnit.id", null, null);

        final Collection metrics = new ArrayList();

        final Metric countMetric = IPopulationEngine.COUNT_METRIC;
        metrics.add(countMetric);

        final Metric enumValueCount = (Metric) metricService.findById(new Long(10));
        metrics.add(enumValueCount);

        final Map<Object, Map> results = populationEngine.findMetrics(population, metrics, groupingAttribute, userId);

        final Map<String, Integer> innerResults = results.get(new Long(0));

        final Integer valueCountNum = innerResults.get("met_10");
        assertEquals(expectedValueCount, valueCountNum.intValue());
    }

    public void testRunMetricsEnumValueOUGrouping() throws Exception {

        int expectedValueCount = 1;

        final Long userId = ROOT_USER_ID;
        final Population population = populationEngine.getAllPositionsPopulation();
        final AnalysisParameter groupingAttribute = new AnalysisParameter(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, null, null);

        final Collection metrics = new ArrayList();

        final Metric countMetric = IPopulationEngine.COUNT_METRIC;
        metrics.add(countMetric);


        final Metric enumValueCount = (Metric) metricService.findById(COUNT_OF_HIGH_IMP);
        metrics.add(enumValueCount);

        final Map<Object, Map> results = populationEngine.findMetrics(population, metrics, groupingAttribute, userId);
        final Map<String, Integer> countResult = results.get(new Long(42));
        final Integer countValueResult = countResult.get("met_6");
        assertEquals(expectedValueCount, countValueResult.intValue());
    }

    public void testRunMetricsGroupedByOrgUnit() throws Exception {

        final Long userId = ROOT_USER_ID;
        final Population population = populationEngine.getAllPositionsPopulation();
        final AnalysisParameter groupingAttribute = new AnalysisParameter(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, null, null);

        runMetricReport(population, groupingAttribute, userId);
    }

    public void testRunMetricsGroupedByExtendedAttribute() throws Exception {

        final Long userId = ROOT_USER_ID;
        final Population population = populationEngine.getAllPositionsPopulation();
        final AnalysisParameter groupingAttribute = new AnalysisParameter(IMPORTANCE_EXT_ATTR, null, null);

        runMetricReport(population, groupingAttribute, userId);
    }

    private void runMetricReport(Population population, AnalysisParameter groupingAttribute, Long userId) throws Exception {

        final Collection metrics = new ArrayList();

        final Metric countMetric = IPopulationEngine.COUNT_METRIC;
        metrics.add(countMetric);

        final Metric sumDerivedAttrMetric = (Metric) metricService.findById(DIRECT_SUB_SUM_METRIC_ID);
        metrics.add(sumDerivedAttrMetric);

        final Metric avgDerivedAttrMetric = (Metric) metricService.findById(DIRECT_SUB_AVG_METRIC_ID);
        metrics.add(avgDerivedAttrMetric);

        final Metric sumExtendedAttrMetric = (Metric) metricService.findById(CONTRACT_DURATION_SUM_METRIC_ID);
        metrics.add(sumExtendedAttrMetric);

        final Metric avgExtendedAttrMetric = (Metric) metricService.findById(CONTRACT_DURATION_AVG_METRIC_ID);
        metrics.add(avgExtendedAttrMetric);

        final Map results = populationEngine.findMetrics(population, metrics, groupingAttribute, userId);
        final Map expectedValues = getExpectedValues(population, metrics, groupingAttribute, userId);
        compareExpectedValues(results, expectedValues, groupingAttribute, metrics);
    }

    private Map getExpectedValues(final Population population, final Collection metrics, final AnalysisParameter groupingAttribute, Long userId) throws Exception {

        final Map expectedValues = new HashMap();

        final boolean hasGroupingAttribute = groupingAttribute != null;
        final String groupingAttributeName = hasGroupingAttribute ? groupingAttribute.getName() : null;

        final List nodes = populationEngine.find(population, userId);
        for (int i = 0; i < nodes.size(); i++) {
            Node node = (Node) nodes.get(i);

            for (Iterator metricIterator = metrics.iterator(); metricIterator.hasNext();) {
                Metric metric = (Metric) metricIterator.next();

                String key = buildMetricKey(metric);
                final Number metricValue = getMetricValue(metric, node);

                if (hasGroupingAttribute) {

                    final String groupingAttributeValue = getValue(groupingAttribute, node);

                    Map expectedValuesMap = (Map) expectedValues.get(groupingAttributeValue);
                    if (expectedValuesMap == null) {
                        expectedValuesMap = new HashMap();
                        expectedValues.put(groupingAttributeValue, expectedValuesMap);
                    }

                    addToMap(expectedValuesMap, key, metricValue);
                }

                // add to total always
                addToTotal(expectedValues, key, metricValue, groupingAttributeName);
            }
        }

        for (Iterator iterator = expectedValues.entrySet().iterator(); iterator.hasNext();) {

            Map.Entry entry = (Map.Entry) iterator.next();
            final Map resultsMap = (Map) entry.getValue();

            for (Iterator metricsIterator = metrics.iterator(); metricsIterator.hasNext();) {

                final Metric metric = (Metric) metricsIterator.next();

                final String metricKey = buildMetricKey(metric);
                Object value = resultsMap.get(metricKey);

                if (metric.isAverage()) {

                    final Metric sumMetric = findMetric(metrics, metric, IPopulationEngine.AVG);
                    assertNotNull(sumMetric);
                    final Number sumValue = (Number) resultsMap.get(buildMetricKey(sumMetric));

                    final Metric countMetric = IPopulationEngine.COUNT_METRIC;
                    final Number countValue = (Number) resultsMap.get(buildMetricKey(countMetric));

                    // recalculate average and put back into results map
                    if (sumValue != null && countValue != null) {
                        value = new Double(sumValue.doubleValue() / countValue.doubleValue());
                    }
                }

                resultsMap.put(metricKey, value);
            }
        }

        return expectedValues;
    }

    private void addToTotal(Map values, String key, Number metricValue, Object groupingAttributeName) {

        final String total = ReportConstants.TOTAL;

        Map expectedValuesMap = (Map) values.get(total);
        if (expectedValuesMap == null) {
            expectedValuesMap = new HashMap();
            expectedValuesMap.put(groupingAttributeName, total);
            values.put(total, expectedValuesMap);
        }

        addToMap(expectedValuesMap, key, metricValue);
    }

    private void compareExpectedValues(Map results, Map expected, AnalysisParameter groupingAttribute, Collection metrics) {

        assertFalse(expected.isEmpty());
        assertFalse(results.isEmpty());

        if (groupingAttribute != null) {

            // check last value is always total if grouping
            final Set temp = results.entrySet();
            final Map.Entry totalEntry = (Map.Entry) new ArrayList(temp).get(temp.size() - 1);
            assertEquals(ReportConstants.TOTAL, totalEntry.getKey());
        } else {

            // size must be 1 if not grouping
            assertEquals(1, results.size());
        }

        for (Iterator iterator = results.values().iterator(); iterator.hasNext();) {
            final Map resultValues = (Map) iterator.next();

            // get grouping attribute value from results and use to find Map in expected values
            Object groupingAttributeValue = ReportConstants.TOTAL;
            if (groupingAttribute != null) {
                groupingAttributeValue = resultValues.get(groupingAttribute.getName());

                // make sure that grouping attribute is String
                if (!groupingAttribute.isDynamicAttribute()) {
                    groupingAttributeValue = groupingAttributeValue.toString();
                }
            }

            // get expected values map and check not null or empty
            final Map expectedValues = (Map) expected.get(groupingAttributeValue);
            if (ReportConstants.NA.equals(groupingAttributeValue)) {
                assertNull(expectedValues);
            } else {
                assertNotNull(expectedValues);
                assertFalse(expectedValues.isEmpty());

                // iterate metrics and get values from results and expected and compare
                for (Iterator metricsIterator = metrics.iterator(); metricsIterator.hasNext();) {
                    final Metric metric = (Metric) metricsIterator.next();
                    final String key = buildMetricKey(metric);
                    final Number resultValue = (Number) resultValues.get(key);
                    final Number expectedValue = (Number) expectedValues.get(key);

                    // format numbers to stop comparison failing due to less significant digits - eg: 1.81817 vs 1.8181
                    assertEquals("Incorrect values for metric: " + key + ", grouping: " + groupingAttributeValue, formatValue(expectedValue), formatValue(resultValue));
                }
            }
        }
    }

    private String buildMetricKey(Metric metric) {
        return ReportConstants.METRIC_ATTR_PREFIX + metric.getId();
    }

    private Metric findMetric(Collection metrics, Metric baseMetric, String operator) {

        for (Iterator iterator = metrics.iterator(); iterator.hasNext();) {
            Metric metric = (Metric) iterator.next();
            if (operator.equals(metric.getOperator()) && metric.getAnalysisParameter().equals(baseMetric.getAnalysisParameter())) {
                return metric;
            }
        }

        return null;
    }
}
