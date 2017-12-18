/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.data;

import com.zynap.common.util.StringUtil;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.web.organisation.ChartPoint;

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
		buildAxisValues();
	}

	public boolean isPieChart() {
		return false;
	}

	public boolean isSeriesChart() {
		return true;
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

	public String getxAxisRange() {
		return xAxisRange;
	}

	public String getyAxisRange() {
		return yAxisRange;
	}

	public String getxAxisValues() {
		return xAxisValues;
	}

	public String getyAxisValues() {
		return yAxisValues;
	}

	private String buildXAxisRange() {
		if (xAxisAttribute.isEnumerationType()) {
			xAxisRangeValues = xAxisAttribute.getRefersToType().getConcatenatedActiveLookupValues();
			xAxisRangeValues.add(NO_ANSWER);
			return StringUtil.wrappedListToDelimitedString(xAxisRangeValues, ",", "'");
		}
		
		xAxisRangeValues = new ArrayList<>();
		for (ChartPoint chartPoint : seriesChartReportAnswers.values()) {
			String xValue = chartPoint.getXValue();
			if(!xAxisRangeValues.contains(xValue) && xValue != null) xAxisRangeValues.add(xValue);
		}
		
		sortRange(xAxisRangeValues, xAxisAttribute.isNumericType());
		if(xAxisRangeValues.isEmpty()) xAxisRangeValues.add(NO_ANSWER);
		return StringUtil.wrappedListToDelimitedString(xAxisRangeValues, ",", "'");
	}

	private String buildYAxisRange() {

		if (yAxisAttribute.isEnumerationType()) {
			yAxisRangeValues = yAxisAttribute.getRefersToType().getConcatenatedActiveLookupValues();
			yAxisRangeValues.add(NO_ANSWER);
			return StringUtil.wrappedListToDelimitedString(yAxisRangeValues, ",", "'");
		}
		
		yAxisRangeValues = new ArrayList<>();
		for (ChartPoint chartPoint : seriesChartReportAnswers.values()) {
			String yValue = chartPoint.getYValue();
			if(!yAxisRangeValues.contains(yValue) && yValue != null) yAxisRangeValues.add(yValue);
		}

		sortRange(yAxisRangeValues, yAxisAttribute.isNumericType());
		if(yAxisRangeValues.isEmpty()) yAxisRangeValues.add(NO_ANSWER);
		return StringUtil.wrappedListToDelimitedString(yAxisRangeValues, ",", "'");
	}

	private void buildAxisValues() {
		List<String> x = new ArrayList<>();
		List<String> y = new ArrayList<>();
		
		for (ChartPoint chartPoint : seriesChartReportAnswers.values()) {
			final String xValue = chartPoint.getXValue();
			final String yValue = chartPoint.getYValue();
			x.add(xValue != null ? xValue : NO_ANSWER);
			y.add(yValue != null ? yValue : NO_ANSWER);
		}
		x.add(NO_ANSWER);
		y.add(NO_ANSWER);

		yAxisValues = StringUtil.wrappedListToDelimitedString(y, ",", "'");
		xAxisValues = StringUtil.wrappedListToDelimitedString(x, ",", "'");
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

	private static final String NO_ANSWER = "No Answer";


	private final Map<Questionnaire, ChartPoint> seriesChartReportAnswers;
	private final ChartReport report;
	private final Column xAxisColumn;
	private final Column yAxisColumn;
	private final DynamicAttribute xAxisAttribute;
	private final DynamicAttribute yAxisAttribute;
	private String xAxisRange;
	private List<String> xAxisRangeValues;
	private String xAxisValues;
	private String yAxisRange;
	private List<String> yAxisRangeValues;
	private String yAxisValues;
}
