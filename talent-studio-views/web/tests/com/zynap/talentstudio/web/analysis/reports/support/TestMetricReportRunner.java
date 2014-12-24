package com.zynap.talentstudio.web.analysis.reports.support;

/**
 * User: amark
 * Date: 02-Mar-2006
 * Time: 15:54:53
 */

import com.zynap.domain.admin.User;
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
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.MixedCalculation;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.Direction;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.views.RunMetricReportWrapper;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.Iterator;
import java.util.List;

public class TestMetricReportRunner extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        userService = (IUserService) applicationContext.getBean("userService");
        reportService = (IReportService) applicationContext.getBean("reportService");
        metricService = (IMetricService) getBean("metricService");
        analysisService = (IAnalysisService) getBean("analysisService");

        metricReportRunner = (MetricReportRunner) getBean("metricReportRunner");
    }

    protected String getDataSetFileName() throws Exception {
        return "test-data.xml";
    }

    public void testRun() throws Exception {

        //User user = getAdminUser(userService);
        //final Long userId = user.getId();

        final Population population = TestAnalysisService.createPopulationAllPositions();
        analysisService.create(population, ADMINISTRATOR_USER_ID);

        // build drill down report
        final Report drillDownReport = new CrossTabReport();
        drillDownReport.setReportType(Report.CROSSTAB_REPORT);
        drillDownReport.setUserId(ADMINISTRATOR_USER_ID);
        reportService.create(drillDownReport);

        // create main report
        final Report report = new MetricReport();
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.METRIC_REPORT);
        report.setDefaultPopulation(population);
        report.setDrillDownReport(drillDownReport);

        // add grouping column first
        final Column groupingColumn = new Column("Org Unit", AnalysisAttributeHelper.ORG_UNIT_ID_ATTR, DynamicAttribute.DA_TYPE_OU);
        groupingColumn.setGrouped(true);
        report.addColumn(groupingColumn);

        // add column for count metric
        final Column column = new Column("Count", null, new Integer(0), null, Direction.NA_DIRECTION.toString());
        column.setMetric(IPopulationEngine.COUNT_METRIC);
        report.addColumn(column);

        // create avg metric
        Metric avgMetric = new Metric();
        avgMetric.setArtefactType(population.getType());
        avgMetric.setLabel("Avg Holders");
        avgMetric.setAccessType(AccessType.PRIVATE_ACCESS.toString());
        avgMetric.setOperator(IPopulationEngine.AVG);
        avgMetric.setAttributeName("targetDerivedAttributes[20]");
        avgMetric.setUserId(ADMINISTRATOR_USER_ID);
        metricService.create(avgMetric);

        // add column for avg metric
        final Column avgColumn = new Column(avgMetric.getLabel(), avgMetric.getAttributeName(), new Integer(1), null, Direction.NA_DIRECTION.toString());
        avgColumn.setMetric(avgMetric);
        report.addColumn(avgColumn);

        // create will set ids and compile design
        reportService.create(report);

        RunMetricReportWrapper wrapperBean = new RunMetricReportWrapper(report);
        wrapperBean.setDecimalPlaces(2);
        metricReportRunner.run(wrapperBean, ADMINISTRATOR_USER_ID);

        FilledReport filledReport = wrapperBean.getFilledReport();
        assertNotNull(filledReport);

        // check number of pages
        final List pages = filledReport.getJasperPrint().getPages();
        assertEquals(1, pages.size());
    }

    public void testRunFormula() throws Exception {

        User user = getAdminUser(userService);
        final Long userId = user.getId();

        final Population population = TestAnalysisService.createPopulationAllPositions();
        analysisService.create(population, userId);

        // build drill down report
        final Report drillDownReport = new CrossTabReport();
        drillDownReport.setReportType(Report.CROSSTAB_REPORT);
        drillDownReport.setUserId(ADMINISTRATOR_USER_ID);
        reportService.create(drillDownReport);

        // create main report
        final Report report = new MetricReport();
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.METRIC_REPORT);
        report.setDefaultPopulation(population);
        report.setDrillDownReport(drillDownReport);

        // add grouping column first
        final Column groupingColumn = new Column("Pref Lang", "38", DynamicAttribute.DA_TYPE_STRUCT);
        groupingColumn.setGrouped(true);
        report.addColumn(groupingColumn);

        // add column for count metric
        final Column column = new Column("Count", null, new Integer(0), null, Direction.NA_DIRECTION.toString());
        column.setMetric(IPopulationEngine.COUNT_METRIC);
        report.addColumn(column);

        // add column for avg metric
        final Column avgColumn = new Column("Double Count", "", new Integer(1), null, Direction.NA_DIRECTION.toString());
        avgColumn.setFormula(true);
        Calculation calc = new MixedCalculation();
        calc.addExpression(new Expression(IPopulationEngine.COUNT_METRIC, "+"));
        calc.addExpression(new Expression(IPopulationEngine.COUNT_METRIC, null));
        avgColumn.setCalculation(calc);
        report.addColumn(avgColumn);

        // create will set ids and compile design
        reportService.create(report);

        RunMetricReportWrapper wrapperBean = new RunMetricReportWrapper(report);
        wrapperBean.setDecimalPlaces(2);
        metricReportRunner.run(wrapperBean, userId);

        FilledReport filledReport = wrapperBean.getFilledReport();
        assertNotNull(filledReport);

        // check number of pages
        final List pages = filledReport.getJasperPrint().getPages();
        assertEquals(1, pages.size());
    }

    public void testGroupingByExtendedAtt() throws Exception {

        User user = getAdminUser(userService);
        final Long userId = user.getId();

        final Population population = TestAnalysisService.createPopulationAllPositions();
        analysisService.create(population, userId);

        // build drill down report
        final Report drillDownReport = new CrossTabReport();
        drillDownReport.setReportType(Report.CROSSTAB_REPORT);
        drillDownReport.setUserId(ADMINISTRATOR_USER_ID);
        reportService.create(drillDownReport);

        // create main report
        final Report report = new MetricReport();
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.METRIC_REPORT);
        report.setDefaultPopulation(population);
        report.setDrillDownReport(drillDownReport);

        // add grouping column first
        final Column groupingColumn = new Column("Preferred Language", "39", DynamicAttribute.DA_TYPE_STRUCT);
        groupingColumn.setGrouped(true);
        report.addColumn(groupingColumn);

        // add column for count metric
        final Column column = new Column("Count", null, new Integer(0), null, Direction.NA_DIRECTION.toString());
        column.setMetric(IPopulationEngine.COUNT_METRIC);
        report.addColumn(column);

        // create will set ids and compile design
        reportService.create(report);

        RunMetricReportWrapper wrapperBean = new RunMetricReportWrapper(report);
        wrapperBean.setDecimalPlaces(2);
        metricReportRunner.run(wrapperBean, userId);

        FilledReport filledReport = wrapperBean.getFilledReport();
        assertNotNull(filledReport);

        // check number of pages
        final List pages = filledReport.getJasperPrint().getPages();
        assertEquals(1, pages.size());
    }

    public void testOneExtendedAttribute() throws Exception {
        Long[] selectedIds = {EXTENDED_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testTwoExtendedAttributes() throws Exception {
        Long[] selectedIds = {EXTENDED_ATTR1_ID, EXTENDED_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testOneDerivedAttribute() throws Exception {
        Long[] selectedIds = {DERIVED_ATTR1_ID};
        runMetricReport(selectedIds);
    }

    public void testTwoDerivedAttributes() throws Exception {
        Long[] selectedIds = {DERIVED_ATTR1_ID, DERIVED_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testOneDerivedAndExtendedAttribute() throws Exception {
        Long[] selectedIds = {DERIVED_ATTR1_ID, EXTENDED_ATTR1_ID};
        runMetricReport(selectedIds);
    }

    public void testOneExtendedAndDerivedAttribute() throws Exception {
        Long[] selectedIds = {EXTENDED_ATTR1_ID, DERIVED_ATTR1_ID};
        runMetricReport(selectedIds);
    }

    public void testTwoDerivedAndExtendedAttribute() throws Exception {
        Long[] selectedIds = {DERIVED_ATTR1_ID, DERIVED_ATTR2_ID, EXTENDED_ATTR1_ID, EXTENDED_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testTwoExtendedAndDerivedAttribute() throws Exception {
        Long[] selectedIds = {EXTENDED_ATTR1_ID, EXTENDED_ATTR2_ID, DERIVED_ATTR1_ID, DERIVED_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testOneQuestionnaireAttribute() throws Exception {
        Long[] selectedIds = {QUE_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testTwoQuestionnaireAttributes() throws Exception {
        Long[] selectedIds = {QUE_ATTR2_ID, QUE_ATTR1_ID};
        runMetricReport(selectedIds);
    }

    public void testOneExtendedAndQuestionnaireAttribute() throws Exception {
        Long[] selectedIds = {EXTENDED_ATTR2_ID, QUE_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testOneQuestionnaireAndExtendedAttribute() throws Exception {
        Long[] selectedIds = {QUE_ATTR1_ID, EXTENDED_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testOneQuestionnaireAndExtendedAndDerivedAttribute() throws Exception {
        Long[] selectedIds = {QUE_ATTR1_ID, EXTENDED_ATTR2_ID, DERIVED_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testOneExtendedAndQuestionnaireAndDerivedAttribute() throws Exception {
        Long[] selectedIds = {EXTENDED_ATTR2_ID, QUE_ATTR1_ID, DERIVED_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testOneDerivedAndExtendedAndQuestionnaireAttribute() throws Exception {
        Long[] selectedIds = {DERIVED_ATTR2_ID, EXTENDED_ATTR2_ID, QUE_ATTR1_ID};
        runMetricReport(selectedIds);
    }

    public void testOneDerivedAndQuestionnaireAttribute() throws Exception {
        Long[] selectedIds = {DERIVED_ATTR1_ID, QUE_ATTR2_ID};
        runMetricReport(selectedIds);
    }

    public void testOneQuestionnaireAndDerivedAttribute() throws Exception {
        Long[] selectedIds = {QUE_ATTR1_ID, DERIVED_ATTR1_ID};
        runMetricReport(selectedIds);
    }

    public void testAll() throws Exception {
        runMetricReport(null);
    }

    public void testAllGrouped() throws Exception {

        final String selectedMetricId = "-101";

        final Report report = runMetricReport(new Long[]{Long.valueOf(selectedMetricId)});

        // add grouping column
        Column groupingColumn = new Column("test", selectedMetricId, DynamicAttribute.DA_TYPE_STRUCT);
        groupingColumn.setGrouped(true);
        groupingColumn.setReport(report);

        final List columns = report.getColumns();
        columns.add(0, groupingColumn);

        reportService.update(report);

        User user = getAdminUser(userService);
        final Long userId = user.getId();

        run(report, userId);
    }

    private Report runMetricReport(Long[] selectedIds) throws Exception {

        User user = getAdminUser(userService);
        final Long userId = user.getId();

        final Population population = TestAnalysisService.createPopulationAllPositions();
        analysisService.create(population, userId);

        // create main report
        final Report report = new MetricReport();
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.METRIC_REPORT);
        report.setDefaultPopulation(population);

        final List all = metricService.findAll(userId);

        if (selectedIds != null) {
            for (int i = 0; i < selectedIds.length; i++) {
                Long selectedId = selectedIds[i];
                final Metric metric = (Metric) all.get(all.indexOf(new Metric(selectedId)));
                addColumn(report, metric);
            }
        } else {
            for (Iterator iterator = all.iterator(); iterator.hasNext();) {
                final Metric metric = (Metric) iterator.next();
                addColumn(report, metric);
            }
        }

        // check right number of columns added
        if (selectedIds != null) assertEquals(selectedIds.length, report.getColumns().size());

        // save so that report design is generated
        reportService.create(report);

        run(report, userId);

        return report;
    }

    private void run(final Report report, final Long userId) throws TalentStudioException {
        metricReportRunner.run(new RunMetricReportWrapper(report), userId);
    }

    private void addColumn(Report report, Metric metric) {
        Column column = new Column(metric.getLabel());
        column.setMetric(metric);
        report.addColumn(column);
    }

    private IUserService userService;
    private IReportService reportService;
    private IMetricService metricService;
    private IAnalysisService analysisService;
    private MetricReportRunner metricReportRunner;

    private static final Long EXTENDED_ATTR1_ID = new Long(-103);
    private static final Long EXTENDED_ATTR2_ID = new Long(-116);

    private static final Long DERIVED_ATTR1_ID = new Long(-105);
    private static final Long DERIVED_ATTR2_ID = new Long(-117);

    private static final Long QUE_ATTR1_ID = new Long(-101);
    private static final Long QUE_ATTR2_ID = new Long(-119);
}
