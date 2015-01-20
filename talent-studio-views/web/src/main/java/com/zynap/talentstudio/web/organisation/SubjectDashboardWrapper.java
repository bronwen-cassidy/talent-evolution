/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.talentstudio.dashboard.DashboardItem;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractChartProducer;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 25-May-2010 18:13:56
 */
public class SubjectDashboardWrapper {

    public SubjectDashboardWrapper(Long dashboardItemId) {
        this.dashboardItemId = dashboardItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectDashboardWrapper that = (SubjectDashboardWrapper) o;

        if (!dashboardItemId.equals(that.dashboardItemId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return dashboardItemId.hashCode();
    }

    public void setFilledReport(FilledReport filledReport) {
        this.filledReport = filledReport;
    }

    public FilledReport getFilledReport() {
        return filledReport;
    }

    public void setDashboardItem(DashboardItem dashboardItem) {
        this.dashboardItem = dashboardItem;
    }

    public Long getDashboardItemId() {
        return dashboardItemId;
    }

    public DashboardItem getDashboardItem() {
        return dashboardItem;
    }

    public void setExpectedProducer(AbstractChartProducer expectedProducer) {
        this.expectedProducer = expectedProducer;
    }

    public AbstractChartProducer getExpectedProducer() {
        return expectedProducer;
    }

    private Long dashboardItemId;
    private FilledReport filledReport;
    private DashboardItem dashboardItem;
    private AbstractChartProducer expectedProducer;
}
