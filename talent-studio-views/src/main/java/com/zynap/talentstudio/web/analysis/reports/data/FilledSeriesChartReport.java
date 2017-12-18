/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.data;

import com.zynap.common.util.StringUtil;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.organisation.ChartPoint;
import com.zynap.talentstudio.web.organisation.Series;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Apr-2011 09:43:15
 */
public class FilledSeriesChartReport extends FilledReport {

	public FilledSeriesChartReport(ChartReport chartReport, List<Series> seriesChartReportAnswers, DynamicAttribute xAxisAttribute, Column xAxisColumn) {
		super(null);
		this.report = chartReport;
		this.seriesChartReportAnswers = seriesChartReportAnswers;
		this.xAxisAttribute = xAxisAttribute;
		this.xAxisColumn = xAxisColumn;
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

	public List<Series> getSeriesChartReportAnswers() {
		return seriesChartReportAnswers;
	}
	
	public String getxAxisRange() {
		if (xAxisAttribute.isEnumerationType()) {
			List<String> xAxisRangeValues = xAxisAttribute.getRefersToType().getConcatenatedActiveLookupValues();
			xAxisRangeValues.add(NO_ANSWER);
			return StringUtil.wrappedListToDelimitedString(xAxisRangeValues, ",", "'");
		}
		Series first = seriesChartReportAnswers.get(0);
		return first.getXAnswers();
	}

	public String getXAxisLabel() {
		return xAxisColumn.getLabel();
	}

	private static final String NO_ANSWER = "No Datar";

	private final List<Series> seriesChartReportAnswers;
	private final ChartReport report;
	private DynamicAttribute xAxisAttribute;
	private Column xAxisColumn;
}
