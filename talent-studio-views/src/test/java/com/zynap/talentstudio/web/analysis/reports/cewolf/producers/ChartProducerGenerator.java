/*
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.cewolf.producers;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTableKey;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.common.Direction;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.ChartHelper;
import com.zynap.talentstudio.web.analysis.reports.data.CrossTabFilledReport;
import com.zynap.talentstudio.web.analysis.reports.support.CrossTabResultSetFormatter;
import com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 03-Jan-2007 09:11:08
 */
public class ChartProducerGenerator extends ZynapMockControllerTest {

    private ChartHelper chartHelper;

    /**
     * @return A populated IChartProducer for test purposes.
     * @author syeoh
     */
    public IChartProducer getChartProducer() {
        chartHelper = new ChartHelper();
        IChartProducer pChartProducer;
        final String chartType = "horizontalPie3D";
        final String usId = "2";
        final String ukId = "0";
        final String europeId = "3";

        final Heading ukHeading = new Heading(ukId, "UK");
        final Heading usHeading = new Heading(usId, "US");
        final Heading europeHeading = new Heading(europeId, "Europe");

        final List<Heading> headers = new LinkedList<Heading>();
        headers.add(ukHeading);
        headers.add(usHeading);
        headers.add(europeHeading);

        final Long yesId = new Long(122);
        final Long noId = new Long(444);

        final List<Row> rows = new LinkedList<Row>();
        rows.add(new Row(new Heading(yesId.toString(), "Yes")));
        rows.add(new Row(new Heading(noId.toString(), "No")));

        final Integer ukMetricValue = new Integer(1);
        final Integer usMetricValue = new Integer(2);
        final Integer europeYesMetricValue = new Integer(3);
        final Integer europeNoMetricValue = new Integer(4);

        final Map<CrossTableKey, Number> resultSet = new HashMap<CrossTableKey, Number>();
        resultSet.put(new CrossTableKey(yesId.toString(), ukId), ukMetricValue);
        resultSet.put(new CrossTableKey(noId.toString(), usId), usMetricValue);
        resultSet.put(new CrossTableKey(yesId.toString(), europeId), europeYesMetricValue);
        resultSet.put(new CrossTableKey(noId.toString(), europeId), europeNoMetricValue);
        resultSet.put(new CrossTableKey(IPopulationEngine._CROSS_TAB_TOTAL, IPopulationEngine._CROSS_TAB_TOTAL), new Integer(10));

        final RunCrossTabReportWrapper runReportWrapperBean = buildRunCrossTabReportWrapper(chartType, resultSet, headers, rows);

        final Report report = runReportWrapperBean.getReport();
        final List results = (List) runReportWrapperBean.getDiscreetRows();
        final String chartLabel = "chart name";
        final int rowIndex = 0;
        final int columnIndex = 0;

        pChartProducer = chartHelper.createCrossTabPieChartProducer(results, rowIndex, columnIndex, chartLabel, report);
        return pChartProducer;
    }


    private RunCrossTabReportWrapper buildRunCrossTabReportWrapper(String chartType, Map<CrossTableKey, Number> resultSet, List<Heading> columnHeaders, List<Row> rows) {

        Column horizontalColumn = new Column();
        horizontalColumn.setId(new Long(-1));
        horizontalColumn.setColumnSource(Direction.HORIZONTAL.toString());
        horizontalColumn.setLabel("horizontal");

        Column verticalColumn = new Column();
        verticalColumn.setId(new Long(-2));
        verticalColumn.setColumnSource(Direction.VERTICAL.toString());
        verticalColumn.setLabel("vertical");

        CrossTabReport report = new CrossTabReport();
        report.setReportType(Report.CROSSTAB_REPORT);

        report.setHorizontalColumn(horizontalColumn);
        report.setVerticalColumn(verticalColumn);

        final CrossTabResultSetFormatter crossTabResultSetFormatter = new CrossTabResultSetFormatter();
        int decimalPlaces = 0;
        CrossTabFilledReport crossTabFilledReport = crossTabResultSetFormatter.formatResults(resultSet, columnHeaders, rows, decimalPlaces, new RunCrossTabReportWrapper(report));

        final RunCrossTabReportWrapper runReportWrapperBean = new RunCrossTabReportWrapper(report);
        runReportWrapperBean.setHorizontalHeadings(columnHeaders);
        runReportWrapperBean.setChartType(chartType);
        runReportWrapperBean.setResultFormat(RunCrossTabReportWrapper.PERCENTAGE_VALUE);
        runReportWrapperBean.setFilledReport(crossTabFilledReport);

        return runReportWrapperBean;
    }

    protected void setUp() throws Exception {
        super.setUp();
        chartHelper = new ChartHelper();
    }

    public void testSomething() throws Exception {

    }
}
