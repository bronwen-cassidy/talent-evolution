/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.MetricReport;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.web.analysis.reports.cewolf.ChartConstants;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.ChartHelper;
import com.zynap.talentstudio.web.analysis.reports.views.RunMetricReportWrapper;
import com.zynap.talentstudio.web.analysis.reports.views.RunReportWrapperBean;
import com.zynap.talentstudio.web.common.exceptions.InvalidSubmitException;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.lang.StringUtils;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class RunMetricReportController extends RunReportController {

    protected Collection getRunnableReports(RunReportWrapperBean reportWrapperBean, UserSession userSession) {
        return getReportService().findAllMetricReports(userSession.getId());
    }

    protected RunReportWrapperBean createFormBackingObject(Report report, Long userId) {
        return new RunMetricReportWrapper(report);
    }

    protected RunReportWrapperBean recoverFormBackingObject(HttpServletRequest request) {
        return (RunMetricReportWrapper) HistoryHelper.recoverCommand(request, RunMetricReportWrapper.class);
    }

    protected void runReport(RunReportWrapperBean wrapperBean, Long userId, HttpServletRequest request) throws TalentStudioException {
        reportRunner.run(wrapperBean, userId);
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refData = super.referenceData(request, command, errors, page);
        RunMetricReportWrapper wrapper = (RunMetricReportWrapper) command;

        refData.put(CHART_TYPES, barChartTypes);
        ChartHelper chartHelper = new ChartHelper();
        switch (page) {
            case PIE_CHART_IDX:

                // get the selected column being used as pie chart - can be a metric or a function
                String columnId = request.getParameter(COL_ID_PARAM);
                if (columnId == null || !StringUtils.isNumeric(columnId)) {
                    throw new InvalidSubmitException(request.getSession(), wrapper, ZynapWebUtils.getCurrentURI(request), true, this.getClass().getName());
                }
                refData.put(ChartConstants.PRODUCER_PARAM, chartHelper.createMetricPieChartProducer(wrapper, columnId));
                refData.put(ChartConstants.LABEL_PROCESSOR, chartHelper.createPieChartLabelProcessor(wrapper));
                break;

            case BAR_CHART_OPTIONS_IDX:
                refData.put(DISPLAY_BAR_CHART_OPTIONS, Boolean.TRUE);
                break;
            case BAR_CHART_OPTIONS_SELECTED:
                wrapper.setActiveTab(CHART_TAB);
                refData.put(DISPLAY_BAR_CHART_OPTIONS, Boolean.TRUE);
                String[] parameters = (String[]) request.getParameterMap().get(SELECTED_METRICS);
                if (parameters == null || parameters.length == 0) {
                    wrapper.setSelectedMetrics(new Long[0]);
                    errors.rejectValue(SELECTED_METRICS, "error.metric.selection.invalid", "Must select metrics to view the chart");
                } else {

                    if (wrapper.getChartType().equals(ChartHelper.OVERLAID_CHART_TYPE)) {
                        refData.put(ChartConstants.PRODUCER_PARAM, chartHelper.createMetricOverlaidChartProducer(wrapper));
                    } else {

                        refData.put(ChartConstants.PRODUCER_PARAM, chartHelper.createMetricBarChartProducer(wrapper));
                        refData.put(ChartConstants.LABEL_PROCESSOR, chartHelper.createBarChartLabelProcessor(wrapper));
                        refData.put(ChartConstants.PERCENT_ITEM_PROCESSOR, chartHelper.createPercentLabelProcessor(wrapper));
                    }
                }
                break;


        }

        return refData;
    }

    @Override
    public Class getReportClass() {
        return MetricReport.class;
    }

    private static final String DISPLAY_BAR_CHART_OPTIONS = "optionsDisplayable";
    private static final String SELECTED_METRICS = "selectedMetrics";

}
