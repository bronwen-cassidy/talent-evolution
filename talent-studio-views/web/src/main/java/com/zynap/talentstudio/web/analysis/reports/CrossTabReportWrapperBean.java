/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.CrossTabReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportDto;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.analysis.reports.crosstab.CrossTabCellInfo;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.common.Direction;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class CrossTabReportWrapperBean extends DrilldownReportWrapperBean {

    public CrossTabReportWrapperBean(Report report, Collection<MenuSection> reportMenuSections, Collection<MenuSection> homePageMenuSections, String runReportURL) {
        super(report, reportMenuSections, homePageMenuSections, runReportURL);

        // find horizontal column
        Column repHorizontalColumn = ((CrossTabReport) report).getHorizontalColumn();
        if (repHorizontalColumn == null) repHorizontalColumn = new Column();

        this.horizontalColumn = new ColumnWrapperBean(repHorizontalColumn);

        // find vertical column
        Column repVerticalColumn = ((CrossTabReport) report).getVerticalColumn();
        if (repVerticalColumn == null) repVerticalColumn = new Column();

        this.verticalColumn = new ColumnWrapperBean(repVerticalColumn);
    }

    public void setHorizontalColumn(Column column) {
        column.setPosition(new Integer(1));
        column.setColumnSource(Direction.HORIZONTAL.toString());
        this.horizontalColumn = new ColumnWrapperBean(column);
    }

    public ColumnWrapperBean getHorizontalColumn() {
        return horizontalColumn;
    }

    public void setVerticalColumn(Column column) {
        column.setPosition(new Integer(0));
        column.setColumnSource(Direction.VERTICAL.toString());
        this.verticalColumn = new ColumnWrapperBean(column);
    }

    public ColumnWrapperBean getVerticalColumn() {
        return verticalColumn;
    }

    public Report getModifiedReport() {
        checkColumnAttributes();
        List<Column> newColumns = new ArrayList<Column>();

        // check that wrapped columns are not null
        final Column newHorizontalColumn = horizontalColumn.getModifiedColumn();
        if (newHorizontalColumn != null) {
            newHorizontalColumn.setReport(report);
            newColumns.add(newHorizontalColumn);
        }

        final Column newVerticalColumn = verticalColumn.getModifiedColumn();
        if (newVerticalColumn != null) {
            newVerticalColumn.setReport(report);
            newColumns.add(newVerticalColumn);
        }

        report.assignNewColumns(newColumns);
        assignNewMenuItems(report);
        assignCellInfos(report);
        assignNewGroups(report);
        return report;
    }

    private void assignCellInfos(Report report) {

        List<CrossTabCellInfo> result = new ArrayList<CrossTabCellInfo>();
        for (Iterator<List<CrossTabCellInfo>> iterator = cellInfos.iterator(); iterator.hasNext();) {
            List<CrossTabCellInfo> crossTabCellInfos = iterator.next();
            for (Iterator<CrossTabCellInfo> crossTabCellInfoIterator = crossTabCellInfos.iterator(); crossTabCellInfoIterator.hasNext();) {
                CrossTabCellInfo info = crossTabCellInfoIterator.next();
                CrossTabCellInfo current = new CrossTabCellInfo(info.getHorizontalValueId(), info.getVerticalValueId(), info.getLabel());
                result.add(current);
            }
        }
        ((CrossTabReport) report).assignNewCellInfos(result);
    }

    public void checkColumnAttributes() {
        checkColumnAttribute(horizontalColumn);
        checkColumnAttribute(verticalColumn);
    }

    public void setMetrics(Collection<Metric> metrics) {
        this.metrics = metrics;
    }

    public Collection<Metric> getMetrics() {
        return metrics;
    }

    public boolean isShowPreferredMetric() {
        return metrics != null;
    }

    public void setPreferredMetric(Long metricId) {
        final Metric metric = (metricId != null) ? new Metric(metricId) : IPopulationEngine.COUNT_METRIC;
        report.setDefaultMetric(metric);
    }

    public Long getPreferredMetric() {
        return report.getDefaultMetric() != null ? report.getDefaultMetric().getId() : null;
    }

    public void setDisplayLimit(Integer displayLimit) {
        ((CrossTabReport) report).setDisplayLimit(displayLimit);
    }

    public Integer getDisplayLimit() {
        return ((CrossTabReport) report).getDisplayLimit();
    }

    public void setDisplayReportId(Long reportId) {

        final Report displayReport = (reportId != null) ? new TabularReport(reportId) : null;
        report.setDisplayReport(displayReport);
    }

    public Long getDisplayReportId() {
        final Report displayReport = report.getDisplayReport();
        return displayReport != null ? displayReport.getId() : null;
    }

    public Collection getDisplayReports() {
        return CollectionUtils.select(getDrillDownReports(), new Predicate() {
            public boolean evaluate(Object object) {
                ReportDto drillDownReport = (ReportDto) object;
                return drillDownReport.isTabularReport();
            }
        });
    }


    public int getResultFormat() {
        return report.getDisplayOption();
    }

    public void setResultFormat(int resultFormat) {
        report.setDisplayOption(resultFormat);
    }

    public int getDecimalPlaces() {
        return report.getDecimalPlaces();
    }

    public void setDecimalPlaces(int decimalPlaces) {
        report.setDecimalPlaces(decimalPlaces);
    }

    public boolean isHasDisplayReport() {
        return report.getDisplayReport() != null;
    }

    public void setCellInfos(List<List<CrossTabCellInfo>> cellInfos) {
        this.cellInfos = cellInfos;
    }

    public List<List<CrossTabCellInfo>> getCellInfos() {
        return cellInfos;
    }

    public List<CrossTabCellInfo> extractCrosstabCellInfos() {
        List<CrossTabCellInfo> cellInfosExtracted = new ArrayList<CrossTabCellInfo>();
        for (Iterator<List<CrossTabCellInfo>> cellInfoIterator = cellInfos.iterator(); cellInfoIterator.hasNext();) {
            List<CrossTabCellInfo> crossTabCellInfos = cellInfoIterator.next();
            cellInfosExtracted.addAll(crossTabCellInfos);
        }
        return cellInfosExtracted;
    }

    public void setVerticalHeadings(List<String> verticalHeadings) {
        this.verticalHeadings = verticalHeadings;
    }

    public List<String> getVerticalHeadings() {
        return verticalHeadings;
    }

    public void setHorizontalHeadings(List<String> horizontalHeadings) {
        this.horizontalHeadings = horizontalHeadings;
    }

    public List<String> getHorizontalHeadings() {
        return horizontalHeadings;
    }

    private ColumnWrapperBean horizontalColumn;
    private ColumnWrapperBean verticalColumn;
    private Collection<Metric> metrics;
    private List<List<CrossTabCellInfo>> cellInfos = new ArrayList<List<CrossTabCellInfo>>();
    private List<String> verticalHeadings;
    private List<String> horizontalHeadings;
}
