package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.dashboard.Dashboard;
import com.zynap.talentstudio.dashboard.DashboardItem;

import java.io.Serializable;

/**
 *
 */
public class SeriesChartDashboardItemWrapperBean implements Serializable {

	public SeriesChartDashboardItemWrapperBean(Long userId) {
		this.dashboard = new Dashboard(Dashboard.PERSONAL_TYPE);
		this.dashboard.setLabel("Individual Series Dashboard for " + userId);
		this.userId = userId;
	}

	/**
	 * todo this needs to clear all ids and create new clones as this is a new dashboard for this subject only
	 * (note dashboard participant needs to be removed from the existing one for this user to prevent duplicate dashboards)
	 * or we need to select the dashboard items separately (may be better actually)
	 *
	 * @return
	 */
	public Dashboard getModifiedDashboard(Report chartReport) {
		DashboardItem dashboardItem = new DashboardItem();
		dashboardItem.setReport(chartReport);
		dashboard.addDashboardItem(dashboardItem);
		
		return dashboard;
	}
	
	public Report getModifiedReport() {
		Column xAxisColumn = new Column(xAxisLabel, xAxisAttributeId, 0, xAxisType, "X-AXIS");
		xAxisColumn.setQuestionnaireWorkflowId(workflowId);
		Column yAxisColumn = new Column(yAxisLabel, yAxisAttributeId, 1, yAxisType, "Y-AXIS");
		yAxisColumn.setQuestionnaireWorkflowId(workflowId);
		ChartReport chartReport = new ChartReport();
		chartReport.setAccessType(AccessType.PRIVATE);
		chartReport.setUserId(userId);
		chartReport.setLabel("Series chart report for " + userId);
		chartReport.addColumn(xAxisColumn);
		chartReport.addColumn(yAxisColumn);
		return chartReport;
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

	public String getxAxisType() {
		return xAxisType;
	}

	public void setxAxisType(String xAxisType) {
		this.xAxisType = xAxisType;
	}

	public String getyAxisType() {
		return yAxisType;
	}

	public void setyAxisType(String yAxisType) {
		this.yAxisType = yAxisType;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	private final Long userId;

	private String xAxisAttributeId;
	private String yAxisAttributeId;
	private Dashboard dashboard;
	private String xAxisLabel;
	private String yAxisLabel;
	private String xAxisType;
	private String yAxisType;
	private Long workflowId;

}
