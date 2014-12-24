package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.ReportDto;

import java.io.Serializable;
import java.util.Collection;


/**
 * User: bcassidy
 * Date: 20-Apr-2006
 * Time: 10:50:20
 */
public final class ListReportWrapperBean implements Serializable {

    public ListReportWrapperBean(Collection<ReportDto> reports) {
        this.reports = reports;
    }

    public Collection<ReportDto> getReports() {
        return reports;
    }

    private final Collection<ReportDto> reports;
}
