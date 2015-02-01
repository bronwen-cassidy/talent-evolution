/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.metrics;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.metrics.IMetricService;
import com.zynap.talentstudio.analysis.metrics.Metric;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.web.analysis.picker.PopulationCriteriaBuilder;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class BaseMetricController extends DefaultWizardFormController {

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map refData = new HashMap();
        MetricWrapper wrapper = (MetricWrapper) command;
        switch(getTargetPage(request, page)) {
            case SELECT_POPULATION:
                if(wrapper.getType() != null && StringUtils.hasText(wrapper.getOperator())) {
                    // clear any previous attribute in case this has changed
                    wrapper.setAttribute(null);
                    setAttributes(wrapper);
                } else {
                    wrapper.reset();
                }
                break;
            case PICK_ATTRIBUTE:
                wrapper.checkAttribute();
                break;
        }
        return refData;
    }

    protected void validatePage(Object command, Errors errors, int page, boolean finish) {
        if(finish) {
            Validator validator = getValidator();
            validator.validate(command, errors);
        }
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception { /*nothing to do here*/ }

    protected void setAttributes(MetricWrapper wrapper) throws TalentStudioException {
        final String operator = wrapper.getOperator();

        if (IPopulationEngine.COUNT.equals(operator)) {
            wrapper.setAttributeCollection(countBuilder.buildCollection());
        } else {
            wrapper.setAttributeCollection(builder.buildCollection());
        }

        wrapper.checkAttribute();
    }

    protected Metric getMetric(Object command) {
        return ((MetricWrapper) command).getMetric();
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

    protected IMetricService metricService;
    protected PopulationCriteriaBuilder builder;
    protected PopulationCriteriaBuilder countBuilder;

    protected static final int START_PAGE = 0;
    protected static final int SELECT_POPULATION = 1;
    private static final int PICK_ATTRIBUTE = 2;
}
