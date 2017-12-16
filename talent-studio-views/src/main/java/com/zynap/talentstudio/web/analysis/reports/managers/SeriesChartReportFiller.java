package com.zynap.talentstudio.web.analysis.reports.managers;

import net.sf.jasperreports.engine.JRDataSource;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.jasper.JasperDataSourceFactory;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderCategoryURLGenerator;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.SpiderChartPostProcessor;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.SpiderChartProducer;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledSeriesChartReport;
import com.zynap.talentstudio.web.analysis.reports.data.FilledSpiderChartReport;
import com.zynap.talentstudio.web.organisation.ChartPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 31-Jan-2006
 * Time: 11:49:21
 */
public class SeriesChartReportFiller {

	public FilledReport fillReport(ChartReport chartReport, Map<Questionnaire, ChartPoint> seriesChartReportAnswers) {
		return new FilledSeriesChartReport(chartReport, seriesChartReportAnswers);
	}
}