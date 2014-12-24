package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.TestAnalysisService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledTabularReport;
import com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import java.util.Collection;

/**
 * User: amark
 * Date: 09-May-2006
 * Time: 14:15:55
 */
public class TestCrossTabReportRunner extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        crossTabReportRunner = (CrossTabReportRunner) getBean("crossTabReportRunner");
    }

    public void testDisplaySubjectReport() throws Exception {

        final CrossTabReport report = new CrossTabReport();
        report.setReportType(Report.CROSSTAB_REPORT);

        Metric metric = IPopulationEngine.COUNT_METRIC;
        report.setDefaultMetric(metric);

        Population population = TestAnalysisService.createPopulationAllSubjects();
        report.setDefaultPopulation(population);

        final AnalysisParameter horizontalAttribute = AnalysisAttributeHelper.getAttributeFromName(SUBJECT_ORG_UNIT_ATTR);
        final AnalysisParameter verticalAttribute = AnalysisAttributeHelper.getAttributeFromName(EXTENDED_ATTR);
        final Column horizontalColumn = buildColumn(horizontalAttribute, -1);
        final Column verticalColumn = buildColumn(verticalAttribute, -2);

        report.setHorizontalColumn(horizontalColumn);
        report.setVerticalColumn(verticalColumn);

        // build and set display report
        Report displayReport = new TabularReport();
        displayReport.addColumn(horizontalColumn);
        displayReport.addColumn(verticalColumn);
        displayReport.setDefaultPopulation(population);
        displayReport.addColumn(buildColumn(AnalysisAttributeHelper.getAttributeFromName(AnalysisAttributeHelper.PERSON_TITLE_ATTR), -1));
        displayReport.addColumn(buildColumn(AnalysisAttributeHelper.getAttributeFromName(AnalysisAttributeHelper.PERSON_FULL_NAME_ATTR), -2));
        displayReport.setReportType(Report.TABULAR_REPORT);
        report.setDisplayReport(displayReport);

        runDisplayReport(report);
    }

    public void testDisplayReport() throws Exception {

        final CrossTabReport report = new CrossTabReport();
        report.setReportType(Report.CROSSTAB_REPORT);

        Metric metric = IPopulationEngine.COUNT_METRIC;
        report.setDefaultMetric(metric);

        Population population = TestAnalysisService.createPopulationAllPositions();
        report.setDefaultPopulation(population);

        final AnalysisParameter horizontalAttribute = AnalysisAttributeHelper.getAttributeFromName(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR);
        final AnalysisParameter verticalAttribute = AnalysisAttributeHelper.getAttributeFromName(EXTENDED_ATTR);
        final Column horizontalColumn = buildColumn(horizontalAttribute, -1);
        final Column verticalColumn = buildColumn(verticalAttribute, -2);

        report.setHorizontalColumn(horizontalColumn);
        report.setVerticalColumn(verticalColumn);

        // build and set display report
        TabularReport displayReport = new TabularReport();
        displayReport.setHorizontalColumn(horizontalColumn);
        displayReport.setVerticalColumn(verticalColumn);
        displayReport.setDefaultPopulation(population);
        displayReport.addColumn(buildColumn(AnalysisAttributeHelper.getAttributeFromName(AnalysisAttributeHelper.POSITION_TITLE_ATTR), -1));
        displayReport.addColumn(buildColumn(AnalysisAttributeHelper.getAttributeFromName(AnalysisAttributeHelper.ORG_UNIT_LABEL_ATTR), -2));
        displayReport.setReportType(Report.TABULAR_REPORT);
        report.setDisplayReport(displayReport);

        runDisplayReport(report);
    }

    public void testOUAndAppraisalAttribute() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();

        final AnalysisParameter horizontalAttribute = AnalysisAttributeHelper.getAttributeFromName(SUBJECT_ORG_UNIT_ATTR);
        assertTrue(horizontalAttribute.isAssociatedArtefactAttribute());

        final AnalysisParameter verticalAttribute = AnalysisAttributeHelper.getAttributeFromName(APPRAISAL_ATTR);
        assertNotNull(verticalAttribute.getQuestionnaireWorkflowId());
        assertNotNull(verticalAttribute.getRole());

        Metric metric = IPopulationEngine.COUNT_METRIC;
        runReport(population, horizontalAttribute, verticalAttribute, metric);
    }

    public void testOUAndExtendedAttribute() throws Exception {
        Population population = TestAnalysisService.createPopulationAllPositions();

        final AnalysisParameter horizontalAttribute = AnalysisAttributeHelper.getAttributeFromName(AnalysisAttributeHelper.ORG_UNIT_ID_ATTR);
        final AnalysisParameter verticalAttribute = AnalysisAttributeHelper.getAttributeFromName(EXTENDED_ATTR);

        Metric metric = IPopulationEngine.COUNT_METRIC;
        runReport(population, horizontalAttribute, verticalAttribute, metric);
    }

    public void testOUAndQuestionnaireAttribute() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();

        final AnalysisParameter horizontalAttribute = AnalysisAttributeHelper.getAttributeFromName(SUBJECT_ORG_UNIT_ATTR);
        assertTrue(horizontalAttribute.isAssociatedArtefactAttribute());

        final AnalysisParameter verticalAttribute = AnalysisAttributeHelper.getAttributeFromName(QUESTIONNAIRE_ATTR);
        assertNotNull(verticalAttribute.getQuestionnaireWorkflowId());

        Metric metric = IPopulationEngine.COUNT_METRIC;
        runReport(population, horizontalAttribute, verticalAttribute, metric);
    }

    public void testAppraisalAndExtendedAttribute() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();

        final AnalysisParameter horizontalAttribute = AnalysisAttributeHelper.getAttributeFromName(APPRAISAL_ATTR);
        assertNotNull(horizontalAttribute.getQuestionnaireWorkflowId());
        assertNotNull(horizontalAttribute.getRole());

        final AnalysisParameter verticalAttribute = AnalysisAttributeHelper.getAttributeFromName(EXTENDED_ATTR);

        Metric metric = IPopulationEngine.COUNT_METRIC;
        runReport(population, horizontalAttribute, verticalAttribute, metric);
    }

    public void testAppraisalAndQuestionnaireAttribute() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();

        final AnalysisParameter horizontalAttribute = AnalysisAttributeHelper.getAttributeFromName(APPRAISAL_ATTR);
        assertNotNull(horizontalAttribute.getQuestionnaireWorkflowId());
        assertNotNull(horizontalAttribute.getRole());

        final AnalysisParameter verticalAttribute = AnalysisAttributeHelper.getAttributeFromName(QUESTIONNAIRE_ATTR);
        assertNotNull(verticalAttribute.getQuestionnaireWorkflowId());

        Metric metric = IPopulationEngine.COUNT_METRIC;
        runReport(population, horizontalAttribute, verticalAttribute, metric);
    }

    public void testExtendedAndQuestionnaireAttribute() throws Exception {
        Population population = TestAnalysisService.createPopulationAllSubjects();

        final AnalysisParameter horizontalAttribute = AnalysisAttributeHelper.getAttributeFromName(EXTENDED_ATTR);

        final AnalysisParameter verticalAttribute = AnalysisAttributeHelper.getAttributeFromName(QUESTIONNAIRE_ATTR);
        assertNotNull(verticalAttribute.getQuestionnaireWorkflowId());

        Metric metric = IPopulationEngine.COUNT_METRIC;
        runReport(population, horizontalAttribute, verticalAttribute, metric);
    }

    private void runReport(Population population, AnalysisParameter horizontalAttribute, AnalysisParameter verticalAttribute, Metric metric) throws Exception {

        Long userId = ADMINISTRATOR_USER_ID;

        assertNotNull(horizontalAttribute.getName());
        assertNotNull(verticalAttribute.getName());

        final CrossTabReport report = new CrossTabReport();
        report.setReportType(Report.CROSSTAB_REPORT);
        report.setDefaultMetric(metric);
        report.setDefaultPopulation(population);

        // try columns in order specified
        report.setHorizontalColumn(buildColumn(horizontalAttribute, -1));
        report.setVerticalColumn(buildColumn(verticalAttribute, -2));

        RunCrossTabReportWrapper wrapper = new RunCrossTabReportWrapper(report);
        crossTabReportRunner.run(wrapper, userId);
        Collection rows = wrapper.getRows();
        assertNotNull(rows);
        assertNotNull(wrapper.getColumnNaTotal());
        assertNotNull(wrapper.getRowNaTotal());

        // clear columns
        report.getColumns().clear();

        // flip columns so vertical is now horizontal
        report.setVerticalColumn(buildColumn(horizontalAttribute, -2));
        report.setHorizontalColumn(buildColumn(verticalAttribute, -1));

        RunCrossTabReportWrapper inverseWrapper = new RunCrossTabReportWrapper(report);
        crossTabReportRunner.run(inverseWrapper, userId);
        Collection inverseRows = inverseWrapper.getRows();
        assertNotNull(inverseRows);
        assertNotNull(inverseWrapper.getColumnNaTotal());
        assertNotNull(inverseWrapper.getRowNaTotal());
    }

    private void runDisplayReport(final Report report) throws Exception {

        final IReportService reportService = (IReportService) getBean("reportService");
        reportService.compileReportDesign(report.getDisplayReport());

        Long userId = ADMINISTRATOR_USER_ID;
        RunCrossTabReportWrapper wrapper = new RunCrossTabReportWrapper(report);
        wrapper.setResultFormat(RunCrossTabReportWrapper.ARTEFACTS_VALUE);
        assertTrue(wrapper.isArtefactView());

        // run display report and check filled report
        crossTabReportRunner.run(wrapper, userId);
        final FilledReport filledReport = wrapper.getFilledReport();
        assertNotNull(filledReport);
        assertTrue(filledReport instanceof FilledTabularReport);
        assertNotNull(filledReport.getJasperPrint());
    }

    private Column buildColumn(AnalysisParameter attribute, int id) {

        final Column column = new Column();
        column.setAnalysisParameter(attribute);
        column.setId(new Long(id));
        assertEquals(attribute.getName(), column.getAttributeName());

        return column;
    }

    private CrossTabReportRunner crossTabReportRunner;

    private static final String SUBJECT_ORG_UNIT_ATTR = "subjectPrimaryAssociations.position.organisationUnit.id";
    private static final String APPRAISAL_ATTR = "12_12_peer";
    private static final String EXTENDED_ATTR = "39";
    private static final String QUESTIONNAIRE_ATTR = "33_1";
}
