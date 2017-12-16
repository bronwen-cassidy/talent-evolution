/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.data;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.web.organisation.ChartPoint;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	public FilledSeriesChartReport(ChartReport chartReport, Map<Questionnaire, ChartPoint> seriesChartReportAnswers, DynamicAttribute xAxisAttribute, DynamicAttribute yAxisAttribute) {
		super(null);
		this.report = chartReport;
		this.seriesChartReportAnswers = seriesChartReportAnswers;
		this.xAxisColumn = report.getXAxisColumn();
		this.yAxisColumn = report.getYAxisColumn();
		this.xAxisAttribute = xAxisAttribute;
		this.yAxisAttribute = yAxisAttribute;
		this.xAxisRange = buildXAxisRange();
		this.yAxisRange = buildYAxisRange();

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

	public String getXAxisLabel() {
		return xAxisColumn.getLabel();
	}

	public String getYAxisLabel() {
		return yAxisColumn.getLabel();
	}

	public Column getxAxisColumn() {
		return xAxisColumn;
	}

	public Column getyAxisColumn() {
		return yAxisColumn;
	}

	public String getxAxisRange() {
		return xAxisRange;
	}

	public String getyAxisRange() {
		return yAxisRange;
	}

	private String buildXAxisRange() {
		if (xAxisAttribute.isEnumerationType()) {
			return xAxisAttribute.getRefersToType().getConcatenatedActiveLookupValues();
		}

		List<String> results = new ArrayList<>();

		for (ChartPoint chartPoint : seriesChartReportAnswers.values()) {
			results.add(chartPoint.getXValue());
		}

		sortRange(results, xAxisAttribute.isNumericType());
		return StringUtils.arrayToCommaDelimitedString(results.toArray());
	}

	private String buildYAxisRange() {

		// need the dynamicAttribute 
		if (yAxisAttribute.isEnumerationType()) {
			return yAxisAttribute.getRefersToType().getConcatenatedActiveLookupValues();
		}

		List<String> results = new ArrayList<>();

		for (ChartPoint chartPoint : seriesChartReportAnswers.values()) {
			results.add(chartPoint.getYValue());
		}

		sortRange(results, yAxisAttribute.isNumericType());
		return StringUtils.arrayToCommaDelimitedString(results.toArray());
	}

	private void sortRange(List<String> results, final boolean numericType) {
		Collections.sort(results, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1 == null) o1 = "0";
				if (numericType) {
					return new Long(o1).compareTo(new Long(o2));
				}
				return o1.compareTo(o2);
			}
		});
	}


	private final Map<Questionnaire, ChartPoint> seriesChartReportAnswers;
	private final ChartReport report;
	private final Column xAxisColumn;
	private final Column yAxisColumn;
	private final DynamicAttribute xAxisAttribute;
	private final DynamicAttribute yAxisAttribute;
	private String xAxisRange;
	private String yAxisRange;
}
