/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.ChartReportAttribute;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.chart.ChartDataStructure;
import com.zynap.talentstudio.analysis.reports.chart.ChartSlice;
import com.zynap.talentstudio.dashboard.DashboardChartValue;
import com.zynap.talentstudio.dashboard.DashboardItem;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.analysis.populations.PopulationUtils;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.BarChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.PieChartProducer;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.managers.ChartReportFiller;
import com.zynap.talentstudio.web.analysis.reports.managers.ProgressReportFiller;
import com.zynap.talentstudio.web.analysis.reports.managers.SpiderChartReportFiller;
import com.zynap.talentstudio.web.analysis.reports.managers.TabularReportFiller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 25-May-2010 18:19:03
 */
public class SubjectDashboardBuilder implements Serializable {


    public void setChartReportFiller(ChartReportFiller chartReportFiller) {
        this.chartReportFiller = chartReportFiller;
    }

    public void setTabularReportFiller(TabularReportFiller tabularReportFiller) {
        this.tabularReportFiller = tabularReportFiller;
    }

    public void setProgressReportFiller(ProgressReportFiller progressReportFiller) {
        this.progressReportFiller = progressReportFiller;
    }

    public void setSpiderChartReportFiller(SpiderChartReportFiller spiderChartReportFiller) {
        this.spiderChartReportFiller = spiderChartReportFiller;
    }

    public void buildDashboardItem(SubjectDashboardWrapper dw, Subject subject, DashboardItem dashboardItem, IPopulationEngine populationEngine, boolean personal) throws TalentStudioException {
        dw.setDashboardItem(dashboardItem);
        List<Subject> nodes = new ArrayList<Subject>();
        nodes.add(subject);
        List<Long> nodeIds = new ArrayList<Long>();
        nodeIds.add(subject.getId());

        final Report report = dashboardItem.getReport();
        Population population = PopulationUtils.createPersonPopulation(populationEngine, new String[]{subject.getId().toString()});

        if (report.isChartReport()) {
            ChartReport chartReport = (ChartReport) report;
            if (chartReport.getChartType().equals(ChartReport.SPIDER_CHART)) {
                List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
                Map<Long, QuestionAttributeValuesCollection> answers = null;
                if (!questionnaireAttributes.isEmpty()) {
                    answers = populationEngine.findQuestionnaireAnswers(questionnaireAttributes, population, IDomainObject.ROOT_USER_ID);
                }
                final FilledReport filledReport = spiderChartReportFiller.fillReport(report, IDomainObject.ROOT_USER_ID, nodes, answers);
                dw.setFilledReport(filledReport);
            } else {
                List<NodeExtendedAttribute> answers = getChartReportAnswers(subject, populationEngine, personal, report, population);
                final FilledReport filledReport = chartReportFiller.fillReport(report, nodes, answers);
                dw.setFilledReport(filledReport);
                if (dashboardItem.hasChartValues()) {
                    buildExpected(dw, dashboardItem, report);
                }
            }
        } else {
            List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
            Map<Long, QuestionAttributeValuesCollection> answers = new HashMap<Long, QuestionAttributeValuesCollection>();
            if (!questionnaireAttributes.isEmpty()) {
                if (personal) {
                    answers = populationEngine.findPersonalQuestionnaireAnswers(questionnaireAttributes, subject, IDomainObject.ROOT_USER_ID);
                } else {
                    answers = populationEngine.findQuestionnaireAnswers(questionnaireAttributes, nodeIds, IDomainObject.ROOT_USER_ID);
                }
            }
            FilledReport filledReport;
            if (report.isTabularReport()) {
                filledReport = tabularReportFiller.fillReport(report, nodes, answers, null, null, 0, false);
            } else {
                filledReport = progressReportFiller.fillReport(report, subject, answers);
            }
            dw.setFilledReport(filledReport);
        }
    }

    private List<NodeExtendedAttribute> getChartReportAnswers(Subject subject, IPopulationEngine populationEngine, boolean personal, Report report, Population population) throws TalentStudioException {
        List<AnalysisParameter> attributes = buildParameters(report);
        return getAttributes(subject, populationEngine, personal, population, attributes);
    }

    private List<NodeExtendedAttribute> getAttributes(Subject subject, IPopulationEngine populationEngine, boolean personal, Population population, List<AnalysisParameter> attributes) throws TalentStudioException {
        List<NodeExtendedAttribute> answers;
        if (personal) {
            answers = populationEngine.findPersonalQuestionnaireAttributeAnswers(attributes, subject, IDomainObject.ROOT_USER_ID);
        } else {
            answers = populationEngine.findQuestionnaireAnswers(population, IDomainObject.ROOT_USER_ID, attributes);
        }
        return answers;
    }

    private List<AnalysisParameter> buildParameters(Report report) {
        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();
        for (ChartReportAttribute attr : ((ChartReport) report).getChartReportAttributes()) {
            attributes.add(attr.getAnalysisParameter());
        }
        return attributes;
    }

    private void buildExpected(SubjectDashboardWrapper dw, DashboardItem dashboardItem, Report report) {
        // build the expected values chart
        Map<String, ChartSlice> expectedResults = new LinkedHashMap<String, ChartSlice>();
        final Set<DashboardChartValue> values = dashboardItem.getDashboardChartValues();
        for (DashboardChartValue chartValue : values) {
            final ChartSlice value = new ChartSlice(chartValue.getColumn());
            value.setCount(chartValue.getExpectedValue().intValue());
            expectedResults.put(chartValue.getColumn().getLabel(), value);
        }

        ChartDataStructure cds = new ChartDataStructure(expectedResults, report);
        AbstractChartProducer producer;
        if (((ChartReport) report).isPieChartType()) {
            producer = new PieChartProducer(cds);
            ((PieChartProducer) producer).setShowNumbers(false);
        } else {
            producer = new BarChartProducer(cds);
            ((BarChartProducer) producer).setShowNumbers(false);
        }

        dw.setExpectedProducer(producer);
    }

    private ChartReportFiller chartReportFiller;
    private TabularReportFiller tabularReportFiller;
    private ProgressReportFiller progressReportFiller;
    private SpiderChartReportFiller spiderChartReportFiller;

}

