/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;


import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.zynap.common.util.XmlUtils;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.Cell;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractBarChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractPieChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.DefaultBarChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.DefaultOverlaidChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.DefaultPieChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.JasperBarChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.JasperOverlaidChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.JasperPieChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.NullChartProducer;
import com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunMetricReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Handles all things to do with charting creation and setup
 *
 * @author bcassidy
 * @version 0.1
 * @since 02-Mar-2006 12:10:11
 */
public final class ChartHelper implements Serializable {

    /**
     * Creates a pie chart producer for a metric report.
     *
     * @param wrapper
     * @param selectedColumnId
     * @return AbstractPieChartProducer
     */
    public AbstractPieChartProducer createMetricPieChartProducer(RunMetricReportWrapper wrapper, String selectedColumnId) {

        final int numColumns = wrapper.getReport().getColumns().size();
        final ColumnWrapperBean selectedColumn = wrapper.findColumn(new Long(selectedColumnId));
        final ColumnWrapperBean groupingColumn = wrapper.getGroupingColumn();

        return new JasperPieChartProducer(selectedColumn, groupingColumn, wrapper.getFilledReport().getJasperPrint(), numColumns);
    }

    /**
     * Creates a pie chart producer for a cross tab report.
     *
     * @param rows
     * @param rowIndex
     * @param columnIndex
     * @param label
     * @param report
     * @return AbstractPieChartProducer.
     */
    public AbstractPieChartProducer createCrossTabPieChartProducer(List rows, int rowIndex, int columnIndex, String label, Report report) {
        Row row;
        if (columnIndex == -1 && rowIndex == -1) {
            // invalid state build a nullProducer
            return new NullChartProducer();
        }
        if (rowIndex != -1) {
            row = (Row) rows.get(rowIndex);
        } else
            row = buildRow(rows, columnIndex);

        String attributeLabel = row.getHeading().getLabel();
        return new DefaultPieChartProducer(row, label, attributeLabel, report);
    }

    /**
     * Creates a bar chart producer for a metric report.
     *
     * @param wrapper
     * @return AbstractBarChartProducer
     */
    public AbstractBarChartProducer createMetricBarChartProducer(RunMetricReportWrapper wrapper) {

        String chartType = wrapper.getChartType();

        final int numColumns = wrapper.getReport().getColumns().size();
        final List selectedColumns = wrapper.getSelectedColumnMetrics();
        final ColumnWrapperBean groupingColumn = wrapper.getGroupingColumn();

        return new JasperBarChartProducer(chartType, numColumns, selectedColumns, groupingColumn, wrapper.getFilledReport().getJasperPrint());
    }

    public JasperOverlaidChartProducer createMetricOverlaidChartProducer(RunMetricReportWrapper wrapper) {

        String chartType = wrapper.getChartType();

        final int numColumns = wrapper.getReport().getColumns().size();
        final List selectedColumns = wrapper.getSelectedColumnMetrics();
        final ColumnWrapperBean groupingColumn = wrapper.getGroupingColumn();

        return new JasperOverlaidChartProducer(chartType, numColumns, selectedColumns, groupingColumn, wrapper.getFilledReport().getJasperPrint());
    }

    /**
     * Creates a bar chart producer for a cross tab report.
     *
     * @param wrapper
     * @return AbstractBarChartProducer
     */
    public AbstractBarChartProducer createCrossTabBarChartProducer(RunCrossTabReportWrapper wrapper) {

        String chartType = wrapper.getChartType();
        String axisOrientation = wrapper.getAxisOrientation();

        List rows = (List) wrapper.getDiscreetRows();
        List horizontalHeadings = (List) wrapper.getHorizontalHeadings();

        final String horizontalColumnLabel = wrapper.getHorizontalHeader();
        final String verticalColumnLabel = wrapper.getVerticalHeader();

        return new DefaultBarChartProducer(chartType, horizontalHeadings, rows, horizontalColumnLabel, verticalColumnLabel, axisOrientation);
    }

    public DefaultOverlaidChartProducer createCrossTabOverlaidChartProducer(RunCrossTabReportWrapper wrapper) {
        String chartType = wrapper.getChartType();
        String axisOrientation = wrapper.getAxisOrientation();

        List rows = (List) wrapper.getDiscreetRows();
        List horizontalHeadings = (List) wrapper.getHorizontalHeadings();

        final String horizontalColumnLabel = wrapper.getHorizontalHeader();
        final String verticalColumnLabel = wrapper.getVerticalHeader();

        return new DefaultOverlaidChartProducer(chartType, horizontalHeadings, rows, horizontalColumnLabel, verticalColumnLabel, axisOrientation);


    }

    /**
     * Creates a processor for building labels for a cross tab report.
     *
     * @param wrapper
     * @return StandardCategoryItemLabelGenerator
     */
    public AbstractBarChartPercentLabelGenerator createPercentLabelProcessor(RunCrossTabReportWrapper wrapper) {
        final Number total = getCrossTabTotal(wrapper);
        return new PercentBarChartLabelProcessor(total, wrapper.getDecimalPlaces());
    }

    /**
     * Creates a processor for building labels for a metric report.
     *
     * @param wrapper
     * @return StandardCategoryItemLabelGenerator
     */
    public AbstractBarChartPercentLabelGenerator createPercentLabelProcessor(RunMetricReportWrapper wrapper) {

        final List selectedColumns = wrapper.getSelectedColumnMetrics();
        final int numColumns = wrapper.getReport().getColumns().size();
        return new JasperBarChartLabelProcessor(wrapper.getDecimalPlaces(), selectedColumns, numColumns, wrapper.getFilledReport().getJasperPrint());
    }

    /**
     * Create processor that formats labels to correct number of decimal places for pie chart.
     *
     * @param runReportWrapperBean
     * @return PieChartPostProcessor
     */
    public PieChartPostProcessor createPieChartLabelProcessor(RunReportWrapperBean runReportWrapperBean) {
        return new PieChartPostProcessor(runReportWrapperBean.getDecimalPlaces());
    }

    /**
     * Create processor that formats labels to correct number of decimal places for bar chart.
     *
     * @param runReportWrapperBean
     * @return BarChartLabelPostProcessor
     */
    public BarChartLabelPostProcessor createBarChartLabelProcessor(RunReportWrapperBean runReportWrapperBean) {
        return new BarChartLabelPostProcessor();
    }

    public static NodeList getValues(JasperPrint jasperPrint) {

        StringBuffer stringBuffer = new StringBuffer();

        JRXmlExporter exporter = new JRXmlExporter();
        exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, stringBuffer);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

        URL schemaURL = XmlUtils.class.getClassLoader().getResource("jasper/jasperprint.dtd");
        exporter.setParameter(JRXmlExporterParameter.DTD_LOCATION, schemaURL.toString());

        try {
            exporter.exportReport();

            // vicious hack to remove all details from text nodes - if the report has a drill down URL the XML is mal-formed otherwise!!
            String xmlData = stringBuffer.toString().replaceAll("<text textHeight=.*>", "<text>");
            final Document document = XmlUtils.buildDocument(xmlData);

            // get elements from XML
            return document.getElementsByTagName("textContent");

        } catch (Exception e) {
            throw new TalentStudioRuntimeException(e);
        }
    }

    /**
     * Get total.
     *
     * @param wrapper
     * @return Number
     */
    private Number getCrossTabTotal(RunCrossTabReportWrapper wrapper) {

        final List rows = (List) wrapper.getDiscreetRows();

        if (rows == null || rows.isEmpty()) return AbstractBarChartProducer.DEFAULT_VALUE;
        Row row = (Row) rows.get(rows.size() - 1);
        Cell totalCell = row.getCell(row.getCells().size() - 1);

        final String value = totalCell.getValue();
        if (!StringUtils.hasText(value) || !Report.hasValue(value)) {
            return AbstractBarChartProducer.DEFAULT_VALUE;
        }

        return new Double(value);
    }

    /**
     * Builds a row from cells at the given index.
     *
     * @param rows
     * @param columnIndex
     * @return Row
     */
    private Row buildRow(List rows, int columnIndex) {
        Row newRow = new Row();
        for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
            Row row = (Row) iterator.next();
            Cell original = row.getCell(columnIndex);

            newRow.setHeading(original.getHeading());
            Cell newCell = new Cell.DefaultCell(original.getValue());
            final Heading heading = row.getHeading();
            newCell.setHeading(heading);
            newCell.setLabel(heading.getLabel());
            newRow.add(newCell);
        }
        return newRow;
    }


    public static final String OVERLAID_CHART_TYPE = "overlaidxy";
}
