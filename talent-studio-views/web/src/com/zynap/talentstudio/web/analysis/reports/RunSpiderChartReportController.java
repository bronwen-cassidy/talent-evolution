/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.common.util.StringUtil;
import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.reports.ChartReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.populations.PopulationUtils;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.views.RunSpiderChartReportWrapper;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class RunSpiderChartReportController extends RunReportController {

    protected Collection getRunnableReports(RunReportWrapperBean reportWrapperBean, UserSession userSession) {
        return getReportService().findAllChartReports(ChartReport.SPIDER_CHART, userSession.getId());
    }

    @Override
    public Class getReportClass() {
        return ChartReport.class;
    }

    /**
     * Run the report.
     *
     * @param wrapper
     * @param userId
     * @param request
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    protected void runReport(RunReportWrapperBean wrapper, Long userId, HttpServletRequest request) throws TalentStudioException {
        String populationIdString = RequestUtils.getStringParameter(request, "populationIds", null);
        Long[] populationIds = StringUtil.convertToLongArray(StringUtils.commaDelimitedListToStringArray(populationIdString));
        String subjectId = RequestUtils.getStringParameter(request, ParameterConstants.NODE_ID_PARAM, null);

        Population population;
        if (subjectId != null) {
            population = PopulationUtils.createPersonPopulation(populationEngine, new String[]{subjectId});
        } else {
            population = PopulationUtils.buildPopulation(populationIds, populationIds[0], analysisService);
        }
        wrapper.setPopulation(population);
        // todo spider chart runner to run and insert the results intoo the wrapper for tag chart display
        reportRunner.run(wrapper, userId);
        ((RunSpiderChartReportWrapper) wrapper).setResultsDisplayable(true);
    }

    protected RunReportWrapperBean recoverFormBackingObject(HttpServletRequest request) {
        return (RunSpiderChartReportWrapper) HistoryHelper.recoverCommand(request, RunSpiderChartReportWrapper.class);
    }

    protected RunReportWrapperBean createFormBackingObject(Report report, Long userId) {
        return new RunSpiderChartReportWrapper(report);
    }
}