/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.analysis.reports.ColumnWrapperBean;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DisplaySettingsValidator implements Validator {

    public boolean supports(Class clazz) {
        return DisplayConfigWrapper.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        DisplayConfigWrapper wrapper = (DisplayConfigWrapper) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayConfigItem.label", "error.label.required", "The display label is a required field");
        if (wrapper.isReportConfigItem()) {
            List reports = wrapper.getReports();

            for (int j = 0; j < reports.size(); j++) {
                DisplayConfigReportWrapper reportWrapper = (DisplayConfigReportWrapper) reports.get(j);
                Set attributeNames = new HashSet();
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reports[" + j + "].label", "error.label.required", "The display label is a required field");
                List columns = reportWrapper.getColumns();

                if (columns.isEmpty()) errors.reject("error.no.columns.defined", "There must be at least one value added");

                for (int i = 0; i < columns.size(); i++) {
                    ColumnWrapperBean column = (ColumnWrapperBean) columns.get(i);
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reports[" + j + "].columns[" + i + "].label", "error.label.required", "label is required");
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reports[" + j + "].columns[" + i + "].attribute", "error.attribute.required", "attribute is required");
                    attributeNames.add(column.getAttribute());
                }

                if (attributeNames.size() < columns.size()) {
                    errors.reject("error.duplicate.columns.defined", "Duplicate values cannot be used.");
                }

                if (wrapper.getDisplayConfig().isAdd()) {
                    validateRequiredAttributesPresent(wrapper, attributeNames, errors);
                }
            }
        }
    }

    private void validateRequiredAttributesPresent(DisplayConfigWrapper wrapper, Set attributeNames, Errors errors) {
        if (Node.POSITION_UNIT_TYPE_.equals(wrapper.getReportType())) {
            if (!attributeNames.containsAll(positionMandatoryAttributes)) {
                errors.reject("error.position.required.attributes.missing", "A number of required attributes of: " + positionMandatoryAttributes.toString() + " are missing");
            }
        } else if (!attributeNames.containsAll(subjectMandatoryAttributes)) {
            errors.reject("error.subject.required.attributes.missing", "A number of required attributes of: " + subjectMandatoryAttributes.toString() + " are missing");
        }
    }

    public void setSubjectMandatoryAttributes(List subjectMandatoryAttributes) {
        this.subjectMandatoryAttributes = subjectMandatoryAttributes;
    }

    public void setPositionMandatoryAttributes(List positionMandatoryAttributes) {
        this.positionMandatoryAttributes = positionMandatoryAttributes;
    }

    private List subjectMandatoryAttributes;
    private List positionMandatoryAttributes;
}
