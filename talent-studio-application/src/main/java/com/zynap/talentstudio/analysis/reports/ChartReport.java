/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.talentstudio.analysis.AnalysisParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 09-Apr-2010 20:49:55
 */
public class ChartReport extends Report {

	public ChartReport() {
	}

	public ChartReport(Long id) {
		super(id);
	}

	public List<ChartReportAttribute> getChartReportAttributes() {
		return chartReportAttributes;
	}

	public void setChartReportAttributes(List<ChartReportAttribute> chartReportAttributes) {
		this.chartReportAttributes = chartReportAttributes;
	}

	public void addQuestionnaireWorkflow(ChartReportAttribute questionnaireWorkflow) {
		this.chartReportAttributes.add(questionnaireWorkflow);
	}

	public void addChartReportAttribute(ChartReportAttribute chartReportAttribute) {
		chartReportAttribute.setReport(this);
		chartReportAttribute.setPosition(chartReportAttributes.size());
		chartReportAttributes.add(chartReportAttribute);
	}

	public boolean isChartReport() {
		return true;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public boolean isPieChartType() {
		return PIE_CHART.equals(chartType);
	}

	public boolean isSpiderChartType() {
		return SPIDER_CHART.equals(chartType);
	}

	public boolean isSeriesChartType() {
		return SERIES_CHART.equals(chartType);
	}

	public List<AnalysisParameter> getQuestionnaireAttributes() {
		List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();
		if (isSpiderChartType()) {
			for (Column column : getColumns()) {
				for (ChartColumnAttribute attribute : column.getChartColumnAttributes()) {
					if (attribute.getAnalysisParameter().isQuestionnaireAttribute()) {
						attributes.add(attribute.getAnalysisParameter());
					}
				}
			}
		} else {
			for (ChartReportAttribute attribute : chartReportAttributes) {
				if (attribute.getAnalysisParameter().isQuestionnaireAttribute()) {
					attributes.add(attribute.getAnalysisParameter());
				}
			}
		}
		return attributes;
	}

	public Map<String, AnalysisParameter> getXYChartAttributes() {
		Map<String, AnalysisParameter> attributes = new HashMap<>();
		for (Column column : getColumns()) {
			attributes.put(column.getColumnSource(), column.getAnalysisParameter());
		}
		return attributes;
	}

	public Column getXAxisColumn() {
		for (Column column : getColumns()) {
			if(Column.X_AXIS_SOURCE.equals(column.getColumnSource())) {
				return column;
			}
		}
		return null;
	}

	public Column getYAxisColumn() {
		for (Column column : getColumns()) {
			if(Column.Y_AXIS_SOURCE.equals(column.getColumnSource())) {
				return column;
			}
		}
		return null;
	}

	/* used in chart reports, this is the list of forms that the answer count will be determined from */
	private List<ChartReportAttribute> chartReportAttributes = new ArrayList<ChartReportAttribute>();
	private String chartType;


	public static final String BAR_CHART = "BAR";
	public static final String PIE_CHART = "PIE";
	public static final String SPIDER_CHART = "SPIDER";
	public static final String SERIES_CHART = "SERIES";

}
