/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.perfomance;

import com.zynap.talentstudio.util.IFormatter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PerformanceReviewValidator implements Validator {

    public boolean supports(Class clazz) {
        return PerformanceReviewWrapper.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.label.required", "Label is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "populationId", "error.required.field", "This is is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "managerQuestionnaireDefinitionId", "error.required.field", "This is is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "generalQuestionnaireDefinitionId", "error.required.field", "This is is a required field");

        PerformanceReviewWrapper wrapper = (PerformanceReviewWrapper) obj;
        final Date expiryDate = wrapper.getExpiryDate();
        if (expiryDate != null) {

            // compare expiry date to current date - use formatter to remove time so we only compare dates
            SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_DATE_PATTERN);
            String dateValue = formatter.format(new Date());
            Date today = null;
            try {
                today = formatter.parse(dateValue);
            } catch (ParseException e) {
                // ignored will never happen
            }

            if (expiryDate.before(today)) errors.rejectValue("expiryDate", "error.complete.date.before.today", "Cannot set the complete by date before today");
        }
    }
}
