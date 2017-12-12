/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.data;

import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.web.organisation.ChartPoint;

import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Apr-2011 09:43:15
 */
public class FilledSeriesChartReport extends FilledReport {

    public FilledSeriesChartReport(ChartReport chartReport, Map<Questionnaire, ChartPoint> seriesChartReportAnswers) {
    	super(null);
    	this.report = chartReport;
    	this.seriesChartReportAnswers =seriesChartReportAnswers;
	}

    public boolean isPieChart() {
        return false;
    }

    public boolean isSeriesChart() {
        return true;
    }

	public Map<Questionnaire, ChartPoint> getSeriesChartReportAnswers() {
		return seriesChartReportAnswers;
	}

	public ChartReport getReport() {
		return report;
	}

	private final Map<Questionnaire, ChartPoint> seriesChartReportAnswers;
	private final ChartReport report;
}
