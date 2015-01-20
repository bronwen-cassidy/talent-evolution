package com.zynap.talentstudio.web.analysis.reports.managers;

import net.sf.jasperreports.engine.JRDataSource;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.jasper.JasperDataSourceFactory;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderCategoryURLGenerator;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderChartPostProcessor;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.SpiderChartProducer;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledSpiderChartReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 31-Jan-2006
 * Time: 11:49:21
 */
public class SpiderChartReportFiller {

    public FilledReport fillReport(Report report, Long userId, List results, Map<Long, QuestionAttributeValuesCollection> answers) throws TalentStudioException {

        FilledSpiderChartReport filledReport = new FilledSpiderChartReport();

        JRDataSource dataSource = dataSourceFactory.getDataSource(report, results, answers, new ArrayList<GroupingAttribute>(), null, 0, new User(userId));
        SpiderChartProducer producer = new SpiderChartProducer(dataSource, (ChartReport) report, results);

        filledReport.setProducer(producer);
        filledReport.setURLGenerator(new SpiderCategoryURLGenerator());
        filledReport.setPostProcessor(new SpiderChartPostProcessor((ChartReport) report, producer.getDataSet()));

        return filledReport;
    }

    public void setDataSourceFactory(JasperDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    private JasperDataSourceFactory dataSourceFactory;
}