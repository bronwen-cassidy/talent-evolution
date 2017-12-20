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
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.chart.ChartDataStructure;
import com.zynap.talentstudio.analysis.reports.chart.ChartSlice;
import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.dashboard.DashboardChartValue;
import com.zynap.talentstudio.dashboard.DashboardItem;
import com.zynap.talentstudio.dashboard.IDashboardService;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflowDTO;
import com.zynap.talentstudio.web.analysis.populations.PopulationUtils;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.BarChartProducer;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.PieChartProducer;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.managers.ChartReportFiller;
import com.zynap.talentstudio.web.analysis.reports.managers.ProgressReportFiller;
import com.zynap.talentstudio.web.analysis.reports.managers.SeriesChartReportFiller;
import com.zynap.talentstudio.web.analysis.reports.managers.SpiderChartReportFiller;
import com.zynap.talentstudio.web.analysis.reports.managers.TabularReportFiller;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.zynap.talentstudio.organisation.attributes.DynamicAttribute.PUBLISHED_DATE_ATTR;

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

	public void buildDashboardItem(SubjectDashboardWrapper dw, Subject subject, DashboardItem dashboardItem, IPopulationEngine populationEngine,
	                               IQueWorkflowService queWorkflowService, IQuestionnaireService questionnaireService, IDynamicAttributeService dynamicAttributeService,
	                               boolean personal) throws TalentStudioException {
		dw.setDashboardItem(dashboardItem);
		List<Subject> nodes = new ArrayList<>();
		nodes.add(subject);
		List<Long> nodeIds = new ArrayList<>();
		nodeIds.add(subject.getId());

		final Report report = dashboardItem.getReport();
		Population population = PopulationUtils.createPersonPopulation(populationEngine, new String[]{subject.getId().toString()});

		if (report.isChartReport()) {
			ChartReport chartReport = (ChartReport) report;
			switch (chartReport.getChartType()) {
				case ChartReport.SPIDER_CHART: {
					List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
					Map<Long, QuestionAttributeValuesCollection> answers = null;
					if (!questionnaireAttributes.isEmpty()) {
						answers = populationEngine.findQuestionnaireAnswers(questionnaireAttributes, population, IDomainObject.ROOT_USER_ID);
					}
					final FilledReport filledReport = spiderChartReportFiller.fillReport(report, IDomainObject.ROOT_USER_ID, nodes, answers);
					dw.setFilledReport(filledReport);
					break;
				}
				case ChartReport.SERIES_CHART: {
					// find all the answers from all the que workflows where the parent = workflowId
					Map<String, List<Column>> xyChartAttributes = chartReport.getXYChartAttributes();

					final Column xAxisColumn = xyChartAttributes.get(Column.X_AXIS_SOURCE).iterator().next();
					DynamicAttribute dynamicAttribute = dynamicAttributeService.findById(xAxisColumn.getDynamicAttributeId());
					final List<Series> seriesChartReportAnswers = getSeriesChartReportAnswers(subject, xyChartAttributes, queWorkflowService, 
							questionnaireService, xAxisColumn);

					SeriesChartReportFiller filler = new SeriesChartReportFiller();
					FilledReport filledReport = filler.fillReport(chartReport, seriesChartReportAnswers, dynamicAttribute, xAxisColumn);
					dw.setFilledReport(filledReport);
					break;
				}
				default: {
					List<NodeExtendedAttribute> answers = getChartReportAnswers(subject, populationEngine, personal, report, population);
					final FilledReport filledReport = chartReportFiller.fillReport(report, nodes, answers);
					dw.setFilledReport(filledReport);
					if (dashboardItem.hasChartValues()) {
						buildExpected(dw, dashboardItem, report);
					}
					break;
				}
			}
		} else {
			List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
			Map<Long, QuestionAttributeValuesCollection> answers = new HashMap<>();
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
		return getAnswers(subject, populationEngine, personal, population, attributes);
	}

	private List<Series> getSeriesChartReportAnswers(Subject subject, Map<String, List<Column>> xyChartAttributes, IQueWorkflowService queWorkflowService,
	                                                 IQuestionnaireService questionnaireService, Column xAxisParameter) throws TalentStudioException {

		List<Column> yAxisColumns = xyChartAttributes.get(Column.Y_AXIS_SOURCE);

		// always the same workflow  - there is only one
		List<QuestionnaireWorkflowDTO> workflows = findWorkflows(xAxisParameter.getAnalysisParameter(), queWorkflowService);
		
		List<Series> result = new ArrayList<>();
		//t odo need to find the published date attribute for each of the workflows
		for (Column yAxisColumn : yAxisColumns) {
			
			Series series = new Series(yAxisColumn);
			
			// workflows represent x axis
			for (QuestionnaireWorkflowDTO workflow : workflows) {

				final Collection<Questionnaire> questionnaires = questionnaireService.findQuestionnaires(workflow.getId(), subject.getId());

				if (!questionnaires.isEmpty()) {
					Questionnaire q = questionnaires.iterator().next();
					AttributeWrapperBean yAnswer = getAttributeWrapperBean(yAxisColumn, q); 
					 
					AttributeWrapperBean xAnswer;
					if (DynamicAttribute.PUBLISHED_DATE_DA_ID.equals(xAxisParameter.getDynamicAttributeId())) {
						xAnswer = new AttributeWrapperBean(AttributeValue.create(workflow.getCreatedDate().toString(), PUBLISHED_DATE_ATTR));
					} else {
						xAnswer = getAttributeWrapperBean(xAxisParameter, q);
					}
					series.add(yAnswer, xAnswer);
				}
			}
			result.add(series);
		}
		return result;
	}

	private AttributeWrapperBean getAttributeWrapperBean(Column axisColumn, Questionnaire q) {
		AttributeValue value = q.getDynamicAttributeValues().get(new DynamicAttribute(axisColumn.getDynamicAttributeId(), axisColumn.getLabel(), axisColumn.getColumnType()));
		AttributeWrapperBean answer = null;
		if (value != null) {
			answer = new AttributeWrapperBean(value);
		}
		return answer;
	}

	private List<QuestionnaireWorkflowDTO> findWorkflows(AnalysisParameter analysisParameter, IQueWorkflowService queWorkflowService) {
		final Long questionnaireWorkflowId = analysisParameter.getQuestionnaireWorkflowId();
		List<QuestionnaireWorkflowDTO> workflows = queWorkflowService.findAllRelatedWorkflows(questionnaireWorkflowId);
		return workflows;
	}

	private List<NodeExtendedAttribute> getAnswers(Subject subject, IPopulationEngine populationEngine, boolean personal, Population population, List<AnalysisParameter> attributes) throws TalentStudioException {
		List<NodeExtendedAttribute> answers;
		if (personal) {
			answers = populationEngine.findPersonalQuestionnaireAttributeAnswers(attributes, subject, IDomainObject.ROOT_USER_ID);
		} else {
			answers = populationEngine.findQuestionnaireAnswers(population, IDomainObject.ROOT_USER_ID, attributes);
		}
		return answers;
	}

	private List<AnalysisParameter> buildParameters(Report report) {
		List<AnalysisParameter> attributes = new ArrayList<>();
		for (ChartReportAttribute attr : ((ChartReport) report).getChartReportAttributes()) {
			attributes.add(attr.getAnalysisParameter());
		}
		return attributes;
	}

	private void buildExpected(SubjectDashboardWrapper dw, DashboardItem dashboardItem, Report report) {
		// build the expected values chart
		Map<String, ChartSlice> expectedResults = new LinkedHashMap<>();
		final Set<DashboardChartValue> values = dashboardItem.getDashboardChartValues();
		for (DashboardChartValue chartValue : values) {
			final ChartSlice value = new ChartSlice(chartValue.getColumn());
			value.setCount(chartValue.getExpectedValue());
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

	public Set<SubjectDashboardWrapper> buildSubjectDashboards(Subject subject, IDashboardService dashboardService, IPopulationEngine populationEngine,
	                                                           IQueWorkflowService queWorkflowService, IQuestionnaireService questionnaireService,
	                                                           IDynamicAttributeService dynamicAttributeService) throws TalentStudioException {
		List<Dashboard> dashboards = dashboardService.findPersonalDashboards(subject);
		Set<SubjectDashboardWrapper> subjectDashboardItems = new LinkedHashSet<>();
		if (!dashboards.isEmpty()) {
			for (Dashboard dashboard : dashboards) {
				final List<DashboardItem> dashboardItems = dashboard.getDashboardItems();
				for (DashboardItem dashboardItem : dashboardItems) {
					SubjectDashboardWrapper dw = new SubjectDashboardWrapper(dashboardItem.getId());
					if (!subjectDashboardItems.contains(dw)) {
						// build the info we need and add it if this is a chart we need the chart filler otherwsie we need the tabular filler
						buildDashboardItem(dw, subject, dashboardItem, populationEngine, queWorkflowService, questionnaireService, dynamicAttributeService, true);
						subjectDashboardItems.add(dw);
					}
				}
			}
		}
		return subjectDashboardItems;
	}

	private ChartReportFiller chartReportFiller;
	private TabularReportFiller tabularReportFiller;
	private ProgressReportFiller progressReportFiller;
	private SpiderChartReportFiller spiderChartReportFiller;

}

