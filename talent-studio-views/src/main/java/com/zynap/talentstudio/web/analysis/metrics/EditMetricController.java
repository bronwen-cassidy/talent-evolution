/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.metrics;

import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditMetricController extends BaseMetricController {

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Metric metric = getMetric(command);
        metricService.update(metric);
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.METRIC_ID, metric.getId()));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long metrictId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.METRIC_ID);
        MetricWrapper wrapper = new MetricWrapper((Metric) metricService.findById(metrictId));
        if (AccessType.PUBLIC_ACCESS.toString().equals(wrapper.getAccess())) {
            wrapper.setScopeChangeable(!metricService.metricInPublicReport(wrapper.getMetric().getId()));
        }
        setAttributes(wrapper);
        return wrapper;
    }

    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.METRIC_ID, getMetric(command).getId()));
    }
}
