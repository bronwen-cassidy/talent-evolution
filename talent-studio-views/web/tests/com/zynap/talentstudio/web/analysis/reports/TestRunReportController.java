/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 17-Aug-2009 14:27:44
 * @version 0.1
 */

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.List;

public class TestRunReportController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected String getDataSetFileName() throws Exception {
        return "run_report_test_data.xml";
    }

    //public void testBuildChartDrillDown() throws Exception {
        // todo
    //}

    public void testSetSelectedPopulation() throws Exception {

        IReportService reportService = (IReportService) getBean("reportService");
        RunTabularController runTabularController = (RunTabularController) getBean("runTabularController");
        // make the wrapperBean
        Report report = (Report) reportService.findById(TAB_REPORT_ID);

        // we need compile the report
        reportService.compileReportDesign(report);

        assertNotNull(report.getJasperDefinition());

        RunReportWrapperBean reportWrapperBean = new RunReportWrapperBean(report);
        reportWrapperBean.setPopulationIds(TEST_POPULATION_IDS);

        runTabularController.setSelectedPopulation(reportWrapperBean);

        // test to see the criterias are correct
        final Population population = reportWrapperBean.getPopulation();
        // make sure we have the expected number of criteria
        final List<PopulationCriteria> populationCriterias = population.getPopulationCriterias();
        assertEquals(10, populationCriterias.size());
        for (int i = 0; i < populationCriterias.size(); i++) {
            PopulationCriteria criteria = populationCriterias.get(i);
            assertEquals(new Integer(i), criteria.getPosition());
            if (criteria.getId().equals(new Long(1772))) {
                assertEquals(IPopulationEngine.AND, criteria.getOperator());
            }
            if (criteria.getId().equals(new Long(1443))) {
                assertEquals(IPopulationEngine.AND, criteria.getOperator());
            }
        }

        // active only is the default behaviour
        assertEquals(Population.ALL_ACTIVE, population.getActiveCriteria());

        // label is correct
        String expectedLabel = "North West Aspiring DPH + Graduate Trainees - HR (2008 Intake) + WMLI Participants";
        assertEquals(expectedLabel, population.getLabel());

        // test we can run the populaiton through the compiler
        runTabularController.runReport(reportWrapperBean, new Long(0), mockRequest);
    }

    public void testSetSelectedPopulationOneOnly() throws Exception {

        IReportService reportService = (IReportService) getBean("reportService");
        RunTabularController runTabularController = (RunTabularController) getBean("runTabularController");
        // make the wrapperBean
        Report report = (Report) reportService.findById(TAB_REPORT_ID);

        // we need compile the report
        reportService.compileReportDesign(report);
        assertNotNull(report.getJasperDefinition());

        RunReportWrapperBean reportWrapperBean = new RunReportWrapperBean(report);
        reportWrapperBean.setPopulationIds(new Long[]{new Long(1904)});

        runTabularController.setSelectedPopulation(reportWrapperBean);

        // test to see the criterias are correct
        final Population population = reportWrapperBean.getPopulation();
        // make sure we have the expected number of criteria
        final List<PopulationCriteria> populationCriterias = population.getPopulationCriterias();
        assertEquals(1, populationCriterias.size());
        assertEquals(Population.ALL_ACTIVE, population.getActiveCriteria());

        // test we can run the populaiton through the compiler
        runTabularController.runReport(reportWrapperBean, ROOT_USER_ID, mockRequest);
    }

    /**
     * Test that verifies the drill down from a cross tab produces the correct extra criteria that is expected
     *
     * @throws Exception
     */
    //public void testFormBackingObjectDrillDown() throws Exception {
        //todo fail("Need to test drill downs from crosstabs return the correct population criterias for one population and for a combination");
    //}

    public void testRunReportSortByDate() throws Exception {
        // load and recompile the report
        IReportService reportService = (IReportService) getBean("reportService");
        RunTabularController runTabularController = (RunTabularController) getBean("runTabularController");
        // make the wrapperBean
        Report report = (Report) reportService.findById(TAB_REPORT_ID);
        reportService.compileReportDesign(report);
        RunReportWrapperBean reportWrapperBean = new RunReportWrapperBean(report);
        // the last column is the date one
        final List<Column> columns = report.getColumns();
        final Column column = columns.get(columns.size() - 1);
        reportWrapperBean.setOrderBy(column.getAttributeName());
        try {
            runTabularController.runReport(reportWrapperBean, ROOT_USER_ID, mockRequest);
            // assert the order of the nodes
            final List<? extends Node> results = reportWrapperBean.getPage().getResults();
            assertEquals(5, results.size());
            // the first node should br -10
            assertEquals(new Long(-10), results.get(0).getId());
            // last one -8
            assertEquals(new Long(-8), results.get(4).getId());
        } catch (Throwable e) {
            fail("No exception expected got: " + e);
        }
    }

    private final Long TAB_REPORT_ID = new Long(2083);
    private final Long[] TEST_POPULATION_IDS = new Long[]{new Long(1904), new Long(1448), new Long(1203)};

}