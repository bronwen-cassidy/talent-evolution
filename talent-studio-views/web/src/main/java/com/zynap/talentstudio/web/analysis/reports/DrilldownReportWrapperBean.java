package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.ReportDto;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.arenas.MenuSection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: amark
 * Date: 27-Feb-2006
 * Time: 10:04:13
 */
public abstract class DrilldownReportWrapperBean extends ReportWrapperBean {

    public DrilldownReportWrapperBean(Report report, Collection<MenuSection> menuSections, Collection<MenuSection> homePageMenuSections, String url) {
        super(report, menuSections, homePageMenuSections, url);
    }

    public final void setDrillDownReportId(Long reportId) {

        final Report drillDownReport = (reportId != null) ? findReport(reportId) : null;
        report.setDrillDownReport(drillDownReport);

    }

    private Report findReport(Long reportId) {
        for (ReportDto drillDownReport : drillDownReports) {
            if (drillDownReport.getId().equals(reportId)) {
                return Report.create(reportId, drillDownReport.getType());
            }
        }
        return null;
    }

    public final Long getDrillDownReportId() {
        final Report drillDownReport = report.getDrillDownReport();
        return drillDownReport != null ? drillDownReport.getId() : null;
    }

    public final Collection<ReportDto> getDrillDownReports() {
        return drillDownReports;
    }

    public final void setDrillDownReports(Collection<ReportDto> drillDownReports) {
        this.drillDownReports = drillDownReports;
    }

    private Collection<ReportDto> drillDownReports = new ArrayList<ReportDto>();
}
