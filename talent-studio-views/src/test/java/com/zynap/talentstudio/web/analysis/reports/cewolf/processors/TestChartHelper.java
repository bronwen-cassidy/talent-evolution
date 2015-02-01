package com.zynap.talentstudio.web.analysis.reports.cewolf.processors;

/**
 * User: amark
 * Date: 21-Mar-2006
 * Time: 13:49:02
 */

import de.laures.cewolf.tooltips.PieToolTipGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.TestAnalysisService;
import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.MetricReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTableKey;
import com.zynap.talentstudio.analysis.reports.crosstab.Heading;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.Direction;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.analysis.reports.cewolf.ChartConstants;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractBarChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractChartProducer;
import com.zynap.talentstudio.web.analysis.reports.data.CrossTabFilledReport;
import com.zynap.talentstudio.web.analysis.reports.support.CrossTabResultSetFormatter;
import com.zynap.talentstudio.web.analysis.reports.support.MetricReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunMetricReportWrapper;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestChartHelper extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        userService = (IUserService) applicationContext.getBean("userService");
        reportService = (IReportService) applicationContext.getBean("reportService");
        metricService = (IMetricService) getBean("metricService");
        analysisService = (IAnalysisService) getBean("analysisService");
        metricReportRunner = (MetricReportRunner) getBean("metricReportRunner");

        chartHelper = new ChartHelper();
    }

    public void testCreateCrossTabBarChartProducer() throws Exception {

        final String chartType = ChartConstants.BAR_HORIZONTAL_TYPE_3D;

        final String usId = "2";
        final String ukId = "0";
        final String europeId = "3";

        final List<Heading> headers = new LinkedList<Heading>();
        headers.add(new Heading(ukId, "UK"));
        headers.add(new Heading(usId, "US"));
        headers.add(new Heading(europeId, "Europe"));

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

        final AbstractBarChartProducer crossTabBarChartProducer = chartHelper.createCrossTabBarChartProducer(runReportWrapperBean);
        assertEquals(runReportWrapperBean.getHorizontalHeader(), crossTabBarChartProducer.getXAxisLabel());
        assertEquals(runReportWrapperBean.getVerticalHeader(), crossTabBarChartProducer.getYAxisLabel());
        assertEquals(chartType, crossTabBarChartProducer.getChartOrientation());
        assertEquals("", crossTabBarChartProducer.getColumnLabelItems()[0]);
        assertTrue(crossTabBarChartProducer.isBarChart());

        // get output
        final DefaultCategoryDataset dataSet = (DefaultCategoryDataset) crossTabBarChartProducer.produceDataset(new HashMap());

        // check column keys
        final List columnKeys = dataSet.getColumnKeys();
        assertNullAndTotalsNotPresent(columnKeys);

        for (int i = 0; i < columnKeys.size(); i++) {
            Object columnKey = columnKeys.get(i);
            assertEquals(headers.get(i).getLabel(), columnKey);
        }

        // check rows
        final List rowKeys = dataSet.getRowKeys();
        assertNullAndTotalsNotPresent(rowKeys);

        // check specific values
        final Number ukValue = dataSet.getValue(0, 0);
        assertEquals(ukMetricValue.intValue(), ukValue.intValue());

        final Number usValue = dataSet.getValue(1, 1);
        assertEquals(usMetricValue.intValue(), usValue.intValue());

        final Number europeNoValue = dataSet.getValue(1, 2);
        assertEquals(europeNoMetricValue.intValue(), europeNoValue.intValue());

        final Number europeYesValue = dataSet.getValue(0, 2);
        assertEquals(europeYesMetricValue.intValue(), europeYesValue.intValue());
    }

    public void testCreateCrossTabPieChartProducer() throws Exception {

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

        final CrossTabReport report = (CrossTabReport) runReportWrapperBean.getReport();
        final List results = (List) runReportWrapperBean.getDiscreetRows();
        final String chartLabel = "chart name";
        final int rowIndex = 0;
        final int columnIndex = 0;

        final AbstractChartProducer crossTabPieChartProducer = chartHelper.createCrossTabPieChartProducer(results, rowIndex, columnIndex, chartLabel, report);
        assertEquals(chartLabel, crossTabPieChartProducer.getColumnLabelItems()[0]);
        assertEquals("Yes", crossTabPieChartProducer.getColumnLabelItems()[1]);
        assertEquals(report.getVerticalColumn().getLabel(), crossTabPieChartProducer.getColumnLabelItems()[2]);
        assertFalse(crossTabPieChartProducer.isBarChart());

        // get output
        final DefaultPieDataset dataSet = (DefaultPieDataset) crossTabPieChartProducer.produceDataset(new HashMap());
        assertNotNull(dataSet);

        // check keys
        final List keys = dataSet.getKeys();
        assertEquals(ukHeading.getLabel(), keys.get(0));
        assertEquals(europeHeading.getLabel(), keys.get(1));

        // check values
        assertEquals(2, dataSet.getItemCount());
        assertEquals(ukMetricValue.intValue(), dataSet.getValue(0).intValue());
        assertEquals(europeYesMetricValue.intValue(), dataSet.getValue(1).intValue());

        assertPieChartKeys(dataSet, crossTabPieChartProducer);
    }

    public void testCreateMetricBarChartProducer() throws Exception {

        final String chartType = ChartConstants.BAR_HORIZONTAL_TYPE_3D;

        final RunMetricReportWrapper runReportWrapperBean = buildRunMetricReportWrapper(chartType, true);
        final String groupingColumnLabel = runReportWrapperBean.getGroupingColumn().getLabel();

        final AbstractBarChartProducer metricBarChartProducer = chartHelper.createMetricBarChartProducer(runReportWrapperBean);
        assertEquals(chartType, metricBarChartProducer.getChartOrientation());
        assertEquals("", metricBarChartProducer.getColumnLabelItems()[0]);

        assertEquals(groupingColumnLabel, metricBarChartProducer.getXAxisLabel());
        assertEquals("", metricBarChartProducer.getYAxisLabel());

        assertTrue(metricBarChartProducer.isBarChart());

        final DefaultCategoryDataset dataSet = (DefaultCategoryDataset) metricBarChartProducer.produceDataset(new HashMap());
        assertNotNull(dataSet);

        // check rows
        final List rowKeys = dataSet.getRowKeys();
        assertNullAndTotalsNotPresent(rowKeys);

        // check column labels match columns keys in data source
        final List selectedColumnMetrics = runReportWrapperBean.getSelectedColumnMetrics();
        assertEquals(selectedColumnMetrics.size(), dataSet.getColumnKeys().size());
        for (Iterator iterator = selectedColumnMetrics.iterator(); iterator.hasNext();) {
            Column selectedColumn = (Column) iterator.next();
            final int colIndex = dataSet.getColumnIndex(selectedColumn.getLabel());
            final Comparable key = dataSet.getColumnKey(colIndex);
            assertEquals(selectedColumn.getLabel(), key);
            final Number value = dataSet.getValue(0, colIndex);
            assertNotNull(value);
        }
    }

    private void assertNullAndTotalsNotPresent(List headers) {
        assertFalse(headers.isEmpty());
        assertFalse(headers.contains(ReportConstants.TOTAL));
        assertFalse(headers.contains(ReportConstants.NA));
    }

    public void testCreateMetricPieChartProducerWithGrouping() throws Exception {

        final String chartType = "horizontalPie3D";

        final RunMetricReportWrapper runReportWrapperBean = buildRunMetricReportWrapper(chartType, true);

        final Column selectedColumn = runReportWrapperBean.getSelectedColumnMetrics().get(0);
        final String selectedColumnId = selectedColumn.getId().toString();
        final AbstractChartProducer metricPieChartProducer = chartHelper.createMetricPieChartProducer(runReportWrapperBean, selectedColumnId);
        assertFalse(metricPieChartProducer.isBarChart());
        assertTrue(metricPieChartProducer.isHasValues());

        final DefaultPieDataset dataSet = (DefaultPieDataset) metricPieChartProducer.produceDataset(new HashMap());
        assertNotNull(dataSet);

        assertPieChartKeys(dataSet, metricPieChartProducer);

        final String[] columnLabelItems = metricPieChartProducer.getColumnLabelItems();
        assertNotNull(columnLabelItems);
    }

    public void testCreateMetricPieChartProducer() throws Exception {

        final String chartType = "horizontalPie3D";

        final RunMetricReportWrapper runReportWrapperBean = buildRunMetricReportWrapper(chartType, false);

        final Column selectedColumn = runReportWrapperBean.getSelectedColumnMetrics().get(0);
        final String selectedColumnLabel = selectedColumn.getLabel();
        final String selectedColumnId = selectedColumn.getId().toString();

        final AbstractChartProducer metricPieChartProducer = chartHelper.createMetricPieChartProducer(runReportWrapperBean, selectedColumnId);
        assertFalse(metricPieChartProducer.isBarChart());
        assertTrue(metricPieChartProducer.isHasValues());

        final DefaultPieDataset dataSet = (DefaultPieDataset) metricPieChartProducer.produceDataset(new HashMap());
        assertNotNull(dataSet);

        assertPieChartKeys(dataSet, metricPieChartProducer);

        final List keys = dataSet.getKeys();
        assertEquals(1, keys.size());
        final String key = (String) keys.get(0);
        assertEquals(selectedColumnLabel, key);
        assertNotNull(dataSet.getValue(key));

        final String toolTip = ((PieToolTipGenerator) metricPieChartProducer).generateToolTip(dataSet, selectedColumnLabel, 0);
        assertNotNull(toolTip);

        final String[] columnLabelItems = metricPieChartProducer.getColumnLabelItems();
        assertNotNull(columnLabelItems);
    }

    public void testCreatePercentLabelProcessor() throws Exception {

        final String chartType = ChartConstants.BAR_HORIZONTAL_TYPE_3D;
        final RunMetricReportWrapper wrapper = buildRunMetricReportWrapper(chartType, true);

        // produce data set
        final AbstractBarChartProducer metricBarChartProducer = chartHelper.createMetricBarChartProducer(wrapper);
        final DefaultCategoryDataset dataSet = (DefaultCategoryDataset) metricBarChartProducer.produceDataset(new HashMap());

        // get labels
        final AbstractBarChartPercentLabelGenerator percentLabelProcessor = chartHelper.createPercentLabelProcessor(wrapper);
        assertNotNull(percentLabelProcessor);
        final String label = percentLabelProcessor.generateLabel(dataSet, 0, 0);
        assertNotNull(label);
    }

    private RunMetricReportWrapper buildRunMetricReportWrapper(String chartType, boolean addGrouping) throws TalentStudioException {

        final Population population = TestAnalysisService.createPopulationAllPositions();
        analysisService.create(population, ADMINISTRATOR_USER_ID);

        // create main report
        final Report report = new MetricReport();
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.METRIC_REPORT);
        report.setDefaultPopulation(population);

        // create avg metric
        Metric avgMetric = new Metric();
        avgMetric.setArtefactType(population.getType());
        avgMetric.setLabel("Avg Holders");
        avgMetric.setAccessType(AccessType.PRIVATE_ACCESS.toString());
        avgMetric.setOperator(IPopulationEngine.AVG);
        avgMetric.setAttributeName("targetDerivedAttributes[20]");
        avgMetric.setUserId(ADMINISTRATOR_USER_ID);
        metricService.create(avgMetric);

        final String columnSource = Direction.NA_DIRECTION.toString();

        if (addGrouping) {
            // add grouping column
            final Column groupingColumn = new Column("Org Unit", AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, DynamicAttribute.DA_TYPE_OU);
            groupingColumn.setPosition(new Integer(report.getColumns().size()));
            groupingColumn.setGrouped(true);
            report.addColumn(groupingColumn);
        }

        // add column for avg metric
        final Column avgColumn = new Column(avgMetric.getLabel(), avgMetric.getAttributeName(), new Integer(report.getColumns().size()), null, columnSource);
        avgColumn.setMetric(avgMetric);
        report.addColumn(avgColumn);

        // add column for count metric
        final Column countColumn = new Column("Count", null, new Integer(report.getColumns().size()), null, columnSource);
        countColumn.setMetric(IPopulationEngine.COUNT_METRIC);
        report.addColumn(countColumn);

        // create will set ids and compile design
        reportService.create(report);

        RunMetricReportWrapper wrapperBean = new RunMetricReportWrapper(report);

        // select the count column
        wrapperBean.setSelectedMetrics(new Long[]{countColumn.getId()});

        wrapperBean.setChartType(chartType);
        wrapperBean.setDecimalPlaces(2);
        metricReportRunner.run(wrapperBean, ADMINISTRATOR_USER_ID);

        return wrapperBean;
    }

    private RunCrossTabReportWrapper buildRunCrossTabReportWrapper(String chartType, Map resultSet, List<Heading> columnHeaders, List<Row> rows) {

        Column horizontalColumn = new Column("horizontal", "subjectPrimaryAssociations.position.organisationUnit.label", "");
        horizontalColumn.setId(new Long(-1));
        horizontalColumn.setColumnSource(Direction.HORIZONTAL.toString());

        Column verticalColumn = new Column("vertical", "9", "");
        verticalColumn.setId(new Long(-2));
        verticalColumn.setColumnSource(Direction.VERTICAL.toString());

        CrossTabReport report = new CrossTabReport();
        report.setReportType(Report.CROSSTAB_REPORT);

        report.setHorizontalColumn(horizontalColumn);
        report.setVerticalColumn(verticalColumn);

        final CrossTabResultSetFormatter crossTabResultSetFormatter = new CrossTabResultSetFormatter();
        int decimalPlaces = 0;
        RunCrossTabReportWrapper wrapper = new RunCrossTabReportWrapper(report);
        // todo build the crosstabCellInfos
        
        CrossTabFilledReport crossTabFilledReport = crossTabResultSetFormatter.formatResults(resultSet, columnHeaders, rows, decimalPlaces, wrapper);

        wrapper.setHorizontalHeadings(columnHeaders);
        wrapper.setChartType(chartType);
        wrapper.setResultFormat(RunCrossTabReportWrapper.PERCENTAGE_VALUE);
        wrapper.setFilledReport(crossTabFilledReport);

        return wrapper;
    }

    private void assertPieChartKeys(final DefaultPieDataset dataSet, final AbstractChartProducer pieChartProducer) {
        final List keys = dataSet.getKeys();
        assertFalse(keys.isEmpty());
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            assertNotNull(dataSet.getValue(key));

            final String toolTip = ((PieToolTipGenerator) pieChartProducer).generateToolTip(dataSet, key, 0);
            assertNotNull(toolTip);
        }
    }

    private IUserService userService;
    private IReportService reportService;
    private IMetricService metricService;
    private IAnalysisService analysisService;
    private MetricReportRunner metricReportRunner;
    private ChartHelper chartHelper;
}
