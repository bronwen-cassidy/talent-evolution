/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.views;

import de.laures.cewolf.DatasetProducer;
import net.sf.jasperreports.engine.JRDataSource;
import org.jfree.data.category.DefaultCategoryDataset;

import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.reports.ChartColumnAttribute;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.ChartColumnAttributeWrapper;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderCategoryURLGenerator;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderChartPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class used to view and then to trigger the run for spider reports.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Feb-2006 09:40:15
 */
public class RunSpiderChartReportWrapper extends RunReportWrapperBean {

    public RunSpiderChartReportWrapper(Report reportDefinition) {
        super(reportDefinition);
        initColumns(report);
    }

    protected void initColumns(Report report) {
        ChartReport cr = (ChartReport) report;
        columns = new ArrayList<AnalysisAttributeWrapperBean>();
        for (Column column : cr.getColumns()) {
            Set<ChartColumnAttribute> attrs = column.getChartColumnAttributes();
            for (ChartColumnAttribute attr : attrs) {
                columns.add(new ChartColumnAttributeWrapper(attr));
            }
        }
    }

    public boolean isResultsDisplayable() {
        return resultsDisplayable;
    }

    public void setResultsDisplayable(boolean resultsDisplayable) {
        this.resultsDisplayable = resultsDisplayable;
    }

    @Override
    public List<AnalysisParameter> getNonAssociatedQuestionnaireAttributes() {
        List<AnalysisParameter> questionnaireAttributes = new ArrayList<AnalysisParameter>();
        for (AnalysisAttributeWrapperBean column : columns) {
            questionnaireAttributes.add(column.getAnalysisParameter());
        }
        return questionnaireAttributes;
    }

    public void setDataSource(JRDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JRDataSource getDataSource() {
        return dataSource;
    }

    public void setChartProducer(DatasetProducer producer) {
        this.chartProducer = producer;
    }

    public DatasetProducer getChartProducer() {
        return chartProducer;
    }

    public void setURLGenerator(SpiderCategoryURLGenerator urlGenerator) {
        this.urlGenerator = urlGenerator;
    }

    public SpiderCategoryURLGenerator getUrlGenerator() {
        return urlGenerator;
    }

    public void setPostProcessor(SpiderChartPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public SpiderChartPostProcessor getPostProcessor() {
        return postProcessor;
    }

    private boolean resultsDisplayable = false;
    private JRDataSource dataSource;
    private DatasetProducer chartProducer;
    private SpiderCategoryURLGenerator urlGenerator;
    private SpiderChartPostProcessor postProcessor;
}