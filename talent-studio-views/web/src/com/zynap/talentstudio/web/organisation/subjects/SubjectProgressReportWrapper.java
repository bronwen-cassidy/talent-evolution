/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-Feb-2011 09:28:19
 */
public class SubjectProgressReportWrapper implements Serializable {

    public void setReport(Report report) {
        this.report = report;
    }

    public Report getReport() {
        return report;
    }

    public void setFilledReport(FilledReport filledReport) {
        this.filledReport = filledReport;
    }

    public FilledReport getFilledReport() {
        return filledReport;
    }

    private Report report;
    private FilledReport filledReport;
}
