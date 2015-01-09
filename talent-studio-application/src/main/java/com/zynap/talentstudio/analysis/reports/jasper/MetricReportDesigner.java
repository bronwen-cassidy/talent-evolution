package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 23-Feb-2006
 * Time: 13:29:00
 */
public class MetricReportDesigner extends ReportDesigner {

    public JasperReportDesign getDesign(Report report) throws TalentStudioException {

        final MetricReportDesign metricReportDesign = new MetricReportDesign();

        try {
            designReport(metricReportDesign, report);
            metricReportDesign.jasperReport = JasperCompileManager.compileReport(metricReportDesign.jasperDesign);
        } catch (JRException e) {
            logger.error("Unable to compile jasper design due to : " + e.getMessage(), e);
            throw new TalentStudioException(e);
        }

        return metricReportDesign;
    }

    /**
     * Overriden to handle fields correctly for grouped columns and to handle metrics.
     *
     * @param column
     * @return JRDesignField
     */
    protected JRDesignField createField(Column column, JasperDesign jasperDesign) throws JRException {

        final Metric metric = column.getMetric();
        Long id = metric != null ? metric.getId() : null;

        String name = getMetricFieldName(id);
        String className = Number.class.getName();

        return addField(name, jasperDesign, className);
    }

    private String getMetricFieldName(Object id) {
        return ReportConstants.METRIC_ATTR_PREFIX + (id != null ? id.toString() : IPopulationEngine.COUNT);
    }

    /**
     * Adds expression for link to chart.
     * <br/> If grouped column gets expression for link to bar chart page.
     * <br/> Otherwise gets link to pie chart.
     *
     * @param column
     * @param headerText
     */
    protected void createHeaderLinkIfRequired(Column column, JRDesignTextField headerText) {

        headerText.setHyperlinkType(JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR);
        JRDesignExpression expression = new JRDesignExpression();
        String chartLink;

        if (column.isGrouped()) {
            chartLink = "\"" + METRIC_BAR_COLUMN_HEADER_KEY + "(" + column.getId() + ")\"";
        } else {
            chartLink = "\"" + METRIC_COLUMN_HEADER_KEY + "(" + column.getId() + ")\"";
        }

        expression.setText(chartLink);
        expression.setValueClass(String.class);
        headerText.setAnchorNameExpression(expression);
    }

    /**
     * Add group element to report definition.
     * <br/> Adds group plus blank field.
     *
     * @param column  the report column
     * @param design  the overall report design
     * @param style   the style to apply
     * @param x       the x value the field is to be added at
     * @param y       the y value the firld is to be added at
     * 
     * @return A JRDesignElement, which is an instance of JRDesignTextField IF the column is not color displayable
     *         A JRDesignImage if the column is color displayable. The JRDesignElement is one that has been added for the group.
     *         If group already exists null is returned!
     * @throws net.sf.jasperreports.engine.JRException
     */
    protected JRDesignElement addGroup(Column column, JasperReportDesign design, JRStyle style, int x, int y) throws JRException {

        JRDesignTextField element = null;

        // get attribute name
        final AnalysisParameter groupingAttribute = column.getAnalysisParameter();
        final String groupingFieldName = AnalysisAttributeHelper.getName(groupingAttribute);

        // check for group to ensure duplicates are not added
        final JRGroup group = getGroupByName(groupingFieldName, design.jasperDesign);
        if (group == null) {

            // create field for grouping column
            JRDesignField field = addField(groupingFieldName, design.jasperDesign, String.class.getName());

            // add group (N.B this is a safe cast currently as metric reports have no colors - if this changes this method will throw a castClassException!
            // as the add group method returns a JRDesignImage if the column is colorDisplayable
            element = (JRDesignTextField) buildGroup(column, design, field, style, x, y);

            // add link for drill down
            Report drillDownReport = column.getReport().getDrillDownReport();
            if (drillDownReport != null) {

                StringBuffer buffer = new StringBuffer();

                // add field and add expression for field
                // add ID_ATTR_PREFIX to attribute name so that data source can look for field
                String idFieldName = ReportConstants.ID_ATTR_PREFIX + groupingFieldName;

                // determine field class - if a dynamic attribute will be string, otherwise will be number
                final String fieldClass = groupingAttribute.isDynamicAttribute() ? String.class.getName() : Number.class.getName();
                addField(idFieldName, design.jasperDesign, fieldClass);

                // add place holder for drill down url
                buffer.append("\"").append(_DRILL_DOWN_URL_PARAM_NAME);

                // add combined format for selected attribute - eg: "targetDerivedAttributes[12]" or "39" or "organisationUnit.id"
                buffer.append("&").append(ReportConstants.HORIZONTAL_ATTR).append("=").append(groupingFieldName);

                // add to expression to get value for idFieldName for column
                buffer.append("&").append(ReportConstants.HORIZONTAL_ATTR_VALUE).append("=").append("\" + $F{").append(idFieldName).append("}");

                // add to expression to get "column label = column value" eg: "Key Position = Yes"
                buffer.append(" + \"").append("&").append(ReportConstants.HORIZONTAL_ATTR_LABEL).append("=").append(column.getLabel()).append(" = \" + $F{").append(groupingFieldName).append("}");

                element.setHyperlinkType(JRHyperlink.HYPERLINK_TYPE_REFERENCE);
                JRDesignExpression expression = new JRDesignExpression();
                expression.setText(buffer.toString());
                expression.setValueClass(String.class);
                element.setHyperlinkReferenceExpression(expression);
            }
        }


        return element;
    }

    /**
     * Build report design.
     *
     * @param metricReportDesign
     * @param report
     * @throws TalentStudioException
     * @throws JRException
     */
    private void designReport(MetricReportDesign metricReportDesign, Report report) throws TalentStudioException, JRException {

        loadDesign(metricReportDesign, TEMPLATE);

        Map stylesMap = metricReportDesign.jasperDesign.getStylesMap();

        JRStyle headerStyle = (JRStyle) stylesMap.get(HEADER_STYLE);
        JRStyle detailsStyle = (JRStyle) stylesMap.get(DETAIL_STYLE);
        JRStyle groupStyle = (JRStyle) stylesMap.get(GROUP_STYLE);

        int x = 0;
        int headerY = 0;
        int detailsY = 20;

        final List columns = report.getColumns();

        JRDesignBand pageHeader = (JRDesignBand) metricReportDesign.jasperDesign.getPageHeader();
        metricReportDesign.detail = (JRDesignBand) metricReportDesign.jasperDesign.getDetail();

        // set band height for the details
        metricReportDesign.detail.setHeight(columns.size() > 1 ? columns.size() * 20 : 40);
        width = metricReportDesign.jasperDesign.getColumnWidth();

        // set page width and column counts
        metricReportDesign.jasperDesign.setPageWidth(width * report.getColumns().size());
        metricReportDesign.jasperDesign.setColumnCount(columns.size());

        // add parameter for decimal places
        final JRDesignParameter parameter = createParameter(ReportConstants.DECIMAL_PLACES_PARAM, Integer.class.getName());
        metricReportDesign.jasperDesign.addParameter(parameter);

        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            Column column = (Column) iterator.next();

            // add header to detail element
            addHeader(headerStyle, x, headerY, column, pageHeader);

            if (column.isGrouped()) {
                addGroup(column, metricReportDesign, columns.indexOf(column) == 0 ? groupStyle : detailsStyle, x, detailsY);
            } else if (column.isFormula()) {
                addFormula(column, metricReportDesign, detailsStyle, x, detailsY);
            } else {
                addDetail(column, metricReportDesign, detailsStyle, x, detailsY);
            }

            x += width;
        }
    }

    /**
     * Add detail with expression to format value to correct number of decimal places.
     * <br/> Number of decimal places is passed in as a parameter when running the report - see com.zynap.talentstudio.web.analysis.reports.support.MetricReportRunner.
     *
     * @param column
     * @param design
     * @param style
     * @param x
     * @param y
     * @throws JRException
     */
    private void addDetail(Column column, MetricReportDesign design, JRStyle style, int x, int y) throws JRException {

        JRDesignField field = createField(column, design.jasperDesign);

        JRDesignTextField element = new JRDesignTextField();
        element.setStretchWithOverflow(true);
        element.setPrintWhenDetailOverflows(true);

        // add expression for formatting to correct number of decimal places
        JRDesignExpression formatExpression = new JRDesignExpression();
        formatExpression.setValueClass(String.class);
        formatExpression.setText("DataFormatter.formatValue($F{" + field.getName() + "},$P{" + ReportConstants.DECIMAL_PLACES_PARAM + "})");
        element.setExpression(formatExpression);

        applyStyleElements(element, style, x, y, width, height);
        design.detail.addElement(element);
    }

    /**
     * Path to template file.
     */
    private static final String TEMPLATE = TEMPLATE_PATH + "tabular_template.jrxml";

    /**
     * Constants for placeholders used so that Jasper Exporters can substitute URLs at display time.
     */
    public static final String METRIC_COLUMN_HEADER_KEY = "_METRIC_LINK_";
    public static final String METRIC_BAR_COLUMN_HEADER_KEY = "_METRIC_BAR_LINK_";
}
