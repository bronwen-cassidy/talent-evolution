/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.metrics;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
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
public class AddMetricController extends BaseMetricController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        UserSession userSession = ZynapWebUtils.getUserSession(request);
        Metric metric = new Metric();
        metric.setUserId(userSession.getId());
        return new MetricWrapper(metric);
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Metric metric = getMetric(command);
        metricService.create(metric);
        return new ModelAndView(new ZynapRedirectView(getSuccessView(), ParameterConstants.METRIC_ID, metric.getId()));
    }
}
