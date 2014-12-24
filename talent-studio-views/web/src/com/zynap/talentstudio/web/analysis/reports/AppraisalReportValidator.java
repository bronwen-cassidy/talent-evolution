/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class AppraisalReportValidator implements Validator {

    public boolean supports(Class clazz) {
        return (AppraisalReportWrapperBean.class.isAssignableFrom(clazz));
    }

    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "appraisalId", "error.appraisal.review.required");
    }

    public void validate(AppraisalReportWrapperBean obj, Errors errors, boolean completed) {
        validate(obj, errors);
        if(completed) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.required.field");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "operator", "error.required.field");
            final Report modifiedReport = obj.getModifiedReport();            

            int weightCount = 0;
            final List<Column> columns = modifiedReport.getColumns();
            for (int index = 0; index < columns.size(); index++) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "columns[" + index + "].label", "error.required.field");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "columns[" + index + "].attributeName", "error.required.field");
                final Integer weighting = columns.get(index).getWeighting();
                if (weighting != null) {
                    weightCount += weighting.intValue();
                }
            }
            if(weightCount > 0 && weightCount != 100) {
                errors.reject("error.invalid.weighting.total");
            }
        }
    }
}