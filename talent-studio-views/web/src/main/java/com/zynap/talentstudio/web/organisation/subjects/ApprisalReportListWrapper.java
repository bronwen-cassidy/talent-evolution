/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 02-Oct-2009 13:32:14
 */
public class ApprisalReportListWrapper implements Serializable {

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setResults(Map<Report, List<Row>> results) {
        this.results = results;
    }

    public Map<Report, List<Row>> getResults() {
        return results;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public Long getSelectedReportId() {
        return selectedReportId;
    }

    public void setSelectedReportId(Long selectedReportId) {
        this.selectedReportId = selectedReportId;
    }

    private List<Report> reports;
    private Map<Report, List<Row>> results;
    private Long subjectId;
    private Long selectedReportId;
}
