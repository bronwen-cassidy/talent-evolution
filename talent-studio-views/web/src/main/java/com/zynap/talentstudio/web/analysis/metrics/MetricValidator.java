/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.metrics;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.AnalysisParameter;

import org.apache.commons.lang.math.NumberUtils;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class MetricValidator implements Validator {

    public boolean supports(Class clazz) {
        return (MetricWrapper.class.isAssignableFrom(clazz));
    }

    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "access", "error.scope.required", "Scope is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.label.required", "Label is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "error.artefact.type.required", "Artefact type is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "attribute", "error.attribute.required", "Attribute is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "operator", "error.operator.required", "Operator is required");
        MetricWrapper metric = (MetricWrapper) obj;
        if (IPopulationEngine.COUNT.equals(metric.getOperator())) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value", "error.value.required", "Please enter a value");
            // we need to validate number attributes to verify correct value has been entered
            AnalysisParameter analysisParameter = metric.getAnalysisParameter();
            boolean validateNumber = analysisParameter != null && (analysisParameter.isDerivedAttribute() || metric.isNumber());
            String value = metric.getValue();
            if(StringUtils.hasText(value) && validateNumber) {
                if(!NumberUtils.isNumber(value)) {
                    errors.rejectValue("value", "criteria.number.required", "Please enter a valid number");    
                }
            }
        }
    }
}
