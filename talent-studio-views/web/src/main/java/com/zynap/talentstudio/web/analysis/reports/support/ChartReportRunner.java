/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports.support;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.ChartReportAttribute;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;
import com.zynap.talentstudio.web.analysis.reports.data.ChartFilledReport;
import com.zynap.talentstudio.web.analysis.reports.managers.ChartReportFiller;
import com.zynap.talentstudio.web.analysis.reports.views.RunChartReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Apr-2010 11:50:16
 */
public class ChartReportRunner extends ReportRunner {

    @Override
    public void run(RunReportWrapperBean wrapper, Long userId) throws TalentStudioException {
        ChartReport report = (ChartReport) wrapper.getReport();
        final Population population = wrapper.getPopulation();
        List<SubjectDTO> subjects = populationEngine.findSubjects(population, userId);
        List<AnalysisParameter> attributes = new ArrayList<AnalysisParameter>();

        for (ChartReportAttribute attr : report.getChartReportAttributes()) {
            attributes.add(attr.getAnalysisParameter());
        }
        final List<NodeExtendedAttribute> answers = populationEngine.findQuestionnaireAnswers(population, userId, attributes);
        final ChartFilledReport filledReport = (ChartFilledReport) reportFiller.fillReport(report, subjects, answers);

        RunChartReportWrapper chartWrapper = (RunChartReportWrapper) wrapper;
        chartWrapper.setProducer(filledReport.getProducer());
        chartWrapper.setData(filledReport.getChartDataStructure());
        chartWrapper.setChartType(report.getChartType());
    }

    public void setReportFiller(ChartReportFiller reportFiller) {
        this.reportFiller = reportFiller;
    }

    private ChartReportFiller reportFiller;
}
