package com.zynap.talentstudio.web.analysis.reports.support;

import net.sf.jasperreports.engine.JRDataSource;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.analysis.reports.jasper.JasperDataSourceFactory;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderCategoryURLGenerator;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderChartPostProcessor;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.SpiderChartProducer;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.views.RunSpiderChartReportWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 01-Mar-2006
 * Time: 10:21:00
 */
public final class SpiderChartReportRunner extends ReportRunner {

    public void run(RunReportWrapperBean wrapper, Long userId) throws TalentStudioException {

        final List<AnalysisParameter> questionnaireAttributes = wrapper.getNonAssociatedQuestionnaireAttributes();

        final Population population = wrapper.getPopulation();

        Map<Long, QuestionAttributeValuesCollection> answers = null;

        if (!questionnaireAttributes.isEmpty() && IPopulationEngine.P_SUB_TYPE_.equals(population.getType())) {
            answers = populationEngine.findQuestionnaireAnswers(questionnaireAttributes, population, userId);
        }

        final List results = populationEngine.find(population, userId);
        wrapper.setArtefacts(results);
        JRDataSource dataSource = dataSourceFactory.getDataSource(wrapper.getReport(), results, answers, new ArrayList<GroupingAttribute>(), null, 0, new User(userId));
        RunSpiderChartReportWrapper reportWrapper = (RunSpiderChartReportWrapper) wrapper;

        reportWrapper.setDataSource(dataSource);
        SpiderChartProducer producer = new SpiderChartProducer(dataSource, (ChartReport) wrapper.getReport(), results);
        reportWrapper.setChartProducer(producer);

        reportWrapper.setURLGenerator(new SpiderCategoryURLGenerator());
        reportWrapper.setPostProcessor(new SpiderChartPostProcessor((ChartReport) reportWrapper.getReport(), producer.getDataSet()));
    }

    public void setDataSourceFactory(JasperDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }


    private JasperDataSourceFactory dataSourceFactory;
}