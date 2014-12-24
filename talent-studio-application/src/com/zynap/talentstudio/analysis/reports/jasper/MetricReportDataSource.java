package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 27-Feb-2006
 * Time: 16:34:06
 */
public final class MetricReportDataSource implements JRRewindableDataSource {

    private Report report;

    private JasperDataSourceFactory factory;

    /**
     * Constructor.
     *
     * @param results Map of maps
     * @param report
     * @param factory
     */
    public MetricReportDataSource(Map results, Report report, JasperDataSourceFactory factory) {

        records = results.values();
        this.report = report;
        this.factory = factory;
        init();
    }

    public boolean next() {
        boolean hasNext = false;

        if (iterator != null) {
            hasNext = iterator.hasNext();

            if (hasNext) {
                currentRecord = (Map) iterator.next();
            }
        }

        return hasNext;
    }

    public void moveFirst() {
        init();
    }

    public Object getFieldValue(JRField field) {

        final String fieldName = field.getName();
        Object fieldValue = getValue(fieldName);

        try {

            // check for MetricReportDesigner.ID_ATTR_PREFIX - if present remove the prefix and get the value
            boolean isIdField = fieldName.startsWith(ReportConstants.ID_ATTR_PREFIX);
            if (isIdField) {

                String name = fieldName.substring(ReportConstants.ID_ATTR_PREFIX.length());
                // if value equals IPopulationEngine.TOTAL set field value to null
                final Object value = getValue(name);
                if (checkIsNotTotalOrNA(value)) {
                    fieldValue = value;
                } else {
                    fieldValue = null;
                }

            } else if (fieldValue != null && checkIsNotTotalOrNA(fieldValue)) {

                // find column for field name
                final Column column = findColumn(fieldName);
                if (column != null && column.isGrouped()) {

                    // the label
                    String label;

                    // if grouped get attribute and use to determine attribute type - org unit or extended attribute
                    final String attributeName = column.getAttributeName();

                    // depending on type get label that corresponds to value - the value is an id
                    if (AnalysisAttributeHelper.isOrgUnitAttribute(attributeName)) {
                        label = factory.getNodeLabel(fieldValue.toString());
                    } else {
                        label = factory.getLookupValueLabel(fieldValue.toString());
                    }

                    // return here - safer as we don't overwrite the original fieldValue which we would then lose
                    return label;
                }
            } else if (fieldValue == null && !isIdField) {
                // TODO: FIXME (Bronwen) a hack we are not sure why we are doing 0 should be in the results but is not??
                fieldValue = Double.valueOf("0");
            }

        } catch (TalentStudioException e) {
            logger.error("Failed to get value for field: " + fieldName, e);
        }

        return fieldValue;
    }

    private void init() {
        if (records != null) {
            iterator = records.iterator();
        }
    }

    /**
     * Check field value does not equal ReportConstants.TOTAL or ReportConstants.NA.
     *
     * @param fieldValue
     * @return true or false
     */
    private boolean checkIsNotTotalOrNA(Object fieldValue) {
        return !ReportConstants.TOTAL.equals(fieldValue) && !ReportConstants.NA.equals(fieldValue);
    }

    /**
     * Find column associated with field.
     *
     * @param name
     * @return Column or null
     */
    private Column findColumn(String name) {

        final List columns = report.getColumns();
        if (columns != null) {
            for (Iterator colIterator = columns.iterator(); colIterator.hasNext();) {
                Column column = (Column) colIterator.next();

                if (column.isGrouped()) {
                    if (name.equals(AnalysisAttributeHelper.getName(column.getAnalysisParameter()))) {
                        return column;
                    }
                } else {
                    final Metric metric = column.getMetric();
                    if (metric != null) {
                        if (name.equals(metric.getId().toString())) {
                            return column;
                        }
                    } else if (name.equals(IPopulationEngine.COUNT)) {
                        return column;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Get value from current record.
     *
     * @param fieldName
     * @return value or null
     */
    private Object getValue(String fieldName) {
        Object value = null;

        if (currentRecord != null) {
            value = currentRecord.get(fieldName);
        }

        return value;
    }

    /**
     * Logger.
     */
    protected final Log logger = LogFactory.getLog(this.getClass());

    private Collection records = null;
    private Iterator iterator = null;
    private Map currentRecord = null;

}
