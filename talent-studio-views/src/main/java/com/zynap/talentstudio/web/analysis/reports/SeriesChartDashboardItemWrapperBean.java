package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.dashboard.DashboardItem;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.Pair;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SeriesChartDashboardItemWrapperBean implements Serializable {

	public SeriesChartDashboardItemWrapperBean(Long userId, String name) {
		this.dashboard = new Dashboard(Dashboard.PERSONAL_TYPE);
		this.userId = userId;
		this.name = name;
	}

	/**
	 * (note dashboard participant needs to be removed from the existing one for this user to prevent duplicate dashboards)
	 * or we need to select the dashboard items separately (may be better actually)
	 *
	 * @return a new dashbaord for the individual
	 */
	public Dashboard getModifiedDashboard(Report chartReport) {
		DashboardItem dashboardItem = new DashboardItem();
		dashboardItem.setReport(chartReport);
		dashboardItem.setLabel(chartLabel);
		dashboard.addDashboardItem(dashboardItem);
		dashboard.setLabel(chartLabel + " for " + name);
		return dashboard;
	}
	
	public Report getModifiedReport() {
		
		Column xAxisColumn = new Column(xAxisLabel, xAxisAttributeId, 0, findType(xAxisAttributeId), Column.X_AXIS_SOURCE);
		Column yAxisColumn = new Column(yAxisLabel, yAxisAttributeId, 1, findType(yAxisAttributeId), Column.Y_AXIS_SOURCE);
		xAxisColumn.setQuestionnaireWorkflowId(workflowId);
		yAxisColumn.setQuestionnaireWorkflowId(workflowId);
		
		ChartReport chartReport = new ChartReport();
		chartReport.setAccessType(AccessType.PRIVATE);
		chartReport.setChartType(ChartReport.SERIES_CHART);
		chartReport.setUserId(userId);
		chartReport.setLabel(chartLabel);
		chartReport.addColumn(xAxisColumn);
		chartReport.addColumn(yAxisColumn);
		int index = 2;
		for (Pair<String, String> item : series) {
			String attributeName = item.getKey();
			String value = item.getValue();
			if (StringUtils.hasText(attributeName) && StringUtils.hasText(value)) {
				Column column = new Column(value, attributeName, index++, findType(attributeName), Column.Y_AXIS_SOURCE);
				column.setQuestionnaireWorkflowId(workflowId);
				chartReport.addColumn(column);
			}
		}
		return chartReport;
	}

	public Long getUserId() {
		return userId;
	}

	public String getxAxisAttributeId() {
		return xAxisAttributeId;
	}

	public void setxAxisAttributeId(String xAxisAttributeId) {
		this.xAxisAttributeId = xAxisAttributeId;
	}

	public String getyAxisAttributeId() {
		return yAxisAttributeId;
	}

	public void setyAxisAttributeId(String yAxisAttributeId) {
		this.yAxisAttributeId = yAxisAttributeId;
	}

	public String getxAxisLabel() {
		return xAxisLabel;
	}

	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}

	public String getyAxisLabel() {
		return yAxisLabel;
	}

	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}
	
	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public void setAttributes(List<DynamicAttribute> attributes) {
		this.attributes = attributes;
	}
	
	public String getChartLabel() {
		return chartLabel;
	}

	public void setChartLabel(String chartLabel) {
		this.chartLabel = chartLabel;
	}

	public int addSeries() {
		int index = series.size();
		series.add(new Pair<String, String>());
		return index;
	}

	public List<Pair<String, String>> getSeries() {
		return series;
	}

	public void setSeries(List<Pair<String, String>> series) {
		this.series = series;
	}

	private String findType(String attributeId) {
		for (DynamicAttribute attribute : attributes) {
			if(String.valueOf(attribute.getId()).equals(attributeId)) {
				return attribute.getType();
			}
		}
		return DynamicAttribute.PUBLISHED_DATE_ATTR.getType();
	}

	private final Long userId;
	private final String name;

	private String xAxisAttributeId;
	private String yAxisAttributeId;
	private Dashboard dashboard;
	private String xAxisLabel;
	private String yAxisLabel;
	private String chartLabel;
	private Long workflowId;
	private List<Pair<String, String>> series = new ArrayList<>();

	private List<DynamicAttribute> attributes;
}
