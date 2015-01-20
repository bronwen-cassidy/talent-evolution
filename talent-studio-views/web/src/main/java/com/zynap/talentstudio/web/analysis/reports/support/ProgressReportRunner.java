package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.reports.ProgressReport;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.analysis.reports.data.FilledReport;
import com.zynap.talentstudio.web.analysis.reports.managers.ProgressReportFiller;
import com.zynap.talentstudio.web.analysis.reports.views.RunProgressReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: bcassidy
 * Date: 01-Mar-2006
 * Time: 10:21:00
 */
public final class ProgressReportRunner extends ReportRunner {

    public void run(RunReportWrapperBean wrapper, Long userId) throws TalentStudioException {

        ProgressReport report = (ProgressReport) wrapper.getReport();
        List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
        Subject subject = ((RunProgressReportWrapper) wrapper).getNode();
        List<Long> nodeIds = new ArrayList<Long>();
        nodeIds.add(subject.getId());
        Map<Long, QuestionAttributeValuesCollection> answers = populationEngine.findQuestionnaireAnswers(questionnaireAttributes, nodeIds, userId);
        FilledReport filledReport = reportFiller.fillReport(wrapper.getReport(), subject, answers);
        wrapper.setFilledReport(filledReport);
    }

    public void setReportFiller(ProgressReportFiller reportFiller) {
        this.reportFiller = reportFiller;
    }

    private ProgressReportFiller reportFiller;
}