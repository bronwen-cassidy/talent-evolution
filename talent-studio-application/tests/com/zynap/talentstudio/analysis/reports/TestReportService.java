/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.TestAnalysisService;
import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.arenas.MenuItem;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.Direction;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.common.lookups.ILookupManagerDao;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.security.permits.IPermit;
import com.zynap.talentstudio.security.users.IUserService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestReportService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        reportService = (IReportService) applicationContext.getBean("reportService");
        analysisService = (IAnalysisService) applicationContext.getBean("analysisService");
        dynamicAttributeService = (IDynamicAttributeService) applicationContext.getBean("dynamicAttrService");
        metricService = (IMetricService) applicationContext.getBean("metricService");
        arenaManager = (IArenaManager) applicationContext.getBean("arenaManager");
        userService = (IUserService) applicationContext.getBean("userService");
        populationEngine = (IPopulationEngine) applicationContext.getBean("populationEngine");
        lookupManagerDao = (ILookupManagerDao) applicationContext.getBean("lookupManDao");
    }

    public void testPublishReport() throws Exception {
        Population allPositionsPopulation = populationEngine.getAllPositionsPopulation();
        analysisService.create(allPositionsPopulation, ROOT_USER_ID);

        final Report tabularReport = buildTabularReport(allPositionsPopulation);

        final Collection<MenuSection> menuSections = arenaManager.getMenuSections(IArenaManager.ORGANISATION_MODULE);
        final MenuSection menuSection = menuSections.iterator().next();

        String url = "runreport.htm";
        String label = "test label";
        final MenuItem menuItem = new MenuItem(label, menuSection, url);
        tabularReport.addMenuItem(menuItem);

        reportService.create(tabularReport);
        assertNotNull(tabularReport.getId());
        final Set menuItems = tabularReport.getMenuItems();
        assertEquals(1, menuItems.size());

        // check that the menu item has the correct permit and URL
        final MenuItem newMenuItem = (MenuItem) menuItems.iterator().next();
        assertNotNull(newMenuItem.getId());
        assertNotNull(newMenuItem.getUrl());
        assertTrue(newMenuItem.isReportMenuItem());

        final IPermit permit = newMenuItem.getPermit();
        assertNotNull(permit);
        assertEquals(SecurityConstants.RUN_ACTION, permit.getAction());
        assertEquals(SecurityConstants.REPORTS_CONTENT, permit.getContent());
    }

    public void testCreateTabularReport() throws Exception {
        Population positionPopulation = TestAnalysisService.createPopulation();
        analysisService.create(positionPopulation, getAdminUser(userService).getId());
        final Report report = buildTabularReport(positionPopulation);
        reportService.create(report);

        // assert tabular report creation state valid
        Report actualReport = (Report) reportService.findById(report.getId());
        assertEquals(2, actualReport.getColumns().size());
        assertNotNull(actualReport.getDefaultPopulation());
    }

    public void testCreateMetricReport() throws Exception {

        // create metric
        User user = getAdminUser(userService);
        Metric metric = buildSubjectMetric(user);
        PopulationDto population = analysisService.findAll(Node.SUBJECT_UNIT_TYPE_, new Long(0), null).iterator().next();
        metricService.create(metric);
        Report report = buildMetricReport(population, metric);
        reportService.create(report);

        // find report
        Report actual = (Report) reportService.findById(report.getId());
        assertTrue(actual.isMetricReport());
        assertEquals(2, actual.getColumns().size());
    }

    public void testCreateCrossTabReport() throws Exception {
        // create a metric
        User user = getAdminUser(userService);
        Metric metric = buildSubjectMetric(user);
        metricService.create(metric);
        // get the subject population
        PopulationDto allSubjects = analysisService.findAll(Node.SUBJECT_UNIT_TYPE_, new Long(0), null).iterator().next();
        Report report = buildCrossTabReport(allSubjects, metric);
        reportService.create(report);

        // assert cross-tab report creation state valid
        CrossTabReport actualReport = (CrossTabReport) reportService.findById(report.getId());
        assertNotNull(actualReport.getHorizontalColumn());
        assertNotNull(actualReport.getVerticalColumn());
        assertEquals(2, actualReport.getColumns().size());
        assertNotNull(actualReport.getDefaultPopulation());
        assertNotNull(actualReport.getDefaultMetric());
    }

    public void testCreateReportSuccesfullExecution() throws Exception {
        // position population
        Population positionPopulation = TestAnalysisService.createPopulation();
        PopulationCriteria citeria = positionPopulation.getPopulationCriterias().iterator().next();
        citeria.setRefValue("Def");
        analysisService.create(positionPopulation, ADMINISTRATOR_USER_ID);

        Report report = buildTabularReport(positionPopulation);
        reportService.create(report);
        Report actualReport = (Report) reportService.findById(report.getId());
        Collection positions = populationEngine.find(actualReport.getDefaultPopulation(), new Long(0));

        assertNotNull(positions);
    }

    public void testFindAllMetricReports() throws Exception {

        // create a metric
        User user = getAdminUser(userService);
        Metric metric = buildSubjectMetric(user);
        metricService.create(metric);

        // create a private report using the all subject population
        PopulationDto allSubjects = analysisService.findAll(Node.SUBJECT_UNIT_TYPE_, new Long(0), null).iterator().next();
        Report newReport = buildMetricReport(allSubjects, metric);
        newReport.setAccessType(AccessType.PRIVATE_ACCESS.toString());
        reportService.create(newReport);

        ReportDto copy = createDTO(newReport);

        // use administrator id - should find it as it was created by this user
        Collection<ReportDto> metricReports = reportService.findAllMetricReports(ADMINISTRATOR_USER_ID);

        boolean reportFound = false;
        for (Iterator iterator = metricReports.iterator(); iterator.hasNext();) {
            ReportDto report = (ReportDto) iterator.next();
            if (report.getId().equals(newReport.getId())) {
                reportFound = true;
                assertEquals(report.getPopulationId(), newReport.getDefaultPopulation().getId());
                assertEquals(report.getPopulationType(), newReport.getPopulationType());
            }
            assertEquals(Report.METRIC_REPORT, report.getType());
        }
        assertTrue(reportFound);

        // use bogus user id - should not find as users can only view reports they have added or which are public
        final Long testUserId = new Long(-100);
        metricReports = reportService.findAllMetricReports(testUserId);
        assertFalse(metricReports.contains(copy));

        // make report public
        newReport.setAccessType(AccessType.PUBLIC_ACCESS.toString());
        reportService.update(newReport);

        // test user should now see the report
        metricReports = reportService.findAllMetricReports(testUserId);
        assertTrue(metricReports.contains(copy));
    }

    private void addReportToColumns(Collection<Column> positionColumns, final Report report) {
        CollectionUtils.transform(positionColumns, new Transformer() {
            public Object transform(Object input) {
                Column column = (Column) input;
                column.setReport(report);
                return column;
            }
        });
    }

    private Report buildTabularReport(Population population) throws TalentStudioException {
        User user = getAdminUser(userService);
        final Report report = new TabularReport("No of key position report title", "some description", AccessType.PUBLIC_ACCESS.toString());

        List<Column> positionColumns = (List) getPositionCoreColumns();
        addReportToColumns(positionColumns, report);
        report.setColumns(positionColumns);

        report.setLabel("test report");
        report.setDefaultPopulation(population);
        report.setPopulationType(population.getType());
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.TABULAR_REPORT);

        return report;
    }

    private Report buildCrossTabReport(PopulationDto population, Metric metric) throws Exception {
        User user = getAdminUser(userService);
        final CrossTabReport report = new CrossTabReport("No of subject title", "some description", AccessType.PUBLIC_ACCESS.toString());
        List columns = (List) getSubjectDiscreetCoreColumns();

        Column horizontalColumn = (Column) columns.get(0);
        Column verticalColumn = (Column) columns.get(1);

        report.setHorizontalColumn(horizontalColumn);
        report.setVerticalColumn(verticalColumn);
        report.setReportType(Report.CROSSTAB_REPORT);
        report.setDefaultPopulation(new Population(population.getId(), population.getType(), population.getLabel(), AccessType.PUBLIC_ACCESS.toString(), null, null, null));
        report.setDefaultMetric(metric);
        report.setUserId(ADMINISTRATOR_USER_ID);
        return report;
    }

    private Report buildMetricReport(PopulationDto population, Metric metric) throws Exception {
        User user = getAdminUser(userService);
        final Report report = new MetricReport("No of subject title", "some description", AccessType.PUBLIC_ACCESS.toString());

        // add a count column
        report.addColumn(new Column("Count", null, new Integer(0), null, Direction.NA_DIRECTION.toString()));
        if (metric != null) {
            Column column = new Column(metric.getLabel());
            column.setMetric(metric);
            report.addColumn(column);
        }

        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.METRIC_REPORT);
        report.setDefaultPopulation(new Population(population.getId(), population.getType(), population.getLabel(), AccessType.PUBLIC_ACCESS.toString(), null, null, null));
        return report;
    }

    private Metric buildSubjectMetric(User user) {
        Collection numericAttributes = getSubjectNumericColumnAttributes();
        Metric metric = new Metric("Metric Test One", IPopulationEngine.AVG, ((Column) numericAttributes.iterator().next()).getAttributeName());
        metric.setUserId(user.getId());
        return metric;
    }

    private Collection<Column> getSubjectNumericColumnAttributes() {
        Collection<Column> result = new ArrayList<Column>();
        result.add(new Column("Shoe size", "-334", DynamicAttribute.DA_TYPE_NUMBER));
        result.add(new Column("Height", "-335", DynamicAttribute.DA_TYPE_NUMBER));
        result.add(new Column("Age", "-336", DynamicAttribute.DA_TYPE_NUMBER));
        return result;
    }

    private Collection<Column> getPositionCoreColumns() {
        List<Column> result = new ArrayList<Column>();
        result.add(new Column("Title", "title", new Integer(0), DynamicAttribute.DA_TYPE_TEXTFIELD));
        result.add(new Column("Organisation Unit", "organisationUnit.label", new Integer(1), DynamicAttribute.DA_TYPE_TEXTFIELD));
        return result;
    }

    private Collection getSubjectDiscreetCoreColumns() throws Exception {
        Collection attributes = new ArrayList(dynamicAttributeService.getTypedAttributes(Node.SUBJECT_UNIT_TYPE_, DynamicAttribute.DA_TYPE_STRUCT));
        attributes.add(new DynamicAttribute(new Long(-15), "Test Discreet 1", DynamicAttribute.DA_TYPE_STRUCT));
        attributes.add(new DynamicAttribute(new Long(-16), "Test Discreet 2", DynamicAttribute.DA_TYPE_STRUCT));
        attributes.add(new DynamicAttribute(new Long(-17), "Test Discreet 3", DynamicAttribute.DA_TYPE_STRUCT));
        CollectionUtils.transform(attributes, new Transformer() {
            private int index = 0;

            public Object transform(Object input) {
                DynamicAttribute attribute = (DynamicAttribute) input;
                return new Column(attribute.getLabel(), attribute.getId().toString(), new Integer(index++), attribute.getType());
            }
        });
        return attributes;
    }

    public void testFindAllStandardReportsColumnColorDisplayable() throws Exception {
        LookupType gender = lookupManagerDao.findLookupType("GENDER");
        DynamicAttribute genderAttribute = new DynamicAttribute(null, "gender", DynamicAttribute.DA_TYPE_STRUCT, "P", false, true, false);
        genderAttribute.setRefersToType(gender);
        dynamicAttributeService.create(genderAttribute);
        Report report = new TabularReport("Test color type selection", "", "PUBLIC");
        Column column = new Column("gender", genderAttribute.getId().toString(), new Integer(0), DynamicAttribute.DA_TYPE_STRUCT);
        for (Iterator iterator = gender.getLookupValues().iterator(); iterator.hasNext();) {
            LookupValue lookupValue = (LookupValue) iterator.next();
            ColumnDisplayImage columnDisplayImage = new ColumnDisplayImage(lookupValue, "/images/ffffff.gif");
            column.addColumnDisplayImage(columnDisplayImage);
        }
        report.addColumn(column);
        report.setUserId(new Long(1));
        reportService.create(report);
        Report actual = (Report) reportService.findById(report.getId());
        List columns = actual.getColumns();
        assertEquals(1, columns.size());
        Column actualColumn = (Column) columns.iterator().next();
        assertTrue(actualColumn.getColumnDisplayImages() != null);
    }

    public void testFindAllCrossTabReports() throws Exception {

        final User user = getAdminUser(userService);

        Report report = new CrossTabReport("cross tab 1", null, AccessType.PUBLIC_ACCESS.toString());
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.CROSSTAB_REPORT);
        report.setDefaultPopulation(new Population(new Long(-2)));
        reportService.create(report);

        Report tabularReport = new TabularReport("tab 1", null, AccessType.PUBLIC_ACCESS.toString());
        tabularReport.setUserId(ADMINISTRATOR_USER_ID);
        tabularReport.setReportType(Report.TABULAR_REPORT);
        tabularReport.setDefaultPopulation(new Population(new Long(-2)));
        tabularReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));
        reportService.create(tabularReport);

        ReportDto copy = createDTO(report);
        ReportDto tabularCopy = createDTO(tabularReport);

        final Collection<ReportDto> crossTabReports = reportService.findAllCrossTabReports(user.getId());
        assertFalse(crossTabReports.isEmpty());

        assertTrue(crossTabReports.contains(copy));
        assertFalse(crossTabReports.contains(tabularCopy));

        for (ReportDto reportDto : crossTabReports) {
            assertEquals(Report.CROSSTAB_REPORT, reportDto.getType());
        }
    }

    public void testFindCompatibleReports() throws Exception {

        final User user = getAdminUser(userService);
        final Long creatorId = user.getId();

        // create 1 position population and 1 subject population
        final Population positionPopulation = new Population();
        positionPopulation.setLabel("position population");
        positionPopulation.setType(IPopulationEngine.P_POS_TYPE_);
        analysisService.create(positionPopulation, creatorId);

        final Population subjectPopulation = new Population();
        subjectPopulation.setLabel("subject population");
        subjectPopulation.setType(IPopulationEngine.P_SUB_TYPE_);
        analysisService.create(subjectPopulation, creatorId);

        // create 1 public subject crosstab report
        Report publicSubjectCrossTabReport = new CrossTabReport("public subject cross tab", null, AccessType.PUBLIC_ACCESS.toString());
        publicSubjectCrossTabReport.setDefaultPopulation(subjectPopulation);
        publicSubjectCrossTabReport.setPopulationType(subjectPopulation.getType());
        publicSubjectCrossTabReport.setUserId(ADMINISTRATOR_USER_ID);
        publicSubjectCrossTabReport.setReportType(Report.CROSSTAB_REPORT);
        publicSubjectCrossTabReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));
        reportService.create(publicSubjectCrossTabReport);

        ReportDto publicSubjectCrossTab = createDTO(publicSubjectCrossTabReport);

        // create 1 private subject tabular report
        Report privateSubjectReport = new TabularReport("private subject tab", null, AccessType.PRIVATE_ACCESS.toString());
        privateSubjectReport.setDefaultPopulation(subjectPopulation);
        privateSubjectReport.setPopulationType(subjectPopulation.getType());
        privateSubjectReport.setUserId(ADMINISTRATOR_USER_ID);
        privateSubjectReport.setReportType(Report.TABULAR_REPORT);
        privateSubjectReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));
        reportService.create(privateSubjectReport);

        ReportDto privateSubject = createDTO(privateSubjectReport);

        // create 1 public position tabular report
        Report publicPositionReport = new TabularReport("public position tab", null, AccessType.PUBLIC_ACCESS.toString());
        publicPositionReport.setDefaultPopulation(positionPopulation);
        publicPositionReport.setPopulationType(positionPopulation.getType());
        publicPositionReport.setUserId(ADMINISTRATOR_USER_ID);
        publicPositionReport.setReportType(Report.TABULAR_REPORT);
        publicPositionReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));
        reportService.create(publicPositionReport);

        ReportDto publicPosition = createDTO(publicPositionReport);

        // create 1 private position tabular report
        Report privatePositionReport = new TabularReport("private position tab", null, AccessType.PRIVATE_ACCESS.toString());
        privatePositionReport.setDefaultPopulation(positionPopulation);
        privatePositionReport.setPopulationType(positionPopulation.getType());
        privatePositionReport.setUserId(ADMINISTRATOR_USER_ID);
        privatePositionReport.setReportType(Report.TABULAR_REPORT);
        privatePositionReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));
        reportService.create(privatePositionReport);

        ReportDto privatePosition = createDTO(privatePositionReport);

        // create 1 public position crosstab report
        Report publicPositionCrossTabReport = new CrossTabReport("public position cross tab", null, AccessType.PUBLIC_ACCESS.toString());
        publicPositionCrossTabReport.setDefaultPopulation(positionPopulation);
        publicPositionCrossTabReport.setPopulationType(positionPopulation.getType());
        publicPositionCrossTabReport.setUserId(ADMINISTRATOR_USER_ID);
        publicPositionCrossTabReport.setReportType(Report.CROSSTAB_REPORT);
        publicPositionCrossTabReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));
        reportService.create(publicPositionCrossTabReport);

        ReportDto publicPositionCrossTab = createDTO(publicPositionCrossTabReport);

        // create 1 private position crosstab report
        Report privatePositionCrossTabReport = new CrossTabReport("private position cross tab", null, AccessType.PRIVATE_ACCESS.toString());
        privatePositionCrossTabReport.setDefaultPopulation(positionPopulation);
        privatePositionCrossTabReport.setPopulationType(positionPopulation.getType());
        privatePositionCrossTabReport.setUserId(ADMINISTRATOR_USER_ID);
        privatePositionCrossTabReport.setReportType(Report.CROSSTAB_REPORT);
        privatePositionCrossTabReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));
        reportService.create(privatePositionCrossTabReport);

        ReportDto privatePositionCrossTab = createDTO(privatePositionCrossTabReport);

        // get compatible reports - should return public position reports only (crosstab and tabular) but not self
        Collection<ReportDto> publicCompatibleReports = reportService.findCompatibleReports(publicPositionReport, creatorId);

        assertFalse(publicCompatibleReports.contains(publicPosition));
        assertFalse(publicCompatibleReports.contains(privatePosition));
        assertFalse(publicCompatibleReports.contains(privatePositionCrossTab));
        assertFalse(publicCompatibleReports.contains(publicSubjectCrossTab));
        assertFalse(publicCompatibleReports.contains(privateSubject));
        assertTrue(publicCompatibleReports.contains(publicPositionCrossTab));

        // get compatible reports - should return public and private position reports (crosstab and tabular) but not self
        Collection<ReportDto> privateCompatibleReports = reportService.findCompatibleReports(privatePositionCrossTabReport, creatorId);
        assertFalse(privateCompatibleReports.contains(privatePositionCrossTab));
        assertFalse(privateCompatibleReports.contains(publicSubjectCrossTab));
        assertFalse(privateCompatibleReports.contains(privateSubject));
        assertTrue(privateCompatibleReports.contains(publicPositionCrossTab));
        assertTrue(privateCompatibleReports.contains(publicPosition));
        assertTrue(privateCompatibleReports.contains(privatePosition));

        // get compatible reports as a different user - should return public position reports (crosstab and tabular) but not self
        // and no private reports as the private can never be seen by other users 
        Collection<ReportDto> compatibleReports = reportService.findCompatibleReports(privatePositionCrossTabReport, ROOT_USER_ID);
        assertFalse(compatibleReports.contains(privatePositionCrossTab));
        assertFalse(compatibleReports.contains(publicSubjectCrossTab));
        assertFalse(compatibleReports.contains(privatePosition));
        assertFalse(compatibleReports.contains(privateSubject));
        assertTrue(compatibleReports.contains(publicPositionCrossTab));
        assertTrue(compatibleReports.contains(publicPosition));

        // test with private report with no id
        Report unsavedReport = new CrossTabReport("private unsaved cross tab", null, AccessType.PRIVATE_ACCESS.toString());
        unsavedReport.setDefaultPopulation(positionPopulation);
        unsavedReport.setPopulationType(positionPopulation.getType());
        unsavedReport.setUserId(ADMINISTRATOR_USER_ID);
        unsavedReport.setReportType(Report.CROSSTAB_REPORT);
        unsavedReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));

        ReportDto unsaved = createDTO(unsavedReport);

        Collection<ReportDto> unsavedPrivateReports = reportService.findCompatibleReports(unsavedReport, creatorId);
        assertFalse(unsavedPrivateReports.contains(unsaved));
        assertFalse(unsavedPrivateReports.contains(publicSubjectCrossTab));
        assertFalse(unsavedPrivateReports.contains(privateSubject));
        assertTrue(unsavedPrivateReports.contains(publicPositionCrossTab));
        assertTrue(unsavedPrivateReports.contains(privatePositionCrossTab));
        assertTrue(unsavedPrivateReports.contains(publicPosition));
        assertTrue(unsavedPrivateReports.contains(privatePosition));

        // test with public report with no id
        Report unsavedPublicReport = new CrossTabReport("public unsaved cross tab", null, AccessType.PUBLIC_ACCESS.toString());
        unsavedPublicReport.setDefaultPopulation(positionPopulation);
        unsavedPublicReport.setPopulationType(positionPopulation.getType());
        unsavedPublicReport.setUserId(ADMINISTRATOR_USER_ID);
        unsavedPublicReport.setReportType(Report.CROSSTAB_REPORT);
        unsavedPublicReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));

        ReportDto unsavedPublic = createDTO(unsavedPublicReport);

        Collection<ReportDto> unsavedPubReports = reportService.findCompatibleReports(unsavedPublicReport, creatorId);
        assertFalse(unsavedPubReports.contains(unsavedPublic));
        assertFalse(unsavedPubReports.contains(publicSubjectCrossTab));
        assertFalse(unsavedPubReports.contains(privateSubject));
        assertFalse(unsavedPubReports.contains(privatePosition));
        assertFalse(unsavedPubReports.contains(privatePositionCrossTab));
        assertTrue(unsavedPubReports.contains(publicPositionCrossTab));
        assertTrue(unsavedPubReports.contains(publicPosition));
    }

    private ReportDto createDTO(Report report) {
        Population defaultPopulation = report.getDefaultPopulation();
        if (defaultPopulation == null) defaultPopulation = new Population();
        return new ReportDto(report.getLabel(), report.getDescription(),
                defaultPopulation.getLabel(),
                report.getReportType(), report.getAccessType(), report.getId(),
                defaultPopulation.getId(),
                report.getPopulationType());
    }

    public void testFindAllStandardTabReports() throws Exception {

        final User user = getAdminUser(userService);

        Report report = new CrossTabReport("cross tab 1", null, AccessType.PUBLIC_ACCESS.toString());
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.CROSSTAB_REPORT);
        report.setDefaultPopulation(new Population(new Long(-2)));
        reportService.create(report);

        ReportDto reportDto = createDTO(report);

        Report tabularReport = new TabularReport("tab 1", null, AccessType.PUBLIC_ACCESS.toString());
        tabularReport.setUserId(ADMINISTRATOR_USER_ID);
        tabularReport.setReportType(Report.TABULAR_REPORT);
        tabularReport.setDefaultPopulation(new Population(new Long(-2)));
        tabularReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));

        reportService.create(tabularReport);

        ReportDto tabular = createDTO(tabularReport);

        final Collection<ReportDto> standardReports = reportService.findAllStandardReports(user.getId());
        assertFalse(standardReports.isEmpty());

        assertFalse(standardReports.contains(reportDto));
        assertTrue(standardReports.contains(tabular));

        for (ReportDto standardReport : standardReports) {
            assertEquals(Report.TABULAR_REPORT, standardReport.getType());
        }
    }

    public void testFindAll() throws Exception {

        final User user = getAdminUser(userService);

        Report report = new CrossTabReport("cross tab 1", null, AccessType.PUBLIC_ACCESS.toString());
        report.setUserId(ADMINISTRATOR_USER_ID);
        report.setReportType(Report.CROSSTAB_REPORT);
        report.setDefaultPopulation(new Population(new Long(-2)));
        reportService.create(report);

        ReportDto reportDto = createDTO(report);

        final Long userId = user.getId();
        final Collection<ReportDto> metricReports = reportService.findAll(Report.METRIC_REPORT, userId);
        assertFalse(metricReports.contains(reportDto));

        final Collection<ReportDto> reports = reportService.findAll(report.getReportType(), userId);
        assertTrue(reports.contains(reportDto));
    }

    public void testFindReports() throws Exception {
        final User user = getAdminUser(userService);
        Report tabularReport = new TabularReport("tab 1", null, AccessType.PUBLIC_ACCESS.toString());
        tabularReport.setUserId(ADMINISTRATOR_USER_ID);
        tabularReport.setReportType(Report.TABULAR_REPORT);
        tabularReport.setDefaultPopulation(new Population(new Long(-2)));
        tabularReport.setPersonal(true);
        tabularReport.addColumn(new Column("test", "label", new Integer(0), "TEXT", "N/A"));

        reportService.create(tabularReport);

        List<ReportDto> results = reportService.findReports(user.getId(), new String[]{Report.TABULAR_REPORT}, false);
        assertEquals(1, results.size());

        results = reportService.findReports(user.getId(), new String[]{Report.TABULAR_REPORT, Report.CHART_REPORT}, true);
        assertEquals(1, results.size());
    }

    private IReportService reportService;
    private IAnalysisService analysisService;
    private IUserService userService;
    private IPopulationEngine populationEngine;
    private ILookupManagerDao lookupManagerDao;
    private IDynamicAttributeService dynamicAttributeService;
    private IMetricService metricService;
    private IArenaManager arenaManager;
}
