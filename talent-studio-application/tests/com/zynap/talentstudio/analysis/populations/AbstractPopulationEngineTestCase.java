package com.zynap.talentstudio.analysis.populations;

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.reports.DataFormatter;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;

import java.util.Map;

/**
 * User: amark
 * Date: 14-Jun-2006
 * Time: 12:18:15
 */
public abstract class AbstractPopulationEngineTestCase extends ZynapDatabaseTestCase {

    final Number getMetricValue(Metric metric, Node node) {
        Number value = null;

        if (metric.isCount()) {
            value = new Integer(1);
        } else {
            if (AnalysisAttributeHelper.isDynamicAttribute(metric.getAttributeName())) {
                final AttributeValue attributeValue = AnalysisAttributeHelper.findAttributeValue(node, metric.getDynamicAttributeId());
                if (attributeValue != null) value = Double.valueOf(attributeValue.getValue());
            } else if (AnalysisAttributeHelper.isDerivedAttribute(metric.getAttributeName())) {
                value = AnalysisAttributeHelper.getDerivedAttributeValue(metric.getAttributeName(), node);
            }
        }

        return value;
    }

    final String getValue(AnalysisParameter attribute, Node node) {
        final String attributeName = attribute.getName();

        String value = null;

        if (AnalysisAttributeHelper.isCoreAttribute(attributeName)) {
            value = AnalysisAttributeHelper.getCoreAttributeValue(attributeName, node);
        } else {
            final AttributeValue attributeValue = AnalysisAttributeHelper.findAttributeValue(attribute, node);
            if (attributeValue != null) value = attributeValue.getValue();
        }

        return value;
    }

    final void addToMap(Map map, Object key, Number value) {

        final Number countValue = (Number) map.get(key);
        if (countValue == null) {
            map.put(key, value);
        } else if (value != null) {
            final Number newValue = new Integer(countValue.intValue() + value.intValue());
            map.put(key, newValue);
        }
    }

    final String formatValue(final Number expectedValue) {
        return DataFormatter.formatValue(expectedValue, 2);
    }

    static final String IMPORTANCE_EXT_ATTR = "183";
    static final Long CONTRACT_DURATION_AVG_METRIC_ID = new Long(4);
    static final Long CONTRACT_DURATION_SUM_METRIC_ID = new Long(5);
    static final Long DIRECT_SUB_AVG_METRIC_ID = new Long(2);
    static final Long DIRECT_SUB_SUM_METRIC_ID = new Long(3);
    static final Long COUNT_OF_LEVEL_ONES = new Long(7);
    static final Long COUNT_OF_HIGH_IMP = new Long(6);
    final IMetricService metricService = (IMetricService) applicationContext.getBean("metricService");
    final IPopulationEngine populationEngine = (HibernatePopulationEngine) applicationContext.getBean("populationEngine");
}
