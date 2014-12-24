package com.zynap.talentstudio.web.analysis.metrics;

import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: amark
 * Date: 08-Jun-2005
 * Time: 18:11:09
 */
public class DeleteMetricController extends ZynapDefaultFormController {

    public void setMetricService(IMetricService metricService) {
        this.metricService = metricService;
    }

    /**
     * Retrieves the metric for the given id in the request.
     *
     * @param request
     * @return Object an instance of an {@link com.zynap.talentstudio.analysis.metrics.Metric} object
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {
        Long metricId = MetricMultiController.getMetricId(request);
        return metricService.findById(metricId);
    }

    /**
     * Delete the metric.
     *
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        Metric metric = (Metric) command;
        try {
            metricService.delete(metric);
            return new ModelAndView(new ZynapRedirectView(getSuccessView()));
        } catch (DataIntegrityViolationException e) {
            errors.reject("error.metric.in.use", "This metric is used in at least one report and cannot be deleted.");
            return showForm(request, errors, getFormView(), getModel(errors));
        }
    }

    /**
     * Handle the cancel.
     *
     * @param request
     * @param response
     * @param command
     * @return ModelAndView the cancel view
     */
    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) {
        Metric metric = (Metric) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.METRIC_ID, metric.getId()));
    }

    private IMetricService metricService;
}
