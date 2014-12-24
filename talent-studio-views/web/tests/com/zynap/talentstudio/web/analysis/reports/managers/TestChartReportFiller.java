/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.managers;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 25-May-2010 09:17:40
 * @version 0.1
 */

import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.ChartReportAttribute;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.chart.ChartDataStructure;
import com.zynap.talentstudio.analysis.reports.chart.ChartSlice;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;
import com.zynap.talentstudio.web.analysis.reports.data.ChartFilledReport;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestChartReportFiller extends ZynapDbUnitMockControllerTestCase {

    protected String getDataSetFileName() {
        return "chart-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        chartReportFiller = (ChartReportFiller) getBean("chartReportFiller");
        populationEngine = (IPopulationEngine) getBean("populationEngine");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        chartReportFiller = null;
        populationEngine = null;
    }

    public void testFillReport() throws Exception {
        // get the report
        IReportService reportService = (IReportService) getBean("reportService");
        ChartReport report = (ChartReport) reportService.findById(new Long(352));
        Population population = report.getDefaultPopulation();
        List<SubjectDTO> subjects = populationEngine.findSubjects(population, ROOT_USER_ID);
        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();

        for (ChartReportAttribute attr : report.getChartReportAttributes()) {
            attributes.add(attr.getAnalysisParameter());
        }
        final List<NodeExtendedAttribute> answers = populationEngine.findQuestionnaireAnswers(population, ROOT_USER_ID, attributes);
        ChartFilledReport filledReport = (ChartFilledReport) chartReportFiller.fillReport(report, subjects, answers);

        final ChartDataStructure chartDataStructure = filledReport.getChartDataStructure();
        Map<String, ChartSlice> qAnswers = chartDataStructure.getResults();

        // high, medium and low
        assertEquals(3, qAnswers.keySet().size());
        assertEquals(4, qAnswers.get("High").getCount());
        assertEquals(4, qAnswers.get("Medium").getCount());
        assertEquals(9, qAnswers.get("Low").getCount());
    }

    public void testFillReportLastLineItem() throws Exception {
        // get the report
        IReportService reportService = (IReportService) getBean("reportService");
        ChartReport report = (ChartReport) reportService.findById(new Long(352));
        report.setLastLineItem(true);

        Population population = report.getDefaultPopulation();
        List<SubjectDTO> subjects = populationEngine.findSubjects(population, ROOT_USER_ID);
        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();

        for (ChartReportAttribute attr : report.getChartReportAttributes()) {
            attributes.add(attr.getAnalysisParameter());
        }
        final List<NodeExtendedAttribute> answers = populationEngine.findQuestionnaireAnswers(population, ROOT_USER_ID, attributes);
        ChartFilledReport filledReport = (ChartFilledReport) chartReportFiller.fillReport(report, subjects, answers);

        final ChartDataStructure chartDataStructure = filledReport.getChartDataStructure();
        Map<String, ChartSlice> qAnswers = chartDataStructure.getResults();

        // high, medium and low
        assertEquals(3, qAnswers.keySet().size());
        assertEquals(2, qAnswers.get("High").getCount());
        assertEquals(2, qAnswers.get("Medium").getCount());
        assertEquals(4, qAnswers.get("Low").getCount());
    }

    public void testFillReportLastLineItem_OnePerson() throws Exception {
        // get the report
        IReportService reportService = (IReportService) getBean("reportService");
        ChartReport report = (ChartReport) reportService.findById(new Long(352));
        report.setLastLineItem(true);

        Population population = report.getDefaultPopulation();
        List<SubjectDTO> subjects = populationEngine.findSubjects(population, ROOT_USER_ID);
        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();

        for (ChartReportAttribute attr : report.getChartReportAttributes()) {
            attributes.add(attr.getAnalysisParameter());
        }
        final List<NodeExtendedAttribute> answers = populationEngine.findQuestionnaireAnswers(population, ROOT_USER_ID, attributes);
        ChartFilledReport filledReport = (ChartFilledReport) chartReportFiller.fillReport(report, subjects, answers);

        final ChartDataStructure chartDataStructure = filledReport.getChartDataStructure();
        Map<String, ChartSlice> qAnswers = chartDataStructure.getResults();

        // high, medium and low
        assertEquals(3, qAnswers.keySet().size());
        assertEquals(2, qAnswers.get("High").getCount());
        assertEquals(2, qAnswers.get("Medium").getCount());
        assertEquals(4, qAnswers.get("Low").getCount());
    }

    private ChartReportFiller chartReportFiller;
    private IPopulationEngine populationEngine;
}
