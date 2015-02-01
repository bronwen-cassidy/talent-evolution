/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.dashboard;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.AbstractBarChartPercentLabelGenerator;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.BarChartLabelPostProcessor;
import com.zynap.talentstudio.web.analysis.reports.cewolf.processors.ChartHelper;
import com.zynap.talentstudio.web.analysis.reports.cewolf.producers.JasperBarChartProducer;
import com.zynap.talentstudio.web.analysis.reports.support.ReportRunner;
import com.zynap.talentstudio.web.analysis.reports.views.RunMetricReportWrapper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * todo needs deletion - DELETE ME - Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Feb-2009 14:15:00
 */
public class ViewHomePageDashboardChartController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // read off the metric ids (this will come from the database at a later stage)
        final String[] metricIds = RequestUtils.getStringParameters(request, "metric_id");
        int arrayLength = metricIds.length;
        Long userId = ZynapWebUtils.getUserId(request);
        Map<String, Object> viewMap = new HashMap<String, Object>();
        HomePageDashboardWrapper[] dashboardWrappers = new HomePageDashboardWrapper[arrayLength];
        ChartHelper chartHelper = new ChartHelper();
        final UserSession userSession = ZynapWebUtils.getUserSession(request);
        UserSessionFactory.setUserSession(userSession);

        for (int i = 0; i < metricIds.length; i++) {
            String reportId = metricIds[i];

            // todo [bronwen] support for crosstab reports
            Report report = (Report) reportService.findById(new Long(reportId));

            final RunMetricReportWrapper reportWrapper = new RunMetricReportWrapper(report);
            //reportWrapper.setDecimalPlaces(2);
            
            metricReportRunner.run(reportWrapper, userId);
            reportWrapper.setChartType("verticalBar3D");
            final List<AnalysisAttributeWrapperBean> columns = reportWrapper.getColumns();
            Long[] selectedMetrics = new Long[columns.size()];
            int index = 0;
            for (AnalysisAttributeWrapperBean column : columns) {
                selectedMetrics[index++] = column.getId();
            }

            reportWrapper.setSelectedMetrics(selectedMetrics);

            JasperBarChartProducer producer = (JasperBarChartProducer) chartHelper.createMetricBarChartProducer(reportWrapper);
            BarChartLabelPostProcessor labelProcessor = chartHelper.createBarChartLabelProcessor(reportWrapper);
            AbstractBarChartPercentLabelGenerator percentLabelProcessor = chartHelper.createPercentLabelProcessor(reportWrapper);

            producer.setWidth(1.7);
            producer.setDefaultHeight(300);
            producer.setDefaultWidth(400);
            dashboardWrappers[i] = new HomePageDashboardWrapper(reportWrapper, producer, labelProcessor, percentLabelProcessor);
        }

        viewMap.put("dashboardItems", dashboardWrappers);

        return new ModelAndView("dashboard", viewMap);
    }

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }

    public void setMetricReportRunner(ReportRunner reportRunner) {
        this.metricReportRunner = reportRunner;
    }


    private IReportService reportService;
    private ReportRunner metricReportRunner;
}
