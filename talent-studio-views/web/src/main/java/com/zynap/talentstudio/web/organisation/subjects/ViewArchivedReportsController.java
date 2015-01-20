/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.AppraisalReportBuilder;
import com.zynap.talentstudio.analysis.reports.AppraisalSummaryReport;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.crosstab.Row;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 02-Oct-2009 12:41:54
 */
public class ViewArchivedReportsController extends DefaultWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ApprisalReportListWrapper wrapper = new ApprisalReportListWrapper();
        final Long subjectId = RequestUtils.getLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        // get the list of archived reports
        final List<Report> reports = reportService.findAppraisalReports(subjectId, AppraisalSummaryReport.STATUS_ARCHIVED);
        wrapper.setReports(reports);
        wrapper.setSubjectId(subjectId);
        // pick the first one to build
        if(!reports.isEmpty()) {
            assignReportResults(wrapper, reports.get(0));
        }

        return wrapper;
    }

    private void assignReportResults(ApprisalReportListWrapper wrapper, Report report) throws TalentStudioException {
        List<Report> one = new ArrayList<Report>();
        one.add(report);
        final Map<Report,List<Row>> results = appraisalReportBuilder.buildResults(wrapper.getSubjectId(), one);
        wrapper.setResults(results);
        wrapper.setSelectedReportId(report.getId());
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        // get the report id
        final Long reportId = RequestUtils.getLongParameter(request, ParameterConstants.REPORT_ID);
        ApprisalReportListWrapper wrapper = (ApprisalReportListWrapper) command;
        if(reportId != null) {
            final Report report = (Report) reportService.findById(reportId);
            assignReportResults(wrapper, report);
        }
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setAppraisalReportBuilder(AppraisalReportBuilder appraisalReportBuilder) {
        this.appraisalReportBuilder = appraisalReportBuilder;
    }

    private IReportService reportService;
    private AppraisalReportBuilder appraisalReportBuilder;
}
