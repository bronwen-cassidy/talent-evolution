/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.metrics;

import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.web.analysis.picker.AnalysisAttributeCollection;
import com.zynap.talentstudio.web.analysis.picker.PopulationCriteriaBuilder;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MetricMultiController extends ZynapMultiActionController {

    /**
     * Returns the list of metrics viewable by the user.
     *
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView listMetrics(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        final Long userId = ZynapWebUtils.getUserId(request);
        model.put(METRICS, metricService.findAll(userId));
        return new ModelAndView(LIST_METRIC, ControllerConstants.MODEL_NAME, model);
    }

    public ModelAndView viewMetric(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long id = getMetricId(request);
        final Metric metric = (Metric) metricService.findById(id);

        AnalysisAttributeCollection collection;
        if (metric.isCount()) {
            collection = countBuilder.buildCollection();
        } else {
            collection = builder.buildCollection();
        }
        final MetricWrapper metricWrapper = new MetricWrapper(metric, collection);

        if (metric.isCount()) {
            countBuilder.setAttributeLabel(null, metric.getArtefactType(), metricWrapper);
        } else {
            builder.setAttributeLabel(null, metric.getArtefactType(), metricWrapper);
        }
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(METRIC, metricWrapper);
        if(ZynapWebUtils.getUser(request).isRoot() || ZynapWebUtils.getUserId(request).equals(metric.getUserId())) {
            model.put("editable", Boolean.TRUE);
        }
        return new ModelAndView(VIEW_METRIC, ControllerConstants.MODEL_NAME, model);
    }

    public void setMetricService(IMetricService metricService) {
        this.metricService = metricService;
    }

    public void setBuilder(PopulationCriteriaBuilder builder) {
        this.builder = builder;
    }

    public void setCountBuilder(PopulationCriteriaBuilder countBuilder) {
        this.countBuilder = countBuilder;
    }

    public final static Long getMetricId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.METRIC_ID);
    }

    private IMetricService metricService;
    private PopulationCriteriaBuilder builder;
    private PopulationCriteriaBuilder countBuilder;

    private static final String LIST_METRIC = "listmetrics";
    protected static final String VIEW_METRIC = "viewmetric";
    protected static final String CONFIRM_DELETE_METRIC_VIEW = "confirmdeletemetric";

    private static final String METRIC = "report";
    private static final String METRICS = "reports";
}
